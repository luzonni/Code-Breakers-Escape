package studio.retrozoni.engine;

import studio.retrozoni.engine.tools.ActionBack;

import java.awt.*;
import java.awt.image.BufferedImage;

class Transition implements Runnable {

    public volatile boolean isRunning;
	
	private final ActionBack action;
	private double percent;
	
	public Transition(ActionBack action) {
		this.action = action;
        Thread thread = new Thread(this, "Thread - Transition");
		isRunning = true;
		thread.start();
	}
	
	public static void start(ActionBack action) {
		new Transition(action);
	}
	
	private void stop() {
		isRunning = false;
	}
	
	private void tick() {
		int W = Engine.getWidth()/Engine.SCALE;
		int H = Engine.getHeight()/Engine.SCALE;
		BufferedImage image = new BufferedImage(W, H, BufferedImage.TYPE_INT_ARGB);
		int[] rgb = new int[W*H];
		for(int y = 0; y < H; y++)
			for(int x = 0; x < W; x++) {
				if(Engine.RAND.nextBoolean())
					rgb[x+y*W] = Theme.Tertiary.getRGB();
				else if(Engine.RAND.nextBoolean())
					rgb[x+y*W] = Theme.Primary.getRGB();
				else {
					rgb[x+y*W] = new Color(Theme.Primary.getRed(), Theme.Primary.getGreen(), Theme.Primary.getBlue(), Engine.RAND.nextInt(255)).getRGB();
				}
			}
		image.setRGB(0, 0, W, H, rgb, 0, W);
		Graphics g = image.getGraphics();
		g.setColor(new Color(Theme.Secondary.getRed(), Theme.Secondary.getGreen(), Theme.Secondary.getBlue(), 180));
		g.fillRect(0, Engine.RAND.nextInt(Engine.getHeight()), W, H/6);
		
		g.dispose();
		
		g = Engine.Buffer.getDrawGraphics();
		g.drawImage(image, 0, 0, Engine.getWidth(), Engine.getHeight(), null);
		
		
		Engine.Buffer.show();
		
		percent+=0.015;
		if(percent >= 0.35) {
			action.function();
			stop();
		}
	}
	
	@Override
	public void run() {
		//System values
		long lastTimeHZ = System.nanoTime();
		double amountOfHz = Engine.HZ;
		double ns_HZ = Engine.T / amountOfHz;
		double delta_HZ = 0;
		while(isRunning) {
			try {
				long nowHZ = System.nanoTime();
				delta_HZ += (nowHZ - lastTimeHZ) / ns_HZ;
				lastTimeHZ = nowHZ;
				if(delta_HZ >= 1) {
					tick();
					delta_HZ--;
				}
				Thread.sleep(1);
			}catch(Exception e) {
				System.out.println("ERROR!");
				e.printStackTrace();
				System.exit(1);
			}
		}
	}
	
	

}
