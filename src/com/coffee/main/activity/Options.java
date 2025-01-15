package com.coffee.main.activity;

import java.awt.Font;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.Map;

import com.coffee.Inputs.Button.Button;
import com.coffee.ui.command.Receiver;
import com.coffee.graphics.FontG;
import com.coffee.main.Engine;
import com.coffee.main.Theme;
import com.coffee.main.tools.Responsive;

public class Options implements Activity, Receiver {
	
	private final Responsive center;
	
	private Responsive music;
	private Responsive volume;
	private Responsive colors;
	private Responsive fullscreen;
	private Responsive resolution;

	private Map<String, Button> buttons;
	
	public Options() {
		center = Responsive.createPoint(null, 50, 40);
	}

	@Override
	public void enter() {
		buttons = new HashMap<>();

		music = Responsive.createPoint(center, 0, -50*Engine.SCALE);
		buttons.put("plus_music", new Button("->", -12*Engine.SCALE, 0, music, 8));
		buttons.put("minus_music", new Button("<-", 12*Engine.SCALE, 0, music, 8));

		volume = Responsive.createPoint(music, 0, 25*Engine.SCALE);
		buttons.put("plus_volum", new Button("->", -12*Engine.SCALE, 0, volume, 8));
		buttons.put("minus_volum", new Button("<-", 12*Engine.SCALE, 0, volume, 8));
		
		colors = Responsive.createPoint(volume, 0, 30*Engine.SCALE);
		buttons.put("plus_color", new Button("->", -12*Engine.SCALE, 0, colors, 8));
		buttons.put("minus_color", new Button("<-", 12*Engine.SCALE, 0, colors, 8));
		
		fullscreen = Responsive.createPoint(colors, 0, 30*Engine.SCALE);
		buttons.put("bool_fullscreen", new Button("" + Engine.CONFIG.getBool("FULLSCREEN"), 0, 0, fullscreen, 8));
		
		resolution = Responsive.createPoint(fullscreen, 0, 30*Engine.SCALE);
		buttons.put("plus_resolution", new Button("->", 12*Engine.SCALE, 0, resolution, 8));
		buttons.put("minus_resolution", new Button("<-", -12*Engine.SCALE, 0, resolution, 8));

		Responsive setter = Responsive.createPoint(resolution, 0, 20*Engine.SCALE);
		buttons.put("setter", new Button("Set", 0, 0, setter, 8));
		Engine.UI.addOption("Back", () -> {
			Engine.setActivity(new Menu());
		});
	}
	
	@Override
	public void tick() {
		if(buttons == null || buttons.isEmpty())
			return;
		int music = Engine.CONFIG.getInt("MUSIC");
		if(buttons.get("plus_music").function() && (int) music + 5 <= 100)
			Engine.CONFIG.change("MUSIC", music + 5);
		if(buttons.get("minus_music").function() && music - 5 >= 0)
			Engine.CONFIG.change("MUSIC", music - 5);
		int volume = Engine.CONFIG.getInt("VOLUME");
		if(buttons.get("plus_volum").function() && volume + 5 <= 100)
			Engine.CONFIG.change("VOLUME", volume + 5);
		if(buttons.get("minus_volum").function() && volume - 5 >= 0)
			Engine.CONFIG.change("VOLUME", volume - 5);
		int index_pallet = Engine.CONFIG.getInt("PALLET_INDEX");
		if(buttons.get("plus_color").function() && index_pallet + 1 <= Engine.THEME.size()-1)
			Engine.CONFIG.change("PALLET_INDEX", index_pallet + 1);
		if(buttons.get("minus_color").function() && index_pallet - 1 >= 0)
			Engine.CONFIG.change("PALLET_INDEX", index_pallet - 1);
		boolean fs = Engine.CONFIG.getBool("FULLSCREEN");
		if(buttons.get("bool_fullscreen").function()) {
			Engine.CONFIG.change("FULLSCREEN", !fs);
			buttons.put("bool_fullscreen", new Button("" + !fs, 0, 0, fullscreen, 8));
		}
		int index_res = Engine.CONFIG.getInt("RESOLUTION_INDEX");
		if(buttons.get("plus_resolution").function() && index_res + 1 <= Engine.resolutions.length-1)
			Engine.CONFIG.change("RESOLUTION_INDEX", index_res + 1);
		if(buttons.get("minus_resolution").function() && index_res - 1 >= 0)
			Engine.CONFIG.change("RESOLUTION_INDEX", index_res - 1);
		if(buttons.get("setter").function()) {
			Engine.CONFIG.update();
			Engine.restart();
		}
	}

	@Override
	public void render(Graphics2D g) {
		buttons.forEach((name, button) -> button.render(g));
		render_music(g);
		render_volum(g);
		render_colors(g);
		render_fullscreen(g);
		render_resolution(g);
	}

