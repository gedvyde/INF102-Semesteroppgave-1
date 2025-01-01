package no.uib.inf102.wordle.controller.AI;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import no.uib.inf102.wordle.model.Dictionary;

public class MyStrategyTest {

  private Dictionary dictionary = new Dictionary();

  @Test
  void testBestSizePossibleWordsBeforeStrategySwitch() {

    double bestAvg = 5.53;
    List<Integer> sizeBestAvg  = new ArrayList<>();

    double bestMaxGuess = 6;
    List<Integer> sizeBestMaxGuess  = new ArrayList<>();

    double ultimateBestAverage = 5.55;
    double ultimateBestMaxGuess = 6;
    List<Integer> ultimateSize  = new ArrayList<>();
    

    // Adjust intervals for more detailed test 
    int intervals = 50; 

    for (int i = 3; i < 200 ; i += intervals) {
      AIStatistics strategyRun = AIPerformance.runWordleGames(new MyStrategy(dictionary,i));
      double average = strategyRun.getAverage();
      double maxGuess = strategyRun.getMaxGuesses();
      
      if (average <= bestAvg) {
        bestAvg = average;
        sizeBestAvg.add(i);
      }

      if (maxGuess <= bestMaxGuess) {
        bestMaxGuess = maxGuess;
        sizeBestMaxGuess.add(i);
      }

      if ((average <= ultimateBestAverage) && (maxGuess == 6)) {
        ultimateBestAverage = average;
        ultimateBestMaxGuess = maxGuess;
        ultimateSize.add(i);
      }
    }

    System.out.println("The best size " + sizeBestAvg +" for possible words before strategy switch, based on average: " + bestAvg);
    System.out.println("The best size " + sizeBestMaxGuess +  " based on max guesses: " + bestMaxGuess);
    System.out.println("The ultimate best size: " + ultimateSize + " based on average " + ultimateBestAverage + " and on best max guess " + ultimateBestMaxGuess);

  }
}



