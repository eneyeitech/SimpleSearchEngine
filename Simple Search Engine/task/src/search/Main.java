package search;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        new UserInterface().start();
    }
}

class SearchEngine {

    public Scanner scanner = new Scanner(System.in);
    public final String SEPARATOR = " ";
    private final Repository repository = new Repository();

    public void initRepository() {
        int number = Util.getNumberOfPeople();
        System.out.println("Enter all people:");
        List<DataModel> storage = repository.getStorage();
        for (int i = 0; i < number; i++) {
            DataModel dataModel = new DataModel();
            dataModel.setDataLine(scanner.nextLine());
            storage.add(dataModel);
        }
        repository.setStorage(storage);
        System.out.println();
    }

    public void searchingData() {

        List<DataModel> storage = repository.getStorage();

        String queryWord = Util.getQueryWord();
        List<Integer> resultIndex = new ArrayList<>();
        for (int i = 0; i < storage.size(); i++) {
            DataModel dataModel = storage.get(i);
            if (processSearching(dataModel, queryWord)) {
                resultIndex.add(i);
            }
        }
        Util.printResult(storage, resultIndex);
    }

    private boolean processSearching(DataModel dataModel, String queryWord) {
        String[] content = dataModel.getDataLine().toLowerCase(Locale.ROOT).split(SEPARATOR);
        for (String each : content) {
            if (each.contains(queryWord.toLowerCase(Locale.ROOT))) {
                return true;
            }
        }
        return false;
    }

    public void printAllData() {
        List<DataModel> storage = repository.getStorage();
        Util.printResult(storage);
    }
}

class Util {

    static Scanner scanner = new Scanner(System.in);

    public static String getQueryWord() {
        System.out.println("\nEnter a name or email to search all suitable people.");
        String result = scanner.nextLine();
        System.out.println();
        return result;
    }

    public static void printResult(List<DataModel> storage, List<Integer> resultIndex) {
        if (resultIndex.size() > 0) {
            for (Integer each : resultIndex) {
                System.out.println(storage.get(each).getDataLine());
            }
        } else {
            System.out.println("No matching data found.");
        }
        System.out.println();
    }

    public static void printResult(List<DataModel> storage) {
        System.out.println("\n=== List of people ===");
        for (DataModel each : storage) {
            System.out.println(each.getDataLine());
        }
    }

    public static int getNumberOfPeople() {
        System.out.println("Enter the number of people:");
        return Integer.parseInt(scanner.nextLine());
    }

}

class Repository {

    private List<DataModel> storage = new ArrayList<>();

    public Repository() {
    }

    public List<DataModel> getStorage() {
        return storage;
    }

    public void setStorage(List<DataModel> storage) {
        this.storage = storage;
    }
}

class DataModel {

    private String dataLine;

    public DataModel() {
    }

    public String getDataLine() {
        return dataLine;
    }

    public void setDataLine(String dataLine) {
        this.dataLine = dataLine;
    }
}

class UserInterface {

    public Scanner scanner = new Scanner(System.in);

    public void start() {
        SearchEngine searchEngine = new SearchEngine();
        searchEngine.initRepository();
        int option;
        do {
            option = choiceMainMenu();
            switch (option) {
                case 1:
                    searchEngine.searchingData();
                    break;
                case 2:
                    searchEngine.printAllData();
                    break;
                case 0:
                    System.out.println("Bye!");
                    break;
            }
        } while (option != 0);
    }

    public int choiceMainMenu() {
        int option;
        do {
            System.out.println("=== Menu ===\n" +
                    "1. Find a person\n" +
                    "2. Print all people\n" +
                    "0. Exit");
            option = Integer.parseInt(scanner.nextLine());
            if (option < 0 || option > 2) {
                System.out.println("Incorrect option! Try again.\n");
            }
        } while (option < 0 || option > 2);
        return option;
    }
}