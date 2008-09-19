package org.exoplatform.services.document.diff;

public interface ToString {

  /**
   * Default implementation of the {@link java.lang.Object#toString toString() }
   * method that delegates work to a {@link java.lang.StringBuffer StringBuffer}
   * base version.
   */
  public abstract String toString();

  /**
   * Place a string image of the object in a StringBuffer.
   * 
   * @param s the string buffer.
   */
  public abstract void toString(StringBuffer s);

}
