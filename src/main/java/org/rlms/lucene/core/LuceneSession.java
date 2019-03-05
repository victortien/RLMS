package org.rlms.lucene.core;

import org.rlms.lucene.services.LuceneService;

public class LuceneSession {

    private String indexPath;

    private boolean autoCommit;

    private LuceneManager luceneManager;

    private LuceneService luceneService;

    private LuceneSession(String indexPath) {
        this.indexPath = indexPath;
        this.autoCommit = true;
    }

    public static LuceneSession link(String indexPath) {
        return new LuceneSession(indexPath);
    }

    public LuceneSession autoCommit(boolean autoCommit) {
        this.autoCommit = autoCommit;
        return this;
    }

    public LuceneSession createSession() {
        LuceneConnector connector = new LuceneConnector(indexPath.trim());
        this.luceneManager = LuceneManager.createManager(connector);
        this.luceneManager.getTransactionManager().setAutoCommit(autoCommit);
        this.luceneService = new LuceneService(this);
        return this;
    }

    public LuceneManager getLuceneManager() {
        return luceneManager;
    }

    public LuceneService getLuceneService() {
        return luceneService;
    }

    public TransactionManager getTransactionManager() {
        return luceneManager.getTransactionManager();
    }

    public String getIndexPath() {
        return indexPath;
    }

    public boolean isAutoCommit() {
        return autoCommit;
    }

    public void close() {
        luceneManager.close();
    }
}
