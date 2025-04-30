package com.coffee.activity.creator;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.coffee.exceptions.ConsoleError;
import com.coffee.inputs.Mouse;
import com.coffee.main.Theme;
import com.coffee.activity.Activity;
import com.coffee.activity.Menu;
import com.coffee.activity.creator.frame.Framer;
import com.coffee.activity.creator.ui.Commands_Shelf;
import com.coffee.activity.creator.ui.Shelf;
import com.coffee.activity.game.Game;
import com.coffee.objects.entity.EntityTag;
import com.coffee.objects.tiles.TileTag;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.coffee.inputs.buttons.Button;
import com.coffee.inputs.buttons.TextButton;
import com.coffee.ui.command.Commands;
import com.coffee.graphics.FontG;
import com.coffee.items.Item;
import com.coffee.activity.game.Level;
import com.coffee.main.Engine;
import com.coffee.main.tools.Responsive;
import com.coffee.objects.Camera;
import com.coffee.objects.Objects;
import com.coffee.objects.entity.Entity;
import com.coffee.objects.entity.Flag;
import com.coffee.objects.entity.Player;
import com.coffee.objects.tiles.Air;
import com.coffee.objects.tiles.Tile;

public class Creator implements Activity {
	
	public static String PATH = Engine.ResPath + "/levels/";
	private static Responsive center;
	
	private TextButton name_sublime;
	private TextButton name_builder;;
	private TextButton[] sizes;
	private Button create_button;
	private static Selected Selected;
	private Shelf inventoryTiles;
	private Shelf inventoryEntities;
	private Shelf inventoryItems;
	private Commands_Shelf c_b;
	private Framer picture;
	
	private final Camera camera;
	
	private final JSONObject level;
	private String NAME;
	private String BUILDER;
	private List<Commands> COMMANDS;
	private Grid MAP_TILES; 
	private Grid MAP_ENTITIES;
	private int WIDTH, HEIGHT;

	private final char[] nums = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
	private final char[] chars = {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
			'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
			'1','2','3','4','5','6','7','8','9','0'};
	
	public Creator(JSONObject level) {
		this.level = level;
		this.camera = new Camera();
		this.camera.start();
	}
	
	private void buildInventoryTiles(int size) {
		List<Tile> tiles = new ArrayList<Tile>();
		int index = 1;
		while(true) {
			try {
				TileTag[] tag = TileTag.values();
				tiles.add(Tile.Factory(tag[index], 0, 0));
			}catch (Exception e) {
				break;
			}
			index++;
		}
		Responsive res = Responsive.createPoint(Engine.UI.getMenuPosition(), -1, 60*Engine.SCALE);
		inventoryTiles = new Shelf("Tiles", tiles.toArray(new Tile[0]), res, 1, 1, size);
	}
	
	private void buildInventoryEntities(int size) {
		List<Entity> entities = new ArrayList<Entity>();
		int index = 0;
		while(true) {
			try {
				EntityTag[] tag = EntityTag.values();
				Entity E = Entity.Factory(tag[index], 0, 0);
				entities.add(E);
			}catch (Exception e) {
				break;
			}
			index++;
		}
		inventoryEntities = new Shelf("Entity", entities.toArray(new Entity[0]), inventoryTiles.getResponsive(), 8*Engine.SCALE, 0, size);
	}
	
	private void buildInventoryItems(int size) {
		List<Entity> entities = new ArrayList<Entity>();
		int index = 1;
		while(true) {
			try {
				Entity E = Item.Factory(index, 0, 0);
				entities.add(E);
			}catch (Exception e) {
				break;
			}
			index++;
		}
		inventoryItems = new Shelf("Items", entities.toArray(new Entity[0]), inventoryEntities.getResponsive(), 8*Engine.SCALE, 0, size);
	}
	
	public static Camera getCam() {
		Activity activity = Engine.ACTIVITY;
		if(activity instanceof Creator)
			return ((Creator) activity).camera;
		throw new RuntimeException("Not in Creator");
	}
	
	@Override
	public void enter() {
		Engine.UI.addOption("Back", () -> {
			Engine.setActivity(new Menu());
		});
		Creator.getCam().setPosition(0, 0);
		Selected = new Selected();
		center = Responsive.createPoint(null, 50, 50);
		buildInventoryTiles(6);
		buildInventoryEntities(6);
		buildInventoryItems(6);
		if(level != null) {
			loader();
			addButtonsMenu();
		}else {
			sizes = new TextButton[2];
			sizes[0] = new TextButton("Width", -10*Engine.SCALE, 0, center, 8);
			sizes[1] = new TextButton("Height", 10*Engine.SCALE, 0, center, 8);
			name_sublime = new TextButton("level name", 0, -12*Engine.SCALE, center, 8);
			name_builder = new TextButton("your nick or @", 0, -6*Engine.SCALE, name_sublime.getResponsive(), 8);
			create_button = new Button("create", 0, 12*Engine.SCALE, center, 8);
			c_b = new Commands_Shelf(null);
		}
	}
	
