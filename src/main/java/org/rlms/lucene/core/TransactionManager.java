package org.rlms.lucene.core;

import java.io.IOException;

import org.rlms.common.exception.BaseException;

public class TransactionManager {

    private LuceneManager luceneManager;

    private boolean autoCommit;

    public TransactionManager(LuceneManager luceneManager) {
        this.luceneManager = luceneManager;
    }

    public boolean isAutoCommit() {
        return autoCommit;
    }

    public void setAutoCommit(boolean autoCommit) {
        this.autoCommit = autoCommit;
    }

    public long commit() {
        try {
            return luceneManager.getIndexWriter().commit();
        } catch (IOException e) {
            throw new BaseException(e);
        }
    }

    public void rollback() {
        try {
            luceneManager.getIndexWriter().rollback();
        } catch (IOException e) {
            throw new BaseException(e);
        }
    }

}
