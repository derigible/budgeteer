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

	/**
	 * This class is made only by the TransactionsContoller.
	 */
	protected BudgetController(Transactions t){
		//Only constructable by the TransactionsController.
		super.tlist = t;
	}
}
