package RegexTree;

import java.util.Iterator;
import java.util.ArrayList;

/*
RE -> RE'|'RT | RT
RT -> RT RP | RP
RP -> RP* | RF
RF -> (RE) | alpha

RE -> RT {'|' RT}
RT -> RP {'.' RP}
RP -> RF{'*'}
RF -> (RE) | alpha
*/
public class SyntaxTree {
    private RegexTree root;
    private char token;
    private ArrayList<Character> regexStr;
    private ArrayList<Character> character;
    private Iterator iterator_rs;

    private void PreProcess(String str){
        //预处理，将输入正则表达式转换为带连接符. 的正则表达式,存于ArrayList中(便于迭代)
        //同时取第一个token, 取出正则表达式中的字符
        regexStr = new ArrayList<Character>();
        character = new ArrayList<Character>();
        int length = str.length();
        for (int i = 0; i < length; ++i){
            char strAti = str.charAt(i);
            //输入正则表达式转换为带连接符. 的正则表达式
            if(i + 1 < length){
                if(Character.isLetter(strAti) &&
                        (Character.isLetter(str.charAt(i+1))||str.charAt(i+1)=='(')){
                    // ab | a(
                    regexStr.add(strAti);
                    regexStr.add('.');
                }
                else if(strAti==')' &&
                        (Character.isLetter(str.charAt(i+1))||str.charAt(i+1)=='(')){
                    // )a | )(
                    regexStr.add(strAti);
                    regexStr.add('.');
                }
                else if(strAti=='*' &&
                        (Character.isLetter(str.charAt(i+1))|| str.charAt(i+1)=='(')){
                    // *a | *(
                    regexStr.add(strAti);
                    regexStr.add('.');
                }
                else {
                    regexStr.add(strAti);
                }
            }
            else if(i + 1 == length){
                regexStr.add(strAti);
            }
            //存入不重复的字母到character中
            if(Character.isLetter(strAti) && !character.contains(strAti)){
                character.add(strAti);
            }
        }
        iterator_rs = regexStr.iterator();
        getToken();
    }

    private void getToken(){
        if (iterator_rs.hasNext()){
            token = (char)iterator_rs.next();
        }
        else
            token = ' ';
    }
    private void error(char t, char expect){
        System.out.println(t+"is not match "+expect);
    }
    private void match(char expectedToken){
        if(token == expectedToken){
            getToken();
        }
        else {
            error(token,expectedToken);
        }
    }

    private RegexTree RE() {
        RegexTree tree, newtree;
        tree = RT();
        while (token == '|') {
            newtree = new RegexTree(token);
            match(token);
            //左孩子存前面得到的左子树
            newtree.left = tree;
            //右孩子存第一个|右边的优先级高的子树
            newtree.right = RT();
            //得到的新树作为一个新的子树赋给tree，最终tree就是树根
            tree = newtree;
        }
        return tree;
    }
    private RegexTree RT(){
        RegexTree tree, newtree;
        tree = RP();
        while (token == '.'){
            newtree = new RegexTree(token);
            match('.');
            newtree.left = tree;
            newtree.right = RP();
            tree = newtree;
        }
        return tree;
    }
    private RegexTree RP(){
        RegexTree tree, newtree;
        tree = RF();
        while (token == '*'){
            newtree = new RegexTree(token);
            match('*');
            //结点 * 只有左子树
            newtree.left = tree;
            tree = newtree;
        }
        return tree;
    }
    private RegexTree RF(){
        RegexTree tree = null;
        if(token == '('){
            match('(');
            tree = RE();
            match(')');
        }//括号最高优先级，直接建树
        else if(Character.isLetter(token)){
            tree = new RegexTree(token);
            getToken();
        }//输入字符, 返回叶子
        return tree;
    }

    public SyntaxTree(String str){
        PreProcess(str);
        root = RE();
    }
    public RegexTree getRoot(){
        return this.root;
    }
    public ArrayList<Character> getCharacter(){
        return this.character;
    }
}
