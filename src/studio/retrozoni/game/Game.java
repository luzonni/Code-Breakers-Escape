package studio.retrozoni.game;

import studio.retrozoni.engine.Activity;
import studio.retrozoni.levels.LevelMap;
import studio.retrozoni.engine.exceptions.Dead;
import studio.retrozoni.engine.exceptions.OutMap;
import studio.retrozoni.engine.inputs.Mouse;
import studio.retrozoni.engine.inputs.Mouse_Button;
import studio.retrozoni.engine.Engine;
import studio.retrozoni.engine.Theme;
import studio.retrozoni.game.objects.Camera;
import studio.retrozoni.game.objects.Objects;
import studio.retrozoni.game.objects.entity.Entity;
import studio.retrozoni.game.objects.entity.Player;
import studio.retrozoni.ui.Inventory;
import studio.retrozoni.ui.win.Helper;

import java.awt.*;

public class Game implements Activity {
	
	private final Camera camera;
	private final Level level;
	private final Next next;
	private final Inventory inventory;

	public static Helper helper;
	
	private int lastX_mouse, lastY_mouse;
	private float f = 0;
	private boolean change;
	private final Activity activityToReturn;

	public Game(Level level, Next next, Activity activityToReturn) {
		this.level = level;
		this.next = next;
		this.activityToReturn = activityToReturn;
		camera = new Camera();
		inventory = new Inventory(3);
		camera.start();
	}

	@Override
	public void enter() {
		this.level.clearSelect();
		this.level.open();
		if(helper == null)
			helper = new Helper(level.getKeys());
		else
			helper.setCommands(level.getKeys());
		Engine.UI.addOption("Back", () -> {
			Engine.setActivity(activityToReturn);
		});
		Engine.UI.addOption("restart", Game::restart);
		Engine.UI.addView(helper);
	}
	
	@Override
	public String giveCommand(String[] keys) {	
		if(keys.length == 2 && keys[0].equalsIgnoreCase("clear") && keys[1].equalsIgnoreCase("chat")) {
			Engine.UI.getConsole().clearChat();
			return "";
		}
		if(keys[0].equalsIgnoreCase("restart")) {
			Game.restart();
		}
		return level.giveCommand(keys);
	}
	
	public static void clearSelect() {
		getLevel().clearSelect();
	}
	
	public static Player getPlayer() {
		for(Entity e : Game.getLevel().getEntities())
			if(e instanceof Player)
				return (Player)e;
		throw new Dead("Player is dead", null);
	}
	
	public static Objects getSelect() {
		return getLevel().getSelected();
	}
	
	public static Level getLevel() {
		Activity activity = Engine.ACTIVITY;
		if(activity instanceof Game)
			return ((Game) activity).level;
		throw new RuntimeException("Not in game");
	}

	public static Inventory getInventory() {
		if(Engine.ACTIVITY instanceof Game game)
			return game.inventory;
		throw new RuntimeException("Not in Game");
	}
	
	public static Camera getCam() {
		Activity activity = Engine.ACTIVITY;
		if(activity instanceof Game)
			return ((Game) activity).camera;
		throw new RuntimeException("Not in game");
	}
	
	public void tick() {
		try {
			level.tick();
		}catch (OutMap o) {
			restart();
		}
		if(Mouse.pressing(Mouse_Button.SCROOL)) {
			int x = lastX_mouse - Mouse.getX();
			int y = lastY_mouse - Mouse.getY();
			getCam().setPosition(x, y);
		}else {
			lastX_mouse = Mouse.getX() + getCam().getX();
			lastY_mouse = Mouse.getY() + getCam().getY();
		}
		f();
		inventory.tick();
	}
	
	public static void start(Level level) {
		Engine.setActivity(new Game(level, () -> {
			Level nextLevel = null;
			for(int i = 0; i < Engine.LEVELS.length; i++) {
				String currentLevelName = Engine.LEVELS[i].replace("_", " ");
				if (currentLevelName.equalsIgnoreCase(level.getName()) && i + 1 < Engine.LEVELS.length) {
					nextLevel = new Level(Engine.LEVELS[i + 1]);
					Engine.INDEX_LEVEL = i + 1;
					break;
				}
			}
			if(nextLevel != null) 
				Game.start(nextLevel);
			else 
				Engine.setActivity(new LevelMap());
		}, new LevelMap()));
	}
	
	public static void restart() {
		if(!(Engine.ACTIVITY instanceof Game game))
			throw new RuntimeException("Not in game");
		Engine.setActivity(new Game(new Level(game.level.getLevel()), game.next, game.activityToReturn));
	}
	
	public static void end() {
		Activity activity = Engine.ACTIVITY;
		if(!(activity instanceof Game)) 
			throw new RuntimeException("Not in game");
		((Game)activity).next.next();
	}
	
	private void f() {
		if(change) {
			f += 0.02f;
		}else {
			f -= 0.02f;
		}
		if(f > 1f) {
			f = 1f;
			change = false;
		}
		if(f < 0) {
			f = 0;
			change = true;
		}
	}
	
	public void render(Graphics2D g) {
		level.render(g);
		if(this.level.getSelected() != null) {
			int per = (int)(f*20);
			int x = this.level.getSelected().getBounds().x - getCam().getX() + per;
			int y = this.level.getSelected().getBounds().y  - getCam().getY() + per;
			int w = this.level.getSelected().getBounds().width - per*2;
			int h = this.level.getSelected().getBounds().height - per*2;
			g.setStroke(new BasicStroke(Engine.SCALE + per));
			g.setColor(Theme.Tertiary);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, f));
			g.drawRect(x, y, w, h);
			g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
		}
		inventory.render(g);
	}
	
	public void dispose() {
		this.level.clearSelect();
		this.camera.stop();
		this.level.dispose();
		Engine.UI.clearViews();
		Engine.UI.clearOptions();
	}

}
