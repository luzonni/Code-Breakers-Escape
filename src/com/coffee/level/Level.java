package com.coffee.level;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.coffee.main.Theme;
import com.coffee.objects.entity.EntityTag;
import com.coffee.objects.tiles.TileTag;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.coffee.Inputs.Mouse;
import com.coffee.Inputs.Mouse_Button;
import com.coffee.command.Commands;
import com.coffee.command.Receiver;
import com.coffee.ui.creator.DrawableBox;
import com.coffee.exceptions.Dead;
import com.coffee.graphics.FontG;
import com.coffee.items.Item;
import com.coffee.main.Engine;
import com.coffee.main.activity.Game;
import com.coffee.objects.Objects;
import com.coffee.objects.Variables;
import com.coffee.objects.entity.Entity;
import com.coffee.objects.particles.Particle;
import com.coffee.objects.tiles.Tile;

public class Level implements Receiver {
	
	private List<Commands> list_keys;
	private final JSONObject level;
	
	protected Objects selected;
	private Tile[] map;
	private final List<Entity> entities;
	private final List<Particle> particles;
	private BufferedImage picture;
	private Rectangle bounds;
	
	public Level(String level_name) {
		entities = new ArrayList<Entity>();
		particles = new ArrayList<Particle>();
		level = getLevel(level_name);
	}
	
	public Level(File file) {
		entities = new ArrayList<Entity>();
		particles = new ArrayList<Particle>();
		level = getLevel(file);
	}
	
	public Level(JSONObject level_name) {
		level = level_name;
		entities = new ArrayList<Entity>();
		particles = new ArrayList<Particle>();
	}
	
	public static boolean isLevel(File file) {
		try {
			InputStream istream = new FileInputStream(file);
			Reader isr = new InputStreamReader(istream);
			JSONParser parse = new JSONParser();
			parse.reset();
			JSONObject level = (JSONObject) parse.parse(isr);
			String name = (String)level.get("NAME");
			String builder = (String)level.get("BUILDER");
			int w = ((Number)level.get("WIDTH")).intValue();
			int h = ((Number)level.get("HEIGHT")).intValue();
			JSONArray IDs_tile = (JSONArray) level.get("TILE");
			JSONArray IDs_entity = (JSONArray) level.get("ENTITY");
			JSONArray commands = (JSONArray) level.get("COMMANDS");
			int length = w * h;
			if(IDs_tile.size() != length)
				return false;
			if(level == null || name == null || builder == null || IDs_tile == null || IDs_entity == null || commands == null)
				return false;
		}catch (Exception e) {
			return false;
		}
		return true;
	}
	
	private JSONObject getLevel(String level_name) {
		try {
			URL path = getClass().getResource(Engine.ResPath+"/levels/"+level_name+".json");
			InputStream istream = path.openStream();
			Reader isr = new InputStreamReader(istream);
			JSONParser parse = new JSONParser();
			parse.reset();
			JSONObject Json = (JSONObject) parse.parse(isr);
			return Json;
		}catch (Exception e) {
			
		}
		return null;
	}
	
	public static JSONObject getLevel(File level_file) {
		try {
			InputStream istream = new FileInputStream(level_file);
			Reader isr = new InputStreamReader(istream);
			JSONParser parse = new JSONParser();
			parse.reset();
			JSONObject Json = (JSONObject) parse.parse(isr);
			return Json;
		}catch (Exception e) {
			
		}
		return null;
	}
	
	public void open() {
		buildLevel(level);
		Game.getCam().setPosition(-Engine.getWidth()/2 + this.bounds.width/2, -Engine.getHeight()/2 + this.bounds.height/2);
	}
	
