
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;

public class Dashboard extends JFrame {

    private final Color NAVY = new Color(20, 35, 75);
    private final Color BLUE = new Color(45, 105, 220);
    private final Color BG = new Color(242, 246, 252);
    private final Color GREEN = new Color(30, 160, 110);
    private final Color RED = new Color(220, 75, 75);
    private final Color WHITE = Color.WHITE;

    private JTextField jobIdField, deadlineField, valueField;
    private JTextField resourceField, processingTimeField;
    private JTextField machineCountField, slotsField, capacityField;

    private DefaultTableModel tableModel;
    private JTable jobTable;
    private JTextArea resultArea;

    private JLabel totalJobsLabel, completedLabel;
    private JLabel rejectedLabel, totalValueLabel;
    private JLabel resourcesLabel, executionTimeLabel;
    private JLabel complexityLabel;

    private List<Job> jobs = new ArrayList<>();

    private JPanel contentPanel;
    private JScrollPane mainScroll;

    public Dashboard() {

        setTitle("Job Scheduling System");
        setSize(1250, 820);
        setMinimumSize(new Dimension(1000, 700));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        getContentPane().setBackground(BG);

        // Sidebar
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBackground(NAVY);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(
            BorderFactory.createEmptyBorder(25, 15, 20, 15)
        );

        JLabel logo = new JLabel("  JOB SCHEDULER");
        logo.setFont(new Font("Arial", Font.BOLD, 19));
        logo.setForeground(WHITE);
        logo.setAlignmentX(Component.LEFT_ALIGNMENT);

        sidebar.add(logo);
        sidebar.add(Box.createVerticalStrut(45));

        JButton dashboardNav = navButton("Dashboard");
        JButton jobsNav = navButton("Manage Jobs");
        JButton resultsNav = navButton("Scheduling Results");
        JButton performanceNav = navButton("Performance");

        sidebar.add(dashboardNav);
        sidebar.add(Box.createVerticalStrut(12));
        sidebar.add(jobsNav);
        sidebar.add(Box.createVerticalStrut(12));
        sidebar.add(resultsNav);
        sidebar.add(Box.createVerticalStrut(12));
        sidebar.add(performanceNav);

        sidebar.add(Box.createVerticalGlue());

        JLabel footer = new JLabel("  Java Scheduling System");
        footer.setForeground(new Color(180, 195, 220));
        footer.setFont(new Font("Arial", Font.PLAIN, 12));
        sidebar.add(footer);

        add(sidebar, BorderLayout.WEST);

        // Main content
        contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(BG);
        contentPanel.setBorder(
            BorderFactory.createEmptyBorder(25, 30, 25, 30)
        );

        mainScroll = new JScrollPane(contentPanel);
        mainScroll.setBorder(null);
        mainScroll.getVerticalScrollBar().setUnitIncrement(18);

        add(mainScroll, BorderLayout.CENTER);

        // Header
        JLabel heading = new JLabel("Dashboard Overview");
        heading.setFont(new Font("Arial", Font.BOLD, 27));
        heading.setForeground(NAVY);
        heading.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitle = new JLabel(
            "Manage jobs, allocate machines and analyze performance."
        );
        subtitle.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitle.setForeground(Color.GRAY);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        contentPanel.add(heading);
        contentPanel.add(Box.createVerticalStrut(7));
        contentPanel.add(subtitle);
        contentPanel.add(Box.createVerticalStrut(25));

        // Statistics cards
        JPanel stats = new JPanel(new GridLayout(1, 4, 15, 15));
        stats.setBackground(BG);
        stats.setMaximumSize(new Dimension(Integer.MAX_VALUE, 105));
        stats.setAlignmentX(Component.LEFT_ALIGNMENT);

        totalJobsLabel = new JLabel("0");
        completedLabel = new JLabel("0");
        rejectedLabel = new JLabel("0");
        totalValueLabel = new JLabel("0");

        stats.add(createStatCard("TOTAL JOBS", totalJobsLabel, BLUE));
        stats.add(createStatCard("COMPLETED JOBS", completedLabel, GREEN));
        stats.add(createStatCard("REJECTED JOBS", rejectedLabel, RED));
        stats.add(createStatCard("TOTAL VALUE", totalValueLabel,
                                 new Color(145, 95, 205)));

        contentPanel.add(stats);
        contentPanel.add(Box.createVerticalStrut(25));

        // Job input form
        JPanel inputPanel = createPanel("Add New Job");

        inputPanel.setLayout(new GridLayout(0, 4, 12, 12));

        jobIdField = createField();
        deadlineField = createField();
        valueField = createField();
        resourceField = createField();
        processingTimeField = createField();

        machineCountField = createField();
        slotsField = createField();
        capacityField = createField();

        addField(inputPanel, "Job ID", jobIdField);
        addField(inputPanel, "Deadline", deadlineField);
        addField(inputPanel, "Job Value", valueField);
        addField(inputPanel, "Resource Required", resourceField);
        addField(inputPanel, "Processing Time", processingTimeField);
        addField(inputPanel, "Number of Machines", machineCountField);
        addField(inputPanel, "Available Time", slotsField);
        addField(inputPanel, "Resource Capacity / Machine",
                 capacityField);

        contentPanel.add(inputPanel);
        contentPanel.add(Box.createVerticalStrut(15));

        // Action buttons
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        actions.setBackground(BG);
        actions.setAlignmentX(Component.LEFT_ALIGNMENT);
        actions.setMaximumSize(new Dimension(Integer.MAX_VALUE, 55));

        JButton addButton = actionButton("Add Job", BLUE);
        JButton scheduleButton = actionButton("Schedule Jobs", GREEN);
        JButton deleteButton = actionButton("Delete Selected", RED);
        JButton clearButton = actionButton("Clear All",
                                           new Color(100, 110, 125));

        actions.add(addButton);
        actions.add(scheduleButton);
        actions.add(deleteButton);
        actions.add(clearButton);

        contentPanel.add(actions);
        contentPanel.add(Box.createVerticalStrut(15));

        // Job table
        JPanel tablePanel = createPanel("Available Jobs");

        String[] columns = {
            "Job ID", "Deadline", "Value",
            "Resource", "Processing Time"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        jobTable = new JTable(tableModel);
        styleTable(jobTable);

        JScrollPane tableScroll = new JScrollPane(jobTable);
        tableScroll.setPreferredSize(new Dimension(800, 180));
        tablePanel.add(tableScroll, BorderLayout.CENTER);

        contentPanel.add(tablePanel);
        contentPanel.add(Box.createVerticalStrut(20));

        // Results
        JPanel resultPanel = createPanel("Scheduling Results");

        resultArea = new JTextArea(12, 40);
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        resultArea.setBackground(new Color(249, 251, 255));
        resultArea.setBorder(
            BorderFactory.createEmptyBorder(12, 12, 12, 12)
        );

        JScrollPane resultScroll = new JScrollPane(resultArea);
        resultPanel.add(resultScroll, BorderLayout.CENTER);

        contentPanel.add(resultPanel);
        contentPanel.add(Box.createVerticalStrut(20));

        // Performance section
        JPanel performancePanel = createPanel("Performance Analysis");
        performancePanel.setLayout(new GridLayout(2, 2, 12, 12));

        resourcesLabel = new JLabel("Resources Used: 0");
        executionTimeLabel = new JLabel("Execution Time: 0 ns");
        complexityLabel = new JLabel(
            "Time Complexity: O(n log n + n x m)"
        );

        performancePanel.add(resourcesLabel);
        performancePanel.add(executionTimeLabel);
        performancePanel.add(complexityLabel);

        contentPanel.add(performancePanel);

        // Button actions
        addButton.addActionListener(e -> addJob());
        scheduleButton.addActionListener(e -> scheduleJobs());
        deleteButton.addActionListener(e -> deleteSelectedJob());
        clearButton.addActionListener(e -> clearAll());

        // Sidebar navigation
        dashboardNav.addActionListener(e -> scrollTo(0));
        jobsNav.addActionListener(e -> scrollTo(inputPanel.getY()));
        resultsNav.addActionListener(e -> scrollTo(resultPanel.getY()));
        performanceNav.addActionListener(
            e -> scrollTo(performancePanel.getY())
        );
    }

    private JButton navButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setForeground(WHITE);
        button.setBackground(NAVY);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorder(
            BorderFactory.createEmptyBorder(13, 12, 13, 8)
        );
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setMaximumSize(new Dimension(190, 48));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        return button;
    }

