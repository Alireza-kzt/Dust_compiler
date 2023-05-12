package compiler.table;

public class ClassScope extends IScope {


    public ClassScope(String name, int line) {
        super(name, line);
    }

    @Override
    public void print() {
        System.out.println("------ " + name + " : " + line + " ------");
        for (ISymbol scope : scopes) scope.print();
        System.out.println("------ End of Class ------");
    }
}
