package compiler;

import compiler.table.*;
import gen.DustParser;

import java.util.ArrayList;
import java.util.Arrays;

public class CodeAnalysis extends AnalyzerListener {
    public ArrayList<Error> errors = new ArrayList<>();
    public ArrayList<DustParser.Method_callContext> methodCalls = new ArrayList<>();

    public CodeAnalysis(GlobalScope globalScope) {
        super(globalScope);
    }

    public void printErrors() {
        find_constructor_matching_error();

        for (var call : methodCalls) {
            check_method_call_has_error(call);
        }


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

    @Override
    public void enterMethod_call(DustParser.Method_callContext ctx) {
        methodCalls.add(ctx);
        super.enterMethod_call(ctx);
    }

    public void check_method_call_has_error(DustParser.Method_callContext ctx) {
        String methodName = ctx.ID().getText();
        int line = ctx.start.getLine();
        boolean isMethodFind = false;
        MethodScope methodScope = null;


        for (var s : scope.scopes) {
            if (s instanceof ClassScope) {
                for (var m : ((ClassScope) s).scopes) {
                    if (m instanceof MethodScope) {
                        if (((MethodScope) m).name.equals(methodName)) {
                            methodScope = ((MethodScope) m);
                            isMethodFind = true;
                            break;
                        }
                    }
                }
            }

        }

        if (isMethodFind) {
            String argList = ctx.args().getText();
            ArrayList<String> args = new ArrayList<String>(Arrays.asList(argList.replaceAll("[()]", "").split(",")));

            if (methodScope.parametersLen != args.size()) {
                errors.add(new Error("Number of parameter doesnt match", "Method " + methodName, line));
            } else {
                boolean isAllParamMatch = true;
                for (ISymbol param : methodScope.scopes) {
                    for (var arg : args) {
                        if (param instanceof Symbol) {
                            if (((Symbol) param).is_defined.equals("False")) {
                                // TODO
                                isAllParamMatch = false;
                                break;
                            }
                        }
                    }
                }
                if (!isAllParamMatch) {
                    errors.add(new Error("Type of parameter doesnt match", "Method " + methodName, line));
                }
            }

        } else {
            errors.add(new Error("Method not Found", "Method " + methodName + " not found", line));
        }
    }

    public String getDustType(String value) {
        String type = "S";


        return type;
    }

    public void find_constructor_matching_error() {
        for (ISymbol classScope : scope.scopes)
            if (classScope instanceof ClassScope) {
                String className = ((ClassScope) classScope).name;
                for (ISymbol methosScope : ((ClassScope) classScope).scopes) {
                    if (methosScope instanceof MethodScope) {
                        String methodScopeName = ((MethodScope) methosScope).name;
                        if (methodScopeName.toLowerCase().contains("constructor")) {
                            if (!className.equals(methodScopeName.replace("constructor_", ""))) {
                                errors.add(new Error("Constructor not match", "Name of constructor method " + methodScopeName + " does not match with class " + className, ((MethodScope) methosScope).line));
                            }
                        }
                    }
                }
            }
    }
}