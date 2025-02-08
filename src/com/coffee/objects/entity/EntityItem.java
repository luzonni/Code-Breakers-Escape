package com.coffee.objects.entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.coffee.items.Item;
import com.coffee.activity.game.Game;
import com.coffee.main.tools.Timer;
import com.coffee.objects.Variables;

public class EntityItem extends Entity {
	
	private Item item;
	private Timer[] timer = new Timer[2];
	private int flying;
	private boolean side;
	
	public EntityItem(int id, Item item, int x, int y) {
		super(id, x, y);
		this.item = item;
		timer[0] = new Timer(1);
		timer[1] = new Timer(2);
		setEffect(Variables.Alive);
		setEffect(Variables.Selectable);
		setEffect(Variables.Movable);
		setEffect(Variables.Removable);
	}
	
	@Override
	public String giveCommand(String[] values) {
        return super.giveCommand(values);
	}

	@Override
	public BufferedImage getSprite() {
		return item.getSprite();
	}

	@Override
	public void tick() {
		item.tick();
		Player p = Game.getPlayer();
		if(p.collidingWith(this)) {
			if(Game.getInventory().put(item)) {
				Game.getLevel().getEntities().remove(this);
			}
		}
		if(timer[0].pit()) {
			flying += 1;
		}else {
			flying -= 1;
		}
		if(timer[1].tiktak())
			side = !side;
	}

	public Item getItem() {
		return this.item;
	}

	@Override
	public void render(Graphics2D g) {
		double radians = Math.toRadians((flying/10d)*2);
		if(side)
			radians *= -1;
		g.rotate(radians, getX() + (double) getWidth() /2  - Game.getCam().getX(), getY() + getHeight()/2  - Game.getCam().getY());
		g.drawImage(item.getSprite(), (int)getX() - Game.getCam().getX(), (int)getY() + (int)(flying/10d) - Game.getCam().getY(), null);
		g.rotate(-radians, getX() + (double) getWidth() /2  - Game.getCam().getX(), getY() + getHeight()/2  - Game.getCam().getY());
	}
	
	@Override
	public void dispose() {
		
	}

}
