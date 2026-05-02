//2026/05/02

package test.hairetu;

public class hairetu1 {
    public static void main(String[] arg) {
        String[] array;
        array = new String[3];

        array[0] = "Java";
        array[1] = "Python";
        array[2] = "C++";

        String[] names = {"kazuma", "kazuma", "kazuma"};

        String[] love_names = {"mia", "honoka", "maori"};      


        for (int i = 0; i < array.length; i++) {
            System.out.println("配列の要素：" + array[i] + " (インデックス: " + i + ")");
        }

        love_names[1] = "mei"; // love_namesの2番目の要素を"mei"に変更

        for (int i = 0; i < names.length; i++) {
            System.out.println("配列の要素：" + names[i]);
            
            System.out.println(names[i] + "が好きな人は" + love_names[i] + "です");

        }

    }
}
