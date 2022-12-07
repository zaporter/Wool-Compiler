package wool.structure.genericBlocks;

import wool.structure.ClassDef;
import wool.structure.Expr;
import wool.structure.JBC;
import wool.structure.Scope;
import wool.structure.WObject;
import wool.structure.WoolException;

public class ParenExpr implements Expr{
	Expr child;
	public ParenExpr(Expr child) {
		this.child=child;
	}
	@Override
	public String getReturnType(Scope scope) {
		return child.getReturnType(scope);
	}

	@Override
	public boolean isTypeSatisfied(Scope s)throws WoolException {
		return child.isTypeSatisfied(s);
	}

	@Override
	public WObject evaluate(Scope scope) {
		// TODO Auto-generated method stub
		return null;
	}
	public JBC writeJBC(JBC jbc,ClassDef currentClass, Scope scope) {
		child.writeJBC(jbc, currentClass, scope);
		return jbc;
	}
	@Override 
	public String asString() {
		return "("+this.child.asString()+")";
	}
	@Override
	public void setSelfType(String newType) {
		child.setSelfType(newType);	
	}
	@Override
    public Expr cloneExpr() 
    { 
        return new ParenExpr(child.cloneExpr()); 
    }
}
