package compiler.table;

public class Symbol extends ISymbol {
    String key;
    String value;

    String type;

    public Symbol(String key, String value, String type) {
        this.key = key;
        this.value = value;
        this.type = type;
    }

    public String getValue() {
        return key + "_" + value;
    }

    @Override
    public void print() {
        System.out.println("Key: " + getValue() + " | Value : " + value + ", Type: " + type);
    }
}
