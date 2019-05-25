package automata;

public class Edge {
    protected static final char eplison = 'ε';
    protected char condition;
    protected int nextid;

    public Edge(){
        this.condition = ' ';
        this.nextid = 0;
    }
    public Edge(char condition, int nextid){
        this.condition = condition;
        this.nextid = nextid;
    }
    public Edge(Edge edge){
        this.condition = edge.condition;
        this.nextid = edge.nextid;
    }
    public Edge Clone() {
        return new Edge(this);
    }

    @Override
    public String toString() {
        return "through "+condition+" to "+nextid;
    }
}
