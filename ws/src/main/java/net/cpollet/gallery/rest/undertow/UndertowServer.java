package net.cpollet.gallery.rest.undertow;

import io.undertow.Undertow;
import io.undertow.attribute.ExchangeAttributes;
import io.undertow.predicate.Predicates;
import io.undertow.security.api.AuthenticationMode;
import io.undertow.security.handlers.AuthenticationCallHandler;
import io.undertow.security.handlers.AuthenticationConstraintHandler;
import io.undertow.security.handlers.AuthenticationMechanismsHandler;
import io.undertow.security.handlers.SecurityInitialHandler;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import io.undertow.server.RoutingHandler;
import io.undertow.server.handlers.CanonicalPathHandler;
import io.undertow.server.handlers.ExceptionHandler;
import io.undertow.server.handlers.GracefulShutdownHandler;
import io.undertow.server.handlers.PredicateHandler;
import io.undertow.server.handlers.RequestDumpingHandler;
import io.undertow.server.handlers.SetHeaderHandler;
import io.undertow.server.handlers.accesslog.AccessLogHandler;
import io.undertow.server.handlers.encoding.ContentEncodingRepository;
import io.undertow.server.handlers.encoding.EncodingHandler;
import io.undertow.server.handlers.encoding.GzipEncodingProvider;
import io.undertow.util.AttachmentKey;
import io.undertow.util.Headers;
import lombok.extern.slf4j.Slf4j;
import net.cpollet.gallery.domain.gallery.Gallery;
import net.cpollet.gallery.rest.actions.albums.CreateAlbum;
import net.cpollet.gallery.rest.actions.albums.ListAlbums;
import net.cpollet.gallery.rest.actions.albums.ReadAlbum;
import net.cpollet.gallery.rest.actions.sessions.CreateSession;
import net.cpollet.gallery.rest.auth.Sessions;
import net.cpollet.gallery.rest.core.Action;
import net.cpollet.gallery.rest.core.ActionException;
import net.cpollet.gallery.rest.core.ErrorResponse;
import net.cpollet.gallery.rest.core.Response;
import net.cpollet.gallery.rest.core.TransactionalAction;
import net.cpollet.gallery.rest.core.ValidatePayload;
import net.cpollet.gallery.rest.undertow.auth.FailingIdentityManager;
import net.cpollet.gallery.rest.undertow.auth.UndertowUsernamePasswordSessions;
import net.cpollet.gallery.rest.undertow.core.HttpHeaderActionUrlTemplate;
import net.cpollet.gallery.rest.undertow.handlers.BaseUrlHandler;
import net.cpollet.gallery.rest.undertow.handlers.CookiesHandler;
import net.cpollet.gallery.rest.undertow.handlers.CorrelationIdHandler;
import net.cpollet.gallery.rest.undertow.handlers.SerializeHandler;
import net.cpollet.kozan.lazy.Lazy;
import org.springframework.transaction.support.TransactionTemplate;

import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.Optional;
import java.util.function.Supplier;

@Slf4j
public final class UndertowServer {
    public static final AttachmentKey<Response> RESPONSE = AttachmentKey.create(Response.class);
    private static final AttachmentKey<String> REQUEST_PAYLOAD = AttachmentKey.create(String.class);

    private static final String APPLICATION_JSON = "application/json";

    private final String httpHost;
    private final int httpPort;
    private final Lazy<GracefulShutdownHandler> handlers;

    public UndertowServer(String httpHost, int httpPort, Gallery gallery, Sessions sessions, TransactionTemplate transactionTemplate) {
        this.httpHost = httpHost;
        this.httpPort = httpPort;
        this.handlers = new Lazy<>(() -> {
            UndertowUsernamePasswordSessions authentication = new UndertowUsernamePasswordSessions(
                    gallery.logins(),
                    sessions
            );
            return gracefulShutdown(
                    log(
                            configureHeaders(
                                    sanitizePath(
                                            authenticate(
                                                    authentication,
                                                    encode(serialize(configureCookies(
                                                            handleExceptions(
                                                                    filterFormat(writeHeaders(
                                                                            route(
                                                                                    authentication,
                                                                                    gallery,
                                                                                    transactionTemplate
                                                                            )
                                                                    ))

                                                            )
                                                    )))
                                            )
                                    )
                            )
                    )
            );
        });
    }

