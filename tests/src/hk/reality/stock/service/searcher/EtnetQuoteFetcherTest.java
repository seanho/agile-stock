package hk.reality.stock.service.searcher;

import hk.reality.stock.model.StockDetail;
import hk.reality.stock.service.fetcher.EtnetQuoteFetcher;
import junit.framework.Assert;
import junit.framework.TestCase;
import android.util.Log;

public class EtnetQuoteFetcherTest extends TestCase {
    EtnetQuoteFetcher fetcher;
    
    protected void setUp() throws Exception {
        super.setUp();
        fetcher = new EtnetQuoteFetcher();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testFetch() {
        StockDetail detail = fetcher.fetch("0005");
        Assert.assertNotNull(detail);
        Assert.assertNotNull(detail.getName());
        Assert.assertNotNull(detail.getPrice());
        Assert.assertNotNull(detail.getChangePrice());
        Assert.assertNotNull(detail.getChangePricePercent());
        Assert.assertNotNull(detail.getDayHigh());
        Assert.assertNotNull(detail.getDayLow());
        Assert.assertNotNull(detail.getVolume());
        Assert.assertNotNull(detail.getUpdatedAt());
        
        Log.d("Etnet", "name: " + detail.getName());
        Log.d("Etnet", "price: " + detail.getPrice().toPlainString());
        Log.d("Etnet", "change: " + detail.getChangePrice().toPlainString());
        Log.d("Etnet", "change %: " + detail.getChangePricePercent().toPlainString());
        Log.d("Etnet", "high/low: " + detail.getDayHigh().toPlainString() + "/" + detail.getDayLow().toPlainString());
        Log.d("Etnet", "vol: " + detail.getVolume());
        Log.d("Etnet", "updated: " + detail.getUpdatedAt());
    }

    public void testGetUrl() {
        String url = fetcher.getUrl("0005");
        Assert.assertNotNull(url);
        Assert.assertTrue(url.startsWith("http"));
        Assert.assertTrue(url.contains("=0005"));
    }

}
