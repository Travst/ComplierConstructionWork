package RegexTree;

public class RegexTree {
    protected char symbol;
    protected RegexTree left;
    protected RegexTree right;

    public RegexTree(char c){
        symbol = c;
        left = null;
        right = null;
    }

    public char getSymbol(){
        return symbol;
    }
    public RegexTree getLeft(){
        return left;
    }
    public RegexTree getRight(){
        return right;
    }
}
