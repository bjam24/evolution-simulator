package agh.ics.oop;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Genome {
    private List<Integer> genes;
    private int activatedGenesCounter;
    private List<Integer> parameters;

    public Genome(List<Integer> genesStrongerParent, List<Integer> genesWeakerParent, float genesProportion,
                  List<Integer> parameters){
        this.parameters = parameters;
        this.genes = createOffspringGenome(genesStrongerParent, genesWeakerParent, genesProportion);
        this.activatedGenesCounter = 0;
    }

    public Genome(List<Integer> parameters){
        this.parameters = parameters;
        this.genes = generateGenome(this.parameters.get(14));
        this.activatedGenesCounter = 0;
    }

    public List<Integer> generateGenome(int n){ // generowanie losowego genomu
        return new Random().ints(n, 0, 8)
                .boxed()
                .collect(Collectors.toList());
    }

    public List<Integer> createOffspringGenome(List<Integer> genesStrongerParent, List<Integer> genesWeakerParent,
                                               float genesProportion){
        List<Integer> newGenes = new ArrayList<Integer>();
        int intersectionIndex = (int)(genesProportion * genesStrongerParent.size());

        Random rand = new Random();
        int numericalGenomeSite = rand.nextInt(2);

        if(numericalGenomeSite == 0){ // prawa strona genotypu silniejszego rodzica
            List<Integer> subGenesStrongerParent = genesStrongerParent.subList(intersectionIndex, genesStrongerParent.size());
            List<Integer> subGenesWeakerParent = genesWeakerParent.subList(0, intersectionIndex);
            newGenes.addAll(subGenesWeakerParent);
            newGenes.addAll(subGenesStrongerParent);
        }else if(numericalGenomeSite == 1){ // lewa strona genotypu silniejszego rodzica
            List<Integer> subGenesStrongerParent = genesStrongerParent.subList(0, genesStrongerParent.size() - intersectionIndex);
            List<Integer> subGenesWeakerParent = genesWeakerParent.subList(genesStrongerParent.size() - intersectionIndex, genesStrongerParent.size());
            newGenes.addAll(subGenesStrongerParent);
            newGenes.addAll(subGenesWeakerParent);
        }

        for(int i = this.parameters.get(11); i <= this.parameters.get(12); i++){
            int genToMutateIndex = rand.nextInt(genesStrongerParent.size());

            if(this.parameters.get(13) == 0){ // wariant mutacji (pełna losowość)
                int newGene = rand.nextInt(this.parameters.get(14));
                newGenes.set(genToMutateIndex, newGene);
            }else{ // wariant mutacji (lekka korekta)
                int changeGeneDirection = rand.nextInt(2);
                int genToMutate = genesStrongerParent.get(genToMutateIndex);
                int mutatedGene;

                if(changeGeneDirection == 0) { // o 1 w górę
                    mutatedGene = next(genToMutate);
                }else { // o 1 w dół
                    mutatedGene = previous(genToMutate);
                }

                newGenes.set(genToMutateIndex, mutatedGene);
            }
        }

        return newGenes;
    }

    public int randomActivatedGeneIndex(){ // losowanie aktywnej części genomu
        Random rand = new Random();
        this.activatedGenesCounter += 1;
        return rand.nextInt(this.genes.size());
    }

    public int nextActivatedGeneIndex(int oldIndex, int behaviorVariant){ // ustawienie aktywnej części genomu na następny dzień
        int newIndex = oldIndex;

        if(behaviorVariant == 0){ // zachowanie zwierzęcia wariant (pełna predestynacja)
            newIndex += 1;
            if (newIndex > this.genes.size() - 1){
                newIndex = 0; // aktywacja wraca na początek listy genów
            }
        }else{ // zachowanie zwierzęcia wariant (nieco szaleństwa)
            Random rand = new Random();

            if(activatedGenesCounter % 5 == 0){
                newIndex = rand.nextInt(this.genes.size());
            }else{
                newIndex += 1;
                if (newIndex > this.genes.size() - 1){
                    newIndex = 0; // aktywacja wraca na początek listy genów
                }
            }
        }
        this.activatedGenesCounter += 1;

        return newIndex;
    }

    public int next(int gene){
        int newMutatedGene = gene;

        int result = switch(newMutatedGene){
            case 0 -> 1;
            case 1 -> 2;
            case 2 -> 3;
            case 3 -> 4;
            case 4 -> 5;
            case 5 -> 6;
            case 6 -> 7;
            case 7 -> 0;
            default -> throw new IllegalStateException("Unexpected value: " + newMutatedGene);
        };
        return result;
    }

    public int previous(int gene){
        int newMutatedGene = gene;

        int result = switch(newMutatedGene){
            case 0 -> 7;
            case 1 -> 0;
            case 2 -> 1;
            case 3 -> 2;
            case 4 -> 3;
            case 5 -> 4;
            case 6 -> 5;
            case 7 -> 6;
            default -> throw new IllegalStateException("Unexpected value: " + newMutatedGene);
        };
        return result;
    }

    public int getGene(int idx){ // pobranie wartości aktywnej części genomu
        return this.genes.get(idx);
    }

    public List<Integer> getGenes(){
        return this.genes;
    }
}
