class Arith {
	addFiveAndSeven(): int {
		7 + 5
	}

	chainAddition(): int {
		7 + 5 + 10 + 4
	}

	subFiveAndSeven(): int {
		5 - 7
	}
	
	divFiveAndSeven(): int {
		5 / 7
	}

	multFiveAndSeven(): int {
		5 * 7
	}

	compareFiveAndSeven(): boolean {
		7 > 5
	}

	compareTrueAndTrue(): boolean {
		true = true
	}

	correctChainedComparisonAssociativity(): boolean {
		true = 5 < 6 * 8
	}

	correctChainedComparisonAssociativity2(): boolean {
		5 < 6 * 8 = true
	}
	
	canNegateNumber(): int {
		-69
	}

	canNegateBoolean(): boolean {
		~5 < 6
	}
}