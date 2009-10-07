package hk.reality.stock.service.fetcher;

public class QuoteFetcherFactory {
    public static QuoteFetcher getQuoteFetcher() {
        return new Money18QuoteFetcher();
    }
}
