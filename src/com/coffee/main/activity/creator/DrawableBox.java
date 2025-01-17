package com.coffee.main.activity.creator;

import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.coffee.inputs.Mouse;
import com.coffee.inputs.Mouse_Button;
import com.coffee.main.Engine;
import com.coffee.main.Theme;
import com.coffee.main.tools.Responsive;
import com.coffee.objects.tiles.Tile;

public class DrawableBox implements Runnable {
	
	private Thread thread;
	private volatile boolean running;
	
	private Responsive center;
	private int Width, Height;
	private Rectangle grid;
	public BufferedImage picture;
	private Graphics g;
	private int size_drawn;
	private volatile boolean drawnable;
	
	public DrawableBox(Responsive center, Rectangle grid) {
		this.center = center;
		this.grid = grid;
		this.Width = 256*4;
		this.Height = 256*4;
		this.size_drawn = 4;
		this.picture = new BufferedImage(this.Width, this.Height, BufferedImage.TYPE_INT_RGB);
		this.g = this.picture.getGraphics();
		g.setColor(Theme.Tertiary);
		g.fillRect(0, 0, Width, Height);
		g.setColor(Theme.Primary);
		g.drawRect(0, 0, Width-1, Height-1);
		this.start();
	}
	
	public static int[] convertPixels(int[] picture) {
		int[] pixels = new int[picture.length];
		for(int i = 0; i < pixels.length; i++) {
			if(picture[i] >= 0 && picture[i] <= 2) 
				pixels[i] = Engine.THEME.getPallet()[picture[i]].getRGB();
			else
				for(int ii = 0; ii < Engine.THEME.getPallet().length; ii++)
					if(picture[i] == Engine.THEME.getPallet()[ii].getRGB())
						pixels[i] = ii;
		}
		return pixels;
	}
	
	public synchronized void start() {
		this.running = true;
		this.thread = new Thread(this);
		this.thread.start();
	}
	
	public synchronized void stop() {
		this.running = false;
	}
	
	public void setPixels(int[] pixels) {
		this.picture.setRGB(0, 0, this.Width, this.Height, pixels, 0, this.Width);
	}
	
	public int[] getPixels() {
		return this.picture.getRGB(0, 0, this.Width, this.Height, null, 0, this.Width);
	}

	public boolean isDrawing() {
		return this.drawnable;
	}
	
	public void setDrawnable(boolean drawnable) {
		this.drawnable = drawnable;
	}
	
	private Rectangle getBounds() {
		int x = this.center.getBounds().x - (this.Width*Engine.SCALE)/2;
		int y = this.center.getBounds().y - (this.Height*Engine.SCALE)/2;
		return new Rectangle(x, y, this.Width * Engine.SCALE, this.Height * Engine.SCALE);
	}
	
	private Rectangle GridBounds() {
		int w = grid.width * Tile.getSize();
		int h = grid.height * Tile.getSize();
		return new Rectangle(center.getBounds().x - w/2, center.getBounds().y - h/2, w, h);
	}
	
	private void draw() {
		if(Engine.UI.overButtons()) 
			return;
		int scroll = Mouse.Scrool();
		this.size_drawn -= scroll;
		if(size_drawn < 2)
			this.size_drawn = 2;
		int x_d = (Mouse.getX() - getBounds().x + Creator.getCam().getX())/Engine.SCALE;
		int y_d = (Mouse.getY() - getBounds().y + Creator.getCam().getY())/Engine.SCALE;
		if(Mouse.pressingOn(Mouse_Button.LEFT, getBounds())) {
			g.setColor(Theme.Secondary);
			g.fillOval(x_d - size_drawn/2, y_d - size_drawn/2, size_drawn, size_drawn);
		}
		if(Mouse.pressingOn(Mouse_Button.RIGHT, getBounds())) {
			g.setColor(Theme.Tertiary);
			g.fillOval(x_d - size_drawn/2, y_d - size_drawn/2, size_drawn, size_drawn);
		}
		g.setColor(Theme.Tertiary);
		g.fillRect(this.Width/2 - (this.grid.width*16)/2, this.Height/2 - (this.grid.height*16)/2, GridBounds().width/Engine.SCALE, GridBounds().height/Engine.SCALE);
		g.setColor(Theme.Primary);
		g.drawRect(0, 0, Width-1, Height-1);
	}
	
	public void render(Graphics2D g) {
		int x = this.center.getBounds().x - (this.Width*Engine.SCALE)/2;
		int y = this.center.getBounds().y - (this.Height*Engine.SCALE)/2;
		g.drawImage(picture, x - Creator.getCam().getX(), y - Creator.getCam().getY(), this.Width*Engine.SCALE, this.Height*Engine.SCALE, null);
		if(this.drawnable) {
			int x_mouse = Mouse.getX();
			int y_mouse = Mouse.getY();
			g.setStroke(new BasicStroke(Engine.SCALE));
			g.setColor(Theme.Primary);
			g.drawOval(x_mouse - size_drawn, y_mouse - size_drawn, size_drawn*2, size_drawn*2);
		}
	}

	@Override
	public void run() {
		while(running) {
			try {
				if(this.drawnable)
					draw();
				Thread.sleep(1);
			} catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
	}
	
}
