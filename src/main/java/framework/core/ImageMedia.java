package framework.core;

/**
 * Implementation of Media interface for image files.
 * 
 * @author sdk1
 *
 * @see Media
 * @see AbstractMedia
 */
public class ImageMedia extends AbstractMedia implements Media {
  private String src;

  /**
   * Constructor for ImageMedia
   * 
   * @param name
   *          name of the media. Can be null or empty string if it doesn't
   *          exist.
   * @param description
   *          description of the media. Can be null or empty string if it
   *          doesn't exist.
   * @param src
   *          source path to the image file. HTML tag uses this value to
   *          generate the corresponding src path.
   */
  public ImageMedia(String name, String description, String src) {
    super(name, description);
    this.src = src;
  }

  @Override
  public String getHTML() {
    StringBuilder str = new StringBuilder();
    str.append("<img src=\"");
    str.append(this.src);
    str.append("\">");

    return str.toString();
  }
}
