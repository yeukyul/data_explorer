package plugins;

import framework.gui.DisplayPlugin;

import java.awt.Color;

/**
 * Display Plugin that uses 2 X 1 layout. It displays the title, main media, and
 * two sub medias vertically in one column.
 * 
 * @author Vanessa
 *
 * @see DisplayPlugin
 */
public class TwoRowDisplay implements DisplayPlugin {

	@Override
	public String getDisplayName() {
		return "Two Row Display";
	}

	@Override
	public String renderHTML(String rawHTML) {
		// make the audio auto play and repeat
		if (rawHTML.charAt(1) == 'a') {
			String addStyle = " loop autoplay>";
			String[] splitString = rawHTML.split(">");
			String modified = splitString[0];
			modified = modified + addStyle + splitString[1] + ">" + splitString[2] + ">";
			return modified;
		}
		if (rawHTML.charAt(1) == 'i') {
			String addStyle = "<style>img {border-radius: 60%;}</style>\n";
			String newHTML = addStyle + rawHTML;
			return newHTML;
		}
		return rawHTML;
	}

	@Override
	public int getSubMediaRow() {
		return 2;
	}

	@Override
	public int getSubMediaCol() {
		return 1;
	}

	@Override
	public int getNeighborNum() {
		return 4;
	}

	@Override
	public Color getLightColor() {
		return Color.decode("#CF621A");
	}

	@Override
	public Color getMediumColor() {
		return Color.decode("#489CC9");
	}

	@Override
	public Color getDarkColor() {
		return Color.decode("#A32E8E");
	}

	@Override
	public Color getTextColor() {
		return Color.decode("#F6AF27");
	}
}
