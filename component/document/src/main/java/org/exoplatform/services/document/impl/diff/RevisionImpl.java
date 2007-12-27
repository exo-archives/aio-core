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

package org.exoplatform.services.document.impl.diff;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.exoplatform.services.document.diff.Delta;
import org.exoplatform.services.document.diff.DiffService;
import org.exoplatform.services.document.diff.Revision;
import org.exoplatform.services.document.diff.RevisionVisitor;


/**
 * A Revision holds the series of deltas that describe the differences between
 * two sequences.
 * 
 * @version $Revision: 1.8 $ $Date: 2003/10/13 08:00:24 $
 * 
 * @author <a href="mailto:juanco@suigeneris.org">Juanco Anez</a>
 * @author <a href="mailto:bwm@hplb.hpl.hp.com">Brian McBride</a>
 * 
 * @see DeltaImpl
 * @see DiffServiceImpl
 * @see ChunkImpl
 * @see RevisionImpl
 * 
 * modifications 27 Apr 2003 bwm
 * 
 * Added visitor pattern Visitor interface and accept() method.
 */

public class RevisionImpl extends ToStringImpl implements Revision {

  List deltas_ = new LinkedList();

  /**
   * Creates an empty Revision.
   */
  public RevisionImpl() {
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.diff.Rev#addDelta(org.exoplatform.services.diff.Delta)
   */
  public synchronized void addDelta(Delta delta) {
    if (delta == null) {
      throw new IllegalArgumentException("new delta is null");
    }
    deltas_.add(delta);
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.diff.Rev#insertDelta(org.exoplatform.services.diff.Delta)
   */
  public synchronized void insertDelta(Delta delta) {
    if (delta == null) {
      throw new IllegalArgumentException("new delta is null");
    }
    deltas_.add(0, delta);
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.diff.Rev#getDelta(int)
   */
  public DeltaImpl getDelta(int i) {
    return (DeltaImpl) deltas_.get(i);
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.diff.Rev#size()
   */
  public int size() {
    return deltas_.size();
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.diff.Rev#patch(java.lang.Object[])
   */
  public Object[] patch(Object[] src) throws Exception {
    List target = new ArrayList(Arrays.asList(src));
    applyTo(target);
    return target.toArray();
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.diff.Rev#applyTo(java.util.List)
   */
  public synchronized void applyTo(List target) throws Exception {
    ListIterator i = deltas_.listIterator(deltas_.size());
    while (i.hasPrevious()) {
      Delta delta = (Delta) i.previous();
      delta.patch(target);
    }
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.diff.Rev#toString(java.lang.StringBuffer)
   */
  public synchronized void toString(StringBuffer s) {
    Iterator i = deltas_.iterator();
    while (i.hasNext()) {
      ((Delta) i.next()).toString(s);
    }
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.diff.Rev#toRCSString(java.lang.StringBuffer, java.lang.String)
   */
  public synchronized void toRCSString(StringBuffer s, String EOL) {
    Iterator i = deltas_.iterator();
    while (i.hasNext()) {
      ((Delta) i.next()).toRCSString(s, EOL);
    }
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.diff.Rev#toRCSString(java.lang.StringBuffer)
   */
  public void toRCSString(StringBuffer s) {
    toRCSString(s, DiffService.NL);
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.diff.Rev#toRCSString(java.lang.String)
   */
  public String toRCSString(String EOL) {
    StringBuffer s = new StringBuffer();
    toRCSString(s, EOL);
    return s.toString();
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.diff.Rev#toRCSString()
   */
  public String toRCSString() {
    return toRCSString(DiffService.NL);
  }

  /* (non-Javadoc)
   * @see org.exoplatform.services.diff.Rev#accept(org.exoplatform.services.diff.RevisionVisitor)
   */
  public void accept(RevisionVisitor visitor) {
    visitor.visit(this);
    Iterator iter = deltas_.iterator();
    while (iter.hasNext()) {
      ((Delta) iter.next()).accept(visitor);
    }
  }

}
