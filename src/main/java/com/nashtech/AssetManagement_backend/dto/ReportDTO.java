package com.nashtech.AssetManagement_backend.dto;

import lombok.Data;

@Data
public class ReportDTO {
    private String category;
    private int total;
    private int assigned;
    private int available;
    private int notAvailable;
    private int waitingForRecycle;
    private int recycled;

    public ReportDTO(String category, int total, int assigned, int available, int notAvailable, int waitingForRecycle, int recycled) {
        this.category = category;
        this.total = total;
        this.assigned = assigned;
        this.available = available;
        this.notAvailable = notAvailable;
        this.waitingForRecycle = waitingForRecycle;
        this.recycled = recycled;
    }
}
