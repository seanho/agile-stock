package hk.reality.stock.service.searcher;

import hk.reality.stock.model.Portfolio;
import hk.reality.stock.model.Stock;
import hk.reality.stock.model.StockDetail;
import hk.reality.stock.service.PortfolioSerializer;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import junit.framework.Assert;
import junit.framework.TestCase;
import android.util.Log;

public class PortfolioSerializerTest extends TestCase {

    public void testToXML() {
        ArrayList<Portfolio> port = new ArrayList<Portfolio>();
        String xml = PortfolioSerializer.toXML(port);
        List<Portfolio> decodedPort = PortfolioSerializer.fromXML(xml);
        Assert.assertEquals("<list/>", xml);
        Assert.assertEquals(0, decodedPort.size());

        Portfolio p = new Portfolio();
        p.setId(UUID.randomUUID().toString());
        p.setName("Sample Portfolio");
        p.setStocks(new ArrayList<Stock>());
        port.add(p);
        
        Stock s1 = new Stock();
        s1.setQuote("8");        
        p.getStocks().add(s1);
        
        xml = PortfolioSerializer.toXML(port);
        Assert.assertNotSame("<list/>", xml);
        decodedPort = PortfolioSerializer.fromXML(xml);
        Assert.assertEquals(1, decodedPort.size());
        
        Portfolio thePortfolio = decodedPort.get(0);
        Assert.assertEquals(p, thePortfolio);
        Assert.assertEquals(p.getStocks(), thePortfolio.getStocks());
        Log.d("PortfolioSerTest", thePortfolio.getName());

    }


}
