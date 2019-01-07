package net.cpollet.gallery.rest.undertow.endpoints;

import io.undertow.util.AttachmentKey;
import net.cpollet.gallery.domain.gallery.Gallery;
import net.cpollet.gallery.rest.actions.albums.ListAlbums;
import net.cpollet.gallery.rest.core.Action;
import net.cpollet.gallery.rest.core.Response;
import net.cpollet.gallery.rest.core.TransactionalAction;
import net.cpollet.gallery.rest.core.ValidatePayload;
import net.cpollet.gallery.rest.undertow.core.Endpoint;
import net.cpollet.gallery.rest.undertow.core.ValidateEndpoints;
import net.cpollet.gallery.rest.undertow.core.HttpHeaderActionUrlTemplate;
import net.cpollet.gallery.rest.undertow.handlers.RequestWithoutPayloadHandler;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.LinkedList;
import java.util.Optional;
import java.util.function.Supplier;

public final class GetAlbumsEndpoint implements Endpoint {
    private final TransactionTemplate transactionTemplate;
    private final Gallery gallery;
    private final AttachmentKey<Response> responseAttachmentKey;

    public GetAlbumsEndpoint(TransactionTemplate transactionTemplate, Gallery gallery, AttachmentKey<Response> responseAttachmentKey) {
        this.transactionTemplate = transactionTemplate;
        this.gallery = gallery;
        this.responseAttachmentKey = responseAttachmentKey;
    }

    @Override
    public void register(ValidateEndpoints handler) {
        handler.get(
                "/albums",
                new RequestWithoutPayloadHandler(
                        e -> new TransactionalAction(transactionTemplate, ((Supplier<Action>) () -> {
                            ListAlbums.SerializedPayload payload = new ListAlbums.SerializedPayload(
                                    Optional.ofNullable(e.getQueryParameters().get("sort")).orElse(new LinkedList<>()),
                                    Optional.ofNullable(e.getQueryParameters().get("filter")).orElse(new LinkedList<>())
                            );
                            return new ValidatePayload(
                                    new ListAlbums(
                                            gallery,
                                            payload,
                                            new HttpHeaderActionUrlTemplate(e, "/albums/%s")
                                    ),
                                    payload
                            );
                        }).get()),
                        responseAttachmentKey
                )
        );
    }
}
