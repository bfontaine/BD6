# Makefile du projet de BD6
# David Galichet & Baptiste Fontaine
# v 0.1

PG_DRIVER_NAME=postgresql.jdbc4.jar
USER=$(shell whoami)

CLASSPATH=.:./${PG_DRIVER_NAME}
TESTS_CLASSPATH=${CLASSPATH}:/usr/share/java/junit4.jar

INTERFACE_DIR=$(shell pwd)/Interface
INTERFACE_CLASSPATH=.:..:../${PG_DRIVER_NAME}

default: all

# compiler tout les .java
all: ConnexionBDD.class java/inter_princ.class

# efface les fichiers *~
clean:
	find . -name *~ -delete
	find . -name *.class -delete

# affiche la fenetre de connexion
fenetretest: interface
	cd ${INTERFACE_DIR};java -cp ${INTERFACE_CLASSPATH} inter_princ bd6 ${USER} ${USER}

# alias
fenetre: interface

# compile l'interface
interface: Interface/inter_princ.class

Interface/inter_princ.class: ${INTERFACE_DIR}/inter_princ.java
	cd ${INTERFACE_DIR};javac -cp ${INTERFACE_CLASSPATH} inter_princ.java

# pour chaque *.class, il faut compiler le *.java correspondant
%.class: %.java
	javac -cp ${CLASSPATH} $<

# tests de requêtes à la BDD
tests: ConnexionBDD_tests.java ConnexionBDD.class
	javac -cp .:/usr/share/java/junit4.jar $<
	java -cp ${TESTS_CLASSPATH} org.junit.runner.JUnitCore ConnexionBDD_tests
