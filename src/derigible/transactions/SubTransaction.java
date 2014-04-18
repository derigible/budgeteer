/**
 * 
 */
package derigible.transactions;

import java.util.GregorianCalendar;

/**
 * @author marcphillips
 * This class is used for holding transactions for budgets. A transaction is held within this 
 * container, but the ability to partition how much of the transaction is 
 * placed in different budgets is added.
 *
 */
public class SubTransaction implements Transaction {
	private Transaction t;
	
	public SubTransaction(Transact t){
		this.t = t;
		t.addSubTransactions(this);
	}
	
	//More constructors for different Transaction types here.

	/* (non-Javadoc)
	 * @see derigible.transactions.Transaction#getDate()
	 */
	@Override
	public GregorianCalendar getDate() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see derigible.transactions.Transaction#getDescription()
	 */
	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see derigible.transactions.Transaction#getOriginalDescription()
	 */
	@Override
	public String getOriginalDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see derigible.transactions.Transaction#getAmount()
	 */
	@Override
	public double getAmount() {
		// TODO Auto-generated method stub
		return 0;
	}

	/* (non-Javadoc)
	 * @see derigible.transactions.Transaction#getCategory()
	 */
	@Override
	public String getCategory() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see derigible.transactions.Transaction#getAccount()
	 */
	@Override
	public String getAccount() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see derigible.transactions.Transaction#isCredit()
	 */
	@Override
	public boolean isCredit() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see derigible.transactions.Transaction#isExcluded()
	 */
	@Override
	public boolean isExcluded() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see derigible.transactions.Transaction#setExcluded(boolean)
	 */
	@Override
	public void setExcluded(boolean exclude) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see derigible.transactions.Transaction#setGUID(java.lang.String)
	 */
	@Override
	public void setGUID(String guid) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see derigible.transactions.Transaction#getGUID()
	 */
	@Override
	public String getGUID() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see derigible.transactions.Transaction#getNotes()
	 */
	@Override
	public String getNotes() {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see derigible.transactions.Transaction#addNote(java.lang.String)
	 */
	@Override
	public void addNote(String note) {
		// TODO Auto-generated method stub

	}

}
