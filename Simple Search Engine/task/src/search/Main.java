package search;

import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) {
        String line1 = scanner.nextLine();
        String line2 = scanner.nextLine();
        List<String> list = List.of(line1.split(" "));
        List<String> search = List.of(line2);

        int index = Collections.indexOfSubList(list, search);

        if (index == -1){
            System.out.println("Not found");
        } else {
            System.out.println(index + 1);
        }
    }
}
