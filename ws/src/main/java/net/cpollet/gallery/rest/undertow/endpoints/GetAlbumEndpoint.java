package net.cpollet.gallery.rest.undertow.endpoints;

import io.undertow.util.AttachmentKey;
import net.cpollet.gallery.domain.gallery.Gallery;
import net.cpollet.gallery.rest.actions.albums.ReadAlbum;
import net.cpollet.gallery.rest.core.Response;
import net.cpollet.gallery.rest.core.TransactionalAction;
import net.cpollet.gallery.rest.undertow.core.Endpoint;
import net.cpollet.gallery.rest.undertow.core.ValidateEndpoints;
import net.cpollet.gallery.rest.undertow.core.HttpHeaderActionUrlTemplate;
import net.cpollet.gallery.rest.undertow.handlers.RequestWithoutPayloadHandler;
import org.springframework.transaction.support.TransactionTemplate;

public final class GetAlbumEndpoint implements Endpoint {
    private final TransactionTemplate transactionTemplate;
    private final Gallery gallery;
    private final AttachmentKey<Response> responseAttachmentKey;

    public GetAlbumEndpoint(TransactionTemplate transactionTemplate, Gallery gallery, AttachmentKey<Response> responseAttachmentKey) {
        this.transactionTemplate = transactionTemplate;
        this.gallery = gallery;
        this.responseAttachmentKey = responseAttachmentKey;
    }

    @Override
    public void register(ValidateEndpoints handler) {
        handler.get(
                "/albums/{id}",
                new RequestWithoutPayloadHandler(
                        e -> new TransactionalAction(transactionTemplate,
                                new ReadAlbum(
                                        gallery,
                                        new ReadAlbum.SerializedPayload(e.getQueryParameters().get("id").getFirst()),
                                        new HttpHeaderActionUrlTemplate(e, "/albums/%s")
                                )
                        ),
                        responseAttachmentKey
                )
        );
    }
}
