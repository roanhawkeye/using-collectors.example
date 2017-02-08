package org.roan.examples;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MainUsingCollectors {

	public static void main(String[] args) throws IOException {
		
		Path shakespearPath = Paths.get("./files/words.shakespeare.txt");
		Path ospdPath = Paths.get("./files/ospd.txt");
		
		try (Stream<String> ospd = Files.lines(ospdPath);
			Stream<String> shakespeare = Files.lines(shakespearPath);){
			
			Set<String> scrabbleWords = ospd.collect(Collectors.toSet());
			Set<String> shakespeareWords = shakespeare.collect(Collectors.toSet());
			
			System.out.println("Scrabble : " + scrabbleWords.size());
			System.out.println("Shakespeare : " + shakespeareWords.size());
			
			int[] letterScores = {
					//a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p,  q, r, s, t, u, v, w, x, y,  z
					  1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 5, 1, 3, 1, 1, 3, 10, 1, 1, 1, 1, 4, 4, 8, 4, 10
			};
			
			Function<String, Integer> score =
					word -> word.toLowerCase().chars()
						.map(letter -> letterScores[letter - 'a'])
						.sum();
					
			Map<Integer, List<String>> histoWordsByScore = shakespeareWords.stream().filter(scrabbleWords::contains).collect(Collectors.groupingBy(score));
			
			System.out.println("# histoWordsByScore = " + histoWordsByScore.size());
			
			histoWordsByScore.entrySet()
				.stream()
				.sorted(Comparator.comparing(entry -> -entry.getKey()))
				.limit(3)
				.forEach(entry -> System.out.println(entry.getKey() + " - " + entry.getValue()));
			
			int [] scrabbleENDistribution = {
				//a, b, c, d,  e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z
				  9, 2, 2, 1, 12, 2, 3, 2, 9, 1, 1, 4, 2, 6, 8, 2, 1, 6, 4, 6, 4, 2, 2, 1, 2, 1	
			};
			
			Function<String, Map<Integer, Long>> histoWord = word -> word.chars().boxed()
					.collect(Collectors.groupingBy(letter -> letter, Collectors.counting()));
			
			Function<String, Long> nBlanks = word -> histoWord.apply(word) //Map<Integer, Long> Map<letter, # of letters>
					.entrySet()
					.stream() //Map.Entry<Integer, Long>
					.mapToLong(entry -> Long.max(entry.getValue() - (long) scrabbleENDistribution[entry.getKey() - 'a'], 0L))
					.sum();
			
			System.out.println("# of blanks for whizzing : " + nBlanks.apply("whizzing"));
			
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		
	}
	
}
