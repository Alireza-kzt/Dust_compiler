package compiler.table;

import java.util.Stack;

public abstract class IScope implements ISymbol {
    public Stack<IScope> scopes;
    String name;
    int line;

    public IScope(String name, int line) {
        this.name = name;
        this.line = line;
        this.scopes = new Stack();
    }

    public void add(IScope scope) {
        scopes.add(scope);
    }

    public IScope pop() {
        return scopes.pop();
    }


    @Override
    public abstract void print();
}