    public void listen() {
        Undertow.builder()
                .addHttpListener(httpPort, httpHost)
                .setHandler(handlers.value())
                .build()
                .start();

        log.info("HTTP server listening on {}, port {}", httpHost, httpPort);
    }

    public void shutdown() throws InterruptedException {
        handlers.value().shutdown();
        handlers.value().awaitShutdown();
    }

    private GracefulShutdownHandler gracefulShutdown(HttpHandler wrapped) {
        return new GracefulShutdownHandler(
                wrapped
        );
    }

    private HttpHandler log(HttpHandler wrapped) {
        return logAccess(
                new RequestDumpingHandler(
                        wrapped
                )
        );
    }

    private HttpHandler logAccess(HttpHandler wrapped) {
        return new AccessLogHandler(
                wrapped,
                log::info,
                "http: %h %l %u \"%r\" %s %b \"%{i,Referer}\" \"%{i,User-Agent}\"",
                UndertowServer.class.getClassLoader()
        );
    }

    private HttpHandler configureHeaders(HttpHandler wrapped) {
        return new CorrelationIdHandler(
                new BaseUrlHandler(
                        wrapped
                )
        );
    }

    private HttpHandler sanitizePath(HttpHandler wrapped) {
        return new CanonicalPathHandler(wrapped);
    }

    private HttpHandler authenticate(UndertowUsernamePasswordSessions usernamePasswordSessions, HttpHandler wrapped) {
        return new SecurityInitialHandler(
                AuthenticationMode.PRO_ACTIVE,
                new FailingIdentityManager(),
                new AuthenticationMechanismsHandler(
                        new AuthenticationConstraintHandler(
                                new AuthenticationCallHandler(
                                        wrapped
                                )
                        ) {
                            @Override
                            protected boolean isAuthenticationRequired(HttpServerExchange exchange) {
                                return !("/sessions".equals(exchange.getRequestPath()) && exchange.getRequestMethod().equalToString("POST"));
                            }
                        },
                        usernamePasswordSessions.authenticationMechanisms()
                )
        );
    }

    private HttpHandler encode(HttpHandler wrapped) {
        return new EncodingHandler(
                wrapped,
                new ContentEncodingRepository().addEncodingHandler(
                        "gzip",
                        new GzipEncodingProvider(),
                        50,
                        Predicates.parse("max-content-size(5)")
                )
        );
    }

    private HttpHandler serialize(HttpHandler wrapped) {
        return new SerializeHandler(wrapped);
    }

    private HttpHandler configureCookies(HttpHandler wrapped) {
        return new CookiesHandler(
                wrapped
        );
    }

    private HttpHandler handleExceptions(HttpHandler wrapped) {
        return new ExceptionHandler(wrapped)
                .addExceptionHandler(
                        ActionException.class,
                        exchange -> handle(exchange, () -> {
                            ActionException exception = (ActionException) exchange.getAttachment(ExceptionHandler.THROWABLE);

                            return Response.fromError(
                                    new ErrorResponse(
                                            exception.getHttpStatus(),
                                            exception.getMessage(),
                                            exception.getCode(),
                                            exchange.getResponseHeaders().get(CorrelationIdHandler.CORRELATION_ID_HEADER, 0),
                                            exception.getDetails()
                                    )
                            );
                        })
                )
                .addExceptionHandler(
                        Throwable.class,
                        exchange -> handle(exchange, () -> {
                            Throwable exception = exchange.getAttachment(ExceptionHandler.THROWABLE);
                            log.error("{}", exception.getMessage(), exception);

                            return Response.fromError(
                                    new ErrorResponse(
                                            500,
                                            "Internal Server Error",
                                            exchange.getResponseHeaders().get(CorrelationIdHandler.CORRELATION_ID_HEADER, 0)
                                    )
                            );
                        })
                );
    }

    private HttpHandler filterFormat(HttpHandler wrapped) {
        return filterAccept(
                filterContentType(
                        wrapped
                )
        );
    }

