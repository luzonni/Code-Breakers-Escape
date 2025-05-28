package studio.retrozoni.engine;

import org.json.simple.JSONObject;
import studio.retrozoni.activities.menu.Menu;
import studio.retrozoni.activities.creator.Creator;
import studio.retrozoni.activities.game.Level;
import studio.retrozoni.engine.graphics.FontHandler;
import studio.retrozoni.engine.graphics.sprites.SheetHolder;
import studio.retrozoni.engine.io.Resources;
import studio.retrozoni.engine.sound.Sound;
import studio.retrozoni.activities.game.objects.Objects;
import studio.retrozoni.engine.ui.UserInterface;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;

public class Engine implements Runnable {
	
	public static final String VERSION = "Version: Alpha 1.0";
	
	static volatile Thread thread;
	static volatile boolean isRunning;

	public static final double HZ = 60;
	public static final double T = 1_000_000_000.0;
	public static int FRAMES;
	public static int HERTZ;

	public static Setting SETTINGS;
	public static Theme THEME;
	public static final int SCALE = 3;
	public static final int[][] resolutions = {{1280, 720}, {1366, 768}, {1600, 900}, {1920, 1080}, {2560, 1440}, {3840, 2160}};

	public static final String ResPath = "/studio/retrozoni/resources";
	public static final String GameTag = "Code Break's Escape";
	public static double MaxFrames = 60;

	public static SheetHolder sheetHolder;
	public static Window WINDOW;
	public static UserInterface UI;
	private static BufferedImage[] NOISE;
	private static int INDEX_NOISE;
	public static BufferStrategy Buffer;

	public static Activity ACTIVITY;
	public static boolean ACTIVITY_RUNNING;
	
	public static String[] LEVELS = {"move!", "commands", "be_fast", "maze"};
	public static int INDEX_LEVEL = 0;
	
	public static Random RAND;

	public static String currentPath() {
		return System.getProperty("user.dir");
	}

	public Engine(String[] args) {
		FontHandler.loadFont("septem");
		Sound.load();
		Engine.SETTINGS = new Setting();
		Engine.THEME = new Theme();
		THEME.update();
		RAND = new Random();

		try {
			JSONObject handler = Resources.getJsonFile("/studio/retrozoni/resources/sheets", "handler");
			Engine.sheetHolder = new SheetHolder(handler);
		}catch (Exception e) {
			System.err.println("Erro ao carregar o handler do sheetHandler");
			e.printStackTrace();
		}
		start(args);
    }

	public synchronized void start(String[] levelName) {
		WINDOW = new Window(GameTag + " / The Universe");
		UI = new UserInterface();
		if(levelName != null && levelName.length > 0 && !levelName[0].isBlank()) {
			ACTIVITY = new Creator(Level.getLevel(new File(currentPath() + "/created/" + levelName[0] + ".json")));
		}else
			ACTIVITY = new Menu();
		ACTIVITY.enter();
		ACTIVITY_RUNNING = true;
		UI.setReceiver(ACTIVITY);
		
		thread = new Thread(this, "Thread - Game");
		Engine.isRunning = true;
		thread.start();
	}
	
	public synchronized void stop() {
		thread.interrupt();
		new Thread(() -> {
            WINDOW.getFrame().setVisible(false);
            WINDOW.getFrame().dispose();
            Objects.disposeAll();
            start(null);
        }).start();
		if(!WINDOW.isEnabled()) {
			WINDOW = null;
			UI = null;
		}
	}
	
	public synchronized static void restart() {
		isRunning = false;
	}

	public static int[] getResolution() {
		return Engine.resolutions[Engine.SETTINGS.getInt("RESOLUTION_INDEX")];
	}
	
	public synchronized static void setActivity(Activity activity) {
		ACTIVITY_RUNNING = false;
		Transition.start(() -> {
			ACTIVITY.dispose();
			ACTIVITY = activity;
			ACTIVITY.enter();
			ACTIVITY_RUNNING = true;
			UI.setReceiver(Engine.ACTIVITY);
		});
	}

