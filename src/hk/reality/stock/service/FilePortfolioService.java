package hk.reality.stock.service;

import hk.reality.stock.model.Portfolio;

import java.io.File;
import java.util.List;

public class FilePortfolioService implements PortfolioService {
	private File baseDirectory;
	
	public FilePortfolioService(File directory) {
		this.baseDirectory = directory;
	}

	@Override
	public void create(Portfolio portfolio) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(Portfolio portfolio) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Portfolio> list() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(Portfolio portfolio) {
		// TODO Auto-generated method stub

	}

}
