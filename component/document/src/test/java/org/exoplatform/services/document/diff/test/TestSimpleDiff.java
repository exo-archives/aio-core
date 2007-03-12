package org.exoplatform.services.document.diff.test;

import org.exoplatform.services.document.diff.DiffAlgorithm;
import org.exoplatform.services.document.impl.diff.SimpleDiff;

public class TestSimpleDiff extends DiffTest {

  public TestSimpleDiff() {
  }

  protected DiffAlgorithm getAlgo() { 
    return new SimpleDiff();
  }  
  
}
