class A {
    x : int <- 0;
    locB : B <- new B;
    isNil() : boolean { false }
    print(str : Str) : Object {this}
}
class C inherits Bat {
    #isNil(cat: B) : boolean {false}
    #x : int;
}
class B inherits A{
    isNil() : boolean {true}
}
