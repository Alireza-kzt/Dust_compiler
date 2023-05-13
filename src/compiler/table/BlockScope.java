package compiler.table;

public class BlockScope extends IScope {
    public BlockScope(String name, int line) {
        super(name, line);
    }

    @Override
    public void print() {
        System.out.println("------ " + name + " : " + line + " ------");
        for (ISymbol scope : scopes) scope.print();
    }
}
