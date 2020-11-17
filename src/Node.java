public class Node {
    // Review: I would have used getters and setters
    public Object value;
    public Node next;
    public Node(Object value){
        this.value=value;
        next=null;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
