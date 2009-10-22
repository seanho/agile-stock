package hk.reality.stock.service.fetcher;

import hk.reality.stock.model.Index;
import hk.reality.stock.service.exception.DownloadException;
import hk.reality.stock.service.exception.ParseException;

import java.util.List;

/**
 * Return list of stock indexes
 * @author siuying
 *
 */
public interface IndexesFetcher {
    List<Index> fetch() throws DownloadException, ParseException;
}
