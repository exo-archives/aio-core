package org.exoplatform.services.xml.querying;

import java.io.IOException;
import java.io.InputStream;

public interface XMLQuery {
    /**
     * Executes this query
     * For the time only one instruction will executed
     */
    void execute() throws InvalidSourceException, QueryRunTimeException;


    /**
     * Prepares this query
     */
    void prepare( Statement statement, Object resourceContext ) throws InvalidSourceException;

    /**
     * Prepares this query
     */
    void prepare( Statement statement ) throws InvalidSourceException;

    /**
     * Prepares this query
     * Statement's source ID will be ignored
     * Input for the query is result of previous execution.
     */
    void prepareNext( Statement statement ) throws InvalidSourceException;

    /**
     * Serializes (stores) querie's result into <destination> 
     */
    void serialize() throws IOException, InvalidDestinationException;

    void setDestination(String destination);

   XMLData getInput();

   void setInput(XMLData input);

   void setInputStream(InputStream inputStream) throws InvalidSourceException;

   XMLData getResult();

   void setResult(XMLData result);

}
