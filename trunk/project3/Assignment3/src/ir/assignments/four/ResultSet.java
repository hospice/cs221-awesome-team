package ir.assignments.four;

import java.util.List;

public class ResultSet {
	private List<String> urls;
	private List<Integer> docIds;
	private int totalHits;

	public ResultSet(List<String> urls, List<Integer> docIds, int totalHits) {
		this.urls = urls;
		this.docIds = docIds;
		this.totalHits = totalHits;
	}
	
	public List<String> getUrls() { 
		return this.urls;
	}
	
	public List<Integer> getDocIds() { 
		return this.docIds;
	}
	
	public int getTotalHits() {
		return this.totalHits;
	}
}
