package compiler.table;

public class GlobalScope extends IScope {
    public GlobalScope(String name, int line) {
        super(name, line);
    }

    @Override
    public void print() {
        for (IScope scope : scopes) scope.print();
    }
}
