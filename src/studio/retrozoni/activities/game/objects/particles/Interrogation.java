package studio.retrozoni.activities.game.objects.particles;

import studio.retrozoni.engine.Engine;
import studio.retrozoni.engine.Theme;
import studio.retrozoni.activities.game.objects.tiles.Tile;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Interrogation extends Particle {
	
	public Interrogation(int x, int y) {
		super(x, y);
		loadSprite("interrogation");
		setSize(getSprite().getWidth(), getSprite().getHeight());
		setX(getX() - (Tile.getSize()/4d - Engine.RAND.nextInt(Tile.getSize()/2)));
		setY(getY());
	}

	@Override
	public void spawn(int x, int y) {

	}

	@Override
	public void tick() {
		setY(getY() - 1d*Engine.SCALE);
		dead();
	}

	@Override
	public void render(Graphics2D g) {
		renderParticle(getSprite(), g);
	}

}
