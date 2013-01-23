package ir.assignments.three;

public class ICSCrawlerParameters {
	private String seedUrl;
	private IDocumentStorage docStorage;	
	
	public String getSeedUrl() { 
		return this.seedUrl;
	}
	
	public void setSeedUrl(String url){
		this.seedUrl = url;
	}
	
	public IDocumentStorage getDocumentStorage() {
		return this.docStorage;
	}
	
	public void setDocumentStorage(IDocumentStorage docStorage) {
		this.docStorage = docStorage;
	}
}
