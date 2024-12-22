package com.coffee.main.sound;

public enum Sounds {
	
	Click("click"), Poft("poft");
	
	private final String ResourceName;
	
	Sounds(String resourceName) {
		this.ResourceName = resourceName;
	}
	
	public String resource() {
		return this.ResourceName;
	}

}
