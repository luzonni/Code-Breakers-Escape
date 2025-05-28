package studio.retrozoni.activities.game.items;

import studio.retrozoni.activities.game.Game;
import studio.retrozoni.engine.Engine;
import studio.retrozoni.activities.game.objects.Directions;
import studio.retrozoni.activities.game.objects.entity.Arrow;
import studio.retrozoni.activities.game.objects.entity.Player;
import studio.retrozoni.activities.game.objects.tiles.Tile;

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
