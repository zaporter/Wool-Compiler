package wool.structure.genericBlocks.constants;

import wool.structure.ClassDef;
import wool.structure.Expr;
import wool.structure.JBC;
import wool.structure.Scope;
import wool.structure.WObject;

public class NullConstExpr implements Expr {

	public NullConstExpr() {
		
	}
	@Override
	public String getReturnType(Scope scope) {
		// TODO Auto-generated method stub
		return "Object";
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
	@Override 
	public String asString() {
		return "null";
	}
	@Override
	public void setSelfType(String newType) {
		
		
	}
	public JBC writeJBC(JBC jbc,ClassDef currentClass, Scope scope) {
		jbc.methodV.visitInsn(ACONST_NULL);
		return jbc;
	}
	@Override
    public Expr cloneExpr() 
    { 
        return new NullConstExpr(); 
    }
}
