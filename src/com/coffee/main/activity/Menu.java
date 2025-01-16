package com.coffee.main.activity;

import java.awt.AlphaComposite;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import com.coffee.inputs.Mouse;
import com.coffee.inputs.Mouse_Button;
import com.coffee.inputs.buttons.Button;
import com.coffee.main.activity.creator.Creator;
import com.coffee.main.activity.option.Options;
import com.coffee.main.sound.Musics;
import com.coffee.main.sound.Sound;
import com.coffee.ui.command.Receiver;
import com.coffee.graphics.FontG;
import com.coffee.graphics.SpriteSheet;
import com.coffee.main.Engine;
import com.coffee.main.tools.Geometry;
import com.coffee.main.Theme;
import com.coffee.main.tools.Responsive;
import com.coffee.objects.tiles.Tile;

public class Menu implements Activity, Receiver {

	private Button C, P, Q, O;
	
	private BufferedImage Logo;
	private Responsive logo_res;
	private BufferedImage background;
	
	private static List<Bubble> bubbles;
	
	public Menu() {
		Responsive center = Responsive.createPoint(null, 50, 10);
		C = new Button("Creator", -5, 0, center, 10);
		P = new Button("Play", -10, 0, C.getResponsive(), 10);
		O = new Button("Options", 5, 0, center, 10);
		Q = new Button("Quit", 10, 0, O.getResponsive(), 8);
		if(bubbles == null)
			bubbles = new ArrayList<Bubble>();
		SpriteSheet sp = new SpriteSheet(Engine.ResPath + "/ui/LogoName.png", 3*Engine.SCALE);
		sp.replaceColor(0xffffffff, Theme.Primary.getRGB());
		sp.replaceColor(0xff000000, Theme.Tertiary.getRGB());
		Logo = sp.getImage();
		logo_res = Responsive.createRectangle(null, new Rectangle(Logo.getWidth(), Logo.getHeight()), 50, 50);
	}
	
	@Override
	public void enter() {
		for(int i = 0; i < 50; i++) {
			int x = Engine.RAND.nextInt(Engine.getWidth() - 16 * Engine.SCALE);
			int y = Engine.RAND.nextInt(Engine.getHeight() - 16 * Engine.SCALE);
			Bubble bu = new Bubble(x, y);
			bubbles.add(bu);
		}
		Sound.play(Musics.Music1, true);
	}
	
