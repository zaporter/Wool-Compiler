package wool.structure;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import wool.structure.genericBlocks.constants.BoolConstExpr;
import wool.structure.genericBlocks.constants.IntConstExpr;
import wool.structure.genericBlocks.constants.NullConstExpr;
import wool.structure.genericBlocks.constants.StringConstExpr;

// Java Byte Code (Writer)
public class JBC implements Opcodes{
	public ZClassWriter classW = new ZClassWriter(0);
	public FieldVisitor fieldV;
	public MethodVisitor methodV;
	public boolean inConstructor = true;
	public final String WOOL = "wool/";
	public Map<String, Integer> localVars = new HashMap<>(); 
	public int varPos = 0;
	public boolean debug = true;
	public void debug(String s) {
		if (debug) System.out.println(s);
	}
	public boolean isNativeType(String s) {
		return s.equals("int") || s.equals("boolean");
	}
	public String getJBCTypeName(String s) {
		switch (s) {
		case "int":
			return "I";
		case "boolean":
			return "Z";
		case "Str":
			return "Ljava/lang/String;";
		default:
			return "L"+WOOL+s+";";
			//System.err.println("INVALID VAR DEF TYPE "+UUID+" "+expression.getReturnType(currentClass.getScope()));
		}
	}
	public String getJBCTypeNameReturn(String s) {
		switch (s) {
		case "int":
			return "int";
		case "boolean":
			return "boolean";
		case "Str":
			return "java/lang/String";
		default:
			return WOOL+s+"";
		}
	}
	public Expr getDefaultExpr(String type) {
		switch (type) {
		case "int":
			return new IntConstExpr(0);
		case "boolean":
			return new BoolConstExpr(false);
		case "Str":
			return new StringConstExpr("  ");
		default:
			return new NullConstExpr();
		}
	}
	public void dropLocalVars() {
		localVars.clear();
		varPos=0;
	}
	public int pushLocalVar(String name) {
		localVars.put(name,++varPos);
		
		return varPos;
	}
	public boolean containsLocalVar(String name) {
		return localVars.containsKey(name);
	}
	public int getLocalVar(String name) {
		return localVars.get(name);
	}
	public void write(String dir) throws IOException {
		FileOutputStream fos = new FileOutputStream(dir);
		byte[] code = classW.toByteArray();
		fos.write(code);
		fos.close();
	}
}
