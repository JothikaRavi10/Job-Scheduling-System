
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class JobScheduler {

    public static class Machine {

        private String machineId;
        private int availableTime;
        private int resourceCapacity;
        private int usedTime;
        private int usedResources;
        private List<Job> assignedJobs;

        public Machine(String machineId, int availableTime,
                       int resourceCapacity) {

            this.machineId = machineId;
            this.availableTime = availableTime;
            this.resourceCapacity = resourceCapacity;
            this.usedTime = 0;
            this.usedResources = 0;
            this.assignedJobs = new ArrayList<>();
        }

        public boolean canRun(Job job) {

            int finishTime = usedTime + job.getProcessingTime();

            return finishTime <= availableTime
                && finishTime <= job.getDeadline()
                && job.getResourceRequired() <= resourceCapacity;
        }

        public void assignJob(Job job) {

            assignedJobs.add(job);
            usedTime += job.getProcessingTime();

            usedResources = Math.max(
                usedResources,
                job.getResourceRequired()
            );
        }

        public String getMachineId() {
            return machineId;
        }

        public int getUsedTime() {
            return usedTime;
        }

        public int getUsedResources() {
            return usedResources;
        }

        public List<Job> getAssignedJobs() {
            return assignedJobs;
        }
    }

    private List<Machine> machines = new ArrayList<>();

    private long executionTime;
    private int rejectedJobs;

    public void createMachines(int count, int availableTime,
                               int resourceCapacity) {

        machines.clear();

        for (int i = 1; i <= count; i++) {

            machines.add(
                new Machine(
                    "Machine " + i,
                    availableTime,
                    resourceCapacity
                )
            );
        }
    }

    public List<Job> scheduleJobs(List<Job> jobs) {

        long startTime = System.nanoTime();

        List<Job> sortedJobs = new ArrayList<>(jobs);
        List<Job> completedJobs = new ArrayList<>();

        sortedJobs.sort(
            Comparator.comparingInt(Job::getValue).reversed()
        );

        for (Job job : sortedJobs) {

            boolean assigned = false;

            for (Machine machine : machines) {

                if (machine.canRun(job)) {

                    machine.assignJob(job);
                    completedJobs.add(job);
                    assigned = true;
                    break;
                }
            }
        }

        rejectedJobs = jobs.size() - completedJobs.size();

        executionTime = System.nanoTime() - startTime;

        return completedJobs;
    }

    public List<Machine> getMachines() {
        return machines;
    }

    public long getExecutionTime() {
        return executionTime;
    }

    public int getRejectedJobs() {
        return rejectedJobs;
    }

    public String getTimeComplexity() {
        return "O(n log n + n x m)";
    }
}
