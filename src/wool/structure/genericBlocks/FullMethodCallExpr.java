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

public class FullMethodCallExpr implements Expr{
	Expr localObject;
	String functionName;
	List<Expr> params;
	public FullMethodCallExpr(Expr localObject, String fname, List<Expr> params) {
		this.localObject=localObject;
		this.functionName=fname;
		
		this.params=params;
	}
	@Override
	public String getReturnType(Scope scope) {
		MethodDef func= Program.getClass(localObject.getReturnType(scope)).getScope().getMethod(functionName);
		return func.getReturnType(scope);
	}

	@Override
	public boolean isTypeSatisfied(Scope scope)throws WoolException {
		if (!localObject.isTypeSatisfied(scope)) throw new WoolException(this," object reference unsatisfied : "+localObject.asString());
		if (Program.getClass(localObject.getReturnType(scope))==null) throw new WoolException(this, " return type mismatch");
		MethodDef func= Program.getClass(localObject.getReturnType(scope)).getScope().getMethod(functionName);
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
	public JBC writeJBC(JBC jbc,ClassDef currentClass, Scope scope) {
		String funcName = this.functionName;
		if (localObject.getReturnType(scope).equals("Str")&& this.functionName.equals("substr")) {
			funcName="substring";
		}
		localObject.writeJBC(jbc, currentClass, scope);
		List<String> trueTypes = Program.getTrueParamTypes(this.functionName, Program.getClass(this.localObject.getReturnType(scope)));
		if (trueTypes.size()!=this.params.size()) {
			System.err.println("Unequal param and trueType sizes. FullMethodCall: writeJBC");
		}
		int i =0;
		String signature = "(";
		for (Expr e : this.params) {
			e.writeJBC(jbc, currentClass, scope);
			String trueType = trueTypes.get(i++);
			String returnType = jbc.getJBCTypeNameReturn(trueType);
			//if (!jbc.isNativeType(trueType)){
			//	jbc.methodV.visitTypeInsn(CHECKCAST, returnType);
			//}
			signature += jbc.getJBCTypeName(trueType);
		}
		
		/*for (Expr d : this.params) {
			signature += jbc.getJBCTypeName(d.getReturnType(scope));
		}*/
		signature += ")";
		signature += jbc.getJBCTypeName(Program.getTrueReturnType(this.functionName, Program.getClass(this.localObject.getReturnType(scope))));
		//System.out.println(signature);
		String returnType = localObject.getReturnType(scope).equals("Str")?"java/lang/String":jbc.WOOL+localObject.getReturnType(scope);
		jbc.methodV.visitMethodInsn(INVOKEVIRTUAL, returnType, funcName, signature,false);
		if (!Program.getTrueReturnType(this.functionName, Program.getClass(this.localObject.getReturnType(scope))).equals(this.getReturnType(scope))) {
			//System.out.println("TYPECAST ADDED FROM : "+Program.getTrueReturnType(this.functionName, Program.getClass(this.localObject.getReturnType(scope)))+ "to "+this.getReturnType(scope));
			jbc.methodV.visitTypeInsn(CHECKCAST, jbc.WOOL+this.getReturnType(scope));
		}
		return jbc;
	}

	@Override
	public String asString() {
		String str="";
		str+=this.localObject.asString();
		str+=".";
		str+=this.functionName;
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
	@Override
	public void setSelfType(String newType) {
		this.localObject.setSelfType(newType);
		for (Expr d : this.params) {
			d.setSelfType(newType);
		}
	}
	@Override
    public Expr cloneExpr() 
    { 
		List<Expr> exp = new ArrayList<>();
		for (Expr e : this.params) {
			exp.add(e.cloneExpr());
		}
		
        return new FullMethodCallExpr(localObject.cloneExpr(),new String(functionName),exp); 
    }

}
