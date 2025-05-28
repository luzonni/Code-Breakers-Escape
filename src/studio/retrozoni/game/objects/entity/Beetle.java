package studio.retrozoni.game.objects.entity;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Beetle extends Entity {
	
	private static BufferedImage[] sprite;

	public Beetle(int id, int x, int y) {
		super(id, x, y);
		if(sprite == null)
			sprite = getSprite("beetle");
	}

	@Override
	public BufferedImage getSprite() {
		return sprite[0];
	}

	@Override
	public void tick() {

	}

	@Override
	public void render(Graphics2D g) {
		renderEntity(getSprite(), g);
	}
	
	@Override
	public void dispose() {
		sprite = null;
	}

}
