package automata;

import java.util.HashSet;
import java.util.Set;

public class State {
    protected int id;
    protected HashSet<Edge> edges;

    public State(){
        this.id = 0;
        this.edges = new HashSet<Edge>();
    }
    public State(int id){
        this.id = id;
        this.edges = new HashSet<Edge>();
    }
    public State(State state){
        this.id = state.id;
        this.edges = new HashSet<Edge>(state.edges);
    }
    public State Clone(){
        return new State(this);
    }

    public int getId(){
        return this.id;
    }
    public HashSet<Edge> getEdges(){
        return this.edges;
    }

    public Set<Integer> getNextIdBy(char c){
        Set<Integer> set = new HashSet<Integer>();
        for (Edge edge: edges){
            if (edge.condition == c)
                set.add(edge.nextid);
        }
        return set;
    }
}
