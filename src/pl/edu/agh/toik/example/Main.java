package pl.edu.agh.toik.example;

//import pl.edu.agh.toik.ajd.debugger.Debugger;

public class Main {
	
	public static void main(String[] args) {
//		Debugger.getInstance().setDebugWithAJDebugOnly(true);
		main2();
	}

	public static void main2() {
		main3();
		main4();
		main5();
	}
	
	public static void main3() {
		Train train = new Train();
		train.fuelUp();
	}
	
	public static void main4() {
		while(true) {
			Car car = new Car();
			car.fuelUp();
			car.setDriver("SomeDriver");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	public static void main5() {

	}
}
