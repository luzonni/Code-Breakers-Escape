package studio.retrozoni.game.objects.tiles;

import studio.retrozoni.game.Game;
import studio.retrozoni.engine.Theme;
import studio.retrozoni.engine.tools.Timer;
import studio.retrozoni.game.objects.entity.Entity;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Box extends Tile {

	private Timer timer;
	private static BufferedImage[][] sprite;

	public Box(int id, int x, int y) {
		super(id, x, y);
		timer = new Timer(1);
		if (sprite == null) {
			sprite = new BufferedImage[2][2];
			sprite[0] = getSprite("box", Theme.Primary, 0);
			sprite[1] = getSprite("box", Theme.Primary, 1);
		}
		this.setSolid(false);
	}

	public void change(boolean bool) {
		if(this.isSolid() != bool) {
			//TODO Sound.play("poft");
			this.setSolid(bool);
		}
	}
	
	@Override
	public BufferedImage getSprite() {
		return sprite[0][0];
	}

	@Override
	public void tick() {
		if (!this.isSolid()) {
			for(Entity e : Game.getLevel().getEntities())
				if(this.centralizedWith(e))
					change(true);
		}
	}

	@Override
	public void render(Graphics2D g) {
		renderTile(Floor.sprite[Floor.index], g);
		renderTile(sprite[this.isSolid() ? 0 : 1][timer.pit() ? 0 : 1], g);
	}
	
	@Override
	public void dispose() {
		sprite = null;
	}
	
}
