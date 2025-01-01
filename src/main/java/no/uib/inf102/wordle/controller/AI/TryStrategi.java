package no.uib.inf102.wordle.controller.AI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import no.uib.inf102.wordle.model.Dictionary;
import no.uib.inf102.wordle.model.word.AnswerType;
import no.uib.inf102.wordle.model.word.WordleWord;
import no.uib.inf102.wordle.model.word.WordleWordList;

public class TryStrategi implements IStrategy {

    private Dictionary dictionary;
    private WordleWordList guesses;
    private int totalWords;
    private List<AnswerType[]> feedbacks;
    private HashMap<String,Double> entropy;

    public TryStrategi(Dictionary dictionary) {
        this.dictionary = dictionary;
        this.entropy = new HashMap<>();
        reset();
    }

    @Override
    public String makeGuess(WordleWord feedback) {
        if (feedback == null) {
           IStrategy frec = new FrequencyStrategy(dictionary);
           return "tares";
           //return frec.makeGuess(null);
        }
        
        if (feedback != null) {
            guesses.eliminateWords(feedback);
        }

     
        for (String word : guesses.possibleAnswers()) {
            if (!this.entropy.containsKey(word)) {
                this.entropy.put(word, countEntropy(word, feedbacks.size()-1));
            }
            
        }
        double bestScore = 0;
        String bestWord = new String();
        for (String word : guesses.possibleAnswers()) {
            double score = entropy.getOrDefault(word,(double) 0);
            if (score > bestScore) {
                bestWord = word;
            }
        }
        return (!bestWord.isBlank()) ? bestWord : guesses.possibleAnswers().get(0); 
    }

    public double countEntropy(String word, int index) {
        if (index < 0) {
            return 0;
        }
        
        int sizeBefore = guesses.possibleAnswers().size();
        int count = 0;
        WordleWord feedback = new WordleWord(word, feedbacks.get(index));
        
        
        for (String w : guesses.possibleAnswers()) {
            if (WordleWord.isPossibleWord(w, feedback)) {
                count++;
            }
        }
    
        double prob = (double) count / sizeBefore;  
        if (prob == 0) {
            return countEntropy(word, index - 1);  
        }
        double entropyContribution = -prob * (Math.log(prob) / Math.log(2));  // -p(x) * log2(p(x))
        return entropyContribution + countEntropy(word, index - 1);
    }

    private static void allPossibleFeedback(List<AnswerType[]> feedbacks, AnswerType[] feedback, int length, int position) {
        if (position == length) {
            feedbacks.add(feedback.clone());
            return;
        }
        AnswerType[] feedbackTypes = { AnswerType.CORRECT, AnswerType.MISPLACED, AnswerType.WRONG };
        for (int i = 0; i < feedbackTypes.length; i++) {
            feedback[position] = feedbackTypes[i];
            allPossibleFeedback(feedbacks, feedback, length, position + 1);
        }
    }

    @Override
    public void reset() {
        this.guesses = new WordleWordList(dictionary);
        this.totalWords = guesses.size();
        this.feedbacks = new ArrayList<>();
        AnswerType[] feedback = new AnswerType[5];
        allPossibleFeedback(feedbacks, feedback, guesses.wordLength(), 0);       
    }

}
