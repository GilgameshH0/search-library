package o.shilkin.utils;

import o.shilkin.exceptions.SearchException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Библиотека для поиска текста в CSV-файле.
 */
public class SearchLibrary {
    /**
     * Ищет заданный поисковый текст в указанном столбце файла данных.
     *
     * @param filePath    путь к файлу данных аэропорта
     * @param columnIndex индекс столбца для поиска
     * @param searchText  текст для поиска
     * @return количество строк, в которых был найден текст для поиска в указанном столбце
     * @throws IOException если произошла ошибка при чтении файла
     * @throws SearchException если указанный индекс столбца недействителен
     */
    public static int search(String filePath, int columnIndex, String searchText) throws IOException, SearchException {
        int count = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            int chunkSize = 1024 * 1024;
            char[] buffer = new char[chunkSize];
            StringBuilder partialLine = new StringBuilder();
            while (true) {
                int bytesRead = br.read(buffer);
                if (bytesRead == -1) {
                    break;
                }
                for (int i = 0; i < bytesRead; i++) {
                    char c = buffer[i];
                    if (c == '\n') {
                        String line = partialLine.toString().trim();
                        String[] parts = line.split(",");
                        if (parts.length <= columnIndex) {
                            throw new SearchException("Указан неверный индекс столбца.", null);
                        }
                        count = getCount(columnIndex, searchText, count, parts);
                        partialLine.setLength(0);
                    } else {
                        partialLine.append(c);
                    }
                }
            }
            if (partialLine.length() > 0) {
                String line = partialLine.toString().trim();
                String[] parts = line.split(",");
                if (parts.length <= columnIndex) {
                    throw new SearchException("Указан неверный индекс столбца.", null);
                }
                count = getCount(columnIndex, searchText, count, parts);
            }
        }
        return count;
    }

    /**
     * Вспомогательный метод для подсчета количества строк, в которых был найден searchText в указанном столбце.
     *
     * @param columnIndex индекс столбца для поиска
     * @param searchText  текст для поиска
     * @param count       текущее количество строк, в которых был найден текст для поиска
     * @param parts       части текущей строки, разделенные запятой
     * @return обновленное количество строк, в которых был найден searchText
     */
    private static int getCount(int columnIndex, String searchText, int count, String[] parts) {
        String value = parts[columnIndex].trim();
        if (value.startsWith("\"") && value.endsWith("\"")) {
            value = value.substring(1, value.length() - 1);
            if (value.toLowerCase().contains(searchText.toLowerCase())) {
                count++;
            }
        } else if (isNumeric(value)) {
            if (value.equalsIgnoreCase(searchText)) {
                count++;
            }
        } else {
            if (value.toLowerCase().contains(searchText.toLowerCase())) {
                count++;
            }
        }
        return count;
    }

    /**
     * Вспомогательный метод для проверки возможности анализа строки как числового значения.
     *
     * @param str строка для проверки
     * @return true, если строка может быть проанализирована как числовое значение, иначе false
     */
    private static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
