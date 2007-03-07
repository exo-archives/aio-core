/**
 **************************************************************************
 * Copyright 2001-2005 The eXo Platform SARL         All rights reserved. *
 * Please look at license.txt in info directory for more license detail.  *
 **************************************************************************
 */

package org.exoplatform.services.xml.transform.impl.html;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import javax.xml.transform.Source;
import javax.xml.transform.TransformerException;
import org.exoplatform.services.xml.transform.impl.EncodingMapImpl;
import org.exoplatform.services.xml.transform.EncodingMap;
import javax.xml.transform.stream.StreamResult;
import org.w3c.tidy.Tidy;

import org.exoplatform.services.xml.transform.NotSupportedIOTypeException;
import org.exoplatform.services.xml.transform.html.HTMLTransformer;
import org.exoplatform.services.xml.transform.impl.TransformerBase;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;


/**
 * Created by The eXo Platform SARL        .
 *
 * Tidying incoming HTML to XHTML result
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @author <a href="mailto:alex.kravchuk@gmail.com">Alexander Kravchuk</a>
 * @version $Id: TidyTransformerImpl.java 5799 2006-05-28 17:55:42Z geaz $
 */
public class TidyTransformerImpl extends TransformerBase implements
        HTMLTransformer {
    protected Tidy tidy;
    protected Properties props;

    public TidyTransformerImpl() {
        super();
        tidy = new Tidy();
        initProps();
    }

    public Properties getOutputProperties() {
        return this.props;
    }

    /**
     * Sets properties for Tidy parser
     * See Tidy properties
     */
    public void setOutputProperties(Properties props) {
        this.props = props;
//        tidy.setConfigurationFromProps(props);
    }

    private void initProps() {
        this.props = new Properties();

        props.setProperty("quiet", "true");
        props.setProperty("quote-ampersand", "true");
        props.setProperty("output-xhtml", "true");
        props.setProperty("show-warnings", "false");
        props.setProperty("clean", "true");

        props.setProperty("add-xml-decl", "true");
        props.setProperty("char-encoding", "raw");//
        props.setProperty("doctype", "omit");
        props.setProperty("tidy-mark", "no");
    }

    public void processNotNativeResult(ByteArrayInputStream byteInputStream) throws
            TransformerException {

//        ByteArrayInputStream byteInputStream =
//                new ByteArrayInputStream(output.toByteArray());
//
        transformInputStream2Result(byteInputStream, getResult());
        log.debug("Transform from temp output to "+
                  getResult().getClass().getName()+" complete");
    }

    protected void internalTransform(Source source) throws
            NotSupportedIOTypeException,
            TransformerException, IllegalStateException {
        InputStream input = sourceAsInputStream(source);

        try {
            log.debug(" input available bytes " + input.available());
            if (input.available() == 0)
                return;
        } catch (IOException ex) {
            log.error("Error on read Source", ex);
            new TransformerException("Error on read source",ex);
        }


        //to del begin
//          ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
//          transformInputStream2Result(input,new StreamResult(byteOutput));
//          input = new ByteArrayInputStream(byteOutput.toByteArray());
//          writeTofile(byteOutput.toByteArray(),"tidy_input");
        //to del end

        //OutputStream  output = null;
        tidy.setConfigurationFromProps(props);

        if (getResult() instanceof StreamResult) {
            OutputStream output = ((StreamResult) getResult()).getOutputStream();
            log.debug("Prepare to write transform result direct to OutputStream");
            tidy.parse(input, output);
            log.debug("Tidy parse is complete");
        } else {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            log.debug("Prepare to write transform result to temp output");
            tidy.parse(input, output);
            log.debug("Tidy parse is complete");
            //sex with coding
            String outputString = output.toString();
            outputString = outputString.replaceFirst("<\\?xml version=\"1.0\"\\?>",
                    "<?xml version=\"1.0\" encoding=\""+getCurrentIANAEncoding()+"\"?>");
            try {
                output.flush();
            } catch (IOException ex) {
                throw new TransformerException(ex);
            }
            processNotNativeResult( new ByteArrayInputStream(outputString.getBytes()));
        }

    }
    protected String getCurrentIANAEncoding(){
        EncodingMap encodingMap = new EncodingMapImpl();
        return encodingMap.convertJava2IANA(System.getProperty("file.encoding"));

    }
}
