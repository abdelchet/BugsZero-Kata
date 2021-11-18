package com.adaptionsoft.games.uglytrivia;

import java.util.ArrayList;
import java.util.LinkedList;


/* il y a beaucoup d'amelioration à faire en terme de folder , parceque un dossier java ca vieux rien dire je parle du 1er dossier java
* les image doivement etre dans un dossier ressources pour respecter les normes java
* poour le gameTest je ne vois pas l'utilité d'ajouter une lib externe sachant qu'on a deja un pom maven ,
*  et je ne vois pas non plus sont utilité dans le code un simple test Junit suffit pour verifier le resultat
*
* */
public class Game {

	//  c'est mieux de specifier le type
	// vaut mieux plutot utiliser un Set à la place de List qui peut accepter les doublons
    ArrayList players = new ArrayList();
    int[] places = new int[6];
    int[] purses  = new int[6];
    boolean[] inPenaltyBox  = new boolean[6];
    
    LinkedList popQuestions = new LinkedList();
    LinkedList scienceQuestions = new LinkedList();
    LinkedList sportsQuestions = new LinkedList();
    LinkedList rockQuestions = new LinkedList();
    
    int currentPlayer = 0;
    boolean isGettingOutOfPenaltyBox;
    
    public  Game(){
    	for (int i = 0; i < 50; i++) {
			popQuestions.addLast("Pop Question " + i);
			scienceQuestions.addLast(("Science Question " + i));
			sportsQuestions.addLast(("Sports Question " + i));
			rockQuestions.addLast(createRockQuestion(i));
    	}
    }

	public String createRockQuestion(int index){
		return "Rock Question " + index;
	}

	public boolean isPlayable() {
		return (howManyPlayers() >= 2);
	}


	// il n'y a pas de controle dans les cas ou on essaye d'ajouter des joueuers de meme nom
	// et il n'y a pas de controle si on essaye d'ajouter un joureur sans nom !!
	// playerName peut etre null egalement
	public boolean add(String playerName) {

	    players.add(playerName);
	    places[howManyPlayers()] = 0;
	    purses[howManyPlayers()] = 0;
	    inPenaltyBox[howManyPlayers()] = false;
	    
	    System.out.println(playerName + " was added");
	    System.out.println("They are player number " + players.size());
		return true;
	}
	
	public int howManyPlayers() {
		return players.size();
	}
	// les logs ne sont pas bien formatter et il risque d'y avoir des exceptions dans les logs ainsi qu'il faut jamais
	// mettre des sysout dans le code vaut mieux utiliser les loggers
	// il n'y a pas de couverture de test pour cette fonction
	// aucun exception levée si un joueur fait une fausse manipulation
	// il faut plutot utiliser le equals pour comparer les Strings
	// vaut mieux utiliser des constantes au lieu de dupliquer le code
	// pas de NPE check
	public void roll(int roll) {
		// pas de NullPointerException check !!!!!!!!
		System.out.println(players.get(currentPlayer) + " is the current player");
		System.out.println("They have rolled a " + roll);
		
		if (inPenaltyBox[currentPlayer]) {
			if (roll % 2 != 0) {
				isGettingOutOfPenaltyBox = true;
				
				System.out.println(players.get(currentPlayer) + " is getting out of the penalty box");
				movePlayerAndAskQuestion(roll);
			} else {

				System.out.println(players.get(currentPlayer) + " is not getting out of the penalty box");
				isGettingOutOfPenaltyBox = false;
				}
			
		} else {

			movePlayerAndAskQuestion(roll);
		}
		
	}

	// les logs ne sont pas bien formatter et il risque d'y avoir des exceptions dans les logs ainsi qu'il faut jamais
	// mettre des sysout dans le code vaut mieux utiliser les loggers
	// il n'y a pas de couverture de test pour cette fonction
	// aucun exception levée si un joueur fait une fausse manipulation
	private void movePlayerAndAskQuestion(int roll) {
		places[currentPlayer] = places[currentPlayer] + roll;
		if (places[currentPlayer] > 11) places[currentPlayer] = places[currentPlayer] - 12;

		System.out.println(players.get(currentPlayer)
                + "'s new location is "
                + places[currentPlayer]);
		System.out.println("The category is " + currentCategory());
		askQuestion();
	}

