package compiler.table;

import java.util.List;
import java.util.Stack;

public abstract class IScope extends ISymbol {
    public Stack<ISymbol> scopes;
    public String name;
    public int line;

    public Symbol assignment(String name) {
        for (var scope : scopes) {
            if (scope instanceof Symbol) {
                if (((Symbol) scope).name == name) {
                    ((Symbol) scope).is_defined = true;
                }
            }
        }

        return null;
    }

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

    public Symbol add(Symbol symbol) {
        symbol.parent = this;
        scopes.add(symbol);
        return symbol;
    }

    public ISymbol pop() {
        return scopes.pop();
    }


    @Override
    public abstract void print();
}

