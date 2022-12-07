class Class1 {
	a: int <- 5;
	b: Str;

	getB(): Str {
		b
	}
}

class Class2 {
	foo: Class1;

	double(n: int): int {
		factor: int <- 2;
		result: int <- n * 2;
		result
	}

	mult(n: int, m: int): int {
		n * m
	}
}

class Class3 inherits Class1 {
}
