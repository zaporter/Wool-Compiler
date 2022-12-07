class BestIO inherits IO {
	outputHelloWorld(): BestIO {
		outStr("hello world")
	}
}

class Operator {
	a: IO <- (new IO).outStr("hello");
	b: BestIO <- (new BestIO).outStr("world");
}