package hk.reality.stock.service.fetcher;

import hk.reality.stock.model.StockDetail;
import hk.reality.stock.service.exception.DownloadException;
import hk.reality.stock.service.exception.ParseException;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.htmlcleaner.TagNode;
import org.htmlcleaner.XPatherException;

public class EtnetQuoteFetcher extends BaseQuoteFetcher {
    private static final String BASE_URL = "http://www.etnet.com.hk/www/tc/stocks/quote.php?code=%s";
    private static final String XPATH_UPDATE = "//div/div[1]/table[1]/tbody/tr/td[2]/span";
    private static final String XPATH_BASE =  "//div/div/table[3]/tbody/tr[2]/td/table";
    
    private static final String XPATH_NAME = "//table/tbody/tr/td[1]/table/tbody/tr/td[2]";
    private static final String XPATH_PRICE = "//table/tbody/tr[2]/td[1]";    
    private static final String XPATH_PRICE_CHANGE = "//table/tbody/tr[2]/td[2]";
    private static final String REGEXP_PRICE_CHANGE = "(-?[0-9\\.]+).+\\((-?[0-9\\.]+)%\\)";
    private static final Pattern PATTERN_PRICE_CHANGE = Pattern.compile(REGEXP_PRICE_CHANGE);
    
    private static final String XPATH_DAY_HIGH = "//tr[1]/td[2]/table/tbody/tr[2]/td/span";
    private static final String XPATH_DAY_LOW = "//tr[2]/td[1]/table/tbody/tr[2]/td/span";
    private static final String XPATH_DAY_VOLUME = "//tr[1]/td[3]/table/tbody/tr[2]/td/span";
    
    private static final String DATE_FORMAT = "dd/MM/yyyy HH:mm";
    
    @Override
    public StockDetail fetch(String quote) {
        StockDetail detail = new StockDetail();
        String url = getUrl(quote);
        HttpGet req = new HttpGet(url);        
        try {
            detail.setQuote(quote);
            detail.setSourceUrl(url);

            // download html
            HttpResponse resp = getClient().execute(req);
            String html = EntityUtils.toString(resp.getEntity());
            
            // optimization to reduce html size
            int start = html.indexOf("<!-- Content -->");
            int end = html.indexOf("top:-1000px;\">");
            html = StringUtils.substring(html, start, end);
            TagNode document = getCleaner().clean(html);
            resp = null;
            
            // set name
            String name = getFirstMatchedElementContent(document, XPATH_NAME);
            detail.setName(name);
            
            // set updatedAt
            SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
            String updatedAtStr = getFirstMatchedElementContent(document, XPATH_UPDATE);
            Date updatedDate = formatter.parse(updatedAtStr);
            Calendar updatedAt = Calendar.getInstance();
            updatedAt.setTime(updatedDate);
            detail.setUpdatedAt(updatedAt);

            TagNode table = getFirstMatchedElement(document, XPATH_BASE);
            
            // set price
            String pricesStr = getFirstMatchedElementContent(table, XPATH_PRICE);
            BigDecimal price = new BigDecimal(pricesStr);
            detail.setPrice(price);
            
            // set price change and change %
            String priceChangesStr = getFirstMatchedElementContent(table, XPATH_PRICE_CHANGE);
            Matcher priceChangeMatcher = PATTERN_PRICE_CHANGE.matcher(priceChangesStr);
            if (priceChangeMatcher.find()) {
                String priceChangeNumStr = priceChangeMatcher.group(1);
                BigDecimal priceChangeNum = new BigDecimal(priceChangeNumStr);
                detail.setChangePrice(priceChangeNum);

                String priceChangePercentStr = priceChangeMatcher.group(2);
                BigDecimal priceChangePercent = new BigDecimal(priceChangePercentStr);
                detail.setChangePricePercent(priceChangePercent);
            }
            
            String dayHighStr = getFirstMatchedElementContent(table, XPATH_DAY_HIGH);
            BigDecimal dayHigh = new BigDecimal(dayHighStr);
            detail.setDayHigh(dayHigh);

            String dayLowStr = getFirstMatchedElementContent(table, XPATH_DAY_LOW);
            BigDecimal dayLow = new BigDecimal(dayLowStr);
            detail.setDayLow(dayLow);
            
            String volume = getFirstMatchedElementContent(table, XPATH_DAY_VOLUME);
            detail.setVolume(volume);
            
        } catch (ClientProtocolException e) {
            throw new DownloadException("error fetching stock", e);
        } catch (IOException e) {
            throw new DownloadException("error fetching stock", e);
        } catch (XPatherException e) {
            throw new ParseException("unexpected result while fetch stock", e);
        } catch (java.text.ParseException e) {
            throw new ParseException("date format unparsable", e);
        }
        
        return detail;
    }
    
    @Override
    public String getUrl(String quote) {
        return String.format(BASE_URL, quote);
    }
}
