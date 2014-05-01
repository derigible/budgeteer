/*******************************************************************************
 * Copyright (c) 2014 Derigible Enterprises.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Derigible Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.derigible.com/license
 *
 * Contributors:
 *     Derigible Enterprises - initial API and implementation
 *******************************************************************************/
package derigible.transactions;

/**
 * @author marcphillips
 *	
 *	Defines the methods needed for a transaction to be splittable (or 
 *	to be split up into sub transactions.
 */
public interface Splittable extends Transaction {
	
	/**
	 * Get the sub-transactions associated with this transaction, if any.
	 * 
	 * @return the sub transactions
	 */
	public abstract SubTransaction[] getSubTransactions();
	
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
	public abstract void removeSubTransaction(String GUID);
	
	/**
	 * Add a subtransaction object to the Splittable.
	 * 
	 * @param split the subtransaction to add
	 */
	public abstract void addSubTransaction(SubTransaction split);
	
	/**
	 * Creates a new subtransaction object of the amount specified.
	 * 
	 * @param amount the amount of the subtransaction
	 */
	public abstract void addSubTransaction(double amount);
	
	/**
	 * Get the amount of the transaction that has not yet been placed in a subtransaction.
	 * 
	 * @return the amount undivided
	 */
	public abstract double getUndividedAmount();
	
	/**
	 * Get the amount of the transaction that has been placed in a subtransaction.
	 * 
	 * @return the amount divided
	 */
	public abstract double getDividedAmount();
	
	/**
	 * Update the subtransactions list, including any variables that may contain the
	 * undivided amount and the divided amount.
	 * 
	 * @param split the subtransaction that is causing the update
	 */
	public abstract void updateSubTransactions();
	
	/**
	 * Get the subtransaction defined. Returns null if not found.
	 * 
	 * @param split the subtransaction to get
	 */
	public abstract SubTransaction getSubTransaction(SubTransaction split);
	
	/**
	 * Get the subtransaction with the given GUID. Returns null if not found.
	 * 
	 * @param GUID the guid of the subtransaction to get
	 */
	public abstract SubTransaction getSubTransaction(String GUID);
	
}
