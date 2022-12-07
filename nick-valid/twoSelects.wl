class TwoSelects {
	twoSelects(): int {
		{
			# should parse as two selects, not one.
			select
				1 > 2: 5;
			end;
			select 
				1 > 4: 10;
			end;
		}
	}
}