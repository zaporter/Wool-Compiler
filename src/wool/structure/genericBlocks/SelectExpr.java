package wool.structure.genericBlocks;

import java.util.ArrayList;
import java.util.List;

import wool.structure.ClassDef;
import wool.structure.Expr;
import wool.structure.JBC;
import wool.structure.Program;
import wool.structure.Scope;
import wool.structure.WObject;
import wool.structure.WoolException;

public class SelectExpr implements Expr{
	List<Expr> conditions;
	List<Expr> results;
	public SelectExpr(List<Expr> conditions, List<Expr> results) {
		this.conditions=conditions;
		this.results=results;
	}
	@Override
	public String getReturnType(Scope scope) {
		List<String> returnTypes = new ArrayList<>();
		for (Expr e : results) {
			returnTypes.add(e.getReturnType(scope));
		}
		try {
			return Program.lowestCommonClass(returnTypes);
		}catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}

	@Override
	public boolean isTypeSatisfied(Scope scope) throws WoolException{
		for(Expr e : conditions) {
			if (!e.isTypeSatisfied(scope)) throw new WoolException(this,"unsatisfied expression: "+e.asString());
		}
		for(Expr e : results) {
			if (!e.isTypeSatisfied(scope)) throw new WoolException(this,"unsatisfied expression: "+e.asString());
		}
		for(Expr e : conditions) {
			if (!e.getReturnType(scope).equals("boolean")) throw new WoolException(this,"Invalid return type of condition: "+e.asString());
		}
		List<String> returnTypes = new ArrayList<>();
		for (Expr e : results) {
			returnTypes.add(e.getReturnType(scope));
		}
		if (Program.lowestCommonClass(returnTypes)==null) throw new WoolException(this, "no shared return type!");
		return true;
	}

	@Override
	public String asString() {
		String str = "";
		str+="select\n";
		for (int i=0; i<conditions.size();i++) {
			str+="  "+conditions.get(i).asString()+" : "+results.get(i).asString()+"\n";
		}
		str+="end\n";
		return str;
	}
	public JBC writeJBC(JBC jbc,ClassDef currentClass, Scope scope) {
		IfExpr current=null;
		IfExpr base=null;
		Expr defaultReturn = jbc.getDefaultExpr(getReturnType(scope));
		for (int i = 0; i<conditions.size(); i++) {
			IfExpr newIf = new IfExpr(conditions.get(i),results.get(i),defaultReturn);
			if (base==null) {
				base = newIf;
				current=base;
			}else {
				current.cfalse = newIf;
				current = (IfExpr)current.cfalse;
			}
		}
		base.writeJBC(jbc, currentClass, scope);
		return jbc;
	}
	@Override
	public WObject evaluate(Scope scope) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void setSelfType(String newType) {
		for (Expr d : this.results) {
			d.setSelfType(newType);
		}
		for (Expr d : this.conditions) {
			d.setSelfType(newType);
		}
	}
	@Override
    public Expr cloneExpr() 
    { 
		List<Expr> conds = new ArrayList<>();
		List<Expr> resu = new ArrayList<>();
		for (Expr d : results) {
			resu.add(d.cloneExpr());
		}
		for (Expr d : conditions) {
			conds.add(d.cloneExpr());
		}
        return new SelectExpr(conds,resu); 
    }

}
