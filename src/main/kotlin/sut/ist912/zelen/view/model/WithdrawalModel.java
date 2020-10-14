package sut.ist912.zelen.view.model;

import javafx.beans.property.SimpleStringProperty;

public class WithdrawalModel {

    private SimpleStringProperty withdrawalTs;
    private SimpleStringProperty withdrawalId;
    private SimpleStringProperty withdrawalSum;
    private SimpleStringProperty withdrawalFee;
    private SimpleStringProperty withdrawalTotal;

    public WithdrawalModel(
            String withdrawalTs,
            String withdrawalId,
            String withdrawalSum,
            String withdrawalFee,
            String withdrawalTotal
    ) {
        this.withdrawalTs = new SimpleStringProperty(withdrawalTs);
        this.withdrawalId = new SimpleStringProperty(withdrawalId);
        this.withdrawalSum = new SimpleStringProperty(withdrawalSum);
        this.withdrawalFee = new SimpleStringProperty(withdrawalFee);
        this.withdrawalTotal = new SimpleStringProperty(withdrawalTotal);
    }

    public String getWithdrawalTs() {
        return withdrawalTs.get();
    }

    public SimpleStringProperty withdrawalTsProperty() {
        return withdrawalTs;
    }

    public void setWithdrawalTs(String withdrawalTs) {
        this.withdrawalTs.set(withdrawalTs);
    }

    public String getWithdrawalId() {
        return withdrawalId.get();
    }

    public SimpleStringProperty withdrawalIdProperty() {
        return withdrawalId;
    }

    public void setWithdrawalId(String withdrawalId) {
        this.withdrawalId.set(withdrawalId);
    }

    public String getWithdrawalSum() {
        return withdrawalSum.get();
    }

    public SimpleStringProperty withdrawalSumProperty() {
        return withdrawalSum;
    }

    public void setWithdrawalSum(String withdrawalSum) {
        this.withdrawalSum.set(withdrawalSum);
    }

    public String getWithdrawalFee() {
        return withdrawalFee.get();
    }

    public SimpleStringProperty withdrawalFeeProperty() {
        return withdrawalFee;
    }

    public void setWithdrawalFee(String withdrawalFee) {
        this.withdrawalFee.set(withdrawalFee);
    }

    public String getWithdrawalTotal() {
        return withdrawalTotal.get();
    }

    public SimpleStringProperty withdrawalTotalProperty() {
        return withdrawalTotal;
    }

    public void setWithdrawalTotal(String withdrawalTotal) {
        this.withdrawalTotal.set(withdrawalTotal);
    }
}