	public static int getWidth() {
		return WINDOW.getWidth();
	}
	
	public static int getHeight() {
		return WINDOW.getHeight();
	}
	
	private static BufferedImage getNoise() {
		if(NOISE == null)
			NOISE = new BufferedImage[16];
		INDEX_NOISE++;
		if(INDEX_NOISE > NOISE.length-1)
			INDEX_NOISE = 0;
		if(NOISE[INDEX_NOISE] == null || NOISE[INDEX_NOISE].getWidth() != WINDOW.getWidth()/ SCALE)
			NOISE[INDEX_NOISE] = buildNoise();
		return NOISE[INDEX_NOISE];
	}
	
	private static BufferedImage buildNoise() {
		int width = WINDOW.getWidth() / SCALE;
		int height = WINDOW.getHeight() / SCALE;
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		int[] rgb = new int[width*height];
		for(int y = 0; y < height; y++)
			for(int x = 0; x < width; x++) {
				rgb[x+y*width] = new Color(Theme.Primary.getRed(), Theme.Primary.getGreen(), Theme.Primary.getBlue(), RAND.nextInt(35)).getRGB();
			}
		image.setRGB(0, 0, width, height, rgb, 0, width);	
		return image;
	}
	
	private Graphics2D getGraphics() {
		Graphics2D graphics = (Graphics2D) Buffer.getDrawGraphics();
		if(SETTINGS.getBool("ANTIALIASING"))
			graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		graphics.setColor(Theme.Tertiary);
		graphics.fillRect(0, 0, WINDOW.getWidth(), WINDOW.getHeight());
		graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
		return graphics;
	}
	
	private void render(Graphics2D graphics) {
		graphics.drawImage(Engine.getNoise(), 0, 0, Engine.WINDOW.getWidth(), Engine.WINDOW.getHeight(), null);
		graphics.dispose();
		Buffer.show();
	}
	
	
	@Override
	public void run() {
		//System values
		long lastTimeHZ = System.nanoTime();
		double amountOfHz = Engine.HZ;
		double ns_HZ = Engine.T / amountOfHz;
		double delta_HZ = 0;
		
		long lastTimeFPS = System.nanoTime();
		double amountOfFPS = Engine.MaxFrames;
		double ns_FPS = Engine.T / amountOfFPS;
		double delta_FPS = 0;
		int Hz = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		WINDOW.requestFocus();
		while(isRunning) {
			try {
				long nowHZ = System.nanoTime();
				delta_HZ += (nowHZ - lastTimeHZ) / ns_HZ;
				lastTimeHZ = nowHZ;
				if(delta_HZ >= 1) {
					if(ACTIVITY_RUNNING && ACTIVITY != null) {
						ACTIVITY.tick();
						UI.tick();
					}
					Hz++;
					delta_HZ--;
				}
				
				long nowFPS = System.nanoTime();
				delta_FPS += (nowFPS - lastTimeFPS) / ns_FPS;
				lastTimeFPS = nowFPS;
				if(delta_FPS >= 1) {
					Graphics2D g = getGraphics();
					if(ACTIVITY_RUNNING && ACTIVITY != null) {
						ACTIVITY.render(g);
						UI.render(g);
						render(g);
					}
					frames++;
					delta_FPS--;
				}
				
				//Show fps
				if(System.currentTimeMillis() - timer >= 1000){
					Engine.WINDOW.getFrame().setTitle(Engine.GameTag+" - Hz: " + Hz + " / Frames: " + frames);
					Engine.FRAMES = frames;
					frames = 0;
					Engine.HERTZ = Hz;
					Hz = 0;
					timer += 1000;
				}
				Thread.sleep(1);
			}catch(Exception e) {
				System.out.println("ERROR!");
				e.printStackTrace();
				System.exit(1);
			}
		}
		stop();
	}

}

/*

Calcular tempo de execuï¿½ï¿½o {
	long startTime = System.nanoTime();
	
	...
	
	long estimatedTime = System.nanoTime() - startTime;
	System.out.printf("Draw runtime: %.4f ms%n", estimatedTime / 1000000d);
}


*/
