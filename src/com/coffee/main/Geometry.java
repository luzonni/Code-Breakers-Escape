package com.coffee.main;

import java.awt.*;
import java.util.Random;

public class Geometry {

	public static double Theta(double x1, double y1, double x2, double y2) {
		return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
	}
	
	public static double Theta(Point p1, Point p2) {
		return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
	}

	public static boolean isRectangleBetweenPoints(Rectangle rect, Point p1, Point p2) {
		int minX = Math.min(p1.x, p2.x);
		int maxX = Math.max(p1.x, p2.x);
		int minY = Math.min(p1.y, p2.y);
		int maxY = Math.max(p1.y, p2.y);
		Rectangle boundingRect = new Rectangle(minX, minY, maxX - minX, maxY - minY);
		return boundingRect.intersects(rect);
	}

	public static void main(String[] args) {
		//Mega sena da virada
		Random rand = new Random(456456);
		for(int i = 0; i < 5; i++) {
			for(int j = 0; j < 6; j++) {
				System.out.print(rand.nextInt(60) + " ");
			}
			System.out.println();
		}
	}
	
	
}
