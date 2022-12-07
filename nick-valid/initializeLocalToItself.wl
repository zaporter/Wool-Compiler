class A {
	f(): int {
		a: int <- a;
		# should equal 1
		a + 1
	}
}