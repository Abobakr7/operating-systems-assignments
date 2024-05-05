package app.cpu.scheduling;

import java.awt.Color;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedList;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.CategoryItemRenderer;
import org.jfree.data.category.IntervalCategoryDataset;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.data.time.SimpleTimePeriod;

public class GanttChart extends JFrame {
    private final Map<String, Color> colors;
    private final String title;
    private final int nProcess;
    private final double averageTAT, averageWT;
    private final boolean isPreemptive;
    private LinkedList<ProcessClass> processes;
    
    public GanttChart(String title, int nProcess, double averageTAT, double averageWT, boolean isPreemptive, LinkedList<ProcessClass> processes) {
        this.colors = new HashMap<>();
        this.title = title;
        this.nProcess = nProcess;
        this.averageTAT = averageTAT;
        this.averageWT = averageWT;
        this.isPreemptive = isPreemptive;
        this.processes = processes;
        this.mapColors();
    }
    
    public void buildChartWindow() {
        ChartPanel chartPanel = this.getChart();
        chartPanel.setBounds(0, 0, 750, 500);
        
        JFrame frame = new JFrame(this.title);
        frame.setSize(1200, 600);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setLayout(null);
        
        String[][] data = new String[this.processes.size()][7];
        for (int i = 0; i < this.processes.size(); ++i) {
            data[i][0] = Integer.toString(i+1);
            data[i][1] = this.processes.get(i).getName();
            data[i][2] = Integer.toString(this.processes.get(i).getStartTime());
            data[i][3] = Integer.toString(this.processes.get(i).getCompelationTime());
            data[i][4] = Integer.toString(this.processes.get(i).getWaitingTime());
            data[i][5] = Integer.toString(this.processes.get(i).getTurnAroundTime());
            data[i][6] = Integer.toString(this.processes.get(i).getQuantum());
        }
        String[] columns = {"Order", "Process Name", "Start Time", "Compelation Time", "Waiting Time", "Turn Around Time", "Quantum"};
        JTable table = new JTable(data, columns);
        JScrollPane sp = new JScrollPane(table);
        sp.setBounds(750, 0, 420, 400);
        
        JLabel l1 = new JLabel("Average Turn Around Time: " + this.averageTAT + "ms");
        l1.setBounds(800, 400, 300, 30);
        JLabel l2 = new JLabel("Average Waiting Time: " + this.averageWT + "ms");
        l2.setBounds(800, 430, 300, 30);

        frame.add(chartPanel); frame.add(sp);
        frame.add(l1); frame.add(l2);
        frame.setVisible(true);
    }
    
    private ChartPanel getChart() {
        IntervalCategoryDataset dataset = this.isPreemptive ? this.getPreemptiveDataset() : this.getNonPreemptiveDataset();
        
        // make the time axis in milliseconds
        JFreeChart chart = ChartFactory.createGanttChart(this.title, "Processes", "Time (ms)", dataset);
        DateAxis axis = (DateAxis) chart.getCategoryPlot().getRangeAxis();
        axis.setDateFormatOverride(new java.text.SimpleDateFormat("SSS"));
        
        // paint each job with color
        CategoryItemRenderer renderer = ((CategoryPlot)chart.getPlot()).getRenderer();
        for (int i = 0; i < this.processes.size(); ++i) {
            renderer.setSeriesPaint(i, this.colors.get(this.processes.get(i).getColor()));
        }
        return new ChartPanel(chart);
    }
    
    private IntervalCategoryDataset getNonPreemptiveDataset() {
        TaskSeriesCollection dataset = new TaskSeriesCollection();
        for (int i = 0; i < this.processes.size(); ++i) {
            TaskSeries s = new TaskSeries(this.processes.get(i).getName());
            s.add(new Task(this.processes.get(i).getName(), new SimpleTimePeriod(this.processes.get(i).getStartTime(), this.processes.get(i).getCompelationTime())));
            dataset.add(s);
        }
        return dataset;
    }

    private IntervalCategoryDataset getPreemptiveDataset() {
        LinkedList<ProcessClass>[] prcs = new LinkedList[this.nProcess];
        for (int i = 0; i < prcs.length; ++i) {
            prcs[i] = new LinkedList<>();
        }
        for (int i = 0; i < this.processes.size(); ++i) {
            int index = this.processes.get(i).getOrder();
            prcs[index].add(this.processes.get(i));
        }

        TaskSeriesCollection dataset = new TaskSeriesCollection();
        for (int i = 0; i < this.nProcess; ++i) {
            TaskSeries ts = new TaskSeries(prcs[i].get(0).getName());
            Task t = new Task(prcs[i].get(0).getName(), new SimpleTimePeriod(prcs[i].get(0).getStartTime(), prcs[i].get(0).getCompelationTime()));
            for (int j = 1; j < prcs[i].size(); ++j) {
                Task tk = new Task(prcs[i].get(j).getName(), new SimpleTimePeriod(prcs[i].get(j).getStartTime(), prcs[i].get(j).getCompelationTime()));
                t.addSubtask(tk);
            }
            ts.add(t);
            dataset.add(ts);
        }
        return dataset;
    }
    
    private void mapColors() {
        this.colors.put("RED", Color.red);
        this.colors.put("BLUE", Color.blue);
        this.colors.put("GREEN", Color.green);
        this.colors.put("YELLOW", Color.yellow);
        this.colors.put("ORANGE", Color.orange);
        this.colors.put("PINK", Color.pink);
        this.colors.put("CYAN", Color.cyan);
        this.colors.put("MAGENTA", Color.magenta);
        this.colors.put("GRAY", Color.gray);
        this.colors.put("BLACK", Color.black);
        this.colors.put("WHITE", Color.white);
    }
}
