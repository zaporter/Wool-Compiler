class A
{
	get(): int 
	{ 
		{if true then new C else new D fi; 5; }
	}
	get2(): int
	{
		{select false: new C; false: new D; true: new A; end; new Object.get(); 5; }
	}
}