package org.exoplatform.services.xml.querying.impl.xtas;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.ByteArrayInputStream;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamSource;
import org.exoplatform.services.xml.querying.XMLData;

/**
 * Encapsulates multi-formatting tree-like (XML) object presentation
 * for XTAS' manipulation purposes 
 */
abstract public class UniFormTree implements XMLData {
  
  protected ByteArrayOutputStream thisStream;
  protected Transformer transformer;
  protected static TransformerFactory tFactory;
  
  static {
    
    try {
      
      tFactory = TransformerFactory.newInstance();
      // TransformerException, TransformerConfigurationException
    } catch (Exception e) {
      // @todo Log system!
//      System .out.println("Can not INSTANTIATE UniFormTree Object Reason: " + e);
    }
    
  }
  
  /**
   * Default constructor 
   */
  public UniFormTree() 
  {
    thisStream = new ByteArrayOutputStream();
    try {
      
      transformer = tFactory.newTransformer();
      transformer.setOutputProperty(
          javax.xml.transform.OutputKeys.OMIT_XML_DECLARATION, "yes");
      
      //FactoryConfigurationError,SAXException
    } catch (Exception e) {
      
      // @todo Log system!
//      System .out.println("UniFormTree(): Can not create an Instance Reason: " + e);
      //            throw new UniFormTransformationException("Can not parse InputStream Reason: " + e);
    }
    
  }
  
  /**
   * Must be realized in concrete subclass
   */
  public abstract byte[] getAsByteArray();
  
  
  public InputStream getAsInputStream()
  {
    return new ByteArrayInputStream ( getAsByteArray() );
  }
  
  public String getAsString()
  {
    return new String (getAsByteArray());
  }
  
  /**
   * Is this object empty?
   * **/
  public boolean isEmpty()
  {
    return (thisStream.size() == 0);
  }
  
  /**
   * Cleans this object 
   */
  public void close()
  {
    thisStream = new ByteArrayOutputStream();
  }
  
  public String toString()
  {
    if (isEmpty())
      return "";
    return getAsString().trim();
  }
  
  protected void convert(Result result)
  {
    
    try {
      
      convert(result, false);
      
    } catch (Exception e) { }
    
  }
  
  
  /**
   * The main method for object transformation
   * to some output format (for getAs... methods) . 
   */
  protected void convert(Result result, boolean withException) throws Exception
  {
    
    //         if( thisStream.toByteArray().length == 0 ) 
    //             System .out.println("UniFormTree.convert() MUST BE INITIALIZED: EMPTY stream:"+ result.getClass().getName()+
    //                                 " Subclass: "+getClass().getName() );
    
    
    try {
      
      transformer.transform( new StreamSource( new ByteArrayInputStream( 
          thisStream.toByteArray() ) ), result  );
      // TransformerException, TransformerConfigurationException
    } catch (Exception e) { 
      if(withException)  throw e;
      // @todo Log system!
//      System .out.println("UniFormTree.convert(): Can not convert to "+ result.getClass().getName()+" Reason: " + e +
//          " XML: <"+new String(thisStream.toByteArray())+"> Subclass: "+getClass().getName() );
      e.printStackTrace();
    } 
  }  
}
