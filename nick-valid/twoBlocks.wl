class TwoBlocks {
	twoBlocks(): int {
		{
			# These should not somehow be combined into one block bc of greediness
			{
				10;
			};
			{
				100;
				0;
			};
		}
	}
}