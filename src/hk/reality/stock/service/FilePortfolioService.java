package hk.reality.stock.service;

import hk.reality.stock.model.Portfolio;
import hk.reality.stock.service.exception.StorageException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.Vector;

import org.apache.commons.io.IOUtils;

import android.util.Log;

public class FilePortfolioService implements PortfolioService {
	private static final String STORAGE_FILE = "portfolio.xml";
	private static final String TAG = "FilePortfolioService";
	private File baseDirectory;
	private List<Portfolio> portfolios;
	
	public FilePortfolioService(File directory) {
		Log.i(TAG, "init portfolio service: " + directory.getAbsolutePath());
		this.baseDirectory = directory;
		this.portfolios = loadOrCreateNew();
	}

	@Override
	public void create(Portfolio p) {
		Log.i(TAG, "create portfolio: " + p.getName());
		p.setId(UUID.randomUUID().toString());
		portfolios.add(p);
		save();
	}

	@Override
	public void delete(Portfolio p) {
		Log.i(TAG, "delete portfolio: " + p.getName());
		portfolios.remove(p);
		save();
	}

	@Override
	public List<Portfolio> list() {
		Log.i(TAG, "list portfolio ");
		return Collections.unmodifiableList(this.portfolios);
	}

	@Override
	public void update(Portfolio p) {
		Log.i(TAG, "update portfolio: " + p.getName());
		
		int pos = portfolios.indexOf(p);
		if (pos > -1) {
			Portfolio orig = portfolios.get(pos);
			orig.setName(p.getName());
			orig.setStocks(p.getStocks());
		} else {
			throw new IllegalArgumentException("record not found");
		}
		save();
	}
	
	private List<Portfolio> loadOrCreateNew() {
        Log.i(TAG, "loading portfolio file ...");
        File store = new File(baseDirectory, STORAGE_FILE);
        Reader reader = null;
        try {
            if (!store.exists()) {
                return new Vector<Portfolio>();
            }
            
            reader = new FileReader(store);
            return PortfolioSerializer.fromXML(reader);
        } catch (IOException e) {
            throw new StorageException("failed storing datafile", e);
        
        } finally {
            IOUtils.closeQuietly(reader);
        
        }
	}
	
	private void save() {
	    Log.i(TAG, "saving portfolio file ...");
		File store = new File(baseDirectory, STORAGE_FILE);
		Writer writer = null;
		try {
			if (!store.exists()) {
				store.createNewFile();
			}
			
			writer = new BufferedWriter(new FileWriter(store));
			String xml = PortfolioSerializer.toXML(portfolios);
			IOUtils.write(xml, writer);
			Log.i(TAG, "save completed");

		} catch (IOException e) {
			throw new StorageException("failed storing datafile", e);
		
		} finally {
			IOUtils.closeQuietly(writer);
		
		}
	}
}