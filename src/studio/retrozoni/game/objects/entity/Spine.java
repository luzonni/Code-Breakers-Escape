package studio.retrozoni.game.objects.entity;

import studio.retrozoni.game.Game;
import studio.retrozoni.engine.Theme;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Spine extends Entity {

	private static BufferedImage[] sprite;
	private int indexAnim;
	private boolean actived = false;
	private int timer;
	
	public Spine(int id, int x, int y) {
		super(id, x, y);
		if(sprite == null)
			sprite = this.getSprite("spine", Theme.Primary);
	}

	@Override
	public BufferedImage getSprite() {
		return sprite[indexAnim];
	}

	@Override
	public void tick() {
		Player p = Game.getPlayer();
		if(getOE().centralizedWith(p)) {
			actived = true;
			if(indexAnim == sprite.length-1) {
				p.kill();
			}
		}
		if(actived) {
			timer++;
			if(timer > 5) {
				timer = 0;
				indexAnim++;
			}
			if(indexAnim >= sprite.length-1) {
				indexAnim = sprite.length-1;
				
			}
		}
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
