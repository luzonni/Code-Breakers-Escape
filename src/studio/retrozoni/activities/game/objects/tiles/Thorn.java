package studio.retrozoni.activities.game.objects.tiles;

import studio.retrozoni.activities.game.Game;
import studio.retrozoni.engine.Theme;
import studio.retrozoni.activities.game.objects.entity.Player;

import java.awt.image.BufferedImage;

public class Thorn extends Tile {

	public Thorn(int id, int x, int y) {
		super(id, x, y);
		loadSprite("thron");
		this.setSolid(true);
	}
	
	public void tick() {
		Player player = Game.getPlayer();
		if(player.getOE().overTile().equals(this))
			player.kill();
	}

	@Override
	public void dispose() {

	}

}
