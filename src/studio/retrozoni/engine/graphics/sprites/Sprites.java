package studio.retrozoni.engine.graphics.sprites;

import studio.retrozoni.engine.Theme;

import java.awt.image.BufferedImage;

public class Sprites {

    private final BufferedImage[][] sprites;
    private int index;
    private int state;

    public Sprites(SpriteSheet sheet, int width, int height) {
        sheet.replaceColor(Theme.PRIMARY, Theme.Primary.getRGB());
        sheet.replaceColor(Theme.SECONDARY, Theme.Secondary.getRGB());
        sheet.replaceColor(Theme.TERTIARY, Theme.Tertiary.getRGB());
        int length = sheet.getWidth() / width;
        int states = sheet.getHeight() / height;
        sprites = new BufferedImage[states][length];
        for(int y = 0; y < states; y++) {
            for(int x = 0; x < length; x++) {
                sprites[y][x] = sheet.getSprite(x*width, y*height, width, height);
            }
        }
    }

    public void plusIndex() {
        this.index++;
        if(index > sprites[state].length-1) {
            index = 0;
        }
    }

    public void setIndex(int i) {
        this.index = i;
        if(index > sprites[state].length-1) {
            index = 0;
        }
    }

    public void setState(int s) {
        this.state = s;
        if(state > this.sprites.length-1) {
            state = 0;
        }
    }

    public int size() {
        return this.sprites[state].length;
    }

    public BufferedImage getSprite() {
        return sprites[state][index];
    }

    public BufferedImage getSprite(int index) {
        return sprites[state][index];
    }

    public BufferedImage getSprite(int index, int state) {
        return sprites[state][index];
    }

}
