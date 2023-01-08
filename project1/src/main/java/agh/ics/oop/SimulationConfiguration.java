package agh.ics.oop;
import java.io.FileNotFoundException;
import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

public class SimulationConfiguration {
    private List<Integer> parameters; // lista parametrów liczbowych symulacji

    public SimulationConfiguration(){
        this.parameters = new ArrayList<>();
    }

    public SimulationConfiguration(List<Integer> parameters){
        this.parameters = parameters;
    }

    public void loadFromFile(String fileName) throws FileNotFoundException{
        File configurationFile = new File("src/main/resources/" + fileName + ".txt");
        Scanner newScanner = new Scanner(configurationFile);

        while(newScanner.hasNext()){
            String line = newScanner.nextLine().replaceAll("[^0-9]", " ");
            line = line.replaceAll(" +", "");
            int parameter = Integer.parseInt(line);
            this.parameters.add(parameter);
        }

        newScanner.close();
    }

    public void validateConfiguration() throws IllegalArgumentException{
        if (this.parameters.get(0) < 10 & this.parameters.get(0) > 50){
            throw new IllegalArgumentException("Map height is out of range");
        }
        if (this.parameters.get(1) < 10 & this.parameters.get(1) > 50){
            throw new IllegalArgumentException("Map width is out of range");
        }
        if (!(this.parameters.get(2) == 0 | this.parameters.get(2) == 1)){
            throw new IllegalArgumentException("Map variant does not exist");
        }
        if (this.parameters.get(3) < 40 | this.parameters.get(3) > 200){
            throw new IllegalArgumentException("Grass spawning at start is out of range");
        }
        if (this.parameters.get(4) < 30 | this.parameters.get(4) > 50){
            throw new IllegalArgumentException("Grass energy profit is out of range");
        }
        if (this.parameters.get(5) < 10 | this.parameters.get(5) > 15){
            throw new IllegalArgumentException("Grass spawned in each day is out of range");
        }
        if (!(this.parameters.get(6) == 0 | this.parameters.get(6) == 1)){
            throw new IllegalArgumentException("Grass growth variant does not exist");
        }
        if (this.parameters.get(7) < 50 | this.parameters.get(7) > 100){
            throw new IllegalArgumentException("Animals spawned at the start is out of range");
        }
        if (this.parameters.get(8) < 60 | this.parameters.get(8) > 100){
            throw new IllegalArgumentException("Animal start energy is out of range");
        }
        if (this.parameters.get(9) < 40 | this.parameters.get(9) > 60){
            throw new IllegalArgumentException("Minimal energy to copulation is out of range");
        }
        if (this.parameters.get(10) < 40 | this.parameters.get(10) > 60){
            throw new IllegalArgumentException("Energy to create offspring is out of range");
        }
        if (this.parameters.get(11) < 0 | this.parameters.get(11) > 4){
            throw new IllegalArgumentException("Minimum offspring mutation is out of range");
        }
        if (this.parameters.get(12) < this.parameters.get(11)){
            throw new IllegalArgumentException("Maximum offspring mutation cannot be lower then minimum");
        }
        if (!(this.parameters.get(13) == 0 | this.parameters.get(13) == 1)){
            throw new IllegalArgumentException("Mutation variant does not exist");
        }
        if (this.parameters.get(14) < 4 | this.parameters.get(14) > 8){
            throw new IllegalArgumentException("Animal genome length is out of range");
        }
        if (!(this.parameters.get(15) == 0 | this.parameters.get(15) == 1)){
            throw new IllegalArgumentException("Animal behavior variant does not exist");
        }
        if (!(this.parameters.get(16) == 0 | this.parameters.get(16) == 1)){
            throw new IllegalArgumentException("Cannot resolve decision about writing CSV file");
        }
    }

    public List<Integer> getParameters() throws IllegalArgumentException{
        validateConfiguration();
        return this.parameters;
    }
}
