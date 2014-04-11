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
package derigible.transformations;

import java.io.IOException;

import derigible.transactions.Transactions;

/**
 * @author marcphillips
 *
 * This acts as a controller factory to the client. The controller will be the storage
 * of all transactions back into whatever medium is using this interface.
 *
 * Because Transactions can also act as the controller to the model, it may not
 * be necessary to implement this class. Say, for example, that the data rests
 * in a database - use a Transactions class that makes use of this fact. This
 * Class is used primarily for the intent to transform flat files into usable data.
 */
public interface TransformToStorage {

	/**
	 * All classes that implement this interface use this method to push all
	 * transactions in the Transactions object to storage. This is the only
	 * defined method of a TransformToStorage object.
	 */
    public abstract void transactions_to_storage(Transactions list) throws IOException;
}
