/*
 * Copyright (C) 2003-2007 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */
package org.exoplatform.services.document.impl;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.exoplatform.services.document.DCMetaData;
import org.exoplatform.services.log.ExoLogger;

import com.lowagie.text.pdf.PRTokeniser;
import com.lowagie.text.pdf.PdfDate;
import com.lowagie.text.pdf.PdfReader;

/**
 * Created by The eXo Platform SAS
 * 
 * A parser of Adobe PDF files.
 * 
 * @author Phung Hai Nam
 * @author Gennady Azarenkov
 * @version Oct 19, 2005
 */
public class PDFDocumentReader extends BaseDocumentReader {

  protected static Log log = ExoLogger.getLogger("platform.PDFDocumentReader");

  /**
   * Get the application/pdf mime type.
   * 
   * @return The application/pdf mime type.
   */
  public String[] getMimeTypes() {
    return new String[] { "application/pdf" };
  }

  /**
   * Returns only a text from .pdf file content.
   * 
   * @param is
   *          an input stream with .pdf file content.
   * @return The string only with text from file content.
   * @throws Exception
   */
  public String getContentAsText(InputStream is) throws Exception {
      
    PdfReader reader = new PdfReader(is);
    PRTokeniser token;
    StringBuilder builder = new StringBuilder();
      
    for(int i = 1; i<=reader.getNumberOfPages(); i++){
      byte[] pageBytes = reader.getPageContent(i);
      if (pageBytes != null){
        token = new PRTokeniser(pageBytes);
        while (token.nextToken()){
          if (token.getTokenType() == PRTokeniser.TK_STRING){
            builder.append(token.getStringValue()+" ");
          }
        }
      }
    }
    return builder.toString(); 
  }
  
  public String getContentAsText(InputStream is, String encoding) throws Exception {
    //Ignore encoding
    return getContentAsText(is);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.exoplatform.services.document.DocumentReader#getProperties(java.io.InputStream)
   */
  public Properties getProperties(InputStream is) throws Exception {

    PdfReader reader = new PdfReader(is,"".getBytes());
    
    //Read the file metadata
    HashMap info = reader.getInfo();
    
    Properties props = new Properties();
                
    String author = (String)info.get("Author");
    if(author!=null){
      props.put(DCMetaData.CONTRIBUTOR, author);
    }
     
    String creationDate = (String)info.get("CreationDate");
    if(creationDate!=null){
      props.put(DCMetaData.DATE, PdfDate.decode(creationDate));
    }
    
    String creator = (String)info.get("Creator");
    if(creator!=null){
      props.put(DCMetaData.CREATOR, creator);
    }
    
    String subject = (String)info.get("Subject");
    if(subject!=null){
      props.put(DCMetaData.SUBJECT, subject);
    }
    
    String modDate = (String)info.get("ModDate");
    if(modDate!=null){
      props.put(DCMetaData.DATE, PdfDate.decode(modDate));
    }
    
    String publisher = (String)info.get("Producer");
    if(publisher!=null){
      props.put(DCMetaData.PUBLISHER, publisher);
    }
    
    //TODO "Desc"?
    String description = (String)info.get("Desc");
    if(description!=null){
      props.put(DCMetaData.DESCRIPTION, description);
    }
    
    String title = (String)info.get("Title");
    if(title!=null){
      props.put(DCMetaData.TITLE, title);
    }

    return props;
  }

}
