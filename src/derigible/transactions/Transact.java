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

import java.util.GregorianCalendar;

/**
 * @author marphill
 *
 * A Solid implementation of the Transaction object. For most use cases you will
 * want to use this object.
 */
public class Transact extends Splittable {

    private SubTransaction[] subTrans;
    private int arrayPoint = 0;
    private double amountAllocated = 0;

    /**
     * Empty constructor. Add data in later.
     */
    public Transact() {
        // do nothing
    }

    /**
     * Add data in now.
     *
     * @param d Date
     * @param desc Description
     * @param amount Amount
     * @param cat Category
     * @param account Account
     * @param doc Debit or Credit
     */
    public Transact(GregorianCalendar d, String desc, double amount,
            String cat, String account, boolean doc) {
        super.setDate(d);
        super.setDescription(desc);
        super.setAmount(amount);
        super.setCategory(cat);
        super.setAccount(account);
        super.setDebitOrCredit(doc);
    }

    /**
     * If this has been subdivided, will return the amount that has not been subdivided. If all
     * of the transaction has been subdivided, will return zero.
     * 
     * @return the amount
     */
    @Override
    public double getAmount() {
        return this.hasSubTransactions() ? this.getUndividedAmount() : super.getAmount();
    }
    
	@Override
	public SubTransaction[] getSubTransactions() {
		return subTrans;
	}
	
	@Override
	public boolean hasSubTransactions(){
		return subTrans != null ? (subTrans[0] != null ? true : false) : false;
	}

	@Override
	public void addSubTransaction(SubTransaction split) {
		if((amountAllocated + split.getAmount()) > super.getAmount()){
			split.setAmount(super.getAmount() - amountAllocated);
			split.addNote("SubTransaction amount truncated to fit allowed amount. Amount changed to: " + (super.getAmount() - amountAllocated));
		} 
		if(this.subTrans == null){
			subTrans = new SubTransaction[6];
		} else if(subTrans.length == arrayPoint + 1){ //Array is full, resize
			SubTransaction[] temp = new SubTransaction[subTrans.length + 3];
			for(int i = 0; i < subTrans.length; i++){
				temp[i] = subTrans[i];
			}
			subTrans = temp;
		} 
		subTrans[arrayPoint] = split;
		arrayPoint++;
		amountAllocated += split.getAmount();
	}
	
	@Override
	public void removeSubTransaction(SubTransaction split){
		removeSubTransaction(split.getGUID());
	}
	
	@Override
	public void removeSubTransaction(String GUID){
		for(int j = 0; j < subTrans.length; j++){
			if(subTrans[j] == null){ //GUID not found, must not be in subtransaction list
				break;
			}
			if(subTrans[j].getGUID().equals(GUID)){
				amountAllocated -= subTrans[j].getAmount();
				for(int i = j+1; i < subTrans.length; i++){
					subTrans[i - 1] = subTrans[i];
				}
				arrayPoint--;
				break;
			}
		}
	}

	@Override
	public void addSubTransaction(double amount) {
		this.addSubTransaction(new SubTransaction(this, amount));		
	}

	@Override
	public double getUndividedAmount() {
		return super.getAmount() - this.amountAllocated;
	}

	@Override
	public double getDividedAmount() {
		return this.amountAllocated;
	}

	@Override
	public void updateSubTransactions() {
		amountAllocated = 0;
		for(int i = 0; i < subTrans.length; i++){
			if(subTrans[i] == null){ //no more sub transactions in list
				break;
			}
			amountAllocated += subTrans[i].getAmount();
		}
	}

	@Override
	public SubTransaction getSubTransaction(SubTransaction split) {
		return this.getSubTransaction(split.getGUID());
	}

	@Override
	public SubTransaction getSubTransaction(String GUID) {
		for(int i = 0 ; i < subTrans.length; i++){
			if(subTrans[i] == null){ //GUID not found, must not be in subtransaction list
				break;
			}
			if(subTrans[i].getGUID().equals(subTrans[i].getGUID())){
				return subTrans[i];
			}
		}
		return null;
	}

	@Override
	public boolean isSubTransaction() {
		return false;
	}
}
