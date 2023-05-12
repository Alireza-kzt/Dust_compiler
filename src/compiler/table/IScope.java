package compiler.table;

import java.util.Stack;

public abstract class IScope implements ISymbol {
    public Stack<IScope> scopes;
    String name;
    int line;


    @Override
    public abstract void print();
}

