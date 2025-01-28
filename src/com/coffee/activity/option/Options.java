package com.coffee.activity.option;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import com.coffee.inputs.buttons.Button;
import com.coffee.activity.Activity;
import com.coffee.activity.Menu;
import com.coffee.ui.command.Receiver;
import com.coffee.main.Engine;
import com.coffee.main.tools.Responsive;

public class Options implements Activity, Receiver {

    private Map<String, Button> buttons;

	private final Ranger Music;
	private final Ranger Volume;
	private final Clicker FullScreen;
	private final Ranger Resolution;
	private final Paletting Pallet;
	private final Clicker Setter;

	public Options() {
        Responsive center = Responsive.createPoint(null, 50, 40);
		int marge = Engine.SCALE * 7;
		Music = buildConfigRanger("Music", "[" + Engine.SETTINGS.getInt("MUSIC") + "%]", center,0, -marge);
		Volume = buildConfigRanger("Volume", "[" + Engine.SETTINGS.getInt("VOLUME") + "%]", Music.getResponsive(),-marge, 0);
		FullScreen = buildConfigClicker("Full Screen","[" + (Engine.SETTINGS.getBool("FULLSCREEN") ? "ON" : "OFF") + "]", Music.getResponsive(), marge, 0);
		Pallet = buildConfigPaletting("Pallet", Engine.SETTINGS.getInt("PALLET_INDEX"), center, 0, marge/2);
		int index_resolution = Engine.SETTINGS.getInt("RESOLUTION_INDEX");
		String resText = "[" + Engine.resolutions[index_resolution][0] + " / " + Engine.resolutions[index_resolution][1] + "]";
		Resolution = buildConfigRanger("Resolution", resText, Pallet.getResponsive(), -marge, 0);
		Setter = buildConfigClicker("Save new values", "Save and back", Pallet.getResponsive(), marge, 0);
	}

	private Ranger buildConfigRanger(String name, String text, Responsive parent, int pref_x, int pref_y) {
		Responsive point = Responsive.createPoint(parent, pref_x, pref_y);
        return new Ranger(point, name, text);
	}

	private Clicker buildConfigClicker(String name, String text, Responsive parent, int pref_x, int pref_y) {
		Responsive point = Responsive.createPoint(parent, pref_x, pref_y);
		return new Clicker(point, name, text);
	}

	private Paletting buildConfigPaletting(String name, int pallet, Responsive parent, int pref_x, int pref_y) {
		Responsive point = Responsive.createPoint(parent, pref_x, pref_y);
		return new Paletting(point, name, pallet);
	}

	@Override
	public void enter() {
		buttons = new HashMap<>();
		Engine.UI.addOption("Back", () -> {
			Engine.setActivity(new Menu());
		});
	}
	
	@Override
	public void tick() {
		Music.tick();
		Volume.tick();
		FullScreen.tick();
		Resolution.tick();
		Pallet.tick();
		Setter.tick();
		int muc = Engine.SETTINGS.getInt("MUSIC");
		Music.plus(() -> {
			int newMusic = muc + 5;
			if(newMusic <= 100) {
				Engine.SETTINGS.change("MUSIC", newMusic);
				Music.setText("[" + newMusic + "%]");
			}
		}).minus(() -> {
			int newMusic = muc - 5;
			if(newMusic >= 0) {
				Engine.SETTINGS.change("MUSIC", newMusic);
				Music.setText("[" + newMusic+ "%]");
			}
		});

		int vol = Engine.SETTINGS.getInt("VOLUME");
		Volume.plus(() -> {
			int newVol = vol + 5;
			if(newVol <= 200) {
				Engine.SETTINGS.change("VOLUME", newVol);
				Volume.setText("[" + newVol + "%]");
			}
		}).minus(() -> {
			int newVol = vol - 5;
			if(newVol >= 0) {
				Engine.SETTINGS.change("VOLUME", newVol);
				Volume.setText("[" + newVol + "%]");
			}
		});

		FullScreen.click(() -> {
			boolean fullscreen = Engine.SETTINGS.getBool("FULLSCREEN");
			Engine.SETTINGS.change("FULLSCREEN", !fullscreen);
			String fs = !fullscreen ? "ON" : "OFF";
			FullScreen.setText("[" + fs + "]");
		});
		int indexRes = Engine.SETTINGS.getInt("RESOLUTION_INDEX");
		Resolution.plus(() -> {
			int newIndexRes = indexRes + 1;
			if(newIndexRes < Engine.resolutions.length) {
				Engine.SETTINGS.change("RESOLUTION_INDEX", newIndexRes);
				Resolution.setText("[" + Engine.resolutions[newIndexRes][0] + " / " + Engine.resolutions[indexRes][1] + "]");
			}
		}).minus(() -> {
			int newIndexRes = indexRes - 1;
			if(newIndexRes >= 0) {
				Engine.SETTINGS.change("RESOLUTION_INDEX", newIndexRes);
				Resolution.setText("[" + Engine.resolutions[newIndexRes][0] + " / " + Engine.resolutions[indexRes][1] + "]");
			}
		});

		Setter.click(() -> {

			Engine.SETTINGS.update();
			Engine.restart();
		});
	}

	@Override
	public void render(Graphics2D g) {
		buttons.forEach((name, button) -> button.render(g));
		Music.render(g);
		Volume.render(g);
		FullScreen.render(g);
		Pallet.render(g);
		Resolution.render(g);
		Setter.render(g);
	}

	@Override
	public String giveCommand(String[] keys) {
		String call_back = "I didn't understand";
		if(keys[0].equalsIgnoreCase("options")) {
			for(String s : Engine.SETTINGS.keys()) {
				Engine.UI.getConsole().print(s, true);
			}
			return "";
		}
		return call_back;
	}

	@Override
	public void dispose() {
		buttons.clear();
		Engine.UI.clearOptions();
	}

}
