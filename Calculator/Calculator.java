//参考サイト
//https://qiita.com/nakanosakaue_kunren/items/fa910d47759d5baed110



package Calculator; //上のディレクトリを指定する必要がある

import java.util.Scanner;

public class Calculator {
        public static void main(String[] args) {

            Scanner scanner = new Scanner(System.in);

            System.out.println("簡単な計算機へようこそ！！");
        
            //ユーザーからの入力を受け取る
            System.out.print("最初の数値を入力してください: ");
            double num1 = scanner.nextDouble();

            //ユーザーからの演算子の入力を取得
            System.out.print("演算子を入力してください（+、-、*、/）: ");
            char operator = scanner.next().charAt(0);

            // ユーザーから2番目の数値を取得
            System.out.print("2番目の数値を入力してください: ");
            double num2 = scanner.nextDouble();

            //計算を実行
            double result = 0.0;

            switch (operator) {
                case '+':
                    result = num1 + num2;
                    break;
                case '-':
                    result = num1 - num2;
                    break;
                case '*':
                    result = num1 * num2;
                    break;
                case '/':
                    result = num1 / num2;
                    break;
                default:
                    System.out.println("無効な演算子です。");
                    scanner.close();
                    return;
            }

            //結果を表示
            System.out.println(num1 + " " + operator + " " + num2 + " = " + result);

            scanner.close();

        }
}
