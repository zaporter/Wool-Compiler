class Parent {}
class Child inherits Parent {}
class Grandchild inherits Child {}
class Aunt inherits Parent {}
class ExtraIO inherits IO {}

class LUB {
	childAndChild(): Child {
		if true then (new Child) else (new Child) fi
	}

	childAndThis(): Object {
		if true then (new Child) else this fi
	}

	thisAndThis(): LUB {
		if true then this else this fi
	}

	grandchildAndChild(): Child {
		if true then (new Child) else (new Grandchild) fi
	}

	grandchildAndAunt(): Parent {
		if true then (new Aunt) else (new Grandchild) fi
	}
	
	grandchildAndIO(): Object {
		if true then (new IO) else (new Grandchild) fi
	}

	grandchildAndGrandchild(): Grandchild {
		if true then (new Grandchild) else (new Grandchild) fi
	}
	
	extraIOAndIO(): IO {
		if true then (new ExtraIO).outStr("Hello") else (new IO) fi
	}

	ioAndExtraIO(): IO {
		if true then (new IO) else (new ExtraIO).outStr("Hello") fi
	}

	extraIOAndGrandchild(): Object {
		if true then (new ExtraIO).outStr("Hello") else (new Grandchild) fi
	}
	
	nullAndLUB(): Object {
		if true then null else new LUB fi
	}

	lubAndNull(): Object {
		if true then (new LUB) else null fi
	}
}