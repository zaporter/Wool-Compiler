package wool.structure;

public class WoolException extends Exception {
 public WoolException(String s) {
	 super(s);
 }
 public WoolException(Expr expr, String s) {
	 super("["+expr.asString()+"] "+s);
 }
}
