package org.exoplatform.services.xml.querying.impl.xtas.xml;

import org.xml.sax.SAXException;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.ext.LexicalHandler;

/**
 * Lexical handler for the SAXTransformer used in the UniFormTree
 * @version $Id: DtdTracer.java 5799 2006-05-28 17:55:42Z geaz $ 
 */
public class DtdTracer
      implements LexicalHandler,DeclHandler
{

   private String name;
   private String publicId;
   private String systemId;

   private String text="";
   private boolean isInternal = false;

    //
    // LexicalHandler methods
    //

    public DtdTracer()
    {
       name=null;
       publicId=null;
       systemId=null;

       text="";
       isInternal = false;

    }

    /** Start DTD. */
    public void startDTD(String name, String publicId, String systemId)
        throws SAXException {

        //<!DOCTYPE [name] SYSTEM "[systemId]" PUBLIC "[publicId]">
        //<!DOCTYPE employees [
        this.name = name;
        this.systemId = systemId;
        this.publicId = publicId; 


        if (systemId == null)
//          throw new SAXException("Only External DTD is Supported yet!");
          isInternal = true;
        else {
          isInternal = false;
          text = "<!DOCTYPE "+name+" SYSTEM \""+systemId+"\">";
        }
       

    } 

    /** Start entity. */
    public void startEntity(String name) throws SAXException 
    { } 

    /** Start CDATA section. */
    public void startCDATA() throws SAXException 
    { } 

    /** End CDATA section. */
    public void endCDATA() throws SAXException 
    { } 

    /** Comment. */
    public void comment(char[] ch, int offset, int length) throws SAXException {  } 

    /** End entity. */
    public void endEntity(String name) throws SAXException 
    { } 

    /** End DTD. */
    public void endDTD() throws SAXException {
       if ( isInternal )
         text+="]>";
    } 

    //
    // DeclHandler methods
    //

    /** Element declaration. */
    public void elementDecl(String name, String contentModel)
        throws SAXException {

       //<!ELEMENT employee (firstname, lastname, telephone, address?)>
       if( isInternal )
         text+="<!ELEMENT "+name+"("+contentModel+")>\n";
    } 

    /** Attribute declaration. */
    public void attributeDecl(String elementName, String attributeName,
                              String type, String valueDefault,
                              String value) throws SAXException {

        //<!ATTLIST employee id ID #REQUIRED>
       if( isInternal )
          text+="<!ATTLIST "+elementName+" "+attributeName+" "+
                      type+" "+valueDefault+" "+value+">\n";


    } 

    /** Internal entity declaration. */
    public void internalEntityDecl(String name, String text)
        throws SAXException {

        // <!ENTITY PICK5 "0">
       if( isInternal )
          text+="<!ENTITY "+name+"\""+text+"\">\n";

    } 

    /** External entity declaration. */
    public void externalEntityDecl(String name,
                                   String publicId, String systemId)
        throws SAXException {

        //<!ENTITY ttt SYSTEM "http://www.ttt/ttt.xml" PUBLIC "tttt">
        String publicStr=(publicId==null)?"":" PUBLIC "+"\""+publicId+"\"";
        String systemStr=(systemId==null)?"":" SYSTEM "+"\""+systemId+"\"";

       if( isInternal )
          text+="<!ENTITY "+name+"\""+name+"\">"+systemStr+publicStr+"\n";

    } 

    public String getSystemId() 
    {
       return systemId;
    }

    public String getPublicId() 
    {
       return publicId;
    }

    public String getText() 
    {
       return text;
    }

}