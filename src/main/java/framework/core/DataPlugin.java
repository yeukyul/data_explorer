package framework.core;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Plugin for the data part of the program. The two main things that needs to be
 * specified is how to get the information from one data point and how to find
 * relevant data points from one data point. The methods corresponding to the
 * former are {@link #getTitle(Node)}, {@link #getMainMedia(Node)}, and
 * {@link #getSubMediaIterator(Node)}; The method corresponding to the later is
 * {@link #getNeighbors(Node, int)}.
 * <p>
 * If accessing a data point is expensive, the program should find all the
 * information on the first access, then for later methods, return the
 * information found during the first access. When the {@link Node} is
 * initialized, it calls {@link #loadNode(String)}, which is equivalent to the
 * "first access" described above. The return value of this function will be
 * stored inside Node class in the field called <tt>loadedMap</tt>. Subsequent
 * <tt>getX</tt> functions should use <tt>loadedMap</tt> instead of accessing
 * database again.
 * 
 * @author sdk1
 *
 * @see Node
 * @see plugins.ImdbData
 * @see plugins.SpotifyData
 * @see plugins.WikiData
 */
public interface DataPlugin {
  /**
   * @return the name of the domain
   * @see plugins.WikiData#getDomainName()
   */
  String getDomainName();

  /**
   * @return the prompt string that is displayed on the search bar. It should be
   *         relevant to the database you are using. ex) "Type in an artist
   *         name."
   * @see plugins.WikiData#getPromptString()
   */
  String getPromptString();

  /**
   * Function that is called during initialization of the {@link Node}. It is
   * recommend to make any expensive call in this function and store the
   * necessary information in the returned map. This returned map will can be
   * accessed with {@link Node#getLoadedMap()}.
   * <p>
   * For example, if the data is accessed from the web, client should get the
   * information from the web in this function; other subsequent function call
   * to this node should be accessed with {@link Node#getLoadedMap()}. See
   * {@link WikiData} for example.
   * 
   * @param name
   *          name of the node.
   * @return a map containing information. This map can be retrieved through
   *         {@link Node#getLoadedMap()}.
   * @throws NoSuchElementException
   *           if the node does not exist in the database.
   */
  Map<String, ?> loadNode(String name) throws NoSuchElementException;

  /**
   * @param node
   *          the query node.
   * @return the title of the node. The neighbor nodes will display this value.
   */
  String getTitle(Node node);

  /**
   * @param node
   *          the query node.
   * @return the main media representing this node.
   */
  Media getMainMedia(Node node);

  /**
   * Get a submedia iterator that has access to all the submedia of a node. If
   * the number of submedia is short, user can simply get all the submedia and
   * convert it from a list to a iterator. However, if the number of sub media
   * is huge, it should implement a class that extends <tt>Iterator</tt>, and
   * define a way to access the next submedia in the <tt>next</tt> method. This
   * allows the framework to load submedia when it is necessary, instead of
   * loading all of them at the same time.
   * 
   * @param node
   *          the query node.
   * @return an iterator of submedia under this node.
   */
  Iterator<Media> getSubMediaIterator(Node node);

  /**
   * Get the neighbors of the node. The number of neighbors is not guaranteed to
   * be equal to <tt>numNeighbors</tt> if there are not enough neighbors.
   * 
   * @param node
   *          the query node.
   * @param numNeighbors
   *          the number of neighbors needed.
   * @return a list of neighbors of <tt>node</tt> whose size is at most
   *         <tt>numNeighbors</tt>.
   */
  List<String> getNeighbors(Node node, int numNeighbors);
}
