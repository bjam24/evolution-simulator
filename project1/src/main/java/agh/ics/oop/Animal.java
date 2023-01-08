package agh.ics.oop;
import javafx.scene.paint.Color;

import java.awt.*;
import java.util.List;
import java.util.Random;

public class Animal{
    private Vector2d position;
    private  MapDirection mapDirection;
    private Genome genome;
    private int activatedGeneIndex;
    private int energy;
    private int eatenGrasses;
    private int createdOffspring;
    private int daysAlive;
    private int dayDeath;
    private boolean isChosen;
    private boolean hasDominantGenome;
    private List<Integer> parameters;

    public Animal(List<Integer> parameters, Vector2d position, Genome strongerParentGenome, Genome weakerParentGenome,
                  float energyProportion){
        this.position = position;
        this.parameters = parameters;
        this.mapDirection = MapDirection.randomMapDirection(); // zwierzak jest ustawiony w losowym kierunku
        this.genome = new Genome(strongerParentGenome.getGenes(), weakerParentGenome.getGenes(), energyProportion, this.parameters);
        this.activatedGeneIndex = this.genome.randomActivatedGeneIndex(); // ma aktywowany losowy gen
        this.energy = this.parameters.get(10);
        this.eatenGrasses = 0;
        this.createdOffspring = 0;
        this.daysAlive = 0;
        this.dayDeath = -1;
        this.isChosen = false;
        this.hasDominantGenome = false;
    }

    public Animal(List<Integer> parameters, Vector2d position){
        this.position = position;
        this.parameters = parameters;
        this.mapDirection = MapDirection.randomMapDirection(); // zwierzak jest ustawiony w losowym kierunku
        this.genome = new Genome(this.parameters);
        this.activatedGeneIndex = this.genome.randomActivatedGeneIndex(); // ma aktywowany losowy gen
        this.energy = this.parameters.get(7);
        this.eatenGrasses = 0;
        this.createdOffspring = 0;
        this.daysAlive = 0;
        this.dayDeath = -1;
        this.isChosen = false;
        this.hasDominantGenome = false;
    }

    // zmiana pozycji i zwrotu zwierzaka
    public void changeMapDirection(int n){ // zmiana ustawienia zwierzęcia
        for(int i = 0; i < n; i++){
            this.mapDirection = this.mapDirection.next();
        }
    }

    public void move(){ // ruch zwierzęcia
        int activatedGene = this.genome.getGene(this.activatedGeneIndex); // wydobycie aktywnej części genu
        changeMapDirection(activatedGene); // zwierzak zmienia najpierw swoje ustawienie
        Vector2d vector = this.mapDirection.toUnitVector(); // przekształcenie kierunku na wektor
        Vector2d newPosition = this.position.add(vector);

        if(newPosition.follows(new Vector2d(0,0)) & newPosition.precedes(new Vector2d(this.parameters.get(1) - 1,
                this.parameters.get(0) - 1))){
            this.position = newPosition; // nowa pozycja zwierzęcia
        }else{
            if(this.parameters.get(2) == 0){ // wariant mapy kula ziemska
                if(newPosition.y > this.parameters.get(0) - 1 | newPosition.y < 0){
                    changeMapDirection(4);
                }

                if(newPosition.x > this.parameters.get(1) - 1){
                    this.position = new Vector2d(0, newPosition.y);
                }
                else if(newPosition.x < 0){
                    this.position = new Vector2d(this.parameters.get(1) - 1, newPosition.y);
                }

            }else{ // wariant mapy piekielny portal
                Random rand = new Random();
                int x = rand.nextInt(this.parameters.get(1));
                int y = rand.nextInt(this.parameters.get(0));
                this.position = new Vector2d(x, y);
                copulationEnergyReduction((int)this.parameters.get(10) / 2);
            }
        }

        this.activatedGeneIndex = this.genome.nextActivatedGeneIndex(this.activatedGeneIndex, this.parameters.get(15)); // ustawienie aktywnej części genu
    }

    // zmiany energii zwierzaka
    public void eatGrass(){
        this.energy += this.parameters.get(4);
        this.eatenGrasses += 1;
    }

    public void dailyEnergyReduction(){
        this.energy -= 1;
    }

    public void copulationEnergyReduction(int usedEnergy){
        this.energy -= usedEnergy;
    }

    public Animal createOffspring(Animal weakerPartner){
        float energyProportion = (float)(weakerPartner.getEnergy() / (long)(this.energy + weakerPartner.getEnergy()));
        return new Animal(this.parameters, this.position, this.genome, weakerPartner.getGenome(), energyProportion);
    }

    public void updateOffspringCreated(){
        this.createdOffspring += 1;
    }

    public void setDayDeath(int deathDay){
        this.dayDeath = deathDay;
    }

    public void updateEatenGrasses(){
        this.eatenGrasses += 1;
    }

    public void updateDaysAlive(){
        this.daysAlive += 1;
    }

    public Vector2d getPosition(){
        return this.position;
    }

    public int getEnergy(){
        return this.energy;
    }

    public Genome getGenome(){
        return this.genome;
    }

    public int getDaysAlive(){
        return this.daysAlive;
    }

    public int getActivatedGeneIndex(){
        return this.activatedGeneIndex;
    }

    public int getEatenGrasses(){
        return this.eatenGrasses;
    }

    public int getDayDeath(){
        return this.dayDeath;
    }

    public int getCreatedOffspring(){
        return this.createdOffspring;
    }

    public Color getAnimalColor(){
        if(this.energy == 0){
            return Color.web("#ffffff");
        }else if(this.energy < 0.2 * this.parameters.get(7)){
            return Color.web("#d3b6a0");
        }else if(this.energy < 0.4 * this.parameters.get(7)){
            return Color.web("#dc9456");
        }else if(this.energy < 0.6 * this.parameters.get(7)){
            return Color.web("#c3834d");
        }else if(this.energy < 0.8 * this.parameters.get(7)){
            return Color.web("#ab7343");
        }else if(this.energy == this.parameters.get(7)){
            return Color.web("#92623a");
        }else if(this.energy < 1.2 * this.parameters.get(7)){
            return Color.web("#7a5230");
        }else if(this.energy < 1.4 * this.parameters.get(7)){
            return Color.web("#624226");
        }else if(this.energy < 1.6 * this.parameters.get(7)){
            return Color.web("#49311d");
        }else if(this.energy < 1.8 * this.parameters.get(7)){
            return Color.web("#312113");
        }

        return Color.web("#18100a");
    }
}
