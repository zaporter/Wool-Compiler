package wool.structure;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import wool.structure.genericBlocks.constants.IntConstExpr;
import wool.structure.genericBlocks.constants.NewExpr;

public class Program {
	static Map<String, ClassDef> classes = new HashMap<>();
	private static String currentClass = null;
	public static boolean compilationFailed=false;
	static String outputDirectory = ".";
	static List<String> baseClasses = new ArrayList<>();
	// initialize program and setup basic classes
	public static void initialize() {
		classes.clear();
		currentClass=null;
		compilationFailed=false;
		
		addClass("Object",null);
		addClass("Str","Object");
		addClass("IO","Object");
		addClass("int",null);
		addClass("boolean",null);
		
		baseClasses.add("Object");
		baseClasses.add("Str");
		baseClasses.add("IO");
		baseClasses.add("int");
		baseClasses.add("boolean");
		
		Scope objScope=classes.get("Object").getScope();
		Scope ioScope=classes.get("IO").getScope();
		Scope strScope=classes.get("Str").getScope();
		VariableDef[] empty = new VariableDef[] {};
		objScope.addMethod(new MethodDef("abort","Object",empty, new NewExpr("Object")));
		objScope.addMethod(new MethodDef("typeName","Str",empty, new NewExpr("Str")));
		objScope.addMethod(new MethodDef("copy","SELF_TYPE",empty,new NewExpr("SELF_TYPE")));
		
		ioScope.addMethod(new MethodDef("outStr","SELF_TYPE",new VariableDef[] {new VariableDef("x","Str")}, new NewExpr("SELF_TYPE")));
		ioScope.addMethod(new MethodDef("outInt","SELF_TYPE",new VariableDef[] {new VariableDef("x","int")},new NewExpr("SELF_TYPE")));
		ioScope.addMethod(new MethodDef("outBool","SELF_TYPE",new VariableDef[] {new VariableDef("x","boolean")},new NewExpr("SELF_TYPE")));
		ioScope.addMethod(new MethodDef("inStr","Str",empty,new NewExpr("Str")));
		ioScope.addMethod(new MethodDef("inInt","int",empty,new NewExpr("int")));
		ioScope.addMethod(new MethodDef("inBool","boolean",empty, new NewExpr("boolean")));
		
		strScope.addMethod(new MethodDef("length","int",empty,new IntConstExpr(0)));
		strScope.addMethod(new MethodDef("concat","Str",new VariableDef[] {new VariableDef("s","Str")},new NewExpr("Str")));
		strScope.addMethod(new MethodDef("substr","Str",new VariableDef[] {
				new VariableDef("i","int"),
				new VariableDef("l","int")
		},new NewExpr("Str")));
	}
	// Current class used during tree walking. 
	public static void setCurrentClass(String s) {
		currentClass=s;
	}
	public static void setOutputDirectory(String s) {
		outputDirectory=s;
	}
	// Current class used during tree walking. 
	public static ClassDef getCurrentClass() {
		return classes.get(currentClass);
	}
	public static void generateSource() {
		for (Entry<String,ClassDef> d : classes.entrySet()) {
			if (stringContains(baseClasses,d.getKey())) {
				// base class. Don't generate code
			}else {
				JBC jbc = new JBC();
				d.getValue().writeJBC(jbc);
				try {
					jbc.write(outputDirectory+"/wool/"+d.getKey()+".class");
				}catch(IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	// Add a new class from the tree into the list of classes
	public static void addClass(String id, String parent) {
		if (stringContains(classes.keySet(),id)) {
			System.err.println("Cannot create duplicate class: "+id);
			Program.compilationFailed=true;
		}
		classes.put(id, new ClassDef(id,parent));
	}
	// verify the inheritence graph of the program
	public static boolean verifyInheritence() throws WoolException {
		for (String key : classes.keySet()) {
			if ((!parentExists(key)) || (!includesObjectInheritence(key))) {
				System.err.println("Inheritence validation failed for: "+key);
				
				return false;
			}
		}
		return true;
	}
	// perform type analysis and conform that all of the types are satisfied
	public static boolean isTypeSatisfied() throws WoolException{
		for (ClassDef d : classes.values()) {
			if (Program.compilationFailed) throw new WoolException("compilation terminated");
			if (!d.isTypeSatisfied()) {
				return false;
			}
		}
		return true;
	}
	// setup SELF_TYPE definitions in subclasses
	public static boolean setSelfTypes() throws WoolException{
		for (ClassDef d : classes.values()) {
			d.setSelfType();
		}
		return true;
	}
	// after the tree is built, it is possible to simply clone the methods and variables of the parents into the children.
	// this solves a bit of complexity around which symbol table to reference
	public static boolean setParentsScopes() {
		return setParentScope("Object");
	}
	// merge all of the children (if any) for a parent
	public static boolean setParentScope(String parent) {
		for (String key : classes.keySet()) {
			if (key.equals("Object") || key.equals("int") || key.equals("boolean")) continue;
			if (classes.get(key).parent.equals(parent)) {
				boolean success = classes.get(key).setParentScope(classes.get(parent).getScope());
				if (!success) {
					System.err.println("failed to merge inherenence of "+key);
					
					return false;
				}
				success = setParentScope(key);
				if (!success) {
					return false;
				}
			}
		}
		return true;
	}
	// returns true if the parent for an object is a valid parent
	private static boolean parentExists(String id) {
		if (id.equals("Object") || id.equals("int") || id.equals("boolean")) {
			return true;
		}
		if (classes.get(id).parent.equals("Str")) {
			System.err.println("Class: "+id+" inherits from Str. It is prohibited to inherit from Str");
			return false;
		}
		boolean val = classes.containsKey(classes.get(id).parent);
		if (!val) {
			System.err.println("Parent of "+id+" ("+classes.get(id).parent+") does not exist");
		}
		return val;
	}
	// List of all classes that a class inherits from
	private static List<String> generateInheritenceList(String id) throws WoolException{
		if (classes.get(id)==null) {
			throw new WoolException("Tried to access inheritence list of non-existent class: "+id);
		}
		String identifier=id;
		List<String> inheritenceList=new ArrayList<>();
		int count=0;
		while(identifier!=null && count++<150) {
			inheritenceList.add(identifier);
			identifier=classes.get(identifier).parent;
		}
		return inheritenceList;
	}
	// returns true iff for any object, this object includes "Object" in its inheritence list
	private static boolean includesObjectInheritence(String id) throws WoolException{
		if (id.equals("int") || id.equals("boolean")) return true;
		boolean val = stringContains(generateInheritenceList(id),"Object");
		if (val) {
			return true;
		}else {
			System.err.println(id+" does not inherit from Object. (Perhaps cyclical inheritence?)");
			return false;
		}
	}
	// Returns true if
	// A~A
	// A~B iff A is in the inheritance list of B
	// THIS IS NOT AN EQUIVALENCE RELATION. x~y ~= y~x
	// FIRST IS THE ONE WHERE THE OBJECT GOES. SECOND IS THE TESTED TYPE
	public static boolean typeEquiv(String id, String other) throws WoolException{
		
		return (id.equals(other) ||
				stringContains(generateInheritenceList(other),id));
	}
	// similar to lcd of two integers but for classes.
	// find the nearest class that they both share
	public static String lowestCommonClass(String a, String b) throws WoolException {
		if (a==null||b==null) return null;
		List<String> inhA = generateInheritenceList(a);
		List<String> inhB = generateInheritenceList(b);
		for (String s : inhA) {
			if (stringContains(inhB,s)) {
				return s;
			}
		}
		return null;
	}
	// lowest common class of a list of classes. (used for select)
	public static String lowestCommonClass(List<String> classes) throws WoolException {
		if (classes.size()==0) return null;
		if (classes.size()==1) return classes.get(0);
		String toReturn=classes.get(0);
		for (String s : classes) {
			toReturn=lowestCommonClass(toReturn,s);
		}
		return toReturn;
	}
	
	public static ClassDef getClass(String classDef) {
		return classes.get(classDef);
	}
	public static void printClasses() {
		for (ClassDef d : classes.values()) {
			System.out.println(d.asString());
		}
	}
	// (new Collection<String>).contains() uses equality, not .equals()
	private static boolean stringContains(Collection<String> a , String b) {
		for (String s : a) {
			if (s.equals(b)) return true;
		}
		return false;
	}
	public static String getTrueReturnType(String methodName, ClassDef currentClass) {
		if (currentClass.getScope().methods.containsKey(methodName)) {
			return currentClass.getScope().methods.get(methodName).desiredType;
		}else {
			return getTrueReturnType(methodName, Program.getClass(currentClass.parent));
		}
	}
	public static List<String> getTrueParamTypes(String methodName, ClassDef currentClass) {
		if (currentClass.getScope().methods.containsKey(methodName)) {
			MethodDef method = currentClass.getScope().methods.get(methodName);
			List<String> types = new ArrayList<>();
			for (VariableDef varDef : method.params) {
				types.add(varDef.getReturnType(currentClass.getScope()));
			}
			return types;
		}else {
			return getTrueParamTypes(methodName, Program.getClass(currentClass.parent));
		}
	}
}
