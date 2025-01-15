package com.coffee.objects.entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.coffee.ui.command.Commands;
import com.coffee.graphics.SpriteSheet;
import com.coffee.main.Engine;
import com.coffee.main.Theme;
import com.coffee.main.activity.Game;
import com.coffee.main.sound.Sound;
import com.coffee.main.sound.Sounds;
import com.coffee.objects.Directions;
import com.coffee.objects.Objects;
import com.coffee.objects.Variables;
import com.coffee.objects.particles.Kaboom;
import com.coffee.objects.tiles.Tile;

public abstract class Entity extends Objects {
	
	private final Orienteering_Physics oe;
	
	public static Entity Factory(EntityTag tag, int x, int y) {
		Entity entity;
		int id = tag.getId();
        return switch (tag) {
			case Player -> {
                entity = new Player(id, x, y, Directions.Down);
                yield entity;
            }
			case Flag -> {
                entity = new Flag(id, x, y);
                yield entity;
            }
			case Cannon -> {
                entity = new Cannon(id, x, y);
                yield entity;
            }
			case CrossBow -> {
                entity = new Crossbow(id, x, y);
                yield entity;
            }
			case Ant -> {
                entity = new Ant(id, x, y);
                yield entity;
            }
			case Button -> {
                entity = new Button(id, x, y);
                yield entity;
            }
			case Spine -> {
				entity = new Spine(id, x, y);
				yield entity;
			}
			case Computer -> {
                entity = new Computer(id, x, y);
                yield entity;
            }
			case Skull -> {
                entity = new Skull(id, x, y);
                yield entity;
            }
			case Pluuter -> {
                entity = new Pluuter(id, x, y);
                yield entity;
            }
			case Karto -> {
                entity = new Karto(id, x, y);
                yield entity;
            }
            case Saw -> {
                entity = new Saw(id, x, y);
                yield entity;
            }
			case Bomb -> {
                entity = new Bomb(id, x, y);
                yield entity;
            }
            case Fish -> {
                entity = new Fish(id, x, y);
                yield entity;
            }
			case Barrel -> {
				entity = new Barrel(id, x, y);
				yield entity;
			}
			case Trampoline_UpRight -> {
				entity = new Trampoline(id, x, y, Directions.UpRight);
				yield entity;
			}
			case Trampoline_RightDown -> {
				entity = new Trampoline(id, x, y, Directions.RightDown);
				yield entity;
			}
			case Trampoline_DownLeft -> {
				entity = new Trampoline(id, x, y, Directions.DownLeft);
				yield entity;
			}
			case Trampoline_LeftUp -> {
				entity = new Trampoline(id, x, y, Directions.LeftUp);
				yield entity;
			}
			case Blaster_Up -> {
				entity = new Blaster(id, x, y, Directions.Up);
				yield entity;
			}
			case Blaster_Right -> {
				entity = new Blaster(id, x, y, Directions.Right);
				yield entity;
			}
			case Blaster_Down -> {
				entity = new Blaster(id, x, y, Directions.Down);
				yield entity;
			}
			case Blaster_Left -> {
				entity = new Blaster(id, x, y, Directions.Left);
				yield entity;
			}
			case Cleft -> {
				entity = new Cleft(id, x, y);
				yield entity;
			}
			case Worm -> {
				entity = new Worm(id, x, y);
				yield entity;
			}
			case Vase -> {
				entity = new Vase(id, x, y);
				yield entity;
			}
        };
    }
	
	public BufferedImage[] getSprite(String name) {
		SpriteSheet spriteSheet = new SpriteSheet(Engine.ResPath+"/entity/"+name+".png", Engine.SCALE);
		spriteSheet.replaceColor(Theme.PRIMARY, Theme.Primary.getRGB());
		spriteSheet.replaceColor(Theme.SECONDARY, Theme.Secondary.getRGB());
		spriteSheet.replaceColor(Theme.TERTIARY, Theme.Tertiary.getRGB());
		int length = spriteSheet.getWidth()/16;
		BufferedImage[] sprites = new BufferedImage[length];
		for(int i = 0; i < length; i++) {
			sprites[i] = spriteSheet.getSprite(i*16, 0, 16, 16);
		}
		return sprites;
	}
	
