package compiler;

import compiler.table.*;
import gen.DustParser;

import java.util.ArrayList;

public class CodeAnalysis extends AnalyzerListener {
    public ArrayList<Error> errors = new ArrayList<>();

    public CodeAnalysis(GlobalScope globalScope) {
        super(globalScope);
    }

    public void printErrors() {
        if (errors.isEmpty()) {
            System.out.println("Code Executed with 0 error.");
        } else {
            for (Error error : errors) {
                System.out.println(error);
            }
        }
    }

    @Override
    public void enterImportclass(DustParser.ImportclassContext ctx) {
        String className = ctx.CLASSNAME().getText();
        int classLine = ctx.start.getLine();
        boolean isClassDef = false;

        for (ISymbol s : scope.scopes) {
            if (s instanceof ClassScope) {
                if (((ClassScope) s).name.equals(className)) {
                    isClassDef = true;
                    break;
                }

            }
        }

        if (!isClassDef) {
            errors.add(new Error("Class not defined", "Class " + className + " not defined", classLine));
        }

        super.enterImportclass(ctx);
    }

    @Override
    public void enterVarDec(DustParser.VarDecContext ctx) {
        String fieldName = ctx.ID().getText();
        String fieldType = ctx.TYPE() != null ? ctx.TYPE().getText() : ctx.CLASSNAME().getText();
        int fieldLine = ctx.start.getLine();
        Symbol symbol = new Symbol("Field", fieldName, fieldType);
        boolean isVarDef = false;
        IScope tempScope = scope;

        while (!(tempScope instanceof GlobalScope)) {
            for (var s : tempScope.scopes) {
                if (s instanceof Symbol) {
                    if (((Symbol) s).getValue().equals(symbol.getValue())) {
                        isVarDef = true;
                        break;
                    }
                }
            }
            tempScope = tempScope.parent;
        }

        if (isVarDef) {
            errors.add(new Error("Duplicated Defined", "var " + fieldName + " is defined", fieldLine));
        } else {
            super.enterVarDec(ctx);
        }
    }
}
