package compiler.table;

public class MethodScope extends IScope {
    public MethodScope(String name, int line) {
        super(name, line);
    }

    @Override
    public void print() {
        System.out.println("Method_" + name);
        for (ISymbol scope : scopes) scope.print();
    }
}
