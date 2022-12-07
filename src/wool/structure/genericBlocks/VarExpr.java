package wool.structure.genericBlocks;

import wool.structure.ClassDef;
import wool.structure.Expr;
import wool.structure.JBC;
import wool.structure.Scope;
import wool.structure.WObject;
import wool.structure.WoolException;

public class VarExpr implements Expr{
	String var;
	public VarExpr(String var) {
		this.var=var;
	}
	@Override
	public String getReturnType(Scope scope) {
		return scope.getVar(var).getReturnType(scope);
	}

	@Override
	public boolean isTypeSatisfied(Scope scope)throws WoolException {
		if (scope.getVar(var)==null) throw new WoolException(this,"Undefined variable");
		return true;
	}

	@Override
	public WObject evaluate(Scope scope) {
		// TODO Auto-generated method stub
		return null;
	}
	public JBC writeJBC(JBC jbc,ClassDef currentClass, Scope scope) {
		if (jbc.containsLocalVar(var)) {
			// load it as a local variable
			
			jbc.methodV.visitVarInsn(jbc.isNativeType(scope.getVarType(var))?ILOAD:ALOAD, jbc.getLocalVar(var));
		}else {
			// load it as a field
			jbc.methodV.visitVarInsn(ALOAD, 0);
			jbc.methodV.visitFieldInsn(GETFIELD, jbc.WOOL+currentClass.UUID, var, jbc.getJBCTypeName(scope.getVarType(var)));
		}
		
		return jbc;
	}
	@Override 
	public String asString() {
		return var;
	}
	@Override
	public void setSelfType(String newType) {
			
	}
	@Override
    public Expr cloneExpr() 
    { 
        return new VarExpr(new String(var)) ; 
    }

}
