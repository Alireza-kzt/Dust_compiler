package compiler.table;

public class ClassScope extends IScope {
    public String extended;

    public ClassScope(String name, int line, String extended) {
        super(name, line);
        this.extended = extended;
    }

    @Override
    public void print() {
        System.out.println("------ " + name + " : " + line + " ------");
        for (ISymbol scope : scopes) {
            if (scope instanceof MethodScope) {
                System.out.println("Method_" + ((MethodScope) scope).name + " | Value: Method(name: " + ((MethodScope) scope).name + "), (returnType: " + ((MethodScope) scope).returnType + ")");
            }
        }
        ;
        for (ISymbol scope : scopes) scope.print();
        System.out.println("=========================================================================================");
    }
}
