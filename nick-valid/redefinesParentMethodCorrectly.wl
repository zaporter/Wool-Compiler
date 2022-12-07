class Parent {
	n(a: int): int {
		a
	}
}

class Child inherits Parent {
	n(a: int): int {
		a * 2
	}
	
	callN(): int {
		this.n(5)
	}
}