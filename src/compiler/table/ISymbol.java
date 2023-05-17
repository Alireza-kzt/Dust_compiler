package compiler.table;

public abstract class ISymbol {
    public IScope parent;
    public abstract void print();
}
