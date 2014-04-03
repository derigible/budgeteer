/*******************************************************************************
 * Copyright (c) 2014 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package derigible.welcome;

import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.Color;
import org.eclipse.wb.swing.FocusTraversalOnArray;
import java.awt.Component;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import javax.swing.ImageIcon;

public class splash {

	private JFrame frmBudgeteerWelcome;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					splash window = new splash();
					window.frmBudgeteerWelcome.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public splash() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmBudgeteerWelcome = new JFrame();
		frmBudgeteerWelcome.getContentPane().setBackground(Color.WHITE);
		frmBudgeteerWelcome.setTitle("Budgeteer - Welcome!");
		frmBudgeteerWelcome.setForeground(Color.GREEN);
		frmBudgeteerWelcome.setBounds(100, 100, 889, 800);
		frmBudgeteerWelcome.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JLabel lblNewLabel = new JLabel("New label");
		lblNewLabel.setLabelFor(frmBudgeteerWelcome.getContentPane());
		lblNewLabel.setIcon(new ImageIcon("C:\\Users\\marphill\\Desktop\\darnel2.jpg"));
		frmBudgeteerWelcome.getContentPane().add(lblNewLabel, BorderLayout.NORTH);
		frmBudgeteerWelcome.getContentPane().setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{lblNewLabel}));
		frmBudgeteerWelcome.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{frmBudgeteerWelcome.getContentPane()}));
	}

}
