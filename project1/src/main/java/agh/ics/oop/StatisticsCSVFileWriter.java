package agh.ics.oop;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class StatisticsCSVFileWriter{
    private int randomId;
    private Statistics statistics;

    public StatisticsCSVFileWriter(Statistics simulationStatistics) {
        Random rand = new Random();
        this.randomId = rand.nextInt(10000);
        this.statistics = simulationStatistics;
        createFile(this.randomId);
        initFileContent(this.randomId, this.statistics);
    }

    public static void createFile(int fileId)
    {
        File file = new File("src/main/resources/statistics" + fileId + ".csv");

        try{
            if (file.createNewFile())
                System.out.println("CSV file created");
            else
                System.out.println("CSV file already exists. Try again.");
        }
        catch (Exception e) {
            System.err.println(e);
        }
    }

    public static void initFileContent(int fileId, Statistics newStatistics){
        try(FileWriter writer = new FileWriter("src/main/resources/statistics" + fileId + ".csv")){
            writer.append("Days,Animals,Grasses,UnoccupiedFields,PopularGenome,AverageEnergy,AverageLifespanDeadAnimals");
            writer.append("\n");

            String days = Integer.toString(newStatistics.getDays());
            String animalsNumber = Integer.toString(newStatistics.getAnimalsNumber());
            String grassesNumber = Integer.toString(newStatistics.getGrassesNumber());
            String unoccupiedFieldsNumber = Integer.toString(newStatistics.getUnoccupiedFieldsNumber());
            String popularGenotype = newStatistics.getPopularGenotype().toString();
            String averageEnergyOfAliveAnimals = Float.toString(newStatistics.getAverageEnergyOfAliveAnimals());
            String averageLifespanDeadAnimals = Float.toString(newStatistics.getAverageLifeSpanOfDeadAnimals());

            StringBuilder newStringBuilder = new StringBuilder();
            newStringBuilder.append(days).append(",").append(animalsNumber).append(",").append(grassesNumber).append(",")
                    .append(unoccupiedFieldsNumber).append(",").append(popularGenotype).append(",")
                    .append(averageEnergyOfAliveAnimals).append(",").append(averageLifespanDeadAnimals);

            writer.append(newStringBuilder);
            writer.append("\n");

            System.out.println("Initial content successfully written to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public void updateFileContent(){
        try(FileWriter writer = new FileWriter("src/main/resources/statistics" + this.randomId + ".csv", true)){
            String days = Integer.toString(this.statistics.getDays());
            String animalsNumber = Integer.toString(this.statistics.getAnimalsNumber());
            String grassesNumber = Integer.toString(this.statistics.getGrassesNumber());
            String unoccupiedFieldsNumber = Integer.toString(this.statistics.getUnoccupiedFieldsNumber());
            String popularGenotype = this.statistics.getPopularGenotype().toString();
            String averageEnergyOfAliveAnimals = Float.toString(this.statistics.getAverageEnergyOfAliveAnimals());
            String averageLifespanDeadAnimals = Float.toString(this.statistics.getAverageLifeSpanOfDeadAnimals());

            StringBuilder newStringBuilder = new StringBuilder();
            newStringBuilder.append(days).append(",").append(animalsNumber).append(",").append(grassesNumber).append(",")
                    .append(unoccupiedFieldsNumber).append(",").append(popularGenotype).append(",")
                    .append(averageEnergyOfAliveAnimals).append(",").append(averageLifespanDeadAnimals);

            writer.append(newStringBuilder);
            writer.append("\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
