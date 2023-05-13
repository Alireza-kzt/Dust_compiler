package compiler.table;

public class MethodScope extends IScope {
    String returnType;

    public MethodScope(String name, int line, String returnType) {
        super(name, line);
        this.returnType = returnType;
    }

    @Override
    public void print() {
        System.out.println("Method_" + name + "Value : Method (name: " + name + ") (return type : " + returnType + ")");
        for (ISymbol scope : scopes) scope.print();
    }
}
