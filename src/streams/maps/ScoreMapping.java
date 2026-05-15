package streams.maps;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ScoreMapping {

    static double getAverageScore(List<TestResult> results, String name){
        return results.stream()
                .filter(test -> test.getName().equals(name))
                .mapToDouble(TestResult::getScore)
                .average()
                .orElse(0);
    }

    public static void main(String[] args) throws IOException {
        List<TestResult> results = Files.lines(Path.of("data/scores.csv"))
                .skip(1)
                .map(line -> line.split(","))
                .map(tokens -> new TestResult(tokens[0], tokens[1], Double.parseDouble(tokens[2]), Integer.parseInt(tokens[3])))
                .toList();

        results.forEach(System.out::println);


        //chci mapu studentu dle kategorii:
        //1) "Good": prumer vsech testu >= 50
        //2) "Bad": prumer vsech test >= 30
        //3)"Really bad": jinak
        Map<String, List<String>> studentsCategories = results.stream()
                .map(TestResult::getName)
                .distinct()
                .collect(Collectors.groupingBy(name -> {
                    double avg = getAverageScore(results, name);
//                    System.out.println(avg);
                    if (avg >= 50) return "Good";
                    if (avg >= 30) return "Bad";
                    return "Really bad";
                }, Collectors.toList()));
       studentsCategories.forEach((category, students) -> System.out.println(category + "\n\t" + students));
    }
}
class TestResult{
    String name, subject;
    double score;
    int time;

    public TestResult(String name, String subject, double score, int time) {
        this.name = name;
        this.subject = subject;
        this.score = score;
        this.time = time;
    }

    @Override
    public String toString() {
        return name + " (" + subject + ") [" + score + "/100]. Time: " + time + " s";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
