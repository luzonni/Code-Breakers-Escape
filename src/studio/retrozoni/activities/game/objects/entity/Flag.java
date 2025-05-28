package studio.retrozoni.activities.game.objects.entity;

import studio.retrozoni.activities.game.Game;
import studio.retrozoni.engine.Theme;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Flag extends Entity {
	
	private int indexAnim;
	private int counter;
	private static BufferedImage[] sprites;
	
	public Flag(int id, int x, int y) {
		super(id, x, y);
		if(sprites == null)
			sprites = getSprite("Flag", Theme.Primary);
		setDepth(0);
	}
	
	@Override
	public String giveCommand(String[] values) {
		String message = super.giveCommand(values);
		
		return message;
	}

	@Override
	public BufferedImage getSprite() {
		return sprites[indexAnim];
	}
	
	@Override
	public void tick() {
		Player p = Game.getPlayer();
		if(p.collidingWith(this)) 
			Game.end();
		counter++;
		if(counter > 10 ) {
			counter = 0;
			indexAnim++;
			if(indexAnim > sprites.length-1)
				indexAnim = 0;
		}
	}

	@Override
	public void render(Graphics2D g) {
		renderEntity(sprites[indexAnim], g);
	}
	
	@Override
	public void dispose() {
		sprites = null;
	}

}
