class A{
	get(): int {a}
	b: A <- undefined();
}
class B{
	a: A <- new A;
	get(): int { { c <- new A; a.undefined();} }
	get2(): C {new C}
	b: C;
}