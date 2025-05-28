package studio.retrozoni.engine.io;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

public class Resources {

    public static JSONObject getJsonFile(String path, String name) throws IOException {
        path += "/" + name + ".json";
        try {
            URL resource = Resources.class.getResource(path);
            if(resource == null)
                throw new IOException("Erro ao ler o arquivo: " + name);
            InputStream stream = resource.openStream();
            Reader isr = new InputStreamReader(stream);
            JSONParser parse = new JSONParser();
            parse.reset();
            return (JSONObject) parse.parse(isr);
        } catch (ParseException e) {
            System.err.println("Erro getJson(): " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
