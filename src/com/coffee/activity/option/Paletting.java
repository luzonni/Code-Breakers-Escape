package com.coffee.activity.option;

import com.coffee.inputs.buttons.Button;
import com.coffee.main.Engine;
import com.coffee.main.Theme;
import com.coffee.main.tools.Responsive;

import java.awt.*;

class Paletting extends Config {

    private final Color[][] pallets;
    private int indexPallet;

    private final int size = 14 * Engine.SCALE;
    private final Responsive pallet_box;
    protected final Button minus, plus;

    public Paletting(Responsive responsive, String name, int indexPallet) {
        super(responsive, name);
        this.indexPallet = indexPallet;
        pallet_box = Responsive.createRectangle(center, new Rectangle(size*3, size), 0, 0);
        minus = new Button("-",-Engine.SCALE*5,0, pallet_box, 3 * Engine.SCALE);
        plus = new Button("+",Engine.SCALE*5,0, pallet_box, 3 * Engine.SCALE);
        this.pallets = Engine.THEME.PALLETS();
    }

    @Override
    public void tick() {
        this.box.setSize(this.pallet_box.getBounds().width + plus.getBounds().width + Engine.SCALE * 35, this.getBounds().height);
        if(plus.function() && indexPallet + 1 < pallets.length) {
            indexPallet++;
            Engine.SETTINGS.change("PALLET_INDEX", indexPallet);
        }
        if(minus.function() && indexPallet - 1 >= 0) {
            indexPallet--;
            Engine.SETTINGS.change("PALLET_INDEX", indexPallet);
        }
    }

    public void render(Graphics2D g) {
        super.render(g);
        plus.render(g);
        minus.render(g);
        g.setColor(Theme.Primary);
        int margin = Engine.SCALE;
        g.fillRect(pallet_box.getBounds().x - margin, pallet_box.getBounds().y - margin, pallet_box.getBounds().width + margin*2, pallet_box.getBounds().height + margin*2);
        int xB = pallet_box.getBounds().x;
        int yB = pallet_box.getBounds().y;
        for(int i = 0; i < pallets[indexPallet].length; i++) {
            g.setColor(pallets[indexPallet][i]);
            g.fillRect(xB + i*size, yB, size, size);
        }
    }

}
