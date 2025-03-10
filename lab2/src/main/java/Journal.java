import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Journal {
    private final int[][] groupGrades;
    private final Lock lock = new ReentrantLock();

    public Journal(int groups, int students) {
        groupGrades = new int[groups][students];
    }

    public void assignGrade(int group, int student, int grade) {
        lock.lock();
        try {
            groupGrades[group][student] = grade;
            System.out.println("Assigned grade " + grade + " to student " + student + " in group " + group);
        } finally {
            lock.unlock();
        }
    }

    public void printJournal() {
        lock.lock();
        try {
            System.out.println("\nJournal:");
            for (int i = 0; i < groupGrades.length; i++) {
                System.out.println("Group " + i + ":");
                for (int j = 0; j < groupGrades[i].length; j++) {
                    System.out.print(groupGrades[i][j] + " ");
                }
                System.out.println();
            }
        } finally {
            lock.unlock();
        }
    }
}