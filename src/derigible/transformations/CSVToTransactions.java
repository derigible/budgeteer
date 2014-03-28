package derigible.transformations;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.List;

import derigible.transactions.TList;
import derigible.transactions.Transact;
import derigible.transactions.Transaction;
import derigible.transactions.Transactions;
import au.com.bytecode.opencsv.CSVReader;
import derigible.utils.FileU;

public class CSVToTransactions implements Transformation {
	private CSVReader csv = null;
	
	public CSVToTransactions(File file){
		try {
			csv = new CSVReader(FileU.getFileReader(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public Transactions data_to_transactions() {
		List<String[]> lines = null;
		  try {
			lines = csv.readAll();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(lines.get(0).length == 9){
			return new TList(mint_comCSVToTransactions(csv, lines));
		} else {
			return new TList(alteredMint_comCSVToTransactions(csv, lines));
		}
		
	}
	
	private Transaction[] mint_comCSVToTransactions(CSVReader csv, List<String[]> lines){
		Transact[] trans = new Transact[lines.size()];
		for(int i = 0; i < lines.size(); i++){
			String[] column = lines.get(i);
			Transact t = new Transact();
			String[] dateData = column[0].split("/");
			int month = Integer.parseInt(dateData[0]);
			int day = Integer.parseInt(dateData[1]);
			int year = Integer.parseInt(dateData[2]);
			t.setDate(new GregorianCalendar(year, month, day));
			t.setDescription(column[1]);
			t.setOriginalDescription(column[2]);
			t.setAmount(Double.parseDouble(column[3]));
			if(column[4].equals("credit")){
				t.setDebitOrCredit(true);
			} 
			t.setCategory(column[5]);
			t.setAccount(column[6]);
		}
		return trans;
	}
	
	private Transaction[] alteredMint_comCSVToTransactions(CSVReader csv, List<String[]> lines){
		Transact[] trans = new Transact[lines.size()];
		for(String[] line : lines){
			for(String column : line){
				Transact t = new Transact();
				
			}
		}
		return trans;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
