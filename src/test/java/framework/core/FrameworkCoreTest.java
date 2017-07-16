package framework.core;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import plugins.WikiData;

/**
 * Test cases for the core part of the framework.
 * 
 * @author sdk1
 *
 */
public class FrameworkCoreTest {
  private FrameworkCore core;
  private WikiData pg;

  /**
   * Test setup
   * 
   * @throws Exception
   *           if exception occurs
   */
  @Before
  public void setUp() throws Exception {
    pg = new WikiData();
    core = new FrameworkCore(pg);
  }

  /**
   * Test get and set current node.
   */
  @Test
  public void testCurrentNode() {
    core.setCurrentNode("Radiohead");
    Node node = core.getCurrentNode();
    assertTrue(node.getName().equalsIgnoreCase("Radiohead"));
  }
}
