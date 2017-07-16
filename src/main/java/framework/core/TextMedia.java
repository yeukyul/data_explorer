package framework.core;

/**
 * Implementation of Media interface for text files.
 * 
 * @author sdk1
 *
 * @see Media
 * @see AbstractMedia
 *
 */
public class TextMedia extends AbstractMedia implements Media {
  private String html; 
  
  /**
   * Constructor for text.
   * 
   * @param name
   *          name of the media. Can be null or empty string if it doesn't
   *          exist.
   * @param description
   *          description of the media. Can be null or empty string if it
   *          doesn't exist.
   * @param text
   *          the text content of the media.
   */
  public TextMedia(String name, String description, String text) {
    super(name, description);
    this.html = "<p>".concat(text).concat("</p>");
  }

  @Override
  public String getHTML() {
    return this.html;
  }
}
