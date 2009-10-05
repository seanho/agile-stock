package hk.reality.stock.service.searcher;

import hk.reality.stock.service.Lang;


public class StockSearcherFactory {
    public static StockSearcher getDefaultStockSearcher(String lang) {
        return new HkexStockSearcher(Lang.valueOf(lang));
    }
}
