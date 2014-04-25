/**
 * 
 */
package derigible.controller;

import java.io.IOException;

import derigible.transactions.Transactions;

/**
 * @author marcphillips
 * 
 * A controller that will 
 *
 */
class BudgetController extends AbstractController {
	private final String NAME;

	/**
	 * This class is made only by the TransactionsContoller.
	 */
	protected BudgetController(Transactions t, String name){
		//Only constructable by the TransactionsController.
		super.tlist = t;
		NAME = name;
	}

	@Override
	public void transactionsToCSV(String filename, boolean toAppStorage) {
		// TODO Auto-generated method stub
		
	}
}
