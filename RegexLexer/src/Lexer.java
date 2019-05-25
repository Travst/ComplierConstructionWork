import RegexTree.SyntaxTree;
import automata.DFA;
import automata.NFA;
import automata.State;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Set;

public class Lexer {
    private JPanel Jpanel;
    private JTextField RexTextField;
    private JTable ShowTable;
    private javax.swing.JLabel JLabel;
    private JButton NFAButton;
    private JButton DFAButton;
    private JButton MinDFAButton;
    private JButton CodeBotton;
    private JButton ClearButton;
    private javax.swing.JScrollPane ScrollPane;
    private JTextArea textArea;
    private JPanel JpanelOnScroll;
    private JScrollPane tableScrollPane;
    private ArrayList<Character> allLetter;
    private String[] columnNames;
    private enum FATYPE{TYPENFA,TYPEDFA};

    public Lexer() {
        NFAButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String regex = RexTextField.getText().toString();
                SyntaxTree tree = new SyntaxTree(regex);
                allLetter = tree.getCharacter();
                NFA nfa = new NFA(tree.getRoot());

                ShowTable.setVisible(true);
                ShowTable.setModel(setShowTable(nfa.allState, FATYPE.TYPENFA));
                DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();// 设置table内容居中
                // tcr.setHorizontalAlignment(JLabel.CENTER);
                tcr.setHorizontalAlignment(SwingConstants.CENTER);// 这句和上句作用一样
                ShowTable.setDefaultRenderer(Object.class, tcr);

                textArea.setText(nfa.getStartEnd());
                //ShowTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            }
        });
        DFAButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String regex = RexTextField.getText().toString();
                SyntaxTree tree = new SyntaxTree(regex);
                allLetter = tree.getCharacter();
                DFA dfa = new DFA(regex);

                ShowTable.setVisible(true);
                ShowTable.setModel(setShowTable(dfa.DFAState, FATYPE.TYPEDFA));
                DefaultTableCellRenderer tcr = new DefaultTableCellRenderer();// 设置table内容居中
                // tcr.setHorizontalAlignment(JLabel.CENTER);
                tcr.setHorizontalAlignment(SwingConstants.CENTER);// 这句和上句作用一样
                ShowTable.setDefaultRenderer(Object.class, tcr);

                textArea.setText(dfa.getStartEnd());
                //ShowTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            }
        });
        ClearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RexTextField.setText("");
                ShowTable.setVisible(false);
                textArea.setText("");
            }
        });
    }

    private DefaultTableModel setShowTable(ArrayList<State> stateArrayList, FATYPE fatype){
        String columms = "State";
        if(fatype == FATYPE.TYPENFA)
            columms += ",ε";

        for (char c: allLetter){
            columms += "," + c;
        }
        columnNames = columms.split(",");
        int columnsize = columnNames.length;
        int rowsize = stateArrayList.size();
        String[][] cells = new String[rowsize][columnsize];
        for(int i = 0; i < rowsize; i++){
            for (int j = 0; j < columnsize; j++){
                cells[i][j] = getCellValue(i, j, stateArrayList);
            }
        }
        DefaultTableModel tableModel = new DefaultTableModel(cells,columnNames);
        return tableModel;
    }
    private String getCellValue(int row, int col, ArrayList<State> stateArrayList){
        String string = "";
        State state = stateArrayList.get(row);
        if(col == 0){
            string += state.getId();
        }
        else {
            Set<Integer> set = state.getNextIdBy(columnNames[col].charAt(0));
            for (int i : set) {
                string += i + " ";
            }
        }
        return string;
    }

    //改变系统默认字体，需放在视图代码最前面，比如在jframe显示前调用,
    // 统一设置字体，父界面设置之后，所有由父界面进入的子界面都不需要再次设置字体
    public static void setUIFont(Font font){
        java.util.Enumeration keys = UIManager.getDefaults().keys();
        while (keys.hasMoreElements()) {
            Object key = keys.nextElement();
            Object value = UIManager.get(key);
            if (value instanceof javax.swing.plaf.FontUIResource) {
                UIManager.put(key, font);
            }
        }
    }
    public static void main(String[] args) {
        // 统一设置字体
        setUIFont(new Font("alias", Font.PLAIN, 16));

        JFrame frame = new JFrame("Lexer");
        frame.setContentPane(new Lexer().Jpanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();frame.setBounds(0,0,600,600);
        frame.setVisible(true);
    }
}
