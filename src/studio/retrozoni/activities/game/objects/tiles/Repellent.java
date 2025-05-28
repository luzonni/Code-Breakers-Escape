package studio.retrozoni.activities.game.objects.tiles;

import studio.retrozoni.activities.game.Game;
import studio.retrozoni.engine.Theme;
import studio.retrozoni.activities.game.objects.Variables;
import studio.retrozoni.activities.game.objects.entity.Entity;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class Repellent extends Tile {
	
	public Repellent(int id, int x, int y) {
		super(id, x, y);
		loadSprite("repellent");
		setVar(Variables.Breakable, true);
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
	public void dispose() {

	}

}
