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
 *	This class is used for the specific purpose of transporting
 *	updated transaction information to a transaction object.
 *
 */
public class TransUpdater extends Transaction {

	/* (non-Javadoc)
	 * @see derigible.transactions.Transaction#hasSubTransactions()
	 */
	@Override
	public boolean hasSubTransactions() {
		return false;
	}

	/* (non-Javadoc)
	 * @see derigible.transactions.Transaction#isSubTransaction()
	 */
	@Override
	public boolean isSubTransaction() {
		return false;
	}

}
