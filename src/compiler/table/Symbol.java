package compiler.table;

import java.util.Objects;

public class Symbol extends ISymbol {
    public String field;
    public String name;
    public String type;
    public String is_defined;

    public Symbol(String field, String name, String type) {
        this.field = field;
        this.name = name;
        this.type = type;

        boolean is_int = Objects.equals(this.type, "int");
        boolean is_String = Objects.equals(this.type, "String");
        boolean is_float = Objects.equals(this.type, "float");
        boolean is_bool = Objects.equals(this.type, "bool");
        boolean is_builtin_type = is_int || is_String || is_float || is_bool;
        this.is_defined = (is_builtin_type) ? "True" : "False";
    }

    public String getValue() {
        return field + "_" + name;
    }

    public String getSignature() {
        return field + "_" + type + "_" + name;
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
