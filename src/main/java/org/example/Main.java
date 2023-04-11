package org.example;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import com.google.gson.Gson;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.model.ClusterO;
import org.example.model.Data;
import weka.clusterers.SimpleKMeans;
import weka.core.*;

public class Main implements ActionListener {
    private static final double LAT_MIN = -90;
    private static final double LAT_MAX = 90;
    private static final double LONG_MIN = -180;
    private static final double LONG_MAX = 180;
    private JFrame frame;
    private JButton importButton, showButton, applyButton, kmeansButton, showClusterButton;
    private JTable table;
    private int numCluster = 4;
    private JTextField numClusterField, distanceField;
    private double distanceThreshold = 1;
    private List<Data> dataList = new ArrayList<>();
    private List<ClusterO> clusterOS = new ArrayList<>();
    static final Gson gson = new Gson();

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Main window = new Main();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public Main() {
        initialize();
    }

    private void initialize() {
        Data[] datas = gson.fromJson("[{\"id\":\"1\",\"name\":\"Nguyễn Hữu Ánh\",\"address\":\"Số nhà 45, ngõ 120, Láng Hạ, Đống Đa, Hà Nội\",\"longitude\":21.013289547968125,\"latitude\":105.81032244281,\"distance\":4.4},{\"id\":\"2\",\"name\":\"Phạm Thu Hà\",\"address\":\"Số nhà 10, ngõ 172,Pháo Đài Láng, Đống Đa, Hà Nội\",\"longitude\":21.02216340050488,\"latitude\":105.806701408824,\"distance\":4.8},{\"id\":\"3\",\"name\":\"Trần Thanh Tùng\",\"address\":\"Số nhà 17, ngõ 3, Kim Mã, Ba Đình, Hà Nội\",\"longitude\":21.033295824229658,\"latitude\":105.822305878556,\"distance\":3.2},{\"id\":\"4\",\"name\":\"Nguyễn Thị Thanh\",\"address\":\"Số nhà 102, đường Cầu Giấy, Cầu Giấy, Hà Nội\",\"longitude\":21.035279463982057,\"latitude\":105.791127076644,\"distance\":6.8},{\"id\":\"5\",\"name\":\"Trần Văn Đông\",\"address\":\"Số nhà 87, đường Nguyễn Khánh Toàn, Cầu Giấy, Hà Nội\",\"longitude\":21.043626214736467,\"latitude\":105.795255224729,\"distance\":5.7},{\"id\":\"6\",\"name\":\"Vũ Thị Anh\",\"address\":\"Số nhà 56, đường Trần Hưng Đạo, Hoàn Kiếm, Hà Nội\",\"longitude\":21.024991980344527,\"latitude\":105.844420055303,\"distance\":2.5},{\"id\":\"7\",\"name\":\"Lê Thị Lan\",\"address\":\"Số nhà 34, ngõ 163, Tây Sơn, Đống Đa, Hà Nội\",\"longitude\":21.02040270485661,\"latitude\":105.812927288679,\"distance\":3.6},{\"id\":\"8\",\"name\":\"Đỗ Minh Hiền\",\"address\":\"Số nhà 49, ngõ 12, Đặng Thai Mai, Tây Hồ, Hà Nội\",\"longitude\":21.049874313087408,\"latitude\":105.827454503227,\"distance\":5.2},{\"id\":\"9\",\"name\":\"Hoàng Văn Nam\",\"address\":\"Số nhà 34, ngõ 44, phố Lê Văn Thiêm, Nhân Chính, Thanh Xuân, Hà Nội\",\"longitude\":21.005800856669975,\"latitude\":105.801562186119,\"distance\":3.9},{\"id\":\"10\",\"name\":\"Tuyết Ngân Vương\",\"address\":\"64 Nguyễn Trãi, Thanh Xuân, Hà Nội\",\"longitude\":21.004989829364796,\"latitude\":105.8141240628,\"distance\":4.3},{\"id\":\"11\",\"name\":\"Trúc Mai Lâm\",\"address\":\"12 Ngõ 87 Láng Hạ, Đống Đa, Hà Nội\",\"longitude\":21.008847514699834,\"latitude\":105.812843463,\"distance\":3.9},{\"id\":\"12\",\"name\":\"Đình Duy Nguyễn\",\"address\":\"29 Võ Chí Công, Xuân La, Tây Hồ, Hà Nội\",\"longitude\":21.06745471409359,\"latitude\":105.8029101701,\"distance\":5.9},{\"id\":\"13\",\"name\":\"Minh Châu Hồ\",\"address\":\"23B Ngõ 3/54, Tổ 11, Phố Vọng, Hai Bà Trưng, Hà Nội\",\"longitude\":20.998898210732417,\"latitude\":105.843757166,\"distance\":4.1},{\"id\":\"14\",\"name\":\"Ngọc Minh Đặng\",\"address\":\"19 Ngõ 93 Lò Đúc, Hai Bà Trưng, Hà Nội\",\"longitude\":21.00797749264913,\"latitude\":105.856252953,\"distance\":3.2},{\"id\":\"15\",\"name\":\"Quỳnh Anh Lý\",\"address\":\"102 Nguyễn Trãi, Thanh Xuân, Hà Nội\",\"longitude\":20.99535809979263,\"latitude\":105.81500733,\"distance\":5.2},{\"id\":\"16\",\"name\":\"Thịnh Duy Võ\",\"address\":\"35 Ngõ 118 Trần Bình, Mai Dịch, Cầu Giấy, Hà Nội\",\"longitude\":21.0375533688712,\"latitude\":105.7873040835,\"distance\":5.6},{\"id\":\"17\",\"name\":\"Thu Hường Trần\",\"address\":\"3 Ngõ 12 Nguyễn Văn Ngọc, Giảng Võ, Ba Đình, Hà Nội\",\"longitude\":21.033303266055118,\"latitude\":105.81251213,\"distance\":4.4},{\"id\":\"18\",\"name\":\"Hoài Phương Ngô\",\"address\":\"30 Lương Định Của, Đống Đa, Hà Nội\",\"longitude\":21.02112194283696,\"latitude\":105.811295704,\"distance\":4.8},{\"id\":\"19\",\"name\":\"Văn Hiển Nguyễn\",\"address\":\"37B1 Nguyễn Phong Sắc, Cầu Giấy, Hà Nội\",\"longitude\":21.03733321907321,\"latitude\":105.793096151,\"distance\":5.5},{\"id\":\"20\",\"name\":\"Bạch Trung Ngân\",\"address\":\"15 Đàm Quang Trung, Thanh Lương, Hai Bà Trưng, Hà Nội\",\"longitude\":21.005207223010316,\"latitude\":105.851051968269,\"distance\":1.92},{\"id\":\"21\",\"name\":\"Văn Thị Hiền\",\"address\":\"41/320 Đường Láng, Láng Thượng, Đống Đa, Hà Nội\",\"longitude\":21.031639642224583,\"latitude\":105.80130106854,\"distance\":4.38}]", Data[].class);
        dataList = new ArrayList<Data>(Arrays.asList(datas));
        frame = new JFrame();
        frame.setBounds(300, 300, 1200, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        JPanel buttonPanel = new JPanel(new FlowLayout());
        // Thêm nút Import file vào panel
        importButton = new JButton("Import file");
        importButton.addActionListener(this);
        buttonPanel.add(importButton);

        // Thêm nút Show vào panel
        showButton = new JButton("Show");
        showButton.addActionListener(this);
        buttonPanel.add(showButton);

        // Thêm panel vào frame với layout là BorderLayout và vị trí là SOUTH
        frame.getContentPane().add(buttonPanel, BorderLayout.NORTH);
        JPanel actionPanel = new JPanel();
        actionPanel.setLayout(new BoxLayout(actionPanel, BoxLayout.Y_AXIS));
        JPanel configPanel = new JPanel(new FlowLayout());
        JLabel numClusterLabel = new JLabel("Number of clusters:");
        numClusterField = new JTextField();
        numClusterField.setPreferredSize(new Dimension(100, 20));


        JLabel distanceLabel = new JLabel("Distance from clusters:");
        distanceField = new JTextField();
        distanceField.setPreferredSize(new Dimension(100, 20));
        applyButton = new JButton("Apply");
        applyButton.addActionListener(this);

        configPanel.add(numClusterLabel);
        configPanel.add(numClusterField);
        configPanel.add(distanceLabel);
        configPanel.add(distanceField);
        configPanel.add(applyButton);


        //Kmeans
        kmeansButton = new JButton("KMeans");
        kmeansButton.addActionListener(this);
        configPanel.add(kmeansButton);
        //showClusterButton
        showClusterButton = new JButton("Cluster");
        showClusterButton.addActionListener(this);
        configPanel.add(showClusterButton);

        actionPanel.add(configPanel);

        frame.getContentPane().add(actionPanel, BorderLayout.CENTER);
//        frame.getContentPane().add(configPanel, BorderLayout.CENTER);

    }

    private void importExcel() {
        JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Excel files", "xlsx", "xls");
        fileChooser.setFileFilter(filter);
        int returnVal = fileChooser.showOpenDialog(null);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            try {
                FileInputStream inputStream = new FileInputStream(file);
                XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
                XSSFSheet sheet = workbook.getSheetAt(0);
                Iterator<Row> rowIterator = sheet.iterator();

                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    if (row.getRowNum() == 0)
                        continue;
                    Data data = new Data();
                    data.setId(String.valueOf(row.getRowNum()));
                    Cell name = row.getCell(1);
                    data.setName(name.getStringCellValue());
                    Cell address = row.getCell(2);
                    data.setAddress(address.getStringCellValue());
                    Cell longitude = row.getCell(3);
                    data.setLongitude(Double.parseDouble(longitude.getStringCellValue()));
                    Cell latitude = row.getCell(4);
                    data.setLatitude(Double.parseDouble(String.valueOf(latitude.getNumericCellValue())));
                    Cell distance = row.getCell(5);
                    data.setDistance(Double.parseDouble(String.valueOf(distance.getNumericCellValue())));
                    dataList.add(data);
                }
                System.out.println(gson.toJson(dataList));
                workbook.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean tableVisible = false;

    private void showData() {
        Object[][] data = new Object[dataList.size()][6];

        for (int i = 0; i < dataList.size(); i++) {
            data[i][0] = dataList.get(i).getId();
            data[i][1] = dataList.get(i).getName();
            data[i][2] = dataList.get(i).getAddress();
            data[i][3] = dataList.get(i).getLongitude();
            data[i][4] = dataList.get(i).getLatitude();
            data[i][5] = dataList.get(i).getDistance();
        }
        // Tạo một mảng chứa tên cột của bảng
        String[] columnNamesView = {"Id", "Tên", "Địa chỉ", "Kinh độ", "Vĩ độ", "Khoảng cách"};
        // Tạo bảng với dữ liệu và tên cột đã chuẩn bị
        JTable table = new JTable(data, columnNamesView);
        // Tạo cửa sổ dialog
        JDialog dialog = new JDialog(frame, "Dữ liệu phân cụm", true);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.getContentPane().add(new JScrollPane(table));
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
        frame.revalidate();
        frame.repaint();
    }

    private void showDataClusters() {
        Object[][] data = new Object[dataList.size()][8];
        int k = 0;
//        for (ClusterO clusterO : clusterOS) {
//            List<Data> dataList = clusterO.getClusterDataList();
//            k = k + 1;
//            for (int i = 0; i < dataList.size(); i++) {
//                Data data1 = dataList.get(i);
//                int a = i + k;
//                data[i][0] = clusterO.getId();
//                data[i][1] = clusterO.getLongitude();
//                data[i][2] = clusterO.getLatitude();
//                data[i][3] = data1.getId();
//                data[i][4] = data1.getName();
//                data[i][5] = data1.getAddress();
//                data[i][6] = data1.getLongitude();
//                data[i][7] = data1.getLatitude();
//
//            }
//        }
//
        JTable table = new JTable();
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ID");
        model.addColumn("Kinh Độ Cụm");
        model.addColumn("Vĩ Độ Cụm");
        model.addColumn("Id Điểm");
        model.addColumn("Tên điểm");
        model.addColumn("Địa chỉ");
        model.addColumn("Kinh độ");
        model.addColumn("Vĩ độ");

        for (ClusterO clusterO : clusterOS) {
            List<Data> dataList = clusterO.getClusterDataList();
            for (int i = 0; i < dataList.size(); i++) {
                Data data1 = dataList.get(i);
                Object[] row = new Object[9];
                row[0] = clusterO.getId();
                row[1] = clusterO.getLongitude();
                row[2] = clusterO.getLatitude();
                row[3] = data1.getId();
                row[4] = data1.getName();
                row[5] = data1.getAddress();
                row[6] = data1.getLongitude();
                row[7] = data1.getLatitude();
                model.addRow(row);

            }
        }
//        // Tạo một mảng chứa tên cột của bảng
        String[] columnNamesView = {"Id Cụm", "Kinh Độ Cụm",
                "Vĩ Độ Cụm", "Id Điểm", "Tên điểm", "Địa chỉ", "Kinh độ", "Vĩ độ"};
//        // Tạo bảng với dữ liệu và tên cột đã chuẩn bị
//        JTable table = new JTable(data, columnNamesView);
        table.setModel(model);
        // Tạo cửa sổ dialog
        JDialog dialog = new JDialog(frame, "Dữ liệu phân cụm", true);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.getContentPane().add(new JScrollPane(table));
        dialog.pack();
        dialog.setSize(1000, 600);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
        frame.revalidate();
        frame.repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == importButton) {
            importExcel();
        } else if (e.getSource() == showButton) {
            showData();
        } else if (e.getSource() == applyButton) {
            try {
                numCluster = Integer.parseInt(numClusterField.getText());
                distanceThreshold = Double.parseDouble(distanceField.getText());
                // Thực hiện các thao tác liên quan đến việc sử dụng các tham số
                // Như phân cụm bằng thuật toán Kmean với số lượng tâm cụm và khoảng cách cần thiết
                System.out.println("Num cluster:" + numCluster);
                System.out.println("Distance:" + distanceThreshold);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid input", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == kmeansButton) {
            // Lấy số lượng cụm và khoảng cách từ các trường nhập liệu
//            int numCluster = Integer.parseInt(numClusterField.getText());
//            double distance = Double.parseDouble(distanceField.getText());
            clusterOS = new ArrayList<>();
            kMeansCluster();
        } else if (e.getSource() == showClusterButton) {
            showDataClusters();
        }

    }

    public void kMeansCluster() {

        try {
            // Tạo đối tượng Instances
            Instances instances = createInstances();

            // Áp dụng thuật toán KMeans
            SimpleKMeans kmeans = new SimpleKMeans();
            kmeans.setNumClusters(numCluster); // số cụm cần phân
            kmeans.setMaxIterations(500);
            kmeans.setDistanceFunction(new EuclideanDistance());
            kmeans.buildClusterer(instances);

            //Kiểm tra tâm cụm hợp lệ


            // Lấy các tâm cụm và in ra
            Instances centroids = kmeans.getClusterCentroids();
            List<ClusterO> clusterList = new ArrayList<>();
            for (int i = 0; i < centroids.numInstances(); i++) {
                System.out.println("Centroid " + (i + 1) + ": " + centroids.instance(i));
                ClusterO clusterO = new ClusterO();
                clusterO.setId(String.valueOf(i));
                 Instance instance = centroids.instance(i);
                clusterO.setLongitude(instance.value(0));
                clusterO.setLatitude(instance.value(1));
                clusterList.add(clusterO);
            }
            int[] assignments = new int[instances.numInstances()];
            for (int i = 0; i < instances.numInstances(); i++) {
                Instance instance = instances.instance(i);
                int cluster = kmeans.clusterInstance(instance);
                System.out.println("Instance " + i + " belongs to cluster " + cluster);
                ClusterO clusterO = clusterList.get(cluster);
                clusterO.getClusterDataList().add(dataList.get(i));
                assignments[i] = kmeans.clusterInstance(instance);
            }

            // Compute new cluster centroids
            Instances newCentroids = recalculateCentroids(instances, assignments, kmeans);
            System.out.println(gson.toJson(clusterList));
            //Apply new centroids
            for (int i = 0; i < centroids.numInstances(); i++) {
                ClusterO clusterO =clusterList.get(i);
                Instance instance = centroids.instance(i);
                clusterO.setLongitude(instance.value(0));
                clusterO.setLatitude(instance.value(1));
                clusterOS.add(i,clusterO);
            }



            // Hiển thị các cụm trên bản đồ
//                showClustersOnMap(clusters);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public static double calculateDistance(double lat1, double long1, double lat2, double long2) {
        double R = 6371; // Earth radius in km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLong = Math.toRadians(long2 - long1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLong / 2) * Math.sin(dLong / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = R * c;

        return d;
    }

    public Instances recalculateCentroids(Instances instances, int[] assignments, SimpleKMeans kmeans) throws Exception {
        int numClusters = kmeans.getNumClusters();
        Instances centroids = kmeans.getClusterCentroids();

        // Initialize a matrix to hold the sum of instances for each cluster
        double[][] clusterSums = new double[numClusters][instances.numAttributes()];

        // Count the number of instances in each cluster
        int[] clusterCounts = new int[numClusters];

        // Calculate the sum of instances for each cluster
        for (int i = 0; i < instances.numInstances(); i++) {
            Instance instance = instances.instance(i);
            int cluster = assignments[i];
            clusterCounts[cluster]++;
            for (int j = 0; j < instances.numAttributes(); j++) {
                clusterSums[cluster][j] += instance.value(j);
            }
        }

        // Update the cluster centroids
        for (int i = 0; i < numClusters; i++) {
            Instance centroid = new DenseInstance(instances.numAttributes());
            for (int j = 0; j < instances.numAttributes(); j++) {
                centroid.setValue(j, clusterSums[i][j] / clusterCounts[i]);
            }
            centroids.set(i, centroid);
        }

        return centroids;
    }


    public void draw() {
        // Create a new group to hold the points
        Group root = new Group();

        // Calculate the scaling factors to map latitude and longitude to x-y coordinates
        double latRange = LAT_MAX - LAT_MIN;
        double longRange = LONG_MAX - LONG_MIN;
        double latScale = 500 / latRange; // 500 is the height of the scene
        double longScale = 800 / longRange; // 800 is the width of the scene

        // Loop over the points and create a Circle for each one
        for (ClusterO clusterO : clusterOS) {
            double x = (clusterO.getLongitude() - LONG_MIN) * longScale;
            double y = 500 - (clusterO.getLatitude() - LAT_MIN) * latScale;
            Circle circle = new Circle(x, y, 5);
            circle.setFill(Color.RED);// 5 is the radius of the circle
            root.getChildren().add(circle);
        }

        // Create a new scene and set it on the stage
        Scene scene = new Scene(root, 800, 500);
        Stage primaryStage = new Stage();
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public double calculateDistance(double[] a, double[] b) {
        double sum = 0;
        for (int i = 0; i < a.length; i++) {
            sum += Math.pow(a[i] - b[i], 2);
        }
        return Math.sqrt(sum);
    }

    private Instances createInstances() {
        // Tạo các thuộc tính của dữ liệu (name, address, longitude, latitude, distance)
        ArrayList<Attribute> attributes = new ArrayList<>();
//        attributes.add(new Attribute("id"));
//        attributes.add(new Attribute("name"));
//        attributes.add(new Attribute("address"));
        attributes.add(new Attribute("longitude"));
        attributes.add(new Attribute("latitude"));
//        attributes.add(new Attribute("distance"));

        // Tạo đối tượng Instances
        Instances dataset = new Instances("my_dataset", attributes, 0);

        // Thêm các object vào dataset
        for (Data obj : dataList) {
            double[] values = new double[2];
//            values[0] = dataset.attribute("id").addStringValue(obj.getId());
//            values[1] = dataset.attribute("name").addStringValue(obj.getName());
//            values[2] = dataset.attribute("address").addStringValue(obj.getAddress());
            values[0] = obj.getLongitude();
            values[1] = obj.getLatitude();
//            values[5] = obj.getDistance();

            DenseInstance instance = new DenseInstance(1.0, values);
            dataset.add(instance);
        }
        return dataset;
    }
}
