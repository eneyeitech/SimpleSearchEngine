package search;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        new UserInterface(args).start();
    }
}

class SearchEngine {

    public final String SEPARATOR = " ";
    private final Repository repository = new Repository();

    public void initRepository(String[] source) {
        String fileName = source[1];
        List<DataModel> storage = repository.getStorage();
        Map<String, List<Integer>> invertedIndex = repository.getInvertedIndex();

        File file = new File(fileName);

        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNext()) {
                DataModel dataModel = new DataModel();
                dataModel.setDataLine(fileScanner.nextLine());
                storage.add(dataModel);
                String[] content = dataModel.getDataLine()
                        .toLowerCase(Locale.ROOT)
                        .split(SEPARATOR);
                for (String eachContent : content) {
                    List<Integer> indexes = new ArrayList<>();
                    if (invertedIndex.containsKey(eachContent)) {
                        indexes = invertedIndex.get(eachContent);
                        indexes.add(storage.size() - 1);
                    } else {
                        indexes.add(storage.size() - 1);
                        invertedIndex.put(eachContent, indexes);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("No file found: " + fileName);
        }

        repository.setStorage(storage);
        repository.setInvertedIndex(invertedIndex);

        System.out.println();
    }

    public void searchingData() {

        List<DataModel> storage = repository.getStorage();
        Map<String, List<Integer>> invertedIndex = repository.getInvertedIndex();

        String queryWord = Util.getQueryWord().toLowerCase(Locale.ROOT);
        List<Integer> resultIndex = new ArrayList<>();
        if (invertedIndex.containsKey(queryWord)) {
            resultIndex = invertedIndex.get(queryWord);
        }

        Util.printResult(storage, resultIndex);
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
    private Map<String, List<Integer>> invertedIndex = new HashMap<>();

    public Repository() {
    }

    public List<DataModel> getStorage() {
        return storage;
    }

    public void setStorage(List<DataModel> storage) {
        this.storage = storage;
    }

    public Map<String, List<Integer>> getInvertedIndex() {
        return invertedIndex;
    }

    public void setInvertedIndex(Map<String, List<Integer>> invertedIndex) {
        this.invertedIndex = invertedIndex;
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
    private final String[] source;

    public UserInterface(String[] source) {
        this.source = source;
    }

    public void start() {
        SearchEngine searchEngine = new SearchEngine();
        searchEngine.initRepository(source);
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