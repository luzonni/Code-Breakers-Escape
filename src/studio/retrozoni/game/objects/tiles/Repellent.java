package studio.retrozoni.game.objects.tiles;

import studio.retrozoni.game.Game;
import studio.retrozoni.engine.Theme;
import studio.retrozoni.game.objects.Variables;
import studio.retrozoni.game.objects.entity.Entity;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class Repellent extends Tile {
	
	public static BufferedImage sprite;

	public Repellent(int id, int x, int y) {
		super(id, x, y);
		if(sprite == null)
			sprite = getSprite("repellent", Theme.Primary)[0];
		setVar(Variables.Breakable, true);
	}

	@Override
	public BufferedImage getSprite() {
		return sprite;
	}

	@Override
	public void tick() {
		List<Entity> entities = Game.getLevel().getEntities();
		for(int i = 0; i < entities.size(); i++) {
			Entity entity = entities.get(i);
			if(entity.getOE().overTile() == this) {
				entity.getOE().setDirection(entity.getOE().getReverse());
			}
		}
	}

	@Override
	public void render(Graphics2D g) {
		renderTile(sprite, g);
	}
	
	@Override
	public void dispose() {
		sprite = null;
	}

}
