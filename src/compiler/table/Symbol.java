package compiler.table;

public class Symbol extends ISymbol {
    String key;
    String value;

    public Symbol(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public void print() {

    }
}
