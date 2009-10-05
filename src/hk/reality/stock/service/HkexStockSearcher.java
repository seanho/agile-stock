package hk.reality.stock.service;

import hk.reality.stock.model.Stock;
import hk.reality.stock.service.exception.DownloadException;
import hk.reality.stock.service.exception.ParseException;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.EntityUtils;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;

import android.util.Log;

public class HkexStockSearcher implements StockSearcher {
    private static final String TAG = "HKEX";
    private static final String USER_AGENT = "AgileStock/0.1.0";
    private static final String BASE_URL = "http://www.hkex.com.hk/invest/company/profile_page_%s.asp?WidCoID=%s&WidCoAbbName=&Month=";
    private static final String XPATH = "//table//table[1]//tr[2]/td[2]/table//tr[1]/td/font";
    private static final String REGEXP = "^.+\\([^\\)]+\\)$";
    private static final Pattern pattern = Pattern.compile(REGEXP);
    
    private HttpClient client;
    private HtmlCleaner cleaner;
    
    private Lang language;

    public HkexStockSearcher(Lang language) {        
        HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params, 30 * 1000);
        HttpConnectionParams.setSoTimeout(params, 30 * 1000);
        HttpProtocolParams.setUserAgent(params, USER_AGENT);
        this.client = new DefaultHttpClient(params);
        this.cleaner = new HtmlCleaner();
        this.language = language;
    }
    
    @Override
    public Stock searchStock(String quote) {
        Log.i(TAG, "search stock: " + quote + ", lang=" + language);

        String url = getStockURL(quote, language);
        HttpGet req = new HttpGet(url);
        try {
            HttpResponse resp = client.execute(req);
            String content = EntityUtils.toString(resp.getEntity());
            TagNode document = cleaner.clean(content);
            Object[] xpathResult = document.evaluateXPath(XPATH);

            for(Object o : xpathResult) {
                if (o instanceof TagNode) {
                    TagNode contentNode = (TagNode) o;
                    String fullName = StringUtils.trim(contentNode.getText().toString());
                    Log.d(TAG, "  name found: " + fullName);
                    Matcher matcher = pattern.matcher(fullName);
                    if (matcher.matches()) {
                        String stockName = matcher.group(1);
                        String stockQuote = matcher.group(2);
                        Stock stock = new Stock();
                        stock.setName(stockName);
                        stock.setQuote(stockQuote);
                        return stock;
                    }
                }
            }
        } catch (ClientProtocolException e) {
            throw new DownloadException("error searching stock", e);
        } catch (IOException e) {
            throw new DownloadException("error searching stock", e);
        } catch (XPatherException e) {
            throw new ParseException("unexpected result while searching stock", e);
        }
        throw new ParseException("stock quote not found");
    }
    
    /**
     * @return the language
     */
    public Lang getLanguage() {
        return language;
    }

    /**
     * @param language the language to set
     */
    public void setLanguage(Lang language) {
        this.language = language;
    }

    private String getStockURL(String quote, Lang lang) {
        String langCode = lang.equals(Lang.CHI) ? "c" : "e";
        String baseURL = String.format(BASE_URL, langCode, quote);
        return baseURL;
    }
}
