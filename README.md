# HassSyn
**HassSyn** is a Home Assistant(**Hass**) Automation **Syn**thesizer. This synthesizer takes specifications from a given history of the user's Hass platform and generates an automation rule in forms of YAML that consistent with the user's data.

## Author Infomation
Name: Jaeho Choi   
Contact: zero0000@kaist.ac.kr

--------------------------------------------------------------------------------

## Installation Guide

HassSyn is developed in Scala. Before installation, you need to install [JDK 8+](https://www.oracle.com/java/technologies/downloads/) and [sbt](https://www.scala-sbt.org/) (build tool for Scala).

### Download HassSyn
```bash
$ git clone https://github.com/workstudy-zero/hasssyn.git hasssyn
```

### Installation of HassSyn using `sbt`

```bash
$ cd hasssyn && sbt compile
```

--------------------------------------------------------------------------------

## Run Commands

To run the synthesizer, start sbt shell.
```bash
$ sbt
```
In sbt shell, you can run the synthesizer with the following command:
```bash
$sbt:hasssyn> run <history-file-name> <output-file-name> <action-name> <error-bound> 
# For example, run test/history3 example2 turnOn 150
```
- `history-file-name` is a name of JSON file containing user data that are inputs of HassSyn. This history file can be obtained from Hass using [REST API](https://developers.home-assistant.io/docs/api/rest/)
- `output-file-name` is a name of YAML file where the automation rule that is output of HassSyn will be dumped.
- `action-name` is a name of target action. (Currently, only `turnOff` and `turnOn` are supported.)
- `error-bound` is an integer (in seconds) that bounds error in the synthesis process.

--------------------------------------------------------------------------------

## Test

I uploaded 3 test cases in `test` directory.
For each history file (`.json`), HassSyn synthesize automation rule (`.yaml`) well with `error-bound = 150`.
