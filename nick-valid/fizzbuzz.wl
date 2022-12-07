class FizzBuzz inherits IO {
	run(): int { 
		n: int <- 0;
		{
			while n < 100 loop {
				if n / 3 * 3 = n then
					{
						outStr("fizz");
						if n / 5 * 5 = n then
							outStr("buzz")
						else
							null
						fi;
						outStr("\n");
					}
				else
					if n / 5 * 5 = n then
						outStr("buzz\n")
					else 
						# Test the targeted method call here, too
						this.outInt(n)
					fi
				fi;
				n <- n + 1;
			} pool;
			0;
		}
	}
}
