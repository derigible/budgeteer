/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package derigible.utils;

import java.io.File;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
//import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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
        System.out.println("getFileInJavaProjectFolder");
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
