package framework.core;

/**
 * Implementation of Media interface for audio files.
 * 
 * @author sdk1
 *
 * @see Media
 * @see AbstractMedia
 */
public class AudioMedia extends AbstractMedia implements Media {
  private String src;

  /**
   * Constructor for AudioMedia
   * 
   * @param name
   *          name of the media. Can be null or empty string if it doesn't
   *          exist.
   * @param description
   *          description of the media. Can be null or empty string if it
   *          doesn't exist.
   * @param src
   *          source path to the audio file. HTML tag uses this value to
   *          generate the corresponding src path.
   */
  public AudioMedia(String name, String description, String src) {
    super(name, description);
    this.src = src;
  }

  @Override
  public String getHTML() {
    StringBuilder str = new StringBuilder();
    str.append("<audio controls='controls' preload='metadata'>\n");
    str.append("<source src='");
    str.append(this.src);
    str.append("' />\n");
    str.append("</audio>");

    return str.toString();
  }
}
