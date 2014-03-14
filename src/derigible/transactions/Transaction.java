/**
 * 
 */
package derigible.transactions;

import java.util.Date;

/**
 * @author marphill
 *	An interface to define transaction objects to be used in the Transactions
 *	object. Since it is unclear at this point how a Transaction may need to be
 *	stored, it is best to just define an interface and let the implementation
 *	determine the structure.
 *
 *	Some Transactions classes can use this to call themselves to get data.
 */
public interface Transaction {
	public abstract Date getDate();
	public abstract String getDescription();
	public abstract double getAmount();
	public abstract String getCategory();
	public abstract String getAccount();
	public abstract boolean isDebitOrCredit();
}
