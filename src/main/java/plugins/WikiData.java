package plugins;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import framework.core.AudioMedia;
import framework.core.DataPlugin;
import framework.core.ImageMedia;
import framework.core.Media;
import framework.core.Node;
import framework.core.VideoMedia;

/**
 * Example {@link framework.core.DataPlugin} that browses Wikipedia database.
 * The node corresponds to a keyword that has a page in wikipedia. For main
 * media, it displays the first image it sees; similarly, the neighbor
 * corresponds to the first hyperlink it sees. 
 * 
 * @author sdk1
 */
public class WikiData implements DataPlugin {
  @Override
  public String getDomainName() {
    return "Wikipedia";
  }

  @Override
  public String getPromptString() {
    return "type in a wikipedia keyword";
  }

  @Override
  public Map<String, ?> loadNode(String name) throws NoSuchElementException {
    Map<String, Document> loadedMap = new HashMap<String, Document>();

    name = name.replace(' ', '_');
    String url = "https://en.wikipedia.org/wiki/".concat(name);
    Document doc;
    try {
      Connection con = Jsoup.connect(url);
      Response res = con.execute();
      String body = res.body();
      if (body.contains("<b>Wikipedia does not have an article with this exact name.</b>")) {
        throw new NoSuchElementException(url);
      }
      doc = con.get();
    } catch (MalformedURLException e) {
      throw new NoSuchElementException(url);
    } catch (IOException e) {
      throw new NoSuchElementException(url);
    }

    loadedMap.put("doc", doc);
    return loadedMap;
  }

  @Override
  public String getTitle(Node node) {
    Document doc = (Document) node.getLoadedMap().get("doc");

    return doc.getElementById("firstHeading").text();
  }

  @Override
  public Media getMainMedia(Node node) {
    Document doc = (Document) node.getLoadedMap().get("doc");
    Elements images = doc.getElementsByClass("image");
    // no images found
    if (images.size() == 0) {
      return null;
    }
    Element image = images.get(0).child(0);

    Media media = new ImageMedia("MainMedia", "This is a main image for ".concat(this.getTitle(node)), "http:".concat(image.attr("src")));
    return media;
  }

  @Override
  public Iterator<Media> getSubMediaIterator(Node node) {
    Document doc = (Document) node.getLoadedMap().get("doc");
    Elements links = doc.getElementsByTag("source");
    List<Media> medias = new ArrayList<Media>();

    for (Element link : links) {
      String src = link.attr("src");
      // Audio
      if (src.endsWith(".ogg")) {
        String[] split = src.split("/");
        String name = split[split.length - 1].replaceAll(".ogg", "");
        medias.add(new AudioMedia(name, "", "https:".concat(src)));
      }
      if (src.endsWith(".mp3")) {
        String[] split = src.split("/");
        String name = split[split.length - 1].replaceAll(".mp3", "");
        medias.add(new AudioMedia(name, "", src));

        // Image
      } else if (src.endsWith(".jpg")) {
        String[] split = src.split("/");
        String name = split[split.length - 1].replaceAll(".jpg", "");
        medias.add(new ImageMedia(name, "", src));
      } else if (src.endsWith(".gif")) {
        String[] split = src.split("/");
        String name = split[split.length - 1].replaceAll(".gif", "");
        medias.add(new ImageMedia(name, "", src));

        // Video
      } else if (src.endsWith(".mp4")) {
        String[] split = src.split("/");
        String name = split[split.length - 1].replaceAll(".mp4", "");
        medias.add(new VideoMedia(name, "", src));
      } else if (src.endsWith(".ogv")) {
        String[] split = src.split("/");
        String name = split[split.length - 1].replaceAll(".ogv", "");
        medias.add(new VideoMedia(name, "", src));
      }

    }

    return medias.iterator();
  }

  @Override
  public List<String> getNeighbors(Node node, int numNeighbors) {
    List<String> neighbors = new ArrayList<String>();

    Document doc = (Document) node.getLoadedMap().get("doc");
    Iterator<Element> pIter = doc.getElementsByTag("p").iterator();

    while (pIter.hasNext()) {
      Element p = pIter.next();
      Iterator<Element> aIter = p.getElementsByTag("a").iterator();
      while (aIter.hasNext()) {
        Element a = aIter.next();
        String[] names = a.attr("href").split("/wiki/");
        if (names.length == 2) {
          try {
            neighbors.add(names[1].replaceAll("_", " "));
            if (neighbors.size() >= numNeighbors) {
              return neighbors;
            }
          } catch (NoSuchElementException e) {

          }
        }
      }
    }

    // not enough neighbors were found
    return neighbors;
  }

}
