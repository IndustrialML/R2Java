# R2Java - R-Model as a Service
This project was set up by Matthias Leopold and Veronica Pohl at Zühlke Engineering AG Schlieren, to gather the option to use a trained R model for production. We looked at the approach **"Model as a Service"**. This means that the whole machine learning model is supposed to be transferred from R to Java, to execute predictions directly in Java.

## Project structure
The project is split into two sub-projects: 
1. [MLR (R part)](https://github.com/IndustrialML/R2Java/MLR)
2. [MLJava (Java part)](https://github.com/IndustrialML/R2Java/tree/master/MLJava)


### Requirenments
* Installed [R](https://cran.r-project.org/) and integrated development environment (IDE) for R like [RStudio](https://www.rstudio.com/).
* Installed [JDK8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) or higher and [Maven3](https://maven.apache.org/download.cgi).
* Install the MLJava project using maven:
	1. Navigate to the [MLJava](https://github.com/IndustrialML/R2Java/tree/master/MLJava) directory
	2. Run `mvn install`
## Getting started 

Assuming you have cloned this repository and meet the requirements shown above:

1. Set your working directory to MLR e.g. `setwd("~/R2Java/MLR")` in R.
2. Run the *train_randomForestonMNIST_pmml.R file* by using the command:
```{r}
source('train_randomForestonMNIST_pmml.R')
```

This will load the MNIST data set where one image has 28*28 = 784 pixels with values between 0 and 255. The set is organized row-wise. After that the model is trained by using random forests and save it as *.pmml file*.

3. To load the saved *.pmml file* into java and start using it, you can run the [MNISTClassifier](https://github.com/IndustrialML/R2Java/blob/master/MLJava/src/main/java/RandomForest/MNISTClassifier.java) class with maven:
```bash
	mvn exec:java -Dexec.mainClass="RandomForest.MNISTClassifier" -Dexec.args="-r"
```
## 

## Model as a Service

To get started with using a trained machine learning model from R in Java, you can follow this workflow:

### R part

1. Decide which model you want to use and train it. The trained model should be generated with input data using the formula interface e.g. ` randomForest( Y ~ . , d.train) ` instead of `randomForest(x = d.train[, -785], y = d.train[, 785])` .

> ### @icon-info-circle *pmml* function in R (version 1.5.2)
>In general, the *pmml* function expects a R object and so will not work on a list of such objects. One error occur from the fact that in the present release, the pmml function expects the randomForest object to be made using a formula....not using input matrices. Therefore, one would have to access the input data using the formula interface e.g. `V1 + V2 ~ . ` .

2. For making a trained R model available by using the approach "Model as a Service" the next step is to save the model as a *.pmml file* such that one can load it in Java easily: First the R packages ["pmml"](https://cran.r-project.org/web/packages/pmml/pmml.pdf) (version >= 1.5.2) and ["XML"](https://cran.r-project.org/web/packages/XML/XML.pdf) (version >= 3.98-1.9) should be installed. After that one uses the following code which convert the model to pmml and save it as *"model.pmml"*.

```{r}
# load xml and pmml library
library(XML)
library(pmml)

# convert model to pmml
model.pmml <- pmml(model, name = "MNIST Random Forest", data = model)

# save to file "model.pmml"
saveXML(model.pmml, file = "/path_where_it_should_be_saved/model.pmml"))
```

Now that we saved the R-model as *.pmml file*, we can start using the saved *model.pmml* for predictions in Java.

### Java part
3. You can use the saved *.pmml file* in Java by running the [MNISTClassifier](https://github.com/IndustrialML/R2Java/blob/master/MLJava/src/main/java/RandomForest/MNISTClassifier.java) class. For the full documentation on the `MNISTClassifier` please refer to the [Python2Java](https://github.com/IndustrialML/Python2Java/tree/master/MaschineLearning4J/src/main/java/RandomForest) repository. For now I will try to make you familiar with its usage. We are using the `mvn exec:java` command to run the program. For `Maven` to know which method to run, we need to specify a `mainClass` and pass this argument: `-Dexec.mainClass="RandomForest.MNISTClassifier"`
4. You can and will need to pass additional program parameters to the class by adding the `-Dexec.args="..."` argument to the `mvn exec:java` command. You can pass in following arguments to the program:
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
                       value will be R2Java/MLJava/MNIST-7.png
 -r,--use-R            pass, if the pmml you want to load is from a model,
                       created with R
```
**Notes**:
* *Always*! use the `-r` parameter
* Never use the `-c` parameter!
* Make sure to pass in a valid value to the `-n` parameter. Do not pass it in if you want to use the default of 50 trees
* If you use the `-p` parameter, you need to pass in an absolute path to a .png file

Basic usage example:
```bash
	mvn exec:java -Dexec.mainClass="RandomForest.MNISTClassifier" -Dexec.args="-r"
```

Advanced usage example:
```bash
	mvn exec:java -Dexec.mainClass="RandomForest.MNISTClassifier" -Dexec.args="-r -p C:/Users/lema/Pictures/Mnist-test.png -n 500 -b"
```

When running the advanced example, the output might look confusing at first and might include a lot of superfluous information and warnings, but if it says: `[INFO] Build SUCCESS` in the end, everything worked as intended. By scrolling up you will find the output of the actual program:
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
