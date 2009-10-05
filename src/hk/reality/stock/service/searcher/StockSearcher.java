package hk.reality.stock.service.searcher;

import hk.reality.stock.model.Stock;
import hk.reality.stock.service.Lang;

public interface StockSearcher {
    void setLanguage(Lang language);

    Stock searchStock(String quote);

}
