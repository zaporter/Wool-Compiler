class B inherits A
{
	get(b: int): A
	{
		new A
	}
}

class A
{
    get(a: int) : B
    {
	  	new B
    }
}
