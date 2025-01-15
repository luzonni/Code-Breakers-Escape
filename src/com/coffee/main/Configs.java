package com.coffee.main;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.awt.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Configs {

    private final Map<String, Object> configs;

    public Configs() {
        configs = new HashMap<>();
        configs.put("FULLSCREEN", false);
        configs.put("AWAYES_ON_TOP", false);
        configs.put("VOLUME", 60);
        configs.put("MUSIC", 60);
        configs.put("ANTIALIASING", false);
        configs.put("OPEN_GL", false);
        configs.put("RESOLUTION_INDEX", 1);
        configs.put("PALLET_INDEX", 1);
        if(!init()) {
            throw new RuntimeException("Something are wrong on file Config.json, it's removed for create a new file...");
        }
    }

    private boolean init() {
        String path = System.getProperty("user.dir") + "/config.json";
        File file = new File(path);
        if(!file.exists()) {
            update();
            return true;
        }
        JSONObject object = null;
        try {
            InputStream istream = new FileInputStream(file);
            Reader isr = new InputStreamReader(istream);
            JSONParser parse = new JSONParser();
            parse.reset();
            object = (JSONObject) parse.parse(isr);
        }catch (Exception e) {}
        try {
            String[] keys = configs.keySet().toArray(new String[0]);
            for(int i = 0; i < keys.length; i++) {
                String key = keys[i];
                if(object != null && object.containsKey(key)) {
                    configs.replace(key, object.get(key));
                }
            }
        } catch (Exception e) {
            file.delete();
            return false;
        }
        return true;
    }

    public int getInt(String key) {
        if(!configs.containsKey(key)) {
            throw new RuntimeException("The key \"" + key + "\" of config not exits!");
        }
        return ((Number)configs.get(key)).intValue();
    }

    public boolean getBool(String key) {
        if(!configs.containsKey(key)) {
            throw new RuntimeException("The key \"" + key + "\" of config not exits!");
        }
        return (Boolean) configs.get(key);
    }

    public void change(String key, Object newValue) {
        if(!configs.containsKey(key)) {
            throw new RuntimeException("The key \"" + key + "\" of config not exits!");
        }
        this.configs.replace(key, newValue);
    }

    public void update() {
        String path = System.getProperty("user.dir") + "/config.json";
        JSONObject object = new JSONObject();
        String[] keys = configs.keySet().toArray(new String[0]);
        for(int i = 0; i < keys.length; i++) {
            String key = keys[i];
            object.put(key, configs.get(key));
        }
        if(Engine.THEME != null) {
            JSONArray pallets_json = new JSONArray();
            Color[][] PALLETS = Engine.THEME.PALLETS();
            for(int i = 0; i < PALLETS.length; i++) {
                JSONArray pallet = new JSONArray();
                pallet.add(Theme.rgbToHex(PALLETS[i][0].getRed(), PALLETS[i][0].getGreen(), PALLETS[i][0].getBlue()));
                pallet.add(Theme.rgbToHex(PALLETS[i][1].getRed(), PALLETS[i][1].getGreen(), PALLETS[i][1].getBlue()));
                pallet.add(Theme.rgbToHex(PALLETS[i][2].getRed(), PALLETS[i][2].getGreen(), PALLETS[i][2].getBlue()));
                pallets_json.add(pallet);
            }
            object.put("PALLET", getSource());
        }
        try {
            FileWriter writer = new FileWriter(path);
            writer.write(object.toJSONString());
            writer.close();
        } catch (IOException ignore) { }
    }

    private JSONArray getSource() {
        try {
            String source = Engine.ResPath + "/source/pallets.json";
            InputStream is = Objects.requireNonNull(getClass().getResource(source)).openStream();
            InputStreamReader inputStreamReader = new InputStreamReader(is);
            JSONParser parse = new JSONParser();
            parse.reset();
            JSONObject object = (JSONObject) parse.parse(inputStreamReader);
            return (JSONArray) object.get("colors");
//            for(int i = 0; i < pallets.size(); i++) {
//                JSONArray pallet = (JSONArray) pallets.get(i);
//                String hx1 = (String) pallet.get(0);
//                String hx2 = (String) pallet.get(1);
//                String hx3 = (String) pallet.get(2);
//                PALLET.add(new Color[] {
//                        new Color(hexToRgb(hx1)[0], hexToRgb(hx1)[1], hexToRgb(hx1)[2]),
//                        new Color(hexToRgb(hx2)[0], hexToRgb(hx2)[1], hexToRgb(hx2)[2]),
//                        new Color(hexToRgb(hx3)[0], hexToRgb(hx3)[1], hexToRgb(hx3)[2])
//                });
//            }
        }catch (IOException | ParseException ignored) {
            throw new RuntimeException("Erro ao subir as paletas!");
        }
    }

}
