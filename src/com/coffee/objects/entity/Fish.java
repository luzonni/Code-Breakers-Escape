package com.coffee.objects.entity;

import java.awt.*;
import java.awt.image.BufferedImage;

import com.coffee.graphics.Flip;
import com.coffee.main.activity.Game;
import com.coffee.objects.Directions;
import com.coffee.objects.Variables;
import com.coffee.objects.tiles.Tile;

public class Fish extends Entity {
	
	private static BufferedImage[] sprite;
	private int indexAnim, counter;
	private double speed;
	
	public Fish(int id, int x, int y) {
		super(id, x, y);
		if(sprite == null)
			sprite = getSprite("fish");
		setDirection(Directions.Left);
		setVar(Variables.Selectable, true);
		setVar(Variables.Removable, true);
		setVar(Variables.Alive, true);
		setVar(Variables.Reanimable, true);
	}

	@Override
	public BufferedImage getSprite() {
		return sprite[0];
	}

	@Override
	public void tick() {
		counter++;
		if(counter > 10 ) {
			counter = 0;
			indexAnim++;
			if(indexAnim == 1)
				speed = 5;
			if(indexAnim > sprite.length-1)
				indexAnim = 0;
		}
		swim();
		if(Game.getPlayer().collidingWith(this))
			Game.getPlayer().kill();
	}
	
	private void swim() {
		int side = 0;
		if(getOE().getDirection() == Directions.Right)
			side = 1;
		if(getOE().getDirection() == Directions.Left)
			side = -1;
		int x = (getMiddle().x + side*getWidth()/2) / Tile.getSize();
		int y = (getMiddle().y) / Tile.getSize();
		Tile t = Game.getLevel().getTile(x, y);
		if(t != null && t.isSolid()) {
			speed = 0;
			getOE().setDirection(getOE().getReverse());
			indexAnim = 0;
		}
		double currentSpeed = side * speed;
		setX(getX() + currentSpeed);
		if(speed > 0)
			speed -= 0.1;
	}

	@Override
	public void render(Graphics2D g) {
		BufferedImage currentSprite = sprite[indexAnim];
		if(getOE().getDirection() == Directions.Right)
			currentSprite = Flip.Vertical(currentSprite);
		renderEntity(currentSprite, g);
	}
	
	@Override
	public void dispose() {
		sprite = null;
	}

}