	// les logs ne sont pas bien formatter et il risque d'y avoir des exceptions dans les logs ainsi qu'il faut jamais
	// mettre des sysout dans le code vaut mieux utiliser les loggers
	// il n'y a pas de couverture de test pour cette fonction
	// aucun exception levée si un joueur fait une fausse manipulation
	// il faut plutot utiliser le equals pour comparer les Strings
	// vaut mieux utiliser des constantes static au lieu de dupliquer le code je donne reference à ("Pop ,  Science , Sports , Rock" ...)
	private void askQuestion() {
		if (currentCategory() == "Pop")
			System.out.println(popQuestions.removeFirst());
		if (currentCategory() == "Science")
			System.out.println(scienceQuestions.removeFirst());
		if (currentCategory() == "Sports")
			System.out.println(sportsQuestions.removeFirst());
		if (currentCategory() == "Rock")
			System.out.println(rockQuestions.removeFirst());		
	}

	// il y a plusieurs if dans la methode vaut mieux les regrouper par retours
	// vaut mieux utiliser des constantes static au lieu de dupliquer le code je donne reference à ("Pop ,  Science , Sports , Rock" ...)
	private String currentCategory() {
		if (places[currentPlayer] == 0) return "Pop";
		if (places[currentPlayer] == 4) return "Pop";
		if (places[currentPlayer] == 8) return "Pop";
		if (places[currentPlayer] == 1) return "Science";
		if (places[currentPlayer] == 5) return "Science";
		if (places[currentPlayer] == 9) return "Science";
		if (places[currentPlayer] == 2) return "Sports";
		if (places[currentPlayer] == 6) return "Sports";
		if (places[currentPlayer] == 10) return "Sports";
		return "Rock";
	}
	// les logs ne sont pas bien formatter et il risque d'y avoir des exceptions dans les logs ainsi qu'il faut jamais
	// mettre des sysout dans le code vaut mieux utiliser les loggers
	// il n'y a pas de couverture de test pour cette fonction
	// aucun exception levée si un joueur fait une fausse manipulation
	// il faut plutot utiliser le equals pour comparer les Strings
	// vaut mieux utiliser des constantes au lieu de dupliquer le code
	// pas de NPE check
	public boolean wasCorrectlyAnswered() {
		if (inPenaltyBox[currentPlayer]){
			if (isGettingOutOfPenaltyBox) {
				System.out.println("Answer was correct!!!!");
				currentPlayer++;
				if (currentPlayer == players.size()) currentPlayer = 0;
				purses[currentPlayer]++;
				System.out.println(players.get(currentPlayer)
						+ " now has "
						+ purses[currentPlayer]
						+ " Gold Coins.");
				// on peut faire directement un retour sans declaré une variable temporaire ( winner )
				boolean winner = didPlayerWin();

				return winner;
			} else {
				currentPlayer++;
				// pas lisbile vaut mieux ajouter des accolades
				// repetition de ce code en bas donc il peut etre externaliser
				if (currentPlayer == players.size()) currentPlayer = 0;
				return true;
			}
			
			
			
		} else {
		
			System.out.println("Answer was corrent!!!!");
			purses[currentPlayer]++;
			System.out.println(players.get(currentPlayer) 
					+ " now has "
					+ purses[currentPlayer]
					+ " Gold Coins.");
			
			boolean winner = didPlayerWin();
			currentPlayer++;
			if (currentPlayer == players.size()) currentPlayer = 0;
			
			return winner;
		}
	}
	// les logs ne sont pas bien formatter et il risque d'y avoir des exceptions dans les logs ainsi qu'il faut jamais
	// mettre des sysout dans le code vaut mieux utiliser les loggers
	// il n'y a pas de couverture de test pour cette fonction
	// aucun exception levée si un joueur fait une fausse manipulation
	// NPE check
	public boolean wrongAnswer(){
		System.out.println("Question was incorrectly answered");
		System.out.println(players.get(currentPlayer)+ " was sent to the penalty box");
		inPenaltyBox[currentPlayer] = true;
		
		currentPlayer++;
		if (currentPlayer == players.size()) currentPlayer = 0;
		return true;
	}


	//pour ameliorer la lisibilté vaut mieux mettre l'operateur !=
	// il n'y a pas de couverture de test pour cette fonction
	private boolean didPlayerWin() {
		return !(purses[currentPlayer] == 6);
	}
}
