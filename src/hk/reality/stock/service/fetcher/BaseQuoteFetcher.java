package hk.reality.stock.service.fetcher;

import hk.reality.stock.Constants;

import org.apache.commons.lang.StringUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
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
    
    public BaseQuoteFetcher() {
        HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params, 30 * 1000);
        HttpConnectionParams.setSoTimeout(params, 30 * 1000);
        HttpProtocolParams.setUserAgent(params, Constants.USER_AGENT);
        this.client = new DefaultHttpClient(params);

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

    public String getFirstElementAsString(TagNode document, String xpath) throws XPatherException {
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
