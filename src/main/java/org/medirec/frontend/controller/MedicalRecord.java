package org.medirec.frontend.controller;

public class MedicalRecord {
    private String date;
    private String examType;
    private String meds;
    private String diagnosis;

    public MedicalRecord(String date, String examType, String meds, String diagnosis) {
        this.date = date;
        this.examType = examType;
        this.meds = meds;
        this.diagnosis = diagnosis;
    }

    public String getDate() {
        return date;
    }

    public String getExamType() {
        return examType;
    }

    public String getMeds() {
        return meds;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setExamType(String examType) {
        this.examType = examType;
    }

    public void setMeds(String meds) {
        this.meds = meds;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    @Override
    public String toString() {
        return "MedicalRecord{" +
                "date='" + date + '\'' +
                ", examType='" + examType + '\'' +
                ", meds='" + meds + '\'' +
                ", diagnosis='" + diagnosis + '\'' +
                '}';
    }
}
