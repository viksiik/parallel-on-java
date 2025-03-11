import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        int groups = 3;
        int studentsPerGroup = 10;
        Journal journal = new Journal(groups, studentsPerGroup);
        ExecutorService executor = Executors.newFixedThreadPool(3);

        executor.execute(new Lecturer(journal, groups, studentsPerGroup));

        for (int i = 0; i < groups; i++) {
            executor.execute(new Assistant(journal, i, studentsPerGroup));
        }

        executor.shutdown();
        while (!executor.isTerminated()) {
        }

        journal.printJournal();
    }
}
