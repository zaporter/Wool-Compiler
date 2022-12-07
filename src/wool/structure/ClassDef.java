package wool.structure;

import java.util.Map.Entry;

import org.objectweb.asm.ClassWriter.*;
import org.objectweb.asm.*;
import java.io.*;


public class ClassDef implements Opcodes{
	public String parent;
	private Scope scope;
	public String UUID;
	public ClassDef(String UUID, String parent) {
		this.UUID=UUID;
		this.parent=parent;
		this.scope = new Scope();
	}
	public void addVariable(VariableDef v) {
		if (scope.getVar(v.UUID)!=null) {
			System.err.println("Duplicate var "+v.asString()+" detected in "+this.UUID);
			Program.compilationFailed=true;
		}
		scope.addVariable(v);
	}
	public void addMethod(MethodDef m) {
		if (scope.getMethodNonRecursive(m.UUID)!=null) {
			System.err.println("Duplicate method identifier in same class ["+m.asString()+"]");
			Program.compilationFailed=true;
		}
		scope.addMethod(m);
	}
	public Scope getScope() {
		return scope;
	}
	public JBC writeJBC(JBC jbc) {
		jbc.classW = new ZClassWriter(ClassWriter.COMPUTE_FRAMES + ClassWriter.COMPUTE_MAXS);
		jbc.classW.visit(V1_8, ACC_PUBLIC+ACC_SUPER, jbc.WOOL+this.UUID, null,jbc.WOOL+this.parent, null);
		this.scope.writeJBC(jbc,this,this.scope);
		jbc.classW.visitEnd();
		return jbc;
	}
	public boolean setParentScope(Scope other_in) {
		Scope other = other_in.clone();
		other.setupSelfType(UUID); // UGH
		for (VariableDef entry : scope.variables) {
			if (other.getVar(entry.getUUID())!=null) {
				System.err.println("Duplicate variable in child as in parent. Variable: "+entry.getUUID()+" Class: "+UUID);
				return false;
			}
		}
		for (Entry<String, MethodDef> entry : scope.methods.entrySet()) {
			if (other.getMethod(entry.getKey())!=null) {
				MethodDef childMethod=(MethodDef)scope.getMethodNonRecursive(entry.getKey()).cloneExpr();
				MethodDef parentMethod = (MethodDef)other.getMethod(entry.getKey()).cloneExpr();
				childMethod.setSelfType(UUID);
				parentMethod.setSelfType(UUID);
				if (! childMethod.equals(parentMethod)) {
					System.err.println("Tried to override parent method but the method signatures are different!");
					System.err.println("Class: "+UUID+" method: "+entry.getKey());
					return false;
				}
				
				
			}
		}
		this.scope.parent=other;
		return true;
	}
	
	public boolean isTypeSatisfied() throws WoolException {
		for (VariableDef d : scope.variables) {
			if (!d.isTypeSatisfied(scope)) {
				throw new WoolException("VariableDef "+d.asString()+" is unsatisfied" );
			}
		}
		for (MethodDef d : scope.methods.values()) {
			if (!d.isTypeSatisfied(scope)) {
				throw new WoolException("MethodDef "+d.asString()+" is unsatisfied" );
			}
		}
		return true;
	}
	public String asString() {
		String str="";
		str+="ClassDef: "+this.UUID+" parent: "+this.parent+"\n";
		str+=this.scope.asString();
		return str;
		
	}
	public void setSelfType() {
		this.scope.setupSelfType(UUID);
	}
	
	
}
