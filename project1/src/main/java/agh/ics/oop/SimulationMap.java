package agh.ics.oop;
import java.util.*;
import java.util.stream.Collectors;

public class SimulationMap {
    private Vector2d lowerLeft;
    private Vector2d upperRight;
    private Vector2d equatorLowerLeft;
    private Vector2d equatorUpperRight;
    private int equatorUnoccupiedFields;
    private int steppeUnoccupiedFields;
    private Map<Vector2d, Integer> positions; // pozycje na mapie z poziomem toksyczności
    private List<Grass> grasses;
    private List<Animal> animals;
    private List<Integer> parameters;
    private List<Animal> deadAnimals;
    private List<Integer> popularGenome;
    private int fieldsUnoccupiedByGrass;
    private int days;

    public SimulationMap(List<Integer> parameters) {
        this.parameters = parameters;

        this.lowerLeft = new Vector2d(0, 0);
        this.upperRight = new Vector2d(this.parameters.get(1) - 1, this.parameters.get(0) - 1);

        this.equatorLowerLeft = new Vector2d(0, this.upperRight.y * 2/5);
        this.equatorUpperRight = new Vector2d(this.upperRight.x, this.upperRight.y * 3/5);

        this.equatorUnoccupiedFields = this.parameters.get(1) * this.parameters.get(0) / 5;
        this.steppeUnoccupiedFields = this.parameters.get(1) * this.parameters.get(0) - this.equatorUnoccupiedFields;

        this.positions = generatePositions();

        this.grasses = new ArrayList<>();
        placeGrasses(this.parameters.get(3));

        this.animals = new ArrayList<>();
        placeAnimals(this.parameters.get(7));

        this.deadAnimals = new ArrayList<>();

        this.popularGenome = new ArrayList<>();

        this.fieldsUnoccupiedByGrass = this.parameters.get(0) * this.parameters.get(1);
        this.days = 0;
    }

    public Map<Vector2d, Integer> generatePositions(){
        Map<Vector2d, Integer> generatedPositions = new HashMap<>();

        for(int i=0; i < this.parameters.get(1); i++){
            for(int j=0; j < this.parameters.get(0); j++){
                Vector2d newVector = new Vector2d(i, j);
                generatedPositions.put(newVector, 0);
            }
        }
        return generatedPositions;
    }

    // tematyka trawy
    public void placeGrasses(int numberOfGrasses) { // umieszczanie trawy na mapie
        if(this.parameters.get(6) == 0){
            placeGrassesEquator(numberOfGrasses);
        }else{
            placeGrassesToxicCorpses(numberOfGrasses);
        }
    }

    public void placeGrassesEquator(int numberOfGrasses){
        Random rand = new Random();
        int counter = 0;
        boolean fullEquator = false, fullSteppe = false;

        while(counter != numberOfGrasses){
            int x = rand.nextInt(this.upperRight.x + 1);
            int y = rand.nextInt(this.upperRight.y + 1);
            Vector2d position = new Vector2d(x, y);

            if(!isOccupiedGrass(position)){
                if(counter % 5 == 0){
                    if(!(position.follows(this.equatorLowerLeft) && position.precedes(this.equatorUpperRight))){
                        this.grasses.add(new Grass(new Vector2d(x, y)));
                        this.steppeUnoccupiedFields -= 1;
                        counter += 1;
                    }else{
                        if(fullSteppe){
                            this.grasses.add(new Grass(new Vector2d(x, y)));
                            this.equatorUnoccupiedFields -= 1;
                            counter += 1;
                        }
                    }
                }else{
                    if(position.follows(this.equatorLowerLeft) && position.precedes(this.equatorUpperRight)){
                        this.grasses.add(new Grass(new Vector2d(x, y)));
                        this.equatorUnoccupiedFields -= 1;
                        counter += 1;
                    }else{
                        if(fullEquator){
                            this.grasses.add(new Grass(new Vector2d(x, y)));
                            this.steppeUnoccupiedFields -= 1;
                            counter += 1;
                        }
                    }
                }
            }

            if(this.equatorUnoccupiedFields > 0){
                fullEquator = false;
            }else{
                fullEquator = true;
            }

            if(this.steppeUnoccupiedFields > 0){
                fullSteppe = false;
            }else{
                fullSteppe = true;
            }

            if(this.fieldsUnoccupiedByGrass == 0){
                break;
            }
        }
    }

