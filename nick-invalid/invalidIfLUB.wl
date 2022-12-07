class InvalidLUB {
	intAndObject(): Object {
		if false then 5 else (new Object) fi
	}

	booleanAndObject(): Object {
		if false then false else (new Object) fi
	}

	intAndBool(): int {
		if false then false else 5 fi
	}
	
	intAndNull(): int {
		if false then 5 else null fi
	}

	nullAndInt(): int {
		if false then 5 else null fi
	}
}