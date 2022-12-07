/*******************************************************************************
// * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Copyright ©2020 Gary F. Pollice
 *******************************************************************************/

package wool.lexparse;

import static org.junit.jupiter.api.Assertions.*;
import java.io.*;
import java.util.*;
import java.util.stream.Stream;
import javax.swing.JFrame;
import org.antlr.v4.gui.TreeViewer;
import org.antlr.v4.runtime.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import wool.testutil.WoolTestRunner;
import wool.utility.WoolRunnerImpl;

/**
 * Description
 */
class WoolParserTest extends WoolTestRunner
{
    /**
     * Clear the state for the new test
     * @throws java.lang.Exception
     */
    @BeforeEach
    void setUp() throws Exception
    {
        parser = null;
        tree = null;
        runner = testRunner = new WoolRunnerImpl();
    }
    
    @ParameterizedTest
    @ValueSource( strings = {
        "tests-pass/"
    })
    void testPassDir(String folderStr) throws IOException
    {
    	System.out.println("--- STARTING SUCCESS TESTS ------");
    	File folder = new File(folderStr);
    	for (String file : folder.list()) {
    		System.out.println("Started: "+file);
    		//System.out.println(CharStreams.fromFileName("tests-pass/" + f).toString());
    		doParse(CharStreams.fromFileName(folderStr + file));
    		assertNotNull(tree);
            System.out.println("Passed: "+file);
            try {
				setUp();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	System.out.println("---- SUCCESS TESTS PASSED ------");
    	
        
        
    }
    @ParameterizedTest
    @ValueSource( strings = {
        "tests-fail/"
    })
    void testFailDir(String folderStr) throws IOException
    {
    	System.out.println("---- STARTED FAIL TESTS -----");

    	File folder = new File(folderStr);
    	for (String file : folder.list()) {
    		System.out.println("Started: "+file);
    		//System.out.println(CharStreams.fromFileName("tests-pass/" + f).toString());
    		try {
    		doParse(CharStreams.fromFileName(folderStr + file));
    		}catch(Exception e) {
    			
    		}
    		assertNull(tree);
            System.out.println("Passed: "+file);
            try {
				setUp();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	System.out.println("---- FAILURE TESTS PASSED ------");
        
        
    }
   
}
