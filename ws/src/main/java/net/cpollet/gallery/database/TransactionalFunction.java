package net.cpollet.gallery.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.UUID;
import java.util.function.Function;

public final class TransactionalFunction<T, R> implements Function<T, R> {
    private static final Logger log = LoggerFactory.getLogger(TransactionalFunction.class);

    private final TransactionTemplate transactionTemplate;
    private final Function<T, R> wrapped;

    public TransactionalFunction(TransactionTemplate transactionTemplate, Function<T, R> wrapped) {
        this.transactionTemplate = transactionTemplate;
        this.wrapped = wrapped;
    }

    @Override
    public R apply(T t) {
        String marker = UUID.randomUUID().toString();
        try {
            log.debug("Transaction {} begins", marker);
            return transactionTemplate.execute(status -> wrapped.apply(t));
        } finally {
            log.debug("Transaction {} ends", marker);
        }
    }
}
