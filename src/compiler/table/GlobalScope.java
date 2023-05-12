package compiler.table;

public class GlobalScope extends IScope {
    public GlobalScope() {
        super("Program", 1);
    }

    @Override
    public void print() {
        System.out.println("------ " + name + " : " + line + " ------");
        for (IScope scope : scopes) scope.print();
        System.out.println("------ End of Project ------");
    }
}
