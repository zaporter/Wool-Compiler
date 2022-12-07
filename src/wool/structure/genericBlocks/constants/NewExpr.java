package wool.structure.genericBlocks.constants;

import wool.structure.ClassDef;
import wool.structure.Expr;
import wool.structure.JBC;
import wool.structure.Scope;
import wool.structure.WObject;

public class NewExpr implements Expr{
	public String type;
	public NewExpr(String type) {
		this.type=type;
	}

	@Override
	public String getReturnType(Scope scope) {
		return type;
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
		return "new "+type;
	}
	public JBC writeJBC(JBC jbc,ClassDef currentClass, Scope scope) {
		jbc.methodV.visitTypeInsn(NEW, jbc.WOOL+type);
		jbc.methodV.visitInsn(DUP);
		jbc.methodV.visitMethodInsn(INVOKESPECIAL, jbc.WOOL+type, "<init>", "()V",false);
		return jbc;
	}
	@Override
    public Expr cloneExpr() 
    { 
        return new NewExpr(new String(type)); 
    }
	@Override
	public void setSelfType(String newType) {
		if (type.equals("SELF_TYPE")) {
			type=newType;
		}
		
	}

}
