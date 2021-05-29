package domain.model;


public enum Status {
    CHOOSING("CHOOSING"), READY("READY"), VOTING("VOTING"), DONE("DONE");
    // what are in the parentheses are the codes
    private String code;

    private Status(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}

