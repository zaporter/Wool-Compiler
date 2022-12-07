class A {
   a : boolean <- true;
   f() : int {
	  b: int <- b; #this is fine, b is 0
      a : int <- a; #fails typecheck, a is a boolean
      b
    }
}