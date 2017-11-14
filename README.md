# R2Java - R-Model as a Service
This project was set up by Matthias Leopold and Veronica Pohl at Zühlke Engineering AG Schlieren, to gather the option to use a trained R model for production. We looked at the approach **"Model as a Service"**. This means that the whole machine learning model is supposed to be transferred from R to Java, to execute predictions directly in Java.

## Project structure
The project is split into two sub-projects: 
1. [MLR (R part)](https://github.com/IndustrialML/R2Java/MLR)
2. //todo [MLJava (Java part)](https://github.com/IndustrialML/R2Java/MLJava)


### Requirenments
* Installed [R](https://cran.r-project.org/) and integrated development environment (IDE) for R like [RStudio](https://www.rstudio.com/).
* //todo Installed Java edited

## Getting started 

//todo Assuming you have cloned this repository and installed [R](https://cran.r-project.org/), an integrated development environment (IDE) for R like [RStudio](https://www.rstudio.com/) and Java already. 

1. Set your working directory to MLR e.g. `setwd("~/R2Java/MLR")` in R.
2. Run the *train_randomForestonMNIST_pmml.R file* by using the command:
```{r}
source('train_randomForestonMNIST_pmml.R')
```

This will load the MNIST data set where one image has 28*28 = 784 pixels with values between 0 and 255. The set is organized row-wise. After that the model is trained by using random forests and save it as *.pmml file*.

3. //todo
## 

## Model as a Service

To get started with using a trained machine learning model from R in Java, you can follow this workflow:

In R:

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

3. //todo
4. //todo