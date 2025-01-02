package com.coffee.objects.entity;

import java.awt.Rectangle;

import com.coffee.level.Level;
import com.coffee.main.Engine;
import com.coffee.main.activity.Game;
import com.coffee.objects.Directions;
import com.coffee.objects.tiles.Tile;

public class Orienteering_Physics {

	private Directions direction;
	private final Entity entity;

	public Orienteering_Physics(Entity entity) {
		this.entity = entity;
		this.direction = Directions.Idle;
	}
	
	public Directions getDirection() {
		return this.direction;
	} 

	public Directions getReverse() {
		if(this.direction == Directions.Up) {
			return Directions.Down;
		}else if(this.direction == Directions.Down) {
			return Directions.Up;
		}else if(this.direction == Directions.Left) {
			return Directions.Right;
		}else if(this.direction == Directions.Right) {
			return Directions.Left;
		}
		return Directions.Idle;
	}

	public void setDirection(Directions newdir) {
		this.direction = newdir;
	}

	public boolean slide(int speed) {
		switch(getDirection()) {
			case Idle:
				break;
			case Up:
				if(!colliding(Directions.Up)) {
					entity.setY(entity.getY() - speed);
				}else {
					return false;
				}
				break;
			case Right:
				if(!colliding(Directions.Right)) {
					entity.setX(entity.getX() + speed);
				}else{
					return false;
				}
				break;
			case Down:
				if(!colliding(Directions.Down)) {
					entity.setY(entity.getY() + speed);
				}else{
					return false;
				}
				break;
			case Left:
				if(!colliding(Directions.Left)) {
					entity.setX(entity.getX() - speed);
				}else{
					return false;
				}
				break;
			case UpRight:
				if(!colliding(Directions.Up) && !colliding(Directions.Right)) {
					entity.setX(entity.getX() - speed);
					entity.setY(entity.getY() - speed);
				}else {
					return false;
				}
				break;
			case RightDown:
				if(!colliding(Directions.Right) && !colliding(Directions.Down)) {
					entity.setX(entity.getX() + speed);
					entity.setY(entity.getY() + speed);
				}else {
					return false;
				}
				break;
			case DownLeft:
				if(!colliding(Directions.Down) && !colliding(Directions.Left)) {
					entity.setX(entity.getX() - speed);
					entity.setY(entity.getY() + speed);
				}else {
					return false;
				}
				break;

			case LeftUp:
				if(!colliding(Directions.Left) && !colliding(Directions.Up)) {
					entity.setX(entity.getX() - speed);
					entity.setY(entity.getY() - speed);
				}else {
					return false;
				}
				break;
		}
		return true;
	}

	public boolean colliding(Directions dir) {
		boolean colliding = false;
		Level L = Game.getLevel();
		int map_Width = Game.getLevel().getBounds().width / Tile.getSize();
		int x = (entity.getMiddle().x / Tile.getSize());
		int y = (entity.getMiddle().y / Tile.getSize());
		Tile tile;
		switch(dir) {
			case Up:
				y--;
				tile = L.getTile(x+y*map_Width);
				if(tile == null || outOfMapBounds())
					break;
				if(tile.isSolid()) {
					colliding = true;
					while(!L.getMap()[x+(((int)entity.getY() - 1) / Tile.getSize())*map_Width].isSolid()) {
						entity.setY(entity.getY() - 1);
					}
				}
				break;
			case Right:
				x++;
				tile = L.getTile(x+y*map_Width);
				if(tile == null || outOfMapBounds())
					break;
				if(tile.isSolid()) {
					colliding = true;
					while(!L.getMap()[(((int)entity.getX() + entity.getWidth()) / Tile.getSize())+y*map_Width].isSolid()) {
						entity.setX(entity.getX() + 1);
					}
				}
				break;
			case Down:
				y++;
				tile = L.getTile(x+y*map_Width);
				if(tile == null || outOfMapBounds())
					break;
				if(tile.isSolid()) {
					colliding = true;
					while(!L.getMap()[x+(((int)entity.getY() + entity.getHeight()) / Tile.getSize())*map_Width].isSolid()) {
						entity.setY(entity.getY() + 1);
					}
				}
				break;
			case Left:
				x--;
				tile = L.getTile(x+y*map_Width);
				if(tile == null || outOfMapBounds())
					break;
				if(L.getMap()[x+y*map_Width].isSolid()) {
					colliding = true;
					while(!L.getMap()[(((int)entity.getX() - 1) / Tile.getSize())+y*map_Width].isSolid()) {
						entity.setX(entity.getX() - 1);
					}
				}
				break;
			default:
				break;
		}
		return colliding;
	}

