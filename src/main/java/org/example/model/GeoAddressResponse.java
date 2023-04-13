package org.example.model;

import java.util.List;

public class GeoAddressResponse {
    private String status;
    private List<GeoAddressData> results;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<GeoAddressData> getResults() {
        return results;
    }

    public void setResults(List<GeoAddressData> result) {
        this.results = result;
    }
}
