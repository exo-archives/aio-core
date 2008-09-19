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

package org.exoplatform.services.document.test;

import java.io.InputStream;

import org.exoplatform.container.PortalContainer;
import org.exoplatform.services.document.DocumentReaderService;
import org.exoplatform.test.BasicTestCase;

/**
 * Created by The eXo Platform SAS Author : Sergey Karpenko
 * <sergey.karpenko@exoplatform.com.ua>
 * 
 * @version $Id: $
 */

public class TestOpenOfficeDocumentReader extends BasicTestCase {

  DocumentReaderService service_;

  public void setUp() throws Exception {
    PortalContainer pcontainer = PortalContainer.getInstance();
    service_ = (DocumentReaderService) pcontainer.getComponentInstanceOfType(DocumentReaderService.class);
  }

  public void testGetContentAsString() throws Exception {
    InputStream is = TestOpenOfficeDocumentReader.class.getResourceAsStream("/test.odt");
    String text = service_.getDocumentReader("application/vnd.oasis.opendocument.text")
                          .getContentAsText(is);
    System.out.println("[" + text + "]");
    /*
     * String etalon = "Subscription:" +
     * "\tEULA with add on warranties and non GPL viral effect (all Customer's development free of GPL license limitations). This agreement continues to be valid even if customers do not renew their subscription.\n"
     * +
     * "\tProduct documentation including user and admin guides to eXo platform portal, ECM, JCR, and Portlet Container. (currently we have all the docs accessible for free but it will not)\n"
     * +
     * "\tAccess to all Flash tutorial gives a visual guide to eXo platform and demonstrates a comprehensive tutorial that enhance the understanding of eXo products. (only part of Flash tutorials are accessible for free)\n"
     * +
     * "\tAdvanced Installer is an application that makes it quick and easy to install eXo platform products with simple clicks and allows better configuration to integrate existing database and directories (LDAP) in a multi platform environment.\n"
     * +
     * "\tUnlimited access to online premium forum (customer request tracking?) support, you will get answer from eXo platform company technical specialist employees. (TODO) \n"
     * +
     * "\tUnlimited email support within 48 hours response time for limited contact names per CPU.(i think we may join it with prev item using term customer request tracking with email notification)\n"
     * +
     * "\tUpdate alert will send out email periodically to subscribers all the latest change in eXo documentations, flash tutorials, and product services. (TODO)\n"
     * +
     * "\tTechnical code improvement alert will send out email periodically to subscribers to inform of all the latest code patches, latest version, latest code improvement download. (TODO)\n"
     * +
     * "\tKnowledge Base subscribers can access to all of eXo platform wiki knowledge bases. (TODO, i do not think we may put it into agreement yet)\n"
     * +
     * "\tFeature Request Priority As customers you have priorities to request the latest features improvement for any of eXo products next version. (what does it mean?)\n"
     * + "\n" +
     * "Subscription is annual and per CPU because it is the unit of load increase and so more support demand. Subscription advantages are:\n"
     * + "\tEULA � it is obligatory to be Subscriber to get EULA\n" +
     * "\tAdditional documentations (Guides, Flash tutorials)\n" +
     * "\tAdditional software (Installer)\n" +
     * "\tAdditional product/documentation changes notifications\n" +
     * "\tProfessional support: limited contact name (3 customer contact with 1 eXo contact per CPU), 48 hours to answer via email\n"
     * + "\tFeature request priority (also depending on CPU number)\n" + "\n";
     * System.out.println("["+etalon+"]");
     * System.out.println("TEXT size ["+text.
     * length()+"]  ETALON LEN ["+etalon.length()+"]");
     * assertEquals("Wrong string returned", etalon, text);
     */
  }
}