    private HttpHandler filterAccept(HttpHandler wrapped) {
        return new PredicateHandler(
                Predicates.contains(ExchangeAttributes.requestHeader(Headers.ACCEPT), APPLICATION_JSON),
                wrapped,
                exchange -> {
                    throw ActionException.NOT_ACCEPTABLE;
                }
        );
    }

    private HttpHandler filterContentType(HttpHandler wrapped) {
        return new PredicateHandler(
                Predicates.contains(ExchangeAttributes.requestHeader(Headers.CONTENT_TYPE), APPLICATION_JSON),
                wrapped,
                exchange -> {
                    throw ActionException.NOT_ACCEPTABLE;
                }
        );
    }

    private HttpHandler writeHeaders(HttpHandler wrapped) {
        return new SetHeaderHandler(
                wrapped,
                "Content-Type", APPLICATION_JSON
        );
    }

    private RoutingHandler route(UndertowUsernamePasswordSessions usernamePasswordSessions, Gallery gallery, TransactionTemplate transactionTemplate) {
        return new RoutingHandler()
                .get("/500", e -> {
                    throw new Exception("error");
                })
                .get("/400", e -> {
                    throw new ActionException("message", "0400", 400);
                })
                .get("/204", e -> {
                })
                .post("/sessions", e -> handleWithPayload(e,
                        new TransactionalAction(transactionTemplate, action(() -> {
                            CreateSession.SerializedPayload payload = new CreateSession.SerializedPayload(() -> e.getAttachment(REQUEST_PAYLOAD));
                            return new ValidatePayload(
                                    new CreateSession(
                                            usernamePasswordSessions,
                                            new UndertowUsernamePasswordSessions.UndertowNotifier(e.getSecurityContext()),
                                            payload,
                                            new HttpHeaderActionUrlTemplate(e, "/sessions/%s")
                                    ),
                                    payload
                            );
                        })))
                )
                .get("/albums", e -> handle(e,
                        new TransactionalAction(transactionTemplate, action(() -> {
                            ListAlbums.SerializedPayload payload = new ListAlbums.SerializedPayload(
                                    Optional.ofNullable(e.getQueryParameters().get("sort"))
                                            .orElse(new LinkedList<>()),
                                    Optional.ofNullable(e.getQueryParameters().get("filter"))
                                            .orElse(new LinkedList<>())
                            );
                            return new ValidatePayload(
                                    new ListAlbums(
                                            gallery,
                                            payload,
                                            new HttpHeaderActionUrlTemplate(e, "/albums/%s")
                                    ),
                                    payload
                            );
                        }))))
                .post("/albums", e -> handleWithPayload(e,
                        new TransactionalAction(transactionTemplate, action(() -> {
                            CreateAlbum.SerializedPayload payload = new CreateAlbum.SerializedPayload(() -> e.getAttachment(REQUEST_PAYLOAD));
                            return new ValidatePayload(
                                    new CreateAlbum(
                                            gallery,
                                            payload,
                                            new HttpHeaderActionUrlTemplate(e, "/albums/%s")
                                    ),
                                    payload);
                        })))
                )
                .get("/albums/{id}", e -> handle(e,
                        new TransactionalAction(transactionTemplate,
                                new ReadAlbum(
                                        gallery,
                                        new ReadAlbum.SerializedPayload(e.getQueryParameters().get("id").getFirst()),
                                        new HttpHeaderActionUrlTemplate(e, "/albums/%s")
                                )
                        )
                ))
                .setInvalidMethodHandler(exchange -> {
                    throw ActionException.METHOD_NOT_ALLOWED;
                })
                .setFallbackHandler(exchange -> {
                    throw ActionException.NOT_FOUND;
                });
    }

    private void handleWithPayload(HttpServerExchange exchange, Action action) {
        exchange.getRequestReceiver().receiveFullBytes((exchange2, data) -> {
            exchange2.putAttachment(REQUEST_PAYLOAD, new String(data, 0, data.length, StandardCharsets.UTF_8));
            handle(exchange2, action);
        });
    }

    private Action action(Supplier<Action> supplier) {
        return supplier.get();
    }

    private void handle(HttpServerExchange exchange, Action action) {
        exchange.putAttachment(
                RESPONSE,
                action.execute()
        );
    }
}
