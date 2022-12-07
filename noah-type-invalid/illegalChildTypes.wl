class A
{
	get(b: B): B { new A}
	b: B <- new A;
	bb: B <- get(new A);
	bbb: B <- get(new B); #valid (though not really since get isn't valid)
}
class B inherits A {}