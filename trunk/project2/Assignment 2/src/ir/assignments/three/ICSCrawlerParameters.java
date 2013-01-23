package ir.assignments.three;

public class ICSCrawlerParameters {
	private String seedUrl;
	private String intermediateStoragePath;
	private String finalStoragePath;
	
	public String getSeedUrl() { 
		return this.seedUrl;
	}
	
	public void setSeedUrl(String url){
		this.seedUrl = url;
	}
	
	public String getIntermediateStoragePath() { 
		return this.intermediateStoragePath;
	}
	
	public void setIntermediateStoragePath(String path) { 
		this.intermediateStoragePath = path;
	}
	
	public String getFinalStoragePath() { 
		return this.finalStoragePath;
	}
	
	public void setFinalStoragePath(String path) { 
		this.finalStoragePath = path;
	}
}
