package hk.reality.stock.service.fetcher;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class Utils {
    public static String rounded(double value, double ratio) {
        return String.format("%.3f", (Math.round(value * ratio) / ratio));
    }
    
    public static JSONObject preprocessJson(String content) throws JSONException {
        int pos = content.indexOf('{');
        String result = StringUtils.substring(content, pos);
        JSONObject json = new JSONObject(result);
        return json;
    }

}
