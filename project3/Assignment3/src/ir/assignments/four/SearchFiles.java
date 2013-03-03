package ir.assignments.four;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.spans.SpanFirstQuery;
import org.apache.lucene.search.spans.SpanTermQuery;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class SearchFiles {
	private IndexReader reader;
	private IndexSearcher searcher;
	private Analyzer analyzer;
	
	public SearchFiles(String indexLocation) throws IOException {
		this.reader = DirectoryReader.open(FSDirectory.open(new File(indexLocation)));
		this.searcher = new IndexSearcher(reader);
		
		Analyzer regularAnalyzer = new StandardAnalyzer(Version.LUCENE_41);	
		Analyzer stemAnalyzer = new EnglishAnalyzer(Version.LUCENE_41);
		
		// Use the stem analyzer for some fields, the regular analyzer for the rest
		// Note: Should match mapping used in Indexer.java
		Map<String,Analyzer> analyzerPerField = new HashMap<String,Analyzer>();
		analyzerPerField.put("stemcontent", stemAnalyzer);
		analyzerPerField.put("stemtitle", stemAnalyzer);
		analyzerPerField.put("stemanchortext", stemAnalyzer);

		this.analyzer = new PerFieldAnalyzerWrapper(regularAnalyzer, analyzerPerField);
	}
	
	public List<String> search(String queryString, int count) {
		
		// Do search by combining the query among different fields with different boosting weights
		
		try {
			// Build query
			Query[] queries = new Query[]
			{
				getFieldQuery(queryString, "url", 0.0f),
				getFieldQuery(queryString, "urldomain", 27.1f),
				
				getFieldQuery(queryString, "title", 45.9f),
				getFieldSpanQuery(queryString, "title", 57.35f),
				getFieldQuery(queryString, "stemtitle", 2.5f),
				getFieldSpanQuery(queryString, "stemtitle", 86.0f),
				
				getFieldQuery(queryString, "content", 56.2f),
				getFieldQuery(queryString, "stemcontent", 44.5f),
				getFieldPhraseQuery(queryString, "content", 39.4f),
				
				getFieldQuery(queryString, "contentheaders", 28.4f),
				getFieldQuery(queryString, "importantcontent", 81.8f),
				
				getFieldQuery(queryString, "outgoingtext", 22.0f),
				getFieldQuery(queryString, "anchortext", 2.6f),
				getFieldQuery(queryString, "stemanchortext", 69.0f)
			};
			
			BooleanQuery finalQuery = new BooleanQuery();
			for (Query query : queries) {
				if (query.getBoost() > 0.0f)
					finalQuery.add(query, Occur.SHOULD);
			}

			return doPagingSearch(finalQuery, count);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	public List<String> search(String queryString, int count, float[] weightVector) {
		// Used by SearchOptimizer.java to find the best weights for the test queries
		
		try {
			// Build query
			Query[] queries = new Query[]
			{
				getFieldQuery(queryString, "url", weightVector[0]),
				getFieldQuery(queryString, "urldomain", weightVector[1]),
				
				getFieldQuery(queryString, "title", weightVector[2]),
				getFieldSpanQuery(queryString, "title", weightVector[3]),
				getFieldQuery(queryString, "stemtitle", weightVector[4]),
				getFieldSpanQuery(queryString, "stemtitle", weightVector[5]),
				
				getFieldQuery(queryString, "content", weightVector[6]),
				getFieldQuery(queryString, "stemcontent", weightVector[7]),
				getFieldPhraseQuery(queryString, "content", weightVector[8]),
				
				getFieldQuery(queryString, "contentheaders", weightVector[9]),
				getFieldQuery(queryString, "importantcontent", weightVector[10]),
				
				getFieldQuery(queryString, "outgoingtext", weightVector[11]),
				getFieldQuery(queryString, "anchortext", weightVector[12]),
				getFieldQuery(queryString, "stemanchortext", weightVector[13])
			};

			BooleanQuery finalQuery = new BooleanQuery();
			for (Query query : queries) {
				if (query.getBoost() > 0.0f) {
					finalQuery.add(query, Occur.SHOULD);
				}
			}
			
			return doPagingSearch(finalQuery, count);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	private Query getFieldQuery(String queryString, String field, float boost) throws ParseException {
		// Build a default query for the field
		QueryParser parser = new QueryParser(Version.LUCENE_41, field, this.analyzer);
		Query query = parser.parse(queryString);
		query.setBoost(boost);
		return query;		
	}
	
	private Query getFieldSpanQuery(String queryString, String field, float boost) throws ParseException, IOException {
		// Build a query that matches documents were the query string is in the beginning of the field
		// e.g. this can be used to give preference to title's with the query strings near the beginning
		
		// Split the query string into tokens
		TokenStream tokens = this.analyzer.tokenStream(field, new StringReader(queryString));
		tokens.reset();
		
		BooleanQuery query = new BooleanQuery();
		
		// Allow each token to be k terms from the start (starting with 1)
		int k = 1;
		while (tokens.incrementToken()) {
			String word = tokens.getAttribute(CharTermAttribute.class).toString();
			SpanTermQuery spanTermQuery = new SpanTermQuery(new Term(field, word));
			SpanFirstQuery spanFirstQuery = new SpanFirstQuery(spanTermQuery, k);
			k++;
			
			query.add(spanFirstQuery, Occur.SHOULD);
		}
		query.setBoost(boost);
		
		return query;		
	}
	
	private Query getFieldPhraseQuery(String queryString, String field, float boost) throws ParseException, IOException {
		// Build a query that matches the query string as an exact phrase
		TokenStream tokens = this.analyzer.tokenStream(field, new StringReader(queryString));
		tokens.reset();
		
		PhraseQuery query = new PhraseQuery();
		while (tokens.incrementToken()) {
			String word = tokens.getAttribute(CharTermAttribute.class).toString();
			query.add(new Term(field, word));
		}
		query.setBoost(boost);
		
		return query;
	}
	
	private List<String> doPagingSearch(Query query, int topNResults) throws IOException {

		// Do the search and collect the top N results
		TopDocs results = searcher.search(query, topNResults);
		ScoreDoc[] hits = results.scoreDocs;
		
		ArrayList<String> urls = new ArrayList<String>();		
		for (int i = 0; i < Math.min(topNResults, hits.length); i++) {
			// Results are in docIds, find the indexed document
			Document doc = searcher.doc(hits[i].doc);
			
			// Save the URL of the document
			String url = doc.get("url");			
			urls.add(url);
		}
		
		return urls;
	}
	
	public void close(){
		if (reader != null) {
			try {
				reader.close();
				analyzer.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
