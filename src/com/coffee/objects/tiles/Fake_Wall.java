package com.coffee.objects.tiles;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.coffee.level.Level;
import com.coffee.main.Engine;
import com.coffee.main.activity.Game;
import com.coffee.objects.particles.Kaboom;

public class Fake_Wall extends Tile {
	
	protected static BufferedImage sprites;
	
	public Fake_Wall(int id, int x, int y) {
		super(id, x, y);
		if(sprites == null) {
			sprites = buildSprite();
		}
		this.setSolid(true);
	}
	
	private BufferedImage buildSprite() {
		BufferedImage cur_sprite = Wall.sprite[Wall.index];
		BufferedImage sprite = new BufferedImage(cur_sprite.getWidth(), cur_sprite.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics g = sprite.getGraphics();
		g.drawImage(cur_sprite, 0, 0, null);
		g.dispose();
		int[] colors = sprite.getRGB(0, 0, sprite.getWidth(), sprite.getHeight(), null, 0, sprite.getWidth());
		for(int i = 0; i < 24; i++) {
			int x = Engine.RAND.nextInt(sprite.getWidth()/Engine.GameScale)*Engine.GameScale;
			int y = Engine.RAND.nextInt(sprite.getHeight()/Engine.GameScale)*Engine.GameScale;
			for(int yy = y; yy < y + Engine.GameScale; yy++)
				for(int xx = x; xx < x + Engine.GameScale; xx++) {
					colors[(xx)+(yy)*sprite.getWidth()] = 0x00000000;
				}
		}
		sprite.setRGB(0, 0, sprite.getWidth(), sprite.getHeight(), colors, 0, sprite.getWidth());
		return sprite;
	}
	
	public void tick() {
		if(Game.getPlayer().getOE().nextTile().equals(this)) {
			Level level = Game.getLevel();
			Tile[] map = level.getMap();
			for(int i = 0; i < map.length; i++) {
				Tile tile = map[i];
				if(tile.equals(this)) {
					map[i] = Tile.Factory(TileTag.Floor, (int)getX(), (int)getY());
					for(int y = (int)getY()/Engine.GameScale; y < (int)getY()/Engine.GameScale + getHeight()/Engine.GameScale; y++)
						for(int x = (int)getX()/Engine.GameScale; x < (int)getX()/Engine.GameScale + getWidth()/Engine.GameScale; x++)
							if(Engine.RAND.nextInt(100) < 15)
								Game.getLevel().addParticle(new Kaboom(x*Engine.GameScale, y*Engine.GameScale));
				}
			}
		}
	}

	@Override
	public BufferedImage getSprite() {
		return sprites;
	}
	
	@Override
	public void dispose() {
		sprites = null;
	}
	
}
