package wool.structure;

import org.objectweb.asm.Opcodes;

public  interface Expr extends Opcodes {
	public String getReturnType(Scope scope); // class when expression is evaluated
	public boolean isTypeSatisfied(Scope scope) throws WoolException; // true iff the expression is semantically valid
	public String asString(); // expr as string
	public JBC writeJBC(JBC jbc,ClassDef currentClass, Scope scope);
	public WObject evaluate(Scope scope); // UNUSED
	public void setSelfType(String newType); // replaces SELF_TYPE with newType where applicable
	public Expr cloneExpr(); // deep copy (except for ThisConstExpr. I might change that though..)
	
}
