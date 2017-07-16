package framework.core;

/**
 * Customized implementation of media. Client can customized the HTML this media
 * renders into.
 * 
 * @author sdk1
 *
 */
public class CustomMedia extends AbstractMedia implements Media {
  private String html;

  /**
   * Constructor for CustomMedia.
   * 
   * @param name
   *          name of the media. Can be null or empty string if it doesn't
   *          exist.
   * @param description
   *          description of the media. Can be null or empty string if it
   *          doesn't exist.
   * @param customHTML
   *          the customized HTML tag that this media is rendered into.
   */
  public CustomMedia(String name, String description, String customHTML) {
    super(name, description);
    this.html = customHTML;
  }

  @Override
  public String getHTML() {
    return html;
  }
}
