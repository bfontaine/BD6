# Makefile du projet de BD6
# David Galichet & Baptiste Fontaine
# v 0.1

CLASSPATH=".:./postgresql.jdbc4.jar"
TESTS_CLASSPATH="${CLASSPATH}:/usr/share/java/junit4.jar"
INTERFACE_CLASSPATH="${CLASSPATH}:java"

default: all

# compiler tout les .java
all: ConnexionBDD.class java/inter_princ.class

# efface les fichiers *~
clean:
	rm -f *~

# efface les fichiers *~ et *.class
cleanall: clean
	rm -f *.class

# affiche la fenetre de connexion
fenetre: java/inter_princ.class
	java -cp ${INTERFACE_CLASSPATH} $<

# compile l'interface
java/inter_princ.class: java/inter_princ.java
	javac -cp ${INTERFACE_CLASSPATH} $<

# pour chaque *.class, il faut compiler le *.java correspondant
%.class: %.java
	javac -cp ${CLASSPATH} $<

# tests de requêtes à la BDD
tests: ConnexionBDD_tests.java ConnexionBDD.class
	javac -cp .:/usr/share/java/junit4.jar $<
	java -cp ${TESTS_CLASSPATH} org.junit.runner.JUnitCore ConnexionBDD_tests
