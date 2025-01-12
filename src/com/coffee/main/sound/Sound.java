package com.coffee.main.sound;

import java.util.HashMap;
import java.util.Map;

import com.coffee.main.Engine;
import kuusisto.tinysound.*;

public class Sound {
	
	private static Map<String, kuusisto.tinysound.Sound> sounds;
	private static Map<String, kuusisto.tinysound.Music> musics;

	public static synchronized void load() {
		TinySound.init();
        if(!TinySound.isInitialized())
			throw new RuntimeException("Error on load sounds!");
		TinySound.setGlobalVolume(1f);
		if(sounds == null)
			loadSounds();
		if(musics == null)
			loadMusics();
	}

	private static synchronized void loadSounds() {
		sounds = new HashMap<>();
		Sounds[] names = Sounds.values();
		for(Sounds name : names) {
			kuusisto.tinysound.Sound sound = TinySound.loadSound(Engine.ResPath + "/audio/" + name.resource() + ".wav");
			sounds.put(name.resource(), sound);
		}
	}

	private static synchronized void loadMusics() {
		musics = new HashMap<>();
		Musics[] names = Musics.values();
		for(Musics name : names) {
			kuusisto.tinysound.Music music = TinySound.loadMusic(Engine.ResPath + "/audio/" + name.resource() + ".wav");
			musics.put(name.resource(), music);
		}
	}
	
	public static void play(Sounds sound) {
		if(!sounds.containsKey(sound.resource()))
			throw new RuntimeException("sound not exists");
		sounds.get(sound.resource()).play((double) Engine.Volume / 100d);
	}

	public static void play(Sounds sound, double pan) {
		if(!sounds.containsKey(sound.resource()))
			throw new RuntimeException("sound not exists");
		sounds.get(sound.resource()).play((double) Engine.Volume / 100d, pan);
	}
	
	public static void play(Musics music, boolean loop) {
		if(!musics.containsKey(music.resource()))
			throw new RuntimeException("sound not exists");
		musics.get(music.resource()).play(loop, (double) Engine.Music / 100d);
	}

	public static void play(Musics music, boolean loop, double pan) {
		if(!musics.containsKey(music.resource()))
			throw new RuntimeException("sound not exists");
		musics.get(music.resource()).play(loop, (double) Engine.Music / 100d, pan);
	}

	public static void stop(Sounds sound) {
		if(!sounds.containsKey(sound.resource()))
			throw new RuntimeException("sound not exists");
		sounds.get(sound.resource()).stop();
	}

	public static void stop(Musics music) {
		if(!musics.containsKey(music.resource()))
			throw new RuntimeException("sound not exists");
		musics.get(music.resource()).stop();
	}

	public static void dispose() {
		TinySound.shutdown();
	}
	
}
