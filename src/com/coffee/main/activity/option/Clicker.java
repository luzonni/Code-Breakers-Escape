package com.coffee.main.activity.option;

import com.coffee.graphics.FontG;
import com.coffee.inputs.buttons.Button;
import com.coffee.main.Engine;
import com.coffee.main.tools.ActionBack;
import com.coffee.main.tools.Responsive;

import java.awt.*;

public class Clicker extends Config {

    private final Button button;

    public Clicker(Responsive responsive, String name, String text) {
        super(responsive, name);
        button = new Button(text, 0, 0, Responsive.createPoint(center, 0, Engine.SCALE*3), Engine.SCALE * 3);
    }

    public void click(ActionBack fun) {
        if(button.function()) {
            fun.function();
        }
    }

    public void setText(String newText) {
        button.setName(newText);
    }

    @Override
    public void tick() {
        int nameSize = FontG.getWidth(this.name, this.font);
        int buttonSize = button.getBounds().width;
        int finalSize = Math.max(nameSize, buttonSize);
        this.box.setSize(finalSize + Engine.SCALE * 10, this.box.getBounds().height);
    }

    public void render(Graphics2D g) {
        super.render(g);
        button.render(g);
    }
}
