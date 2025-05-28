package studio.retrozoni.game.objects.entity;

import studio.retrozoni.game.Game;
import studio.retrozoni.game.items.CMD;
import studio.retrozoni.game.items.Item;
import studio.retrozoni.engine.Engine;
import studio.retrozoni.engine.Theme;
import studio.retrozoni.ui.command.Commands;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Computer extends Entity {
	
	private static BufferedImage[] sprite;
	private int index;
	
	public Computer(int id, int x, int y) {
		super(id, x, y);
		if(sprite == null)
			sprite = getSprite("server", Theme.Primary);
		this.index = Engine.RAND.nextInt(sprite.length);
	}

	@Override
	public BufferedImage getSprite() {
		return sprite[index];
	}

	@Override
	public void tick() {
		Player player = Game.getPlayer();
		if(player.collidingWith(this)) {
			Item[] items = Game.getInventory().getList();
			for(Item item : items) {
				if(item instanceof CMD) {
					Commands c = ((CMD)item).getCMD();
					Game.getLevel().addKey(c);
					Game.getInventory().remove(item);
				}
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
