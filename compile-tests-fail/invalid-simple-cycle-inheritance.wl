class A inherits C{
    x : int <- 0;
    locB : B <- new B;
    isNil() : boolean { false }
    print(str : Str) : Object {this}
}
class C inherits B {
    #isNil(cat: B) : boolean {false}
    #x : int;
}
class B inherits A{
    isNil() : boolean {true}
}
