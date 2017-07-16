package framework.core;

/**
 * Media class for representing media (Main Media or sub-media) of a node. A
 * media contains a title, description, and the corresponding html. The title
 * and description does not need to be set (could be NULL or empty string), but
 * HTML tag must exist for the framework to display onto the window (This means
 * that, DisplayPlugin can choose to not display title/description). The html
 * tag can be simple tag, for those will be rendered in
 * {@link framework.gui.DisplayPlugin#renderHTML(String)}.
 * <p>
 * There are many built-in implementations of Media but client can generate a
 * customized media implementation with {@link CustomMedia}. Client should not
 * need to implement Media interface themselves.
 * 
 * @author sdk1
 * 
 * @see AbstractMedia
 * @see AudioMedia
 * @see CustomMedia
 * @see ImageMedia
 * @see TextMedia
 * @see VideoMedia
 */
public interface Media {
  /**
   * @return the title of the media. Can be null or empty string if it doesn't
   *         exist.
   */
  String getTitle();

  /**
   * @return the description of the media. Can be null or empty string if it
   *         doesn't exist.
   */
  String getDescription();

  /**
   * @return the corresponding HTML tag. This tag should be as simple as
   *         possible for {@link framework.gui.DisplayPlugin#renderHTML(String)}
   *         to render it.
   */
  String getHTML();
}
