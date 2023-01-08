package agh.ics.oop;
import javafx.scene.layout.HBox;

public interface ISimulationChangeObserver {
    void updateSimulationWindow(HBox hbox, SimulationEngine engine);
}
