class A {
    make() : A { new A }
    # Static types do not match in the following method
    # Even a has static type A and c has static type C.
    # Even though this would execute, it is 
    # a semantic error.
    illegal() : C { c <- a }
    c : C <- new C;
    a : A <- c.make();  # this is really a C
}
class B inherits A {
    make() : B { new B }    # B ≤ A
}
class C inherits B {
    make() : B { new C }    # C ≤ B
    make1() : A { this }    # C ≤ A
}