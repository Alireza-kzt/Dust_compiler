package compiler.table;

public class ClassScope extends IScope {
    final String extended;

    public ClassScope(String name, int line, String extended) {
        super(name, line);
        this.extended = extended;
    }

    @Override
    public void print() {
        System.out.println("Class_" + name + "Value : Class (name: " + name + ") (Parent: " + extended + ")");
        System.out.println("=========================================================================================");
        System.out.println("------ " + name + " : " + line + " ------");
        for (ISymbol scope : scopes) scope.print();
        System.out.println("=========================================================================================");
    }
}
