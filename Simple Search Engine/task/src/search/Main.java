package search;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        new UserInterface(args).start();
    }
}

class AllSearcher extends SearchEngine {

    private final Repository repository;

    public AllSearcher(Repository repository) {
        this.repository = repository;
    }

    @Override
    public Set<Integer> searchingData(String searchData) {

        Map<String, List<Integer>> invertedIndex = repository.getInvertedIndex();

        String[] searchContent = searchData.toLowerCase(Locale.ROOT).split(SEPARATOR);
        List<Integer> tmpIndex = new ArrayList<>();
        Set<Integer> resultIndex = new HashSet<>();

        for (int i = 0; i < searchContent.length; i++) {
            if (i == 0 && invertedIndex.containsKey(searchContent[0])) {
                tmpIndex.addAll(invertedIndex.get(searchContent[0]));
            }
            List<Integer> checked = invertedIndex.get(searchContent[i]);
            for (Integer eachTmp : tmpIndex) {
                for (Integer eachCheked : checked) {
                    if (eachTmp.equals(eachCheked)) {
                        resultIndex.add(eachCheked);
                    }
                }
            }
        }

        return resultIndex;
    }
}

class AnySearcher extends SearchEngine {

    private final Repository repository;

    public AnySearcher(Repository repository) {
        this.repository = repository;
    }

    @Override
    public Set<Integer> searchingData(String searchData) {
        Set<Integer> resultIndex = new HashSet<>();

        Map<String, List<Integer>> invertedIndex = repository.getInvertedIndex();
        String[] searchContent = searchData.toLowerCase(Locale.ROOT).split(SEPARATOR);

        Arrays.stream(searchContent).filter(invertedIndex::containsKey)
                .map(invertedIndex::get)
                .forEach(resultIndex::addAll);

        return resultIndex;
    }
}

class NoneSearcher extends SearchEngine{

    private final Repository repository;

    public NoneSearcher(Repository repository) {
        this.repository = repository;
    }
    @Override
    public Set<Integer> searchingData(String searchData) {
        Map<String, List<Integer>> invertedIndex = repository.getInvertedIndex();

        String[] searchContent = searchData.toLowerCase(Locale.ROOT).split(SEPARATOR);
        Set<Integer> resultIndex = new HashSet<>();
        for(String each: invertedIndex.keySet()) {
            resultIndex.addAll(invertedIndex.get(each));
        }
        for (String eachSearch : searchContent) {
            resultIndex.removeAll(invertedIndex.get(eachSearch));
        }

        return resultIndex;
    }
}

abstract class SearchEngine {

    public static final String SEPARATOR = " ";

    public abstract Set<Integer> searchingData(String searchData);

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

    public final String SEPARATOR = " ";

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

    public void initRepository(String[] source) {
        String fileName = source[1];

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

        System.out.println();
    }

    public void printAllData() {
        System.out.println("\n=== List of people ===");
        for (DataModel each : storage) {
            System.out.println(each.getDataLine());
        }
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
    private final Repository repository = new Repository();

    public UserInterface(String[] source) {
        this.source = source;
    }

    public void start() {
        repository.initRepository(source);
        int option;
        do {
            option = choiceMainMenu();
            switch (option) {
                case 1:
                    choiceStrategy();
                    break;
                case 2:
                    repository.printAllData();
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

    public void choiceStrategy() {
        System.out.println("Select a matching strategy: ALL, ANY, NONE");
        String strategy = scanner.nextLine();
        System.out.println("Enter a name or email to search all suitable people.");
        String searchData = scanner.nextLine();
        Set<Integer> resultIndex;
        switch (strategy) {
            case "ALL" :
                SearchEngine allSearcher = new AllSearcher(repository);
                resultIndex = allSearcher.searchingData(searchData);
                printResult(repository.getStorage(), resultIndex);
                break;
            case "ANY" :
                SearchEngine anySearcher = new AnySearcher(repository);
                resultIndex = anySearcher.searchingData(searchData);
                printResult(repository.getStorage(), resultIndex);
                break;
            case "NONE" :
                SearchEngine noneSearcher = new NoneSearcher(repository);
                resultIndex = noneSearcher.searchingData(searchData);
                printResult(repository.getStorage(), resultIndex);
                break;
        }
    }

    public void printResult(List<DataModel> storage, Set<Integer> resultIndex) {
        if (resultIndex.size() > 0) {
            System.out.printf("%d persons found:\n", resultIndex.size());
            for (Integer each : resultIndex) {
                System.out.println(storage.get(each).getDataLine());
            }
        } else {
            System.out.println("No matching people found.");
        }
        System.out.println();
    }

}