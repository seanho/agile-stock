package hk.reality.stock.service.fetcher;

import junit.framework.TestCase;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Money18QuoteFetcherTest extends TestCase {

    protected void setUp() throws Exception {
        super.setUp();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testJson() throws JSONException {
        String jsonSrc = "M18.r_00005 = {    ltt: '2009/10/07 16:00',    np: '87.450',    iep: '0.000',    iev: '00000000000',    ltp: '87.600',    vol: '00025255927',    tvr: '02204863352',    dyh: '87.700',    dyl: '86.850'};";
        int pos = jsonSrc.indexOf('{');
        String result = StringUtils.substring(jsonSrc, pos);
        JSONObject json = new JSONObject(result);
        
        Log.d("TEST", json.toString(2));

    }

}
