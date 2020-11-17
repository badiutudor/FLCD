public class Identifier {
    // Review: I would have used getters and setters
    public String name;
    public Identifier(String name){
        this.name=name;
    }
    @Override
    public String toString() {
        return name;
    }
}
