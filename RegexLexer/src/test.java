import RegexTree.RegexTree;
import RegexTree.SyntaxTree;
import automata.DFA;
import automata.Edge;
import automata.NFA;
import automata.State;

import java.util.*;

public class test{
    private static void visitTree(RegexTree tree){
        if(tree != null) {
            visitTree(tree.getLeft());
            visitTree(tree.getRight());
            System.out.print(tree.getSymbol());
        }

    }
    private static void testOutput(Iterator<State> stateIterator){
        while (stateIterator.hasNext()){
            State state = stateIterator.next();
            Iterator<Edge> edgeIterator = state.getEdges().iterator();
            String string = "";
            string+=state.getId() +" ";
            while (edgeIterator.hasNext()){
                Edge edge = edgeIterator.next();
                string += edge.toString();
            }
            string += "\n";
            System.out.print(string);
        }
    }
    private static void vectest(){
        ArrayList<ArrayList<Integer>> vvi = new ArrayList<ArrayList<Integer>>();
        ArrayList<Integer> v1 = new ArrayList<Integer>(),
                v2 = new ArrayList<Integer>(),
                v3 = new ArrayList<Integer>();
        for (int i = 0; i<3; ++i){
            v1.add(i);
            v3.add(i);
        }
        for (int i = 3; i < 6; ++i){
            v2.add(i);
        }
        vvi.add(v1);vvi.add(v2);
        if(!vvi.contains(v3)) vvi.add(v3);
        for (ArrayList<Integer> vi: vvi){
            String s = "";
            for (int i: vi){
                s+=i;
            }
            System.out.print(s+"\n");
        }
    }
    private static void print(String str){
        System.out.print(str);
    }

    public static void main(String[] args){
        String str = "a(a|b)*ab";
        SyntaxTree tree = new SyntaxTree(str);
        visitTree(tree.getRoot());
//        NFA nfa = new NFA(str);
//        Iterator<State> stateIterator = nfa.allState.iterator();
        /*DFA dfa = new DFA(str);
        Iterator<State> stateIterator = dfa.DFAState.iterator();
        testOutput(stateIterator);
        print("endstate: ");
        for (int i: dfa.endset){
            print(Integer.toString(i));
        }*/
    }
}
