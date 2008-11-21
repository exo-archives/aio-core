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
package org.exoplatform.services.web.css.sac.wrapper;

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
class ParserDocumentHandler implements DocumentHandler {

  final DocumentHandler next;

  public ParserDocumentHandler(DocumentHandler next) {
    this.next = next;
  }

  public void startDocument(InputSource source) throws CSSException {
    next.startDocument(source);
  }

  public void endDocument(InputSource source) throws CSSException {
    next.endDocument(source);
  }

  public void comment(String text) throws CSSException {
    // Ignore comments from the parser as they are wrong
  }

  public void ignorableAtRule(String atRule) throws CSSException {
  }

  public void namespaceDeclaration(String prefix, String uri) throws CSSException {
  }

  public void importStyle(String uri, SACMediaList media, String defaultNamespaceURI) throws CSSException {
    next.importStyle(uri, media, defaultNamespaceURI);
  }

  public void startMedia(SACMediaList media) throws CSSException {
  }

  public void endMedia(SACMediaList media) throws CSSException {
  }

  public void startPage(String name, String pseudo_page) throws CSSException {
  }

  public void endPage(String name, String pseudo_page) throws CSSException {
  }

  public void startFontFace() throws CSSException {
  }

  public void endFontFace() throws CSSException {
  }

  public void startSelector(SelectorList selectors) throws CSSException {
    next.startSelector(selectors);
  }

  public void endSelector(SelectorList selectors) throws CSSException {
    next.endSelector(selectors);
  }

  public void property(String name, LexicalUnit value, boolean important) throws CSSException {
    next.property(name, value, important);
  }
}