package com.coffee.items;

import com.coffee.main.Engine;
import com.coffee.activity.game.Game;
import com.coffee.objects.Directions;
import com.coffee.objects.entity.Arrow;
import com.coffee.objects.entity.Player;
import com.coffee.objects.tiles.Tile;

public class Bow extends Item {

	public Bow() {
		super("Bow");
		setSprite("bow");
	}

	@Override
	public void fun() {
		
	}
	
	public void shot(Player player, Directions dir) {
		int x = player.getMiddle().x;
		int y = player.getMiddle().y;
		switch (dir) {
			case Up:
				y -= Tile.getSize();
				Arrow a = new Arrow(x, y, -Math.PI/2, 5*Engine.SCALE);
				Game.getLevel().addEntity(a);
				Game.getInventory().remove(this);
				break;
			case Down:
				y += Tile.getSize();
				a = new Arrow(x, y, Math.PI/2, 5*Engine.SCALE);
				Game.getLevel().addEntity(a);
				Game.getInventory().remove(this);
				break;
			case Left:
				x -= Tile.getSize();
				a = new Arrow(x, y, Math.PI, 5*Engine.SCALE);
				Game.getLevel().addEntity(a);
				Game.getInventory().remove(this);
				break;
			case Right:
				x += Tile.getSize();
				a = new Arrow(x, y, 0, 5*Engine.SCALE);
				Game.getLevel().addEntity(a);
				Game.getInventory().remove(this);
				break;
			default:
				break;
		}
	}

}
