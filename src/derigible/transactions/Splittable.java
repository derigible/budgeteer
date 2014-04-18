/**
 * 
 */
package derigible.transactions;

/**
 * @author marcphillips
 *	
 *	Defines the methods needed for a transaction to be splittable (or 
 *	to be split up into sub transactions.
 */
public interface Splittable {
	
	/**
	 * Remove a sub transaction from the list. Will do nothing if sub transaction not in list.
	 * 
	 * @param split the sub transaction to remove
	 */
	public abstract void removeSubTransaction(SubTransaction split);
	
	/**
	 * Remove sub transaction from the list by the GUID. Will do nothing if sub transaction not in list.
	 * 
	 * @param GUID the GUID of the sub transaction to remove.
	 */
	public void removeSubTransaction(String GUID);
}
