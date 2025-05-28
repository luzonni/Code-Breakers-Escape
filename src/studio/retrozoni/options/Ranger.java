package studio.retrozoni.options;

import studio.retrozoni.engine.graphics.FontG;
import studio.retrozoni.engine.inputs.buttons.Button;
import studio.retrozoni.engine.Engine;
import studio.retrozoni.engine.tools.ActionBack;
import studio.retrozoni.engine.tools.Responsive;

import java.awt.*;

class Ranger extends Config {

    protected String text;
    protected final Button minus, plus;

    public Ranger(Responsive responsive, String name, String text) {
        super(responsive, name);
        this.text = text;
        int size = 3 * Engine.SCALE;
        Responsive minus_res = Responsive.createPoint(center, -size, 0);
        minus = new Button("-",-1,0, minus_res, size);
        Responsive plus_res = Responsive.createPoint(center, size, 0);
        plus = new Button("+",+1,0, plus_res, size);
    }


    public void tick() {
        center.setSize(getBoundsText().width, getBoundsText().height);
        this.box.setSize(this.getBoundsText().width + plus.getBounds().width + Engine.SCALE * 35, this.getBounds().height);
    }

    public void setText(String newText) {
        this.text = newText;
    }

    public Ranger plus(ActionBack fun) {
        if(this.plus.function()) {
            fun.function();
        }
        return this;
    }

    public Ranger minus(ActionBack fun) {
        if(this.minus.function()) {
            fun.function();
        }
        return this;
    }

    protected Rectangle getBoundsText() {
        int width = FontG.getWidth(text, font) + Engine.SCALE;
        int height =  FontG.getHeight(text, font);
        return new Rectangle(width, height);
    }

    public void render(Graphics2D g) {
        super.render(g);
        minus.render(g);
        plus.render(g);
        drawText(g);
    }

    private void drawText(Graphics2D g) {
        Rectangle recText = getBoundsText();
        g.setFont(font);
        int x = center.getPosition().x - recText.width/2;
        int y = center.getPosition().y + recText.height/2;
        g.drawString(text, x, y);
    }

}
