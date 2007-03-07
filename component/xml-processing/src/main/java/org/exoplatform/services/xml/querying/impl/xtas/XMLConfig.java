package org.exoplatform.services.xml.querying.impl.xtas;
import java.util.Collection;
import java.io.IOException;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import org.exoplatform.services.xml.querying.ConfigException;
import org.exoplatform.services.xml.querying.impl.xtas.resource.ResourceDescriptor;
import org.exoplatform.services.xml.querying.impl.xtas.xml.Utils;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import java.util.ArrayList;
import org.w3c.dom.Node;
import org.w3c.dom.Text;


public class XMLConfig implements Config
{
    private static XMLConfig config = null;
    protected Collection resources;

    protected XMLConfig(String fileName) throws IOException, SAXException,
        ParserConfigurationException, ConfigException
    {

        Document doc = Utils.createDocument(this.getClass().getResourceAsStream(fileName));
        NodeList list1 = doc.getElementsByTagName("resource");

        resources = new ArrayList();

        for(int i=0; i< list1.getLength(); i++) {
            ResourceDescriptor descr = new ResourceDescriptor();
            resources.add(descr);
            Node params = list1.item(i);
            NodeList list2 = params.getChildNodes();
            for(int j=0;j <list2.getLength(); j++) {
                Node param = list2.item(j);

                if(param.hasChildNodes())                
                if( param.getNodeName().equals("name") )
                    descr.setName(((Text)param.getFirstChild()).getData());
                else if( param.getNodeName().equals("classname") )
                    descr.setClassname(((Text)param.getFirstChild()).getData());
                else if( param.getNodeName().equals("prefix") )
                    descr.setPrefix(((Text)param.getFirstChild()).getData());
                else if( param.getNodeName().equals("description") )
                    descr.setDescription(((Text)param.getFirstChild()).getData());
            }
        }

        if (resources.size() == 0)
            throw new ConfigException("Xtas Config Exception - there are No Resources in config!");
       
    }

    public static XMLConfig getInstance() throws ConfigException
    {
       if (config == null) {
          try {
            config = new XMLConfig("/xtas-config.xml");
          } catch (ConfigException e) {
            throw e;
          } catch (Exception e) {
            throw new ConfigException("Error while creating XmlConfig! "+e);
          }
        }
       return config;
    }

    public Collection getResources()
    {
       return resources;
    }

}
