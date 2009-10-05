package hk.reality.stock.service;

import hk.reality.stock.model.Stock;

public interface StockSearcher {
    void setLanguage(Lang language);

    Stock searchStock(String quote);

}