	private void buildLevel(JSONObject level) {
		int w = ((Number)level.get("WIDTH")).intValue();
		int h = ((Number)level.get("HEIGHT")).intValue();
		int ww = w * Tile.getSize();
		int hh = h * Tile.getSize();
		this.bounds = new Rectangle(0, 0, ww, hh);
		JSONArray tiles = (JSONArray) level.get("TILE");
		JSONArray entities = (JSONArray) level.get("ENTITY");
		map = new Tile[w*h];
		for(int i = 0; i < tiles.size(); i++) {
			JSONObject tile = (JSONObject)tiles.get(i);
			int id = ((Number)tile.get("ID")).intValue();
			int x = ((Number)tile.get("X")).intValue();
			int y = ((Number)tile.get("Y")).intValue();
			map[i] = Tile.Factory(TileTag.values()[id], x * Tile.getSize(), y * Tile.getSize());
		}
		for(int i = 0; i < entities.size(); i++) {
			JSONObject entity = (JSONObject)entities.get(i);
			String type = (String) entity.get("TYPE");
			int id = ((Number)entity.get("ID")).intValue();
			int x = ((Number)entity.get("X")).intValue();
			int y = ((Number)entity.get("Y")).intValue();
			Entity e = null;
			if(type.equals("E"))
				e = Entity.Factory(EntityTag.values()[id], x * Tile.getSize(), y * Tile.getSize());
			else if(type.equals("I"))
				e = Item.Factory(id, x * Tile.getSize(), y * Tile.getSize());
			addEntity(e);
		}
		
		list_keys = new ArrayList<Commands>();
		JSONArray list = (JSONArray) level.get("COMMANDS");
		for(int i = 0; i < list.size(); i++) {
			String name = (String)list.get(i);
			list_keys.add(Commands.valueOf(name));
		}
		JSONArray list_pixels = (JSONArray)level.get("PICTURE");
		int[] pixels = new int[list_pixels.size()];
		for(int i = 0; i < list_pixels.size(); i++)
			pixels[i] = ((Number)list_pixels.get(i)).intValue();
		setPicture(pixels);
	}
	
	private void setPicture(int[] pixels) {
		int w = 256*4;
		int h = 256*4;
		picture = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		picture.setRGB(0, 0, w, h, DrawableBox.convertPixels(pixels), 0, w);
	}

	public JSONObject getLevel() {
		return this.level;
	}
	
	public String getName() {
		return (String)this.level.get("NAME");
	}
	
	public void clearSelect() {
		this.selected = null;
	}

	public Objects getSelected() {
		return this.selected;
	}
	
	public List<Commands> getKeys() {
		return this.list_keys;
	}
	
	public void addKey(Commands com) {
		this.list_keys.add(com);
	}
	
	public void addEntity(Entity o) {
		this.entities.add(o);
	}
	
	public void addParticle(Particle o) {
		this.particles.add(o);
	}
	
	public Rectangle getBounds() {
		return this.bounds;
	}

	public Tile[] getMap() {
		return this.map;
	}
	
	public Tile getTile(int index) {
		Tile tile = null;
		try {
			tile = getMap()[index];
		}catch (ArrayIndexOutOfBoundsException e) {
		}
		return tile;
	}
	
	public Tile getTile(int x, int y) {
		Tile tile = null;
		if(x < 0 || x > this.bounds.width/Tile.getSize() || y < 0 || y > this.bounds.height/Tile.getSize())
			return null;
		try {
			tile = getMap()[x+y*(bounds.width/Tile.getSize())];
		}catch (ArrayIndexOutOfBoundsException e) {
			
		}
		return tile;
	}
	
	public List<Entity> getEntities(){
		return this.entities;
	}
	
	public List<Particle> getParticles(){
		return this.particles;
	}
	
	@Override
	public String giveCommand(String[] keys) {
		String message = "Command no access";

		if(take(keys, Commands.clear)) {
			if(selected != null) {
				clearSelect();
				used(Commands.clear);
				message = "Clean";
			}
		}

		if(selected != null)
			return selected.giveCommand(keys);

		if(take(keys, Commands.select)) {
			message = "Click on an tile to select it";
			EXE.selector(this);
		}

		if(take(keys, Commands.shot)) {
			EXE.shot(keys, this);
		}
		if(take(keys, Commands.put)) {
			EXE.put(keys, this);
		}
		return message;
	}

