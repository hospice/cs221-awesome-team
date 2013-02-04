Team Members:
  1) Armando Pensado Valle <apensado@uci.edu>
  2) Vibhor Mathur <mathurv@uci.edu>

How to Run:
  > To run the web crawler, run class MainCrawler
  > To run the analysis on the crawled documents, run class MainStatistics

Assumptions:
  > Crawling:
    - URLs of the following file formats were not visited:
      css,js,bmp,gif,jpg,jpeg,png,tif,tiff,mid,mp2,mp3,mp4,wav,avi,mov,mpeg,ram,m4v,pdf,rm,smil,wmv,swf,wma,zip,rar,gz,ico,pfm,c,h,o
    - Only visited documents with HTML content were saved
    - Only documents up to ~2MB were saved
  > Analysis:
    - Stop words were filtered based on the "Default English stopwords list" in http://www.ranks.nl/resources/stopwords.html
    - Words that were shorter than 4 characters were considered uninteresting (and treated the same as stop words)
    - Words that contained at least one number (0-9) were considered uninteresting
    - The longest document length excluded stop and uninteresting words