package studio.retrozoni.engine.graphics;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public abstract class FontHandler {

	private static final String PATH = "studio/retrozoni/resources/ui/";

	private static Map<String, Font> FONTS;
	
	public static void addFont(String... fonts) {
		FONTS = new HashMap<>();
		try {
			for(int i = 0; i < fonts.length; i++) {
				String fontName = fonts[i];
				InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream(PATH + fontName + ".ttf");
				if(stream != null)
					FONTS.put(fontName, Font.createFont(Font.TRUETYPE_FONT, stream));
			}
		} catch (FontFormatException | IOException e) {
			System.out.println("ERROR!");
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public static Font font(String font, float size) {
		if(!FONTS.containsKey(font))
			throw new RuntimeException("Font not found");
		return FONTS.get(font).deriveFont(size);
	}
	
	public static int getWidth(String text, Font font) {
		FontRenderContext frc = new FontRenderContext(new AffineTransform(), false, false);
		return (int)(font.getStringBounds(text, frc).getWidth());
	}
	
	public static int getHeight(String text, Font font) {
		FontRenderContext frc = new FontRenderContext(new AffineTransform(), false, false);
		return (int)(font.getStringBounds(text, frc).getHeight() - 0.29*font.getSize());
	}
	
	public static int getSize(String font) {
		if(!FONTS.containsKey(font))
			throw new RuntimeException("Font not found");
		return FONTS.get(font).getSize();
	}
	
}
