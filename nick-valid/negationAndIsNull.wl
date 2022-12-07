class BooleanExpressions {
	doSomething(): Str {
		obj: Object;
		isThisNonNull: boolean <- ~isnull obj;
		# spoiler alert, this should return that it's null
		if ~isThisNonNull then "It's null!" else "It's not null, for some reason" fi
	}
}