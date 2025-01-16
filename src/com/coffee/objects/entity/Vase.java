package com.coffee.objects.entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.coffee.main.Theme;
import com.coffee.main.activity.game.Game;
import com.coffee.objects.Variables;
import com.coffee.objects.tiles.Tile;

public class Vase extends Entity {
	
	protected static BufferedImage[] sprite;
	private int durability;
	
	public Vase(int id, int x, int y) {
		super(id, x, y);
		if(sprite == null)
			sprite = getSprite("vase", Theme.Primary);
		setVar(Variables.Selectable, true);
		setVar(Variables.Movable, true);
		setEffect(Variables.Breakable);
	}

	@Override
	public BufferedImage getSprite() {
		return sprite[durability];
	}

	@Override
	public void tick() {
		Player P = Game.getPlayer();
		Tile currentTile = Game.getLevel().getTile((int)getX()/Tile.getSize(), (int)getY()/Tile.getSize());
		if(P.getOE().nextTile() == currentTile) {
			durability = 1;
		}else if(durability == 1){
			durability = 2;
		}
		if(durability >= sprite.length-1) {
			setVar(Variables.Breakable, false);
			durability = sprite.length-1;
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
