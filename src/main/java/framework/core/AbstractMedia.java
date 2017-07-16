package framework.core;

/**
 * Abstract implementation for media class. All the builtin media implementation
 * is the extension of the abstract media implementation. Client should NOT need
 * to work with this class; they should either use builtin media implementations
 * or {@link CustomMedia} to generate customized media implementation.
 * 
 * @author sdk1
 * 
 * @see Media
 * @see AudioMedia
 * @see CustomMedia
 * @see ImageMedia
 * @see TextMedia
 * @see VideoMedia
 */
public abstract class AbstractMedia implements Media {
  private String name;
  private String description;

  /**
   * Constructor for abstract media.
   * 
   * @param name
   *          name of the media.
   * @param description
   *          description of the media.
   */
  public AbstractMedia(String name, String description) {
    this.name = name;
    this.description = description;
  }

  @Override
  public String getTitle() {
    return name;
  }

  @Override
  public String getDescription() {
    return this.description;
  }
}
