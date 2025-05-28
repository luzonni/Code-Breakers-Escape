package studio.retrozoni.activities.game.objects.particles;

import studio.retrozoni.activities.game.Game;
import studio.retrozoni.engine.exceptions.OutMap;
import studio.retrozoni.engine.graphics.SpriteSheet;
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
	public BufferedImage getSprite() {
		return null;
	}
	
	protected BufferedImage[] getSprite(String name, Color color) {
		SpriteSheet spriteSheet = new SpriteSheet(Engine.ResPath+"/particles/"+name+".png", Engine.SCALE);
		spriteSheet.replaceColor(0xffffffff, color.getRGB());
		spriteSheet.replaceColor(0xff000000, Theme.Tertiary.getRGB());
		int lenght = (spriteSheet.getWidth())/16;
		BufferedImage[] sprites = new BufferedImage[lenght];
		for(int i = 0; i < lenght; i++) {
			sprites[i] = spriteSheet.getSprite(i*(16), 0, 16, 16);
		}
		return sprites;
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
