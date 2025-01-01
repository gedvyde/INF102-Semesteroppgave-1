package no.uib.inf102.wordle.model.word;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * This class represents a word given to the Wordle board. It is one row of
 * letters combined with the colors of the squares.
 * A Wordle Word is an iterable of WordleCharacter.
 */
public class WordleWord implements Iterable<WordleCharacter> {

    private List<WordleCharacter> word;

    /**
     * Creates a WordleWord by matching the characters in the given string to the
     * given AnswerType array.
     * 
     * @param word
     * @param feedback
     */
    public WordleWord(String word, AnswerType[] feedback) {
        if (word.length() != feedback.length)
            throw new IllegalArgumentException("word and feedback must have same length");
        for (AnswerType type : feedback) {
            if (type == AnswerType.BLANK) {
                throw new IllegalArgumentException("Feedback can not contain BLANK");
            }
            if (type == null) {
                throw new IllegalArgumentException("Feedback can not contain null");
            }
        }

        this.word = new ArrayList<>();
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);
            WordleCharacter wordleChar = new WordleCharacter(c, feedback[i]);
            this.word.add(wordleChar);
        }
    }

    /**
     * Checks if all WordleCharacters have the answer type CORRECT
     * 
     * @return true if all corret, false if not
     */
    public boolean allMatch() {
        for (WordleCharacter wordleChar : this) {
            if (wordleChar.answerType != AnswerType.CORRECT)
                return false;
        }
        return true;
    }

    /**
     * Checks if <code>c</code> is in the WordelWord
     * 
     * @param c
     * @return true if the word contains the character, false if not
     */
    public boolean contains(Character c) {
        for (WordleCharacter wordleChar : this) {
            if (wordleChar.letter == c) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the WordleWord as a String without AnswerType.
     * 
     * @return
     */
    public String getWordString() {
        StringBuilder sb = new StringBuilder();
        for (WordleCharacter wordleChar : word) {
            sb.append(wordleChar.letter);
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (WordleCharacter wordleChar : word) {
            sb.append("(");
            sb.append(wordleChar.letter);
            sb.append(", ");
            sb.append(wordleChar.answerType);
            sb.append(") ");
        }
        return sb.toString();
    }

    @Override
    public Iterator<WordleCharacter> iterator() {
        return word.iterator();
    }

    /**
     * Check if word is legal given the feedback. Cheks that all letters that we
     * know the position of is in the word, and that all correctly placed letters
     * are in that position.
     * 
     * @param word
     * @param feedback
     * @return true if the word adheres to the feedback
     */
    public static boolean isPossibleWord(String word, WordleWord feedback) {
        WordleWord otherFeedback = WordleAnswer.matchWord(feedback.getWordString(), word);
        return otherFeedback.equals(feedback);
    }


    /**
     * /**
    * Creates a map that associates characters with their positions (indexes) in the word
    * where the character's {@code AnswerType} matches the specified {@code answerType}.
    * The method iterates over the word, checking each character's {@code AnswerType}, and
    * adds the character and its index to the map if it matches the provided {@code answerType}.
    *
    * @param answerType the {@code AnswerType} to match against the characters in the word.
    * @return a {@code HashMap<Character, Integer>} where each key is a character that has
    *         the specified {@code answerType}, and each value is the index of that character
    *         in the word.
    */
    public HashMap<Character,Integer> mapIndexOfAnswerType(AnswerType answerType) {
        HashMap<Character,Integer> correctMap = new HashMap<>();
        for (int i = 0; i<word.size(); i++) {
            if (word.get(i).answerType.equals(answerType) ) {
                correctMap.put(word.get(i).letter, i);
            }
        }
        return correctMap;
    }

    @Override
    public int hashCode() {
        return Objects.hash(word);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        WordleWord other = (WordleWord) obj;
        return Objects.equals(word, other.word);
    }

}
