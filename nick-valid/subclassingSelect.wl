class Parent {}
class Child inherits Parent {}
class Grandchild inherits Child {}
class Aunt inherits Parent {}
class ExtraIO inherits IO {}

class LUB {
	grandchildAndChild(): Child {
		select
			true: (new Child);
			false: (new Grandchild);
			false: (new Grandchild);
		end
	}

	allSubtypes(): Parent {
		select
			false: (new Child);
			false: (new Grandchild);
			false: (new Aunt);
		end
	}
	
	oneBranch(): Child {
		select
			true: (new Child);
		end
	}
	
	(*
	grandchildAndIO(): Object {
		if true then (new IO) else (new Grandchild) fi
	}

	grandchildAndGrandchild(): Grandchild {
		if true then (new Grandchild) else (new Grandchild) fi
	}
	
	extraIOAndIO(): IO {
		if true then (new ExtraIO).outStr("Hello") else (new IO) fi
	}

	extraIOAndGrandchild(): Object {
		if true then (new ExtraIO).outStr("Hello") else (new Grandchild) fi
	}
	*)
}