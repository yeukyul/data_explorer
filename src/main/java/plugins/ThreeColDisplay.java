package plugins;

import framework.gui.DisplayPlugin;

import java.awt.Color;

/**
 * Display Plugin that uses 1 X 3 layout. It displays the title, main media, and
 * three sub medias horizontally in one row.
 * 
 * @author Vanessa
 * @see DisplayPlugin
 */
public class ThreeColDisplay implements DisplayPlugin {
	@Override
	public String getDisplayName() {
		return "Three Column Display";
	}

	@Override
	public String renderHTML(String rawHTML) {
		return rawHTML;
	}

	@Override
	public int getSubMediaRow() {
		return 1;
	}

	@Override
	public int getSubMediaCol() {
		return 3;
	}

	@Override
	public int getNeighborNum() {
		return 3;
	}

	@Override
	public Color getLightColor() {
		return Color.decode("#BB9398");
	}

	@Override
	public Color getMediumColor() {
		return Color.decode("#746174");
	}

	@Override
	public Color getDarkColor() {
		return Color.decode("#615666");
	}

	@Override
	public Color getTextColor() {
		return Color.decode("#FBEDE9");
	}
}
