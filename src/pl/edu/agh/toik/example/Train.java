package pl.edu.agh.toik.example;

public class Train implements Vehicle {

	String machinist;
	
	public void fuelUp() {
		System.out.println("Train fueled up!");
	}
	
	public void setMachinist(String machinist) {
		this.machinist = machinist;
		System.out.println("Machinist is now: "+machinist);
	}

}
