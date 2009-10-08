package hk.reality.stock.service.fetcher;

import hk.reality.stock.model.StockDetail;
import hk.reality.stock.service.exception.DownloadException;
import hk.reality.stock.service.exception.ParseException;

public interface QuoteFetcher {
    String getUrl(String quote);

    StockDetail fetch(String quote) throws DownloadException, ParseException;
}
