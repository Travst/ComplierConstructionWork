package automata;

import RegexTree.RegexTree;
import RegexTree.SyntaxTree;

import java.util.*;

public class DFA extends NFA {
    public int begin;
    public Set<Integer> endset;
    public ArrayList<State> DFAState;//保存所有状态结点
    private static int stateid;//辅助记忆状态id
    public ArrayList<Character> allLetter;
    private Map<Set<Integer>, Integer> setIdMap;

    public DFA(){
        super();
        this.begin = super.start;
        this.endset = new HashSet<Integer>();
        this.DFAState = new ArrayList<State>();
        this.stateid = 1;//状态从1开始
        this.allLetter = new ArrayList<Character>();
        this.setIdMap = new HashMap<Set<Integer>, Integer>();
    }
    public DFA(String regex){
        super(regex);//initialize to NFA first
        this.begin = super.start;
        this.endset = new HashSet<Integer>();
        this.DFAState = new ArrayList<State>();
        this.stateid = 1;//状态从1开始
        SyntaxTree tree = new SyntaxTree(regex);
        this.allLetter = tree.getCharacter();
        this.setIdMap = new HashMap<Set<Integer>, Integer>();
        RexgexTreeDFA(tree.getRoot());
    }
    public void RexgexTreeDFA(RegexTree regexTree){
        //得到开始状态的eplison同态闭包
        Set<Integer> beginset = getClosure(this.begin, Edge.eplison);
        beginset.add(begin);
        //开始状态加入map<set,id>中
        setIdMap.clear();
        setIdMap.put(beginset, stateid);
        this.begin = stateid;
        NewState();//增加开始状态
        //更新setIdMap
        for(int i = 0;i < setIdMap.size();++i){
            Set<Integer> set = getKey(i+1);
            updateMapSet(set);
        }

    }
    private Set<Integer> getKey(int value){
        //通过传入的值value找到setIdMap里的key: set
        Set<Integer> set = new HashSet<Integer>();
        Iterator<Set<Integer>> iterator = setIdMap.keySet().iterator();
        while (iterator.hasNext()){
            set = iterator.next();
            if (setIdMap.get(set) == value)
                break;
        }
        return set;
    }
    private void updateMapSet(Set<Integer> set){
        State front = DFAState.get(setIdMap.get(set) - 1);
        State behind;
        for (char sym: allLetter){
            Set<Integer> tmp = getClosureEplisonNext(set, sym);
            if(tmp.size() == 0){
                continue;
            }
            else if (!setIdMap.containsKey(tmp)) {
                behind = NewState();
                setIdMap.put(tmp, stateid - 1);
                if(tmp.contains(super.end)){
                    endset.add(stateid - 1);
                }
            }
            else {
                behind = DFAState.get(setIdMap.get(tmp) - 1);
            }
            Connect(front, behind, sym);
        }
    }

    private State NewState(){
        //create new state and return
        State newstate = new State(stateid);
        DFAState.add(newstate);
        ++stateid;
        return newstate;
    }
    private void Connect(State front, State behind, char sym){
        // connect two states, edge is saved in the front state
        Edge newedge = new Edge(sym, behind.id);
        front.edges.add(newedge);
    }

    private Set<Integer> getClosure(int sid, char symbol){
        //输入参数状态id,得到经过symbol的闭包集合
        boolean[] visit = new boolean[allState.size()];
        Set<Integer> integerSet = new HashSet<>();
        String[] res = Closure(sid, symbol, visit).split(",");
        if(res != null) {
            for (String str : res) {
                if (str != null && !str.isEmpty()) {
                    integerSet.add(Integer.parseInt(str));
                }
            }
        }
        return integerSet;
    }
    private String Closure(int stid, char symbol, boolean[] visit){
        //某个状态开始深度遍历,得到经过 symbol 的 stateid 字符串(id用逗号分隔开)
        //所得结果不包括进入的 stateid
        String res = "";
        int sid = stid - 1;//stid 在数组中索引
        visit[sid] = true;
        Iterator<Edge> edgeIterator = allState.get(sid).edges.iterator();
        while (edgeIterator.hasNext()){
            Edge edge = edgeIterator.next();
            if (!visit[edge.nextid - 1] && edge.condition == symbol){
                res += edge.nextid + ",";
                res += Closure(edge.nextid, symbol, visit);
            }
        }
        return res;
    }

    private Set<Integer> getClosureEplisonNext(Set<Integer> set, char symbol){
        //求集合set对应字符symbol的闭包(包括eplison闭包)
        Set<Integer> integers = new HashSet<Integer>();
        for (int item: set){
            //求对应字符的闭包
            Set<Integer> tmp1 = getClosure(item, symbol);
            integers.addAll(tmp1);
            //在求完对应字符闭包后对所得集合求一次eplison闭包
            Set<Integer> tmp2 = getClosure(tmp1, Edge.eplison);
            integers.addAll(tmp2);
        }
        return integers;
    }

    private Set<Integer> getClosure(Set<Integer> set, char symbol){
        //求集合set对应字符的闭包
        Set<Integer> integers = new HashSet<Integer>();
        for (int item: set){
            Set<Integer> tempset = getClosure(item, symbol);
            integers.addAll(tempset);
        }
        return integers;
    }

    public ArrayList<State> MinDFA(){
        ArrayList<State> MinDFAState = new ArrayList<State>();
        Set<Integer> beginset = new HashSet<Integer>();
        for(State state: DFAState){
            if(!endset.contains(state.id))
                beginset.add(state.id);
        }

        ArrayList<Set<Integer>> groups = new ArrayList<Set<Integer>>();
        groups.add(beginset);
        groups.add(endset);
        for (int i = 0; i < groups.size(); ++i){
            Set<Integer> differ = new HashSet<Integer>();
            Set<Integer> same = groups.get(i);
            if (same.size() == 1){
                continue;
            }
            else {
                Iterator<Integer> iterator = same.iterator();
                int check = iterator.next();
                while (iterator.hasNext()){
                    int item = iterator.next();

                }
            }
        }
        setIdMap.clear();



        return MinDFAState;
    }
    private boolean isEqualState(State state1, State state2, ArrayList<Set> list){
        if (state1.edges.size() != state2.edges.size())
            return false;
        else {
            for (char cond: allLetter){
                int next1 = getStateNext(state1, cond);
                int next2 = getStateNext(state2, cond);
                if (next1 != 0  && next2 != 0){


                }
            }
        }
        return true;
    }
    private int getStateNext(State state, char cond){
        int next = 0;
        for (Edge edge: state.edges){
            if (edge.condition == cond){
                next = edge.nextid;
                break;
            }
        }
        return next;
    }
    private boolean setEqual(Set<Integer> set1, Set<Integer> set2){
        if(set1 == null || set2 == null){
            return false;
        }
        if(set1.size() != set2.size()){
            return false;
        }
        return set1.containsAll(set2);
    }
    private int IdInList(Set set, ArrayList<Set> list){
        int index = 0;
        while (index < list.size()){
            if (list.get(index).contains(set)){
                break;
            }
        }
        //if index == list.size(), then can't set doesn't contains in list
        return index;
    }

    public String getStartEnd(){
        String s = "StartID: " + begin + "\tEndID:";
        for (int i: endset){
            s += " "+ i;
        }
        return s;
    }
}