	public BufferedImage[] getSprite(String name, Color color) {
		SpriteSheet spriteSheet = new SpriteSheet(Engine.ResPath+"/entity/"+name+".png", Engine.SCALE);
		spriteSheet.replaceColor(Theme.PRIMARY, color.getRGB());
		spriteSheet.replaceColor(Theme.SECONDARY, Theme.Secondary.getRGB());
		spriteSheet.replaceColor(Theme.TERTIARY, Theme.Tertiary.getRGB());
		int lenght = (spriteSheet.getWidth())/16;
		BufferedImage[] sprites = new BufferedImage[lenght];
		for(int i = 0; i < lenght; i++) {
			sprites[i] = spriteSheet.getSprite(i*(16), 0, 16, 16);
		}
		return sprites;
	}
	
	public BufferedImage[] getSprite(String name, Color color, int verticalIndex) {
		SpriteSheet spriteSheet = new SpriteSheet(Engine.ResPath+"/entity/"+name+".png", Engine.SCALE);
		spriteSheet.replaceColor(Theme.PRIMARY, color.getRGB());
		spriteSheet.replaceColor(Theme.SECONDARY, Theme.Secondary.getRGB());
		spriteSheet.replaceColor(Theme.TERTIARY, Theme.Tertiary.getRGB());
		int lenght = (spriteSheet.getWidth())/16;
		BufferedImage[] sprites = new BufferedImage[lenght];
		for(int i = 0; i < lenght; i++)
			sprites[i] = spriteSheet.getSprite(i*16, 16*verticalIndex, 16, 16);
		return sprites;
	}
	
	public Entity(int id, int x, int y) {
		super(id);
		this.setX(x);
		this.setY(y);
		this.oe = new Orienteering_Physics(this);
	}
	
	@Override
	public String giveCommand(String[] keys) {
		String message = "Command no access";

		if(take(keys, Commands.remove)) {
			EXE.remove(this);
			used(Commands.remove);
			return "";
		}

		if(take(keys, Commands.move)) {
			EXE.move(keys, this);
			used(Commands.move);
			return "";
		}

		if(take(keys, Commands.freeze)) {
			EXE.freeze(this);
			used(Commands.freeze);
			return "";
		}

		if(take(keys, Commands.revive)) {
			EXE.revive(this);
			used(Commands.revive);
			return "";
		}

		return message;
	}
	
	public Orienteering_Physics getOE() {
		return this.oe;
	}

	public void setDirection(Directions newdir) {
		this.oe.setDirection(newdir);
	} 
	
	public void setEffect(Variables var) {
		this.setVar(var, true);
	}
	
	public void kill() {
		for(int i = 0; i < 40; i++)
			Game.getLevel().addParticle(new Kaboom(getMiddle().x, getMiddle().y));
		Game.getLevel().getEntities().remove(this);
		Sound.play(Sounds.Die);
		if(this instanceof Player ) {
			new Thread(() -> {
                try {
                    Thread.sleep(1000);
                	Game.restart();
                } catch (Exception ignore) { }
            }).start();
		}
	}

	public void disappear() {
		Game.getLevel().getEntities().remove(this);
		if(this instanceof Player ) {
			new Thread(() -> {
				try {
					Thread.sleep(1000);
					Game.restart();
				} catch (Exception ignore) { }
			}).start();
		}
	}
	
	public boolean collidingWith(Entity o) {
		int xMe = ((int)getX() + getWidth()/2) / Tile.getSize();
		int yMe = ((int)getY() + getHeight()/2) / Tile.getSize();
		Tile meTile = Game.getLevel().getTile(xMe, yMe);
		int xO = ((int)o.getX() + o.getWidth()/2) / Tile.getSize();
		int yO = ((int)o.getY() + o.getHeight()/2) / Tile.getSize();
		Tile oTile = Game.getLevel().getTile(xO, yO);
        return getBounds().intersects(o.getBounds()) && meTile == oTile;
    }
	
	public void renderEntity(BufferedImage sprite, Graphics2D g) {
		int x = (int)getX();
		int y = (int)getY();
		g.drawImage(sprite, x - Game.getCam().getX(), y - Game.getCam().getY(), getWidth(), getHeight(), null);
	}
	
}
