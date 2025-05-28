package studio.retrozoni.game.objects.entity;

import studio.retrozoni.game.Game;
import studio.retrozoni.engine.Engine;
import studio.retrozoni.engine.Theme;
import studio.retrozoni.engine.tools.Geometry;
import studio.retrozoni.game.objects.Variables;
import studio.retrozoni.game.objects.particles.Dust;
import studio.retrozoni.game.objects.particles.Kaboom;
import studio.retrozoni.game.objects.tiles.Tile;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Bomb extends Entity {

	private static BufferedImage[] sprites;
	private int[][] positions = {{-5, -5}, {-4, -6}, {-3, -6}, {-2, -7}, {-1, -7}, {0, -6}, {0, -6}, {0, -6}, {0, -6}};
	private final double radius;
	private boolean activity;
	private int index;
	private int count;
	
	
	public Bomb(int id, int x, int y) {
		super(id, x, y);
		if(sprites == null) {
			sprites = getSprite("bomb", Theme.Primary);
		}
		setVar(Variables.Selectable, true);
		setVar(Variables.Movable, true);
		setVar(Variables.Breakable, true);
		radius = Tile.getSize()*2.5;
	}

	@Override
	public BufferedImage getSprite() {
		return sprites[index];
	}

	@Override
	public void tick() {
		Player player = Game.getPlayer();
		if(Geometry.Theta(player.getMiddle(), getMiddle()) <= radius) {
			activity = true;
			//TODO Sound.loop("sss");
		}
		if(activity) {
			count++;
			Game.getLevel().addParticle(new Dust(getMiddle().x + positions[index][0]*Engine.SCALE, getMiddle().y + positions[index][1]*Engine.SCALE, -Math.PI/2));
			if(count > 15) {
				count = 0;
				index++;
				if(index == sprites.length-1) {
					//TODO Sound.stop("sss");
					explode();
				}
			}
		}
	}
	
	private void explode() {
		Entity[] entities = Game.getLevel().getEntities().toArray(new Entity[0]);
		for(int i = 0; i < entities.length; i++) {
			Entity entity = entities[i];
			if(Geometry.Theta(entity.getMiddle(), getMiddle()) <= radius && entity.getVar(Variables.Alive)) {
				entity.kill();
			}
		}
		int amount = 0;
		while(amount < 500) {
			Point point = new Point((int)(getMiddle().x - radius) + Engine.RAND.nextInt((int)radius*2), (int)(getMiddle().y - radius) + Engine.RAND.nextInt((int)radius*2));
			if(Geometry.Theta(getMiddle(), point) <= radius) {
				Game.getLevel().addParticle(new Kaboom(point.x, point.y));
				amount++;
			}
		}
		Game.getLevel().getEntities().remove(this);
		//TODO Sound.play("kabum");
	}

	@Override
	public void render(Graphics2D g) {
		renderEntity(getSprite(), g);
	}
	
	@Override
	public void dispose() {
		sprites = null;
	}

}
