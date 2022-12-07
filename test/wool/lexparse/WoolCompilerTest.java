package wool.lexparse;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.File;
import java.io.IOException;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import wool.structure.Program;
import wool.structure.WoolException;
import wool.testutil.WoolTestRunner;
import wool.utility.CompilationTreeWalker;
import wool.utility.WoolRunnerImpl;

class WoolCompilerTest extends WoolTestRunner
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
        "compile-tests-pass/",
        "compile-tests-fail/"
    })
    void testSemanticPassDir(String folderStr) throws IOException
    {
    	System.out.println("--- STARTING SEMANTIC TESTS ------");
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
    	System.out.println("---- SEMANTIC TESTS PASSED ------");

        
    }
    @ParameterizedTest
    @ValueSource( strings = {
        "compile-tests-pass/",
        "nick-valid/"
    })
    void testPassDir(String folderStr) throws IOException
    {
    	System.out.println("--- COMPILE SUCCESS TESTS ------"+folderStr);
    	File folder = new File(folderStr);
    	for (String file : folder.list()) {
    		System.out.println("Started: "+file);
    		//System.out.println(CharStreams.fromFileName("tests-pass/" + f).toString());
    		doParse(CharStreams.fromFileName(folderStr + file));
    		assertNotNull(tree);
    		 CompilationTreeWalker pt = new CompilationTreeWalker();

	        Program.initialize();
	        ParseTreeWalker walker = new ParseTreeWalker();
	        walker.walk(pt, tree);
	        
	        try {
	        	
	        	 if (!Program.verifyInheritence()){
	        		 assertNotNull(null);
	        	 }
	        	  if(!Program.setParentsScopes()) {
	        		  assertNotNull(null);
	        	  }
	        	  Program.setSelfTypes();
	          //    Program.printClasses();
	              Program.isTypeSatisfied();
	        }catch (Exception e) {
	        	
	        	e.printStackTrace();
	        	Program.printClasses();
	        	assertNotNull(null);
	        } 
            System.out.println("Passed: "+file);
            try {
				setUp();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	System.out.println("---- COMPILE SUCCESS TESTS PASSED ------");

        
    }
    @ParameterizedTest
    @ValueSource( strings = {
        "compile-tests-fail/",
        "noah-type-invalid/",
        "noah-symbol-invalid/",
        "nick-invalid/"
    })
    void testFailDir(String folderStr) throws IOException
    {
    	System.out.println("---- COMPILE FAIL TESTS ----- "+folderStr);

    	File folder = new File(folderStr);
    	for (String file : folder.list()) {
    		System.out.println("Started: "+file);
    		//System.out.println(CharStreams.fromFileName("tests-pass/" + f).toString());
    		boolean failure = false;
    		try {
    		doParse(CharStreams.fromFileName(folderStr + file));
    		}catch(Exception e) {
    			failure=true;
    		}
    		if (!failure) {
	    		//assertNotNull(tree);
	    		CompilationTreeWalker pt = new CompilationTreeWalker();
	
	 	        Program.initialize();
	 	        ParseTreeWalker walker = new ParseTreeWalker();
	 	        walker.walk(pt, tree);
	 	        
	 	        try {
	 	        	
	 	        	 if (!Program.verifyInheritence()){
	 	        		 failure=true;
	 	        	 }
	 	        	  if(!failure & !Program.setParentsScopes()) {
	 	        		  failure=true;
	 	        	  }
	 	        	  if (!failure) {
	 	        		 Program.setSelfTypes();
	 	   	          //    Program.printClasses();
	 	   	              Program.isTypeSatisfied();
	 	        	  }
	 	        	  
	 	        }catch (WoolException e) {
	 	        	//e.printStackTrace();
	 	        	failure=true;
	 	        } catch (Exception e) {
	 	        	e.printStackTrace();
	 	        }
    		}
 	        if (failure) {
 	        	assertNull(null);
 	        	 System.out.println("Passed: "+file);
 	        }else{
 	        	System.out.println("---FAILED--: "+file);
 	        	assertNotNull(null);
 	        }
           
            try {
				setUp();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	System.out.println("---- COMPILE FAILURE TESTS PASSED ------");
        
        
    }
   
}
