package studio.retrozoni.game.items;

import studio.retrozoni.engine.graphics.SpriteSheet;
import studio.retrozoni.engine.Engine;
import studio.retrozoni.engine.Theme;
import studio.retrozoni.game.objects.entity.EntityItem;
import studio.retrozoni.game.objects.entity.EntityTag;
import studio.retrozoni.ui.command.Commands;

import java.awt.image.BufferedImage;

public abstract class Item {
	
	private String name;
	private BufferedImage[] sprite;
	
	private int indexAnim;
	private int counter;
	private int timer;
	
	public static EntityItem Factory(Object... values) {
		EntityItem item;
		int id = (int)values[0];
		int x = (int)values[1];
		int y = (int)values[2];
        return switch (id) {
            case 0 -> null;
            case 1 -> {
                item = new EntityItem(id, new Key(), x, y);
                yield item;
            }
            case 2 -> {
                item = new EntityItem(id, new CMD(Commands.select), x, y);
                yield item;
            }
            case 3 -> {
                item = new EntityItem(id, new CMD(Commands.move), x, y);
                yield item;
            }
			case 4 -> {
				item = new EntityItem(id, new CMD(Commands.put), x, y);
				yield item;
			}
			case 5 -> {
				item = new EntityItem(id, new CMD(Commands.clear), x, y);
				yield item;
			}
			case 6 -> {
				item = new EntityItem(id, new CMD(Commands.remove), x, y);
				yield item;
			}
			case 7 -> {
				item = new EntityItem(id, new CMD(Commands.shot), x, y);
				yield item;
			}
			case 8 -> {
				item = new EntityItem(id, new CMD(Commands.revive), x, y);
				yield item;
			}
            case 9 -> {
                item = new EntityItem(id, new Bow(), x, y);
                yield item;
            }
            case 10 -> {
                item = new EntityItem(id, new Placeable(EntityTag.Bomb), x, y);
                yield item;
            }
			case 11 -> {
				item = new EntityItem(id, new Placeable(EntityTag.Barrel), x, y);
				yield item;
			}
            default -> throw new RuntimeException("Tile not exist");
        };
    }
	
	public Item(String name) {
		this.name = name.toLowerCase();
		this.timer = 15;
	}
	
	protected void setTimer(int newtimer) {
		this.timer = newtimer;
	}
	
	protected void setSprite(String sprite) {
		this.sprite = getSprite(sprite.toLowerCase());
	}
	
	protected void setSprite(BufferedImage[] sprite) {
		this.sprite = sprite;
	}
	
	public String getName() {
		return this.name;
	}

	public BufferedImage getSprite() {
		return this.sprite[indexAnim];
	}
	
	public BufferedImage getIcon() {
		return this.sprite[0];
	}
	
	public static BufferedImage[] getSprite(String name) {
		SpriteSheet spriteSheet = new SpriteSheet(Engine.ResPath+"/items/"+name+".png", Engine.SCALE);
		spriteSheet.replaceColor(Theme.PRIMARY, Theme.Primary.getRGB());
		spriteSheet.replaceColor(Theme.SECONDARY, Theme.Secondary.getRGB());
		spriteSheet.replaceColor(Theme.TERTIARY, Theme.Tertiary.getRGB());
		int lenght = (spriteSheet.getWidth())/16;
		BufferedImage[] sprites = new BufferedImage[lenght];
		for(int i = 0; i < lenght; i++) {
			sprites[i] = spriteSheet.getSprite(i*(16), 0, 16, 16);
		}
		return sprites;
	}
	
	public void tick() {
		counter++;
		if(counter > timer) {
			counter = 0;
			indexAnim++;
			if(indexAnim > sprite.length-1)
				indexAnim = 0;
		}
	}
	
	public void fun() {
		
	}
	
}
