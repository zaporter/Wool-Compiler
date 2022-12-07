package wool.structure;

import java.util.ArrayList;
import java.util.List;

import org.objectweb.asm.Label;

import wool.structure.genericBlocks.constants.NullConstExpr;

public class MethodDef  implements ExprProperty{
	
	public Expr expression;
	String UUID;
	Scope scope;
	String desiredType;
	public List<VariableDef> params;
	public List<VariableDef> paramIntializationList; // methodBlock : LCURLY (lines+=varDecLine)* expr RCURLY ;
	public MethodDef (String UUID, String desiredType, VariableDef[] parameters, Expr value) {
		this.params=new ArrayList<>();
		this.paramIntializationList=new ArrayList<>();
		if (parameters!=null) {
			for (VariableDef d : parameters) {
				this.params.add(d);
			}
		}
		this.UUID=UUID;
		this.expression=value;
		this.desiredType=desiredType;
	}
	public MethodDef (String UUID, String desiredType, List<VariableDef> parameters, Expr value){
		this.UUID=UUID;
		this.paramIntializationList=new ArrayList<>();
		this.expression=value;
		this.desiredType=desiredType;
		if (parameters==null) {
			this.params=new ArrayList<>();
		}else {
			this.params=parameters;
		}
		
		
	}
	
	// returns true if the method signature of another method is equivalent to this one
	
	@Override
	public boolean equals(Object otherObj) {
		if (!(otherObj instanceof MethodDef)) {
			System.err.println("Not a methoddef");
			return false;
		}
		MethodDef other = (MethodDef) otherObj;
		if (!this.UUID.equals(other.UUID)) {
			System.err.println("Different Name");
			return false;
		}
		try {
			if (!this.desiredType.equals(other.desiredType)/*Program.typeEquiv(this.desiredType, other.desiredType)*/) {
				System.err.println("Different type");
				return false;
			}
		}catch(Exception e) {
			e.printStackTrace();
			Program.compilationFailed=true;
		
		}
		if (this.params.size()!=other.params.size()) {
			System.err.println("Differnt params "+this.params.size()+" "+other.params.size());
			for (VariableDef d : this.params) {
				System.err.println(d.asString());
			}
			
			return false;
		}
		for (int i=0; i<params.size(); i++) {
			if (!this.params.get(i).returnType.equals(other.params.get(i).returnType)) {
				System.out.println("paramx");
				return false;
			}
		}
		return true;
	}
		
	@Override
	public WObject evaluate(Scope scope) {
		return expression.evaluate(scope);
	}

	@Override
	public String getReturnType(Scope scope) {
		return desiredType;
	}

	@Override
	public String getUUID() {
		return UUID;
	}
	
	@Override
	public boolean isTypeSatisfied(Scope s) throws WoolException{
		Scope thisScope = new Scope();
		thisScope.parent=s;
		this.scope=thisScope;
		for (VariableDef d : params) {
			if (thisScope.getVarNonRecursive(d.UUID)!=null) {
				throw new WoolException(this, "Duplicate parameter name: "+d.UUID);
			}
			thisScope.addVariable(d);
		}
		for (VariableDef d : this.paramIntializationList) {
			for (VariableDef param : params) {
				if (d.getUUID().equals(param.UUID)) throw new WoolException(this, "illegal shadowing of method parameter in init list");
			}
			
			if (thisScope.getVar(d.UUID)!=null) {
				if (!d.isTypeSatisfied(thisScope)) throw new WoolException(this,"invalid param init "+d.asString());
				thisScope.addVariable(d);
			}else {
				thisScope.addVariable(d);
				if (!d.isTypeSatisfied(thisScope)) throw new WoolException(this,"invalid param init "+d.asString());

			}
			
			
			
			
			
		}
		if (!expression.isTypeSatisfied(thisScope)) throw new WoolException(this,"unsatisfied expression");
		if (!(expression.getReturnType(thisScope).equals("Object"))) {
			if (!Program.typeEquiv( this.getReturnType(thisScope),expression.getReturnType(thisScope))) throw new WoolException(this, " mismatched return type and expression type"
					+ " ("+this.getReturnType(thisScope)+") <- ("+expression.getReturnType(thisScope)+")");
		}
		return true;
	}
	public JBC writeJBC(JBC jbc,ClassDef currentClass, Scope scope) {
		jbc.dropLocalVars();
		jbc.inConstructor=false;
		String signature = "(";
		for (VariableDef d : this.params) {
			signature += jbc.getJBCTypeName(d.returnType);
			jbc.pushLocalVar(d.UUID);
		}
		signature += ")";
		signature += jbc.getJBCTypeName(desiredType);
		
		
		jbc.methodV = jbc.classW.visitMethod(ACC_PUBLIC, UUID, signature, null, null);
		jbc.methodV.visitCode();
		for (VariableDef d : this.paramIntializationList) {
			d.writeJBC(jbc, currentClass, this.scope);
		}
		this.expression.writeJBC(jbc, currentClass, this.scope);
		jbc.methodV.visitInsn(jbc.isNativeType(desiredType)?IRETURN:ARETURN);
		jbc.methodV.visitMaxs(200, 200);
		jbc.methodV.visitEnd();
		
		return jbc;
	}
	@Override 
	public String asString() {
		String str= this.UUID+"(";
		for (VariableDef d : this.params) {
			str+=d.asString();
			str+=",";
		}
		str+=") : "+this.desiredType + " (";
		for (VariableDef d : this.paramIntializationList) {
			str+=d.asString();
			str+=",";
		}
		str+=") ";
				
		str+=this.expression.asString();
		return str;
	}
	@Override
	public void setSelfType(String newType) {
		for (VariableDef d : this.paramIntializationList) {
			d.setSelfType(newType);
		}
		if (this.desiredType.equals("SELF_TYPE")) {
			this.desiredType=newType;
		}
		expression.setSelfType(newType);
		
	}
	@Override
    public Expr cloneExpr() {
		List<VariableDef> p = new ArrayList<>();
		List<VariableDef> pi = new ArrayList<>();
		for (VariableDef d : this.params) {
			p.add((VariableDef)d.cloneExpr());
		}
		for (VariableDef d : this.paramIntializationList) {
			pi.add((VariableDef) d.cloneExpr());
		}
		MethodDef m= new MethodDef(new String(UUID),new String(desiredType),p,expression.cloneExpr());
		m.paramIntializationList=pi;
		return m;
		
    }

}
