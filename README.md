Projet BD6
==========

Remplissage de la base de données
---------------------------------

    $ cd scripts
    $ ./createdb.sh # crée la base BD6 avec toutes les tables
    $ ./filldb.sh   # remplie la base avec les lignes de data.csv

Si la base existe déjà, pour la réinitialiser utilisez:

    $ cd scripts
    $ ./dropdb.sh

Ne pas oublier d'installer le paquet `python3-postgresql` pour le remplissage de
la base de données.

Note: Pour aller plus vite, le script `reset.sh` combine `dropdb.sh`,
`createdb.sh` et `filldb.sh`.

Tests
-----

Pour faire des tests, il faut avoir installé le paquet `junit4`, avoir rempli la
base avec le CSV donnée, puis aller dans le répertoire `java` et faire:

    $ make tests

Les tests sont faits en utilisant JUnit. Pour ajouter des tests, il faut
s'inspirer des tests qui existent déjà dans `ConnexionBDD_tests.java`:
    
    @Test
    public void monNouveauTestTropBien() {
        /*  le code du test */
    }

Les principales
[méthodes](http://junit.sourceforge.net/javadoc/org/junit/Assert.html) sont les
suivantes:

* `assertEquals(A, B)` : vérifie que `A` est bien égal à `B` (`A` est ce qu'on
  attend, `B` est ce qu'on obtient).
* `assertTrue(A)` : vérifie que `A` est bien égal à `true`.
* `assertFalse(A)` : vérifie que `A` est bien égal à `false`.
* `assertNull(A)` : vérifie que `A` est bien égal à `null`.
* `assertNotNull(A)` : contraire de `assertNull(A)`.
* `fail(msg)` : fait rater le test, en affichant `msg`.

