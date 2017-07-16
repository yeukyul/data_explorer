package framework.core;

import java.util.NoSuchElementException;

/**
 * Framework for the core part of the data explorer.
 * 
 * @see {@link Node}
 * @author sdk1
 *
 */
public class FrameworkCore {
  private DataPlugin dataPlugin;
  private Node currentNode;

  /**
   * Constructor for frameworkcore. This should not depend on data plugin.
   * 
   * @param dp
   *          dataplugin.
   */
  public FrameworkCore(DataPlugin dp) {
    this.dataPlugin = dp;
  }

  /**
   * @return get the application name from domain name of the data plugin.
   */
  public String getAppName() {
    return this.dataPlugin.getDomainName();
  }

  /**
   * @return get the prompt string that is asked to the user.
   */
  public String getPromptString() {
    return this.dataPlugin.getPromptString();
  }

  /**
   * Get the current node of the graph.
   * 
   * @see {@link Node}
   * @return the current node. If no current node exists, return <tt>NULL</tt>.
   */
  public Node getCurrentNode() {
    return this.currentNode;
  }

  /**
   * Set the current node of the graph. The next node MUST be the neighbor of
   * the current node. If this is the first time node is being created, you need
   * to call {@link #startGraph(String)} instead.
   * 
   * @param nodeName
   *          the name of the new current node.
   * @return the new current node.
   * @throws NoSuchElementException
   *           if the node can't be found on the database
   * 
   */
  public Node setCurrentNode(String nodeName) throws NoSuchElementException {
    this.currentNode = new Node(dataPlugin, nodeName);
    return currentNode;
  }

  @Override
  public String toString() {
    StringBuilder str = new StringBuilder();
    str.append(this.currentNode);
    return str.toString();
  }
}
