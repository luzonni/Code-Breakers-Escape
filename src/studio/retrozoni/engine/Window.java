package studio.retrozoni.engine;

import studio.retrozoni.engine.graphics.FontG;
import studio.retrozoni.engine.graphics.SpriteSheet;
import studio.retrozoni.engine.inputs.Keyboard;
import studio.retrozoni.engine.inputs.Mouse;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.Serial;

public class Window extends Canvas implements Runnable {
	
	@Serial
	private static final long serialVersionUID = 36752349087L;

	private final String name;
	private Thread thread;
	private JFrame frame;
	private SpriteSheet cursor;
	private final Toolkit toolkit = Toolkit.getDefaultToolkit();
	private int C_W, C_H;
	boolean oglEnabled = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment()
            .getDefaultScreenDevice()
            .getDefaultConfiguration()
            .getImageCapabilities()
            .isAccelerated();

	
	public Window(String name) {
		this.name = name;
		createOpenGl(Engine.SETTINGS.getBool("OPEN_GL"));
		initFrame();
		Mouse m = new Mouse();
		Keyboard k = new Keyboard();
		addMouseListener(m);
		addMouseMotionListener(m);
		addMouseWheelListener(m);
		addKeyListener(k);
	}
	
	private void createOpenGl(boolean bool) {
		if(bool)
			try {
	            System.setProperty("sun.java2d.opengl", "True");
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
				System.out.println("OpenGL Pipeline enabled: " + oglEnabled);
			} catch (Exception ignore) { }
	}
	
	public void initFrame(){
		this.frame = new JFrame(this.name);
		frame.add(this);
		frame.setUndecorated(Engine.SETTINGS.getBool("FULLSCREEN"));
		frame.setResizable(true);
		frame.setAlwaysOnTop(Engine.SETTINGS.getBool("AWAYES_ON_TOP"));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		if(Engine.SETTINGS.getBool("FULLSCREEN")) {
			GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
			gd.setFullScreenWindow(frame);
			if (!gd.isFullScreenSupported()) {
				System.out.println("Fullscreen without support!");
				System.exit(0);
			}
		}else {
			setPreferredSize(new Dimension(Engine.getResolution()[0], Engine.getResolution()[1]));
			frame.setMinimumSize(new Dimension(Engine.getResolution()[0], Engine.getResolution()[1]));
		}
		frame.pack();
		try {
			SpriteSheet icon = new SpriteSheet(Engine.ResPath+"/ui/icon.png", 3);
			icon.replaceColor(Theme.PRIMARY, Theme.Primary.getRGB());
			icon.replaceColor(Theme.SECONDARY, Theme.Secondary.getRGB());
			icon.replaceColor(Theme.TERTIARY, Theme.Tertiary.getRGB());
			cursor = new SpriteSheet(Engine.ResPath+"/ui/cursor.png", 2);
			cursor.replaceColor(Theme.PRIMARY, Theme.Primary.getRGB());
			cursor.replaceColor(Theme.TERTIARY, Theme.Tertiary.getRGB());
			cursor.replaceColor(Theme.SECONDARY, Theme.Secondary.getRGB());
			setCursor(cursor.getImage());
			frame.setIconImage(icon.getImage());
		}catch(Exception ignore) { }
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		createBufferStrategy(3);
		Engine.Buffer = getBufferStrategy();
		start();
	}

	public synchronized void setCursor(BufferedImage cursor) {
		Cursor c = toolkit.createCustomCursor(cursor, new Point(0,0), "cursor");
		frame.setCursor(c);
	}

	public synchronized void setCursor(BufferedImage cursor, Point pointClick) {
		Cursor c = toolkit.createCustomCursor(cursor, pointClick, "cursor");
		frame.setCursor(c);
	}

	public synchronized void resetCursor() {
		Cursor c = toolkit.createCustomCursor(cursor.getImage(), new Point(0,0), "cursor");
		frame.setCursor(c);
	}
	
	private void closeFrame() {
		frame.setVisible(false);
		frame.dispose();
	}
	
	//Getter's and Setter's
	
	public JFrame getFrame() {
		return this.frame;
	}
	
	public void setResolution() {
		closeFrame();
		initFrame();
	}	
	
	public int getWidth() {
		Component c = frame.getComponent(0);
		return c.getWidth();
	}
	
	public int getHeight() {
		Component c = frame.getComponent(0);
		return c.getHeight();
	}
	
	public boolean isResizing() {
		int W = getWidth();
		int H = getHeight();
		if(W != C_W || H != C_H) {
			return true;
		}
		return false;
	}
	
	public Dimension getScreenSize() {
		return toolkit.getScreenSize();
	}
	
	public void tick() {
		BufferStrategy bufferS = getBufferStrategy();
		if (bufferS == null){
			createBufferStrategy(3);
			return;
		}
		Graphics g = bufferS.getDrawGraphics();
		g.setColor(Color.black);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		g.setColor(Color.WHITE);
		int middleX = getWidth()/2;
		int middleY = getHeight()/2;
		Font font = FontG.font(Engine.SCALE *16);
		String name = "Capulus";
		int w = FontG.getWidth(name, font);
		int h = FontG.getHeight(name, font);
		int x = middleX - w/2;
		int y = middleY - h/2;
		g.setFont(font);
		g.drawString(name, x, y);
		g.dispose();
		bufferS.show();
	}
	
	public synchronized void start() {
		thread = new Thread(this, "Thread - Window");
		thread.start();
	}
	
	public synchronized void stop() {
		thread = null;
	}
	
	@Override
	public void run() {
		//System values
		long lastTimeHZ = System.nanoTime();
		double amountOfHz = Engine.HZ;
		double ns_HZ = 1000000000 / amountOfHz;
		double delta_HZ = 0;
		while(!Engine.isRunning) {
			try {
				long nowHZ = System.nanoTime();
				delta_HZ += (nowHZ - lastTimeHZ) / ns_HZ;
				lastTimeHZ = nowHZ;
				if(delta_HZ >= 1) {
					tick();
					delta_HZ--;
				}
				
			}catch(Exception e) {
				System.out.println("ERROR!");
				e.printStackTrace();
				System.exit(1);
			}
		}
	}
	
}
