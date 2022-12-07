package wool.utility;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import org.antlr.v4.runtime.misc.NotNull;

import wool.lexparse.WoolBaseListener;
import wool.lexparse.WoolParser;
import wool.structure.Expr;
import wool.structure.MethodDef;
import wool.structure.Program;
import wool.structure.Scope;
import wool.structure.VariableDef;
import wool.structure.genericBlocks.AddExpr;
import wool.structure.genericBlocks.CompExpr;
import wool.structure.genericBlocks.EqualityExpr;
import wool.structure.genericBlocks.ExprBlockExpr;
import wool.structure.genericBlocks.ExprFlag;
import wool.structure.genericBlocks.FullMethodCallExpr;
import wool.structure.genericBlocks.IfExpr;
import wool.structure.genericBlocks.IsNullExpr;
import wool.structure.genericBlocks.LocalMethodCallExpr;
import wool.structure.genericBlocks.LogicalNegateExpr;
import wool.structure.genericBlocks.LoopExpr;
import wool.structure.genericBlocks.MultExpr;
import wool.structure.genericBlocks.NegateExpr;
import wool.structure.genericBlocks.ParenExpr;
import wool.structure.genericBlocks.SelectExpr;
import wool.structure.genericBlocks.VarAssnExpr;
import wool.structure.genericBlocks.VarExpr;
import wool.structure.genericBlocks.constants.BoolConstExpr;
import wool.structure.genericBlocks.constants.IntConstExpr;
import wool.structure.genericBlocks.constants.NewExpr;
import wool.structure.genericBlocks.constants.NullConstExpr;
import wool.structure.genericBlocks.constants.StringConstExpr;
import wool.structure.genericBlocks.constants.ThisConstExpr;

/*
 * 
 * Walk the parse tree and fill in the classes.
 * This uses a stack to keep track of which expressions go where. 
 * After that, it is fairly simple. 
 * 
 */
public class CompilationTreeWalker extends WoolBaseListener{
	private int indentLevel;
	private StringBuilder printedTree;
	private MethodDef currentMethodDec;
	Stack<Expr> exprStack = new Stack<>();
	public CompilationTreeWalker() {
		indentLevel = 0;
		printedTree = new StringBuilder();
	}
	@Override public void enterProgram(@NotNull WoolParser.ProgramContext ctx) { 
		Program.initialize();
	}
	@Override public void enterMethodDec(@NotNull WoolParser.MethodDecContext ctx) { 
		currentMethodDec = new MethodDef(ctx.methodNameT.getText(),ctx.returnType.getText(),new VariableDef[] {},null);
		//currentScope=currentMethodDec;
	}
	@Override public void exitMethodDec(@NotNull WoolParser.MethodDecContext ctx) { 
		currentMethodDec.expression = exprStack.pop();
		Program.getCurrentClass().addMethod(currentMethodDec);
		currentMethodDec=null;
		
	}
	@Override public void enterMethodBlock(@NotNull WoolParser.MethodBlockContext ctx) { 
		
		/*for ( VarDecLineContext line : ctx.lines) {
			currentMethodDec.paramIntializationList.add(new VariableDef())
		}*/
		
	}
	@Override public void enterClassDec(@NotNull WoolParser.ClassDecContext ctx) { 
		
		Program.addClass(ctx.className.getText(), ctx.parent==null ? "Object" : ctx.parent.getText());
		Program.setCurrentClass(ctx.className.getText());
	}
	@Override public void exitClassDec(@NotNull WoolParser.ClassDecContext ctx) { 
		Program.setCurrentClass(null);
	}


