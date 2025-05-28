package studio.retrozoni.activities.game.objects.entity;

import studio.retrozoni.activities.game.Game;
import studio.retrozoni.engine.graphics.SpriteHandler;
import studio.retrozoni.engine.Engine;
import studio.retrozoni.engine.Theme;
import studio.retrozoni.activities.game.objects.Directions;
import studio.retrozoni.activities.game.objects.Variables;
import studio.retrozoni.activities.game.objects.ai.Path;
import studio.retrozoni.activities.game.objects.particles.Dust;
import studio.retrozoni.activities.game.objects.tiles.Tile;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Pluuter extends Entity {
	
	private static BufferedImage[][] sprites;
	private Path path;
	private boolean walking;
	private Directions dir;
	private Directions side;
	private int index_state;
	private int index;
	private int t_anim;
	
	
	public Pluuter(int id, int x, int y) {
		super(id, x, y);
		if(sprites == null) {
			sprites = new BufferedImage[3][];
			sprites[0] = getSprite("pluuter", Theme.Primary, 0);
			sprites[1] = getSprite("pluuter", Theme.Primary, 1);
			sprites[2] = getSprite("pluuter", Theme.Primary, 2);
		}
		setVar(Variables.Alive, true);
		this.dir = Directions.Idle;
		this.side = Directions.Right;
		this.path = new Path();
	}

	@Override
	public BufferedImage getSprite() {
		if(this.dir == Directions.Left || this.dir == Directions.Right)
			this.index_state = 1;
		else if(this.dir == Directions.Up || this.dir == Directions.Down)
			this.index_state = 2;
		return sprites[this.index_state][index];
	}

	@Override
	public void tick() {
		Player P = Game.getPlayer();
		anim_walkink(P);
	}
	
	private void anim_walkink(Player player) {
		Directions d = path.direction();
		this.dir = d;
		if(d == Directions.Left || d == Directions.Right)
			this.side = d;
		this.walking = path.follow(d);
		
		if(!this.walking) {
			this.walking = false;
			int x = (int)(player.getMiddle().x / Tile.getSize());
			int y = (int)(player.getMiddle().y / Tile.getSize());
			Tile tile_left = Game.getLevel().getTile(x + 2, y);
			Tile tile_right = Game.getLevel().getTile(x - 2, y);
			if(tile_left != null && !tile_left.isSolid() && !tile_left.centralizedWith(this))
				path.buildPath(this, tile_left.getMiddle(), 25);
			else if(tile_right != null && !tile_right.isSolid() && !tile_right.centralizedWith(this))
				path.buildPath(this, tile_right.getMiddle(), 25);
			else
				anim_attack(player);
		}else {
			t_anim++;
			if(t_anim > 4) {
				t_anim = 0;
				index++;
			}	
			if(index > sprites[0].length-1)
				index = 1;
		}
	}
	
	private void anim_attack(Player player) {
		this.index_state = 0;
		if(this.getMiddle().x > player.getMiddle().x)
			this.side = Directions.Left;
		else
			this.side = Directions.Right;
		t_anim++;
		if(t_anim > 10) {
			t_anim = 0;
			index++;
			if(index >= sprites[1].length-1) {
				index = 0;
				spit();
			}
		}	
		
		if(index > sprites[1].length-1) {
			index = 0;
		}
	}
	
	private void spit() {
		int x = getMiddle().x;
		if(this.side.equals(Directions.Left))
			x -= 4*Engine.SCALE;
		else
			x += 4*Engine.SCALE;
		for(int i = 0; i < 25; i++)
			Game.getLevel().addParticle(new Dust(x, getMiddle().y - 2*Engine.SCALE, this.side == Directions.Left ? Math.PI - Math.PI/6 + Engine.RAND.nextDouble()*(Math.PI/4) : 0 - Math.PI/6 + Engine.RAND.nextDouble()*(Math.PI/4)));
		Game.getLevel().addEntity(new Spit(x, getMiddle().y - 3*Engine.SCALE, this.side));
	}
	
	@Override
	public void render(Graphics2D g) {
		BufferedImage sprite = getSprite();
		if(this.side.equals(Directions.Left))
			sprite = SpriteHandler.Vertical(sprite);
		renderEntity(sprite, g);
	}
	
	@Override
	public void dispose() {
		sprites = null;
	}

}
