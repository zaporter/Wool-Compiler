package wool.structure.genericBlocks.constants;

import wool.structure.ClassDef;
import wool.structure.Expr;
import wool.structure.JBC;
import wool.structure.Scope;
import wool.structure.WObject;

public class BoolConstExpr implements Expr{
	boolean val;
	public BoolConstExpr(boolean val) {
		this.val=val;
	}
	@Override
	public String getReturnType(Scope scope) {
		// TODO Auto-generated method stub
		return "boolean";
	}

	@Override
	public boolean isTypeSatisfied(Scope scope) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public WObject evaluate(Scope scope) {
		// TODO Auto-generated method stub
		return null;
	}
	public JBC writeJBC(JBC jbc,ClassDef currentClass, Scope scope) {
		if (val) {
			jbc.methodV.visitInsn(ICONST_1);
		}else {
			jbc.methodV.visitInsn(ICONST_0);
		}
		return jbc;
	}
	@Override 
	public String asString() {
		return val+"";
	}
	@Override
	public void setSelfType(String newType) {
		
		
	}
	@Override
    public Expr cloneExpr() 
    { 
        return new BoolConstExpr(val) ; 
    }
}
