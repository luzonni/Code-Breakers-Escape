package studio.retrozoni.activities.game.objects.entity;

import studio.retrozoni.activities.game.Game;
import studio.retrozoni.engine.graphics.Flip;
import studio.retrozoni.engine.graphics.SpriteSheet;
import studio.retrozoni.engine.inputs.Keyboard;
import studio.retrozoni.engine.Engine;
import studio.retrozoni.engine.Theme;
import studio.retrozoni.engine.sound.Sound;
import studio.retrozoni.engine.sound.Sounds;
import studio.retrozoni.activities.game.objects.Directions;
import studio.retrozoni.activities.game.objects.Variables;
import studio.retrozoni.activities.game.objects.particles.Kaboom;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Player extends Entity {
	
	private BufferedImage[][] sprite;
	private boolean collide;
	private int indexAnim;
	private int ticksAnim = 0;
	
	private void buildSprites() {
		SpriteSheet sp = new SpriteSheet(Engine.ResPath+"/entity/Player.png", Engine.SCALE);
		sp.replaceColor(0xffffffff, Theme.Primary.getRGB());
		sp.replaceColor(0xff000000, Theme.Tertiary.getRGB());
		sprite = new BufferedImage[2][7];
		for(int i = 0; i < sprite[0].length; i++) {
			sprite[0][i] = sp.getSprite(i*16, 0);
		}
		for(int i = 0; i < sprite[1].length; i++) {
			sprite[1][i] = sp.getSprite(i*16, 16);
		}
	}
	
	public Player(int id, int x, int y, Directions direction) {
		super(id, x, y);
		buildSprites();
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
			indexAnim++;
		}
		if(indexAnim > sprite[getOE().colliding(getOE().getDirection()) ? 0 : 1].length - 1) {
			indexAnim = 0;
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
		return sprite[0][indexAnim];
	}
	
	public BufferedImage Sprite() {
		BufferedImage image = sprite[getOE().colliding(getOE().getDirection()) ? 0 : 1][indexAnim];
		switch (getOE().getDirection()) {
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
	public void render(Graphics2D g) {
		renderEntity(Sprite(), g);
	}
	
	@Override
	public void dispose() {
		
	}
}
