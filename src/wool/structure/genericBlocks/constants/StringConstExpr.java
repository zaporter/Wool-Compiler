package wool.structure.genericBlocks.constants;

import wool.structure.ClassDef;
import wool.structure.Expr;
import wool.structure.JBC;
import wool.structure.Scope;
import wool.structure.WObject;

public class StringConstExpr implements Expr{
	String val="";
	public StringConstExpr(String in) {
		String last = "";
		String tmp=in.substring(1,in.length()-1);
		for (char c : tmp.toCharArray()) {
			if (last.equals("\\") && c=='n') {
				//val +="\n";
				c='\n';
			}else {
				val +=last;
			}
			
			last=c+"";
		}
		val += last;
	}
	@Override
	public String getReturnType(Scope scope) {
		// TODO Auto-generated method stub
		return "Str";
	}

	@Override
	public boolean isTypeSatisfied(Scope scope) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public WObject evaluate(Scope scope) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override 
	public String asString() {
		
		return "\""+val.replaceAll("\n", "\\\\n")+"\"";
	}
	@Override
	public void setSelfType(String newType) {
		
		
	}
	public JBC writeJBC(JBC jbc,ClassDef currentClass, Scope scope) {
		jbc.methodV.visitLdcInsn(val);
		return jbc;
	}
	@Override
    public Expr cloneExpr() 
    { 
        return new StringConstExpr(new String(" "+val+" ")); 
    }
}
