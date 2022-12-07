class InvalidLUB {
	numbersAndObject(): Object {
		select
			true: 1;
			true: 1;
			false: new InvalidLUB;
		end
	}

	booleansAndObject(): Object {
		select
			true: false;
			true: false;
			false: new InvalidLUB;
			true: true;
		end
	}

	booleansAndInts(): Object {
		select
			true: 5;
			false: true;
			true: 5;
		end
	}
}