package hk.reality.stock.service;

import hk.reality.stock.service.StockSearcher.Lang;

public class StockSearcherFactory {
    public static StockSearcher getDefaultStockSearcher(String lang) {
        return new HkexStockSearcher(Lang.valueOf(lang));
    }
}
