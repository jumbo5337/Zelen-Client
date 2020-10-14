package sut.ist912.zelen.view.model;

import javafx.beans.property.SimpleStringProperty;

public class DepositModel {

    private SimpleStringProperty depositTs;
    private SimpleStringProperty depositId;
    private SimpleStringProperty depositSum;

    public DepositModel(String depositTs, String depositId, String depositSum) {
        this.depositTs = new SimpleStringProperty(depositTs);
        this.depositId = new SimpleStringProperty(depositId);
        this.depositSum = new SimpleStringProperty(depositSum);
    }

    public String getDepositTs() {
        return depositTs.get();
    }

    public SimpleStringProperty depositTsProperty() {
        return depositTs;
    }

    public void setDepositTs(String depositTs) {
        this.depositTs.set(depositTs);
    }

    public String getDepositId() {
        return depositId.get();
    }

    public SimpleStringProperty depositIdProperty() {
        return depositId;
    }

    public void setDepositId(String depositId) {
        this.depositId.set(depositId);
    }

    public String getDepositSum() {
        return depositSum.get();
    }

    public SimpleStringProperty depositSumProperty() {
        return depositSum;
    }

    public void setDepositSum(String depositSum) {
        this.depositSum.set(depositSum);
    }
}
