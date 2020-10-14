package sut.ist912.zelen.view.model;

import javafx.beans.property.SimpleStringProperty;

public class OpeationModel {

    private SimpleStringProperty opTs;
    private SimpleStringProperty opId;
    private SimpleStringProperty opSum;
    private SimpleStringProperty opFee;
    private SimpleStringProperty opType;
    private SimpleStringProperty opMember;
    private SimpleStringProperty opTotal;

    public OpeationModel(
            String opTs,
            String opId,
            String opSum,
            String opFee,
            String opTotal,
            String opType,
            String opMember
    ) {
        this.opTs = new SimpleStringProperty(opTs);
        this.opId = new SimpleStringProperty(opId);
        this.opSum = new SimpleStringProperty(opSum);
        this.opFee = new SimpleStringProperty(opFee);
        this.opType = new SimpleStringProperty(opType);
        this.opMember = new SimpleStringProperty(opMember);
        this.opTotal = new SimpleStringProperty(opTotal);
    }

    public String getOpTs() {
        return opTs.get();
    }

    public SimpleStringProperty opTsProperty() {
        return opTs;
    }

    public void setOpTs(String opTs) {
        this.opTs.set(opTs);
    }

    public String getOpId() {
        return opId.get();
    }

    public SimpleStringProperty opIdProperty() {
        return opId;
    }

    public void setOpId(String opId) {
        this.opId.set(opId);
    }

    public String getOpSum() {
        return opSum.get();
    }

    public SimpleStringProperty opSumProperty() {
        return opSum;
    }

    public void setOpSum(String opSum) {
        this.opSum.set(opSum);
    }

    public String getOpFee() {
        return opFee.get();
    }

    public SimpleStringProperty opFeeProperty() {
        return opFee;
    }

    public void setOpFee(String opFee) {
        this.opFee.set(opFee);
    }

    public String getOpType() {
        return opType.get();
    }

    public SimpleStringProperty opTypeProperty() {
        return opType;
    }

    public void setOpType(String opType) {
        this.opType.set(opType);
    }

    public String getOpMember() {
        return opMember.get();
    }

    public SimpleStringProperty opMemberProperty() {
        return opMember;
    }

    public void setOpMember(String opMember) {
        this.opMember.set(opMember);
    }

    public String getOpTotal() {
        return opTotal.get();
    }

    public SimpleStringProperty opTotalProperty() {
        return opTotal;
    }

    public void setOpTotal(String opTotal) {
        this.opTotal.set(opTotal);
    }
}