    public void placeGrassesToxicCorpses(int numberOfGrasses){
        List<Vector2d> sortedPositions = this.positions.entrySet().stream()
                .sorted(Comparator.comparing(Map.Entry::getValue, Comparator.reverseOrder()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

        List<Vector2d> toxicFields = sortedPositions.subList(0, sortedPositions.size() / 5);
        List<Vector2d> lessToxicFields = sortedPositions.subList(sortedPositions.size() / 5, sortedPositions.size());

        Random rand = new Random();
        int counter = 0;

        while(counter != numberOfGrasses){
            Vector2d vector;

            if(counter % 5 == 0){ // toksyczne
                vector = toxicFields.get(rand.nextInt(toxicFields.size()));
            }else{ // mniej toksyczne
                vector = lessToxicFields.get(rand.nextInt(lessToxicFields.size()));
            }

            if(!isOccupiedGrass(vector)){
                this.grasses.add(new Grass(vector));
                this.fieldsUnoccupiedByGrass -= 1;
                counter += 1;
            }

            if(this.fieldsUnoccupiedByGrass == 0){
                break;
            }
        }
    }

    public boolean isOccupiedGrass(Vector2d position){
        for (int i = 0; i < this.grasses.size(); i++){
            if(this.grasses.get(i).getPosition().equals(position)){
                return true;
            }
        }
        return false;
    }

    public void removeGrass(Vector2d position){
        for (int i = 0; i < this.grasses.size(); i++){
            Vector2d grassPosition = this.grasses.get(i).getPosition();
            if(grassPosition.equals(position)){
                this.grasses.remove(i);

                if(grassPosition.follows(this.equatorLowerLeft) && grassPosition.precedes(this.equatorUpperRight)){
                    this.equatorUnoccupiedFields += 1;
                    this.fieldsUnoccupiedByGrass += 1;
                }

                if(!(grassPosition.follows(this.equatorLowerLeft) && grassPosition.precedes(this.equatorUpperRight))){
                    this.steppeUnoccupiedFields += 1;
                    this.fieldsUnoccupiedByGrass += 1;
                }
            }
        }
    }

    // tematyka zwierząt
    public void placeAnimals(int numberOfAnimals){
        Random rand = new Random();
        int counter = 0;

        while(counter != numberOfAnimals){
            int x = rand.nextInt(this.upperRight.x);
            int y = rand.nextInt(this.upperRight.y);

            if(!isOccupiedAnimals(new Vector2d(x, y))){
                this.animals.add(new Animal(this.parameters, new Vector2d(x, y)));
                counter += 1;
            }
        }
    }

    public boolean isOccupiedAnimals(Vector2d position){
        for (int n = 0; n < this.animals.size(); n++){
            if(this.animals.get(n).getPosition().equals(position)){
                return true;
            }
        }
        return false;
    }

    public List<Animal> getAnimalsAt(Vector2d position){
        List<Animal> animalsAtPosition = new ArrayList<>();
        for (int l = 0; l < this.animals.size(); l++){
            if(this.animals.get(l).getPosition().equals(position)){
                animalsAtPosition.add(this.animals.get(l));
            }
        }
        return animalsAtPosition;
    }

    public void moveAnimals(){
        for(int i = 0; i < this.animals.size(); i++){
            this.animals.get(i).move();
        }
    }

    public void animalsConsumeGrasses(){
        for(int i = 0; i < this.animals.size(); i++){
            Vector2d position = this.animals.get(i).getPosition();

            if(isOccupiedGrass(position)){
                List<Animal> animalsOnGrass = getAnimalsAt(position);

                if(animalsOnGrass.size() == 1){
                    this.animals.get(i).eatGrass();
                }else if(animalsOnGrass.size() > 1){
                    animalsOnGrass = animalsCompetition(animalsOnGrass);
                    Animal winner = animalsOnGrass.get(0);
                    this.animals.get(this.animals.indexOf(winner)).eatGrass();
                    this.animals.get(this.animals.indexOf(winner)).updateEatenGrasses();
                }
                removeGrass(position);
            }
        }
    }

    public void animalsCopulate(){
        for (Vector2d vector : this.positions.keySet()) {
            Vector2d position = vector;
            if(isOccupiedAnimals(position)){
                List<Animal> animalsToCopulation = getAnimalsAt(position);
                if (animalsToCopulation.size() >= 2) {
                    animalsToCopulation = animalsCompetition(animalsToCopulation);
                    Animal strongerPartner = animalsToCopulation.get(0);
                    Animal weakerPartner = animalsToCopulation.get(1);
                    int copulationEnergy = this.parameters.get(9);

                    if (strongerPartner.getEnergy() >= copulationEnergy) {
                        if (weakerPartner.getEnergy() >= copulationEnergy){
                            int strongerPartnerIndex = this.animals.indexOf(strongerPartner);
                            int weakerPartnerIndex = this.animals.indexOf(weakerPartner);

                            this.animals.get(strongerPartnerIndex).copulationEnergyReduction((int)(this.parameters.get(10) / 2));
                            this.animals.get(weakerPartnerIndex).copulationEnergyReduction((int)(this.parameters.get(10) / 2));

                            this.animals.get(strongerPartnerIndex).updateOffspringCreated();
                            this.animals.get(weakerPartnerIndex).updateOffspringCreated();
                            Animal newOffspring = this.animals.get(strongerPartnerIndex).createOffspring(weakerPartner);
                            this.animals.add(newOffspring);
                        }
                    }
                }
            }
        }
    }

    public List<Animal> animalsCompetition(List<Animal> animalsOnGrass){
        List<Animal> competingAnimals = animalsOnGrass;
        Collections.sort(competingAnimals, Comparator.comparing(Animal::getEnergy, Comparator.reverseOrder())
                .thenComparing(Animal::getDaysAlive)
                .thenComparing(Animal::getCreatedOffspring));
        return competingAnimals;
    }

    public void reduceAnimalsEnergy(){
        for(int i = 0; i < this.animals.size(); i++){
            this.animals.get(i).dailyEnergyReduction();
        }
    }

    public void removeDeadAnimals(){
        for(int i = 0; i < this.animals.size(); i++){
            if(this.animals.get(i).getEnergy() <= 0){
                Animal animal = this.animals.get(i);
                this.positions.put(animal.getPosition(), this.positions.getOrDefault(animal.getPosition(), 0) + 1);
                animal.setDayDeath(this.days);
                this.deadAnimals.add(animal);
                this.animals.remove(i);
            }else{
                this.animals.get(i).updateDaysAlive();
            }
        }
    }

    public void updateDay(){
        this.days += 1;
    }

    public int getAnimalsNumber(){
        return this.animals.size();
    }

    public int getGrassesNumber(){
        return this.grasses.size();
    }

    public int getUnoccupiedFieldsNumber(){
        return this.steppeUnoccupiedFields + this.equatorUnoccupiedFields;
    }

    public List<Integer> getPopularGenome(){
        Map<List<Integer>, Integer> animalsGenomesOccurrences = new HashMap<>();

        if(this.animals.size() > 0){
            for(Animal animal : this.animals){
                List<Integer> genome = animal.getGenome().getGenes();
                if(animalsGenomesOccurrences.containsKey(genome)){
                    int counter = animalsGenomesOccurrences.get(genome) + 1;
                    animalsGenomesOccurrences.put(genome, counter);
                }else{
                    animalsGenomesOccurrences.put(genome, 0);
                }
            }

            List<List<Integer>> sortedGenomes = animalsGenomesOccurrences.entrySet().stream()
                    .sorted(Comparator.comparing(Map.Entry::getValue, Comparator.reverseOrder()))
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());

            this.popularGenome = sortedGenomes.get(0);
            return sortedGenomes.get(0);
        }
        return null;
    }

    public float getAverageEnergyOfAnimals(){
        int summarisedEnergy = 0;
        if(this.animals.size() > 0){
            for(Animal animal : this.animals){
                summarisedEnergy += animal.getEnergy();
            }

            return (float)(summarisedEnergy / this.animals.size());
        }
        return 0f;
    }

    public float getAverageLifespanOFDeadAnimals(){
        int summarisedDays = 0;
        if(this.deadAnimals.size() > 0){
            for(Animal deadAnimal : this.deadAnimals){
                summarisedDays += deadAnimal.getDaysAlive();
            }
            return (float)(summarisedDays / this.deadAnimals.size());
        }
        return 0f;
    }

    public List<Animal> getAnimals(){
        return this.animals;
    }

    public List<Animal> getDeadAnimals(){
        return this.deadAnimals;
    }

}
