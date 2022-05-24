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
    public static void main(String[] args) {
        GameProgress firstGame = new GameProgress(1, 1, 1, 27);
        GameProgress secondGame = new GameProgress(2, 6, 2, 63);
        GameProgress thirdGame = new GameProgress(3, 5, 3, 45);

        List<String> files = new ArrayList<>();
        files.add("C://Games/savegames/save1.dat");
        files.add("C://Games/savegames/save2.dat");
        files.add("C://Games/savegames/save3.dat");

        saveGame("C://Games/savegames/save1.dat", firstGame);
        saveGame("C://Games/savegames/save2.dat", secondGame);
        saveGame("C://Games/savegames/save3.dat", thirdGame);

        zipFiles("C://Games/savegames/all.zip", files);
    }

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

    private static void zipFiles(String zipPath, List<String> files) {
        for (String path : files) {
            processFile(zipPath, path);
        }

    }

    private static void processFile(String zipPath, String path){
        try (ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(zipPath));
             FileInputStream fis = new FileInputStream(path)) {
            ZipEntry entry = new ZipEntry(path);
            zout.putNextEntry(entry);
            // считываем содержимое файла в массив byte
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            // добавляем содержимое к архиву
            zout.write(buffer);
            // закрываем текущую запись для новой записи
            zout.closeEntry();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
