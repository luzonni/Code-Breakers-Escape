package studio.retrozoni.activities.game.objects.tiles;

import studio.retrozoni.activities.game.Game;
import studio.retrozoni.engine.Engine;
import studio.retrozoni.engine.Theme;
import studio.retrozoni.engine.tools.Timer;
import studio.retrozoni.activities.game.objects.entity.Entity;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Box extends Tile {

	private Timer timer;

	public Box(int id, int x, int y) {
		super(id, x, y);
		timer = new Timer(1);
		loadSprite("box");
		this.setSolid(false);
	}

	public void change(boolean bool) {
		if(this.isSolid() != bool) {
			//TODO Sound.play("poft");
			this.setSolid(bool);
		}
	}

	@Override
	public void tick() {
		if (!this.isSolid()) {
			for(Entity e : Game.getLevel().getEntities())
				if(this.centralizedWith(e))
					change(true);
		}
		getSheet().setState(this.isSolid() ? 0 : 1);
		getSheet().setIndex(timer.pit() ? 0 : 1);
	}

	@Override
	public void render(Graphics2D g) {
		BufferedImage spriteFloor = Engine.sheetHolder.getSheet("tiles", "floot").getSprite();
		renderTile(spriteFloor, g);
		renderTile(getSprite(), g);
	}
	
	@Override
	public void dispose() {

	}
	
}
