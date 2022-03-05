package homework;

public enum Banknote {

    ONE_THOUSAND(1000),
    FIVE_HUNDRED(500),
    ONE_HUNDRED(100);

    private final Integer faceValue;

    Banknote(Integer faceValue) {
    this.faceValue = faceValue;
    }

    public int getFaceValue() {
        return faceValue;
    }
}