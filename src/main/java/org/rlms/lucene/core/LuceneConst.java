package org.rlms.lucene.core;

import org.rlms.common.util.PropertiesUtil;

public class LuceneConst {

    private LuceneConst() {}

    public static final String CONFIG_FILE = "lucene";

    public static final String CONFIG_INDEX_PATH = PropertiesUtil.getResourceBundle("lucene.indexPath", CONFIG_FILE);

}
