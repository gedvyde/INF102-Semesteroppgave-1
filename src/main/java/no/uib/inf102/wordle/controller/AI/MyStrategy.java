package no.uib.inf102.wordle.controller.AI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import no.uib.inf102.wordle.model.Dictionary;
import no.uib.inf102.wordle.model.word.AnswerType;
import no.uib.inf102.wordle.model.word.WordleWord;
import no.uib.inf102.wordle.model.word.WordleWordList;

public class MyStrategy implements IStrategy {

    private Dictionary dictionary;
    private WordleWordList guesses;
    private WordleWordList nonEliminatedGuesses;
    private HashSet<Character> guessedChar;
    private int size; // var for strategy testing

    public MyStrategy(Dictionary dictionary) {
        this.dictionary = dictionary;
        // To select size value, I used MyStrategyTest to test every constant in intervall 3-200.
        // 165 : best avg  /m seed 3.530
        // 5   : best max guess && games won 
        // 39  : mid avg & mid max guess
        this.size = 5 ;
        reset();
    }

    // Package private constructor for testing
    MyStrategy(Dictionary dictionary, int size) {
        this.dictionary = dictionary;
        this.size = size;
        reset();
    }

    @Override
    public String makeGuess(WordleWord feedback) {
        if (feedback != null) {
            guesses.eliminateWords(feedback);
            updateGuessedLetterHistory(feedback);
        }
        List<String> possibleWords = guesses.possibleAnswers();
        List<HashMap<Character, Integer>> frequencyMap;
        // Strategi 1:
        if (possibleWords.size() > size ) {
        frequencyMap = guesses.getListOfFrequencyMaps(true);
        return getBestWord(nonEliminatedGuesses.possibleAnswers(), frequencyMap, false);
        } 
        // Strategi 2:
        frequencyMap = guesses.getListOfFrequencyMaps(false);
        return getBestWord(possibleWords, frequencyMap, true);
    }

    @Override
    public void reset() {
        this.guesses = new WordleWordList(dictionary);
        this.guessedChar = new HashSet<>();
        this.nonEliminatedGuesses = new WordleWordList(dictionary);
    }

    /**
     * Selects the "best" word from a list of words based on character frequency maps and optional use of guessed characters.
     * The method scores each word by summing the frequencies of its unique letters at each position, according to the frequency maps.
     * If a word has more than 4 unique letters and its score is the highest, it is considered optimal.
     * The function returns the highest-scoring word, prioritizing those with more than 4 unique letters when available.
     *
     * @param words the list of candidate words to evaluate.
     * @param frequencyMap a list of frequency maps, where each map contains character frequencies for a specific position in the words.
     * @param useGuessedChar if true, allows already guessed characters to be included in the score; 
     *                       if false, excludes guessed characters from consideration.
     * @return the best word based on character frequencies and uniqueness, with preference for words with more than 4 unique letters.
     */
    private String getBestWord(List<String> words, List<HashMap<Character, Integer>> frequencyMap, boolean useGuessedChar) {
        String bestWord    = new String(); 
        String optimalWord = new String();
        int bestScore    = 0;
        int optimalScore = 0;
        boolean isBetter;

        for (String word : words) {
            int wordScore = 0;
            HashSet<Character> wordLetters = new HashSet<>();

            for (int i = 0; i < word.length(); i++)  {
                char letter = word.charAt(i); 
                if (wordLetters.add(letter) && (!guessedChar.contains(letter) || useGuessedChar)) {
                    wordScore += frequencyMap.get(i).getOrDefault(letter,0);
                }
            } 
          
            isBetter = wordScore > bestScore;
            bestWord = isBetter ? word : bestWord;
            bestScore = isBetter ? wordScore : bestScore;
      
            isBetter = wordScore > optimalScore;
            optimalScore = isBetter ? wordScore : optimalScore;
            optimalWord = (isBetter && wordLetters.size() == 5) ?  word : optimalWord;
        } 
        return (!optimalWord.isEmpty()) ? optimalWord : bestWord;
    }

    /**
     * Updates the history of guessed letters based on the feedback from a Wordle guess.
     * The method adds characters to the guessed letters set if their {@code AnswerType} 
     * is either {@code CORRECT} or {@code WRONG} based on the feedback provided.
     *
     * @param feedback the {@code WordleWord} feedback
     */
    private void updateGuessedLetterHistory(WordleWord feedback) {
        for (Character cha : feedback.mapIndexOfAnswerType(AnswerType.CORRECT).keySet()){ guessedChar.add(cha);}
        for (Character cha : feedback.mapIndexOfAnswerType(AnswerType.WRONG  ).keySet()){ guessedChar.add(cha);}
    }
}