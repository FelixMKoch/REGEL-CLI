# REGEL CLI
The task was to make a CLI tool in Java (used here), Kotlin, or Scala to run REGEL in an interactive (Customized) mode.  
It should also use one of the pretrained models (Stack Overflow used here), read from the STDIN, and print to STDOUT.  

Source used: [Multi-modal Synthesis of Regular Expressions](https://arxiv.org/abs/1908.03316) (GitHub: [utopia-group/regel](https://github.com/utopia-group/regel/))

## Requirements
- Set up [Sempre](https://github.com/percyliang/sempre) tool:
```bash
cd sempre
./pull-dependencies core
./pull-dependencies corenlp
./pull-dependencies freebase
./pull-dependencies tables

```
- Sempre tool setup can also be executed over a Bash-Script for Linux users:  
```bash
chmod +x dependencies.sh 
./startup.sh
```

- Other dependencies:
	- [Z3](https://github.com/Z3Prover/z3) with Java binding
	- ant to compile Java files
	- Java 1.8.0
	- [Gradle](https://gradle.org/) if you want to build with that

## How to Run REGEL on CLI

- Gradle:
```bash
./gradlew run
```

- IntelliJ IDEA:
```bash
git clone https://github.com/FelixMKoch/REGEL-CLI.git
```


## Known Problems / Bugs
- Running this program several times it sometimes happened that I found bugs while executing (files not being created etc.).  
Due to further trying I found out that those bugs mostly appeared when running this single-threaded.  
Because of the given task, this should be run single-threaded, else I would prefer to run this application on more than one thread.
- If there is not any output appearing it might be the case that you did not download Z3, or you are using an older version of Apache Ant (because starting the program also starts an ant clear + build every time).


## Comments
I tested this application on both Windows and Linux to find out, that Linux works better.  
First of all, there appear to be complications on Windows with downloading the dependencies as well as Z3 (by using 'nmake' instead of 'make' on the Makefile).  
This is why this program might not work on a Windows OS. Some bugs came up with deprecated Gradle versions if you build with that, so keep an eye on that (I used Gradle 7.0, bugs appeared with versions 6.7 and below).

  
