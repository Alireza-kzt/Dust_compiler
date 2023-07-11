package compiler;

public class Error {
    String type;
    String message;
    int line;

    public Error(String type, String message, int line) {
        this.type = type;
        this.message = message;
        this.line = line;
    }

    public String toString() {
        return "Error: " + type + ", line " + line + ", " + message;
    }
}
