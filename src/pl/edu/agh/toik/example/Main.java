package pl.edu.agh.toik.example;

public class Main {
	
	public static void main(String[] args) {
		while(true) {
			Car car = new Car();
			car.fuelUp();
			car.setDriver("SomeDriver");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
