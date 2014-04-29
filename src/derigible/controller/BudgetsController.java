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
package derigible.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * @author marcphillips
 *
 *	Basically a container for each budget that is created by a TransactionsController.
 *	Due to the fact that Budgets are only ever created by a TransactionsController,
 *	this class is also tied to a TransactionsController, and BudgetControllers
 *  should really only be created from within this class.
 */
public class BudgetsController {
	private final TransactionsController tc;
	private HashMap<String, BudgetController> budgets = new HashMap<String, BudgetController>();

	/**
	 * Creates a new instance of BudgetsController that is bound to a TransactionsController.
	 * 
	 * @param tc the TransactionsController
	 */
	public BudgetsController(TransactionsController tc){
		this.tc = tc;
	}
	
	/**
	 * Create a new budget with the given budget name. The name must be unique or an 
	 * IOException will be thrown.
	 * 
	 * @param budgetName the name of the budget
	 * @return the BudgetController for the budget
	 * @throws IOException budgetName is already in BudgetsController
	 */
	public BudgetController createNewBudget(String budgetName) throws IOException{
		if(budgets.containsKey(budgetName)){
			throw new IOException("Budget already exists. Create a new budget name.");
		}
		BudgetController b = tc.createBudget(budgetName);
		budgets.put(budgetName, b);
		return b;
	}
	
	/**
	 * Create a budget from a budget file. Give it a name, preferrably the name of the old
	 * budget. The name must be unique or an IOException will be thrown.
	 * 
	 * @param budgetName the name of the budget
	 * @param file the file that the budget is housed in
	 * @return the BudgetController for the budget
	 * @throws IOException budgetName is already in BudgetsController
	 */
	public BudgetController readInBudget(String budgetName, File file) throws IOException{
		if(budgets.containsKey(budgetName)){
			throw new IOException("Budget already exists. Create a new budget name.");
		}
		BudgetController b = tc.createBudgetFromFile(budgetName, file);
		budgets.put(budgetName, b);
		return b;
	}
	
	public void budgetsToCSV(){
		//TODO write this
	}
}
