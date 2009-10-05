package hk.reality.stock.service;

import hk.reality.stock.model.Portfolio;

import java.util.List;

public interface PortfolioService {
    void create(Portfolio portfolio);
    
    List<Portfolio> list();
    
    void update(Portfolio portfolio);
    
    void delete(Portfolio portfolio);

    
}
