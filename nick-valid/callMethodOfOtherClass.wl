class Test {
	a(): int {
		5
	}
}

class Test2 {
	callA(): int {
		t: Test <- new Test;
		t.a()
	}
}