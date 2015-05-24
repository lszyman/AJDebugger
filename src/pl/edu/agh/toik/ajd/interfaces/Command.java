package pl.edu.agh.toik.ajd.interfaces;

public interface Command {
	
	void nextBreakpoint();
	void stepInto();
	void stepOver();
	void stepOut();
}
