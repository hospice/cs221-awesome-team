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

	public static String getLongestPage(Collection<String> urls, IDocumentStorage docStorage) {
		// Get the page with the most terms (including or excluding stop words?)
		HtmlDocument readHtml=null;
		StopWord stopWord = new StopWord();
		Iterator<String> iterator;
		ArrayList<String> tokens = new ArrayList<String>();
		int maxLength = 0;
		String maxLengthURL = "";

		if(urls != null){
			iterator = urls.iterator();   

			while (iterator.hasNext()){
				String it = iterator.next();
				readHtml = docStorage.getDocument(it);
				if(readHtml != null){
					String data = readHtml.getAllText();
					tokens = Utilities.tokenizeFile(data);
					int count =0;
					for (int i = 0; i < tokens.size(); i++) {
						if(!StopWord.isStopWord(tokens.get(i))){
							count++;
						}
					}

					if(count > maxLength){
						maxLengthURL = it;
					}

				}
			}
		}

		return maxLengthURL;
	}

	public static List<String> getMostCommonWords(Collection<String> urls, IDocumentStorage docStorage) {
		// Get top 500 most common words across all documents (excluding stop words)
		HtmlDocument readHtml=null;
		StopWord stopWord = new StopWord();
		Iterator<String> iterator;
		ArrayList<String> totalWords = new ArrayList<String>();
		List<Frequency> frequency = new ArrayList<Frequency>();
		List<String> commomWords = new ArrayList<String>();

		if(urls != null){
			iterator = urls.iterator();   

			while (iterator.hasNext()){
				String it = iterator.next();
				readHtml = docStorage.getDocument(it);
				if(readHtml != null){
					String data = readHtml.getAllText();

					for (int i = 0; i < Utilities.tokenizeFile(data).size(); i++) {
						if(!StopWord.isStopWord(Utilities.tokenizeFile(data).get(i))){
							totalWords.add(Utilities.tokenizeFile(data).get(i));
						}
					}
				}
			}
		}

		frequency = WordFrequencyCounter.computeWordFrequencies(totalWords);

		for (int i = 0; i < frequency.size(); i++) {
			if(i>500){
				break;
			}
			commomWords.add(frequency.get(i).getText());
		}

		return commomWords;
	}

	public static List<String> getMostCommon2Grams(Collection<String> urls, IDocumentStorage docStorage) {
		// Get top 20 2-grams excluding stop words
		HtmlDocument readHtml=null;
		StopWord stopWord = new StopWord();
		Iterator<String> iterator;
		ArrayList<String> tokens = new ArrayList<String>();
		List<Frequency> frequency = new ArrayList<Frequency>();
		List<String> commom2grams = new ArrayList<String>();

		if(urls != null){
			iterator = urls.iterator();   

			while (iterator.hasNext()){
				String it = iterator.next();
				readHtml = docStorage.getDocument(it);
				if(readHtml != null){
					String data = readHtml.getAllText();
					tokens = Utilities.tokenizeFile(data);

					for (int i = 0; i < TwoGramFrequencyCounter.computeTwoGramFrequencies(tokens).size(); i++) {
						Frequency temp = TwoGramFrequencyCounter.computeTwoGramFrequencies(tokens).get(i);

						if(!StopWord.isStopWord(Utilities.tokenizeFile(temp.getText()).get(0)) && !StopWord.isStopWord(Utilities.tokenizeFile(temp.getText()).get(1)))
						{
							int flag=0;

							for(Frequency f:frequency){
								if(f.getText().equals(temp.getText())){
									temp.incrementFrequency();
									flag=1;
									break;
								}
							}
							if(flag==0)
								frequency.add(temp);

						}
					}
				}
			}
		}

		// Order by frequency (desc) and break ties with alphabetical order (asc)
		FrequencyComparator comparator = new FrequencyComparator();
		Collections.sort(frequency, comparator);

		for (int i = 0; i < frequency.size(); i++) {
			if(i>20){
				break;
			}
			commom2grams.add(frequency.get(i).getText());
		}

		return commom2grams;	
	}
}
