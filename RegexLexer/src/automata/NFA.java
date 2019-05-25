package automata;

import RegexTree.RegexTree;
import RegexTree.SyntaxTree;

import java.util.ArrayList;

public class NFA {
    public int start;//开始状态id
    public int end;//结束状态id
    public ArrayList<State> allState;//保存所有状态结点
    private static int stateid;//辅助记忆状态id

    public NFA(){
        this.start = 0;
        this.end = 0;
        this.allState = new ArrayList<State>();
        this.stateid = 1;//状态从1开始
    }
    public NFA(String regex){
        SyntaxTree tree = new SyntaxTree(regex);
        this.start = 0;
        this.end = 0;
        this.allState = new ArrayList<State>();
        this.stateid = 1;//状态从1开始
        RegexTreeNFA(tree.getRoot());
    }
    public NFA(RegexTree regexTree){
        this.start = 0;
        this.end = 0;
        this.allState = new ArrayList<State>();
        this.stateid = 1;//状态从1开始
        RegexTreeNFA(regexTree);
    }
    public void RegexTreeNFA(RegexTree regexTree){
        if (regexTree != null){
            Automata automata = StartEnd(regexTree);
            this.start = automata.begin.id;
            this.end = automata.end.id;
        }
    }
    //辅助内部类，保存某个阶段的开始状态和结束状态
    public class Automata {
        public State begin;
        public State end;

        public Automata(){
            this.begin = null;
            this.end = null;
        }
        public Automata(State begin,State end){
            this.begin = begin.Clone();
            this.end = end.Clone();
        }
        public Automata Clone(){
            return new Automata(this.begin,this.end);
        }
    }

    //create new state and return
    private State NewState(){
        State newstate = new State(stateid);
        allState.add(newstate);
        ++stateid;
        return newstate;
    }
    // connect two states, edge is saved in the front state
    private void Connect(State front, State behind, char sym){
        Edge newedge = new Edge(sym, behind.id);
        front.edges.add(newedge);
    }
    private void Connect(State front, State behind){
        Edge newedge = new Edge(Edge.eplison, behind.id);
        front.edges.add(newedge);
    }// 缺省 char 值, 默认连接 eplison 边

    private Automata StartEnd(RegexTree regexTree){
        Automata automata = null;
        if(regexTree != null){
            char symbol = regexTree.getSymbol();
            if(Character.isLetter(symbol)){
                //扫描到字符,建立两个状态,连接开始和结束状态
                State begin = NewState();
                State end = NewState();
                Connect(begin, end, symbol);

                automata = new Automata(begin, end);
            }
            else if(symbol == '|'){
                //或操作,建立两个新状态,新开始态分别连接左右子树的开始态
                // 左右子树的结束态分别连接新结束态连接
                Automata left = StartEnd(regexTree.getLeft());
                Automata right = StartEnd(regexTree.getRight());
                State newbegin = NewState();
                State newend = NewState();
                Connect(newbegin, left.begin);
                Connect(newbegin, right.begin);
                //不能直接拿返回的 Autumata 的 state 当头结点,否则会出现没有赋值问题
                State leftend = allState.get(left.end.id - 1);
                State rightend = allState.get(right.end.id - 1);
                Connect(leftend, newend);
                Connect(rightend, newend);

                automata = new Automata(newbegin, newend);
            }
            else if(symbol == '.'){
                Automata left = StartEnd(regexTree.getLeft());
                Automata right = StartEnd(regexTree.getRight());
                //
                State leftend = allState.get(left.end.id - 1);
                State rightbegin = allState.get(right.begin.id - 1);
                Connect(leftend, rightbegin);

                automata = new Automata(left.begin, right.end);
            }
            else if(symbol == '*'){
                Automata left = StartEnd(regexTree.getLeft());
                //
                State leftbegin = allState.get(left.begin.id - 1);
                State leftend = allState.get(left.end.id - 1);
                Connect(leftend, leftbegin);
                State newbegin = NewState();
                State newend = NewState();
                Connect(newbegin, leftbegin);
                Connect(newbegin, newend);
                Connect(leftend, newend);

                automata = new Automata(newbegin, newend);
            }
        }
        return automata;
    }

    public String getStartEnd(){
        return "StartID: "+start+"\tEndID: "+end;
    }
}
