/**
 * 
 */
package derigible.transformations;

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
public interface Transformation {
	
	/**
	 * All Transformation objects will return a Transactions object.
     * This is the only requirement for a Transformation object.
     * 
	 * @return the transactions from the data store
	 * @throws IOException - if the data store does not match the basic requirements for data in the program
	 */
    public abstract derigible.transactions.Transactions data_to_transactions() throws IOException;

}
