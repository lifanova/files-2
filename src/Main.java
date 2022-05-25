import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by Vera Lifanova
 * Date: 24.05.2022
 */

public class Main {
    public static final String PATH = "C://Games/savegames/";

    public static void main(String[] args) {
        GameProgress firstGame = new GameProgress(1, 1, 1, 27);
        GameProgress secondGame = new GameProgress(2, 6, 2, 63);
        GameProgress thirdGame = new GameProgress(3, 5, 3, 45);

        List<String> files = new ArrayList<>();
        files.add("save1.dat");
        files.add("save2.dat");
        files.add("save3.dat");

        saveGame(PATH + "save1.dat", firstGame);
        saveGame(PATH + "save2.dat", secondGame);
        saveGame(PATH + "save3.dat", thirdGame);

        zipFiles(PATH + "all.zip", files);

        deleteFiles(files);
    }

    /**
     * Сохранение сериализуемого объекта GameProgress в файл .dat
     *
     * @param path         - путь к файлу
     * @param gameProgress - сохраняемый объект
     */
    private static void saveGame(String path, GameProgress gameProgress) {
        // откроем выходной поток для записи в файл
        try (FileOutputStream fos = new FileOutputStream(path);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            // запишем экземпляр класса в файл
            oos.writeObject(gameProgress);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Упаковка списка файлов в zip-архив
     *
     * @param zipPath - путь к архиву
     * @param files   - список имен файлов
     */
    private static void zipFiles(String zipPath, List<String> files) {
        try (ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(zipPath))) {
            for (String filename : files) {
                processFile(zout, filename);
                // закрываем текущую запись для новой записи
                zout.closeEntry();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Добавление файла к архиву
     *
     * @param zout     - объект ZipOutputStream
     * @param filename - имя файла
     */
    private static void processFile(ZipOutputStream zout, String filename) {
        String filepath = PATH + filename;
        try (FileInputStream fis = new FileInputStream(filepath)) {
            ZipEntry entry = new ZipEntry(filename);
            zout.putNextEntry(entry);
            // считываем содержимое файла в массив byte
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            // добавляем содержимое к архиву
            zout.write(buffer);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Удаление спсика файлов, не лежащих в архиве
     *
     * @param files - список имен файлов
     */
    public static void deleteFiles(List<String> files) {
        for (String filename : files) {
            String filepath = PATH + filename;
            File file = new File(filepath);

            if (file.delete()) {
                System.out.println("[deleteFiles]: Файл " + filename + " удален");
            }
        }
    }
}
