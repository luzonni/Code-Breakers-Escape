package studio.retrozoni.activities.game.objects;

import studio.retrozoni.activities.game.Game;
import studio.retrozoni.engine.exceptions.OutMap;
import studio.retrozoni.engine.Engine;
import studio.retrozoni.activities.game.objects.entity.Entity;
import studio.retrozoni.activities.game.objects.entity.EntityTag;
import studio.retrozoni.activities.game.objects.tiles.Tile;
import studio.retrozoni.activities.game.objects.tiles.TileTag;
import studio.retrozoni.engine.graphics.sprites.Sprites;
import studio.retrozoni.engine.ui.command.Receiver;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Comparator;

public abstract class Objects implements Receiver {

	private Sprites sheet;
	private final Values values;
	
	public Objects(int ID) {
		this.values = new Values();
		this.values.addInt("id", ID);
		this.values.addDouble("x", 0);
		this.values.addDouble("y", 0);
		this.values.addInt("width", Tile.getSize());
		this.values.addInt("height", Tile.getSize());
		this.values.addInt("depth", 0);

		for(Variables type : Variables.values())
				this.values.addBoolean(type.getName(), false);
	}

	protected void loadSprites(String module, String spriteName) {
		this.sheet = Engine.sheetHolder.getSheet(module, spriteName);
	}

	protected abstract void loadSprite(String name);

	public BufferedImage getSprite() {
		return getSheet().getSprite();
	}

	public Sprites getSheet() {
		return this.sheet;
	}
	
	public int ID() {
		return getValues().getInt("id");
	}
	
	public Values getValues() {
		return this.values;
	}
	
	public boolean getVar(Variables type) {
		return this.getValues().getBoolean(type.getName());
	}
	
	public boolean collidingWith(Objects o) {
		return this.getBounds().intersects(o.getBounds());
	}
	
	public void setVar(Variables type, boolean bool) {
		this.getValues().setBoolean(type.getName(), bool);
	}
	
	protected void setDepth(int newDepth) {
		this.values.setInt("depth", newDepth);
	}
	
	public void setX(double X) {
		if(Engine.ACTIVITY instanceof Game) {
			Rectangle bounds = Game.getLevel().getBounds();
			if (X < bounds.x || X > bounds.width)
				throw new OutMap("Out bounds map");
		}
		this.getValues().setDouble("x", X);
	}
	
	public void setY(double Y) {
		if(Engine.ACTIVITY instanceof Game) {
			Rectangle bounds = Game.getLevel().getBounds();
			if(Y < bounds.y || Y > bounds.height)
				throw new OutMap("Out bounds map");
		}
		this.getValues().setDouble("y", Y);
	}
	
	public double getX() {
		return this.getValues().getDouble("x");
	}
	
	public double getY() {
		return this.getValues().getDouble("y");
	}
	
	public int getWidth() {
		return this.getValues().getInt("width");
	}
	
	public int getHeight() {
		return this.getValues().getInt("height");
	}

	public void setSize(int Width, int Height) {
		this.getValues().setInt("width", Width);
		this.getValues().setInt("height", Height);
	}
	
	public Point getMiddle() {
		return new Point((int)this.getX() + this.getHeight()/2, (int)this.getY() + this.getHeight()/2);
	}
	
	public static Comparator<Objects> Depth = new Comparator<Objects>() {
		@Override
		public int compare(Objects n0, Objects n1) {
			return Integer.compare(n0.values.getInt("depth"), n1.values.getInt("depth"));
		}
	};
	
	public abstract void tick();
	
	public abstract void render(Graphics2D g);
	
	public abstract void dispose();
	
	public Rectangle getBounds() {
		return new Rectangle((int)getX(), (int)getY(), getWidth(), getHeight());
	}
	
	public static void disposeAll() {
		boolean entities = true;
		int indexEntities = 1;
		while(entities) {
			try {
				Entity en = Entity.Factory(EntityTag.values()[indexEntities], 0, 0);
				en.dispose();
				indexEntities++;
			}catch (Exception e) {
				entities = false;
			}
		}
		boolean tiles = true;
		int indexTiles = 0;
		while(tiles) {
			try {
				Tile t = Tile.Factory(TileTag.values()[indexTiles], 0, 0);
				t.dispose();
				indexTiles++;
			}catch (Exception e) {
				tiles = false;
			}
		}
	}
	
}
