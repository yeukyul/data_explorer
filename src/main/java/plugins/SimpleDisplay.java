package plugins;

import framework.gui.DisplayPlugin;

import java.awt.Color;

public class SimpleDisplay implements DisplayPlugin {

	@Override
	public String getDisplayName() {
		return "SimpleDisplay";
	}

	@Override
	public int getSubMediaRow() {
		return 0;
	}

	@Override
	public int getSubMediaCol() {
		return 0;
	}

	@Override
	public String renderHTML(String rawHtml) {
		if (rawHtml.charAt(1) == 'i') {
			String addStyle = "<style>img {border-radius: 60%;}</style>\n";
			String newHtml = addStyle + rawHtml;
			return newHtml;
		}
		return rawHtml;
	}

	@Override
	public int getNeighborNum() {
		return 3;
	}

	@Override
	public Color getLightColor() {
		return null;
	}

	@Override
	public Color getMediumColor() {
		return null;
	}

	@Override
	public Color getDarkColor() {
		return null;
	}

	@Override
	public Color getTextColor() {
		return null;
	}
}