package wool.structure.genericBlocks;

import org.objectweb.asm.Label;

import wool.structure.ClassDef;
import wool.structure.Expr;
import wool.structure.JBC;
import wool.structure.Scope;
import wool.structure.WObject;
import wool.structure.WoolException;

public class EqualityExpr implements Expr{
	Expr v1,v2;
	String type;
	public EqualityExpr(Expr v1, Expr v2, String type) {
		this.v1=v1;
		this.v2=v2;
		this.type=type;
	}
	@Override
	public String getReturnType(Scope scope) {
		return "boolean";
	}

	@Override
	public WObject evaluate(Scope scope) {
		// TODO Auto-generated method stub
		return null;
	}
	private int getCompType(JBC jbc, Scope s) {
		if (jbc.isNativeType(v1.getReturnType(s))) {
			return this.type.equals("=") ? IF_ICMPEQ : IF_ICMPNE;
		}else {
			return this.type.equals("=") ? IF_ACMPEQ : IF_ACMPNE;
		}
	}
	public JBC writeJBC(JBC jbc,ClassDef currentClass, Scope scope) {
		v1.writeJBC(jbc, currentClass, scope);
		v2.writeJBC(jbc, currentClass, scope);
		Label l1 = new Label();
		Label l2 = new Label();
		jbc.methodV.visitJumpInsn(getCompType(jbc,scope), l1);
		jbc.methodV.visitInsn(ICONST_0);
		jbc.methodV.visitJumpInsn(GOTO, l2);
		jbc.methodV.visitLabel(l1);
		jbc.methodV.visitInsn(ICONST_1);
		jbc.methodV.visitLabel(l2);
		return jbc;
	}

	@Override
	public boolean isTypeSatisfied(Scope s) throws WoolException{
		// TODO Auto-generated method stub
		
		return v1.isTypeSatisfied(s) && v2.isTypeSatisfied(s) &&
				v1.getReturnType(s).equals(v2.getReturnType(s));
	}
	@Override 
	public String asString() {
		return v1.asString()+type+v2.asString();
	}
	@Override
	public void setSelfType(String newType) {
		v1.setSelfType(newType);
		v2.setSelfType(newType);
	}
	@Override
    public Expr cloneExpr() 
    { 
        return new EqualityExpr(v1.cloneExpr(),v2.cloneExpr(),new String(type)) ; 
    }
}
