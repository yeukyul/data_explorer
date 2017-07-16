package framework.core;

/**
 * Implementation of Media interface for video files.
 * 
 * @author sdk1
 *
 * @see Media
 * @see AbstractMedia
 * @author sdk1
 *
 */
public class VideoMedia extends AbstractMedia implements Media {
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
   *          source path to the video file. HTML tag uses this value to
   *          generate the corresponding src path.
   */
  public VideoMedia(String name, String description, String src) {
    super(name, description);
    this.src = src;
  }

  @Override
  public String getHTML() {
    StringBuilder str = new StringBuilder();
    str.append("<video controls>\n");
    str.append("source src=\"");
    str.append(this.src);
    str.append("\">\n");
    str.append("</video>");

    return str.toString();
  }

}
