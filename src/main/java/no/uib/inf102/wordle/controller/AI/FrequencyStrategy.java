package no.uib.inf102.wordle.controller.AI;

import java.util.HashMap;
import java.util.List;

import no.uib.inf102.wordle.model.Dictionary;
import no.uib.inf102.wordle.model.word.WordleWord;
import no.uib.inf102.wordle.model.word.WordleWordList;

/**
 * This strategy finds the word within the possible words which has the highest
 * expected
 * number of green matches.
 */
public class FrequencyStrategy implements IStrategy {

    private Dictionary dictionary;
    private WordleWordList guesses;
    private List<HashMap<Character, Integer>> frequencyMapIndex;

    public FrequencyStrategy(Dictionary dictionary) {
        this.dictionary = dictionary;
        reset();
    }

    @Override
    public String makeGuess(WordleWord feedback) {
        if (feedback != null) {
            guesses.eliminateWords(feedback);
        }
        frequencyMapIndex = guesses.getListOfFrequencyMaps(true);
        String bestWord = new String();
        int bestScore = 0;

        for (String word : guesses.possibleAnswers()) {
            int wordScore = 0;
            for (int i = 0; i < word.length(); i++)  {
                char letter = word.charAt(i); 
                wordScore += frequencyMapIndex.get(i).get(letter);
            }
            if (wordScore > bestScore) {
                bestWord = word;
                bestScore = wordScore;
            }
        }
        return bestWord;
    }

    @Override
    public void reset() {
        guesses = new WordleWordList(dictionary);
        frequencyMapIndex = guesses.getListOfFrequencyMaps(true);
    }
}