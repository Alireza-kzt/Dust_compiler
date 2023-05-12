package compiler.table;

import java.util.Stack;

public abstract class IScope extends ISymbol {
    public IScope parent;
    public Stack<ISymbol> scopes;
    public String name;
    public int line;

    public IScope(String name, int line) {
        this.name = name;
        this.line = line;
        this.scopes = new Stack();
    }

    public IScope add(IScope scope) {
        scope.parent = this;
        scopes.add(scope);
        return scope;
    }

    public void add(ISymbol scope) {
        scopes.add(scope);
    }

    public ISymbol pop() {
        return scopes.pop();
    }


    @Override
    public abstract void print();
}

