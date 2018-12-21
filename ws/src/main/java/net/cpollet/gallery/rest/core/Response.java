package net.cpollet.gallery.rest.core;

import com.google.common.collect.ImmutableList;
import net.cpollet.gallery.rest.core.cookie.Cookie;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class Response {
    private static final Response NO_CONTENT = new Response(204);

    private final int httpStatus;
    private final Object payload;
    private final List<Cookie> cookies;

    private Response(int httpStatus, Object payload, List<Cookie> cookies) {
        this.httpStatus = httpStatus;
        this.payload = payload;
        this.cookies = cookies;
    }

    private Response(int httpStatus, Object payload) {
        this(httpStatus, payload, Collections.emptyList());
    }

    private Response(int httpStatus) {
        this(httpStatus, null, Collections.emptyList());
    }

    public static Response ok(Object payload) {
        return new Response(200, payload);
    }

    public static Response created(Object payload) {
        return new Response(201, payload);
    }

    public static Response noContent() {
        return NO_CONTENT;
    }

    public static Response fromError(ErrorResponse errorResponse) {
        return new Response(errorResponse.getHttpStatus(), errorResponse);
    }

    public Response withCookie(Cookie cookie) {
        return new Response(
                httpStatus,
                payload,
                ImmutableList.<Cookie>builder()
                        .addAll(cookies)
                        .add(cookie)
                        .build()
        );
    }

    public Object payload() {
        return payload;
    }

    public int code() {
        return httpStatus;
    }

    public <T> List<T> mapCookies(Function<Cookie, T> mapper) {
        return cookies.stream()
                .map(mapper)
                .collect(Collectors.toList());
    }
}
