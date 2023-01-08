package agh.ics.oop;

import java.util.List;

public class AnimalStatistics {
    private Animal chosenAnimal;
    private List<Integer> genome;
    private Integer activatedGeneIndex;
    private Integer energy;
    private Integer eatenGrasses;
    private Integer createdOffspring;
    private Integer daysLifespan;
    private Integer dayOfDeath;
    private SimulationMap map;

    public AnimalStatistics(SimulationMap map){
        this.map = map;
        this.chosenAnimal = null;
    }

    public void updateAnimalStatistics(SimulationMap map){
        if(map.getAnimals().contains(this.chosenAnimal)){
            this.genome = this.chosenAnimal.getGenome().getGenes();
            this.activatedGeneIndex = this.chosenAnimal.getActivatedGeneIndex();
            this.energy = this.chosenAnimal.getEnergy();
            this.eatenGrasses = this.chosenAnimal.getEatenGrasses();
            this.createdOffspring = this.chosenAnimal.getCreatedOffspring();
            this.daysLifespan = this.chosenAnimal.getDaysAlive();
            this.dayOfDeath = null;
        }else if(map.getDeadAnimals().contains(this.chosenAnimal)){
            this.dayOfDeath = this.chosenAnimal.getDayDeath();
        }else{
            this.genome = null;
            this.activatedGeneIndex = null;
            this.energy = null;
            this.eatenGrasses = null;
            this.createdOffspring = null;
            this.daysLifespan = null;
            this.dayOfDeath = null;
        }
    }

    public void setAnimal(Animal animal){
        this.chosenAnimal = animal;
    }

    public List<Integer> getGenome(){
        if(this.chosenAnimal != null){
            return this.genome;
        }
        return null;
    }

    public Integer getActivatedGeneIndex(){
        if(this.chosenAnimal != null){
            return this.activatedGeneIndex;
        }
        return null;
    }

    public Integer getEnergy(){
        if(this.chosenAnimal != null){
            return this.energy;
        }
        return null;
    }

    public Integer getEatenGrasses(){
        if(this.chosenAnimal != null){
            return this.eatenGrasses;
        }
        return null;
    }

    public Integer getCreatedOffspring(){
        if(this.chosenAnimal != null){
            return this.createdOffspring;
        }
        return null;
    }

    public Integer getDaysLifespan(){
        if(this.chosenAnimal != null){
            return this.daysLifespan;
        }
        return null;
    }

    public Integer getDayOfDeath(){
        if(this.chosenAnimal != null){
            return this.dayOfDeath;
        }
        return null;
    }

    public Object getAnimal(){
        return this.chosenAnimal;
    }

}
