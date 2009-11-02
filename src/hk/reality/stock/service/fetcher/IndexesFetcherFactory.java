package hk.reality.stock.service.fetcher;

import android.content.Context;

public class IndexesFetcherFactory {
    public static IndexesFetcher getIndexesFetcher(Context context) {
        return new Money18IndexesFetcher(context);
    }
}
