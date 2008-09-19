//$Id: HibernateEntityResolver.java 5332 2006-04-29 18:32:44Z geaz $
//Contributed by Markus Meissner
package org.exoplatform.services.database.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

public class HibernateEntityResolver implements EntityResolver, Serializable {

  private static final String   URL = "http://hibernate.sourceforge.net/";

  private transient ClassLoader resourceLoader;

  /**
   * Default constructor using DTDEntityResolver classloader for resource
   * loading.
   */
  public HibernateEntityResolver() {
    // backward compatibility
    resourceLoader = this.getClass().getClassLoader();
  }

  /**
   * Set the class loader used to load resouces
   * 
   * @param resourceLoader class loader to use
   */
  public HibernateEntityResolver(ClassLoader resourceLoader) {
    this.resourceLoader = resourceLoader;
  }

  public InputSource resolveEntity(String publicId, String systemId) {
    // S ystem.out.println("====> Resolve entity, public id " + publicId +
    // " system id " + systemId) ;
    if (systemId != null && systemId.startsWith(URL)) {
      // S ystem.out.println("trying to locate " + systemId +
      // " in classpath under org/hibernate/");
      // Search for DTD
      InputStream dtdStream = resourceLoader.getResourceAsStream("org/hibernate/"
          + systemId.substring(URL.length()));
      if (dtdStream == null)
        return null;
      // S ystem.out.println("found " + systemId + " in classpath");
      InputSource source = new InputSource(dtdStream);
      source.setPublicId(publicId);
      source.setSystemId(systemId);
      return source;
    }
    // use the default behaviour
    return null;
  }

  private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
    /** to allow serialization of configuration */
    ois.defaultReadObject();
    this.resourceLoader = this.getClass().getClassLoader();
  }
}
