package com.coffee.objects.entity;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.coffee.main.Engine;
import com.coffee.main.Theme;
import com.coffee.activity.game.Game;
import com.coffee.main.tools.Timer;
import com.coffee.objects.Variables;
import com.coffee.objects.ai.Path;
import com.coffee.objects.particles.Interrogation;

public class Ant extends Entity {
	
	private static BufferedImage[] sprite;
	private int index_animation;
	private final Path ai;
	private final Timer timer;
	private int tt;
	
	public Ant(int id, int x, int y) {
		super(id, x, y);
		if(sprite == null) 
			sprite = getSprite("ant", Theme.Primary);
		ai = new Path();
		timer = new Timer(1);
		setDepth(1);
		setVar(Variables.Alive, true);
		setEffect(Variables.Reanimable);
	}

	@Override
	public BufferedImage getSprite() {
		return sprite[index_animation];
	}

	@Override
	public void tick() {
		if(!ai.follow(ai.direction())) {
			index_animation = 0;
			if(timer.tiktak()) {
				Game.getLevel().addParticle(new Interrogation(getMiddle().x, (int)getY() + getWidth()/2));
			Player P = Game.getPlayer();
			if(P.getOE().nextTile().isSolid() && Engine.RAND.nextInt(100) > 60 && !P.collidingWith(this)) 
				ai.buildPath(this, P.getMiddle(), 25);
			}
		}else {
			tt++;
			if(tt > 5) {
				tt = 0;
				index_animation++;
			}	
			if(index_animation > sprite.length-1)
				index_animation = 1;
		}
		Player P = Game.getPlayer();
		if(this.collidingWith(P))
			P.kill();
	}

	@Override
	public void render(Graphics2D g) {
		renderEntity(getSprite(), g);
		//ai.render(g);
	}
	
	@Override
	public void dispose() {
		sprite = null;
	}
}
