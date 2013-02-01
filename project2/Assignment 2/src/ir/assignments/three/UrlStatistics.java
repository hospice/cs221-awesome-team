package ir.assignments.three;

import ir.assignments.two.a.*;
import ir.assignments.two.b.*;
import ir.assignments.two.c.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.io.*;
import java.net.*;

public class UrlStatistics {
	public static int countUniquePages(Collection<String> urls) {
		// Count the number of unique pages in the URL list (e.g. http://www.example.com/test.php and http://www.example.com/test.php?param are 2 URLs but 1 page
		HashSet<String> hm = new HashSet<String>();
		Iterator<String> iterator;
		int numbersOfUniquePages = 0;

		if(urls != null){

			iterator = urls.iterator();   

			while (iterator.hasNext()){
				URL url=null;
				try {
					url = new URL((String) iterator.next());
				}
				catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String a = url.getProtocol()+"://"+url.getAuthority()+url.getPath();
				hm.add(a);
			}
			numbersOfUniquePages = hm.size();
		}

		return numbersOfUniquePages ;  

	}

	public static List<Frequency> countSubdomains(Collection<String> urls) {
		// http://vision.ics.uci.edu, 10
		// etc.
		ArrayList<Frequency> frequencies = new ArrayList<Frequency>();
		HashMap<String, Integer> hmap = new HashMap<String, Integer>();
		HashSet<String> hset = new HashSet<String>();
		Iterator<String> iterator;

		if(urls != null){
			iterator = urls.iterator();   

			while (iterator.hasNext()){
				URL url=null;
				try {
					url = new URL((String) iterator.next());
				}
				catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String a = url.getProtocol()+"://"+url.getAuthority()+url.getPath();
				hset.add(a);
			}

			iterator = hset.iterator();   

			while (iterator.hasNext()){
				URL url=null;
				try {
					url = new URL(iterator.next());
				}
				catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				String b = url.getProtocol()+"://"+url.getAuthority();
				int count = hmap.containsKey(b) ? hmap.get(b) : 0;
				hmap.put(b, count + 1);

			}

			for (String st : hmap.keySet()) {
				Frequency frequency = new Frequency(st, hmap.get(st));
				frequencies.add(frequency);

			}

		}

		// Order by frequency (desc) and break ties with alphabetical order (asc)
		FrequencyComparator comparator = new FrequencyComparator();
		Collections.sort(frequencies, comparator);


		return frequencies;
	}



	public static Result calculations(Collection<String> urls, IDocumentStorage docStorage) {
		// Get the page with the most terms (including or excluding stop words?)
		HtmlDocument readHtml=null;
		Result r=new Result();
		StopWord stopWord = new StopWord();
		Iterator<String> iterator;
		ArrayList<String> tokens = new ArrayList<String>();
		HashMap<String, Integer> tokenMap = new HashMap<String,Integer>();
		HashMap<String, Integer> gramMap = new HashMap<String,Integer>();
		ArrayList<String> tokens2gram = new ArrayList<String>();
		ArrayList<String> totalWords = new ArrayList<String>();
		List<Frequency> frequency = new ArrayList<Frequency>();
		List<String> commonWords = new ArrayList<String>();
		List<Frequency> frequency1 = new ArrayList<Frequency>();
		List<String> common2grams = new ArrayList<String>();
		String maxLengthURL = "";
		int maxLength = 0;
		int count=0;

		if(urls != null){
			iterator = urls.iterator();   

			while (iterator.hasNext()){
				String it = iterator.next();
				readHtml = docStorage.getDocument(it);
				
				if(readHtml != null){
					String data = readHtml.getAllText();
					tokens = Utilities.tokenizeFile(data);

					// Placing the words in the HashMap with Value=1 for Stop word and Value=0 for Non-Stop word
					for (String s: tokens){
						if(!Utilities.isNumber(s))
							tokenMap.put(s,1);
					}

					// Counting the number of words in this page
					count =0;
					for (int i = 0; i < tokens.size(); i++) {
						if(!Utilities.isNumber(tokens.get(i))){
							if(tokenMap.get(tokens.get(i)) == 1 ){
								if(!StopWord.isStopWord(tokens.get(i))){
									totalWords.add(tokens.get(i));
									tokenMap.put(tokens.get(i),0);
									count++;
								}
								else{
									tokenMap.put(tokens.get(i),2);
								}
							}
							else{
								if(tokenMap.get(tokens.get(i)) != 2 ){
									totalWords.add(tokens.get(i));
									count++;
								}
							}
						}
						else count++;
					}

					// Creating and finding the frequency of 2-grams
					frequency = TwoGramFrequencyCounter.computeTwoGramFrequencies(tokens);
					for (int i = 0; i < frequency.size(); i++) {
						Frequency temp = frequency.get(i);
						String text = temp.getText();
						tokens2gram = Utilities.tokenizeFile(text);
						if(!Utilities.isNumber(tokens2gram.get(0)) && !Utilities.isNumber(tokens2gram.get(1))){
							if(tokenMap.get(tokens2gram.get(0)) == 0 && tokenMap.get(tokens2gram.get(1)) == 0)
							{
								int currentValue = gramMap.containsKey(text) ? gramMap.get(text) : 0;
								gramMap.put(text, temp.getFrequency() + currentValue);
							}
						}
					}
				}

				// Checking if existing word count is greater than the word count for this page
				if(count > maxLength){
					maxLength=count;
					maxLengthURL = it;
				}
			}
		}


		// Finding the frequency of words from all pages
		frequency = WordFrequencyCounter.computeWordFrequencies(totalWords);

		// Getting top 500 most common words
		for (int i = 0; i < frequency.size(); i++) {
			if(i>=500){
				break;
			}
			commonWords.add(frequency.get(i).getText());
		}

		// Convert the 2-gram dictionary to an array
		for (String str : gramMap.keySet()) {
			Frequency freq = new Frequency(str, gramMap.get(str));
			frequency1.add(freq);
		}

		// Order by frequency (desc) and break ties with alphabetical order (asc)
		FrequencyComparator comparator = new FrequencyComparator();
		Collections.sort(frequency1, comparator);

		// Getting the top 20 most common 2-grams
		for (int i = 0; i < frequency1.size(); i++) {
			if(i>=20){
				break;
			}
			common2grams.add(frequency1.get(i).getText());
		}

		r.setLongestPageUrlString(maxLengthURL);
		r.setMostCommonWords(commonWords);
		r.setMostCommon2Grams(common2grams);
		return r;		
	}
}