	private void drawBackground() {
		background = new BufferedImage(Engine.getWidth(), Engine.getHeight(), BufferedImage.TYPE_INT_ARGB);
		SpriteSheet sp = new SpriteSheet(Engine.ResPath+"/ui/background.png", 2*Engine.SCALE);
		sp.replaceColor(0xffffffff, Theme.Secondary.getRGB());
		sp.replaceColor(0xff000000, Theme.Tertiary.getRGB());
		Graphics2D g = (Graphics2D)background.getGraphics();
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.25f));
		int w = Engine.getWidth() / sp.getWidth();
		int h = Engine.getHeight() / sp.getHeight();
		for(int y = 0; y < h; y++) {
			for(int x = 0; x < w; x++) {
				g.drawImage(sp.getImage(), x*sp.getImage().getWidth(), y*sp.getImage().getHeight(), null);
			}
		}
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
		g.dispose();
	}
	
	public void tick() {
		logo_res.setPosition();
		if(background == null || (background.getWidth() != Engine.getWidth() || background.getHeight() != Engine.getHeight()))
			drawBackground();
		for(int i = 0; i < bubbles.size(); i++) {
			bubbles.get(i).tick();
		}
		if(Mouse.click(Mouse_Button.LEFT)) {
			int x = Mouse.getX();
			int y = Mouse.getY();
			for(int i = 0; i < bubbles.size(); i++) {
				Bubble b = bubbles.get(i);
				if(Geometry.Theta(x, y, b.middle().x, b.middle().y) < 64*Engine.SCALE) {
					double a = Math.atan2(b.middle().y - y, b.middle().x - x);
					b.direction = a;
					b.speed += 15;
				}
			}
		}
		if(P.function()) {
			Engine.setActivity(new LevelMap());
		}
		if(C.function())
			Engine.setActivity(new Creator(null));
		if(O.function())
			Engine.setActivity(new Options());
		if(Q.function()) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) { }
			System.exit(0);
		}
	}
	
	public void render(Graphics2D g) {
		g.drawImage(background, 0, 0, null);
		for(int i = 0; i < bubbles.size(); i++) {
			bubbles.get(i).render(g);
		}
		P.render(g);
		C.render(g);
		Q.render(g);
		O.render(g);
		Rectangle recLogo = logo_res.getBounds();
		g.drawImage(Logo, recLogo.x, recLogo.y, null);
		Font f = FontG.font(8*Engine.SCALE);
		String value = Engine.VERSION;
		int wF = FontG.getWidth(value, f);
		int hF = FontG.getHeight(value, f);
		int x = Engine.getWidth() - wF - Engine.SCALE;
		int y = Engine.getHeight() - hF/2;
		Rectangle recDesc = new Rectangle(x, y - hF, wF, hF);
		if(Mouse.On_Mouse(recDesc))
			g.setColor(Theme.Secondary);
		else
			g.setColor(Theme.Primary);
		g.setFont(f);
		g.drawString(value, x, y);
		if(Mouse.clickOn(Mouse_Button.LEFT, recDesc)) {
			String url = "https://www.instagram.com/lucaszonzini_/";

	        try {
	            if (Desktop.isDesktopSupported()) {
	                Desktop desktop = Desktop.getDesktop();
	                if (desktop.isSupported(Desktop.Action.BROWSE)) {
	                    desktop.browse(new URI(url));
	                } else {
	                    System.err.println("Ação de browse não suportada.");
	                }
	            } else {
	                System.err.println("Desktop não suportado.");
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		}
		if(Mouse.clickOn(Mouse_Button.LEFT, recLogo)) {
			String url = "https://github.com/luzonni/Code-Breakers-Escape";
	        try {
	            if (Desktop.isDesktopSupported()) {
	                Desktop desktop = Desktop.getDesktop();
	                if (desktop.isSupported(Desktop.Action.BROWSE)) {
	                    desktop.browse(new URI(url));
	                } else {
	                    System.err.println("Ação de browse não suportada.");
	                }
	            } else {
	                System.err.println("Desktop não suportado.");
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
		}
	}

	@Override
	public String giveCommand(String[] values) {
		String message = "";
		String text = "";
		for(String v : values)
			text += v+" ";
		if(values[0] != "" && values[0].equalsIgnoreCase("exit")) {
			System.exit(1);
		}
		text = text.trim();
		message += response(text);
		return message.trim();
		
	}

	private volatile int index_how_to_play = 0;
	private String response(String message) {
		if(message.equalsIgnoreCase("hello"))
			return "Hello! How are you?";
		if(message.equalsIgnoreCase("im good"))
			return "That's good, soon it won't be anymore!";
		if(message.equalsIgnoreCase("How do i play this")) {
			String[] steps = {
					"At each voice level you will have to use strategies",
					"Along with commands that the level made available",
					"You can use WASD keys to walk",
					"Or the arrows"
			};
			index_how_to_play = 0;
			new Thread(() -> {
				while(index_how_to_play < steps.length) {
					if(!Engine.UI.getConsole().isWriting()) {
						Engine.UI.getConsole().print(steps[index_how_to_play], true);
						index_how_to_play++;
					}
				}
			}).start();
			return "";
		}
		if(message.equalsIgnoreCase("how do i play the level i created")) {
			String[] steps = {
					"After saving the level",
					"It will be saved in the same folder where the game was executedes",
					"Press \"Play\" and type the command:",
					"load (level name)",
					"Remembering that the level needs to be in the same directory as me"
			};
			index_how_to_play = 0;
			new Thread(() -> {
				while(index_how_to_play < steps.length) {
					if(!Engine.UI.getConsole().isWriting()) {
						Engine.UI.getConsole().print(steps[index_how_to_play], true);
						index_how_to_play++;
					}
				}
			}).start();
			return "";
		}
		if(message.equalsIgnoreCase("Oi"))
			return "Oi oque? vai jogar logo oxe";
		else
			return "I didn't understand what you meant";
	}

	public void dispose() {
		bubbles.clear();
		//TODO stop menu music!
		Sound.stop(Musics.Music1);
	}

	private static class Bubble {

		private final BufferedImage sprite;
		private double x, y;
		private final int size;
		private double direction;
		private int speed;
		private final int default_speed;
		private int timer;

		public Bubble(double x, double y) {
			this.sprite = getSprite();
			this.x = x;
			this.y = y;
			this.size = 16 * Engine.SCALE;
			this.direction = Engine.RAND.nextDouble() * (Math.PI*2);
			this.speed = Engine.RAND.nextInt(2) + 1;
			this.default_speed = speed;
		}

		private BufferedImage getSprite() {
			SpriteSheet sheet = new SpriteSheet(Engine.ResPath + "/ui/bubble.png", Engine.SCALE);
			sheet.replaceColor(Theme.SECONDARY, Theme.Secondary.getRGB());
			return sheet.getImage();
		}

		public void tick() {
			if(this.x > Engine.getWidth() - Tile.getSize() || this.x < 0) {
				double defx = Math.cos(direction);
				double defy = Math.sin(direction);
				defx *= -1;
				this.direction = Math.atan2(defy, defx);
			}
			if(this.y > Engine.getHeight() - Tile.getSize() || this.y < 0) {
				double defx = Math.cos(direction);
				double defy = Math.sin(direction);
				defy *= -1;
				this.direction = Math.atan2(defy, defx);
			}
			this.x += Math.cos(direction) * speed;
			this.y += Math.sin(direction) * speed;
			this.timer++;
			if(timer > 5 && this.speed > this.default_speed) {
				this.timer = 0;
				this.speed -= 0.15d;
			}
		}

		public Point middle() {
			return new Point((int)x + size/2, (int)y + size/2);
		}

		public void render(Graphics2D g) {
			g.drawImage(sprite, (int)x, (int)y, size, size, null);
		}

	}

	
}
