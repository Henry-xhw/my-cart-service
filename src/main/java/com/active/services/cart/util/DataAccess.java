package com.active.services.cart.util;

import java.sql.Connection;

import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataAccess {
    private final PlatformTransactionManager txManager;

    public void doInTx(Runnable task) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        def.setIsolationLevel(Connection.TRANSACTION_READ_UNCOMMITTED);

        TransactionStatus status = txManager.getTransaction(def);
        try {
            task.run();

            txManager.commit(status);
        } catch (Throwable ex) {
            txManager.rollback(status);
            throw new RuntimeException(ex);
        }
    };

    public void read(Runnable task) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        def.setIsolationLevel(Connection.TRANSACTION_READ_UNCOMMITTED);
        def.setReadOnly(true);

        TransactionStatus status = txManager.getTransaction(def);
        try {
            task.run();

            txManager.commit(status);
        } catch (Throwable ex) {
            txManager.rollback(status);
            throw new RuntimeException(ex);
        }
    };
}
