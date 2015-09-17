# Software Architecture 2015-2016 team FT_3
## Lab: Quality Care Robot

Team members FT_3

### Team members:
* Felix Barten
* Joel Bartholomew
* Bart de Man
* Spiros Tzavaras

## Installation

## Requirements

* Eclipse Java IDE (Tested with Mars version) 
* Java JDK

## Installation

Before starting installation install all the necessary tools from the requirements section.

1. Clone the project with `git clone git@github.com:felixbarten/software-architecture.git` (for SSH) 
2. Run the gradle script (linux assumed) 
```
> cd ./QC_robot/buildscripts
> chmod u+x ./gradlew
> ./gradlew eclipse
> ./gradlew build
```
These commands will create the necessary eclipse project files and build the project. 

3. Import the project into eclipse via the Import wizard. 
4. Choose "import project from Git" 
5. Browse to the repository from where to import
6. Import the QC_robot/buildscripts folder.
7. A project should appear in the import window.
8. Click finish to complete the project import. 

### Building the project

```
> ./gradlew build
```

### Compiling the project

```
> ./gradlew compile
```

### Deploying the project

```
> ./gradlew deploy
```

### Testing the project

```
> ./gradlew test
```


