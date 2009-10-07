package hk.reality.stock.service.fetcher;

import hk.reality.stock.model.Stock;
import hk.reality.stock.model.StockDetail;
import android.os.AsyncTask;

public class QuoteUpdateTask extends AsyncTask<Stock, Void, Boolean> {
    public QuoteUpdateTask() {
    }

    @Override
    protected Boolean doInBackground(Stock... stocks) {
        QuoteFetcher fetcher = QuoteFetcherFactory.getQuoteFetcher();
        for(Stock s : stocks) {
            StockDetail detail = fetcher.fetch(s.getQuote());
            s.setDetail(detail);
        }
        return Boolean.TRUE;
    }
    
    

}
