package studio.retrozoni.activities.game.objects.particles;

import studio.retrozoni.activities.game.Game;
import studio.retrozoni.engine.Theme;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Trail extends Particle {
	
	private int index;
	private int count;
	
	public Trail(int x, int y) {
		super(x, y);
		loadSprite("trail");
		setSize(getSprite().getWidth(), getSprite().getHeight());
		setX(x - getWidth()/2);
		setY(y - getHeight()/2);	
		setDepth(-1);
	}

	@Override
	public void spawn(int x, int y) {

	}
	
	@Override
	public void tick() {
		count++;
		if(count > 4) {
			count = 0;
			index++;
			if(index > getSheet().size()-1) {
				index = 0;
				Game.getLevel().getParticles().remove(this);
			}
		}
		getSheet().setIndex(index);
	}

	@Override
	public void render(Graphics2D g) {
		renderParticle(getSprite(), g);
	}

}
