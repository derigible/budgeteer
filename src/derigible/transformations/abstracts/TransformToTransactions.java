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

package derigible.transformations.abstracts;

import java.io.IOException;

/**
 * @author marphill
 * 
 * This acts as a controller factory to the client. The controller will be the Transactions
 * object passed back from the data_to_transactions() method. This method
 * should be the only call made by the client to the underlying data transformation
 * layer.
 *
 * Because Transactions can also act as the controller to the model, it may not
 * be necessary to implement this class. Say, for example, that the data rests
 * in a database - use a Transactions class that makes use of this fact. This
 * Class is used primarily for the intent to transform flat files into usable data.
 */
public interface TransformToTransactions {
	
	/**
	 * All Transformation objects will return a Transactions object.
     * This is the only requirement for a Transformation object.
     * Note that the Transformation class should seek to populate
     * each transaction with a GUID if one is not already found.
     * 
	 * @return the transactions from the data store
	 * @throws IOException - if the data store does not match the basic requirements for data in the program
	 */
    public abstract derigible.transactions.abstracts.Transactions data_to_transactions() throws IOException;

}
