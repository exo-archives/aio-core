package org.exoplatform.services.xml.querying.impl.xtas.object.plugins;

/**
 * Castor map-to element's content
 * @version $Id: CastorMapToElement.java 5799 2006-05-28 17:55:42Z geaz $
 */

public class CastorMapToElement {

    private String table;
    private String xml;
    private String nsUri;
    private String nsPrefix;
    private String ldapDn;
    private String ldapOc;

    public String getXml()
    { 
       return xml; 
    }

    public void setXml(String xml)
    { 
       this.xml = xml; 
    }


}
