# REGEL CLI
The task was to make a CLI tool in Java (used here), Kotlin or Scala to run REGEL in an interactive (Customized) mode.  
It should also use on of the pretrained models (Stack Overflow used here) and read from the STDIN and print to STDOUT.  

Source Used: [Multi-modal Synthesis of Regular Expressions](https://arxiv.org/abs/1908.03316) (GitHub: [utopia-group/regel](https://github.com/utopia-group/regel/))

## Requirements
- Set up [Sempre](https://github.com/percyliang/sempre) tool:
```bash
cd sempre
./pull-dependencies core
./pull-dependencies corenlp
./pull-dependencies freebase
./pull-dependencies tables

```

- Other dependencies:
	- [Z3](https://github.com/Z3Prover/z3) with Java binding
	- ant to compile java files
	- java 1.8.0
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
Running this program several times it sometimes happened that I found bugs while executing (files not being created etc.).  
Due to further trying I found out that those bugs mostly appeared when running this Single-Threaded.  
Because of the given task this should be run Single-Threaded, else I would prefer to run this application on more than one thread.
