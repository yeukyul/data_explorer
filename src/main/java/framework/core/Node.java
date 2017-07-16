package framework.core;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Class that represents a node. Each node has a title, main media, a list of
 * zero or more sub-media. The mechanism to fetch the appropriate data is done
 * by <tt>plugin</tt>.
 * 
 * @see {@link plugin}
 * @author sdk1
 *
 */
public class Node {
  private DataPlugin plugin;
  private String name;
  private Map<String, ?> loadedMap;

  /**
   * Node constructor. Ideally, this method should be more expensive than other
   * methods.
   * 
   * @param plugin
   *          the data plugin the framework uses.
   * @param name
   *          the name of the node.
   * @throws NoSuchElementException
   *           if the <tt>name</tt> could not be found in the database.
   */
  public Node(DataPlugin plugin, String name) throws NoSuchElementException {
    this.plugin = plugin;
    this.name = name;
    this.loadedMap = plugin.loadNode(name);
  }

  /**
   * Get the name of the node. This may be different from the title of the node.
   * Title is what user sees on the interface, name is how it is identified in
   * the database.
   * 
   * @return name of the node.
   */
  public String getName() {
    return name;
  }

  /**
   * Get the map that was created when the node was initialized. This field
   * allows {@link DataPlugin} to "cache" the information of the node upon
   * initialization. This might be particularly helpful if data access if
   * expensive-you can access data once in {@link DataPlugin#loadNode(String)}
   * and save the appropriate fields in <tt>loadedMap</tt>. Then plugin's
   * <tt>Get</tt> functions such as {@link DataPlugin#getMainMedia()} can access
   * this map instead of accessing data again.
   * 
   * 
   * @return the map that was created when the node was created.
   */
  public Map<String, ?> getLoadedMap() {
    return this.loadedMap;
  }

  /**
   * Get the title of the Node. This may be different from the name of the node.
   * Title is what user sees on the interface, name is how it is identified in
   * the database.
   * 
   * @return title of the node.
   */
  public String getTitle() {
    return plugin.getTitle(this);
  }

  /**
   * Get the main media of the node.
   * 
   * @see {@link Media}
   * @return main media of the node.
   * @throws NoSuchElementException
   *           if main media does not exist.
   */
  public Media getMainMedia() throws NoSuchElementException {
    return plugin.getMainMedia(this);
  }

  /**
   * Get the iterator that contains a list of sub media of the node.
   * 
   * @see {@link Media}
   * @see {@link MediaIterator}
   * @return the iterator that contains a list of sub-media.
   * @throws NoSuchElementException
   *           if the submedia does not exist.
   */
  public Iterator<Media> getSubMediaIterator() throws NoSuchElementException {
    return plugin.getSubMediaIterator(this);
  }

  /**
   * Get the neighbors of the node. The number of neighbors it returns is not
   * guaranteed to be same as the number client requested for. If the node does
   * not have enough neighbor, the number of neighbors it returns will be less
   * than <tt>numNeighbors</tt>.
   * 
   * @param numNeighbors
   *          number of neighbors needed
   * @return a list of neighbors whose size is less or equal to
   *         <tt>numNeighbors</tt>.
   */
  public List<Node> getNeighbors(int numNeighbors) {
    List<Node> neighbors = new ArrayList<Node>();
    List<String> neighborNames = plugin.getNeighbors(this, numNeighbors);
    for (String name : neighborNames) {
      neighbors.add(new Node(plugin, name));
    }

    return neighbors;
  }

  @Override
  public String toString() {
    StringBuilder str = new StringBuilder();
    String[] categories = new String[] { "Name", "Title", "MainMedia", "SubMedia"};
    for (int i = 0; i < categories.length; i++) {
      str.append(categories[i]);
      str.append(": ");
      try {
        switch (i) {
        case 0:
          str.append(this.getName());
          break;
        case 1:
          str.append(this.getTitle());
          break;
        case 2:
          str.append(this.getMainMedia().getHTML());
          break;
        case 3:
          Iterator<Media> iter = this.getSubMediaIterator();
          while (iter.hasNext()) {
            str.append(iter.next().getHTML());
          }
          break;
        default:
          throw new RuntimeException();
        }
      } catch (NullPointerException e) {
        str.append("None");
      }
      str.append('\n');
    }
    return str.toString();
  }
}