	private void render_music(Graphics2D g) {
		if(!buttons.containsKey("plus_music") || !buttons.containsKey("minus_music"))
			return;
		String value = "Music: " + Engine.CONFIG.getInt("MUSIC");
		Font font = FontG.font(12*Engine.SCALE);
		int wF = FontG.getWidth(value, font);
		int hF = FontG.getHeight(value, font);
		int x = music.getPosition().x - wF/2;
		int y = music.getPosition().y + hF/2;
		buttons.get("plus_music").getResponsive().setRelative(wF/2 + 2*Engine.SCALE, 0);
		buttons.get("minus_music").getResponsive().setRelative(-wF/2 - 2*Engine.SCALE, 0);
		g.setFont(font);
		g.setColor(Theme.Tertiary);
		g.drawString(value, x + Engine.SCALE, y + Engine.SCALE);
		g.setColor(Theme.Primary);
		g.drawString(value, x, y);
	}
	
	private void render_volum(Graphics2D g) {
		if(!buttons.containsKey("plus_volum") || !buttons.containsKey("minus_volum"))
			return;
		String value = "Volum: " + Engine.CONFIG.getInt("VOLUME");
		Font font = FontG.font(12*Engine.SCALE);
		int wF = FontG.getWidth(value, font);
		int hF = FontG.getHeight(value, font);
		int x = volume.getPosition().x - wF/2;
		int y = volume.getPosition().y + hF/2;
		buttons.get("plus_volum").getResponsive().setRelative(wF/2 + 2*Engine.SCALE, 0);
		buttons.get("minus_volum").getResponsive().setRelative(-wF/2 - 2*Engine.SCALE, 0);
		g.setFont(font);
		g.setColor(Theme.Tertiary);
		g.drawString(value, x + Engine.SCALE, y + Engine.SCALE);
		g.setColor(Theme.Primary);
		g.drawString(value, x, y);
	}
	
	private void render_colors(Graphics2D g) {
		if(!buttons.containsKey("plus_color") || !buttons.containsKey("minus_color"))
			return;
		String value = "Colors:";
		Font font = FontG.font(12*Engine.SCALE);
		int wF = FontG.getWidth(value, font);
		int hF = FontG.getHeight(value, font);
		int xS = colors.getPosition().x - wF/2;
		int yS = colors.getPosition().y - hF;
		g.setFont(font);
		g.setColor(Theme.Tertiary);
		g.drawString(value, xS + Engine.SCALE, yS + Engine.SCALE);
		g.setColor(Theme.Primary);
		g.drawString(value, xS, yS);
		int size = 12*Engine.SCALE;
		int wB = Engine.THEME.getPallet().length * size;
		int xB = colors.getPosition().x - wB/2;
		int yB = colors.getPosition().y - size/2;
		for(int i = 0; i < Engine.THEME.getPallet().length; i++) {
			g.setColor(Engine.THEME.getPallet()[i]);
			g.fillRect(xB + i*size, yB, size, size);
		}
		buttons.get("plus_color").getResponsive().setRelative(wB/2 + 2*Engine.SCALE, 0);
		buttons.get("minus_color").getResponsive().setRelative(-wB/2 - 2*Engine.SCALE, 0);
	}
	
	private void render_fullscreen(Graphics2D g) {
		String value = "Fullscreen:";
		Font font = FontG.font(12*Engine.SCALE);
		int wF = FontG.getWidth(value, font);
		int hF = FontG.getHeight(value, font);
		int xS = fullscreen.getPosition().x - wF/2;
		int yS = fullscreen.getPosition().y - hF;
		g.setFont(font);
		g.setColor(Theme.Tertiary);
		g.drawString(value, xS + Engine.SCALE, yS + Engine.SCALE);
		g.setColor(Theme.Primary);
		g.drawString(value, xS, yS);
	}
	
	private void render_resolution(Graphics2D g) {
		if(!buttons.containsKey("plus_resolution") || !buttons.containsKey("minus_resolution"))
			return;
		String value = "Resolution:";
		Font font = FontG.font(12*Engine.SCALE);
		int wF = FontG.getWidth(value, font);
		int hF = FontG.getHeight(value, font);
		int xS = resolution.getPosition().x - wF/2;
		int yS = resolution.getPosition().y - hF;
		g.setFont(font);
		g.setColor(Theme.Tertiary);
		g.drawString(value, xS + Engine.SCALE, yS + Engine.SCALE);
		g.setColor(Theme.Primary);
		g.drawString(value, xS, yS);
		int index_res = (int) Engine.CONFIG.getInt("RESOLUTION_INDEX");
		String value_res = Engine.resolutions[index_res][0] + " / " + Engine.resolutions[index_res][1];
		int wF_res = FontG.getWidth(value_res, font);
		int hF_res = FontG.getHeight(value_res, font);
		int xS_res = resolution.getPosition().x - wF_res/2;
		int yS_res = resolution.getPosition().y + hF_res/2;
		g.setFont(font);
		g.setColor(Theme.Tertiary);
		g.drawString(value_res, xS_res + Engine.SCALE, yS_res + Engine.SCALE);
		g.setColor(Theme.Primary);
		g.drawString(value_res, xS_res, yS_res);
		buttons.get("plus_resolution").getResponsive().setRelative(wF_res/2 + 2*Engine.SCALE, 0);
		buttons.get("minus_resolution").getResponsive().setRelative(-wF_res/2 - 2*Engine.SCALE, 0);
	}
	
	@Override
	public String giveCommand(String[] keys) {
		String call_back = "I didn't understand";
		
		return call_back;
	}

	@Override
	public void dispose() {
		buttons.clear();
		Engine.UI.clearOptions();
	}

}
