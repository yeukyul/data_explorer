package plugins;

import framework.core.DataPlugin;

import framework.core.ImageMedia;
import framework.core.Media;
import framework.core.Node;
import framework.core.TextMedia;

import java.io.IOException;

import java.net.MalformedURLException;
import java.net.URL;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.apache.commons.io.IOUtils;

import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Data Plugin for IMDb. Uses IMDb web data to get various media of a movie
 * 
 * @author Vanessa
 * @see DataPlugin
 */
public class ImdbData implements DataPlugin {
	@Override
	public String getDomainName() {
		return "IMDb";
	}

	@Override
	public String getPromptString() {
		return "type a movie title";
	}

	@Override
	public Map<String, ?> loadNode(String name) throws NoSuchElementException {
		name = name.replaceAll("\\s+", "_");
		String idUrl = "http://www.omdbapi.com/?t=".concat(name);
		String id = "";
		try {
			String info = IOUtils.toString(new URL(idUrl), Charset.forName("UTF-8"));
			JSONObject infoObject = (JSONObject) JSONValue.parseWithException(info);
			// get the title
			id = infoObject.get("imdbID").toString();
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}

		String url = "http://www.imdb.com/title/" + id;
		Document doc;
		try {
			doc = Jsoup.connect(url).get();
		} catch (MalformedURLException e) {
			throw new NoSuchElementException(idUrl);
		} catch (IOException e) {
			throw new NoSuchElementException(idUrl);
		}
		Map<String, Document> loadedMap = new HashMap<String, Document>();

		loadedMap.put("doc", doc);
		return loadedMap;
	}

	@Override
	public String getTitle(Node node) {
		Document doc = (Document) node.getLoadedMap().get("doc");
		return doc.getElementsByTag("title").text().replaceAll(" - IMDb", "");
	}

	@Override
	public Media getMainMedia(Node node) throws NoSuchElementException {
		Document doc = (Document) node.getLoadedMap().get("doc");
		Elements images = doc.getElementsByClass("poster");
		if (images.size() == 0) {
			return null;
		}
		Element image = images.get(0).child(0);
		String imageSource = image.getElementsByAttributeStarting("src").toString();
		int src1 = image.getElementsByAttributeStarting("src").toString().lastIndexOf("src=\"");
		imageSource = imageSource.substring(src1 + 5);
		int src2 = imageSource.indexOf("\"");
		imageSource = imageSource.substring(0, src2);

		Media media = new ImageMedia("Picture", "", imageSource);
		return media;
	}

	@Override
	public Iterator<Media> getSubMediaIterator(Node node) throws NoSuchElementException {
		Document doc = (Document) node.getLoadedMap().get("doc");
		List<Media> medias = new ArrayList<Media>();

		Elements texts = doc.select("p[itemprop=reviewBody]");
		Element text = texts.get(0);
		String review = text.select("p[itemprop=reviewBody]").text();
		medias.add(new TextMedia("Top Review", "", review));

		Elements photos = doc.select("div[class=mediastrip]");
		Element photoList = photos.get(0);
		Elements photo = photoList.getElementsByAttribute("href");

		for (Element e : photo) {
			Elements images = e.getElementsByTag("img");
			Element image = images.get(0);
			medias.add(new ImageMedia(image.attr("title"), "", image.attr("loadlate")));
		}
		return medias.iterator();
	}

	@Override
	public List<String> getNeighbors(Node node, int numNeighbors) {
		List<String> neighbors = new ArrayList<String>();
		Document doc = (Document) node.getLoadedMap().get("doc");

		Elements movies = doc.getElementsByClass("rec_poster_img");
		for (int i = 0; i < movies.size() / 2; i++) {
			Element movie = movies.get(i);
			String movieName = movie.attr("alt").toString();

			if (!movieName.equals("Genius")) {
				neighbors.add(movieName);
			}
		}

		if (neighbors.size() > numNeighbors) {
			neighbors = neighbors.subList(0, numNeighbors);
		}
		return neighbors;
	}

}
