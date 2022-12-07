class Parent {
	a: int <- 5;
	
	getA(): int {
		a
	}
}

class Child inherits Parent {
	b: int <- a;
	c: int <- this.getA();
}
