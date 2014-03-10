/**
 * 
 */
package derigible.transformations;

/**
 * @author marphill
 *
 */
public interface Transformation {
	
	 /**
     * All Transformation objects will return a Transactions object.
     * This is the only requirement for a Transformation object.
     */
    public abstract derigible.transactions.Transactions data_to_transactions();

}
