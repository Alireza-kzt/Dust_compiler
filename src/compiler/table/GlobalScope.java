package compiler.table;

public class GlobalScope extends IScope {
    public GlobalScope() {
        super("Program", 0);
    }

    @Override
    public void print() {
        System.out.println("------ " + name + " : " + line + " ------");
        for (ISymbol scope : scopes) {
            if(scope instanceof ClassScope) {
                System.out.println("Class_" + ((ClassScope) scope).name + " | Value: Class(name: " + ((ClassScope) scope).name + "), (parents: " + ((ClassScope) scope).extended + ")");
            }
        };
        System.out.println("=========================================================================================");
        for (ISymbol scope : scopes) scope.print();
        System.out.println("------ End of Project ------");
    }
}
