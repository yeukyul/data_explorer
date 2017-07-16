package framework.core;

import static org.junit.Assert.*;

import java.util.Iterator;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

import plugins.WikiData;

/**
 * Test case for {@link Node}
 * 
 * @author sdk1
 * 
 */
public class NodeTest {
  Node node;

  /**
   * Test setup
   * 
   * @throws Exception
   *           if exception happens
   */
  @Before
  public void setUp() throws Exception {
    node = new Node(new WikiData(), "Radiohead");
  }

  @Test
  public void testGetLoadedMap() {
    Map<String, ?> map = node.getLoadedMap();
    assertEquals(1, map.size());
    assertNotNull(map.get("doc"));
  }

  @Test
  public void testGetMainMedia() {
    Media media = node.getMainMedia();
    assertEquals("MainMedia", media.getTitle());
    assertEquals("This is a main image for Radiohead", media.getDescription());
    Document doc = Jsoup.parse(media.getHTML());
    Elements elems = doc.getElementsByTag("img");
    assertEquals(1, elems.size());
    assertEquals("img", elems.get(0).tag().toString());
  }

  @Test
  public void testGetSubMedia() {
    Iterator<Media> medias = node.getSubMediaIterator();
    while (medias.hasNext()) {
      Media media = medias.next();
      Document doc = Jsoup.parse(media.getHTML());
      Elements elems = doc.getAllElements();
      assertNotSame(0, elems.size());
    }
  }
  
  @Test
  public void testToString() {
    assertNotNull(node.toString());
  }
}
