package com.example.mediccompanion;

public class Medications {
     private String drugName, drugType, drugQuantity,amountPerDose,  drugDosages;

    public Medications(String drugName, String drugType, String drugQuantity, String amountPerDose, String drugDosages) {
        this.setDrugName(drugName);
        this.setDrugDosages(drugDosages);
        this.setDrugQuantity(drugQuantity);
        this.setDrugType(drugType);
        this.setAmountPerDose(amountPerDose);
    }

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public String getDrugDosages() {
        return drugDosages;
    }

    public String getDrugQuantity() {
        return drugQuantity;
    }

    public String getDrugType() {
        return drugType;
    }

    public void setDrugDosages(String drugDosages) {
        this.drugDosages = drugDosages;
    }

    public String getAmountPerDose() {
        return amountPerDose;
    }

    public void setAmountPerDose(String amountPerDose) {
        this.amountPerDose = amountPerDose;
    }

    public void setDrugQuantity(String drugQuantity) {
        this.drugQuantity = drugQuantity;
    }

    public void setDrugType(String drugType) {
        this.drugType = drugType;
    }
}
