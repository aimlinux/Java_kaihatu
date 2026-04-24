package Calculator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CalculatorGUI extends JFrame implements ActionListener {
    private JTextField displayField;
    private double num1, num2, result;
    private char operator;
    
    public CalculatorGUI() {
        // ウィンドウの設定
        setTitle("簡単電卓");
        setSize(300, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // 表示フィールド
        displayField = new JTextField();
        displayField.setFont(new Font("Arial", Font.PLAIN, 24));
        displayField.setHorizontalAlignment(JTextField.RIGHT);
        displayField.setEditable(false);
        add(displayField, BorderLayout.NORTH);
        
        // ボタンパネル
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5, 4, 5, 5));
        
        // ボタンの配列
        String[] buttonLabels = {
            "7", "8", "9", "/",
            "4", "5", "6", "*",
            "1", "2", "3", "-",
            "0", ".", "=", "+",
            "C"
        };
        
        // ボタンを追加
        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            button.setFont(new Font("Arial", Font.PLAIN, 18));
            button.addActionListener(this);
            buttonPanel.add(button);
        }
        
        add(buttonPanel, BorderLayout.CENTER);
        
        // ウィンドウを表示
        setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        
        // 数字かドットの場合
        if ((command.charAt(0) >= '0' && command.charAt(0) <= '9') || command.equals(".")) {
            displayField.setText(displayField.getText() + command);
        }
        // クリアボタンの場合
        else if (command.equals("C")) {
            displayField.setText("");
            num1 = num2 = result = 0;
            operator = '\0';
        }
        // 演算子の場合
        else if (command.equals("+") || command.equals("-") || command.equals("*") || command.equals("/")) {
            num1 = Double.parseDouble(displayField.getText());
            operator = command.charAt(0);
            displayField.setText("");
        }
        // イコールの場合
        else if (command.equals("=")) {
            num2 = Double.parseDouble(displayField.getText());
            
            switch(operator) {
                case '+': result = num1 + num2; break;
                case '-': result = num1 - num2; break;
                case '*': result = num1 * num2; break;
                case '/': 
                    if(num2 != 0) {
                        result = num1 / num2;
                    } else {
                        displayField.setText("エラー");
                        return;
                    }
                    break;
            }
            
            displayField.setText(String.valueOf(result));
        }
    }
    
    public static void main(String[] args) {
        // イベントディスパッチスレッドでGUIを作成
        SwingUtilities.invokeLater(() -> {
            new CalculatorGUI();
        });
    }
}
