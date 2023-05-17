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
                String methodScopeName = ((MethodScope) scope).name;
                if (methodScopeName.toLowerCase().contains("constructor"))
                    System.out.println("Method_" + methodScopeName + " | Value: Constructor(name: " + methodScopeName + "), (returnType: " + ((MethodScope) scope).returnType + "), (Params: " + ((MethodScope) scope).parameters + ")");
                else
                    System.out.println("Method_" + methodScopeName + " | Value: Method(name: " + methodScopeName + "), (returnType: " + ((MethodScope) scope).returnType + "), (Params: " + ((MethodScope) scope).parameters + ")");
            }
        }
        ;
        for (ISymbol scope : scopes) scope.print();
        System.out.println("=========================================================================================");
    }
}
