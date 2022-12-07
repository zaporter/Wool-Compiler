package wool.utility;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.function.Supplier;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import wool.lexparse.*;
import wool.structure.Program;

public class WoolRunnerImpl implements WoolRunner
{
    private WoolLexer lexer;
    private WoolParser parser;
//    private ASTNode ast;
    private ParserRuleContext parseTree;
//    private LinkedList<IRinstruction> ir;
    
    private Supplier<Token> nextToken;
    
    /**
     * Default constructor. Everything is uninitialized.
     */
    public WoolRunnerImpl()
    {
        nextToken = () -> { throw new WoolException("Lexer has not been initialized"); } ;
    }

    /************************************************************************** 
     * Compiler Actions 
     * These methods will usually be called by external clients. These are the
     * methods called by the CoolRunner interface
     */
    
    /*
     * @see cool.utility.CoolRunner#nextToken()
     */
    @Override
    public Token nextToken()
    {
        return nextToken.get();
    }
    
    /*
     * @see cool.utility.CoolRunner#parse()
     */
    @Override
    public ParserRuleContext parse()
    {
        parseTree = parser.program();
        return parseTree;
    }
    
//    @Override
//    public ParserRuleContext buildSymbolTable()
//    {
//        parseTree = parse();
//        SymbolTableBuilder stb = new SymbolTableBuilder();
//        parseTree.accept(stb);
//        return parseTree;
//    }
    
//    @Override
//    public ASTNode createAST()
//    {
//        parseTree = parse();
//        ASTCreator creator = new ASTCreator();
//        ast = parseTree.accept(creator);
//        return ast;
//    }
//    
    @Override
    public void typecheck()
    {
        parseTree = parser.program();
        if(parseTree==null) {
        	System.err.println("Syntax errors in provided program");
        	return;
        }
        CompilationTreeWalker pt = new CompilationTreeWalker();

        Program.initialize();
        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(pt, parseTree);
        
        try {
        	
        	 if (!Program.verifyInheritence()){
        		 return;
        	 }
        	  if(!Program.setParentsScopes()) {
        		 return;
        	  }
        	  Program.setSelfTypes();
              Program.isTypeSatisfied();
        }catch (Exception e) {
        	e.printStackTrace();
        	return;
        } 
    }
    @Override
    public void compile() {
    	Program.generateSource();
    }
//    
//    @Override
//    public LinkedList<IRinstruction> makeIR()
//    {
//        typecheck();
//        IRCreator irc = new IRCreator();
//        ast.accept(irc);
//        ir = irc.ir;
//        return ir;
//    }
//    
//    @Override
//    public Map<String, byte[]> compile()
//    {
//        makeIR();
//        CoolCodeEmitter emitter = new CoolCodeEmitter();
//        ast.accept(emitter);
//        return emitter.getBytecodes();
//    }

    /************************************************************************** 
     * Initializers
     * These methods are called by the factory in order to set up and 
     * initialize the compiler components.
     */
    
    /**
     * Set the lexer and change the nextToken variable
     * @param lexer the lexer to set
     */
    public void setLexer(WoolLexer lexer)
    {
        this.lexer = lexer;
        nextToken = () -> lexer.nextToken();
    }

    public void setParser(WoolParser parser)
    {
        this.parser = parser;
    }

    /**
     * @return the lexer
     */
    public WoolLexer getLexer()
    {
        return lexer;
    }

    /**
     * @return the parser
     */
    public WoolParser getParser()
    {
        return parser;
    }
    
    public ParserRuleContext getParseTree()
    {
        return parseTree;
    }

	
//    /**
//     * @return the ast
//     */
//    public ASTNode getAst()
//    {
//        return ast;
//    }
//    
//    /**
//     * get the IR code
//     */
//    public LinkedList<IRinstruction> getIR()
//    {
//        return ir;
//    }
}
