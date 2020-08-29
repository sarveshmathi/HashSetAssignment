
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Analyzer {

	/*
	 * Implement this method in Part 1
	 */
	public static List<Sentence> readFile(String filename) {
		List<Sentence> output = new LinkedList<>();

		if (filename == null) {
			return output;
		}

		FileReader fr;
		try {
			fr = new FileReader(filename);
			Scanner in = new Scanner(fr);
			while (in.hasNextLine()) {
				String line = in.nextLine();
				String scoreString = line.substring(0, line.indexOf(" ")).trim();
				if (scoreString.equals("-2") || scoreString.equals("-1") || scoreString.equals("0") || scoreString.equals("1")
						|| scoreString.equals("2")) {
					int score = Integer.parseInt(scoreString);
					String text = line.substring(line.indexOf(" ") + 1);
					Sentence sentence = new Sentence(score, text);
					output.add(sentence);
				}
			}
			in.close();
			return output;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			return output;

		}
		
		
	} 

	/*
	 * Implement this method in Part 2
	 */
	public static Set<Word> allWords(List<Sentence> sentences) {
		HashMap<String, Word> wordMap = new HashMap<>();
		HashSet<Word> wordSet = new HashSet<>();

		if (sentences == null || sentences.isEmpty()) {
			return wordSet;
		}

		for (Sentence sentence : sentences) {
			if (sentence != null && !sentence.getText().isEmpty()) {
				String line = sentence.getText();
				String[] words = line.split(" ");
				for (String word : words) {
					if (Character.isLetter(word.charAt(0))) {
						word = word.toLowerCase();
						if (!Character.isLetter(word.charAt(word.length() -1))) {
							word = word.substring(0,word.length()-1);
						}
						int sentenceScore = sentence.getScore();

						if (wordMap.containsKey(word)) {
							Word w = wordMap.get(word);
							w.increaseTotal(sentenceScore);
							wordMap.put(word,w);
						} else {
							Word w = new Word(word);
							w.increaseTotal(sentenceScore);
							wordMap.put(word, w);
						}
					}
				}
			}
		}

		for (String word : wordMap.keySet()) {
			Word w = wordMap.get(word);
			wordSet.add(w);
		}

		return wordSet;
	}

	/*
	 * Implement this method in Part 3
	 */
	public static Map<String, Double> calculateScores(Set<Word> words) {
		Map<String, Double> wordMap = new HashMap<>();

		if (words == null || words.isEmpty()) {
			return wordMap;
		}

		for (Word w : words) {
			if (w != null) {
				wordMap.put(w.getText(), w.calculateScore());
			}
		}

		return wordMap;
	}

	/*
	 * Implement this method in Part 4
	 */
	public static double calculateSentenceScore(Map<String, Double> wordScores, String sentence) {

		if (wordScores == null || wordScores.isEmpty() || sentence == null || sentence.isEmpty()) {
			return 0;
		}

		double totalScore = 0;
		int count = 0;
		String[] words = sentence.split(" ");

		for (String word : words) {

			if (Character.isLetter(word.charAt(0))) {
				word = word.toLowerCase();
				
				if (!Character.isLetter(word.charAt(word.length() -1))) {
					word = word.substring(0,word.length()-1);
				}
				
				if (wordScores.containsKey(word)) {
					totalScore += wordScores.get(word);
					count++;
				} else {
					count++;
				}
			}

		}

		return totalScore/count;
	}

	/*
	 * You do not need to modify this code but can use it for testing your program!
	 */
	public static void main(String[] args) {

//		List<Sentence> s = Analyzer.readFile("reviews.txt");
//		System.out.println("Sentences list size: " + s.size()); 
//		Set<Word> w = Analyzer.allWords(s);
//		System.out.println("Words set size: " + w.size());

				if (args.length == 0) {
					System.out.println("Please specify the name of the input file");
					System.exit(0);
				}
				String filename = args[0];
				System.out.print("Please enter a sentence: ");
				Scanner in = new Scanner(System.in);
				String sentence = in.nextLine();
				in.close();
				List<Sentence> sentences = Analyzer.readFile(filename);
				Set<Word> words = Analyzer.allWords(sentences);
				Map<String, Double> wordScores = Analyzer.calculateScores(words);
				
				for (String s : wordScores.keySet()) {
					System.out.println(s+ ", " + wordScores.get(s));
				}
				
				double score = Analyzer.calculateSentenceScore(wordScores, sentence);
				System.out.println("The sentiment score is " + score);
	}

}
