package wool.structure.genericBlocks;

import org.objectweb.asm.Label;

import wool.structure.ClassDef;
import wool.structure.Expr;
import wool.structure.JBC;
import wool.structure.Program;
import wool.structure.Scope;
import wool.structure.WObject;
import wool.structure.WoolException;

public class IfExpr implements Expr {
	Expr cond;
	Expr ctrue;
	Expr cfalse;
	public IfExpr(Expr cond, Expr ctrue, Expr cfalse) {
		this.cond=cond;
		this.ctrue=ctrue;
		this.cfalse=cfalse;
	}
	@Override
	public String getReturnType(Scope scope) {
		try{
			return Program.lowestCommonClass(this.ctrue.getReturnType(scope), this.cfalse.getReturnType(scope));
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean isTypeSatisfied(Scope s) throws WoolException{
		if (!this.ctrue.isTypeSatisfied(s)) throw new WoolException(this," Unsatisfied true expression");
		if (!this.cfalse.isTypeSatisfied(s)) throw new WoolException(this," Unsatisfied false expression");
		if (!this.cond.isTypeSatisfied(s)) throw new WoolException(this," Unsatisfied cond expression");
		if (!this.cond.getReturnType(s).equals("boolean")) throw new WoolException(this, "Invalid if condition. Does not evaluate to boolean");
		if (null==Program.lowestCommonClass(this.ctrue.getReturnType(s), this.cfalse.getReturnType(s))) throw new WoolException(this,"No common return class for if statement");
		return true;
		
	}
	public JBC writeJBC(JBC jbc,ClassDef currentClass, Scope scope) {
		cond.writeJBC(jbc, currentClass, scope);
		Label lStart = new Label();
		jbc.methodV.visitJumpInsn(IFEQ, lStart);
		ctrue.writeJBC(jbc, currentClass, scope);
		Label lElse = new Label();
		jbc.methodV.visitJumpInsn(GOTO, lElse);
		jbc.methodV.visitLabel(lStart);
		jbc.methodV.visitFrame(F_APPEND, 1, new Object[] {INTEGER}, 0, null);
		cfalse.writeJBC(jbc, currentClass, scope);
		jbc.methodV.visitLabel(lElse);
		jbc.methodV.visitFrame(F_SAME, 0, null, 0, null);
		
		return jbc;
	}

	@Override
	public WObject evaluate(Scope scope) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override 
	public String asString() {
		return "if ("+this.cond.asString()+") : "+this.ctrue.asString()+" else "+this.cfalse.asString();
	}
	@Override
	public void setSelfType(String newType) {
		this.cond.setSelfType(newType);
		this.cfalse.setSelfType(newType);
		this.ctrue.setSelfType(newType);
	}
	@Override
    public Expr cloneExpr() 
    { 
		
        return new IfExpr(cond.cloneExpr(),ctrue.cloneExpr(),cfalse.cloneExpr()); 
    }
}
