package org.exoplatform.services.xml.querying.helper;

import org.exoplatform.services.xml.querying.InvalidStatementException;
import org.exoplatform.services.xml.querying.Statement;
import org.exoplatform.services.xml.querying.XMLData;
import org.exoplatform.services.xml.querying.XMLWellFormedData;

public interface SimpleStatementHelper {
   /**
    * Append statement helper
    * @param match - XPath expression
    * @param source - XTAS resource identifier for query source 
    * @param destination - XTAS resource identifier for query destination
    * @param value - new node(set) value
    * @return Statement object
    */
   Statement append(String match, String source, String destination, XMLData value) throws InvalidStatementException;

   /**
    * Append statement helper
    * @param match - XPath expression
    * @param resource - XTAS resource identifier (will be used for both source and destination)
    * @param value - new node(set) value
    * @return Statement object
    */
   Statement append(String match, String resource, XMLData value) throws InvalidStatementException;

   /**
    * Append statement helper
    * @param match - XPath expression
    * @param value - new node(set) value
    * @return Statement object
    */
   Statement append(String match, XMLData value) throws InvalidStatementException;

   /**
    * Create Resource statement helper
    * @param resource - XTAS resource identifier
    * @param initTree - initial node
    * @return Statement object
    */
   Statement create(String resource, XMLWellFormedData initTree) throws InvalidStatementException;

   /**
    * Delete statement helper
    * @param match - XPath expression
    * @param source - XTAS resource identifier for query source 
    * @param destination - XTAS resource identifier for query destination
    * @return Statement object
    */
   Statement delete(String match, String source, String destination) throws InvalidStatementException;

   /**
    * Delete statement helper
    * @param match - XPath expression
    * @param resource - XTAS resource identifier (will be used for both source and destination)
    * @return Statement object
    */
   Statement delete(String match, String resource) throws InvalidStatementException;

   /**
    * Delete statement helper
    * @param match - XPath expression
    * @return Statement object
    */
   Statement delete(String match) throws InvalidStatementException;

   /**
    * Drop Resource statement helper
    * @param resource - XTAS resource identifier
    * @return Statement object
    */
   Statement drop(String resource) throws InvalidStatementException;

   /**
    * Select statement helper
    * @param match - XPath expression
    * @param source - XTAS resource identifier for query source 
    * @param destination - XTAS resource identifier for query destination
    * @return Statement object
    */
   Statement select(String match, String source, String destination) throws InvalidStatementException;

    /**
     * Select statement helper
     * @param match - XPath expression
     * @param resource - XTAS resource identifier (will be used for both source and destination)
     * @return Statement object
     */
   Statement select(String match, String resource) throws InvalidStatementException;

   /**
    * Select statement helper
    * @param match - XPath expression
    * @return Statement object
    */
   Statement select(String match) throws InvalidStatementException;

   /**
    * Update statement helper
    * @param match - XPath expression
    * @param source - XTAS resource identifier for query source 
    * @param destination - XTAS resource identifier for query destination
    * @param value - new node(set) value
    * @return Statement object
    */
   Statement update(String match, String source, String destination, XMLData value) throws InvalidStatementException;

   /**
    * Update statement helper
    * @param match - XPath expression
    * @param resource - XTAS resource identifier (will be used for both source and destination)
    * @param value - new node(set) value
    * @return Statement object
    */
   Statement update(String match, String resource, XMLData value) throws InvalidStatementException;

   /**
    * Update statement helper
    * @param match - XPath expression
    * @param value - new node(set) value
    * @return Statement object
    */
   Statement update(String match, XMLData value) throws InvalidStatementException;
}
