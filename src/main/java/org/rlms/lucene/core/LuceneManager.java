package org.rlms.lucene.core;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.rlms.common.exception.BaseException;
import org.rlms.common.util.ObjectUtil;
import org.rlms.common.util.StreamUtil;

public class LuceneManager {

    private LuceneConnector connector;

    private TransactionManager transactionManager;

    private IndexWriter indexWriter;

    private IndexSearcher indexSearcher;

    private LuceneManager(LuceneConnector connector) {
        try {
            this.connector = connector.connect();
            this.transactionManager = new TransactionManager(this);
        } catch (IOException e) {
            throw new BaseException(e);
        }
    }

    public static LuceneManager createManager(LuceneConnector connector) {
        return new LuceneManager(connector);
    }

    private IndexWriter initIndexWriter() throws IOException {
        Directory indexDirectory = connector.getDir();
        IndexWriterConfig conf = new IndexWriterConfig(new StandardAnalyzer());
        conf.setOpenMode(OpenMode.CREATE_OR_APPEND);
        return new IndexWriter(indexDirectory, conf);
    }

    public void create(Document... documents) {
        try {
            for(Document doc : documents) {
                getIndexWriter().addDocument(doc);
            }
            if(autoCommit())
                getIndexWriter().close();
        } catch (IOException e) {
            throw new BaseException(e);
        }
    }

    public void update(Term term, Document document) {
        try {
            getIndexWriter().updateDocument(term, document);
            if(autoCommit())
                getIndexWriter().close();
        } catch (IOException e) {
            throw new BaseException(e);
        }
    }

    public void delete(Query... queries) {
        try {
            getIndexWriter().deleteDocuments(queries);
            if(autoCommit())
                getIndexWriter().close();
        } catch (IOException e) {
            throw new BaseException(e);
        }
    }

    public void delete(Term... terms) {
        try {
            getIndexWriter().deleteDocuments(terms);
            if(autoCommit())
                getIndexWriter().close();
        } catch (IOException e) {
            throw new BaseException(e);
        }
    }

    public List<Document> search(Query query) {
        try {
            TopDocs hits = getIndexSearcher().search(query, Integer.MAX_VALUE);

            List<Document> ret = new ArrayList<>();
            for(ScoreDoc scoreDoc : hits.scoreDocs) {
                Document doc = getIndexSearcher().doc(scoreDoc.doc);
                ret.add(doc);
            }
            if(autoCommit())
                getIndexSearcher().getIndexReader().close();
            return ret;
        } catch (IOException e) {
            throw new BaseException(e);
        }
    }

    protected IndexWriter getIndexWriter() throws IOException {
        if(ObjectUtil.isEmpty(indexWriter))
            indexWriter = initIndexWriter();
        return indexWriter;
    }

    protected IndexSearcher getIndexSearcher() throws IOException {
        if(ObjectUtil.isEmpty(indexSearcher))
            indexSearcher = new IndexSearcher(DirectoryReader.open(connector.getDir()));
        return indexSearcher;
    }

    public boolean isSegmentsExist() throws IOException {
        String[] files = connector.getDir().listAll();
        return StreamUtil.ofNullable(files).anyMatch(f -> f.startsWith("segments"));
    }

    public TransactionManager getTransactionManager() {
        return transactionManager;
    }

    private boolean autoCommit() {
        return transactionManager.isAutoCommit();
    }

    public void close() {
        try {
            if(ObjectUtil.isNotEmpty(indexWriter))
                indexWriter.close();
            if(ObjectUtil.isNotEmpty(indexSearcher))
                indexSearcher.getIndexReader().close();
        } catch (IOException e) {
            throw new BaseException(e);
        }
    }
}
