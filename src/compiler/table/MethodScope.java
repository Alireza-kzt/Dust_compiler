package compiler.table;

public class MethodScope extends IScope {
    public String parameters = "";
    public int parametersLen = 0;
    String returnType;

    public MethodScope(String name, int line, String returnType) {
        super(name, line);
        this.returnType = returnType;
    }

    @Override
    public Symbol add(Symbol symbol) {
        if (symbol.field.toLowerCase().contains("param")) {
            parameters += "[name: " + symbol.name + ", type: " + symbol.type + ", index: " + parametersLen + "]";
            parametersLen++;
        }
        return super.add(symbol);
    }

    @Override
    public void print() {
        System.out.println("------------------------------- " + name + " : " + line + " -----------------------------");
        for (ISymbol scope : scopes) scope.print();
        System.out.println("=========================================================================================");
    }
}
