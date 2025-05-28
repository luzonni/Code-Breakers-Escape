package studio.retrozoni.engine.graphics.sprites;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SheetHolder {

    private final Map<String, Sprites> SHEETS;
    private final JSONObject handler;

    public SheetHolder(JSONObject handler) {
        this.handler = handler;
        SHEETS = new HashMap<>();
        loadSheets();
    }

    public Sprites getSheet(String module,String name) {
        return SHEETS.get(module + "." + name);
    }

    public void loadSheets() {
        String mainPath = handler.get("mainPath").toString().replace(".", "/");
        JSONArray modules = (JSONArray) handler.get("modules");
        JSONObject specificsSizes = (JSONObject) handler.get("specificsSizes");
        for(int i = 0; i < modules.size(); i++) {
            JSONObject module = (JSONObject) modules.get(i);
            String moduleName = module.get("module").toString();
            JSONArray sprites = (JSONArray) module.get("sprites");
            JSONObject defaultSize = (JSONObject)module.get("defaultSize");
            int width = ((Number)defaultSize.get("width")).intValue();
            int height = ((Number)defaultSize.get("height")).intValue();
            String path = "/" + mainPath + "/" + moduleName + "/";
            for(int j = 0; j < sprites.size(); j++) {
                String spriteName = sprites.get(j).toString();
                SpriteSheet sheet = new SpriteSheet(path + spriteName + ".png");
                if(specificsSizes.containsKey(spriteName)) {
                    JSONObject size = (JSONObject) specificsSizes.get(spriteName);
                    width = ((Number)size.get("width")).intValue();
                    height = ((Number)size.get("height")).intValue();
                }
                this.SHEETS.put(moduleName+"."+spriteName, new Sprites(sheet, width, height));
            }
        }
    }

    public void restartSheets() {
        dispose();
        loadSheets();
    }

    public void dispose() {
        SHEETS.clear();
    }

}
