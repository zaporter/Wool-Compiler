package wool.structure.genericBlocks;

import org.objectweb.asm.Label;

import wool.structure.ClassDef;
import wool.structure.Expr;
import wool.structure.JBC;
import wool.structure.Scope;
import wool.structure.WObject;
import wool.structure.WoolException;
import wool.structure.genericBlocks.constants.NewExpr;

public class LoopExpr implements Expr{
	Expr cond;
	Expr expr;
	public LoopExpr(Expr cond, Expr expr) {
		this.expr=expr;
		this.cond=cond;
	}
	@Override
	public String getReturnType(Scope scope) {
		return "Object";
	}

	@Override
	public boolean isTypeSatisfied(Scope s) throws WoolException{
		return ( 
				this.cond.isTypeSatisfied(s) && 
				this.expr.isTypeSatisfied(s) && 
				this.cond.getReturnType(s).equals("boolean") );
	}

	@Override
	public WObject evaluate(Scope scope) {
		// TODO Auto-generated method stub
		return null;
	}
	public JBC writeJBC(JBC jbc,ClassDef currentClass, Scope scope) {
		
		
		Label condLabel = new Label();
		Label bodyLabel = new Label();
		jbc.methodV.visitJumpInsn(GOTO, condLabel);
		jbc.methodV.visitLabel(bodyLabel);
		expr.writeJBC(jbc, currentClass, scope);
		jbc.methodV.visitInsn(POP);
		jbc.methodV.visitLabel(condLabel);
		cond.writeJBC(jbc, currentClass, scope);
		jbc.methodV.visitJumpInsn(IFNE, bodyLabel);
		(new NewExpr("Object")).writeJBC(jbc,currentClass,scope);
		return jbc;
	}
	@Override 
	public String asString() {
		return "while ("+cond.asString()+") : "+expr.asString();
	}
	@Override
	public void setSelfType(String newType) {
		cond.setSelfType(newType);
		expr.setSelfType(newType);
	}
	@Override
    public Expr cloneExpr() 
    { 
        return new LoopExpr(cond.cloneExpr(),expr.cloneExpr()); 
    }

}
