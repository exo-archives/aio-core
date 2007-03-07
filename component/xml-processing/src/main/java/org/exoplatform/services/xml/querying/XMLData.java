package org.exoplatform.services.xml.querying;

import java.io.InputStream;

public interface XMLData {
    /**
     * Must be realized in concrete subclass
     */
    byte[] getAsByteArray();

    InputStream getAsInputStream();

    String getAsString();

    /**
     * Is this object empty?
     * **/
    boolean isEmpty();
}