	private void loader() {
		this.NAME = (String)level.get("NAME");
		this.BUILDER = (String)level.get("BUILDER");
		this.WIDTH = ((Number)level.get("WIDTH")).intValue();
		this.HEIGHT = ((Number)level.get("HEIGHT")).intValue();
		
		JSONArray tiles = (JSONArray) level.get("TILE");
		Tile[] array_tile = new Tile[this.WIDTH * this.HEIGHT];
		for(int i = 0; i < array_tile.length; i++) {
			JSONObject tile = (JSONObject) tiles.get(i);
			int id = ((Number)tile.get("ID")).intValue();
			Tile t = Tile.Factory(TileTag.values()[id], 0, 0);
			if(t instanceof Air)
				continue;
			array_tile[i] = t;
		}
		MAP_TILES = new Grid(array_tile, this.WIDTH, this.HEIGHT);
		
		
		JSONArray entities = (JSONArray) level.get("ENTITY");
		Entity[] array_item = new Entity[this.WIDTH * this.HEIGHT];
		for(int i = 0; i < entities.size(); i++) {
			JSONObject entity = (JSONObject) entities.get(i);
			String type = (String)entity.get("TYPE");
			int id = ((Number)entity.get("ID")).intValue();
			int x = ((Number)entity.get("X")).intValue();
			int y = ((Number)entity.get("Y")).intValue();
			if(type.equals("E")) {
				array_item[x + y * this.WIDTH] = Entity.Factory(EntityTag.values()[id], x, y);
			} else if(type.equals("I"))
				array_item[x+y*this.WIDTH] = Item.Factory(id, x, y);
		}
		MAP_ENTITIES = new Grid(array_item, this.WIDTH, this.HEIGHT);
		
		JSONArray commands = (JSONArray) level.get("COMMANDS");
		COMMANDS = new ArrayList<Commands>();
		for(int i = 0; i < commands.size(); i++) {
			String name = (String)commands.get(i);
			COMMANDS.add(Commands.valueOf(name));
		}
		c_b = new Commands_Shelf(COMMANDS);

		picture = new Framer(center, new Rectangle(this.WIDTH, this.HEIGHT));
		JSONArray list_pixels = (JSONArray)level.get("PICTURE");
		int[] pixels = new int[list_pixels.size()];
		for(int i = 0; i < list_pixels.size(); i++)
			pixels[i] = ((Number)list_pixels.get(i)).intValue();
		picture.setPixels(Framer.convertPixels(pixels));
	}
	
	@Override
	public String giveCommand(String[] keys) {
		if(keys[0].equalsIgnoreCase("load") && keys.length > 1) {
			loadLevel(keys);
			return "";
		}
		if(keys[0].equalsIgnoreCase("try") || keys[0].equalsIgnoreCase("t")) {
			testeAndSaveLevel();
			return "";
		}
		if(keys[0].equalsIgnoreCase("rename") && keys.length == 2) {
			if(!this.NAME.isBlank()) {
				Engine.UI.getConsole().print("Renamed to \"" + keys[1] + "\"", true);
				this.NAME = keys[1];
				return "";
			}else {
				throw new ConsoleError("The level name not beginning");
			}
		}
		if(keys[0].equalsIgnoreCase("name") && keys.length == 1) {
			Engine.UI.getConsole().print("The level name is " + NAME, true);
			return "";
		}
		return "I'm not sure what you told me...";
	}

	private static void loadLevel(String[] keys) {
		String level_name = "";
		for(int i = 1; i < keys.length; i++)
			level_name += keys[i] + " ";
		level_name = level_name.strip().replace(" ", "_").replace("\"", "").toLowerCase().strip();
		File dir = new File(Engine.currentPath() + "/created/" + level_name + ".json");
		if(dir.exists() && Level.isLevel(dir)) {
			JSONObject level = Level.getLevel(dir);
			Engine.setActivity(new Creator(level));
		}else {
			throw new ConsoleError("Level not found");
		}
	}

	private void getLevel() {
		if(sizes == null)
			return;
		NAME = name_sublime.readText(chars);
		BUILDER = name_builder.readText(null);
		String string_w = sizes[0].readText(nums);
		String string_h = sizes[1].readText(nums);
		if(string_w == "" || string_h == "")
			return;
		int w = Integer.parseInt(string_w);
		int h = Integer.parseInt(string_h);
		if(w < 3 || h < 3 || NAME == "" || w > 48 || h > 48)
			return;
		if(create_button.function()) {
			addButtonsMenu();
			MAP_TILES = new Grid(new Tile[w*h], w, h);
			MAP_ENTITIES = new Grid(new Entity[w*h], w, h);
			picture = new Framer(center, new Rectangle(w, h));
			WIDTH = w;
			HEIGHT = h;
			sizes = null;
		}
	}
	
