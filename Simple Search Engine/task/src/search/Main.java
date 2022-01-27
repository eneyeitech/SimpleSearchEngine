package search;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        new SearchEngine().run();
    }
}

class SearchEngine {

    public Scanner scanner = new Scanner(System.in);
    public final String SEPARATOR = " ";
    private final Repository repository = new Repository();

    public void run() {
        initRepository();
        searchingData();

    }

    private void initRepository() {
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

    private void searchingData() {
        int queryNumber = Util.getQueriesNumber();

        List<DataModel> storage = repository.getStorage();

        for (int j = 0; j < queryNumber; j++) {
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
    }

    private boolean processSearching(DataModel dataModel, String queryWord) {
        String[] content = dataModel.getDataLine().toLowerCase(Locale.ROOT).split(SEPARATOR);
        for (String each : content) {
            if(each.contains(queryWord.toLowerCase(Locale.ROOT))){
                return true;
            }
        }
        return false;
    }
}

class Util {

    static Scanner scanner = new Scanner(System.in);

    public static int getQueriesNumber() {
        System.out.println("Enter the number of search people:");
        int result = Integer.parseInt(scanner.nextLine());
        System.out.println();
        return result;
    }

    public static String getQueryWord() {
        System.out.println("Enter data to search people:");
        String result = scanner.nextLine();
        System.out.println();
        return result;
    }

    public static void printResult(List<DataModel> storage, List<Integer> resultIndex) {
        if (resultIndex.size() > 0) {
            System.out.println("Found people:");
            for(Integer each: resultIndex) {
                System.out.println(storage.get(each).getDataLine());
            }
        } else {
            System.out.println("No matching data found.");
        }
        System.out.println();
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