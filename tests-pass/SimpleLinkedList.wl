class SinglyLinkedList {
    head : Node;
           pushInt(val : int) : Node {
                head.push(new Node.init())
           }
}
class Node {
    next : Node <-null;
    val : Object;

    hasNext() : boolean {~(isnull next)}
    push(next_ : Node) : Node {
        {
            if hasNext() then next.push(newNext) else next <- next_ fi;
            this;
        }
    }
      init(val_ : Object) : Node {
          {
            val <- val_;
            this;
          }
      }
}




