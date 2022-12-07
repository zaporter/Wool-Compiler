class Incorrect {
	method(a: int): int {
		a
	}
	
	multiply(n: int, m: int): int {
		method(n, m)
	}
} 