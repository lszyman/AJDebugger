package pl.edu.agh.toik.debugger;

import pl.edu.agh.toik.gui.BreakpointFrame;

public interface DebuggerInterface {

	void takeCommand();
	void setBreakpointFrame(BreakpointFrame bf);
}