	public boolean centralizedWith(Entity e) {
		return entity.getMiddle().x == e.getMiddle().x && entity.getMiddle().y == e.getMiddle().y;
	}
	
	private boolean outOfMapBounds() {
		Rectangle rec = Game.getLevel().getBounds();
		return !rec.contains(entity.getMiddle());
	}
	
	public Tile nextTile() {
		Level level = Game.getLevel();
		Tile tile = null;
		int x = (entity.getMiddle().x) / Tile.getSize();
		int y = (entity.getMiddle().y) / Tile.getSize();
		switch(entity.getOE().getDirection()) {
			case Up:
				y--;
				tile = level.getTile(x, y);
				if(tile == null) 
					break;
				return tile;
			case Right:
				x++;
				tile = level.getTile(x, y);
				if(tile == null) 
					break;
				return tile;
			case Down:
				y++;
				tile = level.getTile(x, y);
				if(tile == null) 
					break;
				return tile;
			case Left:
				x--;
				tile = level.getTile(x, y);
				if(tile == null) 
					break;
				return tile;
			default:
				break;
		}
		return tile;
	}

	public Tile nextTile(Directions dir) {
		Level level = Game.getLevel();
		Tile tile = null;
		int x = (entity.getMiddle().x) / Tile.getSize();
		int y = (entity.getMiddle().y) / Tile.getSize();
		switch(dir) {
			case Up:
				y--;
				tile = level.getTile(x, y);
				if(tile == null)
					break;
				return tile;
			case Right:
				x++;
				tile = level.getTile(x, y);
				if(tile == null)
					break;
				return tile;
			case Down:
				y++;
				tile = level.getTile(x, y);
				if(tile == null)
					break;
				return tile;
			case Left:
				x--;
				tile = level.getTile(x, y);
				if(tile == null)
					break;
				return tile;
			default:
				break;
		}
		return tile;
	}

	public Tile overTile() {
		Level level = Game.getLevel();
		Tile tile_over = null;
		int x = entity.getMiddle().x / Tile.getSize();
		int y = entity.getMiddle().y / Tile.getSize();
		switch(entity.getOE().getDirection()) {
			case Up:
				y = (entity.getMiddle().y - entity.getHeight()/2 - Engine.SCALE)  / Tile.getSize();
				tile_over = level.getTile(x, y);
				if(tile_over == null) 
					break;
				return tile_over;
			case Right:
				x = (entity.getMiddle().x + entity.getWidth()/2 + Engine.SCALE) / Tile.getSize();
				tile_over = level.getTile(x, y);
				if(tile_over == null) 
					break;
				return tile_over;
			case Down:
				y = (entity.getMiddle().y + entity.getHeight()/2 + Engine.SCALE) / Tile.getSize();
				tile_over = level.getTile(x, y);
				if(tile_over == null) 
					break;
				return tile_over;
			case Left:
				x = (entity.getMiddle().x - entity.getWidth()/2 - Engine.SCALE) / Tile.getSize();
				tile_over = level.getTile(x, y);
				if(tile_over == null) 
					break;
				return tile_over;
			default:
				break;
		}
		return tile_over;
	}
	
}
