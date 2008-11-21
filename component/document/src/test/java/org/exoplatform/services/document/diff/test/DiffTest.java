/*
 * ====================================================================
 *
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 1999-2003 The Apache Software Foundation.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowledgement:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowledgement may appear in the software itself,
 *    if and wherever such third-party acknowledgements normally appear.
 *
 * 4. The names "The Jakarta Project", "Commons", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.exoplatform.services.document.diff.test;

import junit.framework.TestCase;

import org.exoplatform.services.document.diff.AddDelta;
import org.exoplatform.services.document.diff.ChangeDelta;
import org.exoplatform.services.document.diff.DeleteDelta;
import org.exoplatform.services.document.diff.Delta;
import org.exoplatform.services.document.diff.DiffAlgorithm;
import org.exoplatform.services.document.diff.DiffService;
import org.exoplatform.services.document.diff.Revision;
import org.exoplatform.services.document.diff.RevisionVisitor;
import org.exoplatform.services.document.impl.diff.DeltaImpl;
import org.exoplatform.services.document.impl.diff.DiffServiceImpl;

public abstract class DiffTest extends TestCase {
  static final int LARGE    = 2 * 1024;

  Object[]         empty    = new Object[] {};

  Object[]         original = new String[] { "[1] one", "[2] two", "[3] three", "[4] four",
      "[5] five", "[6] six", "[7] seven", "[8] eight", "[9] nine" };

  // lines 3 and 9 deleted
  Object[]         rev1     = new String[] { "[1] one", "[2] two", "[4] four", "[5] five",
      "[6] six", "[7] seven", "[8] eight", };

  // lines 7 and 8 changed, 9 deleted
  Object[]         rev2     = new String[] { "[1] one", "[2] two", "[3] three", "[4] four",
      "[5] five", "[6] six", "[7] seven revised", "[8] eight revised", };

  protected abstract DiffAlgorithm getAlgo();

  public void testCompare() {
    DiffService diffService = new DiffServiceImpl(getAlgo());
    assertTrue(!diffService.compare(original, empty));
    assertTrue(!diffService.compare(empty, original));
    assertTrue(diffService.compare(empty, empty));
    assertTrue(diffService.compare(original, original));
  }

  public void testEmptySequences() throws Exception {
    String[] emptyOrig = {};
    String[] emptyRev = {};
    DiffService diffService = new DiffServiceImpl(getAlgo());
    Revision revision = diffService.diff(emptyOrig, emptyRev);

    assertEquals("revision size is not zero", 0, revision.size());
  }

  public void testOriginalEmpty() throws Exception {
    String[] emptyOrig = {};
    String[] rev = { "1", "2", "3" };
    DiffService diffService = new DiffServiceImpl(getAlgo());
    Revision revision = diffService.diff(emptyOrig, rev);

    assertEquals("revision size should be one", 1, revision.size());
    assertTrue(revision.getDelta(0) instanceof AddDelta);
  }

  public void testRevisedEmpty() throws Exception {
    String[] orig = { "1", "2", "3" };
    String[] emptyRev = {};
    DiffService diffService = new DiffServiceImpl(getAlgo());
    Revision revision = diffService.diff(orig, emptyRev);

    assertEquals("revision size should be one", 1, revision.size());
    assertTrue(revision.getDelta(0) instanceof DeleteDelta);
  }

  public void testDeleteAll() throws Exception {
    DiffService diffService = new DiffServiceImpl(getAlgo());
    Revision revision = diffService.diff(original, empty);
    assertEquals(1, revision.size());
    assertTrue(revision.getDelta(0) instanceof DeleteDelta);
    assertTrue(diffService.compare(revision.patch(original), empty));
  }

  public void testTwoDeletes() throws Exception {
    DiffService diffService = new DiffServiceImpl(getAlgo());
    Revision revision = diffService.diff(original, rev1);
    assertEquals(2, revision.size());
    assertTrue(revision.getDelta(0) instanceof DeleteDelta);
    assertTrue(revision.getDelta(1) instanceof DeleteDelta);
    assertTrue(diffService.compare(revision.patch(original), rev1));
    assertEquals("3d2" + DiffService.NL + "< [3] three" + DiffService.NL + "9d7" + DiffService.NL
        + "< [9] nine" + DiffService.NL, revision.toString());
  }

  public void testChangeAtTheEnd() throws Exception {
    DiffService diffService = new DiffServiceImpl(getAlgo());
    Revision revision = diffService.diff(original, rev2);
    assertEquals(1, revision.size());
    assertTrue(revision.getDelta(0) instanceof ChangeDelta);
    assertTrue(diffService.compare(revision.patch(original), rev2));
    assertEquals("d7 3" + DiffService.NL + "a9 2" + DiffService.NL + "[7] seven revised"
        + DiffService.NL + "[8] eight revised" + DiffService.NL, revision.toRCSString());
  }

  public void testPatchFailed() throws Exception {
    try {
      DiffService diffService = new DiffServiceImpl(getAlgo());
      Revision revision = diffService.diff(original, rev2);
      assertTrue(!diffService.compare(revision.patch(rev1), rev2));
      fail("Exception not thrown");
    } catch (Exception e) {
    }
  }

  public void testPreviouslyFailedShuffle() throws Exception {
    Object[] orig = new String[] { "[1] one", "[2] two", "[3] three", "[4] four", "[5] five",
        "[6] six" };

    Object[] rev = new String[] { "[3] three", "[1] one", "[5] five", "[2] two", "[6] six",
        "[4] four" };

    DiffService diffService = new DiffServiceImpl(getAlgo());
    Revision revision = diffService.diff(orig, rev);
    Object[] patched = revision.patch(orig);
    assertTrue(diffService.compare(patched, rev));
  }

  public void testEdit5() throws Exception {
    Object[] orig = new String[] { "[1] one", "[2] two", "[3] three", "[4] four", "[5] five",
        "[6] six" };

    Object[] rev = new String[] { "one revised", "two revised", "[2] two", "[3] three",
        "five revised", "six revised", "[5] five" };

    DiffService diffService = new DiffServiceImpl(getAlgo());
    Revision revision = diffService.diff(orig, rev);
    Object[] patched = revision.patch(orig);
    assertTrue(diffService.compare(patched, rev));
  }

  public void testShuffle() throws Exception {
    Object[] orig = new String[] { "[1] one", "[2] two", "[3] three", "[4] four", "[5] five",
        "[6] six" };

    DiffService diffService = new DiffServiceImpl(getAlgo());

    for (int seed = 0; seed < 10; seed++) {
      Object[] shuffle = ((DiffServiceImpl) diffService).shuffle(orig);
      Revision revision = diffService.diff(orig, shuffle);
      Object[] patched = revision.patch(orig);
      if (!diffService.compare(patched, shuffle)) {
        fail("iter " + seed + " revisions differ after patch");
      }
    }
  }

  public void testRandomEdit() throws Exception {
    Object[] orig = original;

    DiffService diffService = new DiffServiceImpl(getAlgo());
    for (int seed = 0; seed < 10; seed++) {
      Object[] random = ((DiffServiceImpl) diffService).randomEdit(orig, seed);
      Revision revision = diffService.diff(orig, random);
      Object[] patched = revision.patch(orig);
      if (!diffService.compare(patched, random)) {
        fail("iter " + seed + " revisions differ after patch");
      }
      orig = random;
    }
  }

  public void testVisitor() {
    Object[] orig = new String[] { "[1] one", "[2] two", "[3] three", "[4] four", "[5] five",
        "[6] six" };
    Object[] rev = new String[] { "[1] one", "[2] two revised", "[3] three", "[4] four revised",
        "[5] five", "[6] six" };

    class Visitor implements RevisionVisitor {

      StringBuffer sb = new StringBuffer();

      public void visit(Revision revision) {
        sb.append("visited Revision\n");
      }

      public void visit(DeleteDelta delta) {
        visit((DeltaImpl) delta);
      }

      public void visit(ChangeDelta delta) {
        visit((DeltaImpl) delta);
      }

      public void visit(AddDelta delta) {
        visit((DeltaImpl) delta);
      }

      public void visit(Delta delta) {
        sb.append(delta.getRevised());
        sb.append("\n");
      }

      public String toString() {
        return sb.toString();
      }
    }

    Visitor visitor = new Visitor();
    DiffService diffService = new DiffServiceImpl(getAlgo());
    try {
      diffService.diff(orig, rev).accept(visitor);
      assertEquals(visitor.toString(), "visited Revision\n" + "[2] two revised\n"
          + "[4] four revised\n");
    } catch (Exception e) {
      fail(e.toString());
    }
  }

  public void testLargeShuffles() throws Exception {
    DiffService diffService = new DiffServiceImpl(getAlgo());
    Object[] orig = ((DiffServiceImpl) diffService).randomSequence(LARGE);
    for (int seed = 0; seed < 3; seed++) {
      Object[] rev = ((DiffServiceImpl) diffService).shuffle(orig);
      Revision revision = diffService.diff(orig, rev);
      Object[] patched = revision.patch(orig);
      if (!diffService.compare(patched, rev)) {
        fail("iter " + seed + " revisions differ after patch");
      }
      orig = rev;
    }
  }

  public void testLargeShuffleEdits() throws Exception {
    DiffService diffService = new DiffServiceImpl(getAlgo());
    Object[] orig = ((DiffServiceImpl) diffService).randomSequence(LARGE);
    for (int seed = 0; seed < 3; seed++) {
      Object[] rev = ((DiffServiceImpl) diffService).randomEdit(orig, seed);
      Revision revision = diffService.diff(orig, rev);
      Object[] patched = revision.patch(orig);
      if (!diffService.compare(patched, rev)) {
        fail("iter " + seed + " revisions differ after patch");
      }
    }
  }

  public void testLargeAllEdited() throws Exception {
    DiffService diffService = new DiffServiceImpl(getAlgo());
    Object[] orig = ((DiffServiceImpl) diffService).randomSequence(LARGE);
    Object[] rev = ((DiffServiceImpl) diffService).editAll(orig);
    Revision revision = diffService.diff(orig, rev);
    Object[] patched = revision.patch(orig);
    if (!diffService.compare(patched, rev)) {
      fail("revisions differ after patch");
    }

  }
}
