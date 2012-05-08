# Makefile du projet de BD6
# David Galichet & Baptiste Fontaine
# v 0.1

CLASSPATH=".:./postgresql.jdbc4.jar"

default: all

# compiler tout les .java
all: ConnexionBDD.class fenetre_co.class

# efface les fichiers *~
clean:
	rm *~

# efface les fichiers *~ et *.class
cleanall: clean
	rm *.class

# pour chaque *.class, il faut compiler le *.java correspondant
%.class: %.java
	javac -classpath ${CLASSPATH} $<
