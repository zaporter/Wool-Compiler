package wool.structure.genericBlocks.constants;

import wool.structure.ClassDef;
import wool.structure.Expr;
import wool.structure.JBC;
import wool.structure.Scope;
import wool.structure.WObject;

public class IntConstExpr implements Expr{
	int in;
	public IntConstExpr(int val) {
		this.in=val;
	}
	@Override
	public String getReturnType(Scope scope) {
		// TODO Auto-generated method stub
		return "int";
	}

	@Override
	public WObject evaluate(Scope scope) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public boolean isTypeSatisfied(Scope scope) {
		// TODO Auto-generated method stub
		return true;
	}
	@Override 
	public String asString() {
		return in+"";
	}
	public JBC writeJBC(JBC jbc,ClassDef currentClass, Scope scope) {
		jbc.methodV.visitLdcInsn(new Integer(in));
		return jbc;
	}
	@Override
	public void setSelfType(String newType) {
		
		
	}
	@Override
    public Expr cloneExpr() 
    { 
        return new IntConstExpr(in); 
    }
}
