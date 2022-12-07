class A
{
	get(): int
	{
		{
			#my typechecker should print an error for lines 7-19
			3 > 2 < 1;
			5 = true;
			false ~= "str";
			new A + "str";
			5 - "str";
			true + 3;
			~3;
			"str" = 3;
			-true;
			if 3 then 1 else 2 fi;
			if true then 1 else false fi;
			if true then new Object else false fi;
			while 1 loop 10 pool;
			while true loop "str" + 1 pool;
			select true: false; false: 1; end;
			select 3: true; 4: false; end;
			isnull 7;
		}
	}
}
			