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


package derigible.utils;

import java.io.File;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author marcphillips
 */
public class FileUTest {
   
    /**
     * Test of getFileInJavaProjectFolder method, of class FileU.
     */
    @Test
    public void testGetFileInJavaProjectFolder() throws Exception {
        String path = "testDocs/csvModified.csv";
        File expResult = null;
        File result = null;
        try{
        expResult = FileU.getFileInJavaProjectFolder(path);
        result = FileU.getFileInJavaProjectFolder(path);
        } catch (Exception e){
            e.printStackTrace();
        }
        assertNotSame("The two objects should not be the same.", expResult, result);
    }
}
