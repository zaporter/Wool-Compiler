package wool.structure.genericBlocks.constants;

import wool.structure.ClassDef;
import wool.structure.Expr;
import wool.structure.JBC;
import wool.structure.Scope;
import wool.structure.WObject;

public class ThisConstExpr implements Expr{
	ClassDef classDec;
	public ThisConstExpr(ClassDef classDef) {
		this.classDec=classDef;
	}
	@Override
	public String getReturnType(Scope scope) {
		// TODO Auto-generated method stub
		return classDec.UUID;
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
		return "this";
	}
	@Override
	public void setSelfType(String newType) {
		
		
	}
	public JBC writeJBC(JBC jbc,ClassDef currentClass, Scope scope) {
		jbc.methodV.visitVarInsn(ALOAD,0);
		return jbc;
	}
	@Override
    public Expr cloneExpr() 
    { //ZTODO this doesnt change... huh
        return new ThisConstExpr(classDec); 
    }

}
