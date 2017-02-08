package org.roan.examples;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
					
			Map<Integer, List<String>> histoWordsByScore = shakespeareWords.stream().collect(Collectors.groupingBy(score));
			
			System.out.println("# histoWordsByScore = " + histoWordsByScore.size());
			
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		
	}
	
}
