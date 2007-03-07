package org.exoplatform.services.xml.querying.impl.xtas;

import java.io.InputStream;
import java.io.ByteArrayInputStream;

/**
 * Compiled (by XSLT) XTAS instruction
 * @version $Id: Command.java 5799 2006-05-28 17:55:42Z geaz $
 */
public class Command
{
    private byte[] buf;

    public Command(byte[] bytes)
    {
        this.buf = bytes;
    }

    public InputStream getAsInputStream()
    {
        return new ByteArrayInputStream( buf );
    }

    public String toString()
    {
        return new String( buf );
    }

}
