package o.shilkin;

import o.shilkin.exceptions.SearchException;
import o.shilkin.utils.SearchLibrary;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String filePath = "airports.csv";
        int columnIndex = 0;
        try {
            columnIndex = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.err.println("Указан неверный индекс столбца.");
            System.exit(1);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Индекс столбца не указан.");
            System.exit(1);
        }
        while (true) {
            System.out.print("Введите строку: ");
            String searchText = scanner.nextLine();
            if (searchText.equals("!quit")) {
                break;
            }
            try {
                long startTime = System.currentTimeMillis();
                int count = SearchLibrary.search(filePath, columnIndex, searchText);
                long endTime = System.currentTimeMillis();
                System.out.println("Количество найденных строк: " + count + ", время затраченное на поиск:" + (endTime - startTime) + " мс.");
            } catch (IOException e) {
                System.err.println("Ошибка чтения файла: " + e.getMessage());
            } catch (SearchException e) {
                System.err.println("Ошибка библиотеки " + e.getMessage());
            }
        }
        scanner.close();
    }
}