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
import java.util.Calendar;
import java.util.Properties;

import org.apache.poi.hssf.usermodel.*;

/**
 * Created by The eXo Platform SAS
 *
 * A parser of Microsoft Excel files.
 * @author <a href="mailto:phunghainam@gmail.com">Phung Hai Nam</a>
 * @author Gennady Azarenkov
 * @version Oct 21, 2005
 */
public class MSExcelDocumentReader extends BaseDocumentReader {

  /**
   * Get the application/excel mime type.
   * @return The string with application/excel mime type.
   */
  public String[] getMimeTypes() {
    return new String[] {"application/excel", "application/xls"} ;
  }

  /**
   * Returns only a text from .xls file content.
   * @param is an input stream with .xls file content.
   * @return The string only with text from file content.
   * @throws Exception
   */
  public String getContentAsText(InputStream is) throws Exception {
    String text = "";
    try {
      HSSFWorkbook wb = new HSSFWorkbook(is);
      for (int i=0; i < wb.getNumberOfSheets(); i++) {
        HSSFSheet sheet = wb.getSheetAt(i);
        if (sheet != null) {
          for (int j=0; j < sheet.getPhysicalNumberOfRows(); j++) {
            HSSFRow row = sheet.getRow(j);
            for(int k=0; k < row.getPhysicalNumberOfCells(); k++) {
              HSSFCell cell = row.getCell((short) k) ;
              switch(cell.getCellType()) {
                case HSSFCell.CELL_TYPE_NUMERIC : {
                  double d = cell.getNumericCellValue();
                  if (isCellDateFormatted(cell)) {
                    Calendar cal = null ;
                    cal.setTime(HSSFDateUtil.getJavaDate(d));
                    String cellText = "" ;
                    cellText= (String.valueOf(cal.get(Calendar.YEAR))).substring(2);
                    cellText = cal.get(Calendar.MONTH)+1 + "/" + cal.get(Calendar.DAY_OF_MONTH) +
                    "/" +  cellText;
                  } else text =text + d + " \n";
                  break;
                }
                case HSSFCell.CELL_TYPE_FORMULA  :
                  text =text + cell.getCellFormula().toString() + " ";
                  break;
                case  HSSFCell.CELL_TYPE_BOOLEAN  :
                  text =text + cell.getBooleanCellValue() + " ";
                  break;
                case HSSFCell.CELL_TYPE_ERROR  :
                  text =text + cell.getErrorCellValue() + " ";
                  break;
                case HSSFCell.CELL_TYPE_STRING :
                  text =text + cell.getStringCellValue().toString() + " ";
                  break ;
                default :
                  break ;
              }
            }
          }
        }
      }
    }catch(Exception e)  {}
    return text ;
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.document.DocumentReader#getProperties(java.io.InputStream)
   */
  public Properties getProperties(InputStream is) throws Exception {
    POIPropertiesReader reader = new POIPropertiesReader();
    reader.readDCProperties(is);
    return reader.getProperties();
  }

  public static boolean isCellDateFormatted(HSSFCell cell) {
    boolean bDate = false;
    double d = cell.getNumericCellValue();
    if( HSSFDateUtil.isValidExcelDate(d) ) {
      HSSFCellStyle style = cell.getCellStyle();
      int i = style.getDataFormat();
      switch(i) {
        case 0xe:
        case 0xf:
        case 0x10:
        case 0x11:
        case 0x12:
        case 0x13:
        case 0x14:
        case 0x15:
        case 0x16:
        case 0x2d:
        case 0x2e:
        case 0x2f:
           bDate = true;
           break;
        default:
          bDate = false;
          break;
       }
     }
     return bDate;
     }
}
