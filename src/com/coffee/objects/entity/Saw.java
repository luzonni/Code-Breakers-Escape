package com.coffee.objects.entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.coffee.level.Level;
import com.coffee.main.Engine;
import com.coffee.main.Theme;
import com.coffee.main.activity.Game;
import com.coffee.objects.Directions;
import com.coffee.objects.Variables;
import com.coffee.objects.particles.Speck;
import com.coffee.objects.tiles.Tile;

public class Saw extends Entity {

	private static BufferedImage sprite;
	private double rotate;

	public Saw(int id, int x, int y) {
		super(id, x, y);
		if(sprite == null)
			sprite = getSprite("saw", Theme.Primary)[0];
		getOE().setDirection(Directions.Idle);
		setDepth(4);
	}


	private void system() {
		Directions[] dirs = {Directions.Down, Directions.Left, Directions.Up, Directions.Right};
		int x = getMiddle().x / Tile.getSize();
		int y = getMiddle().y / Tile.getSize();
		Level level = Game.getLevel();
		Tile tile = level.getTile(x, y);
		if(this.getOE().getDirection().equals(Directions.Idle)) {
			for(int i = 0; i < dirs.length; i++) {
				if(level.getTile((int)tile.getX()/Tile.getSize() + dirs[i].getX(), (int)getY()/Tile.getSize() + dirs[i].getY()).isSolid()) {
					int index = i + 1;
					if(index > dirs.length-1)
						index = 0;
					getOE().setDirection(dirs[index]);
					this.setX(tile.getX() + (tile.getWidth()/2d) * dirs[i].getX());
					this.setY(tile.getY() + (tile.getHeight()/2d) * dirs[i].getY());
					break;
				}
			}
		}
		if(tile.isSolid()) {
			kill();
			return;
		}
		int botton_index = 0;
		int index = 0;
		for(int i = 0; i < dirs.length; i++)
			if(this.getOE().getDirection().equals(dirs[i])) {
				index = i;
				botton_index = i - 1;
				if(botton_index < 0)
					botton_index = dirs.length-1;
			}
		Tile botton_tile = level.getTile((getMiddle().x + dirs[botton_index].getX()*Engine.SCALE) / Tile.getSize(), (getMiddle().y + dirs[botton_index].getY()*Engine.SCALE) / Tile.getSize());
		Tile next_tile = level.getTile((getMiddle().x + dirs[index].getX()*Engine.SCALE) / Tile.getSize(), (getMiddle().y + dirs[index].getY()*Engine.SCALE) / Tile.getSize());
		if(next_tile.isSolid() && botton_tile.isSolid()) {
			int next = index + 1;
			if(next > dirs.length-1)
				next = 0;
			getOE().setDirection(dirs[next]);
		}else if(!next_tile.isSolid() && !botton_tile.isSolid()) {
			int next = index - 1;
			if(next < 0)
				next = dirs.length-1;
			getOE().setDirection(dirs[next]);
		}
		moving(getOE().getDirection());
	}

	private void moving(Directions dir) {
		int speed = Engine.SCALE;
		if(dir.equals(Directions.Left)) {
			this.setX(this.getX() - speed);
		}else if(dir.equals(Directions.Up)) {
			this.setY(this.getY() - speed);
		}else if(dir.equals(Directions.Right)) {
			this.setX(this.getX() + speed);
		}else if(dir.equals(Directions.Down)) {
			this.setY(this.getY() + speed);
		}
	}

	@Override
	public BufferedImage getSprite() {
		return sprite;
	}

	@Override
	public void tick() {
		Level level = Game.getLevel();
		system();
		if(!isColliding(level)) {
			kill();
			return;
		}
		killing();
		this.rotate -= 0.15;
		spawnParticles();
	}

	private boolean isColliding(Level level) {
		boolean colliding = false;
		for(int i = 0; i < level.getMap().length; i++) {
			Tile t = level.getMap()[i];
			if(this.getBounds().intersects(t.getBounds()) && t.isSolid()) {
				colliding = true;
			}
		}
		return colliding;
	}

	private void killing() {
		for(int i = 0; i < Game.getLevel().getEntities().size(); i++) {
			Entity entity = Game.getLevel().getEntities().get(i);
			if(entity.getVar(Variables.Alive) && entity.collidingWith(this)) {
				entity.kill();
			}
		}
	}

	private void spawnParticles() {
		int x_particle = getMiddle().x;
		int y_particle = getMiddle().y;
		for(int i = 0; i < 5; i++) {
			Game.getLevel().addParticle(new Speck(x_particle, y_particle));
		}
	}

	@Override
	public void render(Graphics2D g) {
		g.rotate(rotate, getMiddle().x - Game.getCam().getX(), getMiddle().y - Game.getCam().getY());
		renderEntity(sprite, g);
		g.rotate(-rotate, getMiddle().x - Game.getCam().getX(), getMiddle().y - Game.getCam().getY());
	}
	
	@Override
	public void dispose() {
		sprite = null;
	}

}
