# R - Model as a Service

## Predictive Model Markup Language (pmml)

### Requirenments
* Installed [R](https://cran.r-project.org/) and integrated development environment (IDE) for R like [RStudio](https://www.rstudio.com/).
* You have some data and a trained model. The trained model should be generated with input data using the formula interface e.g. ` randomForest( Y ~ . , d.train) ` instead of `randomForest(x = d.train[, -785], y = d.train[, 785])` . 

### Code in R

By making a trained R model available as inference as a service the goal is to save the model as a *.pmml file* such that one can load it in Java easily.

First the R packages ["pmml"](https://cran.r-project.org/web/packages/pmml/pmml.pdf) and ["XML"](https://cran.r-project.org/web/packages/XML/XML.pdf) should be installed. After that one uses the following code which convert the model to pmml and save it as *"model.pmml"*.

```{r}
# load xml and pmml library
library(XML)
library(pmml)

# convert model to pmml
model.pmml <- pmml(model, name = "MNIST Random Forest", data = model)

# save to file "model.pmml"
saveXML(model.pmml, file = "/path_where_it_should_be_saved/model.pmml"))
```