package compiler.table;

public class Symbol extends ISymbol {
    String field;
    String name;
    String type;
    boolean is_defined;

    public Symbol(String field, String name, String type) {
        this.field = field;
        this.name = name;
        this.type = type;
        this.is_defined = false;
    }

    public String getValue() {
        return field + "_" + name;
    }

    String parentType() {
        if (parent instanceof ClassScope) {
            return "ClassField";
        } else if (parent instanceof MethodScope) {
            return "MethodVar";
        } else if (parent instanceof BlockScope) {
            return "LocalVar";
        } else {
            return "var";
        }
    }

    @Override
    public void print() {
        System.out.println("Key: " + getValue() + " | Value : " + parentType() + "(name: " + name + "), (Type: " + type + "), isDefined: " + is_defined);
    }
}
