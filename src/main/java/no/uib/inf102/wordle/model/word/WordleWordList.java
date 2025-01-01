package no.uib.inf102.wordle.model.word;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import no.uib.inf102.wordle.model.Dictionary;
//import no.uib.inf102.wordle.model.word.WordleWord;

/**
 * This class describes a structure of two lists for a game of Wordle: The list
 * of words that can be used as guesses and the list of words that can be
 * possible answers.
 */
public class WordleWordList {

	/**
	 * All words in the game. These words can be used as guesses.
	 */
	private Dictionary allWords;

	/**
	 * A subset of <code>allWords</code>. <br>
	 * </br>
	 * These words can be the answer to a wordle game.
	 */
	private List<String> possibleAnswers;

	/**
	 * Create a WordleWordList that uses the full words and limited answers of the
	 * GetWords class.
	 */
	public WordleWordList(Dictionary dictionary) {
		this.allWords = dictionary;
		this.possibleAnswers = new ArrayList<>(dictionary.getAnswerWordsList());
	}

	/**
	 * Get the list of all guessing words.
	 * 
	 * @return all words
	 */
	public Dictionary getAllWords() {
		return allWords;
	}

	/**
	 * Returns the list of possible answers.
	 * 
	 * @return
	 */
	public List<String> possibleAnswers() {
		return Collections.unmodifiableList(possibleAnswers);
	}

	/**
	 * Eliminates words from the possible answers list using the given
	 * <code>feedback</code>
	 * 
	 * @param feedback
	 */
	public void eliminateWords(WordleWord feedback) {
		List<String> newPossibleAnswers = new ArrayList<>();
		
		for (String word : possibleAnswers) {
			if (WordleWord.isPossibleWord(word, feedback)) {
				newPossibleAnswers.add(word);
			}
		}
		this.possibleAnswers = newPossibleAnswers;

	}

	/**
	 * Returns the amount of possible answers in this WordleWordList
	 * 
	 * @return size of
	 */
	public int size() {
		return possibleAnswers.size();
	}

	/**
	 * Removes the given <code>answer</code> from the list of possible answers.
	 * 
	 * @param answer
	 */
	public void remove(String answer) {
		possibleAnswers.remove(answer);
	}

	/**
	 * Returns the word length in the list of valid guesses.
	 * 
	 * @return
	 */
	public int wordLength() {
		return allWords.WORD_LENGTH;
	}

	/**
	 * Generates a list of frequency maps for characters in the possible answers.
	 * Each map contains character frequencies either across all positions or 
	 * at a specific index in the words, depending on the {@code byIndex} parameter.
	 *
	 * @param byIndex if true, computes frequency maps per index (i.e., position-wise);
	 *                otherwise, computes frequency maps for the entire set of possible answers
	 *                without distinguishing by index.
	 * @return a {@code List<HashMap<Character, Integer>>} where each map represents 
	 *         the frequency of characters at a specific position in the possible answers 
	 *         (when {@code byIndex} is true), or for the whole word set (when {@code byIndex} is false).
	 */
	public List<HashMap<Character,Integer>> getListOfFrequencyMaps(boolean countByIndex) {
		List<HashMap<Character, Integer>> frequencyMaps = new ArrayList<>(5);
		for (int i = 0; i < wordLength(); i++) {
				frequencyMaps.add(new HashMap<>());
		}

		for (String word : this.possibleAnswers()) {
			for (int i = 0; i < word.length(); i++)  {
					char letter = word.charAt(i);  
					int mapIndex = countByIndex ? i : 0;
					frequencyMaps.get(mapIndex).put(letter, frequencyMaps.get(mapIndex).getOrDefault(letter,0)+1);
					} 
			}
		
		if (!countByIndex) {
			HashMap<Character,Integer> frequencyMap = frequencyMaps.get(0);
			frequencyMaps = new ArrayList<>();
			for (int i = 0; i < wordLength(); i++) {
				frequencyMaps.add(frequencyMap);
			}
		}
		return frequencyMaps;
	} 
}
