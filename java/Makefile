# Makefile du projet de BD6
# David Galichet & Baptiste Fontaine
# v 0.1

CLASSPATH=".:./postgresql.jdbc4.jar"
TESTS_CLASSPATH=".:./postgresql.jdbc4.jar:/usr/share/java/junit4.jar"

default: all

# compiler tout les .java
all: ConnexionBDD.class fenetre_co.class

# efface les fichiers *~
clean:
	rm -f *~

# efface les fichiers *~ et *.class
cleanall: clean
	rm -f *.class

# pour chaque *.class, il faut compiler le *.java correspondant
%.class: %.java
	javac -cp ${CLASSPATH} $<

# tests de requêtes à la BDD
tests: ConnexionBDD_tests.java ConnexionBDD.class
	javac -cp .:/usr/share/java/junit4.jar $<
	java -cp ${TESTS_CLASSPATH} org.junit.runner.JUnitCore ConnexionBDD_tests
