package org.exoplatform.services.xml.querying.impl.xtas;

import org.xml.sax.ContentHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.Locator;

public class XTASMLContentHandler implements ContentHandler
{
  private static final int ROOT = 0;
  private static final int QUERY = 1;
  private static final int INSTRUCTION = 2;

  private int state = ROOT;

  private boolean hasPrefixes = false;

  private String sourceId;
  private String destinationId;

  private String match;
  private String type;
  private String newValue = "";


  public XTASMLContentHandler ()
  {
//     this.statement = statement;
  }

  public void setDocumentLocator (Locator locator)
  {
  }

  public void startDocument ()
    throws SAXException
  {
  }

  public void endDocument()
    throws SAXException
  {
  }

  public void startPrefixMapping (String prefix, String uri)
    throws SAXException
  {
//     if (!prefix.equals("")    
      this.hasPrefixes = true; 
  }


  public void endPrefixMapping (String prefix)
    throws SAXException
  {
  }

  public void startElement (String namespaceURI, String localName,
                            String qName, Attributes atts)
    throws SAXException
  {
//    if (!namespaceURI.equals(""))
//      S ystem.out.println("Element: "+namespaceURI+" "+qName+" "+localName);

    if( state == ROOT ) {
        if( localName.equals("query") ) {

            int n = atts.getLength();
            // source and destination
            for(int i = 0; i < n; i++) {
               if( atts.getLocalName(i).equals("source") )
                  sourceId = atts.getValue(i);
               else if( atts.getLocalName(i).equals("destination") )
                  destinationId = atts.getValue(i);
               else
               if(!hasPrefixes)
                  throw new SAXException("Unknown query attribute '"+atts.getQName(i)+"'!");
            }
            state = QUERY;

        } else
            throw new SAXException("The XTASML's root element must be 'query'!");

    } else if( state == QUERY ) {
        type = localName;
        int n = atts.getLength();
        // match or xpath
        for(int i = 0; i < n; i++) {
           if( atts.getLocalName(i).equals("match") || atts.getLocalName(i).equals("xpath") || atts.getLocalName(i).equals("resource"))
               match=atts.getValue(i);
            else
               throw new SAXException("Unknown query attribute '"+atts.getQName(i)+"'!");
            }
         state = INSTRUCTION;
    } else if( state == INSTRUCTION ) {
        newValue+="<"+localName;
        int n = atts.getLength();
        for(int i = 0; i < n; i++) {
          newValue+=" "+atts.getLocalName(i)+"=\""+atts.getValue(i)+"\"";
        }
        newValue+=">";

    } 


  }


  public void endElement (String namespaceURI, String localName,
                          String qName)
    throws SAXException
  {
     if( localName == type )
         state = QUERY;

     if( state == INSTRUCTION ) {
        newValue+="</"+localName+">";
      }

  }


  public void characters (char[] ch, int start, int length)
    throws SAXException
  {
//    String s = new String(ch, start, (length > 30) ? 30 : length);
    String s = new String(ch, start, length);
    newValue+=s.trim();

//    System .out.println("characters: \""+s+"\"");

  }


  public void ignorableWhitespace (char[] ch, int start, int length)
    throws SAXException
  {
    String s = new String(ch, start, length);
    newValue+=s;
//    S ystem.out.println("characters: \""+s+"\"");

  }

  public void processingInstruction (String target, String data)
    throws SAXException
  {
  }

  public void skippedEntity (String name)
    throws SAXException
  {
  }

  public String getSourceId() 
  {
    return sourceId;
  }

  public String getDestinationId() 
  {
    return destinationId;
  }

  public String getType() 
  {
    return type;
  }

  public String getMatch() 
  {
    return match;
  }

  public String getNewValue() 
  {
    return newValue;
  }
}
