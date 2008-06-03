/**
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

package org.exoplatform.services.xml.transform;

import javax.xml.transform.Source;
import javax.xml.transform.Result;
import javax.xml.transform.TransformerException;

/**
 * Created by The eXo Platform SAS .
 * 
 * @author <a href="mailto:geaz@users.sourceforge.net">Gennady Azarenkov</a>
 * @author <a href="mailto:alex.kravchuk@gmail.com">Alexander Kravchuk</a>
 * @version $Id: AbstractTransformer.java 5799 2006-05-28 17:55:42Z geaz $
 */

public interface AbstractTransformer {
  /**
   * Initialize a result of transformation
   * 
   * @param result
   *          Result
   * @throws NotSupportedIOTypeException
   *           if try to initialize with not supported implementation of Result
   */
  void initResult(Result result) throws NotSupportedIOTypeException;

  /**
   * Transform source data to result
   * 
   * @param source
   *          Source - input of transformation
   * 
   * @throws NotSupportedIOTypeException
   *           if not supported implementation of Source
   * @throws TransformerException
   *           if error occurred on transformation process
   * @throws IllegalStateException
   *           if result not initialized by initResult
   */
  void transform(Source source) throws NotSupportedIOTypeException,
      TransformerException, IllegalStateException;
}
