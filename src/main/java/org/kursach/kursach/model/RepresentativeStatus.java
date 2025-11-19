package org.kursach.kursach.model;

public enum RepresentativeStatus {
    ACTIVE("Активен"),
    ON_LEAVE("В отпуске"),
    PROBATION("Испытательный срок"),
    DISMISSED("Не сотрудничает");

    private final String label;

    RepresentativeStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}

