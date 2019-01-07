package net.cpollet.gallery.rest.undertow.endpoints;

import io.undertow.util.AttachmentKey;
import net.cpollet.gallery.domain.gallery.Gallery;
import net.cpollet.gallery.rest.actions.albums.CreateAlbum;
import net.cpollet.gallery.rest.core.Action;
import net.cpollet.gallery.rest.core.Response;
import net.cpollet.gallery.rest.core.TransactionalAction;
import net.cpollet.gallery.rest.core.ValidatePayload;
import net.cpollet.gallery.rest.undertow.core.Endpoint;
import net.cpollet.gallery.rest.undertow.core.HttpHeaderActionUrlTemplate;
import net.cpollet.gallery.rest.undertow.core.ValidateEndpoints;
import net.cpollet.gallery.rest.undertow.handlers.RequestWithPayloadHandler;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.function.Supplier;

public final class CreateAlbumEndpoint implements Endpoint {
    private static final AttachmentKey<String> REQUEST_PAYLOAD = AttachmentKey.create(String.class);

    private final TransactionTemplate transactionTemplate;
    private final Gallery gallery;
    private final AttachmentKey<Response> responseAttachmentKey;

    public CreateAlbumEndpoint(TransactionTemplate transactionTemplate, Gallery gallery, AttachmentKey<Response> responseAttachmentKey) {
        this.transactionTemplate = transactionTemplate;
        this.gallery = gallery;
        this.responseAttachmentKey = responseAttachmentKey;
    }

    @Override
    public void register(ValidateEndpoints handler) {
        handler.post(
                "/albums",
                new RequestWithPayloadHandler(
                        e -> new TransactionalAction(transactionTemplate, ((Supplier<Action>) () -> {
                            CreateAlbum.SerializedPayload payload = new CreateAlbum.SerializedPayload(() -> e.getAttachment(REQUEST_PAYLOAD));
                            return new ValidatePayload(
                                    new CreateAlbum(
                                            gallery,
                                            payload,
                                            new HttpHeaderActionUrlTemplate(e, "/albums/%s")
                                    ),
                                    payload
                            );
                        }).get()),
                        REQUEST_PAYLOAD,
                        responseAttachmentKey
                )
        );
    }
}
