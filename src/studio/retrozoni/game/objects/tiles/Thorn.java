package studio.retrozoni.game.objects.tiles;

import studio.retrozoni.game.Game;
import studio.retrozoni.engine.Theme;
import studio.retrozoni.game.objects.entity.Player;

import java.awt.image.BufferedImage;

public class Thorn extends Tile {
	
	private static BufferedImage sprite;

	public Thorn(int id, int x, int y) {
		super(id, x, y);
		if(sprite == null)
			sprite = getSprite("thorn", Theme.Primary)[0];
		this.setSolid(true);
	}
	
	public void tick() {
		Player player = Game.getPlayer();
		if(player.getOE().overTile().equals(this))
			player.kill();
	}

	@Override
	public BufferedImage getSprite() {
		return sprite;
	}
	
	@Override
	public void dispose() {
		sprite = null;
	}

}
