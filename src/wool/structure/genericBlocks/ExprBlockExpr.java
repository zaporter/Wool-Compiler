package wool.structure.genericBlocks;

import java.util.ArrayList;
import java.util.List;

import wool.structure.ClassDef;
import wool.structure.Expr;
import wool.structure.JBC;
import wool.structure.Scope;
import wool.structure.WObject;
import wool.structure.WoolException;

public class ExprBlockExpr implements Expr{
	List<Expr> expressions;
	public ExprBlockExpr(List<Expr> expressions) {
		this.expressions=expressions;
	}

	@Override
	public String getReturnType(Scope scope) {
		return this.expressions.get(this.expressions.size()-1).getReturnType(scope); // Last element
	}

	@Override
	public boolean isTypeSatisfied(Scope s) throws WoolException{
		for(Expr e : expressions) {
			if (!e.isTypeSatisfied(s)) throw new WoolException(this,"unsatisfied expression: "+e.asString());
		}
		return true;
	}

	@Override
	public WObject evaluate(Scope scope) {
		// TODO Auto-generated method stub
		return null;
	}
	public JBC writeJBC(JBC jbc,ClassDef currentClass, Scope scope) {
		for (int i =0; i<expressions.size(); i++) {
			expressions.get(i).writeJBC(jbc, currentClass, scope);
			if (i!=expressions.size()-1) {
				jbc.methodV.visitInsn(POP);
			}
		}
		return jbc;
	}
	@Override 
	public String asString() {
		String str = "{\n";
		for (Expr e : expressions) {
			str+="  "+e.asString()+"\n";
		}
		str+="}\n";
		
		return str;
	}
	@Override
	public void setSelfType(String newType) {
		for (Expr e : expressions) {
			e.setSelfType(newType);
		}
	}
	@Override
    public Expr cloneExpr() 
    { 
        List<Expr> exp = new ArrayList<>();
        for (Expr e : expressions) {
        	exp.add(e.cloneExpr());
        }
        return new ExprBlockExpr(exp);
    }
}
