package com.github.diegopacheco.projectc;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import com.github.diegopacheco.projectc.App;

/**
 * Tests for correct dependency retrieval with maven rules.
 */
public class TestApp {

  @Test
  public void testCompare() throws Exception {
    App app = new App();
    assertEquals("should return 0 when both numbers are equal", 0, app.compare());
  }

}
