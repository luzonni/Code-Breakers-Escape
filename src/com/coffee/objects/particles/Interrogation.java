package com.coffee.objects.particles;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.coffee.main.Engine;
import com.coffee.main.Theme;
import com.coffee.objects.tiles.Tile;

public class Interrogation extends Particle {
	
	private static BufferedImage sprite;
	
	public Interrogation(int x, int y) {
		super(x, y);
		if(sprite == null)
			sprite = getSprite("interrogation", Theme.Color_Primary)[0].getSubimage(0, 0, 5*Engine.GameScale, 9*Engine.GameScale);
		setSize(sprite.getWidth(), sprite.getHeight());
		setX(getX() - (Tile.getSize()/4 - Engine.RAND.nextInt(Tile.getSize()/2)));
		setY(getY());
	}

	@Override
	public void spawn(int x, int y) {

	}

	@Override
	public void tick() {
		setY(getY() - 1*Engine.GameScale);
		dead();
	}

	@Override
	public void render(Graphics2D g) {
		renderParticle(sprite, g);
	}

}
