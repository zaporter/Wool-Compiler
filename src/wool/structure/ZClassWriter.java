package wool.structure;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;

public class ZClassWriter extends ClassWriter {

	public ZClassWriter(ClassReader classReader, int flags) {
		super(classReader, flags);
		// TODO Auto-generated constructor stub
	}
	public ZClassWriter(int flags) {
		super(flags);
		// TODO Auto-generated constructor stub
	}
	@Override
	public String getCommonSuperClass(String t1, String t2) {
		//System.out.println("GET COMMON SUPER CLASS "+t1+" "+t2);
		try {
			String out = Program.lowestCommonClass(t1.split("wool/")[1], t2.split("wool/")[1]);
			//System.out.println("COMMON: "+out);
			return "wool/"+out;
		} catch (WoolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "noCommonSuperClass"+t1+"-"+t2;
	}
}
