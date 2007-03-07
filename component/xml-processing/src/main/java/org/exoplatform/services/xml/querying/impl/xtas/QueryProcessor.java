package org.exoplatform.services.xml.querying.impl.xtas;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.stream.StreamResult;
import org.exoplatform.services.xml.querying.InvalidStatementException;
import org.exoplatform.services.xml.querying.QueryRunTimeException;

/**
 * XTAS instructions processor
 * 
 * @version $Id: QueryProcessor.java 5799 2006-05-28 17:55:42Z geaz $ 
 */

public class QueryProcessor {

    private static TransformerFactory tFactory = TransformerFactory.newInstance();
    private Transformer transformer;

    protected UniFormTree process(Command command, UniFormTree input)  throws QueryRunTimeException
    {
       if( transformer == null )
           throw new QueryRunTimeException("QueryProcessor.process(): Query Run Time Exception. Transformer can not be NULL. Set init first! ");
    
       try {

           UniFormTreeFragment fragment = new UniFormTreeFragment ();

           if(!input.isEmpty()) {
           // Transform
           ByteArrayOutputStream os = new ByteArrayOutputStream();

           transformer.transform( new StreamSource(input.getAsInputStream()), new StreamResult( os ) );

           fragment.init( new ByteArrayInputStream (os.toByteArray()) );


//           S ystem.out.println("IS EMPTY = "+fragment.isEmpty());
           }
           return fragment;

        // TransformerException, TransformerConfigurationException
       } catch (Exception e) {

            throw new QueryRunTimeException("QueryProcessor.process(): Query Run Time Exception: " + e);
       }

    }

    public void init(Command command) throws InvalidStatementException
    {
       try {

           transformer = tFactory.newTransformer(new StreamSource(command.getAsInputStream()));
//           transformer.setParameter("sourceId", "test"); 
//           transformer.setParameter("genarate-info", "0"); 


       } catch (Exception e) {

            throw new InvalidStatementException("QueryProcessor.init(): failed Reason: " + e);
       }

    }

    public void setParameter(String name, String value) throws InvalidStatementException
    {
      try {

          transformer.setParameter(name, value); 

       } catch (Exception e) {

            throw new InvalidStatementException("QueryProcessor.init(): failed Reason: " + e);
       }

    }

}
