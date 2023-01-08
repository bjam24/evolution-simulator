package agh.ics.oop;
import java.util.List;

public class Statistics {
    private int days;
    private int animalsNumber;
    private int grassesNumber;
    private int unoccupiedFieldsNumber;
    private List<Integer> popularGenotype;
    private float averageEnergyOfAliveAnimals;
    private float averageLifeSpanOfDeadAnimals;
    private SimulationMap map;

    public Statistics(SimulationMap map){
        this.days = 0;
        this.animalsNumber = map.getAnimalsNumber();
        this.grassesNumber = map.getGrassesNumber();
        this.unoccupiedFieldsNumber = map.getUnoccupiedFieldsNumber();
        this.popularGenotype = map.getPopularGenome();
        this.averageEnergyOfAliveAnimals = map.getAverageEnergyOfAnimals();
        this.averageLifeSpanOfDeadAnimals = map.getAverageLifespanOFDeadAnimals();
        this.map = map;
    }

    public void updateStatistics(SimulationMap map){
        this.days += 1;
        this.animalsNumber = map.getAnimalsNumber();
        this.grassesNumber = map.getGrassesNumber();
        this.unoccupiedFieldsNumber = map.getUnoccupiedFieldsNumber();
        this.popularGenotype = map.getPopularGenome();
        this.averageEnergyOfAliveAnimals = map.getAverageEnergyOfAnimals();
        this.averageLifeSpanOfDeadAnimals = map.getAverageLifespanOFDeadAnimals(); // do poprawy
    }

    public Integer getDays(){
        return this.days;
    }

    public Integer getAnimalsNumber(){
        if(this.map.getAnimals() != null){
            return this.animalsNumber;
        }
        return 0;
    }

    public Integer getGrassesNumber(){
        return this.grassesNumber;
    }

    public Integer getUnoccupiedFieldsNumber(){
        return this.unoccupiedFieldsNumber;
    }

    public List<Integer> getPopularGenotype(){
        if(this.map.getAnimals() != null){
            return this.popularGenotype;
        }
        return null;
    }

    public Float getAverageEnergyOfAliveAnimals(){
        if(this.map.getAnimals() != null){
            return this.averageEnergyOfAliveAnimals;
        }
        return null;
    }

    public Float getAverageLifeSpanOfDeadAnimals(){
        if(this.map.getDeadAnimals() != null){
            return this.averageLifeSpanOfDeadAnimals;
        }
        return null;
    }
}
