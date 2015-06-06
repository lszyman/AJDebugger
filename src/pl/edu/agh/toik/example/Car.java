package pl.edu.agh.toik.example;

import pl.edu.agh.toik.ajd.annotation.AJDebugWithout;

@AJDebugWithout
public class Car implements Vehicle {

	String driver;
	
	public void fuelUp() {
		System.out.println("Car fueled up!");
	}

	public void setDriver(String driver) {
		this.driver = driver;
		System.out.println("Driver has changed");
		System.out.println("Driver is now "+driver);
	}
	
	public void blank() {
		
	}
}
