package org.exoplatform.services.xml.querying.impl.xtas;

/**
 * Factory prodused XTAS instructions executor
 * @version $Id: QueryProcessorFactory.java 5799 2006-05-28 17:55:42Z geaz $ 
 */
public class QueryProcessorFactory {

    private static QueryProcessorFactory instance = null;

    /** @link dependency 
     * @stereotype instantiate*/
    /*# QueryProcessor lnkQueryProcessor; */

    protected QueryProcessorFactory(){}

    public QueryProcessor getProcessor()
    {
        return new QueryProcessor();
    }

    public static QueryProcessorFactory getInstance()
    {
        if (instance == null) {
            instance = new QueryProcessorFactory();
        }
        return instance;
    }
}
