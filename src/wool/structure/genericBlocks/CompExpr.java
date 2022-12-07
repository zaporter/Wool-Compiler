package wool.structure.genericBlocks;

import org.objectweb.asm.Label;

import jdk.internal.org.objectweb.asm.Opcodes;
import wool.structure.ClassDef;
import wool.structure.Expr;
import wool.structure.JBC;
import wool.structure.Scope;
import wool.structure.WObject;
import wool.structure.WoolException;

public class CompExpr implements Expr{
	Expr v1,v2;
	String type;
	public CompExpr(Expr v1, Expr v2, String type) {
		this.v1=v1;
		this.v2=v2;
		this.type=type;
	}
	@Override
	public String getReturnType(Scope scope) {
		return "boolean";
	}
	public int getCompInsn() {
		if (type.equals("<")) {
			return IF_ICMPGE;
		}
		if (type.equals("<=")) {
			return IF_ICMPGT;
		}
		if (type.equals("=")) {
			return IF_ICMPNE;
		}
		if (type.equals(">")) {
			return IF_ICMPLE;
		}
		if (type.equals(">=")) {
			return IF_ICMPLT;
		}
		System.err.println("INVALID COMP EXPR: "+type);
		return IF_ICMPGE;
	}
	public JBC writeJBC(JBC jbc,ClassDef currentClass, Scope scope) {
		v1.writeJBC(jbc, currentClass, scope);
		v2.writeJBC(jbc, currentClass, scope);
		Label start = new Label();
		jbc.methodV.visitJumpInsn(getCompInsn(), start);
		jbc.methodV.visitInsn(ICONST_1);
		Label elseL = new Label();
		jbc.methodV.visitJumpInsn(GOTO, elseL);
		jbc.methodV.visitLabel(start);
		/*jbc.methodV.visitFrame(Opcodes.F_FULL, 
				1,
				new Object[] {jbc.WOOL+currentClass.UUID}, 
				1, 
				new Object[] {jbc.WOOL+currentClass.UUID});*/
		jbc.methodV.visitInsn(ICONST_0);
		jbc.methodV.visitLabel(elseL);
		/*jbc.methodV.visitFrame(Opcodes.F_FULL, 
				1,
				new Object[] {jbc.WOOL+currentClass.UUID}, 
				2, 
				new Object[] {jbc.WOOL+currentClass.UUID, INTEGER});*/
		return jbc;
	}
	@Override
	public WObject evaluate(Scope scope) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isTypeSatisfied(Scope s) throws WoolException{
		if (!this.v2.isTypeSatisfied(s)) throw new WoolException(this,"v2 is not valid");
		if (!this.v1.isTypeSatisfied(s)) throw new WoolException(this,"v1 is not valid");
		if (!this.v1.getReturnType(s).equals("int")) throw new WoolException(this,"Val 1 is not an int");
		return true;
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
        return new CompExpr(v1.cloneExpr(),v2.cloneExpr(),new String(type)); 
    }
}
