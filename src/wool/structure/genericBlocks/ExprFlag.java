package wool.structure.genericBlocks;

import wool.structure.ClassDef;
import wool.structure.Expr;
import wool.structure.JBC;
import wool.structure.Scope;
import wool.structure.WObject;
import wool.structure.WoolException;

public class ExprFlag implements Expr {
	String val;
	public ExprFlag(String val) {
		this.val=val;
	}
	@Override
	public String getReturnType(Scope scope) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isTypeSatisfied(Scope scope) throws WoolException{
		// TODO Auto-generated method stub
		return false;
	}
	public JBC writeJBC(JBC jbc,ClassDef currentClass, Scope scope) {
		return jbc;
	}
	@Override
	public String asString() {
		// TODO Auto-generated method stub
		return "EXPR FLAG : "+val;
	}

	@Override
	public WObject evaluate(Scope scope) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void setSelfType(String newType) {
		
		
	}
	@Override
    public Expr cloneExpr() 
    { 
        return new ExprFlag(new String(val)); 
    }
}
