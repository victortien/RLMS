package org.rlms.lucene.core;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class LuceneConnector {

    private String indexPath;

    private Directory dir;

    public LuceneConnector(String indexPath) {
        this.indexPath = indexPath;
    }

    public LuceneConnector connect() throws IOException {
        this.dir = FSDirectory.open(Paths.get(indexPath));
        return this;
    }

    public Directory getDir() {
        return dir;
    }

    public void closeDir() throws IOException {
        if(dir != null) {
            dir.close();
        }
    }
}
