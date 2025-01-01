package no.uib.inf102.wordle.model.word;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import no.uib.inf102.wordle.model.Dictionary;

/**
 * This class represents an answer to a Wordle puzzle.
 * 
 * The answer must be one of the words in the LEGAL_WORDLE_LIST.
 */
public class WordleAnswer {

    private final String WORD;

    private Dictionary dictionary;

    private static Random random = new Random();

    /**
     * Creates a WordleAnswer object with a random word from the answer word list
     */
    public WordleAnswer(Dictionary dictionary) {
        this(random, dictionary);
    }

    /**
     * Creates a WordleAnswer object with a random word from the answer word list
     * using a specified random object.
     * This gives us the opportunity to set a seed so that tests are repeatable.
     */
    public WordleAnswer(Random random, Dictionary dictionary) {
        this(getRandomWordleAnswer(random, dictionary), dictionary);
    }

    /**
     * Creates a WordleAnswer object with a given word.
     * 
     * @param answer
     */
    public WordleAnswer(String answer, Dictionary dictionary) {
        this.WORD = answer.toLowerCase();
        this.dictionary = dictionary;
    }

    /**
     * Gets a random wordle answer
     * 
     * @param random
     * @return
     */
    private static String getRandomWordleAnswer(Random random, Dictionary dictionary) {
        List<String> possibleAnswerWords = dictionary.getAnswerWordsList();
        int randomIndex = random.nextInt(possibleAnswerWords.size());
        String newWord = possibleAnswerWords.get(randomIndex);
        return newWord;
    }

    /**
     * Guess the Wordle answer. Checks each character of the word guess and gives
     * feedback on which that is in correct position, wrong position and which is
     * not in the answer word.
     * This is done by updating the AnswerType of each WordleCharacter of the
     * WordleWord.
     * 
     * @param wordGuess
     * @return wordleWord with updated answertype for each character.
     */
    public WordleWord makeGuess(String wordGuess) {
        if (!dictionary.isLegalGuess(wordGuess))
            throw new IllegalArgumentException("The word '" + wordGuess + "' is not a legal guess");

        WordleWord guessFeedback = matchWord(wordGuess, WORD);
        return guessFeedback;
    }

    /**
     * Generates a WordleWord showing the match between <code>guess</code> and
     * <code>answer</code>
     * 
     * @param guess
     * @param answer
     * @return
     */
    public static WordleWord matchWord(String guess, String answer) {
        int wordLength = answer.length();
        if (guess.length() != wordLength)
            throw new IllegalArgumentException("Guess and answer must have same number of letters but guess = " + guess
                    + " and answer = " + answer);

        HashMap<Character,Integer> letters = makeMap(answer);
        AnswerType[] feedback = new AnswerType[wordLength];

        for (int i = 0; i < wordLength; i++) {
            char correct = answer.charAt(i);
            char guessed = guess.charAt(i);

            if (correct == guessed) {
                feedback[i] = AnswerType.CORRECT;
                letters.put(guessed, letters.get(guessed)-1);        
            } else {
                feedback[i] = AnswerType.WRONG;
            }
        }

        for (int i = 0; i < wordLength; i++) {
            char guessed = guess.charAt(i);
            if (feedback[i] == AnswerType.CORRECT) {
                continue;
            }
            if (letters.containsKey(guessed)) {
                Integer count = letters.get(guessed);
                if (count > 0) {
                    letters.put(guessed, count-1);
                    feedback[i] = AnswerType.MISPLACED;
                }
            }
        }

        return new WordleWord(guess,feedback);
    }

    /**
     * Creates a frequency map for the characters in a given string.
     * The map contains each unique character from the string as a key, 
     * and the value is the number of times the character appears in the string.
     *
     * @param answer the string from which to generate the frequency map.
     * @return a {@code HashMap<Character, Integer>} where the keys are the characters 
     *         from the input string, and the values represent the frequency of each character.
     */
    private static HashMap<Character,Integer> makeMap(String answer) { 
        HashMap<Character,Integer> letters = new HashMap<>();          
        for (Character letter : answer.toCharArray()) {
            if (!letters.containsKey(letter)) {           
                letters.put(letter, 0);                   
            }
            letters.put(letter, letters.get(letter)+1);  
        }
  
    return letters; 
    }

   

    
}
