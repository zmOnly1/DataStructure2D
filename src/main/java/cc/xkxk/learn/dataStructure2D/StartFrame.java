package cc.xkxk.learn.dataStructure2D;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.*;

import static cc.xkxk.learn.dataStructure2D.RedBlackTree.BLACK;
import static cc.xkxk.learn.dataStructure2D.RedBlackTree.RED;

public class StartFrame {

    private static final String title = "DataStructure2D - v1.0 - create by github.com/gdggfb";
    private static final String rdmButtonDesc = "随机";
    private static final String putButtonDesc = "插入";
    private static final String removeButtonDesc = "删除";
    public static final int DEAULT_SIZE = 15;
    public static final int GEN_SIZE = DEAULT_SIZE;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> new StartFrame().doDraw());
    }

    private void action(JTextField textField, JPanel board, RedBlackTree tree, int action) {
        doAction(textField, board, tree, action);
        textField.requestFocus();
    }

    public int getTextValue(JTextField textField) {
        String value = textField.getText();
        if (value == null || value.isEmpty()) {
            return -1;
        }
        textField.setText("");
        return Integer.parseInt(value);
    }

    private void doAction(JTextField textField, JPanel board, RedBlackTree tree, int action) {
        int textValue = getTextValue(textField);
        if (textValue == -1) {
            return;
        }
        switch (action) {
            case 0:
                if (!tree.put(textValue)) {
                    return;
                }
                break;
            case 1:
                if (!tree.remove(textValue) || tree.size < 1) {
                    return;
                }
                break;
            default:
                break;
        }
        board.add(new TreeJpanel(tree.root, tree.process), 0);
        board.revalidate();
    }

    private void doDraw() {
        JPanel board = createBoard();
        JTextField textField = createTextField();

        JPanel buttonPanel = createButtonPanel(board, textField);
        JScrollPane scrollPane = createScrollPane(board);
        JPanel container = createContainer(scrollPane, buttonPanel);
        JMenuBar menubar = createMenuBar();

        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setJMenuBar(menubar);
        frame.setSize(new Dimension(1366, 760));
        frame.setContentPane(container);
        frame.setVisible(true);
    }

    private void clearTree(RedBlackTree tree) {
        tree.root = null;
        tree.process = null;
        tree.size = 0;
    }

    private JMenuBar createMenuBar() {
        JMenuBar menubar = new JMenuBar();

        JMenu setMenu = new JMenu("设置");
        JMenu helpMenu = new JMenu("帮助");
        JMenuItem aboutItem = new JMenuItem("关于");
        menubar.setFont(new Font("宋体", Font.PLAIN, 12));

        helpMenu.add(aboutItem);
        menubar.add(setMenu);
        menubar.add(helpMenu);
        return menubar;
    }

    private JPanel createContainer(JScrollPane scrollPane, JPanel buttonPanel) {
        JPanel container = new JPanel();
        container.setLayout(new GridBagLayout());
        GridBagConstraints c3 = new GridBagConstraints();
        c3.gridx = 0;
        c3.gridy = 0;
        c3.weightx = 0;
        c3.weighty = 1.0;
        c3.fill = GridBagConstraints.HORIZONTAL;
        container.add(buttonPanel, c3);

        GridBagConstraints c1 = new GridBagConstraints();
        c1.gridx = 1;
        c1.gridy = 0;
        c1.weightx = 1.0;
        c1.weighty = 1.0;
        c1.fill = GridBagConstraints.BOTH;
        container.add(scrollPane, c1);
        container.setOpaque(false);
        return container;
    }

    private JScrollPane createScrollPane(JPanel board) {
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.getViewport().add(board);
        scrollPane.getVerticalScrollBar().setUnitIncrement(15);
        return scrollPane;
    }

    private JPanel createButtonPanel(JPanel board, JTextField textField) {
        RedBlackTree tree = new RedBlackTree();
        addValues(tree, DEAULT_SIZE, board);

        JButton randomButton = new JButton(rdmButtonDesc);
        JButton putButton = new JButton(putButtonDesc);
        JButton removeButton = new JButton(removeButtonDesc);
        putButton.addActionListener((e) -> action(textField, board, tree, 0));
        removeButton.addActionListener((e) -> action(textField, board, tree, 1));
        randomButton.addActionListener(e -> {
            int textValue = getTextValue(textField);
            addValues(tree, textValue, board);
        });

        JPanel textFieldWrap = new JPanel();
        textFieldWrap.setBorder(BorderFactory.createLineBorder(Color.YELLOW));
        textFieldWrap.add(textField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setPreferredSize(new Dimension(80, 760));
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setBorder(BorderFactory.createLineBorder(Color.YELLOW));
        buttonPanel.add(Box.createVerticalStrut(500));
        buttonPanel.add(textFieldWrap);
        buttonPanel.add(randomButton);
        buttonPanel.add(putButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(Box.createVerticalStrut(50));
        return buttonPanel;
    }

    private void addValues(RedBlackTree tree, int textValue, JPanel board) {
        do {
            genTree(tree, textValue);
        } while (!treeCondition(tree));

        board.add(new TreeJpanel(tree.root, tree.process), 0);
        board.revalidate();
    }

    /**
     * 见 image/红黑树删除.png
     */
    private boolean treeCondition(RedBlackTree tree) {
        RedBlackTree.Entry root = tree.root;
        RedBlackTree.Entry B = root.left;
		if(!colorOf(B, BLACK))return false;

        RedBlackTree.Entry C = root.right;
		if(!colorOf(C, RED))return false;

        RedBlackTree.Entry D = B.left;
        if(!colorOf(D, BLACK))return false;
        RedBlackTree.Entry E = B.right;
        if(!colorOf(E, BLACK))return false;

		RedBlackTree.Entry H = D.right;
		if(!colorOf(H, RED))return false;

        RedBlackTree.Entry F = C.left;
        if(!colorOf(F, BLACK))return false;
        RedBlackTree.Entry G = F.left;
        if(!colorOf(G, RED))return false;

        return true;
    }

    private boolean colorOf(RedBlackTree.Entry node, boolean color) {
        return node != null && node.color == color;
    }


    private void genTree(RedBlackTree tree, int textValue) {
        clearTree(tree);

        int size = textValue == -1 ? DEAULT_SIZE : textValue;
        Integer[] nums = randomNumbers(GEN_SIZE);
        for (int val : nums) {
            tree.put(val);
        }

        int delSize = GEN_SIZE - size;
        for (int i = 0; i < delSize; i++) {
            while (!tree.remove(nums[random.nextInt(GEN_SIZE)])) ;
        }
    }

    Random random = new Random();

    private Integer[] randomNumbers(int size) {
        List<Integer> nums = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            int n;
            do {
                n = randomValue();
            } while (nums.contains(n));
            nums.add(n);
        }

        return nums.toArray(new Integer[size]);
    }

    private int randomValue() {
        return random.nextInt(99) + 1;
    }

    private JPanel createBoard() {
        JPanel board = new JPanel();
        board.setLayout(new BoxLayout(board, BoxLayout.Y_AXIS));
        return board;
    }

    private JTextField createTextField() {
        JTextField textField = new JTextField();
        textField.setColumns(4);
        textField.setDocument(new PlainDocument() {
            private static final long serialVersionUID = 1L;

            public void insertString(int offset, String str, AttributeSet attrSet) throws BadLocationException {
                if (str == null) {
                    return;
                }
                if ((getLength() + str.length()) <= 2) {
                    char[] chars = str.toCharArray();
                    int length = 0;
                    for (int i = 0; i < chars.length; i++) {
                        if (chars[i] >= '0' && chars[i] <= '9') {
                            chars[length++] = chars[i];
                        }
                    }
                    super.insertString(offset, new String(chars, 0, length), attrSet);
                }
            }
        });

        return textField;
    }

}
