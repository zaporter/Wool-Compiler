class Parent {
	a(b: Object): Object {
		b
	}
}

class Child inherits Parent {
	a(b: Str): Object {
		b
	}
}

class GrandChild inherits Child {
	a(b: Object, c: Str): Object
	{
		c
	}
}