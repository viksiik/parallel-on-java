import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Journal {
    private final List<List<List<Integer>>> groupGrades;
    private final Lock lock = new ReentrantLock();

    public Journal(int groups, int students) {
        groupGrades = new ArrayList<>();
        for (int i = 0; i < groups; i++) {
            List<List<Integer>> group = new ArrayList<>();
            for (int j = 0; j < students; j++) {
                group.add(new ArrayList<>());
            }
            groupGrades.add(group);
        }
    }

    public void assignGrade(int group, int student, int grade) {
        lock.lock();
        try {
            groupGrades.get(group).get(student).add(grade);
            System.out.println("Assigned grade " + grade + " to student " + student + " in group " + group);
        } finally {
            lock.unlock();
        }
    }

    public void printJournal() {
        lock.lock();
        try {
            System.out.println("\nJournal:");
            for (int i = 0; i < groupGrades.size(); i++) {
                System.out.println("Group " + i + ":");
                for (int j = 0; j < groupGrades.get(i).size(); j++) {
                    List<Integer> grades = groupGrades.get(i).get(j);
                    System.out.print("Student " + j + ": ");
                    for (Integer grade : grades) {
                        System.out.print(grade + " ");
                    }
                    System.out.println();
                }
                System.out.println("--------------------------");
            }
        } finally {
            lock.unlock();
        }
    }
}
