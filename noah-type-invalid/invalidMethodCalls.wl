class A {
	get(a: int, b: Str, c: A): int {5}
}
class B inherits A {
	a: int <- get(5, "hello", new A); #valid
	e: int <- get(5, "hello", new B); #valid
	b: int <- get(5, "hello"); #wrong number of arguments
	c: int <- get("hello", 5, new A); #wrong order of arguments
	d: int <- get(true, "hello", new A); #wrong type of one argument
	f: int <- get(1, "hello", "str"); #wrong type of one argument
}