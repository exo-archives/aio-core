package org.exoplatform.services.document.diff;

import java.util.List;

public interface Revision extends ToString{

  /**
   * Adds a delta to this revision.
   * 
   * @param delta
   *          the {@link Delta Delta} to add.
   */
  public void addDelta(Delta delta);

  /**
   * Adds a delta to the start of this revision.
   * 
   * @param delta
   *          the {@link Delta Delta} to add.
   */
  public void insertDelta(Delta delta);

  /**
   * Retrieves a delta from this revision by position.
   * 
   * @param i
   *          the position of the delta to retrieve.
   * @return the specified delta
   */
  public Delta getDelta(int i);

  /**
   * Returns the number of deltas in this revision.
   * 
   * @return the number of deltas.
   */
  public int size();

  /**
   * Applies the series of deltas in this revision as patches to the given text.
   * 
   * @param src
   *          the text to patch, which the method doesn't change.
   * @return the resulting text after the patches have been applied.
   * @throws Exception
   *           if any of the patches cannot be applied.
   */
  public Object[] patch(Object[] src) throws Exception;

  /**
   * Applies the series of deltas in this revision as patches to the given text.
   * 
   * @param target
   *          the text to patch.
   * @throws Exception
   *           if any of the patches cannot be applied.
   */
  public void applyTo(List target) throws Exception;

  /**
   * Converts this revision into its Unix diff style string representation.
   * 
   * @param s
   *          a {@link StringBuffer StringBuffer} to which the string
   *          representation will be appended.
   */
  public void toString(StringBuffer s);

  /**
   * Converts this revision into its RCS style string representation.
   * 
   * @param s
   *          a {@link StringBuffer StringBuffer} to which the string
   *          representation will be appended.
   * @param EOL
   *          the string to use as line separator.
   */
  public void toRCSString(StringBuffer s, String EOL);

  /**
   * Converts this revision into its RCS style string representation.
   * 
   * @param s
   *          a {@link StringBuffer StringBuffer} to which the string
   *          representation will be appended.
   */
  public void toRCSString(StringBuffer s);

  /**
   * Converts this delta into its RCS style string representation.
   * 
   * @param EOL
   *          the string to use as line separator.
   */
  public String toRCSString(String EOL);

  /**
   * Converts this delta into its RCS style string representation using the
   * default line separator.
   */
  public String toRCSString();

  /**
   * Accepts a visitor.
   * 
   * @param visitor
   *          the {@link Visitor} visiting this instance
   */
  public void accept(RevisionVisitor visitor);

}