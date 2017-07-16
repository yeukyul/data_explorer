package framework.gui;

import java.awt.Color;

/**
 * Display Plugin interace that could customize the display of the program. 
 * @author sdk1
 *
 * @see plugins.SimpleDisplay
 * @see plugins.TwoRowDisplay
 * @see plugins.ThreeColDisplay
 */
public interface DisplayPlugin {
  /**
   * Get the name of the display plugin. This is to show client which display
   * plugin you are using.
   * 
   * @return the name of the display plugin.
   */
  String getDisplayName();

  /**
   * Customize the raw HTML for your display. The function should case on
   * different tag it gets and render each tag accordingly. For example, audio
   * tag and image tag should be rendered differently.
   * 
   * @param rawHTML
   *          the raw HTML string. This string is to be rendered.
   * @return the rendered HTML the new HTML string.
   */
  String renderHTML(String rawHTML);

  /**
   * @return row value of the sub-media grid layout.
   */
  int getSubMediaRow();

  /**
   * @return column value of the sub-media grid layout.
   */
  int getSubMediaCol();

  /**
   * @return the number of neighboring node.
   */
  int getNeighborNum();

  /**
   * @return the customized light color; return null for default color. 
   */
  Color getLightColor();

  /**
   * @return the customized medium color; return null for default color. 
   */
  Color getMediumColor();

  /**
   * @return the customized dark color; return null for default color. 
   */
  Color getDarkColor();

  /**
   * @return the customized text color; return null for default color. 
   */
  Color getTextColor();
}
