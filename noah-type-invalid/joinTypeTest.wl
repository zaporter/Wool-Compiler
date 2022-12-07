class A {}
class B inherits A{}
class C inherits A{}
class D inherits A{}
class E inherits B{}
class F inherits B{}
class G inherits C{}
class H inherits F{

	main(): A{
		b: B <- select true : new E;
		 false : new H; 
		 true : new F; end; #ok
		e: E <- select true : new E;
		 false : new H; 
		 true : new F; end; #not ok (since static type is "B")
		a: A <- select true : new C; false : new H; end; #ok
		c: C <- select true: new C; false : new H; end; #not ok, static type is A
		o: Object <- select true: new B; false: "string" ; a ~= b : new F; end; #can it find Object?
		b
	}

}