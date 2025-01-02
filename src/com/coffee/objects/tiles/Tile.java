package com.coffee.objects.tiles;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.List;

import com.coffee.ui.command.Commands;
import com.coffee.graphics.SpriteSheet;
import com.coffee.main.Engine;
import com.coffee.main.Theme;
import com.coffee.main.activity.Game;
import com.coffee.objects.Objects;
import com.coffee.objects.Variables;
import com.coffee.objects.entity.Entity;

public abstract class Tile extends Objects {

	public static int getSize() {
		return Engine.SCALE *16;
	}
	
	public static Tile Factory(TileTag tag, int x, int y) {
		Tile tile;
		int id = tag.getId();
        return switch (tag) {
            case Air -> {
                tile = new Air(id, x, y);
                yield tile;
            }
            case Wall -> {
                tile = new Wall(id, x, y);
                yield tile;
            }
            case Floor -> {
                tile = new Floor(id, x, y);
                yield tile;
            }
            case Door -> {
                tile = new Door(id, x, y);
                yield tile;
            }
            case Gate -> {
                tile = new Gate(id, x, y);
                yield tile;
            }
            case Box -> {
                tile = new Box(id, x, y);
                yield tile;
            }
            case Crate -> {
                tile = new Crate(id, x, y);
                yield tile;
            }
			case Thron -> {
                tile = new Thorn(id, x, y);
                yield tile;
            }
			case Repellent -> {
                tile = new Repellent(id, x, y);
                yield tile;
            }
			case Fake_Wall -> {
                tile = new Fake_Wall(id, x, y);
                yield tile;
            }
			case Portalizer -> {
				tile = new Portalizer(id, x, y);
				yield tile;
			}
        };
    }
	
	public Tile(int id, int x, int y) {
		super(id);
		this.setX(x);
		this.setY(y);
		this.setSize(getSize(), getSize());
	}

	public BufferedImage[] getSprite(String name, Color color) {
		SpriteSheet spriteSheet = new SpriteSheet(Engine.ResPath + "/tiles/" + name + ".png", Engine.SCALE);
		spriteSheet.replaceColor(Theme.PRIMARY, color.getRGB());
		spriteSheet.replaceColor(Theme.SECONDARY, Theme.Color_Secondary.getRGB());
		spriteSheet.replaceColor(Theme.TERTIARY, Theme.Color_Tertiary.getRGB());
		int length = spriteSheet.getWidth()/16;
		BufferedImage[] sprites = new BufferedImage[length];
		for(int i = 0; i < length; i++) {
			sprites[i] = spriteSheet.getSprite(i*(16), 0, 16, 16);
		}
		return sprites;
	}
	
	public BufferedImage[] getSprite(String name, Color color, int verticalIndex) {
		SpriteSheet spriteSheet = new SpriteSheet(Engine.ResPath + "/tiles/" + name + ".png", Engine.SCALE);
		spriteSheet.replaceColor(Theme.PRIMARY, color.getRGB());
		spriteSheet.replaceColor(Theme.SECONDARY, Theme.Color_Secondary.getRGB());
		spriteSheet.replaceColor(Theme.TERTIARY, Theme.Color_Tertiary.getRGB());
		int length = (spriteSheet.getWidth())/16;
		BufferedImage[] sprites = new BufferedImage[length];
		for(int i = 0; i < length; i++) {
			sprites[i] = spriteSheet.getSprite(i*(16), 16*verticalIndex, 16, 16);
		}
		return sprites;
	}

	public boolean isSolid() {
		boolean breaking = false;
		try {
			List<Entity> entities = Game.getLevel().getEntities();
			for(int i = 0; i < entities.size(); i++) {
				Entity e = entities.get(i);
				if(e.getVar(Variables.Breakable) && e.collidingWith(this)) {
					breaking = true;
				}
			}
		}catch (Exception ignore) { }
		return this.getVar(Variables.Breakable) || breaking;
	}
	
	public void setSolid(boolean solid) {
		setVar(Variables.Breakable, solid);
	}
	
	public boolean centralizedWith(Entity o) {
		Point p1 = new Point((int)o.getX() + 1, (int)o.getY() + 1);
		Point p2 = new Point((int)o.getX() + o.getWidth() - 1, (int)o.getY() + 1);
		Point p3 = new Point((int)o.getX() + o.getWidth() - 1, (int)o.getY() + o.getHeight() - 1);
		Point p4 = new Point((int)o.getX() + 1, (int)o.getY() + o.getHeight() - 1);
		if((this.getBounds().contains(p1) && this.getBounds().contains(p2) && this.getBounds().contains(p3) && this.getBounds().contains(p4))) {
			return true;
		}
		return false;
	}
	
	@Override
	public String giveCommand(String[] keys) {
		String message = "Command no access";
		if(take(keys, Commands.move)) {
			EXE.move(keys, this);
			used(Commands.move);
			return "";
		}
		if(take(keys, Commands.remove)) {
			EXE.remove(this);
			used(Commands.remove);
			return "";
		}
		return message;
	}
	
	public void tick() {
		
	}
	
	public void render(Graphics2D g) {
		renderTile(getSprite(), g);
	}
	
	public void renderTile(BufferedImage Sprite, Graphics2D g) {
		int x = (int)getX();
		int y = (int)getY();
		g.drawImage(Sprite, x - Game.getCam().getX(), y - Game.getCam().getY(), null);
	}

}
