
package com.college.sports;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonProperty;

public class IssueRecord {
    public long id;
    public long equipmentId;
    public String equipmentName;
    public String studentName;
    public String studentRollNo;
    public int issuedQuantity;
    public int returnedQuantity;
    public int damagedQuantity;
    public String issueDate;
    public String lastReturnDate;

    public IssueRecord() {}

    public IssueRecord(long id, Equipment e, String student, String roll, int qty) {
        this.id = id;
        this.equipmentId = e.id;
        this.equipmentName = e.name;
        this.studentName = student;
        this.studentRollNo = roll;
        this.issuedQuantity = qty;
        this.issueDate = LocalDateTime.now().toString();
    }

    @JsonProperty("pendingQuantity")
    public int getPendingQuantity() {
        return issuedQuantity - returnedQuantity;
    }

    @JsonProperty("pendingQuantity")
    public void setPendingQuantity(int pendingQuantity) {
        // Value is calculated from issuedQuantity and returnedQuantity.
    }
}
