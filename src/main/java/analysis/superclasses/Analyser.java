package main.java.analysis.superclasses;

import main.java.model.model.Model;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

public abstract class Analyser {

    protected Analyser(String trialName){

        this.trialName = trialName;

        model = new Model();

    }

    final String trialName;

    protected Model model;

    protected abstract double[] computeData();

    protected abstract String[] getIVValues();

    int getIVCount(){
        return getIVValues().length;
    }


    void writeResults(String fileName, double[][] allData){

        System.out.println("Writing Data");

        //write the results
        try {
            File file = new File(fileName);
            if(!file.getParentFile().exists()){
                if(!file.getParentFile().mkdirs()){
                    throw new RuntimeException("Could not create analysis directories");
                }
            }
            FileWriter fileWriter = new FileWriter(file);
            PrintWriter out = new PrintWriter(fileWriter);
            //print a header
            out.print("IV");
            for(int i = 0; i < allData.length; i++){
                out.printf(",Trial %d", (i+1));
            }
            out.print('\n');
            //for every iv
            for(int i = 0; i < getIVCount(); i++){
                out.printf(getIVValues()[i]);
                //print out each column of data
                for (double[] allDatum : allData) {
                    out.printf(",%f", allDatum[i]);
                }
                out.print('\n');
            }
            out.close();
        }catch (Exception e){
            System.out.println("Failed writing file "+fileName);
            e.printStackTrace();
        }

    }


    public void runAnalyser(int numTrials){

        double[][] allData = new double[numTrials][getIVCount()];

        //run the trials
        for(int i = 0; i < numTrials; i++){

            model.initializeRandomBoard();

            allData[i] = computeData();

        }

        writeResults("data/" + trialName + ".csv", allData);

    }


}
