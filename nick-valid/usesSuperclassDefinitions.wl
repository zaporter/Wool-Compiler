class Parent {
	a: int;

	setA(value: int): int {
		a <- value
	}
}

class Child inherits Parent {
	setAToSeven(): int {
		this.setA(7)
	}
	
	getA(): int {
		a
	}
}