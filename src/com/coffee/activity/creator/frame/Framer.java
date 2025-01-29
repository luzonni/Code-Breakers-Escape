package com.coffee.activity.creator.frame;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

import com.coffee.inputs.Mouse;
import com.coffee.inputs.Mouse_Button;
import com.coffee.main.Engine;
import com.coffee.main.Theme;
import com.coffee.activity.creator.Creator;
import com.coffee.main.tools.Responsive;
import com.coffee.objects.tiles.Tile;

public class Framer implements Runnable {

    private volatile boolean running;
	
	private final Responsive center;
	private final int Width;
    private final int Height;
	private final Rectangle grid;
	public BufferedImage picture;
	private final Graphics2D drawG;

	public volatile BufferedImage viewer;
	private Graphics2D viewG;

	private final Dock dock;
	
	public Framer(Responsive center, Rectangle grid) {
		this.center = center;
		this.grid = grid;
		this.Width = 256*4;
		this.Height = 256*4;
		this.picture = new BufferedImage(this.Width, this.Height, BufferedImage.TYPE_INT_RGB);
		this.viewer = new BufferedImage(this.Width, this.Height, BufferedImage.TYPE_INT_ARGB);
		this.viewG = (Graphics2D) this.viewer.getGraphics();
		this.drawG = (Graphics2D) this.picture.getGraphics();
		this.drawG.setColor(Theme.Tertiary);
		this.drawG.fillRect(0, 0, Width, Height);
		this.drawG.setColor(Theme.Primary);
		this.drawG.drawRect(0, 0, Width-1, Height-1);
		this.dock = new Dock();
	}

	public boolean isRunning() {
		return this.running;
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
        new Thread(this).start();
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

	private synchronized void draw() {
		tick();
		if(Engine.UI.overButtons() || dock.over())
			return;
		int x_d = (Mouse.getX() - getBounds().x + Creator.getCam().getX()) / Engine.SCALE;
		int y_d = (Mouse.getY() - getBounds().y + Creator.getCam().getY()) / Engine.SCALE;
		viewG.drawImage(picture, 0, 0, null);
		dock.getPainter().paint(x_d, y_d, viewG);
		if(Mouse.pressingOn(Mouse_Button.LEFT, getBounds())) {
			dock.getPainter().paint(x_d, y_d, drawG);
		}
		drawG.setColor(Theme.Tertiary);
		drawG.fillRect(this.Width/2 - (this.grid.width*16)/2, this.Height/2 - (this.grid.height*16)/2, GridBounds().width/Engine.SCALE, GridBounds().height/Engine.SCALE);
		drawG.setColor(Theme.Primary);
		drawG.drawRect(0, 0, Width-1, Height-1);
	}

	public void tick() {
		dock.tick();
	}
	
	public void render(Graphics2D g) {
		int x = this.center.getBounds().x - (this.Width*Engine.SCALE)/2;
		int y = this.center.getBounds().y - (this.Height*Engine.SCALE)/2;
		g.drawImage(picture, x - Creator.getCam().getX(), y - Creator.getCam().getY(), this.Width*Engine.SCALE, this.Height*Engine.SCALE, null);
		if(isRunning()) {
			synchronized (this) {
				g.drawImage(viewer, x - Creator.getCam().getX(), y - Creator.getCam().getY(), this.Width * Engine.SCALE, this.Height * Engine.SCALE, null);
				dock.render(g);
			}
		}
	}

	@Override
	public void run() {
		while(running) {
			try {
				draw();
				Thread.sleep(1);
			} catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
	}
	
}
