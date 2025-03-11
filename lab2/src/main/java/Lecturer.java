import java.util.Random;

class Lecturer implements Runnable {
    private final Journal journal;
    private final int groups;
    private final int students;

    public Lecturer(Journal journal, int groups, int students) {
        this.journal = journal;
        this.groups = groups;
        this.students = students;
    }

    @Override
    public void run() {
        for (int g = 0; g < groups; g++) {
            for (int s = 0; s < students; s++) {
                journal.assignGrade(g, s, (int) (Math.random() * 100));
            }
        }
    }
}