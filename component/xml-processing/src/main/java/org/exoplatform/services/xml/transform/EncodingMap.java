/***************************************************************************
* Copyright 2001-2003 The eXo Platform SARL                                *
 * All rights reserved.                                                    *
 * Please look at license.txt in info directory for more license detail.   *
 **************************************************************************/

package org.exoplatform.services.xml.transform;

/**
 * Created by The eXo Platform SARL        .
 * Conversions between
 *     IANA encoding names and Java encoding names,
 *
 * @author <a href="mailto:alex.kravchuk@gmail.com">Alexander Kravchuk</a>
 * @version $Id:
 */

public interface EncodingMap {
    public String convertIANA2Java(String iana);
    public String convertJava2IANA(String java);
}
