package studio.retrozoni.activities.game.objects.entity;

import studio.retrozoni.activities.game.Game;
import studio.retrozoni.engine.graphics.SpriteHandler;
import studio.retrozoni.engine.graphics.sprites.Sprites;
import studio.retrozoni.engine.inputs.Keyboard;
import studio.retrozoni.engine.Engine;
import studio.retrozoni.engine.sound.Sound;
import studio.retrozoni.engine.sound.Sounds;
import studio.retrozoni.activities.game.objects.Directions;
import studio.retrozoni.activities.game.objects.Variables;
import studio.retrozoni.activities.game.objects.particles.Kaboom;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends Entity {

	private boolean collide;
	private int ticksAnim = 0;
	
	public Player(int id, int x, int y, Directions direction) {
		super(id, x, y);
		loadSprite("player");
		getValues().addInt("speed", Engine.SCALE * 8);
		setDepth(3);
		setVar(Variables.Alive, true);
		setVar(Variables.Selectable, true);
		setVar(Variables.Movable, false);
		setVar(Variables.Removable, false);
		getOE().setDirection(direction);
	}
	
	public void tick() {
		ticksAnim++;
		if(ticksAnim > 7) {
			ticksAnim = 0;
			getSheet().plusIndex();
		}
		keyDirection();
		getOE().slide(getSpeed());
	}

	@Override
	public void kill() {
		for(int i = 0; i < 40; i++)
			Game.getLevel().addParticle(new Kaboom(getMiddle().x, getMiddle().y));
		Game.getLevel().getEntities().remove(this);
		Sound.play(Sounds.Die);
		new Thread(() -> {
			try {
				Thread.sleep(1000);
				Game.restart();
			} catch (Exception ignore) { }
		}).start();
	}
	
	private void keyDirection() {
		if(!getOE().colliding(getOE().getDirection())) {
			collide = true;
			return;
		}
		if(collide) {
			collide = false;
			//TODO Game.getCam().impact(getOE().getDirection(), Engine.GameScale);
		}
		Directions new_dir = this.getOE().getDirection();
		if((Keyboard.KeyPressing("W") || Keyboard.KeyPressing("Up"))) 
			new_dir = Directions.Up;
		if((Keyboard.KeyPressing("D") || Keyboard.KeyPressing("Right"))) 
			new_dir = Directions.Right;
		if((Keyboard.KeyPressing("S") || Keyboard.KeyPressing("Down"))) 
			new_dir = Directions.Down;
		if((Keyboard.KeyPressing("A") || Keyboard.KeyPressing("Left"))) 
			new_dir = Directions.Left;
		if(!getOE().getDirection().equals(new_dir))
			setDirection(new_dir);
	}
	
	private void dynamics() {
		switch(getOE().getDirection()) {
		case Up:
			if(!getOE().colliding(Directions.Up)) {
				this.setY(this.getY() - this.getSpeed());
			}
			break;
		case Right:
			if(!getOE().colliding(Directions.Right)) {
				this.setX(this.getX() + this.getSpeed());
			}
			break;
		case Down:
			if(!getOE().colliding(Directions.Down)) {
				this.setY(this.getY() + this.getSpeed());
			}
			break;
		case Left:
			if(!getOE().colliding(Directions.Left)) {
				this.setX(this.getX() - this.getSpeed());
			}
			break;
		default:
			break;
		}
	}
	
	@Override
	public String giveCommand(String[] values) {
		String message = super.giveCommand(values);
		
		switch (values[0]) {
			case "speed", "Speed":
				setSpeed(Integer.parseInt(values[1]));
				message = "Speed change to " + values[1];
				break;
		}
		return message;
	}
	
	public int getSpeed() {
		return this.getValues().getInt("speed");
	}
	
	public void setSpeed(int newSpeed) {
		this.getValues().setInt("speed", newSpeed);
	}
	
	@Override
	public BufferedImage getSprite() {
		int state = getOE().colliding(getOE().getDirection()) ? 0 : 1;
		getSheet().setState(state);
		return getSheet().getSprite();
	}
	
	public BufferedImage Sprite() {
		BufferedImage image = getSprite();
		switch (getOE().getDirection()) {
		case Up: 
			image = SpriteHandler.Horizontal(image);
			break;
		case Right:
			image = SpriteHandler.Rotate(image, -90);
			break;
		case Left:
			image = SpriteHandler.Rotate(image, 90);
			break;
		default:
			break;
		}
		return image;
	}

	@Override
	public void render(Graphics2D g) {
		renderEntity(Sprite(), g);
	}
	
	@Override
	public void dispose() {
		
	}
}
