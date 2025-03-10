import java.util.Random;

class Assistant implements Runnable {
    private final Journal journal;
    private final int groupIndex;
    private final int groupSize;
    private final Random random = new Random();

    public Assistant(Journal journal, int groupIndex, int groupSize) {
        this.journal = journal;
        this.groupIndex = groupIndex;
        this.groupSize = groupSize;
    }

    @Override
    public void run() {
        for (int i = 0; i < groupSize; i++) {
            int grade = random.nextInt(101);
            journal.assignGrade(groupIndex, i, grade);
        }
    }
}