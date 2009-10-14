package hk.reality.stock.service.fetcher;

import hk.reality.stock.Constants;

import org.apache.commons.lang.StringUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;

public abstract class BaseQuoteFetcher implements QuoteFetcher {
    private HttpClient client;
    private HtmlCleaner cleaner;
    private static final int TIMEOUT = 10;
    
    public BaseQuoteFetcher() {
        
        HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params, TIMEOUT * 1000);
        HttpConnectionParams.setSoTimeout(params, TIMEOUT * 1000);
        HttpProtocolParams.setUserAgent(params, Constants.USER_AGENT);
        
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        ClientConnectionManager cm = new ThreadSafeClientConnManager(params, schemeRegistry);
        this.client = new DefaultHttpClient(cm, params);

        this.cleaner = new HtmlCleaner();
        CleanerProperties prop = cleaner.getProperties();
        prop.setOmitDoctypeDeclaration(true);
        prop.setOmitUnknownTags(true);
        prop.setOmitComments(true);
        prop.setIgnoreQuestAndExclam(true);
        prop.setOmitDeprecatedTags(true);
        prop.setOmitXmlDeclaration(true); 
        prop.setAdvancedXmlEscape(false);
        prop.setRecognizeUnicodeChars(false);
        prop.setOmitHtmlEnvelope(false);        
        prop.setUseCdataForScriptAndStyle(true);        
    }

    /**
     * From the document, search the specified xpath, return the TagNode of first matched element
     * @param document
     * @param xpath
     * @return
     * @throws XPatherException
     */
    public TagNode getFirstMatchedElement(TagNode document, String xpath) throws XPatherException {
        Object[] xpathResult = document.evaluateXPath(xpath);
        for(int i=0; i<xpathResult.length; i++) {
            if (xpathResult[i] instanceof TagNode) {
                return (TagNode) xpathResult[i];
            }
        }
        return null;
    }
    
    /**
     * From the document, search the specified xpath, return the content text of first matched element
     * @param document
     * @param xpath
     * @return
     * @throws XPatherException
     */
    public String getFirstMatchedElementContent(TagNode document, String xpath) throws XPatherException {
        Object[] elements = document.evaluateXPath(xpath);
        for(int i=0; i<elements.length; i++) {
            if (elements[i] instanceof TagNode) {
                TagNode node = (TagNode) elements[i];
                return StringUtils.trim(node.getText().toString());
            }            
        }
        return "";
    }
    
    /**
     * @return the client
     */
    public HttpClient getClient() {
        return client;
    }

    /**
     * @return the cleaner
     */
    public HtmlCleaner getCleaner() {
        return cleaner;
    }
}
