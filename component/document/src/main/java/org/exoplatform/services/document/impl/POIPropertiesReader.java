/**
 * Copyright 2001-2006 The eXo Platform SARL         All rights reserved.  *
 * Please look at license.txt in info directory for more license detail.   *
 */

package org.exoplatform.services.document.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.poi.hpsf.PropertySet;
import org.apache.poi.hpsf.PropertySetFactory;
import org.apache.poi.hpsf.SummaryInformation;
import org.apache.poi.poifs.eventfilesystem.POIFSReader;
import org.apache.poi.poifs.eventfilesystem.POIFSReaderEvent;
import org.apache.poi.poifs.eventfilesystem.POIFSReaderListener;
import org.exoplatform.services.document.DCMetaData;

/**
 * Created by The eXo Platform SARL        .
 * @author Gennady Azarenkov
 * @version $Id: $
 */

public class POIPropertiesReader {
  
  private final Properties props = new Properties();
  
  public Properties getProperties() {
    return props;
  }
  
  public Properties readDCProperties(InputStream is) throws Exception {
    
    POIFSReaderListener readerListener = new POIFSReaderListener() {
      public void processPOIFSReaderEvent(final POIFSReaderEvent event) {
        try {
          PropertySet ps = PropertySetFactory.create(event.getStream());
          if (ps instanceof SummaryInformation) {
            SummaryInformation si = (SummaryInformation) ps;
            
            if(si.getLastAuthor() != null && si.getLastAuthor().length() > 0 )
              props.put(DCMetaData.CONTRIBUTOR, si.getLastAuthor());
            if(si.getComments() != null && si.getComments().length() > 0)
              props.put(DCMetaData.DESCRIPTION, si.getComments());
            if(si.getCreateDateTime() != null)
              props.put(DCMetaData.DATE, si.getCreateDateTime());
            if(si.getAuthor() != null && si.getAuthor().length() > 0)
              props.put(DCMetaData.CREATOR, si.getAuthor());
            if(si.getKeywords() != null && si.getKeywords().length() > 0)
              props.put(DCMetaData.SUBJECT, si.getKeywords());
            if(si.getLastSaveDateTime() != null)
              props.put(DCMetaData.DATE, si.getLastSaveDateTime());
//            if(docInfo.getProducer() != null)
//              props.put(DCMetaData.PUBLISHER, docInfo.getProducer());
            if(si.getSubject() != null && si.getSubject().length() > 0)
              props.put(DCMetaData.SUBJECT, si.getSubject());
            if(si.getTitle() != null && si.getTitle().length() > 0)
              props.put(DCMetaData.TITLE, si.getTitle());

          }
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }
    };
    
    try {
      POIFSReader poiFSReader = new POIFSReader();
      poiFSReader.registerListener(readerListener,
          SummaryInformation.DEFAULT_STREAM_NAME);
      poiFSReader.read(is);
    } catch (IOException ie) {
      // This exception cause by POIFS
    }finally {
      if (is != null) {
        try {
          is.close();
        } catch (IOException e) {
        }
      }
    }

    return props;
  }

}
