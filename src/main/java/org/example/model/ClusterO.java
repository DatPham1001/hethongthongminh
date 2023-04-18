package org.example.model;

import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class ClusterO  {
    private String id;
    private double longitude;
    private double latitude;
    private String address;
    private List<GeoAddressData> geoAddresses;
    private double distanceToTarget;
    private List<Data> clusterDataList = new ArrayList<>();

}
