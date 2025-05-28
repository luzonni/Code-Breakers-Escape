package studio.retrozoni.game.objects.entity;

import studio.retrozoni.engine.Engine;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Berne extends Entity {
	
	private int timer;
	private int indexAnim;
	
	private static BufferedImage[] sprite;
	
	public Berne(int id, int x, int y) {
		super(id, x, y);
		if(sprite == null)
			sprite = getSprite("berne");
	}

	@Override
	public BufferedImage getSprite() {
		return sprite[0];
	}

	@Override
	public void tick() {
		timer++;
		if(timer > 15) {
			timer = 0;
			indexAnim++;
			this.setX(this.getX() + Engine.SCALE);
		}
		if(indexAnim > sprite.length-1) {
			indexAnim = 0;
		}
	}

	@Override
	public void render(Graphics2D g) {
		renderEntity(sprite[indexAnim], g);
	}
	
	@Override
	public void dispose() {
		sprite = null;
	}

}
