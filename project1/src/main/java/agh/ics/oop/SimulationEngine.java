package agh.ics.oop;
import javafx.scene.layout.HBox;
import java.util.ArrayList;
import java.util.List;

public class SimulationEngine implements Runnable {
    private List<Integer> parameters;
    public SimulationMap map;
    private List<ISimulationChangeObserver> observers;
    private HBox hboxRenderSimulationDetails;
    private StatisticsCSVFileWriter statisticsFile;
    private Statistics simulationStatistics;
    private AnimalStatistics animalStatistics;
    public int loopCounter = 0;
    private boolean suspended;

    public SimulationEngine(List<Integer> parameters){
        this.parameters = parameters;
        this.map = new SimulationMap(parameters);
        this.observers = new ArrayList<>();
        this.simulationStatistics = new Statistics(this.map);
        this.animalStatistics = new AnimalStatistics(this.map);

        if(this.parameters.get(16) == 1){
            this.statisticsFile = new StatisticsCSVFileWriter(this.simulationStatistics);
        }
    }

    @Override
    public void run(){
        while(loopCounter < 400){
            this.map.removeDeadAnimals(); // usunięcie martwych zwierząt z mapy
            this.map.moveAnimals(); // skręt i przemieszczenie każdego zwierzęcia
            this.map.animalsConsumeGrasses(); // konsumpcja roślin na których pola weszły zwierzęta
            this.map.animalsCopulate(); // rozmnażanie się najedzonych zwierząt znajdujących się na tym samym polu
            this.map.placeGrasses(this.parameters.get(5)); // wzrastanie nowych roślin na wybranych polach mapy
            this.map.reduceAnimalsEnergy();
            this.simulationStatistics.updateStatistics(this.map);
            this.animalStatistics.updateAnimalStatistics(this.map);

            if(this.parameters.get(16) == 1){
                this.statisticsFile.updateFileContent();
            }

            for (ISimulationChangeObserver observer : this.observers) {
                observer.updateSimulationWindow(this.hboxRenderSimulationDetails, this);
            }

            this.map.updateDay();
            this.loopCounter += 1;

            try{
                Thread.sleep(150);
                synchronized (this) {
                    while (suspended){
                        wait();
                    }
                }
                if(this.loopCounter == 400){
                    System.out.println("Simulation reached maximum days number: 500");
                    Thread.currentThread().interrupt();
                }
            }catch(InterruptedException e){
                System.out.println("Simulation has been interrupted");
            }
        }
    }

    public void addObserver(ISimulationChangeObserver observer){
        this.observers.add(observer);
    }

    public void setHBox(HBox hbox){
        this.hboxRenderSimulationDetails = hbox;
    }

    synchronized void resume() {
        suspended = false;
        notify();
    }

    synchronized void suspend() {
        suspended = true;
    }

    public List<Integer> getParameters(){
        return this.parameters;
    }

    public Statistics getStatistics(){
        return this.simulationStatistics;
    }

    public AnimalStatistics getAnimalStatistics(){
        return this.animalStatistics;
    }

}
