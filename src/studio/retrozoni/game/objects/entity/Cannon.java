package studio.retrozoni.game.objects.entity;

import studio.retrozoni.game.Game;
import studio.retrozoni.engine.Engine;
import studio.retrozoni.engine.Theme;
import studio.retrozoni.engine.tools.Geometry;
import studio.retrozoni.engine.tools.Timer;
import studio.retrozoni.game.objects.Variables;
import studio.retrozoni.game.objects.tiles.Tile;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Cannon extends Entity {
	
	private Timer timer;
	private static BufferedImage base;
	private static BufferedImage cannon;
	private double radians;

	public Cannon(int id, int x, int y) {
		super(id, x, y);
		if(base == null)
			base = getSprite("Cannon", Theme.Tertiary)[0];
		if(cannon == null)
			cannon = getSprite("Cannon", Theme.Primary)[1];
		timer = new Timer(2 + Engine.RAND.nextInt(5));
		setVar(Variables.Selectable, true);
		setVar(Variables.Movable, true);
	}

	@Override
	public BufferedImage getSprite() {
		BufferedImage sprite = new BufferedImage(base.getWidth(), base.getHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics g = sprite.getGraphics();
		g.drawImage(base, 0, 0, null);
		g.drawImage(cannon, 0, 0, null);
		g.dispose();
		return sprite;
	}

	@Override
	public void tick() {
		Player p = Game.getPlayer();
		if(Geometry.Theta(this.getMiddle(), p.getMiddle()) < Tile.getSize()*12)
			radians = Math.atan2(this.getMiddle().getY() - p.getMiddle().getY(), this.getMiddle().getX() - p.getMiddle().getX()) - Math.PI/2;
		if(timer.tiktak()) {
			double r = radians - Math.PI/2; 
			int x = (int)getMiddle().x + (int)(Math.cos(r)*Tile.getSize()/1.75);
			int y = (int)getMiddle().y + (int)(Math.sin(r)*Tile.getSize()/1.75);
			Game.getLevel().addEntity(new IronBall(x, y, r, Engine.SCALE *3));
			//TODO Sound.play("poft");
		}
	}
	
	@Override
	public void render(Graphics2D g) {
		renderEntity(base, g);
		g.rotate(radians, getX() - Game.getCam().getX() + getWidth()/2, getY() - Game.getCam().getY() + getHeight()/2);
		renderEntity(cannon, g);
		g.rotate(-radians, getX() - Game.getCam().getX() + getWidth()/2, getY() - Game.getCam().getY() + getHeight()/2);
	}
	
	@Override
	public void dispose() {
		base = null;
		cannon = null;
	}

}
