package hk.reality.stock.service.fetcher;

import org.apache.http.client.HttpClient;

import hk.reality.stock.model.StockDetail;
import hk.reality.stock.service.exception.DownloadException;
import hk.reality.stock.service.exception.ParseException;

public interface QuoteFetcher {
    HttpClient getClient();
    
    String getUrl(String quote);

    StockDetail fetch(String quote) throws DownloadException, ParseException;
}
