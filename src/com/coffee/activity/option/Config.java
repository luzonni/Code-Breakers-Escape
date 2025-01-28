package com.coffee.activity.option;

import com.coffee.graphics.FontG;
import com.coffee.inputs.Mouse;
import com.coffee.inputs.Mouse_Button;
import com.coffee.inputs.buttons.Button;
import com.coffee.main.Engine;
import com.coffee.main.Theme;
import com.coffee.main.tools.ActionBack;
import com.coffee.main.tools.Responsive;

import java.awt.*;

abstract class Config {

    protected final String name;

    protected final Responsive box;
    protected final Responsive center;
    protected final Font font;

    public Config(Responsive responsive, String name) {
        this.name = name;
        this.box = responsive;
        this.font = FontG.font(Engine.SCALE * 10);
        this.box.setSize(Engine.SCALE * 90, Engine.SCALE * 40);
        this.center = Responsive.createPoint(Responsive.createPoint(this.box, 0, 0), 0, Engine.SCALE * 3);

    }

    public Responsive getResponsive() {
        return box;
    }

    public abstract void tick();


    public Rectangle getBounds() {
        return this.box.getBounds();
    }

    public void render(Graphics2D g) {
        g.setColor(Theme.Primary);
        float stroke = Engine.SCALE * 2f;
        g.setStroke(new BasicStroke(stroke));
        g.drawRect(getBounds().x, getBounds().y, getBounds().width, getBounds().height);
        drawName(g);
    }

    private void drawName(Graphics2D g) {
        g.setFont(this.font);
        int wF = FontG.getWidth(this.name, this.font);
        int hF = FontG.getHeight(this.name, this.font);
        int x = (int)box.getBounds().getCenterX() - wF/2;
        int y = box.getBounds().y + hF + Engine.SCALE * 5;
        g.drawString(name, x, y);
    }

}
