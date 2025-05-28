package studio.retrozoni.activities.game.objects.particles;

import studio.retrozoni.activities.game.Game;
import studio.retrozoni.engine.exceptions.OutMap;
import studio.retrozoni.engine.graphics.sprites.SpriteSheet;
import studio.retrozoni.engine.Engine;
import studio.retrozoni.engine.Theme;
import studio.retrozoni.activities.game.objects.Objects;

import java.awt.*;
import java.awt.image.BufferedImage;

public abstract class Particle extends Objects {
	
	private float f = 1f;
	
	public Particle(int x, int y) {
		super(-1);
		int size = (3 + Engine.RAND.nextInt(3)) * Engine.SCALE;
		this.setSize(size, size);
		this.setX(x - (double) size /2);
		this.setY(y - (double) size /2);
	}

	public abstract void spawn(int x, int y);
	
	@Override
	public String giveCommand(String[] keys) {
		return null;
	}

	@Override
	protected void loadSprite(String name) {
		loadSprites("particles", name);
	}

	@Override
	public void setX(double X) {
		try {
			super.setX(X);
		}catch (OutMap o) {
			this.dead();
		}
	}

	@Override
	public void setY(double Y) {
		try {
			super.setY(Y);
		}catch (OutMap o) {
			this.dead();
		}
	}

	public void dead() {
		f -= 0.02F;
		if(f < 0) {
			f = 0;
			Game.getLevel().getParticles().remove(this);
		}
	}
	
	public void renderParticle(BufferedImage image, Graphics2D g) {
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, f));
		g.drawImage(image, (int)getX() - Game.getCam().getX(), (int)getY() - Game.getCam().getY(), getWidth(), getHeight(), null);
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
	}
	
	public void renderParticle(Color color, Graphics2D g) {
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, f));
		g.setColor(color);
		g.fillRect((int)getX() - Game.getCam().getX(), (int)getY() - Game.getCam().getY(), getWidth(), getHeight());
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
	}
	
	@Override
	public void dispose() {
		
	}

}
