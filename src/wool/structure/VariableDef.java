package wool.structure;

import wool.structure.genericBlocks.constants.NullConstExpr;

public class VariableDef  implements ExprProperty{
	 Expr expression;
	String UUID;
	String returnType;
	public VariableDef(String UUID, String returnType) {
		this.returnType=returnType;
		this.UUID=UUID;
		this.expression=null;
	}
	public VariableDef (String UUID, String returnType,Expr value){
		this.returnType=returnType;
		this.UUID=UUID;
		this.expression=value;
	}

	@Override
	public WObject evaluate(Scope scope) {
		return expression.evaluate(scope);
	}
	public JBC writeJBC(JBC jbc,ClassDef currentClass, Scope scope) {
		if (jbc.inConstructor) {
			jbc.methodV.visitVarInsn(ALOAD, 0);
			if (expression == null) {
				jbc.getDefaultExpr(returnType).writeJBC(jbc, currentClass, scope);
			}else {
				expression.writeJBC(jbc, currentClass,scope);
				String returnType = jbc.getJBCTypeNameReturn(expression.getReturnType(scope));
				if (!jbc.isNativeType(expression.getReturnType(scope))) {
					jbc.methodV.visitTypeInsn(CHECKCAST, returnType);
				}
			}
			String type = currentClass.UUID.equals("Str")?"java/lang/String":jbc.WOOL+currentClass.UUID;
			jbc.methodV.visitFieldInsn(PUTFIELD, type, UUID, 
						jbc.getJBCTypeName(returnType));
		}else {
			if (expression == null) {
				jbc.getDefaultExpr(returnType).writeJBC(jbc, currentClass, scope);
			}else {
				expression.writeJBC(jbc, currentClass,scope);
				String returnType = jbc.getJBCTypeNameReturn(expression.getReturnType(scope));
				if (!jbc.isNativeType(expression.getReturnType(scope))) {
					jbc.methodV.visitTypeInsn(CHECKCAST, returnType);
				}
			}
			jbc.methodV.visitVarInsn(jbc.isNativeType(returnType)?ISTORE:ASTORE, jbc.pushLocalVar(UUID));
		}
		
		return jbc;
	}

	@Override
	public String getReturnType(Scope scope) {
		return this.returnType;
	}

	@Override
	public String getUUID() {
		return UUID;
	}
	
	@Override
	public boolean isTypeSatisfied(Scope scope) throws WoolException{
		if (Program.getClass(returnType)==null) throw new WoolException(this,"No class "+returnType);
		if (expression!=null) {
			if (!expression.isTypeSatisfied(scope)) throw new WoolException(this, "type unsatisfied");
			if (this.expression.getReturnType(scope).equals("Object")) {
				if (!Program.typeEquiv("Object",returnType ))	throw new WoolException(this, "non-nullable type");
			}else {
				if (!Program.typeEquiv(getReturnType(scope), (expression.getReturnType(scope)))) throw new WoolException(this,"Unequal types for assignment ("+(expression.getReturnType(scope))+")");
			}
		}
		return true;
	}
	@Override 
	public String asString() {
		return this.UUID+" : "+this.returnType+(this.expression==null?"":"<-"+this.expression.asString());
	}
	@Override
	public void setSelfType(String newType) {
		if (this.returnType.equals("SELF_TYPE")) {
			this.returnType=newType;
		}
		if (this.expression!=null) {
			this.expression.setSelfType(newType);
		}
		
	}
	
    public VariableDef cloneExpr() 
    { 
        return new VariableDef(new String(UUID),new String(returnType),this.expression==null?null:this.expression.cloneExpr()); 
    }
	
}
