package org.exoplatform.services.xml.querying.impl.xtas;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerException;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.stream.StreamResult;
import org.exoplatform.services.xml.querying.InvalidStatementException;


/**
 * Prepares query's instruction part of statement
 * for executing
 *  (TrAX based transformation)
 * @version $Id: InstructionCompiler.java 5799 2006-05-28 17:55:42Z geaz $
 */
public class InstructionCompiler {

    protected Transformer queryResolver;

    public InstructionCompiler(InputStream xslStream) throws InstructionCompilerException 
    {

        try {

            TransformerFactory tFactory = TransformerFactory.newInstance();
            queryResolver = tFactory.newTransformer( new StreamSource( xslStream ) );

        } catch (Exception e) {

            throw new InstructionCompilerException("Can not instantiate an InstructionCompiler Reason: " + e);
        }

    }

    /**
     * Compiles instruction and returns it as Command object
     *  
     */
    public Command compile( String instruction ) throws InvalidStatementException
    {
        try {

            ByteArrayOutputStream os = new ByteArrayOutputStream();

            StreamResult strRes = new StreamResult( os );

            queryResolver.transform(new StreamSource(
                 new ByteArrayInputStream(instruction.getBytes()) ), strRes);

            return new Command( os.toByteArray() );

        } catch (TransformerException e) {

            throw new InvalidStatementException("Can not init XSLTStatement Reason: " + e);

        }
    }
}
