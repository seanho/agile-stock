package hk.reality.stock.service.fetcher;

import static hk.reality.stock.service.fetcher.Utils.rounded;
import hk.reality.stock.R;
import hk.reality.stock.model.Index;
import hk.reality.stock.service.exception.DownloadException;
import hk.reality.stock.service.exception.ParseException;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

public class Money18IndexesFetcher extends BaseIndexesFetcher {
    private static final String DATE_FORMAT = "yyyy/MM/dd HH:mm";
    private Context context;
    
    public Money18IndexesFetcher(Context context) {
        this.context = context;
    }

    @Override
    public List<Index> fetch() throws DownloadException, ParseException {
        List<Index> indexes = new ArrayList<Index>();
        indexes.add(getHsi());
        indexes.addAll(getWorldIndexes());
        return indexes;
    }

    private Index getHsi() throws ParseException, DownloadException {
        try {
            Index hsi = new Index();
            HttpGet req = new HttpGet(getHSIURL());
            req.setHeader("Referer", "http://money18.on.cc/");
    
            HttpResponse resp = getClient().execute(req);
            String content = EntityUtils.toString(resp.getEntity());
            JSONObject json = preprocessJson(content);
            
            SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);
            Date updateTime = formatter.parse(json.getString("ltt"));
            Calendar updatedAt = Calendar.getInstance();
            updatedAt.setTime(updateTime);
            hsi.setUpdatedAt(updatedAt);
            
            double value = json.getDouble("value");
            double change = json.getDouble("difference");
            double changePercent = change * 100.0 / value;
            
            hsi.setName(getContext().getString(R.string.msg_hsi));
            hsi.setValue(new BigDecimal(json.getString("value")));
            hsi.setChange(new BigDecimal(rounded(change, 1000.0)));
            hsi.setChangePercent(new BigDecimal(rounded(changePercent, 100.0)));
    
            return hsi;
        } catch (org.apache.http.ParseException pe) {
            throw new ParseException("error parsing http data", pe);
        } catch (JSONException je) {
            throw new ParseException("error parsing http data", je);
        } catch (IOException ie) {
            throw new DownloadException("error parsing http data", ie);
        } catch (java.text.ParseException e) {
            throw new ParseException("error parsing json data", e);
        }
    }
    
    private String getHSIURL() {
        return "http://money18.on.cc/js/real/index/HSI_r.js";
    }
    
    private String getWorldIndexURL() {
        return "http://money18.on.cc/js/daily/worldidx/worldidx_b.js";
    }
    
    private JSONObject preprocessJson(String content) throws JSONException {
        int pos = content.indexOf('{');
        String result = StringUtils.substring(content, pos);
        JSONObject json = new JSONObject(result);
        return json;
    }
    
    private List<Index> getWorldIndexes()  throws ParseException, DownloadException {
        try {
            HttpGet req = new HttpGet(getWorldIndexURL());
            req.setHeader("Referer", "http://money18.on.cc/");
    
            HttpResponse resp = getClient().execute(req);
            String content = EntityUtils.toString(resp.getEntity(), "Big5");
            return getWorldIndexesFromJson(content);
        } catch (org.apache.http.ParseException pe) {
            throw new ParseException("error parsing http data", pe);
        } catch (JSONException je) {
            throw new ParseException("error parsing http data", je);
        } catch (IOException ie) {
            throw new DownloadException("error parsing http data", ie);
        }
    }
    
    private List<Index> getWorldIndexesFromJson(String content) throws JSONException {
        List<Index> indexes = new ArrayList<Index>();
        int start = content.indexOf('{');
        while (start > 0) {
            int end = content.indexOf(";", start);
            String result = StringUtils.substring(content, start, end);
            JSONObject json = new JSONObject(result);
            String name = json.getString("Name");
            String value = json.getString("Point");
            String diff = json.getString("Difference");

            Index index = new Index();
            index.setName(name);
            index.setValue(new BigDecimal(value));
            
            if (diff != null && !StringUtils.equalsIgnoreCase(diff, "null"))
                index.setChange(new BigDecimal(diff));
            
            if (diff != null && !StringUtils.equalsIgnoreCase(diff, "null"))
                index.setChangePercent(new BigDecimal(json.getDouble("Difference") - json.getDouble("Point")));

            indexes.add(index);
            start = content.indexOf('{', end);
        }
        return indexes;
    }

    /**
     * @return the context
     */
    public Context getContext() {
        return context;
    }

    /**
     * @param context the context to set
     */
    public void setContext(Context context) {
        this.context = context;
    }

}
