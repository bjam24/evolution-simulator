# Evolution simulator

This project was made for the Object-Oriented Programming course at the AGH UST in 2022/2023. I uploaded it a few days ago because I am considering to create even more advanced version.

## Description
This project is the implementation of a simple simulator based on Darwin's Theory. It is based on Multithreading so there is possibility to run many simulations at the same time. Animals are moving on the map, eating grass, and reproducing. Each move requires some energy, so animals need to eat to survive. Also, breeding consumes part of the parent's energy. An animal dies after having run out of energy.

Each animal has its genome. A genome is a combination of genomes inherited from an animal's parents. Genome is built of numbers from 0 to 7. Each number is correlated with the animal's preference to move in a specific direction.
The more repetition of a digit, the more likely it is for the animal to move in the direction represented by this digit.

## Variants
Certain aspects of the simulation are configurable and can significantly change its course. Some are simple numerical parameters (e.g. initial population sizes). Some of them, however, significantly modify its rules. 
This applies in particular to: the operation of the map, the operation of plant growth, the operation of mutations, and the behavior of animals.

In the case of a map, the key is how we handle its edges. We will implement the following variants:

- **globe** - the left and right edges of the map are looped (if the animal goes beyond the left edge, it will appear on the right - and if it goes beyond the right, then on the left); the upper and lower edges of the map are the poles 
you cannot enter them (if the animal tries to go beyond these edges of the map, it remains in the field it was on and its direction changes to the opposite);

- **hell portal** - if the animal goes beyond the edge of the map, it goes to a magical portal; its energy decreases by a certain amount (the same as in the case of the offspring's generation), and then it is teleported to a new,
random place on the map. When it comes to plant growth, certain fields are strongly preferred, according to the Pareto principle. There is an 80% chance that the new plant will grow in the preferred field and only a 20% chance
 that it will grow in the second-class field. About 20% of all places on the map are preferred, 80% of places are considered unattractive. We implement the following variants:

- **forested equators** - plants prefer the horizontal strip of fields in the central part of the map (imitating the equator and the surrounding area);

- **toxic corpses** - plants prefer those fields where animals die least often - they grow in those fields where the fewest animals died during the simulation.
In the case of mutations, we have two simple options:

- **complete randomness** - mutation changes a gene to any other gene;

- **slight correction** - a mutation changes a gene by 1 up or down (e.g. gene 3 can be changed to 2 or 4, and gene 0 to 1 or 7).
The behavioral variants are similarly simple:

- **full predestination** - the animal always executes the genes sequentially, one after the other;

- **a bit of madness** - in 80% of cases, after executing a gene, the animal activates the gene immediately following it, but in 20% of cases it jumps to another, random gene.


Features
Adjusting the speed of the simulation,
Highlighting animals with dominant genomes,
Showing dominant genomes list and animals that have such genomes,
Tracking the specific animal (the number of its direct children and indirect descendants),
Checking which animals are placed on the selected field,
Showing simulation preview window,
Showing statistics chart,
Saving statistics to a file
Displaying simulation on two maps at the same time:
Folding map - a map on which, after crossing the border, the animal will be on the other side of the map,
Fenced map - a map surrounded by a fence which makes it impossible for animals to be on the other side of the map,
Magic strategy - a simulation mode in which the number of animals will be doubled after it has dropped to the specific value. The number of such respawns is limited.
A possibility to adjust many settings before starting the simulation.
Core technology stack
Java 16,
Gradle,
JavaFX
