package hk.reality.stock.service.fetcher;

import static hk.reality.stock.service.fetcher.Utils.rounded;
import hk.reality.stock.model.Index;
import hk.reality.stock.service.exception.DownloadException;
import hk.reality.stock.service.exception.ParseException;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class Money18IndexesFetcher extends BaseIndexesFetcher {
    public Money18IndexesFetcher() {  
    }

    @Override
    public List<Index> fetch() throws DownloadException, ParseException {
        List<Index> indexes = new ArrayList<Index>();
        indexes.add(getHsi());
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
            
            double value = json.getDouble("value");
            double change = json.getDouble("difference");
            double changePercent = change * 100.0 / value;
            
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

}
