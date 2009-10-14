package hk.reality.stock.service.fetcher;

import hk.reality.stock.model.StockDetail;
import hk.reality.stock.service.exception.DownloadException;
import hk.reality.stock.service.exception.ParseException;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Money18QuoteFetcher extends BaseQuoteFetcher {
    private static final String TAG = "Money18QuoteFetcher";
    private static final String DATE_FORMAT = "yyyy/MM/dd HH:mm";
    private static final String DATE_PARAM_FORMAT = "yyyyMMdd";
    
    @Override
    public StockDetail fetch(String quote) throws DownloadException, ParseException {
        StockDetail d = new StockDetail();
        String content = null;
        HttpGet openReq = new HttpGet(getOpenUrl(quote));
        try {            
            openReq.setHeader("Referer", "http://money18.on.cc/");
            HttpResponse resp = getClient().execute(openReq);
            content = EntityUtils.toString(resp.getEntity());
            JSONObject json = preprocessJson(content);
            double preClosePrice = json.getDouble("preCPrice");

            HttpGet req = new HttpGet(getUpdateUrl(quote));
            req.setHeader("Referer", "http://money18.on.cc/");
            resp = getClient().execute(req);
            content = EntityUtils.toString(resp.getEntity());
            json = preprocessJson(content);
            
            double price = json.getDouble("np");
            double change = price - preClosePrice;
            double changePercent = (price - preClosePrice) * 100.0 / preClosePrice;
            
            Log.i(TAG, "change and change percent: " + change + ", " + changePercent + 
                    ". preClose: " + preClosePrice + ", price =" + price);
            d.setPrice(new BigDecimal(json.getString("np")));
            d.setChangePrice(new BigDecimal(rounded(change, 1000.0)));
            d.setChangePricePercent(new BigDecimal(rounded(changePercent, 100.0)));
            d.setDayHigh(new BigDecimal(json.getString("dyh")));
            d.setDayLow(new BigDecimal(json.getString("dyl")));
            d.setQuote(quote);
            d.setSourceUrl(getUrl(quote));
            
            SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
            Date updateTime = formatter.parse(json.getString("ltt"));
            Calendar updatedAt = Calendar.getInstance();
            updatedAt.setTime(updateTime);
            d.setUpdatedAt(updatedAt);
            d.setVolume(new BigDecimal(json.getString("vol")).toPlainString());
            
            return d;
        } catch (ClientProtocolException e) {
            openReq.abort();
            throw new DownloadException("protocol exception", e);
        } catch (IOException e) {
            openReq.abort();
            throw new DownloadException("download stock error", e);
        } catch (JSONException e) {
            openReq.abort();
            throw new ParseException("unexpected return value," +
                    " content = " + content, e);
        } catch (java.text.ParseException e) {
            openReq.abort();
            throw new ParseException("failed to parse date format," +
            		" content = " + content, e);
        }
    }
    
    private String rounded(double value, double ratio) {
        return String.format("%.3f", (Math.round(value * ratio) / ratio));
    }
    
    private JSONObject preprocessJson(String content) throws JSONException {
        int pos = content.indexOf('{');
        String result = StringUtils.substring(content, pos);
        JSONObject json = new JSONObject(result);
        return json;
    }

    @Override
    public String getUrl(String quote) {
        return String.format("http://money18.on.cc/info/liveinfo_quote.html?symbol=%s", quote);
    }
    
    private String getOpenUrl(String quote) {
        SimpleDateFormat formatter = new SimpleDateFormat(DATE_PARAM_FORMAT);
        Calendar cal = Calendar.getInstance();
        return String.format("http://money18.on.cc/js/daily/quote/%s_d.js?t=%s", 
                quote, formatter.format(cal.getTime()));
    }
    
    private String getUpdateUrl(String quote) {
        return String.format("http://money18.on.cc/js/real/quote/%s_r.js", quote);
    }

}
