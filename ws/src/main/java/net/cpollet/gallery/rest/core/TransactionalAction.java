package net.cpollet.gallery.rest.core;

import net.cpollet.gallery.database.TransactionalFunction;
import org.springframework.transaction.support.TransactionTemplate;

public final class TransactionalAction implements Action {
    private final TransactionTemplate transactionTemplate;
    private final Action wrapped;

    public TransactionalAction(TransactionTemplate transactionTemplate, Action wrapped) {
        this.transactionTemplate = transactionTemplate;
        this.wrapped = wrapped;
    }

    @Override
    public Response execute() {
        return new TransactionalFunction<Void, Response>(
                transactionTemplate,
                v -> wrapped.execute()
        ).apply(null);
    }
}
