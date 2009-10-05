package hk.reality.stock.service;

import hk.reality.stock.model.Stock;

public interface StockSearcher {
    enum Lang {
        ENG, CHI
    }

    Stock searchStock(String quote);

}
