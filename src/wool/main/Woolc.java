package wool.main;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.swing.JFrame;

import org.antlr.v4.gui.TreeViewer;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import wool.lexparse.WoolParser;
import wool.structure.Program;
import wool.structure.genericBlocks.constants.NewExpr;
import wool.utility.CompilationTreeWalker;
import wool.utility.WoolFactory;
import wool.utility.WoolRunnerImpl;


/**
 * Compiler tool for the Wool programming language.
 */
public class Woolc
{
    public static enum Phase {PARSE, AST, SEMANTIC, IR, COMPILE};
    private List<String> fileNames;
    private String outputDirectory;
    private Phase phase = Phase.COMPILE;
    private boolean displayParseTree;
    private boolean displayGUI;
    private boolean displayAST;
    private boolean displayIR;
    private boolean displayTables;
    private boolean displaySource;
    private final String PACKAGE = "wool"; 
    private WoolRunnerImpl runner;
    private Map<String, byte[]> bytecode;
    
    public Woolc()
    {
        fileNames = new ArrayList<String>();
        outputDirectory = ".";        // default
        phase = Phase.COMPILE;
        displayParseTree = false;
        displayGUI = false;
        displayAST = false;
        displayIR = false;
        displayTables = false;
        displaySource = false;
        bytecode = null;
    }
    
    
    public void executeTool(String[] args) throws Exception
    {
        parseArgs(args);
        StringBuilder woolText = new StringBuilder();
        if (fileNames.size() > 0) {
            for (String fn : fileNames) {
//                woolText.append("########################################\n"
//                        + "#\n# File: " +fn 
//                        + "\n#\n########################################\n");
                woolText.append(new Scanner(new File(fn)).useDelimiter("\\A").next());
            }
            processProgram(woolText);
            postProcess(woolText);
        }
    }
    
    private void processProgram(StringBuilder coolText) throws Exception
    {
        runner = WoolFactory.makeParserRunner(CharStreams.fromString(coolText.toString()));
        System.out.println();   
        switch (phase) {
            case PARSE: runner.parse();  break;
//            case AST: runner.createAST(); break;
            case SEMANTIC: runner.typecheck(); break;
//            case IR: runner.makeIR(); break;
            case COMPILE: 
            	runner.typecheck();
                 runner.compile(); 
//                writeOutput();
                break;
            default: 
                System.err.println("Phase not yet implemented: " + phase);
                System.exit(0);
        }
    }
    
    /*private void writeOutput() throws Exception
    {
        if (bytecode != null) {
            for (String s : bytecode.keySet()) {
                String classFilePath =  outputDirectory + "/cool/" + s + ".class";
                FileOutputStream fos = new FileOutputStream(classFilePath);
                fos.write(bytecode.get(s));
                fos.close();
            }
        }
    }*/
    
    private void postProcess(StringBuilder coolText)
    {
        if (displaySource) {
            System.out.println("\n------------------------------\nSource:\n");
            System.out.println(coolText.toString());
        }
        if (displayTables) {
            System.out.println("\n------------------------------\nProcessed tables:\n");
            System.out.println("Method format: name(paramters) : returnType (<varDec>*) expr\n");
            Program.printClasses();
        }
        
        if (displayParseTree) {
            System.out.println("\n------------------------------\nParse tree:\n");
            System.out.println(runner.getParseTree().toStringTree(runner.getParser()));
        }
        
        if (displayGUI) {
            showGUI(runner.getParser(), runner.getParseTree());
        }
        

    }

    private void showGUI(WoolParser parser, ParserRuleContext tree) {
        List<String> ruleNames = Arrays.asList(parser.getRuleNames());
        TreeViewer tv = new TreeViewer(ruleNames, tree);
        JFrame frame = new JFrame("Parse Tree");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(tv);

        // Display the window.
        frame.pack();
        frame.setVisible(true);
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
            br.readLine();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    private void showHelp()
    {
        System.out.println("Usage: coolc [ options ] [ sourcefiles ]\n"
            + "Options: \n"
            + "    -o outputdirectory\n"
            + "    -h\n"
            + "    -p phase (parse|ast|semantic|compile)\n"
            + "    -d internals (pt|gui|ast|tables|source)");
    }
    
    /**
     * Parse the command line arguments.
     * @param args
     */
    private boolean parseArgs(String[] args)
    {
        boolean argsOK = true;
        boolean optionsDone = false;
        for (int i = 0; i < args.length; i++) {
            String s = args[i];
            switch (s) {
            case "-o":
                outputDirectory = args[++i];
                Program.setOutputDirectory(outputDirectory);
                break;
            case "-h":
                showHelp();
                break;
            case "-p":
                switch (args[++i]) {
                    case "parse": phase = Phase.PARSE; break;
                    case "semantic": phase = Phase.SEMANTIC; break;
                    case "compile": phase = Phase.COMPILE; break;
                    default: throw new RuntimeException("Invalid phase");
                }
                break;
            case "-d":
                switch (args[++i]) {
                    case "pt": displayParseTree = true; break;
                    case "gui": displayGUI = true; break;
                  //  case "ir": displayIR = true; break;
                    case "tables": displayTables = true; break;
                    case "source": displaySource = true; break;
                    default: throw new RuntimeException("Invalid display");
                }
                break;
            default: 
                fileNames.add(args[i]);
                break;
            }
        }
        return argsOK;
    }

    /**
     * Main runner
     * @param args
     * @throws Exception 
     */
    public static void main(String[] args) throws Exception
    {
    	
    	//System.out.println("Started");
    	//args = new String[] {"-d","tables","testdir/creationZone.wl"};
        Woolc tool = new Woolc();
        tool.executeTool(args);
        
    //    System.out.println("Exited");
    }

}

