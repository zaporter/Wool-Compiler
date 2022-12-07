class A inherits C
{
	c: int <- 5;
	get(a: int): int
	{
		a: int <- 5;
		a
	}
}

class B inherits A
{
	get(b: int): A
	{
		new A
	}
}

class C inherits A
{
	c: int <- 5;
}