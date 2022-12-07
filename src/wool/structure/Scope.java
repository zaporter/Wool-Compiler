package wool.structure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.objectweb.asm.Label;

import jdk.internal.org.objectweb.asm.Opcodes;


public class Scope implements Opcodes{
	Scope parent = null;
	List<VariableDef> variables = null;
	Map<String, MethodDef> methods = null;
	
	public Scope() {
		this.methods=new HashMap<>();
		this.variables=new ArrayList<>();
	}
	public void addVariable(VariableDef var) {
		variables.add(var);
	}
	public void addMethod(MethodDef var) {
		methods.put(var.getUUID(), var);
	}
	public VariableDef getVarNonRecursive(String uuid) {
		for (VariableDef v : variables) {
			if (v.getUUID().equals(uuid)) {
				return v;
			}
		}
		return null;
	}
	public JBC writeJBC(JBC jbc, ClassDef currentClass, Scope scope) {
		for (VariableDef d : variables) {
			jbc.fieldV=jbc.classW.visitField(ACC_PROTECTED, d.getUUID(), jbc.getJBCTypeName(d.returnType), null, null);
			jbc.fieldV.visitEnd();
		}
		jbc.methodV = jbc.classW.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
		jbc.methodV.visitCode();
		Label startLabel = new Label();
		jbc.methodV.visitLabel(startLabel);
		jbc.methodV.visitVarInsn(ALOAD, 0);
		jbc.methodV.visitMethodInsn(INVOKESPECIAL,jbc.WOOL+currentClass.parent,"<init>","()V",false);
		for (VariableDef d : this.variables) {
			d.writeJBC(jbc, currentClass,scope);
		}
		jbc.methodV.visitInsn(RETURN);
		Label endLabel = new Label();
		jbc.methodV.visitLabel(endLabel);
		jbc.methodV.visitMaxs(200, 200);
		jbc.methodV.visitEnd();
		for (MethodDef d : this.methods.values()) {
			d.writeJBC(jbc, currentClass, scope);
		}
		return jbc;
	}
	// travel up parent scopes to discover variable
	public VariableDef getVar(String uuid) {
		VariableDef var = getVarNonRecursive(uuid);
		return (var!=null || parent==null) ? var : parent.getVar(uuid);
	}
	public String getVarType(String uuid) {
		return getVar(uuid).returnType;
	}
	public MethodDef getMethodNonRecursive(String uuid) {
		return methods.get(uuid);
	}
	// travel up parent scopes to discover variable;
	public MethodDef getMethod(String uuid) {
		MethodDef var = methods.get(uuid);
		return (var!=null || parent==null) ? var : parent.getMethod(uuid);
	}
	public String asString() {
		String str = "";
		str+="  Variables:\n";
		for (VariableDef d : this.variables) {
			str+="    "+d.asString()+"\n";
		}
		str+="  Methods:\n";
		for (MethodDef d : this.methods.values()) {
			str+="    "+d.asString()+"\n";
		}
		return str;
	}
	
	public Scope clone() {
		Scope toReturn = new Scope();
		for (VariableDef d : this.variables) {
			toReturn.addVariable(d.cloneExpr());
		}
		for (MethodDef d : this.methods.values()) {
			toReturn.addMethod((MethodDef)d.cloneExpr());
		}
		if (this.parent!=null) {
			toReturn.parent=this.parent.clone();
		}
		return toReturn;
	}
	public void setupSelfType(String type) {
		for (VariableDef d : this.variables) {
			d.setSelfType(type);
		}
		for (MethodDef d : this.methods.values()) {
			d.setSelfType(type);
		}
		if (this.parent!=null) {
			this.parent.setupSelfType(type);
		}
	}
	
	
	
	
}
