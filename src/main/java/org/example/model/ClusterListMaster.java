package org.example.model;

import java.util.ArrayList;
import java.util.List;

public class ClusterListMaster {
    private int numOverMaxDistance = 0;
    private List<ClusterO> clusterOList = new ArrayList<>();
    private double totalDistance = 0;

    public double getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(double totalDistance) {
        this.totalDistance = totalDistance;
    }

    public int getNumOverMaxDistance() {
        return numOverMaxDistance;
    }

    public void setNumOverMaxDistance(int numOverMaxDistance) {
        this.numOverMaxDistance = numOverMaxDistance;
    }

    public List<ClusterO> getClusterOList() {
        return clusterOList;
    }

    public void setClusterOList(List<ClusterO> clusterOList) {
        this.clusterOList = clusterOList;
    }
}
