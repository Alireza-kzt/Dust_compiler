package compiler;

import compiler.table.ClassScope;
import compiler.table.GlobalScope;
import compiler.table.ISymbol;
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

        if(!isClassDef){
            errors.add(new Error("Class not defined", "Class " + className + " not defined", classLine));
        }

        super.enterImportclass(ctx);
    }


}
