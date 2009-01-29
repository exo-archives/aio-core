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
package org.exoplatform.services.web.css.sac;

import org.w3c.css.sac.DocumentHandler;
import org.w3c.css.sac.InputSource;
import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.SACMediaList;
import org.w3c.css.sac.SelectorList;
import org.w3c.css.sac.LexicalUnit;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class AbstractDocumentHandler implements DocumentHandler {

  public void startDocument(InputSource source) throws CSSException {
    throw new UnsupportedOperationException();
  }

  public void endDocument(InputSource source) throws CSSException {
    throw new UnsupportedOperationException();
  }

  public void comment(String text) throws CSSException {
    throw new UnsupportedOperationException();
  }

  public void ignorableAtRule(String atRule) throws CSSException {
    throw new UnsupportedOperationException();
  }

  public void namespaceDeclaration(String prefix, String uri) throws CSSException {
    throw new UnsupportedOperationException();
  }

  public void importStyle(String uri, SACMediaList media, String defaultNamespaceURI) throws CSSException {
    throw new UnsupportedOperationException();
  }

  public void startMedia(SACMediaList media) throws CSSException {
    throw new UnsupportedOperationException();
  }

  public void endMedia(SACMediaList media) throws CSSException {
    throw new UnsupportedOperationException();
  }

  public void startPage(String name, String pseudo_page) throws CSSException {
    throw new UnsupportedOperationException();
  }

  public void endPage(String name, String pseudo_page) throws CSSException {
    throw new UnsupportedOperationException();
  }

  public void startFontFace() throws CSSException {
    throw new UnsupportedOperationException("font face not implemented");
  }

  public void endFontFace() throws CSSException {
    throw new UnsupportedOperationException("font face not implemented");
  }

  public void startSelector(SelectorList selectors) throws CSSException {
    throw new UnsupportedOperationException();
  }

  public void endSelector(SelectorList selectors) throws CSSException {
    throw new UnsupportedOperationException();
  }

  public void property(String name, LexicalUnit value, boolean important) throws CSSException {
    throw new UnsupportedOperationException();
  }
}
