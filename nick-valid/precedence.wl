class Precedence {
	run(): boolean {
		# The ~ at the front is to test that this precedence overrides everything.
		~1 + 2 * (3 + 4) <= 4 * 5 + -3 / 4 - 1
	}
	
	notRun(): boolean {
		result: boolean <- false;
		# Is used to check that ~ has lower precedence than ., and that assignment is lower than either
		result <- ~this.run()
	}
}