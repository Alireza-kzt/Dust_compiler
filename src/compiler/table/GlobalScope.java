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
                Symbol sym = new Symbol("Class", ((ClassScope) scope).name, ((ClassScope) scope).extended);
                sym.print();
            }
        };
        System.out.println("=========================================================================================");
        for (ISymbol scope : scopes) scope.print();
        System.out.println("------ End of Project ------");
    }
}
