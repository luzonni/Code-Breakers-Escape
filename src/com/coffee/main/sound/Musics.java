package com.coffee.main.sound;

public enum Musics {

    Music1("BackgroundMusic");

    private final String ResourceName;

    Musics(String resourceName) {
        this.ResourceName = resourceName;
    }

    public String resource() {
        return this.ResourceName;
    }

}
