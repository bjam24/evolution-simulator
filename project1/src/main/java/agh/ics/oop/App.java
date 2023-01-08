package agh.ics.oop;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Scene;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class App extends Application implements ISimulationChangeObserver {
    @Override
    public void start(Stage settingsStage) throws Exception{
        settingsStage.setTitle("Evolution Simulator (Settings)");

        GridPane settingsGrid = new GridPane();
        settingsGrid.setMinSize(480,650);
        settingsGrid.setPadding(new Insets(10,10,10,10));
        settingsGrid.setAlignment(Pos.CENTER);
        settingsGrid.setVgap(5);
        settingsGrid.setHgap(5);

        // wczytanie alternatywnej,sporzadzonej przez uzytkownika konfiguracji
        Label alternativeConf = new Label("Create own alternative configuration");
        alternativeConf.setStyle("-fx-font-weight: bold");

        Label mapHeight = new Label("Map height (10 - 50):"); // wysokość mapy
        TextField mapHeightTF = new TextField();

        Label mapWidth = new Label("Map width (10 - 50):"); // szerokość mapy
        TextField mapWidthTF = new TextField();

        Label mapVariant = new Label("Map variant:"); // wariant mapy
        ChoiceBox<String> mapVariantCB = new ChoiceBox<>();
        mapVariantCB.getItems().addAll("globe", "hell portal");

        Label grassSpawningStart = new Label("Grass spawning at the start (40 - 200):"); // startowa liczba roślin
        TextField grassSpawningStartTF = new TextField();

        Label grassEnergy = new Label("Grass energy profit (30 - 50):"); // energia zapewniana przez zjedzenie jednej rośliny
        TextField grassEnergyTF = new TextField();

        Label grassSpawnDaily = new Label("Grass spawned in each day (10 - 15):"); // liczba roślin wyrastająca każdego dnia
        TextField grassSpawnDailyTF = new TextField();

        Label grassGrowthVariant = new Label("Grass growth variant:"); // wariant wzrostu roślin
        ChoiceBox<String> grassGrowthVariantCB = new ChoiceBox<>();
        grassGrowthVariantCB.getItems().addAll("forested equators", "toxic corpses");

        Label animalsSpawningStart = new Label("Animals spawning at the start (50 - 100):"); // startowa liczba zwierzaków
        TextField animalsSpawningStartTF = new TextField();

        Label animalEnergy = new Label("Animal start energy (60 - 100):"); // startowa energia zwierzaków
        TextField animalEnergyTF = new TextField();

        Label copulationEnergy = new Label("Minimal energy to copulation (40 - 60):"); // energia konieczna, by uznać zwierzaka za gotowego do rozmnażania
        TextField copulationEnergyTF = new TextField();

        Label createOffspringEnergy = new Label("Energy to create offspring (40 - 60):"); // energia rodziców zużywana by stworzyć potomka
        TextField createOffspringEnergyTF = new TextField();

        Label minimumOffspringMutations = new Label("Minimum offspring mutations (0 - 4):"); // minimalna liczba mutacji u potomków
        TextField minimumOffspringMutationsTF = new TextField();

        Label maximumOffspringMutations = new Label("Maximum offspring mutations max >= min (0 - 4):"); // maksymalna liczba mutacji u potomków
        TextField maximumOffspringMutationsTF = new TextField();

        Label mutationVariant = new Label("Mutation variant:"); // wariant mutacji
        ChoiceBox<String> mutationVariantCB = new ChoiceBox<>();
        mutationVariantCB.getItems().addAll("full randomness", "slight correction");

        Label genomeLength = new Label("Animal genome length (4 - 8):"); // długość genomu zwierzaków
        TextField genomeLengthTF = new TextField();

        Label animalBehavior = new Label("Animal behavior variant:"); // wariant zachowania zwierzaków
        ChoiceBox<String> animalBehaviorCB = new ChoiceBox<>();
        animalBehaviorCB.getItems().addAll("full predestination", "bit of madness");

        Label writeCSV = new Label("Write daily staistics to CSV file:"); // zapis statystyk do pliku CSV
        ChoiceBox<String> writeCSVCB = new ChoiceBox<>();
        writeCSVCB.getItems().addAll("no", "yes");

        // wybranie jednej z uprzednio przygotowanych gotowych konfiguracji
        Label chooseConf = new Label("Choose existing configuration");
        chooseConf.setStyle("-fx-font-weight: bold");

        Label fileSimulationConf = new Label("Simulation configuration:"); // konfiguracja symulacji z pliku
        ChoiceBox<String> fileConfigurationCB = new ChoiceBox<>();
        fileConfigurationCB.getItems().addAll("configuration1", "configuration2", "configuration3");

        // przycisk czyszczący formularz by ustawić nową symulację
        Button clearAllButton = new Button("Clear all");
        clearAllButton.setOnAction(action -> {
            mapVariantCB.setValue(null);
            grassGrowthVariantCB.setValue(null);
            mutationVariantCB.setValue(null);
            animalBehaviorCB.setValue(null);
            fileConfigurationCB.setValue(null);
            writeCSVCB.setValue(null);
            mapHeightTF.clear();
            mapWidthTF.clear();
            grassSpawningStartTF.clear();
            grassEnergyTF.clear();
            grassSpawnDailyTF.clear();
            animalsSpawningStartTF.clear();
            animalEnergyTF.clear();
            copulationEnergyTF.clear();
            createOffspringEnergyTF.clear();
            minimumOffspringMutationsTF.clear();
            maximumOffspringMutationsTF.clear();
            genomeLengthTF.clear();
        });

        // przycisk uruchamiający nową symulację
        Button startButton = new Button("Start simulation");
        startButton.setOnAction(action -> {
            List<Integer> parameters = new ArrayList<>(); // lista numerycznych paramterów symulacji

            try{
                if(fileConfigurationCB.getSelectionModel().isEmpty()){ // własna konfiguracja
                    Integer mapHeightInt = Integer.valueOf(mapHeightTF.getText());
                    Integer mapWidthInt = Integer.valueOf(mapWidthTF.getText());

                    int mapVariantInt = -1;
                    if(mapVariantCB.getValue().equals("globe")){ mapVariantInt = 0;
                    }else if(mapVariantCB.getValue().equals("hell portal")){ mapVariantInt = 1;}

                    Integer grassSpawningStartInt = Integer.valueOf(grassSpawningStartTF.getText());
                    Integer grassEnergyInt = Integer.valueOf(grassEnergyTF.getText());
                    Integer grassSpawningDailyInt = Integer.valueOf(grassSpawnDailyTF.getText());

                    int grassGrowthVariantInt = -1;
                    if(grassGrowthVariantCB.getValue().equals("forested equators")){ grassGrowthVariantInt = 0;
                    }else if(grassGrowthVariantCB.getValue().equals("toxic corpses")){ grassGrowthVariantInt = 1;}

                    Integer animalsSpawningStartInt = Integer.valueOf(animalsSpawningStartTF.getText());
                    Integer animalEnergyInt = Integer.valueOf(animalEnergyTF.getText());
                    Integer copulationEnergyInt = Integer.valueOf(copulationEnergyTF.getText());
                    Integer createOffspringEnergyInt = Integer.valueOf(createOffspringEnergyTF.getText());
                    Integer minimumOffspringMutationsInt = Integer.valueOf(minimumOffspringMutationsTF.getText());
                    Integer maximumOffspringMutationsInt = Integer.valueOf(maximumOffspringMutationsTF.getText());

                    int mutationVariantInt = -1;
                    if(mutationVariantCB.getValue().equals("full randomness")){ mutationVariantInt = 0;
                    }else if(mutationVariantCB.getValue().equals("slight correction")){ mutationVariantInt = 1;}

                    Integer genomeLengthInt = Integer.valueOf(genomeLengthTF.getText());

                    int animalBehaviorInt = -1;
                    if(animalBehaviorCB.getValue().equals("full predestination")){ animalBehaviorInt = 0;
                    }else if(animalBehaviorCB.getValue().equals("bit of madness")){ animalBehaviorInt = 1;}

                    int writeCSVInt = -1;
                    if(writeCSVCB.getValue().equals("no")){ writeCSVInt = 0;
                    }else if(writeCSVCB.getValue().equals("yes")){ writeCSVInt = 1;}

                    Collections.addAll(parameters, mapHeightInt, mapWidthInt, mapVariantInt, grassSpawningStartInt,
                            grassEnergyInt, grassSpawningDailyInt, grassGrowthVariantInt, animalsSpawningStartInt,
                            animalEnergyInt, copulationEnergyInt, createOffspringEnergyInt, minimumOffspringMutationsInt,
                            maximumOffspringMutationsInt, mutationVariantInt, genomeLengthInt, animalBehaviorInt, writeCSVInt);

                    SimulationConfiguration configuration = new SimulationConfiguration(parameters);
                    parameters = configuration.getParameters();
                }else{ // wybrana konfiguracja z pliku
                    SimulationConfiguration configuration = new SimulationConfiguration();
                    String configurationName = fileConfigurationCB.getValue();
                    configuration.loadFromFile(configurationName);
                    parameters = configuration.getParameters();
                }

                simulationWindow(parameters); // uruchomienie nowego okna symulacji
            }catch(IllegalArgumentException e){ // sprawdzenie czy dane są wprowadzone i czy spełniają kryteria
                System.out.println(e);
            }catch(FileNotFoundException e){ // sprawdzenie czy plik o wybranej nazwie nadal istnieje w folderze
                System.out.println(e);
            }
        });

        // ustawienia szerokości CheckBoxów i przycisków
        startButton.setPrefWidth(150);
        clearAllButton.setPrefWidth(150);
        mapVariantCB.setPrefWidth(150);
        grassGrowthVariantCB.setPrefWidth(150);
        mutationVariantCB.setPrefWidth(150);
        animalBehaviorCB.setPrefWidth(150);
        fileConfigurationCB.setPrefWidth(150);
        writeCSVCB.setPrefWidth(150);

        // ustawienie położenia elementów formularza
        settingsGrid.add(alternativeConf, 0, 0);
        settingsGrid.add(mapHeight, 0, 1);
        settingsGrid.add(mapHeightTF, 1, 1);
        settingsGrid.add(mapWidth, 0, 2);
        settingsGrid.add(mapWidthTF, 1, 2);
        settingsGrid.add(mapVariant, 0, 3);
        settingsGrid.add(mapVariantCB, 1, 3);
        settingsGrid.add(grassSpawningStart, 0, 4);
        settingsGrid.add(grassSpawningStartTF, 1, 4);
        settingsGrid.add(grassEnergy, 0, 5);
        settingsGrid.add(grassEnergyTF, 1, 5);
        settingsGrid.add(grassSpawnDaily, 0, 6);
        settingsGrid.add(grassSpawnDailyTF, 1, 6);
        settingsGrid.add(grassGrowthVariant, 0, 7);
        settingsGrid.add(grassGrowthVariantCB, 1, 7);
        settingsGrid.add(animalsSpawningStart, 0, 8);
        settingsGrid.add(animalsSpawningStartTF, 1, 8);
        settingsGrid.add(animalEnergy, 0, 9);
        settingsGrid.add(animalEnergyTF, 1, 9);
        settingsGrid.add(copulationEnergy, 0, 10);
        settingsGrid.add(copulationEnergyTF, 1, 10);
        settingsGrid.add(createOffspringEnergy, 0, 11);
        settingsGrid.add(createOffspringEnergyTF, 1, 11);
        settingsGrid.add(minimumOffspringMutations, 0, 12);
        settingsGrid.add(minimumOffspringMutationsTF, 1, 12);
        settingsGrid.add(maximumOffspringMutations, 0, 13);
        settingsGrid.add(maximumOffspringMutationsTF, 1, 13);
        settingsGrid.add(mutationVariant, 0, 14);
        settingsGrid.add(mutationVariantCB, 1, 14);
        settingsGrid.add(genomeLength, 0, 15);
        settingsGrid.add(genomeLengthTF, 1, 15);
        settingsGrid.add(animalBehavior, 0, 16);
        settingsGrid.add(animalBehaviorCB, 1, 16);
        settingsGrid.add(writeCSV, 0, 17);
        settingsGrid.add(writeCSVCB, 1, 17);
        settingsGrid.add(chooseConf, 0, 18);
        settingsGrid.add(fileSimulationConf, 0, 19);
        settingsGrid.add(fileConfigurationCB, 1, 19);
        settingsGrid.add(clearAllButton, 0, 20);
        settingsGrid.add(startButton, 1, 20);

        Scene settingsScene = new Scene(settingsGrid);
        settingsStage.setScene(settingsScene);
        settingsStage.show();
    }

    public void renderSimulationMap(HBox hbox, SimulationEngine engine){
        GridPane gridRenderSimulationMap = new GridPane();
        gridRenderSimulationMap.setGridLinesVisible(true);
        gridRenderSimulationMap.setHgap(1);
        gridRenderSimulationMap.setVgap(1);

        int rows = engine.getParameters().get(0);
        int columns = engine.getParameters().get(1);

        int scale;
        if (rows >= columns){
            scale = rows;
        }else{
            scale = columns;
        }

        for(int i = 0; i < columns; i++){
            for(int j = 0; j < rows; j++){
                Rectangle rec = new Rectangle();
                rec.setWidth((int)(600/scale));
                rec.setHeight((int)(600/scale));
                rec.setFill(Color.web("#c2f045"));

                if(engine.map.isOccupiedGrass(new Vector2d(i, j))){
                    rec.setFill(Color.web("#64dd17"));
                }

                gridRenderSimulationMap.add(rec, i, j);

                if(engine.map.isOccupiedAnimals(new Vector2d(i, j))){
                    Circle circle = new Circle((int)(300/scale), (int)(300/scale), (int)(300/scale));
                    Color animalColor = engine.map.getAnimalsAt(new Vector2d(i, j)).get(0).getAnimalColor();
                    circle.setFill(animalColor);

                    int finalI = i;
                    int finalJ = j;

                    circle.setOnMouseClicked(new EventHandler<MouseEvent>() {
                        int countCircleClicked = 0;
                        @Override
                        public void handle(MouseEvent event) {
                            if(countCircleClicked % 2 == 0){
                                for(Node node: gridRenderSimulationMap.getChildren()){
                                    node.setDisable(true);
                                }

                                circle.setDisable(false);
                                circle.setFill(Color.web("#0074D9"));

                                if(engine.getAnimalStatistics().getAnimal() == null){
                                    Animal animal = engine.map.getAnimalsAt(new Vector2d(finalI, finalJ)).get(0);
                                    engine.getAnimalStatistics().setAnimal(animal);
                                }

                            }else{
                                for(Node node: gridRenderSimulationMap.getChildren()){
                                    node.setDisable(false);
                                }
                                circle.setFill(animalColor);
                                engine.getAnimalStatistics().setAnimal(null);
                            }

                            countCircleClicked += 1;
                        }
                    });
                    gridRenderSimulationMap.add(circle, i, j);
                }
            }
        }
        hbox.getChildren().add(0, gridRenderSimulationMap);
    }

    public void showSimulationStatistics(HBox hbox, SimulationEngine engine){
        VBox vbox = new VBox();
        Label statisticsMap = new Label("Map statistics"); // Statystyki symulacji
        statisticsMap.setStyle("-fx-font-weight: bold");

        Text statisticsDays = new Text("Days (Max 400): " + engine.getStatistics().getDays());
        Text statisticsAllAnimals = new Text("Animals: " + engine.getStatistics().getAnimalsNumber());
        Text statisticsAllGrasses = new Text("Grasses: " + engine.getStatistics().getGrassesNumber());
        Text statisticsUnoccupiedFields = new Text("Unoccupied fields: " + engine.getStatistics().getUnoccupiedFieldsNumber());
        Text statisticsPopularGenotype = new Text("Popular genotype: " + engine.getStatistics().getPopularGenotype().toString());
        Text statisticsAverageEnergy = new Text("Average animal energy: " + engine.getStatistics().getAverageEnergyOfAliveAnimals());
        Text statisticsAverageLife = new Text("Average lifespan of dead animals: " +  engine.getStatistics().getAverageLifeSpanOfDeadAnimals());

        Label statisticsAnimal = new Label("Animal statistics"); // Statystyki wybranego zwierzęcia
        statisticsAnimal.setStyle("-fx-font-weight: bold");
        Text statisticsGenome = new Text("Genome: " + engine.getAnimalStatistics().getGenome());
        Text statisticsActivatedGeneIndex = new Text("Activated gene index: " + engine.getAnimalStatistics().getActivatedGeneIndex());
        Text statisticsEnergy = new Text("Energy: " + engine.getAnimalStatistics().getEnergy());
        Text statisticEatenGrasses = new Text("Eaten grasses: " + engine.getAnimalStatistics().getEatenGrasses());
        Text statisticsCreatedOffspring = new Text("Created offspring: " + engine.getAnimalStatistics().getCreatedOffspring());
        Text statisticsAnimalLifespan = new Text("Lifespan in days: " + engine.getAnimalStatistics().getDaysLifespan());
        Text statisticsDeathDay = new Text("Day of death: " + engine.getAnimalStatistics().getDayOfDeath());

        vbox.getChildren().addAll(statisticsMap, statisticsDays, statisticsAllAnimals, statisticsAllGrasses,
                statisticsUnoccupiedFields, statisticsPopularGenotype, statisticsAverageEnergy, statisticsAverageLife,
                statisticsAnimal, statisticsGenome, statisticsActivatedGeneIndex, statisticsEnergy, statisticEatenGrasses,
                statisticsCreatedOffspring, statisticsAnimalLifespan, statisticsDeathDay);

        hbox.getChildren().add(0, vbox);
    }

    public void simulationWindow(List<Integer> parameters){
        SimulationEngine engine = new SimulationEngine(parameters);
        Thread engineThread = new Thread(engine);
        engineThread.setDaemon(true);
        engineThread.start();
        engine.addObserver(this);

        Stage newSimulationStage = new Stage();
        newSimulationStage.setTitle("Evolution Simulator");

        HBox hbox = new HBox(40);
        engine.setHBox(hbox);

        renderSimulationMap(hbox, engine);
        showSimulationStatistics(hbox, engine);

        Button stopSimulation = new Button("Stop simulation");
        stopSimulation.setOnMouseClicked(new EventHandler<MouseEvent>() {
            int countButtonClicked = 0;
            @Override
            public void handle(MouseEvent event) {
                if(countButtonClicked % 2 == 0){
                    engine.getAnimalStatistics().setAnimal(null);
                    engine.suspend();
                    stopSimulation.setText("Resume simulation");
                }else{
                    engine.resume();
                    stopSimulation.setText("Stop simulation");
                }
                countButtonClicked += 1;
            }
        });

        StackPane root = new StackPane();
        root.getChildren().add(hbox);
        root.getChildren().add(stopSimulation);
        StackPane.setAlignment(stopSimulation, Pos.BOTTOM_LEFT);
        root.setPadding(new Insets(30,30,30,30));

        Scene newSimulationScene = new Scene(root);
        newSimulationStage.setScene(newSimulationScene);
        newSimulationStage.show();
    }

    public void updateSimulationWindow(HBox hboxRenderSimulationMap, SimulationEngine engine){
        Platform.runLater(() -> {
            if(hboxRenderSimulationMap != null){
                hboxRenderSimulationMap.getChildren().clear();
                renderSimulationMap(hboxRenderSimulationMap, engine);
                showSimulationStatistics(hboxRenderSimulationMap, engine);
            }
        });
    }
}
