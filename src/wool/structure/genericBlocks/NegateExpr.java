package wool.structure.genericBlocks;

import wool.structure.ClassDef;
import wool.structure.Expr;
import wool.structure.JBC;
import wool.structure.Scope;
import wool.structure.WObject;
import wool.structure.WoolException;
import wool.structure.genericBlocks.constants.IntConstExpr;

public class NegateExpr implements Expr{
	Expr v1;
	public NegateExpr(Expr v1) {
		this.v1=v1;
	}

	@Override
	public String getReturnType(Scope scope) {
		// TODO Auto-generated method stub
		return "int";
	}

	@Override
	public boolean isTypeSatisfied(Scope scope)throws WoolException {
		// TODO Auto-generated method stub
		return (v1.isTypeSatisfied(scope) && v1.getReturnType(scope).equals("int"));
	}

	@Override
	public String asString() {
		// TODO Auto-generated method stub
		return "-"+v1.asString();
	}

	@Override
	public WObject evaluate(Scope scope) {
		// TODO Auto-generated method stub
		return null;
	}
	public JBC writeJBC(JBC jbc,ClassDef currentClass, Scope scope) {
		(new MultExpr(v1,new IntConstExpr(-1),"*")).writeJBC(jbc, currentClass, scope);
		return jbc;
	}
	@Override
	public void setSelfType(String newType) {
		v1.setSelfType(newType);	
	}
	@Override
    public Expr cloneExpr() 
    { 
        return new NegateExpr(v1.cloneExpr()); 
    }
}
