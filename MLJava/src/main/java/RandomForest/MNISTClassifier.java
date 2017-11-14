package RandomForest;

import org.apache.commons.cli.*;

import java.io.*;
import java.util.Arrays;

public class MNISTClassifier {

    private final static String importDirPython = "../Maschine Learning/RandomForest/MaaS/export/";
    private final static String importDirR = "../MLR/models/pmml/";
    private final static String picDir = "../Maschine Learning/Data/Own_dat/"; //where the test pictures are stored
    private final static String picName = "MNIST-7.png"; //default
    private static String picPath = picDir + picName; //default

    private static String n_estimators = "50";//default
    private static String importDir = importDirPython;//default
    private static String pmmlFile = importDir + "model_rf_" + n_estimators + "trees_60000.pmml";//default
    private static boolean compare = false; //if results in java/python should be compared
    private static boolean benchmark = false; //if benchmark test should be run


    public static void main(String[] args) {
        evaluateArguments(args);

        System.out.println("Creating an evaluator from PMML file: " + pmmlFile + ". \nDepending on the size of the RandomForest, this might take a while...");
        long time = System.nanoTime();
        RandomForestWrapper randomForest = new RandomForestWrapper(pmmlFile, picDir);
        long timeDifference = (System.nanoTime() - time) / 1000000;
        System.out.println("Finished creating the evaluator! Took " + timeDifference + "ms to finish.");
        //10->3,3s | 100->5,2s | 1000->40s

        if (compare) {
            String statisticsFile = importDir + "statistics_" + n_estimators + ".json";
            randomForest.compareResults(statisticsFile);
        }

        time = System.nanoTime();
        int prediction = randomForest.predict(picPath, false);
        timeDifference = (System.nanoTime() - time) / 1000000;
        System.out.println("\nThe prediction call for given png, using the Random Forest, took " + timeDifference + "ms. (reading the pixel information included)");
        //if only prediction: 10->1ms | 100->4ms |1000->17ms (loading is approximatly 160ms)
        System.out.println("--> The given picture at \"" + picPath + "\" is probably a: " + prediction);

        if (benchmark) {
            BenchmarkTest test = new BenchmarkTest(randomForest, 2000, n_estimators);
            test.run();
        }
    }


    /**
     * Evaluates given program arguments
     *
     * @param args program arguments passed forward from main function
     */
    private static void evaluateArguments(String[] args) {
        CommandLineParser parser = new DefaultParser();
        Options options = new Options();

        options.addOption(new Option("h", "help", false, "print this info message again"));
        options.addOption(new Option("r", "use-R", false, "pass, if the pmml you want to load is from a model, created with R"));
        options.addOption(new Option("n", "n-trees", true, "number of decision trees that were used to create the PMML. Please check for which number of trees, you have a valid PMML file on your system. Default value will be 50"));
        options.addOption(new Option("p", "picture", true, "name of the picture, to predict it's displayed digit. <Arg> can either be a full path or a file in the Data/Own_dat directory of this project. Default value will be Data/Own_dat/MNIST-7.png"));
        options.addOption(new Option("c", "compare", false, "pass, if you want the results from Python and Java to be compared for sameness. This action cannot be performed for the R-technology"));
        options.addOption(new Option("b", "benchmark-test", false, "pass, if you want to run and print the benchmark test"));

        CommandLine line = null;
        try {
            line = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println("You passed in wrong program arguments: "+ Arrays.toString(args));
            System.out.println("Printing out -help and exiting the program:");
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("MNISTClassifier", options);
            System.exit(1);
        }
        if (line.hasOption("h")) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("MNISTClassifier", options);
            System.exit(0);
        }
        if (line.hasOption("r")) {
            importDir = importDirR;
            pmmlFile = importDir + "model_rf_" + n_estimators + "trees_60000.pmml";//default
        }
        if (line.hasOption("n")) {
            pmmlFile = importDir + "model_rf_" + line.getOptionValue("n") + "trees_60000.pmml";
            File f = new File(pmmlFile);
            if (f.exists() && !f.isDirectory()) {
                n_estimators = line.getOptionValue("n");
            } else {
                System.out.println("You do not have a valid PMML file for the given number of trees: " + line.getOptionValue("n"));
                System.out.println("Please check the directory: " + importDir);
                System.out.println("Exiting program now...");
                System.exit(1);
            }
        }
        if (line.hasOption("e")) {
            if (importDir.equals(importDirPython)) {
                compare = true;
            } else {
                System.out.println("You cannot use the -e option with the R-technology. The Program will skip this step.");
            }
        }
        if (line.hasOption("p")) {
            setPicPath(line.getOptionValue("p"));
        }
        if (line.hasOption("b")) {
            benchmark = true;
        }
    }

    /**
     * Utility function for evaluateArguments, sets "picPath", according to given argument. Given argument could be full path or filename in picdir, with or without .png ending
     *
     * @param fileArgument given value of the program arguments
     */
    private static void setPicPath(String fileArgument) {
        String path = fileArgument;//assume full path given
        if (!path.contains(".png")) {
            path += ".png";
        }

        if (!new File(path).exists()) { //if no full path is given try picDir
            path = picDir + path;
        }

        File f = new File(path);
        if (f.exists() && !f.isDirectory()) {
            picPath = path;
            System.out.println("Will use given picture at: " + path + " to predict its number.");
        } else {
            System.out.println("The given file at: " + path + " is not a valid png file!");
            System.out.println("Exiting program...");
            System.exit(1);
        }
    }
}