class Exprs {
	add(n: int, m: int): int {
		n + m
	}

	sub(q: int, r: int): int {
		q - r
	}

	div(q: int, r: int): int {
		q / r
	}

	dne(): boolean {
		1 ~= 2
	}

	geq(): boolean {
		10 >= 5
	}

	complicated(): boolean {
		1 + 2 * 3 < 4 * 5 + 3
	}

	stringLiteral(): Str {
		"hello world"
	}

	trueValue: boolean <- true;
	falseValue: boolean <- false;
	nullValue: Object <- null;

	isNegative(n: int): boolean {
		n <= -1
	}

	not(bool: boolean): boolean {
		~bool
	}

	isNull(obj: Object): boolean {
		isnull obj
	}

	createString(): Str {
		new Str
	}

	boolToInt(b: boolean): int {
		if b then 1 else 0 fi
	}

	# halt the program by looping forever
	halt(): Object {
		while true loop "oops" pool
	}

	selectSomething(n: int): int {
		select
			n = 3 : 5;
			n = 5 : 3;
			n > 100: -1;
		end
	}

	capnBlock(): Str {
		{
			1;
			2;
			3;
			"Oops! All integers!";
		}
	}

	isFiveNegative(): boolean {
		isNegative(5)
	}

	addFourNumbers(n: int, m: int, q: int, r: int): int {
		add(n, m) + this.add(q, r)
	}
}
