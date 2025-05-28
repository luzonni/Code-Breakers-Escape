package studio.retrozoni.game.objects.entity;

import studio.retrozoni.game.Game;
import studio.retrozoni.engine.Engine;
import studio.retrozoni.game.objects.Directions;
import studio.retrozoni.game.objects.particles.Dust;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

public class Trampoline extends Entity {
	
	private static BufferedImage[] sprites;

	public Trampoline(int id, int x, int y, Directions direction) {
		super(id, x, y);
		if(sprites == null)
			sprites = getSprite("trampoline");
		getOE().setDirection(direction);
	}
	
	public void tick() {
		List<Entity> entities = Game.getLevel().getEntities();
		for(int i = 0; i < entities.size(); i++) {
			Entity entity = entities.get(i);
			if(entity == this) {
				continue;
			}
			if(this.getOE().centralizedWith(entity)) {
				double radians = Math.PI;
				Directions direction = entity.getOE().getDirection();
				switch(getOE().getDirection()) {
					case UpRight -> {
						radians = Math.PI/2;
						if(direction.equals(Directions.Down))
							entity.setDirection(Directions.Right);
						else if(direction.equals(Directions.Left))
							entity.setDirection(Directions.Up);
					}
					case RightDown -> {
						radians = Math.PI;
						if(direction.equals(Directions.Left))
							entity.setDirection(Directions.Down);
						else if(direction.equals(Directions.Up))
							entity.setDirection(Directions.Right);
					}
					case DownLeft -> {
						radians = Math.PI + Math.PI/2;
						if(direction.equals(Directions.Up))
							entity.setDirection(Directions.Left);
						else if(direction.equals(Directions.Right))
							entity.setDirection(Directions.Down);
					}
					case LeftUp -> {
						radians = 0;
						if(direction.equals(Directions.Right))
							entity.setDirection(Directions.Up);
						else if(direction.equals(Directions.Down))
							entity.setDirection(Directions.Left);
					}
				}
				for(int ii = 0; ii < 25; ii++) {
					Game.getLevel().addParticle(new Dust(getMiddle().x, getMiddle().y, radians - (Math.PI/4 + Engine.RAND.nextDouble()*(Math.PI))));
				}
			}
		}
	}
	
	@Override
	public BufferedImage getSprite() {
		switch(getOE().getDirection()) {
			case UpRight -> {
				return sprites[0];
			}
			case RightDown -> {
				return sprites[1];
			}
			case DownLeft -> {
				return sprites[2];
			}
			case LeftUp -> {
				return sprites[3];
			}
			default -> throw new RuntimeException("Sprite not found for direction");
		}
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
