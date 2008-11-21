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
package org.exoplatform.services.web.css.comment;

import java.io.Reader;
import java.io.IOException;

/**
 * @author <a href="mailto:julien.viet@exoplatform.com">Julien Viet</a>
 * @version $Revision$
 */
public class CommentFilter extends Reader {

  /** . */
  private final PushbackReader in;

  /** . */
  private Status status;

  /** . */
  private final CommentListener listener;

  public CommentFilter(Reader in) {
    this(in, null);
  }

  public CommentFilter(Reader in, Object lock) {
    this(in, null, lock);
  }

  public CommentFilter(Reader in, CommentListener listener) {
    this.in = new PushbackReader(in);
    this.status = Status.CSS;
    this.listener = listener;
  }

  public CommentFilter(Reader in, CommentListener listener, Object lock) {
    super(lock);
    this.in = new PushbackReader(in);
    this.status = Status.CSS;
    this.listener = listener;
  }

  public int read(char cbuf[], int off, int len) throws IOException {
    int count = 0;
    StringBuilder comment = null;

    //
    main:
    while (len > 0) {

      int i = in.read();

      // 
      if (i == -1) {
        break;
      }
      char c = (char)i;

      switch (status) {
        case CSS:
          if (c == '/') {
            status = Status.BEGIN_COMMENT;
          } else {
            cbuf[off++] = c;
            count++;
            len--;
          }
          break;
        case BEGIN_COMMENT:
          if (c == '/') {
            throw new UnsupportedOperationException("todo");
          } else if (c == '*') {
            status = Status.COMMENT;
            if (count > 2) {
              break main;
            }
          } else {
            cbuf[off++] = '/';
            cbuf[off++] = c;
            count += 2;
            len -= 2;
            status = Status.CSS;
          }
          break;
        case COMMENT:
          if (c == '*') {
            status = Status.END_COMMENT;
          } else if (listener != null) {
            if (comment == null) {
              comment = new StringBuilder();
            }
            comment.append(c);
          }

          break;
        case END_COMMENT:
          if (c == '/') {
            status = Status.CSS;

            // Emit a comment
            if (listener != null) {
              String text = comment.toString();
              listener.comment(text);
            }

            // We force a break just after the comment
            break main;
          } else if (listener != null) {
            if (comment == null) {
              comment = new StringBuilder();
            }
            comment.append(c);
          }
      }
    }

    //
    if (status != Status.CSS && status != Status.COMMENT) {
      throw new IllegalStateException();
    } else if (count == 0) {
      int i = in.read();
      if (i == -1) {
        if (status == Status.CSS) {
          return -1;
        } else {
          throw new IllegalStateException();
        }
      } else {
        in.pushback((char)i);
        return 0;
      }
    } else {
      return count;
    }
  }

  public void close() throws IOException {
  }
}
