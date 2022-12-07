package wool.structure.genericBlocks;

import wool.structure.ClassDef;
import wool.structure.Expr;
import wool.structure.JBC;
import wool.structure.Scope;
import wool.structure.WObject;
import wool.structure.WoolException;

public class AddExpr implements Expr {
	Expr val1,val2;
	String sym;
	public AddExpr(Expr val1, Expr val2, String diff) {
		this.val1=val1;
		this.val2=val2;
		this.sym=diff;
	}
	@Override
	public String getReturnType(Scope scope) {
		return "int";
	}

	@Override
	public boolean isTypeSatisfied(Scope s) throws WoolException{
		if (!this.val2.isTypeSatisfied(s)) throw new WoolException(this,"v2 is not valid");
		if (!this.val1.isTypeSatisfied(s)) throw new WoolException(this,"v1 is not valid");
		if (!this.val1.getReturnType(s).equals("int")) throw new WoolException(this,"Val 1 is not an int");
		if (!this.val2.getReturnType(s).equals("int")) throw new WoolException(this,"Val 2 is not an int");
		return true;
	}
	public JBC writeJBC(JBC jbc,ClassDef currentClass, Scope scope) {
		val1.writeJBC(jbc, currentClass, scope);
		val2.writeJBC(jbc, currentClass, scope);
		if (sym.equals("+")) {
			jbc.methodV.visitInsn(IADD);
		}else if(sym.equals("-")) {
			jbc.methodV.visitInsn(ISUB);
		}else {
			System.err.println("Unknown add symbol"+sym);
		}
		
		
		return jbc;
	}
	@Override
	public WObject evaluate(Scope scope) {
		return null;
	}
	@Override 
	public String asString() {
		return this.val1.asString()+sym+this.val2.asString();
	}
	@Override
	public void setSelfType(String newType) {
		val1.setSelfType(newType);
		val2.setSelfType(newType);
	}
	@Override
    public Expr cloneExpr() 
    { 
        return new AddExpr(val1.cloneExpr(),val2.cloneExpr(),new String(sym)); 
    }

}
