package wool.structure;


public  interface ExprProperty extends Expr{
	// holdover class for MethodDef and VariableDef.
	// These are attributes of a class rather than simple expressions
	public String getUUID();
}
