# Java Side - use the saved model
## Installation
As pointed out earlier, I will assume that you have Java8 and Maven3 installed on your machine.

Now after cloning this repository, you will have to install the Java project first, befour you can start using it. Initially, navigate to your [MLJava](https://github.com/IndustrialML/R2Java/tree/master/MLJava) directory.

With Maven and my [pom](https://github.com/IndustrialML/R2Java/tree/master/MLJava/pom.xml) dependency file, the installation is as easy as running the following command:
```bash
	mvn install
```

## Usage
After installing the project, you can use the [MNSITClassifier](https://github.com/IndustrialML/R2Java/blob/master/MLJava/src/main/java/RandomForest/MNISTClassifier.java) class, to import a previously saved PMML file and start making predictions. You can find the full documentation inside the repository for the [Python2Java](https://github.com/IndustrialML/Python2Java/tree/master/MaschineLearning4J/src/main/java/RandomForest) project. But for now you will need to know two things:
1. You can execute the main class with:
```bash
	mvn exec:java -Dexec.mainClass="RandomForest.MNISTClassifier"
```
2. You can pass program parameters to the class by adding the `-Dexec.args="..."` argument to the `mvn exec:java` command. You can pass in following arguments to the program:
```bash
usage: MNISTClassifier
 -b,--benchmark-test   pass, if you want to run and print the benchmark
                       test
 -c,--compare          pass, if you want the results from Python and Java
                       to be compared for sameness. This action cannot be
                       performed for the R-technology
 -h,--help             print this info message again
 -n,--n-trees <arg>    number of decision trees that were used to create
                       the PMML. Please check for which number of trees,
                       you have a valid PMML file on your system. Default
                       value will be 50
 -p,--picture <arg>    name of the picture, to predict its displayed
                       digit. <Arg> can either be a full path or a file in
                       the Data/Own_dat directory of this project. Default
                       value will be Data/Own_dat/MNIST-7.png
 -r,--use-R            pass, if the pmml you want to load is from a model,
                       created with R
```
**Notes**:
* Always use the `-r` parameter!
* Never use the `-c` parameter!
* Make sure to pass in a valid value to the `-n` parameter. Do not pass it in if you want to use the default of 50 trees
* If you use the `-p` parameter, you need to pass in an absolute path to a .png file

Basic example:
```bash
	mvn exec:java -Dexec.mainClass="RandomForest.MNISTClassifier" -Dexec.args="-r"
```

Advanced example:
```bash
	mvn exec:java -Dexec.mainClass="RandomForest.MNISTClassifier" -Dexec.args="-r -p C:/Users/lema/Pictures/Mnist-test.png -n 500 -b"
```

The output might look confusing at first and might include a lot of superfluous information and warnings, but if it says: `[INFO] Build SUCCESS` in the end, everything worked as intended. By scrolling up you will find the output of the actual program:
```bash
	Will use given picture at: C:/Users/lema/Pictures/Mnist-test.png to predict its number.
	Creating an evaluator from PMML file: ../MLR/models/pmml/model_rf_500trees_60000.pmml.
	Depending on the size of the RandomForest, this might take a while...
	Finished creating the evaluator! Took 25105ms to finish.

	The prediction call for given png, using the Random Forest, took 2621ms. (reading the pixel information included)
	--> The given picture at "C:/Users/lema/IdeaProjects/Maschine Learning/Data/Own_dat/Mnist-7.png" is probably a: 7

	Running benchmark test now...
			<some more unnecessary information>
 	Printed benchmark results to: MaschineLearning4J/src/main/java/RandomForest/benchmark_500.html
			<possibly some more unnecessary information>
	
```