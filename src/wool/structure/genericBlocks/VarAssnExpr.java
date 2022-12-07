package wool.structure.genericBlocks;

import org.objectweb.asm.Label;

import wool.structure.ClassDef;
import wool.structure.Expr;
import wool.structure.JBC;
import wool.structure.Program;
import wool.structure.Scope;
import wool.structure.WObject;
import wool.structure.WoolException;
import wool.structure.genericBlocks.constants.NullConstExpr;

public class VarAssnExpr implements Expr{
	String var;
	Expr val;
	public VarAssnExpr(String var, Expr val) {
		this.var=var;
		this.val=val;
	}
	@Override
	public String getReturnType(Scope scope) {
		return val.getReturnType(scope);
	}

	@Override
	public boolean isTypeSatisfied(Scope scope)throws WoolException {
		if (!val.isTypeSatisfied(scope)) throw new WoolException(this,"unsatisfied val");
		if (scope.getVar(var)==null) throw new WoolException(this, "No such var "+var);
		if (this.val instanceof NullConstExpr) {
			if (!Program.typeEquiv("Object",scope.getVar(var).getReturnType(scope) ))	throw new WoolException(this, "non-nullable type");
		}else {
			if (!Program.typeEquiv(scope.getVar(var).getReturnType(scope), (val.getReturnType(scope)))) throw new WoolException(this,"Unequal types for assignment ("+scope.getVar(var).getReturnType(scope)+") <- ("+(val.getReturnType(scope))+")");
		}
		return true;
		

	}
	public JBC writeJBC(JBC jbc,ClassDef currentClass, Scope scope) {
		val.writeJBC(jbc, currentClass, scope);
		jbc.methodV.visitInsn(DUP);
		String returnType = jbc.getJBCTypeNameReturn(val.getReturnType(scope));
		if (!jbc.isNativeType(val.getReturnType(scope))) {
			jbc.methodV.visitTypeInsn(CHECKCAST, returnType);
		}
		if (jbc.containsLocalVar(var)) {
			jbc.methodV.visitVarInsn(jbc.isNativeType(getReturnType(scope))?ISTORE:ASTORE, jbc.getLocalVar(var));
		}else {
			jbc.methodV.visitVarInsn(ALOAD, 0);
			jbc.methodV.visitInsn(SWAP);
			String type = currentClass.UUID.equals("Str")?"java/lang/String":jbc.WOOL+currentClass.UUID;
			jbc.methodV.visitFieldInsn(PUTFIELD, type, var, 
						jbc.getJBCTypeName(getReturnType(scope)));
		}
		return jbc;
	}
	
	@Override
	public WObject evaluate(Scope scope) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override 
	public String asString() {
		return var+"<-"+val.asString();
	}
	@Override
	public void setSelfType(String newType) {
		val.setSelfType(newType);
	}
	@Override
    public Expr cloneExpr() 
    { 
        return new VarAssnExpr(new String(var), val.cloneExpr()); 
    }

}
