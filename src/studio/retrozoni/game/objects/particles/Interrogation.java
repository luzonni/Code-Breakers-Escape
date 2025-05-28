package studio.retrozoni.game.objects.particles;

import studio.retrozoni.engine.Engine;
import studio.retrozoni.engine.Theme;
import studio.retrozoni.game.objects.tiles.Tile;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Interrogation extends Particle {
	
	private static BufferedImage sprite;
	
	public Interrogation(int x, int y) {
		super(x, y);
		if(sprite == null)
			sprite = getSprite("interrogation", Theme.Primary)[0].getSubimage(0, 0, 5*Engine.SCALE, 9*Engine.SCALE);
		setSize(sprite.getWidth(), sprite.getHeight());
		setX(getX() - (Tile.getSize()/4 - Engine.RAND.nextInt(Tile.getSize()/2)));
		setY(getY());
	}

	@Override
	public void spawn(int x, int y) {

	}

	@Override
	public void tick() {
		setY(getY() - 1*Engine.SCALE);
		dead();
	}

	@Override
	public void render(Graphics2D g) {
		renderParticle(sprite, g);
	}

}
