package com.company.ceriomollica;

public class Edge {


    private boolean isIncluded = false;
    private boolean isPrinted = false;

    public Edge() {
    }

    public boolean isIncluded() {
        return isIncluded;
    }

    public void setIncluded(boolean included) {
        isIncluded = included;
    }

    public boolean isPrinted() {
        return isPrinted;
    }

    public void setPrinted(boolean printed) {
        isPrinted = printed;
    }
}