	@Override public void exitAssignExpression(@NotNull WoolParser.AssignExpressionContext ctx) { 
		
		exprStack.push(new VarAssnExpr(ctx.varName.getText(),popBack(0)));
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterParameterListDec(@NotNull WoolParser.ParameterListDecContext ctx) {
		//exprStack.push(new ExprFlag("parameterListDec"));
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitParameterListDec(@NotNull WoolParser.ParameterListDecContext ctx) {
		
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterTypeName(@NotNull WoolParser.TypeNameContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitTypeName(@NotNull WoolParser.TypeNameContext ctx) { }

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterLocalMethodCallExpression(@NotNull WoolParser.LocalMethodCallExpressionContext ctx) {
		exprStack.push(new ExprFlag("localMethodCall"));
		//printStack();
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitLocalMethodCallExpression(@NotNull WoolParser.LocalMethodCallExpressionContext ctx) { 
		Expr var;
		List<Expr> values=new ArrayList<>();
		int a=0;
		while (true) {
			var = popBack(0);
			if (var instanceof ExprFlag) {
				break;
			}
			values.add(var);			
		}
		Collections.reverse(values);
		exprStack.push(new FullMethodCallExpr(new ThisConstExpr(Program.getCurrentClass()),ctx.funcName.getText(),values));
	//	printStack();
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitProgram(@NotNull WoolParser.ProgramContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterFullMethodCallExpression(@NotNull WoolParser.FullMethodCallExpressionContext ctx) { 
		exprStack.push(new ExprFlag("fullMethodCall"));
		//printStack();
	}
	@Override public void exitFullMethodCallExpression(@NotNull WoolParser.FullMethodCallExpressionContext ctx) { 
		Expr var;
	//	printStack();
		List<Expr> values=new ArrayList<>();
		while (true) {
			var = popBack(0);
			if (var instanceof ExprFlag) {
				break;
			}
			values.add(var);			
		}
		Collections.reverse(values);
		log("exit full method call");
		Expr first = values.get(0);
		values.remove(0);
		exprStack.push(new FullMethodCallExpr(first,ctx.funcName.getText(),values));
		//printStack();
	}



	@Override public void exitThisExpression(@NotNull WoolParser.ThisExpressionContext ctx) { 
		exprStack.push(new ThisConstExpr(Program.getCurrentClass()));
	}

	@Override public void exitIntConstantExpression(@NotNull WoolParser.IntConstantExpressionContext ctx) {
		log("const int "+ctx.getText());
		exprStack.push(new IntConstExpr(Integer.parseInt(ctx.getText())));
	}

	@Override public void exitBoolConstantExpression(@NotNull WoolParser.BoolConstantExpressionContext ctx) { 
		exprStack.push(new BoolConstExpr(Boolean.parseBoolean(ctx.getText())));
	}
	@Override public void enterClassBody(@NotNull WoolParser.ClassBodyContext ctx) { }
	@Override public void exitClassBody(@NotNull WoolParser.ClassBodyContext ctx) { }
	@Override public void exitEqualityExpression(@NotNull WoolParser.EqualityExpressionContext ctx) { 
		exprStack.push(new EqualityExpr(popBack(1),popBack(0),ctx.eqSymbol.getText()));
	}

	@Override public void exitIfExpression(@NotNull WoolParser.IfExpressionContext ctx) {		
		exprStack.push(new IfExpr(popBack(2),popBack(1),popBack(0)));
	}

	@Override public void exitPlusExpression(@NotNull WoolParser.PlusExpressionContext ctx) {
		log("exit plusl");
		exprStack.push(new AddExpr(popBack(1),popBack(0),ctx.diffSymbol.getText()));
	}
	
	@Override public void exitNewExpression(@NotNull WoolParser.NewExpressionContext ctx) { 
		exprStack.push(new NewExpr(ctx.classNameT.getText()));
	}
	@Override public void exitVariableExpression(@NotNull WoolParser.VariableExpressionContext ctx) { 
		exprStack.push(new VarExpr(ctx.getText()));
	}
    @Override public void exitLogicalNegationExpression(@NotNull WoolParser.LogicalNegationExpressionContext ctx) {
		exprStack.push(new LogicalNegateExpr(popBack(0)));
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterSelectExpression(@NotNull WoolParser.SelectExpressionContext ctx) { 
		exprStack.push(new ExprFlag("select"));
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitSelectExpression(@NotNull WoolParser.SelectExpressionContext ctx) {
		Expr var;
		List<Expr> conds = new ArrayList<>();
		List<Expr> values=new ArrayList<>();
		int a=0;
		while (true) {
			var = popBack(0);
			if (var instanceof ExprFlag) {
				break;
			}
			if (a++%2==0) {
				values.add(var);
			}else {
				conds.add(var);
			}			
		}
		if (conds.size()!=values.size()) {
			System.err.println("Exit select expression had a different number of values and conditions");
			Program.compilationFailed=true;
		}
		Collections.reverse(conds);
		Collections.reverse(values);
		exprStack.push(new SelectExpr(conds, values));
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterBlockExpression(@NotNull WoolParser.BlockExpressionContext ctx) { 
		exprStack.push(new ExprFlag("block"));
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitBlockExpression(@NotNull WoolParser.BlockExpressionContext ctx) {
		Expr var;
		List<Expr> values=new ArrayList<>();
		int a=0;
		while (true) {
			var = popBack(0);
			if (var instanceof ExprFlag) {
				break;
			}
			values.add(var);			
		}
		Collections.reverse(values);
		exprStack.push(new ExprBlockExpr( values));
	}

	
	
	@Override public void exitNullExpression(@NotNull WoolParser.NullExpressionContext ctx) { 
		exprStack.push(new NullConstExpr());
	}

	@Override public void exitIsNullExpression(@NotNull WoolParser.IsNullExpressionContext ctx) { 
		exprStack.push(new IsNullExpr(popBack(0)));
	}

	@Override public void exitNegateExpression(@NotNull WoolParser.NegateExpressionContext ctx) {
		exprStack.push(new NegateExpr(popBack(0)));
	}

	
	@Override public void exitLoopExpression(@NotNull WoolParser.LoopExpressionContext ctx) {
		exprStack.push(new LoopExpr(popBack(1),popBack(0)));
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitParenExpression(@NotNull WoolParser.ParenExpressionContext ctx) { 
		log("exit parenthesis");
		exprStack.push(new ParenExpr(exprStack.pop()));
	}
	
	@Override public void exitStringConstantExpression(@NotNull WoolParser.StringConstantExpressionContext ctx) { 
		exprStack.push(new StringConstExpr(ctx.getText()));
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterVarDecLine(@NotNull WoolParser.VarDecLineContext ctx) {
		
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitVarDecLine(@NotNull WoolParser.VarDecLineContext ctx) { 
		VariableDef dec = new VariableDef(ctx.varNameT.getText(),ctx.typeNameT.getText(), exprStack.isEmpty()?null:exprStack.pop());
		if (currentMethodDec==null) {
			Program.getCurrentClass().addVariable(dec);
		}else {
			currentMethodDec.paramIntializationList.add(dec);
		}
		
		
	}
	@Override 
	public void exitParam(WoolParser.ParamContext ctx) {
		if (currentMethodDec==null) {
			System.err.println("current method dec null for printing" + ctx);
			//Program.compilationFailed=true;
		}
		currentMethodDec.params.add(new VariableDef(ctx.varNameT.getText(), ctx.typeNameT.getText()));
	}

	@Override public void exitCompExpression(@NotNull WoolParser.CompExpressionContext ctx) { 
		exprStack.push(new CompExpr(popBack(1),popBack(0),ctx.compSymbol.getText()));
	}
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void enterMultExpression(@NotNull WoolParser.MultExpressionContext ctx) { }
	/**
	 * {@inheritDoc}
	 *
	 * <p>The default implementation does nothing.</p>
	 */
	@Override public void exitMultExpression(@NotNull WoolParser.MultExpressionContext ctx) {
		log("exited mult expr");
		exprStack.push(new MultExpr(popBack(1),popBack(0),ctx.multSymbol.getText()));
	}
	private Expr popBack(int num) {
		Expr val = exprStack.get(exprStack.size()-1-num);
		exprStack.remove(exprStack.size()-1-num);
		return val;
	}
	 private void log(String ...strings) {
		 for (int i =0; i<indentLevel; i++) {
			 printedTree.append(" ");
		 }
		 for (String s : strings) {
			 printedTree.append(s);
			 printedTree.append(" ");
		 }
		 printedTree.append("\n");
	 }
	 @Override
	 public String toString() {
		 return printedTree.toString();
	 }
	 public void printStack() {
		 System.out.println("-----start stack------");
		 for (int i=0; i<exprStack.size();i++) {
			 System.out.println(exprStack.get(i)+"|"+exprStack.get(i).asString());
		 }
		 System.out.println("-----end stack------");
	 }
}
