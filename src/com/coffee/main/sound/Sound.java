package com.coffee.main.sound;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.coffee.main.Engine;
import com.coffee.main.tools.Clips;

public class Sound {
	
	//Sound
	private static final int AMOUNT = 8;
	private static Map<String, Clips> sounds;
	private static final String[] names = {"poft", "click", "place", "clear", "kabum", "sss", "die"};
	
	public static void load() {
		sounds = new HashMap<String, Clips>();
		for(String name : names) {
			Clips clips = load(name.toLowerCase());
			sounds.put(name.toLowerCase(), clips);
		}	
	}
	
	public static void play(Sounds sound) {
		if(!sounds.containsKey(sound.resource()))
			throw new RuntimeException("sound not exists");
		sounds.get(sound.resource()).play();
	}
	
	public static void loop(Sounds sound) {
		if(!sounds.containsKey(sound.resource()))
			throw new RuntimeException("sound not exists");
		sounds.get(sound.resource()).loop();
	}
	
	public static void stop(Sounds sound) {
		if(!sounds.containsKey(sound.resource()))
			throw new RuntimeException("sound not exists");
		sounds.get(sound.resource()).stop();
	}
	
	private static Clips load(String name) {
		try {
			String path = Engine.ResPath+"/audio/" + name + ".wav";
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			DataInputStream dis = new DataInputStream(Objects.requireNonNull(Sound.class.getResourceAsStream(path)));
			byte[] buffer = new byte[4096];
			int read = 0;
			while((read = dis.read(buffer)) >= 0) {
				baos.write(buffer, 0, read);
			}
			baos.close();
			dis.close();
			byte[] data = baos.toByteArray();
			return new Clips(name, data, AMOUNT);
		}catch(Exception e) {
			System.out.println("Erro na criação do audio: " + name);
			e.printStackTrace();
			throw new RuntimeException("Erro na criação do audio: " + name + " Com quantidade: " + AMOUNT);
		}
	}
	
}
