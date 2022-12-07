class Parent {}
class Child inherits Parent {}

class Assigner {
	p: Parent <- new Child;
	
	makeP(): Parent {
		new Child
	}
}