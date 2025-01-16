package com.coffee.main;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Setting {

    private final Map<String, Object> settings;

    public Setting() {
        settings = new HashMap<>();
        settings.put("FULLSCREEN", false);
        settings.put("AWAYES_ON_TOP", false);
        settings.put("VOLUME", 60);
        settings.put("MUSIC", 60);
        settings.put("ANTIALIASING", false);
        settings.put("OPEN_GL", false);
        settings.put("RESOLUTION_INDEX", 1);
        settings.put("PALLET_INDEX", 1);
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
            String[] keys = settings.keySet().toArray(new String[0]);
            for(int i = 0; i < keys.length; i++) {
                String key = keys[i];
                if(object != null && object.containsKey(key)) {
                    settings.replace(key, object.get(key));
                }
            }
        } catch (Exception e) {
            file.delete();
            return false;
        }
        return true;
    }

    public String[] keys() {
        return settings.keySet().toArray(new String[0]);
    }

    public int getInt(String key) {
        if(!settings.containsKey(key)) {
            throw new RuntimeException("The key \"" + key + "\" of config not exits!");
        }
        return ((Number) settings.get(key)).intValue();
    }

    public boolean getBool(String key) {
        if(!settings.containsKey(key)) {
            throw new RuntimeException("The key \"" + key + "\" of config not exits!");
        }
        return (Boolean) settings.get(key);
    }

    public void change(String key, Object newValue) {
        if(!settings.containsKey(key)) {
            throw new RuntimeException("The key \"" + key + "\" of config not exits!");
        }
        this.settings.replace(key, newValue);
    }

    public void update() {
        String path = System.getProperty("user.dir") + "/config.json";
        JSONObject object = new JSONObject();
        String[] keys = settings.keySet().toArray(new String[0]);
        for(int i = 0; i < keys.length; i++) {
            String key = keys[i];
            object.put(key, settings.get(key));
        }
        try {
            FileWriter writer = new FileWriter(path);
            writer.write(object.toJSONString());
            writer.close();
        } catch (IOException ignore) { }
    }

}