    private JPanel createStatCard(
        String title, JLabel value, Color accent
    ) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(WHITE);
        card.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                    new Color(225, 232, 242)
                ),
                BorderFactory.createEmptyBorder(14, 15, 14, 15)
            )
        );

        JLabel name = new JLabel(title);
        name.setFont(new Font("Arial", Font.BOLD, 11));
        name.setForeground(Color.GRAY);

        value.setFont(new Font("Arial", Font.BOLD, 25));
        value.setForeground(accent);

        card.add(name);
        card.add(Box.createVerticalStrut(10));
        card.add(value);

        return card;
    }

    private JPanel createPanel(String title) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(WHITE);
        panel.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(
                        new Color(220, 228, 240)
                    ),
                    title,
                    TitledBorder.LEFT,
                    TitledBorder.TOP,
                    new Font("Arial", Font.BOLD, 15),
                    NAVY
                ),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
            )
        );

        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 320));
        return panel;
    }

    private JTextField createField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(150, 32));
        return field;
    }

    private void addField(JPanel panel, String label,
                          JTextField field) {
        JPanel item = new JPanel(new BorderLayout(4, 5));
        item.setBackground(WHITE);

        JLabel name = new JLabel(label);
        name.setFont(new Font("Arial", Font.BOLD, 12));
        name.setForeground(NAVY);

        item.add(name, BorderLayout.NORTH);
        item.add(field, BorderLayout.CENTER);
        panel.add(item);
    }

    private JButton actionButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setForeground(WHITE);
        button.setBackground(color);
        button.setFocusPainted(false);
        button.setBorder(
            BorderFactory.createEmptyBorder(10, 18, 10, 18)
        );
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void styleTable(JTable table) {
        table.setRowHeight(32);
        table.setFont(new Font("Arial", Font.PLAIN, 13));
        table.setSelectionBackground(new Color(210, 228, 255));
        table.setGridColor(new Color(230, 235, 243));

        table.getTableHeader().setFont(
            new Font("Arial", Font.BOLD, 13)
        );
        table.getTableHeader().setBackground(
            new Color(225, 237, 255)
        );
        table.getTableHeader().setForeground(NAVY);
    }

    private void scrollTo(int position) {
        SwingUtilities.invokeLater(() ->
            mainScroll.getVerticalScrollBar().setValue(position)
        );
    }

    // Add Job
    private void addJob() {
        try {
            String id = jobIdField.getText().trim();

            int deadline = Integer.parseInt(deadlineField.getText());
            int value = Integer.parseInt(valueField.getText());
            int resource = Integer.parseInt(resourceField.getText());
            int processingTime =
                Integer.parseInt(processingTimeField.getText());

            if (id.isEmpty() || deadline <= 0 || value <= 0 ||
                resource <= 0 || processingTime <= 0) {
                throw new IllegalArgumentException();
            }

            for (Job job : jobs) {
                if (job.getJobId().equalsIgnoreCase(id)) {
                    JOptionPane.showMessageDialog(
                        this, "Job ID already exists!"
                    );
                    return;
                }
            }

            Job job = new Job(
                id, deadline, value, resource, processingTime
            );

            jobs.add(job);

            tableModel.addRow(new Object[]{
                id, deadline, value, resource, processingTime
            });

            jobIdField.setText("");
            deadlineField.setText("");
            valueField.setText("");
            resourceField.setText("");
            processingTimeField.setText("");

            updateStats();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                this, "Enter valid job details!"
            );
        }
    }

    // Schedule Jobs
    private void scheduleJobs() {
        try {
            int machineCount =
                Integer.parseInt(machineCountField.getText());

            int availableTime =
                Integer.parseInt(slotsField.getText());

            int resourceCapacity =
                Integer.parseInt(capacityField.getText());

            if (machineCount <= 0 || availableTime <= 0 ||
                resourceCapacity <= 0 || jobs.isEmpty()) {
                JOptionPane.showMessageDialog(
                    this, "Enter valid machine details and jobs!"
                );
                return;
            }

            JobScheduler scheduler = new JobScheduler();

            scheduler.createMachines(
                machineCount, availableTime, resourceCapacity
            );

            List<Job> completedJobs = scheduler.scheduleJobs(jobs);

            int totalValue = 0;
            int totalResources = 0;

            resultArea.setText(
                "JOB SCHEDULING RESULTS\n"
                + "Algorithm: Greedy (Value Based)\n\n"
            );

            for (Job job : completedJobs) {
                totalValue += job.getValue();
            }

            for (JobScheduler.Machine machine :
                    scheduler.getMachines()) {

                resultArea.append(
                    "\n" + machine.getMachineId() + "\n"
                );

                if (machine.getAssignedJobs().isEmpty()) {
                    resultArea.append("No jobs assigned.\n");
                }

                for (Job job : machine.getAssignedJobs()) {
                    totalResources += job.getResourceRequired();

                    resultArea.append(
                        "  Job: " + job.getJobId()
                        + " | Deadline: " + job.getDeadline()
                        + " | Processing Time: "
                        + job.getProcessingTime()
                        + " | Value: " + job.getValue()
                        + "\n"
                    );
                }

                resultArea.append(
                    "  Time Used: " + machine.getUsedTime()
                    + " / " + availableTime + "\n"
                );

                resultArea.append(
                    "  Resource Capacity: "
                    + resourceCapacity + "\n"
                );
            }

            int rejected = scheduler.getRejectedJobs();
            long time = scheduler.getExecutionTime();

            resultArea.append("\nSUMMARY\n");
            resultArea.append("Total Jobs: " + jobs.size() + "\n");
            resultArea.append(
                "Completed Jobs: " + completedJobs.size() + "\n"
            );
            resultArea.append("Rejected Jobs: " + rejected + "\n");
            resultArea.append("Total Value: " + totalValue + "\n");
            resultArea.append(
                "Total Resources Used: " + totalResources + "\n"
            );

            resultArea.append("\nPERFORMANCE ANALYSIS\n");
            resultArea.append("Execution Time: " + time + " ns\n");
            resultArea.append(
                "Time Complexity: " + scheduler.getTimeComplexity()
                + "\n"
            );

            totalJobsLabel.setText(String.valueOf(jobs.size()));
            completedLabel.setText(String.valueOf(completedJobs.size()));
            rejectedLabel.setText(String.valueOf(rejected));
            totalValueLabel.setText(String.valueOf(totalValue));

            resourcesLabel.setText(
                "Resources Used: " + totalResources
            );
            executionTimeLabel.setText(
                "Execution Time: " + time + " ns"
            );
            complexityLabel.setText(
                "Time Complexity: " + scheduler.getTimeComplexity()
            );

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                this, "Enter valid machine and scheduling details!"
            );
        }
    }

    private void deleteSelectedJob() {
        int selectedRow = jobTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                this, "Please select a job to delete."
            );
            return;
        }

        int modelRow = jobTable.convertRowIndexToModel(selectedRow);

        jobs.remove(modelRow);
        tableModel.removeRow(modelRow);

        updateStats();
        resetResults();
    }

    private void updateStats() {
        totalJobsLabel.setText(String.valueOf(jobs.size()));
    }

    private void resetResults() {
        completedLabel.setText("0");
        rejectedLabel.setText("0");
        totalValueLabel.setText("0");
        resourcesLabel.setText("Resources Used: 0");
        executionTimeLabel.setText("Execution Time: 0 ns");
        resultArea.setText("");
    }

    // Clear All
    private void clearAll() {
        jobs.clear();
        tableModel.setRowCount(0);
        resultArea.setText("");

        jobIdField.setText("");
        deadlineField.setText("");
        valueField.setText("");
        resourceField.setText("");
        processingTimeField.setText("");

        machineCountField.setText("");
        slotsField.setText("");
        capacityField.setText("");

        totalJobsLabel.setText("0");
        completedLabel.setText("0");
        rejectedLabel.setText("0");
        totalValueLabel.setText("0");

        resourcesLabel.setText("Resources Used: 0");
        executionTimeLabel.setText("Execution Time: 0 ns");
        complexityLabel.setText(
            "Time Complexity: O(n log n + n x m)"
        );
    }
}
