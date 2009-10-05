package hk.reality.stock.service.searcher;

import hk.reality.stock.model.Stock;
import hk.reality.stock.service.Lang;
import junit.framework.Assert;
import junit.framework.TestCase;

public class HkexStockSearcherTest extends TestCase {
    HkexStockSearcher searcher;
    
    protected void setUp() throws Exception {
        searcher = new HkexStockSearcher(Lang.CHI);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testSearchStock() {
        Stock stock5 = searcher.searchStock("00005");
        Assert.assertNotNull(stock5);
        Assert.assertNotNull(stock5.getName());
        Assert.assertNotNull(stock5.getQuote());
        Assert.assertEquals("5", stock5.getQuote());

        searcher.setLanguage(Lang.ENG);
        Stock stock8 = searcher.searchStock("8");
        Assert.assertNotNull(stock8);
        Assert.assertNotNull(stock8.getName());
        
        Assert.assertNotNull(stock8.getQuote());
        Assert.assertEquals("8", stock8.getQuote());        
    }

}
