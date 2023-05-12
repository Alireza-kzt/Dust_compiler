package compiler.table;

public class GlobalScope extends IScope {
    public GlobalScope() {
        super("Program", 0);
    }

    @Override
    public void print() {
        System.out.println("------ " + name + " : " + line + " ------");
        for (ISymbol scope : scopes) scope.print();
        System.out.println("------ End of Project ------");
    }
}
