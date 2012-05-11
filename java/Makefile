# Makefile du projet de BD6
# David Galichet & Baptiste Fontaine
# v 0.1

PG_DRIVER_NAME=postgresql.jdbc4.jar
USER=$(shell whoami)

CLASSPATH=.:./${PG_DRIVER_NAME}
TESTS_CLASSPATH=${CLASSPATH}:/usr/share/java/junit4.jar

INTERFACE_DIR=$(shell pwd)/Interface
INTERFACE_CLASSPATH=.:..:../${PG_DRIVER_NAME}

# remplie la bdd
default: reset generate

# reset la BDD
reset: ../scripts/reset.sh
	cd ../scripts;./reset.sh

# efface les fichiers *~
clean:
	rm -f *~ */*~
	rm -f *.class */*.class
	#find . -name *~ -delete
	#find . -name *.class -delete

generate: GenerateCommands.class ConnexionBDD.class
	java -cp ${CLASSPATH} GenerateCommands

# affiche la fenetre de connexion
fenetretest: interface
	cd ${INTERFACE_DIR};java -cp ${INTERFACE_CLASSPATH} inter_princ bd6 ${USER} ${USER}


#
fenetre: Interface/inter_princ.class

# compile l'interface
interface: Interface_texte.class

Interface/inter_princ.class: ${INTERFACE_DIR}/inter_princ.java
	cd ${INTERFACE_DIR};javac -cp ${INTERFACE_CLASSPATH} inter_princ.java

# pour chaque *.class, il faut compiler le *.java correspondant
%.class: %.java
	javac -cp ${CLASSPATH} $<

# tests de requêtes à la BDD
tests: ConnexionBDD_tests.java ConnexionBDD.class
	javac -cp .:/usr/share/java/junit4.jar $<
	java -cp ${TESTS_CLASSPATH} org.junit.runner.JUnitCore ConnexionBDD_tests
