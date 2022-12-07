package wool.structure.genericBlocks;

import wool.structure.ClassDef;
import wool.structure.Expr;
import wool.structure.JBC;
import wool.structure.Program;
import wool.structure.Scope;
import wool.structure.WObject;
import wool.structure.WoolException;
import wool.structure.genericBlocks.constants.NullConstExpr;

public class IsNullExpr implements Expr{
	Expr val;
	public IsNullExpr(Expr val) {
		this.val=val;
	}
	@Override
	public String getReturnType(Scope scope) {
		// TODO Auto-generated method stub
		return "boolean";
	}

	@Override
	public boolean isTypeSatisfied(Scope scope) throws WoolException{
		// TODO Auto-generated method stub
		
		if ( !val.isTypeSatisfied(scope)) throw new WoolException(this, "unsatisfied variable");
		if (!Program.typeEquiv("Object", val.getReturnType(scope))) throw new WoolException(this," val is not nullable (is not an object)");
		return true;
 	}

	@Override
	public String asString() {
		return "isnull "+val.asString();
	}
	public JBC writeJBC(JBC jbc,ClassDef currentClass, Scope scope) {
		(new EqualityExpr(new NullConstExpr(),val,"=")).writeJBC(jbc, currentClass, scope);	
		return jbc;
	}
	@Override
	public WObject evaluate(Scope scope) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setSelfType(String newType) {
		val.setSelfType(newType);
	}
	@Override
    public Expr cloneExpr() 
    { 
        return new IsNullExpr(val.cloneExpr()); 
    }
}
