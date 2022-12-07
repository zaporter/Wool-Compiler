package wool.structure.genericBlocks;

import java.util.ArrayList;
import java.util.List;

import wool.structure.ClassDef;
import wool.structure.Expr;
import wool.structure.JBC;
import wool.structure.MethodDef;
import wool.structure.Program;
import wool.structure.Scope;
import wool.structure.VariableDef;
import wool.structure.WObject;
import wool.structure.WoolException;

public class LocalMethodCallExpr implements Expr {
	String funcName;
	List<Expr> params;
	public LocalMethodCallExpr(String funcName, List<Expr> params) {
		this.funcName=funcName;
		this.params=params;
	}

	@Override
	public String getReturnType(Scope scope) {
		MethodDef func= scope.getMethod(funcName);
		return func.getReturnType(scope);
	}

	@Override
	public boolean isTypeSatisfied(Scope scope) throws WoolException{
		MethodDef func= scope.getMethod(funcName);
		if (func==null) throw new WoolException(this, "cannot find function");
		if (func.params.size()!=params.size()) throw new WoolException(this,"Mismatched parameter count");
		for (int index =0; index<params.size(); index++) {
			Expr d = params.get(index);
			VariableDef other = func.params.get(index);
			if (!d.isTypeSatisfied(scope)) throw new WoolException(this, "unsatisfied/invalid paramter");
			if (!(Program.typeEquiv(other.getReturnType(scope),d.getReturnType(scope)))) throw new WoolException(this," Mismatched parameter types. "+d.asString()+" vs "+other.asString());
		}
		return true;
	}

	@Override
	public String asString() {
		String str="";
		str+=this.funcName;
		str+="(";
		for (Expr d : this.params) {
			str+=d.asString();
			str+=",";
		}
		str+=")";
		return str;
	}

	@Override
	public WObject evaluate(Scope scope) {
		// TODO Auto-generated method stub
		return null;
	}
	public JBC writeJBC(JBC jbc,ClassDef currentClass, Scope scope) {
		jbc.methodV.visitVarInsn(ALOAD, 0);
		for (Expr e : this.params) {
			e.writeJBC(jbc, currentClass, scope);
		}
		String signature = "(";
		for (Expr d : this.params) {
			signature += jbc.getJBCTypeName(d.getReturnType(scope));
		}
		signature += ")";
		//signature += jbc.getJBCTypeName(getReturnType(scope));
		signature += jbc.getJBCTypeName(Program.getTrueReturnType(this.funcName, currentClass));
		//System.out.println(signature);
		//System.out.println(Program.getTrueReturnType(this.funcName, currentClass));
		jbc.methodV.visitMethodInsn(INVOKEVIRTUAL, jbc.WOOL+currentClass.UUID, this.funcName, signature,false);
		if (!Program.getTrueReturnType(this.funcName, currentClass).equals(this.getReturnType(scope))) {
			jbc.methodV.visitTypeInsn(CHECKCAST, jbc.WOOL+this.getReturnType(scope));
		}
		return jbc;
	}
	@Override
	public void setSelfType(String newType) {
		for (Expr d : this.params) {
			d.setSelfType(newType);
		}
		
	}
	@Override
    public Expr cloneExpr() 
    { 
		List<Expr> exp = new ArrayList<>();
		for (Expr d : this.params) {
			exp.add(d.cloneExpr());
		}
        return new LocalMethodCallExpr(new String(this.funcName), exp); 
    }


}
