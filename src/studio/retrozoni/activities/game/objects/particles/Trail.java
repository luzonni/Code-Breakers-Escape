package studio.retrozoni.activities.game.objects.particles;

import studio.retrozoni.activities.game.Game;
import studio.retrozoni.engine.Theme;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Trail extends Particle {
	
	private static BufferedImage[] sprites;
	private int index;
	private int count;
	
	public Trail(int x, int y) {
		super(x, y);
		if(sprites == null)
			sprites = getSprite("trail", Theme.Primary);
		setSize(sprites[index].getWidth(), sprites[index].getHeight());
		setX(x - getWidth()/2);
		setY(y - getHeight()/2);	
		setDepth(-1);
	}

	@Override
	public void spawn(int x, int y) {

	}

	@Override
	public BufferedImage getSprite() {
		return sprites[index];
	}
	
	@Override
	public void tick() {
		count++;
		if(count > 4) {
			count = 0;
			index++;
			if(index > sprites.length-1) {
				index = 0;
				Game.getLevel().getParticles().remove(this);
			}
		}
	}

	@Override
	public void render(Graphics2D g) {
		renderParticle(getSprite(), g);
	}

}
