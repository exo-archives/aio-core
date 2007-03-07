package org.exoplatform.services.xml.querying.impl.xtas;

import java.io.*;
import java.util.*;

import org.w3c.dom.Node;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Attr;
import org.w3c.dom.NamedNodeMap;

/** Some utilites for testing 
 * @version $Id: util.java 5799 2006-05-28 17:55:42Z geaz $*/

public class util
{
   public static final String XML_DECLARATION = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";


    public static String getFileContent(String fileName) throws IOException
    {

        BufferedReader in = new BufferedReader(new FileReader(fileName));
        String str, out="";
        while ((str = in.readLine()) != null) {
           out+=str;
        }
        in.close();
        return out.trim();

    }

    public static void print(Node node) throws UnsupportedEncodingException{

       PrintWriter  out = new PrintWriter(new OutputStreamWriter(System.out, "UTF8"));

//       String lineRowColumn = (String ) ((NodeImpl) node).getUserData("startLine");

       int type = node.getNodeType();
       switch ( type ) {
          // print document

          case Node.DOCUMENT_NODE: {
 //               out.println( "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
                print( ((Document)node).getDocumentElement());
                out.flush();
                break;
             }

             // print element with attributes
          case Node.ELEMENT_NODE: {
                out.print( /*lineRowColumn + ":" +  */'<');
                out.print(node.getNodeName());
                Attr attrs[] = sortAttributes(node.getAttributes());
                for ( int i = 0; i < attrs.length; i++ ) {
                   Attr attr = attrs[i];
                   out.print(' ');
                   out.print(attr.getNodeName());
                   out.print("=\"");
                   out.print(attr.getNodeValue());
                   out.print('"');
                }
                out.print('>');

                NodeList children = node.getChildNodes();
                if ( children != null ) {

                   int len = children.getLength();
                   for ( int i = 0; i < len; i++ ) {
                      print(children.item(i));
                   }

                }

                out.print("\n");
                break;
             }

             // handle entity reference nodes
          case Node.ENTITY_REFERENCE_NODE: {
                out.print('&');
                out.print(node.getNodeName());
                out.print(';');
                break;
             }

             // print cdata sections
          case Node.CDATA_SECTION_NODE: {
                out.print("<![CDATA[");
                out.print(node.getNodeValue());
                out.print("]]>");
                break;
             }

             // print text
          case Node.TEXT_NODE: {
                out.print( node.getNodeValue());
                break;
             }

             // print processing instruction
          case Node.PROCESSING_INSTRUCTION_NODE: {
                out.print("<?");
                out.print(node.getNodeName());
                String data = node.getNodeValue();
                if ( data != null && data.length() > 0 ) {
                   out.print(' ');
                   out.print(data);
                }
                out.print("?>");
                break;
             }
       }
 /*
       if ( type == Node.ELEMENT_NODE ) {
          out.print("</");
          out.print(node.getNodeName());
          out.print('>');
       }
 */
       out.flush();

    }

    /** Returns a sorted list of attributes. */
    private static Attr[] sortAttributes(NamedNodeMap attrs) {

       int len = (attrs != null) ? attrs.getLength() : 0;
       Attr array[] = new Attr[len];
       for ( int i = 0; i < len; i++ ) {
          array[i] = (Attr)attrs.item(i);
       }
       for ( int i = 0; i < len - 1; i++ ) {
          String name  = array[i].getNodeName();
          int    index = i;
          for ( int j = i + 1; j < len; j++ ) {
             String curName = array[j].getNodeName();
             if ( curName.compareTo(name) < 0 ) {
                name  = curName;
                index = j;
             }
          }
          if ( index != i ) {
             Attr temp    = array[i];
             array[i]     = array[index];
             array[index] = temp;
          }
       }

       return (array);

    }

    public static String packXmlString(String str)
    {

       StringTokenizer parser = new StringTokenizer(str,"<");
       String out = "";
       try {
          while(parser.hasMoreTokens()) {

             // Get ELEMENT with attributes...
             String out1 = "<"+parser.nextToken();
             StringTokenizer parser1 = new StringTokenizer(out1,">");
             String out2 = parser1.nextToken()+">";

             // Get nested TEXT_ELEMENT if any... 
             String out3 = "";
             int textInd = out1.indexOf(">");
             if (textInd++ < out1.length()) 
                 out3 = new String( out1.substring( textInd ) ).trim();

              // Concatenate all...
              out+=(out2+out3);

          }
//          System.out.println(out);
          return (out.trim());
       } catch (NoSuchElementException e) {
            System.out.println("QueryResult.parseInput ERROR: "+e);
            return null;
       }

    }
}
