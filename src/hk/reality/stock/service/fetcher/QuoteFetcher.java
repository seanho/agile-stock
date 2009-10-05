package hk.reality.stock.service.fetcher;

import hk.reality.stock.model.StockDetail;

public interface QuoteFetcher {
    String getUrl(String quote);

    StockDetail fetch(String quote);
}
