class BadComparisons {
	stringAndNumber(): boolean {
		"a" >= 5
	}

	stringAndNumberEq(): boolean {
		"a" = 5
	}

	chainLT(): boolean {
		4 < 5 > 5
	}
}