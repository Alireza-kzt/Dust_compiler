package compiler.table;

import java.util.Stack;

public abstract class IScope implements ISymbol {
    public Stack<IScope> scopes;
    public String name;
    public int line;

    public IScope(String name, int line) {
        this.name = name;
        this.line = line;
        this.scopes = new Stack();
    }

    public IScope add(IScope scope) {
        scopes.add(scope);
        return scope;
    }

    public IScope pop() {
        return scopes.pop();
    }


    @Override
    public abstract void print();
}

