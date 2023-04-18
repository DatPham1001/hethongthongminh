package org.example.model;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Kmeans {
    DecimalFormat decimalFormat = new DecimalFormat("#.###");
    private int k;
    private int maxRun = 100;
    private int timeRan = 1;
    private int maxPoints = 16;
    private int maxDistanceToCentroid = 1000;
    private int maxDistanceToCentroidAtStart = maxDistanceToCentroid + 500;
    private List<Data> Datas;
    private List<Data> centroids;

    public Kmeans(int k, List<Data> Datas) {
        this.k = k;
        this.Datas = Datas;
        centroids = new ArrayList<Data>();
    }

    public List<ClusterO> classify() {
        System.out.println("RUNNG TIME :" + timeRan);
        // Initialize centroids randomly
        Random rand = new Random();
        for (int i = 0; i < k; i++) {
            Data centroid = Datas.get(rand.nextInt(Datas.size()));
            centroids.add(centroid);
        }

        List<ClusterO> clusters = new ArrayList<ClusterO>();
        for (int i = 0; i < k; i++) {
            clusters.add(new ClusterO());
        }

        boolean changed = true;
        while (changed) {
            changed = false;

            // Assign Datas to clusters
            for (Data data : Datas) {
                int closest = 0;
                double closestDist = Double.MAX_VALUE;
                for (int i = 0; i < k; i++) {
                    Data centroid = centroids.get(i);
                    double dist = calculateDistance(data.getLatitude(), data.getLongitude(),
                            centroid.getLatitude(), centroid.getLongitude());
                    if (dist < closestDist
//                            && Math.round(dist) < maxDistanceToCentroid
//                            && clusters.get(closest).getClusterDataList().size() < maxPoints
                    ) {
                        closest = i;
                        closestDist = dist;
                    }
                }
                clusters.get(closest).getClusterDataList().add(data);
            }

            // Update centroids
            for (int i = 0; i < k; i++) {
                double sumLat = 0, sumLong = 0;
                ClusterO clusterO = clusters.get(i);
                for (Data data : clusterO.getClusterDataList()) {
                    sumLat += data.getLatitude();
                    sumLong += data.getLongitude();
                }
                List<Data> cluster = clusterO.getClusterDataList();
                if (cluster.size() > 0) {
                    double centroidLat = sumLat / cluster.size();
                    double centroidLong = sumLong / cluster.size();
                    clusterO.setId(String.valueOf(i));
                    clusterO.setLatitude(centroidLat);
                    clusterO.setLongitude(centroidLong);
                    if (!clusterO.equals(clusters.get(i))) {
                        clusters.set(i, clusterO);
                        changed = true;
                    }
                }
            }
        }
//        check centroids
//        update distance to centroid
        int acceptedWrongNum = 5;
        int acceptedWrongCount = 0;
        for (ClusterO cluster : clusters) {
            for (Data data : cluster.getClusterDataList()) {
                double distance = calculateDistance(data.getLatitude(),
                        data.getLongitude(), cluster.getLatitude(), cluster.getLongitude());
                data.setDistance(Double.parseDouble(decimalFormat.format(distance)));
                //Chua toi 100 lan random
//                if (timeRan <= maxRun) {
//                    if (Math.round(distance) > maxDistanceToCentroid) {
//                        acceptedWrongCount += 1;
//                    }
//                    if (acceptedWrongCount > acceptedWrongNum) {
//                        timeRan = timeRan + 1;
//                        acceptedWrongCount = 0;
//                        classify();
//                    }
//                } else {
//                    //Tien hanh them tam cum
//                    System.out.println("===========ADD MORE CENTROID : " + k);
//                    k += 1;
//                    timeRan = 0;
//                    acceptedWrongCount = 0;
//                    classify();
//                }

            }

        }
        return clusters;
    }

    public static double calculateDistance(double lat1, double long1, double lat2, double long2) {
        double R = 6371000; // Earth radius in km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLong = Math.toRadians(long2 - long1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLong / 2) * Math.sin(dLong / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double d = R * c;

        return d;
    }
}
