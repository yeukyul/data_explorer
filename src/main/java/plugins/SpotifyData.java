
package plugins;

import java.io.IOException;

import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import framework.core.AudioMedia;
import framework.core.DataPlugin;
import framework.core.ImageMedia;
import framework.core.Media;
import framework.core.Node;
import framework.core.TextMedia;

/**
 * Spotify Data Plugin that fetches data from Spotify using Spotify API. First
 * gets the name of the artist, searches for that artist's Spotify ID, then use
 * that ID for the rest of the search.
 * 
 * @author Vanessa
 * @see DataPlugin
 */
public class SpotifyData implements DataPlugin {
	@Override
	public String getDomainName() {
		return "Spotify";
	}

	@Override
	public String getPromptString() {
		return "type an artist";
	}

	@Override
	public Map<String, ?> loadNode(String name) throws NoSuchElementException {
		Map<String, JSONObject> loadedMap = new HashMap<String, JSONObject>();
		String aName = name.replaceAll(" ", "%20");
		String gettingIDUrl = "https://api.spotify.com/v1/search?q=" + aName + "&type=artist&limit=1";
		String artistID = "";
		try {
			String idInfo = IOUtils.toString(new URL(gettingIDUrl), Charset.forName("UTF-8"));
			JSONObject idObject = (JSONObject) JSONValue.parseWithException(idInfo);
			JSONObject artistA = (JSONObject) idObject.get("artists");
			JSONArray array = (JSONArray) artistA.get("items");
			Object currentArtist = array.get(0);
			artistID = ((JSONObject) currentArtist).get("id").toString();
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}

		String url = "https://api.spotify.com/v1/artists/" + artistID;

		try {
			String info = IOUtils.toString(new URL(url), Charset.forName("UTF-8"));
			JSONObject infoObject = (JSONObject) JSONValue.parseWithException(info);
			loadedMap.put("obj", infoObject);
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
		return loadedMap;
	}

	@Override
	public String getTitle(Node node) {
		JSONObject object = (JSONObject) node.getLoadedMap().get("obj");
		return object.get("name").toString();
	}

	@Override
	public Media getMainMedia(Node node) {
		JSONObject object = (JSONObject) node.getLoadedMap().get("obj");
		JSONArray images = (JSONArray) object.get("images");
		Media main;
		if (images.size() != 0) {
			Object image = images.get(0);
			String imageURL = ((JSONObject) image).get("url").toString();
			imageURL = imageURL.replace("\\", "");
			main = new ImageMedia("Image", "", imageURL);
			return main;
		} else {
			return null;
		}
	}

	@Override
	public Iterator<Media> getSubMediaIterator(Node node) {
		JSONObject object = (JSONObject) node.getLoadedMap().get("obj");

		List<Media> medias = new ArrayList<Media>();
		// genres
		String genres = object.get("genres").toString();
		genres = genres.replace("\"", " ");
		genres = genres.replace("[", "");
		genres = genres.replace("]", "");
		medias.add(new TextMedia(object.get("name").toString(), "Genres", genres));

		// tracks
		String url = "https://api.spotify.com/v1/artists/" + object.get("id").toString() + "/top-tracks?country=US";
		try {
			String info = IOUtils.toString(new URL(url), Charset.forName("UTF-8"));
			JSONObject infoObject = (JSONObject) JSONValue.parseWithException(info);
			JSONArray tracks = (JSONArray) infoObject.get("tracks");
			for (Object albums : tracks) {
				Object track = ((JSONObject) albums).get("preview_url");
				if (track != null) {
					medias.add(new AudioMedia("Track Preview", "", track.toString()));
				}
			}
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}

		// images
		JSONArray images = (JSONArray) object.get("images");
		if (images.size() != 0) {
			images.remove(images.get(0));
		}
		for (Object image : images) {
			String imageURL = ((JSONObject) image).get("url").toString();
			imageURL = imageURL.replace("\\", "");
			medias.add(new ImageMedia("Image", "", imageURL));
		}

		return medias.iterator();
	}

	@Override
	public List<String> getNeighbors(Node node, int numNeighbors) {
		List<String> neighbors = new ArrayList<String>();
		JSONObject object = (JSONObject) node.getLoadedMap().get("obj");
		String id = object.get("id").toString();

		String url = "https://api.spotify.com/v1/artists/" + id + "/related-artists/";

		JSONArray artists;

		try {
			String info = IOUtils.toString(new URL(url), Charset.forName("UTF-8"));
			JSONObject infoObject = (JSONObject) JSONValue.parseWithException(info);
			artists = (JSONArray) infoObject.get("artists");

			for (int i = 0; i < artists.size() & i < numNeighbors; i++) {
				Object artist = artists.get(i);
				String artistName = ((JSONObject) artist).get("id").toString();
				String artistUrl = "https://api.spotify.com/v1/artists/" + artistName;

				try {
					String artistInfo = IOUtils.toString(new URL(artistUrl), Charset.forName("UTF-8"));
					JSONObject artistObject = (JSONObject) JSONValue.parseWithException(artistInfo);
					String actualName = artistObject.get("name").toString();
					neighbors.add(actualName);
				} catch (IOException | ParseException e) {
					e.printStackTrace();
				}
			}

		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}

		if (neighbors.size() > numNeighbors) {
			neighbors = neighbors.subList(0, numNeighbors);
		}
		return neighbors;
	}

}
