package net.cpollet.gallery.database;

import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.UUID;
import java.util.function.Function;

@Slf4j
public final class TransactionalFunction<T, R> implements Function<T, R> {
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
