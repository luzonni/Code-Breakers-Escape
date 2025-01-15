package com.coffee.main;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Theme {

    public static final int PRIMARY = 0xffffffff;
    public static final int SECONDARY = 0xffcccccc;
    public static final int TERTIARY = 0xff000000;

    public static Color Tertiary;
    public static Color Secondary;
    public static Color Primary;

    private final List<Color[]> PALLET;

    public Theme() {
        if(Engine.CONFIG == null) {
            throw new RuntimeException("The configs not initialized!");
        }
        PALLET = new ArrayList<>();
        init();
    }

    private void init() {
        String path = Engine.currentPath() + "/config.json";
        File config = new File(path);
        if(config.exists()) {
//            Color[][] pallets = Engine.CONFIG.getPallets();
//            PALLET.addAll(Arrays.asList(pallets));
            loadSource();
        }else {
            loadSource();
        }

    }

    private void loadSource() {
        try {
            String source = Engine.ResPath + "/source/pallets.json";
            InputStream is = Objects.requireNonNull(getClass().getResource(source)).openStream();
            InputStreamReader inputStreamReader = new InputStreamReader(is);
            JSONParser parse = new JSONParser();
            parse.reset();
            JSONObject object = (JSONObject) parse.parse(inputStreamReader);
            JSONArray pallets = (JSONArray) object.get("colors");
            for(int i = 0; i < pallets.size(); i++) {
                JSONArray pallet = (JSONArray) pallets.get(i);
                String hx1 = (String) pallet.get(0);
                String hx2 = (String) pallet.get(1);
                String hx3 = (String) pallet.get(2);
                PALLET.add(new Color[] {
                        new Color(hexToRgb(hx1)[0], hexToRgb(hx1)[1], hexToRgb(hx1)[2]),
                        new Color(hexToRgb(hx2)[0], hexToRgb(hx2)[1], hexToRgb(hx2)[2]),
                        new Color(hexToRgb(hx3)[0], hexToRgb(hx3)[1], hexToRgb(hx3)[2])
                });
            }
        }catch (IOException | ParseException ignored) {}
    }

    public int getIndex() {
        return Engine.CONFIG.getInt("PALLET_INDEX");
    }

    public Color[] getPallet() {
        return PALLET.get(getIndex());
    }

    Color[][] PALLETS() {
        return this.PALLET.toArray(new Color[][] {});
    }

    public int size() {
        return this.PALLET.size();
    }

    public void update() {
        Primary = getPallet()[0];
        Secondary = getPallet()[1];
        Tertiary = getPallet()[2];
    }

    static int[] hexToRgb(String hex) {
        if (!hex.matches("^#([A-Fa-f0-9]{6})$")) {
            throw new IllegalArgumentException("Invalid hex color format. Use #RRGGBB.");
        }
        int red = Integer.parseInt(hex.substring(1, 3), 16);
        int green = Integer.parseInt(hex.substring(3, 5), 16);
        int blue = Integer.parseInt(hex.substring(5, 7), 16);
        return new int[]{red, green, blue};
    }

    static String rgbToHex(int red, int green, int blue) {
        if (red < 0 || red > 255 || green < 0 || green > 255 || blue < 0 || blue > 255) {
            throw new IllegalArgumentException("RGB values must be between 0 and 255.");
        }
        return String.format("#%02X%02X%02X", red, green, blue);
    }

}
