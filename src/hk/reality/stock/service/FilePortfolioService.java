package hk.reality.stock.service;

import hk.reality.stock.model.Portfolio;
import hk.reality.stock.model.Stock;
import hk.reality.stock.service.exception.StorageException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import org.apache.commons.io.IOUtils;

public class FilePortfolioService implements PortfolioService {
	private static final String STORAGE_FILE = "portfolio.xml";
	private File baseDirectory;
	private List<Portfolio> portfolios;
	
	public FilePortfolioService(File directory) {
		this.baseDirectory = directory;
		this.portfolios = new Vector<Portfolio>();
	}

	@Override
	public void create(Portfolio portfolio) {

	}

	@Override
	public void delete(Portfolio portfolio) {

	}

	@Override
	public List<Portfolio> list() {
		if (this.portfolios.size() == 0) {
			Portfolio p = new Portfolio();
			p.setName("My Portfolio");
			p.setStocks(new ArrayList<Stock>());
			this.portfolios.add(p);
		}
		return Collections.unmodifiableList(this.portfolios);
	}

	@Override
	public void update(Portfolio portfolio) {

	}
	
	private void save() {
		File store = new File(baseDirectory, STORAGE_FILE);
		Writer writer = null;
		try {
			if (!store.exists()) {
				store.createNewFile();
			}
			
			writer = new BufferedWriter(new FileWriter(store));
			String xml = PortfolioSerializer.toXML(portfolios);
			IOUtils.write(xml, writer);
		
		} catch (IOException e) {
			throw new StorageException("failed storing datafile", e);
		
		} finally {
			IOUtils.closeQuietly(writer);
		
		}
	}
}