import java.util.Random;

class Lecturer implements Runnable {
    private final Journal journal;
    private final int groupSize;
    private final Random random = new Random();

    public Lecturer(Journal journal, int groupSize) {
        this.journal = journal;
        this.groupSize = groupSize;
    }

    @Override
    public void run() {
        for (int i = 0; i < groupSize; i++) {
            int grade = random.nextInt(101);
            journal.assignGrade(0, i, grade);
        }
    }
}