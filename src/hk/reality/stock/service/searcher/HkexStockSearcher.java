package hk.reality.stock.service.searcher;

import hk.reality.stock.Constants;
import hk.reality.stock.model.Stock;
import hk.reality.stock.service.Lang;
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

import android.util.Log;

public class HkexStockSearcher implements StockSearcher {
    private static final String TAG = "HKEX";
    private static final String BASE_URL = "http://www.hkex.com.hk/invest/company/profile_page_%s.asp?WidCoID=%s&WidCoAbbName=&Month=";

    private static final String BEGIN = "#66CCFF\">";
    private static final String END = "</font>";

    private static final String REGEXP = "(.+)\\(([0-9]+).+\\)";
    private static final Pattern pattern = Pattern.compile(REGEXP);
    
    private HttpClient client;    
    private Lang language;

    public HkexStockSearcher(Lang language) {        
        HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(params, 30 * 1000);
        HttpConnectionParams.setSoTimeout(params, 30 * 1000);
        HttpProtocolParams.setUserAgent(params, Constants.USER_AGENT);
        this.client = new DefaultHttpClient(params);
        this.language = language;
    }
    
    @Override
    public Stock searchStock(String quote) throws DownloadException, ParseException {
        Log.i(TAG, "search stock: " + quote + ", lang=" + language);

        String url = getStockURL(quote, language);
        HttpGet req = new HttpGet(url);
        try {
            HttpResponse resp = client.execute(req);
            String content = EntityUtils.toString(resp.getEntity(), "Big5");
            int begin = content.indexOf(Matcher.quoteReplacement(BEGIN));
            int end = content.indexOf(END, begin);
            if (begin > -1 && end > -1) {
                String value = StringUtils.substring(content, begin + BEGIN.length(), end);
                value = StringUtils.strip(value).replaceAll("[\n\r]*", "");
                Log.i(TAG, "result text: " + value);
                
                Matcher m = pattern.matcher(value);
                if (m.find()) {
                    String name = StringUtils.strip(m.group(1));
                    String quoteNumber = String.format("%05d", Integer.parseInt(StringUtils.strip(m.group(2))));
                    Stock s = new Stock();
                    s.setName(name);
                    s.setQuote(quoteNumber);
                    return s;
                } else {
                    Log.w(TAG, "result text not match target pattern " );
                }
            } else {
                Log.w(TAG, "begin or end not found " + begin + ", " + end );
            }
        } catch (ClientProtocolException e) {
            throw new DownloadException("error searching stock", e);
        } catch (IOException e) {
            throw new DownloadException("error searching stock", e);
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
