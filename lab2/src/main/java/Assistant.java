import java.util.Random;

class Assistant implements Runnable {
    private final Journal journal;
    private final int group;
    private final int students;

    public Assistant(Journal journal, int group, int students) {
        this.journal = journal;
        this.group = group;
        this.students = students;
    }

    @Override
    public void run() {
        for (int s = 0; s < students; s++) {
            journal.assignGrade(group, s, (int) (Math.random() * 100));
        }
    }
}