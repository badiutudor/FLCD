public class Node {
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
