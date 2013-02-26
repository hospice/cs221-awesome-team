package ir.assignments.four;

/*
4  * Licensed to the Apache Software Foundation (ASF) under one or more
5  * contributor license agreements. See the NOTICE file distributed with
6  * this work for additional information regarding copyright ownership.
7  * The ASF licenses this file to You under the Apache License, Version 2.0
8  * (the "License"); you may not use this file except in compliance with
9  * the License. You may obtain a copy of the License at
10  *
11  * http://www.apache.org/licenses/LICENSE-2.0
12  *
13  * Unless required by applicable law or agreed to in writing, software
14  * distributed under the License is distributed on an "AS IS" BASIS,
15  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
16  * See the License for the specific language governing permissions and
17  * limitations under the License.
18
19   General code structure borrowed from: http://fossies.org/dox/lucene-4.1.0-src/SearchFiles_8java_source.html
20  */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.DefaultSimilarity;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;


public class SearchFiles {

	private SearchFiles() {}

	public static List<String> searchedurls = new ArrayList<String>();

	public static void main(String[] args) throws Exception {
		String usage =
				"Usage:\tjava org.apache.lucene.demo.SearchFiles [-index dir] [-field f] [-repeat n] [-queries file] [-query string] [-raw] [-paging hitsPerPage]\n\nSee http://lucene.apache.org/core/4_1_0/demo/ for details.";
		if (args.length > 0 && ("-h".equals(args[0]) || "-help".equals(args[0]))) {
			System.out.println(usage);
			System.exit(0);
		}


		String index = "docIndex";
		String field = "content";
		String queries = null;
		int repeat=0;
		boolean raw = false;
		String queryString = null;
		int hitsPerPage = 10;

		for(int i = 0;i < args.length;i++) {
			if ("-index".equals(args[i])) {
				index = args[i+1];
				i++;
			} else if ("-field".equals(args[i])) {
				field = args[i+1];
				i++;
			} else if ("-queries".equals(args[i])) {
				queries = args[i+1];
				i++;
			} else if ("-query".equals(args[i])) {
				queryString = args[i+1];
				i++;
			} else if ("-repeat".equals(args[i])) {
				repeat = Integer.parseInt(args[i+1]);
				i++;
			} else if ("-raw".equals(args[i])) {
				raw = true;
			} else if ("-paging".equals(args[i])) {
				hitsPerPage = Integer.parseInt(args[i+1]);
				if (hitsPerPage <= 0) {
					System.err.println("There must be at least 1 hit per page.");
					System.exit(1);
				}
				i++;
			}
		}

		IndexReader reader = DirectoryReader.open(FSDirectory.open(new File(index)));
		IndexSearcher searcher = new IndexSearcher(reader);
		
		searcher.setSimilarity( new IsolationSimilarity() );
		Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_41);

		BufferedReader in = null;
		if (queries != null) {
			in = new BufferedReader(new InputStreamReader(new FileInputStream(queries), "UTF-8"));
		} else {
			in = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
		}
		QueryParser parser = new QueryParser(Version.LUCENE_41, field, analyzer);

			if (queries == null && queryString == null) { // prompt the user
				System.out.println("Enter query: ");
			}

			String line = queryString != null ? queryString : in.readLine();

			if (line == null || line.length() == -1) {
				return;
			}

			line = line.trim();
			if (line.length() == 0) {
				return;
			}

			Query query = parser.parse(line);
		//	query.setBoost(11);

			System.out.println("Searching for: " + query.toString(field));

			doPagingSearch(in, searcher, query, hitsPerPage, raw, queries == null && queryString == null);

			if (queryString != null)
				return;

		reader.close();
	}

	public static void doPagingSearch(BufferedReader in, IndexSearcher searcher, Query query,
			int hitsPerPage, boolean raw, boolean interactive) throws IOException {

		// Collect hitsPerPage number of docs to be shown
		TopDocs results = searcher.search(query, hitsPerPage);
		ScoreDoc[] hits = results.scoreDocs;

		int numTotalHits = results.totalHits;

		System.out.println(numTotalHits + " total matching documents\n");

			for (int i = 0; i < hitsPerPage; i++) {
				if (raw) { // output raw format
					System.out.println("doc="+hits[i].doc+" score="+hits[i].score);
					continue;
				}

				Document doc = searcher.doc(hits[i].doc);
				String path = doc.get("url");
				if (path != null) {
					System.out.println((i+1) + ". " + path);
					searchedurls.add(path);

					System.out.println("doc="+hits[i].doc+" score="+hits[i].score);

					String title = doc.get("title");
					if (title != null) {
						System.out.println(" Title: " + doc.get("title"));
					}
				} else {
					System.out.println((i+1) + ". " + "No path for this document");
				}

			}

		}
	

	public static List<String> getsearchedurls(){
		return searchedurls;
	}

}


class IsolationSimilarity extends DefaultSimilarity {
	public IsolationSimilarity(){
	}
	public float idf(int docFreq, int numDocs) {
		return(float)15;
	}
	public float coord(int overlap, int maxOverlap) {
		return 3f;
	}
	public float lengthNorm(String fieldName, int numTerms) {
		return 1.3f;
	}
}
