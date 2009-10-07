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

public class Money18QuoteFetcher extends BaseQuoteFetcher {
    private static final String DATE_FORMAT = "yyyy/MM/dd HH:mm";
    
    @Override
    public StockDetail fetch(String quote) {
        StockDetail d = new StockDetail();
        try {
            HttpGet openReq = new HttpGet(getOpenUrl(quote));
            openReq.setHeader("Header", "http://money18.on.cc");
            HttpResponse resp = getClient().execute(openReq);
            String content = EntityUtils.toString(resp.getEntity());
            JSONObject json = preprocessJson(content);
            double openPrice = json.getDouble("openPrice");

            HttpGet req = new HttpGet(getUpdateUrl(quote));
            req.setHeader("Header", "http://money18.on.cc");
            resp = getClient().execute(req);
            content = EntityUtils.toString(resp.getEntity());
            json = preprocessJson(content);
            
            double price = json.getDouble("np");
            double change = openPrice - price;
            double changePercent = (openPrice - price) / openPrice;
            
            d.setPrice(new BigDecimal(json.getString("np")));
            d.setChangePrice(new BigDecimal("" + change));
            d.setChangePricePercent(new BigDecimal("" + changePercent));
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
            throw new DownloadException("protocol exception", e);
        } catch (IOException e) {
            throw new DownloadException("download stock error", e);
        } catch (JSONException e) {
            throw new ParseException("unexpected return value", e);
        } catch (java.text.ParseException e) {
            throw new ParseException("failed to parse date format", e);
        }
        return null;
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
        return String.format("http://money18.on.cc/js/real/opening/%s_o.js", quote);
    }
    
    private String getUpdateUrl(String quote) {
        return String.format("http://money18.on.cc/js/real/ote/%s_r.js", quote);
    }

}