	public static Responsive getCenter() {
		return Creator.center;
	}
	
	public static Objects getSelected() {
		return Creator.Selected.get();
	}

	@Override
	public void tick() {
		Selected.tick();
		getLevel();
		grids();
		if(picture != null && !picture.isRunning()) {
			Tile t = (Tile) inventoryTiles.getItem();
			if(t != null)
				Selected.set(t);
			Entity e = (Entity) inventoryEntities.getItem();
			if(e != null)
				Selected.set(e);
			Entity i = (Entity) inventoryItems.getItem();
			if(i != null)
				Selected.set(i);
			this.COMMANDS = c_b.tick();
		}
	}
	
	private void grids() {
		if(MAP_TILES == null || MAP_ENTITIES == null)
			return;
		if(Mouse.On_Mouse(inventoryEntities.getResponsive().getBounds()) ||
			Mouse.On_Mouse(inventoryItems.getResponsive().getBounds()) ||
			Mouse.On_Mouse(inventoryTiles.getResponsive().getBounds()) ||
			Engine.UI.overButtons() || c_b.over())
			return;
		MAP_TILES.tick();
		MAP_ENTITIES.tick();
		if(picture != null && !picture.isRunning()) {
			if(getSelected() instanceof Tile) {
				MAP_TILES.setGrid(getSelected());
			}
			if(getSelected() instanceof Entity entity) {
                boolean can_place = true;
				if(entity instanceof Player)
					for(Entity e : (Entity[])MAP_ENTITIES.getArray())
						if((e instanceof Player))
							can_place = false;
				if(entity instanceof Flag)
					for(Entity e : (Entity[])MAP_ENTITIES.getArray())
						if((e instanceof Flag))
							can_place = false;
				if(can_place)
					MAP_ENTITIES.setGrid(getSelected());
			}
			MAP_ENTITIES.clearGrid();
			MAP_TILES.clearGrid();
		}
	}
	
	private void testeAndSaveLevel() {
		synchronized (this) {
			this.picture.stop();
		}
		Saver saver = new Saver(NAME, BUILDER, COMMANDS.toArray(new Commands[0]), MAP_TILES.getArray(), MAP_ENTITIES.getArray(), picture.getPixels(), WIDTH, HEIGHT);
		JSONObject level_file = saver.create();
		Engine.setActivity(new Game(new Level(level_file), () -> {
			File curPath = new File(System.getProperty("user.dir") + "/created");
			if(!curPath.exists())
				curPath.mkdir();
			boolean wasSaved = saver.save(curPath, level_file);
			if(wasSaved)
				Engine.UI.getConsole().print("Saved in directory: " + curPath.getAbsolutePath(), true);
			else
				Engine.UI.getConsole().print("Unable to save your level", true);
			Engine.setActivity(new Creator(level_file));
		}, new Creator(level_file)));
	}
	
	private void addButtonsMenu() {
		Engine.UI.addOption("new", () -> {
			Engine.setActivity(new Creator(null));
		});
		Engine.UI.addOption("draw", ()-> {
			if(!picture.isRunning()) {
				picture.start();
			}else {
				picture.stop();
			}
		});
		Engine.UI.addOption("try", this::testeAndSaveLevel);
	}
	
	@Override
	public void render(Graphics2D g) {
		if(picture != null)
			picture.render(g);
		renderLCreator(g);
		if(MAP_TILES != null)
			MAP_TILES.render(g, true);
		if(MAP_ENTITIES != null)
			MAP_ENTITIES.render(g, false);
		if(picture != null && !picture.isRunning()) {
			inventoryTiles.render(g);
			inventoryEntities.render(g);
			inventoryItems.render(g);
			if(inventoryTiles.itemOnSlots(Selected.get()) || inventoryEntities.itemOnSlots(Selected.get()) || inventoryItems.itemOnSlots(Selected.get()))
				Selected.render(g);
			c_b.render(g);
		}
	}
	
	private void renderLCreator(Graphics2D g) {
		if(sizes == null)
			return;
		for(TextButton t : sizes)
			t.render(g);
		name_sublime.render(g);
		name_builder.render(g);
		create_button.render(g);
		String value = "/";
		Font f = FontG.font(12*Engine.SCALE);
		g.setColor(Theme.Primary);
		g.setFont(f);
		int x = Engine.getWidth()/2 - FontG.getWidth(value, f)/2 + Engine.SCALE;
		int y = Engine.getHeight()/2 + FontG.getHeight(value, f)/2;
		g.drawString(value, x, y);
	}
	
	public void dispose() {
		Engine.UI.clearOptions();
		if(this.picture != null)
			this.picture.stop();
		if(this.camera != null)
			this.camera.stop();
	}
	
}
