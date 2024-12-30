package com.coffee.objects.entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.coffee.graphics.Flip;
import com.coffee.main.Engine;
import com.coffee.main.activity.Game;
import com.coffee.main.tools.Timer;
import com.coffee.objects.Directions;
import com.coffee.objects.Variables;

public class Skull extends Entity {
	
	private static BufferedImage[] sprites;
	private final int index_sprite;
	private final Timer timer;

	public Skull(int id, int x, int y) {
		super(id, x, y);
		if(sprites == null)
			sprites = getSprite("skull", Engine.Color_Primary);
		index_sprite = Engine.RAND.nextInt(sprites.length);
		getValues().addInt("speed", Engine.GameScale * 8);
		this.timer = new Timer(2);
		this.setDepth(1);
		this.setVar(Variables.Selectable, true);
		this.setVar(Variables.Freeze, false);
		this.setVar(Variables.Alive, true);
	}

	@Override
	public BufferedImage getSprite() {
		BufferedImage image = sprites[index_sprite];
		Directions dir = getOE().getDirection();
		switch (dir) {
			case Up: 
				image = Flip.Horizontal(image);
				break;
			case Right:
				image = Flip.Rotate(image, -90);
				break;
			case Left:
				image = Flip.Rotate(image, 90);
				break;
			default:
				break;
		}
		return image;
	}

	@Override
	public void tick() {
		if(this.timer.tiktak())
			setDirection();
		getOE().sliding(getSpeed());
		Player p = Game.getPlayer();
		if(p.collidingWith(this))
			p.kill();
	}
	
	private void setDirection() {
		List<Directions> possible_dir = new ArrayList<Directions>();
		if(!getOE().colliding(Directions.Down))
			possible_dir.add(Directions.Down);
		if(!getOE().colliding(Directions.Up))
			possible_dir.add(Directions.Up);
		if(!getOE().colliding(Directions.Left))
			possible_dir.add(Directions.Left);
		if(!getOE().colliding(Directions.Right))
			possible_dir.add(Directions.Right);
		getOE().setDirection(chose_direction(possible_dir));
	}

	private Directions chose_direction(List<Directions> possible_dir) {
		Player p = Game.getPlayer();
		if(possible_dir.contains(Directions.Down) && p.getY() > getY())
			return Directions.Down;
		if(possible_dir.contains(Directions.Up) && p.getY() < getY())
			return Directions.Up;
		if(possible_dir.contains(Directions.Left) && p.getX() < getX())
			return Directions.Left;
		if(possible_dir.contains(Directions.Right) && p.getY() > getY())
			return Directions.Right;
		return possible_dir.get(Engine.RAND.nextInt(possible_dir.size()));
	}
	
	public int getSpeed() {
		return this.getValues().getInt("speed");
	}

	@Override
	public void render(Graphics2D g) {
		renderEntity(getSprite(), g);
	}
	
	@Override
	public void dispose() {
		sprites = null;
	}

}
