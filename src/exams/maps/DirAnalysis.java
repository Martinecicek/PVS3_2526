package exams.maps;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DirAnalysis {

    static void analyzeDirectory(String path) throws IOException {
        Path p = Paths.get(path);

//        Stream<Path> fileStream = Files.walk(p);
//        fileStream.close();//idealni je to napsat jako try-with resources
        try (Stream<Path> filesStream = Files.walk(p)) {
//        filesStream.forEach(System.out::println);

            List<File> files = filesStream
                    .map(Path::toFile)
                    .filter(File::isFile)
                    .toList();
            System.out.println(files);

            //mapy dle typu souboru:
            //1 - mapa typ Soubor : pocet
            Map<String, Long> fileTypeCount = files.stream()
                    .collect(Collectors.groupingBy(file -> getExtension(file.getName()), Collectors.counting()));

            //2 - mapa typSoubor:celkova velikost vsech souboru toho typu
            Map<String, Long> fileTypeSizes = files.stream()
                    .collect(Collectors.groupingBy(file -> getExtension(file.getName()),
                            Collectors.summingLong(File::length)));
            System.out.println(fileTypeSizes);

            System.out.println(fileTypeCount);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    static String getExtension(String fileName){
        int dotIndex = fileName.lastIndexOf(".");
        return (dotIndex == -1) ? "NO_TYPE" : fileName.substring(dotIndex+1).toLowerCase();
    }

    public static void main(String[] args) {
        try {
            analyzeDirectory("data");
        } catch (IOException ex) {
            System.out.println("Chyba pri praci se souborem: " + ex);
        }
    }
}