	public void tick() {
		entities.sort(Objects.Depth);
		for(int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			try {
				if(!e.getVar(Variables.Freeze)) {
					e.tick();
				}
			} catch (Dead ignore) {}
		}
		for(int i = 0; i < map.length; i++) {
			try {
				Tile tile = map[i];
				tile.tick();
			}catch (Dead ignore) {}
		}
		for(int i = 0; i < particles.size(); i++)
			particles.get(i).tick();
		if(selected != null)
			Engine.UI.getConsole().setIcon(selected.getSprite());
		else
			Engine.UI.getConsole().cleatIcon();
	}
	
	public void render(Graphics2D g) {
		drawPicture(g);
		g.setColor(new Color(Theme.Color_Secondary.getRed(), Theme.Color_Secondary.getGreen(), Theme.Color_Secondary.getBlue(), 90));
		g.fillRect(bounds.x - Engine.GameScale - Game.getCam().getX(), bounds.y - Engine.GameScale - Game.getCam().getY(), bounds.width + Engine.GameScale*2, bounds.height + Engine.GameScale*2);
		for(int i = 0; i < map.length; i++)
			map[i].render(g);
		for(int i = 0; i < entities.size(); i++) {
			Entity e = entities.get(i);
			e.render(g);
			if(e.getVar(Variables.Freeze)) {
				g.setColor(new Color(Theme.Color_Primary.getRed(), Theme.Color_Primary.getGreen(), Theme.Color_Primary.getBlue(), 80));
				g.fillRect((int)e.getX() - Game.getCam().getX(), (int)e.getY() - Game.getCam().getY(), e.getWidth(), e.getHeight());
				g.setColor(Theme.Color_Primary);
				g.setStroke(new BasicStroke(Engine.GameScale));
				g.drawRect( (int)e.getX() - Game.getCam().getX(), (int)e.getY() - Game.getCam().getY(), e.getWidth(), e.getHeight());
			}
		}
		for(int i = 0; i < particles.size(); i++)
			particles.get(i).render(g);
		renderName(g);
		renderBuilder(g);
	}
	
	private void renderName(Graphics2D g) {
		Font f = FontG.font(10*Engine.GameScale);
		String value = getName();
		int hF = FontG.getHeight(value, f);
		int x = getBounds().x;
		int y = getBounds().y - hF/2;
		g.setFont(f);
		g.setColor(Theme.Color_Secondary);
		g.drawString(value, x + Engine.GameScale - Game.getCam().getX(), y + Engine.GameScale - Game.getCam().getY());
		g.setColor(Theme.Color_Primary);
		g.drawString(value, x - Game.getCam().getX(), y - Game.getCam().getY());
	}
	
	private void renderBuilder(Graphics2D g) {
		Font f = FontG.font(8*Engine.GameScale);
		String value = (String)getLevel().get("BUILDER");
		int wF = FontG.getWidth(value, f);
		int hF = FontG.getHeight(value, f);
		int x = getBounds().x + getBounds().width - wF;
		int y = getBounds().y + getBounds().height + hF + Engine.GameScale;
		g.setFont(f);
		g.setColor(Theme.Color_Secondary);
		g.drawString(value, x + Engine.GameScale - Game.getCam().getX(), y + Engine.GameScale - Game.getCam().getY());
		g.setColor(Theme.Color_Primary);
		g.drawString(value, x - Game.getCam().getX(), y - Game.getCam().getY());
	}
	
	private void drawPicture(Graphics2D g) {
		int w = picture.getWidth()*Engine.GameScale;
		int h = picture.getHeight()*Engine.GameScale;
		int x = (bounds.x + bounds.width/2) - w/2;
		int y = (bounds.y + bounds.height/2) - h/2;
		g.drawImage(picture, x - Game.getCam().getX(), y - Game.getCam().getY(), w, h, null);
	}
	
	public void dispose() {
		
	}
}
