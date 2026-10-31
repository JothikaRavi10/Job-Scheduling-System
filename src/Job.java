public class Job {
    private String jobId;
    private int deadline;
    private int value;
    private int resourceRequired;
    private int processingTime;

    public Job(String jobId, int deadline, int value,
               int resourceRequired, int processingTime) {

        this.jobId = jobId;
        this.deadline = deadline;
        this.value = value;
        this.resourceRequired = resourceRequired;
        this.processingTime = processingTime;
    }

    public String getJobId() {
        return jobId;
    }

    public int getDeadline() {
        return deadline;
    }

    public int getValue() {
        return value;
    }

    public int getResourceRequired() {
        return resourceRequired;
    }

    public int getProcessingTime() {
        return processingTime;
    }
}