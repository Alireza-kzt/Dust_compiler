package compiler;

import compiler.table.*;
import gen.DustParser;

import java.util.*;

public class CodeAnalysis extends AnalyzerListener {
    public ArrayList<Error> errors = new ArrayList<>();
    public HashMap<DustParser.Method_callContext, IScope> methodCalls = new HashMap();

    public CodeAnalysis(GlobalScope globalScope) {
        super(globalScope);
    }

    public void printErrors() {
        find_constructor_matching_error();

        for (var call : methodCalls.keySet()) check_method_call_has_error(call, methodCalls.get(call));


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

        for (var s : scope.scopes) {
            if (s instanceof Symbol) {
                if (((Symbol) s).getSignature().contains(symbol.getSignature())) {
                    isVarDef = true;
                    break;
                }
            }
        }

        if (isVarDef) {
            errors.add(new Error("Duplicated Defined", "var " + fieldName + " is defined", fieldLine));
        } else {
            super.enterVarDec(ctx);
        }
    }

    @Override
    public void enterMethod_call(DustParser.Method_callContext ctx) {
        methodCalls.put(ctx, scope);
        super.enterMethod_call(ctx);
    }

    public MethodScope find_method_by_name(String methodName) {
        for (var classScope : scope.scopes) {
            if (classScope instanceof ClassScope) {
                for (var m : ((ClassScope) classScope).scopes) {
                    if (m instanceof MethodScope) {
                        if (((MethodScope) m).name.equals(methodName)) {
                            return ((MethodScope) m);
                        }
                    }
                }
            }
        }

        return null;
    }

    public void check_method_call_has_error(DustParser.Method_callContext ctx, IScope currentScope) {
        String methodName = ctx.ID().getText();
        int line = ctx.start.getLine();
        MethodScope methodScope = find_method_by_name(methodName);

        if (methodScope == null) {
            errors.add(new Error("Method not Found", "Method " + methodName + " not found", line));
        } else {
            String argList = ctx.args().getText();
            ArrayList<String> args = new ArrayList(Arrays.asList(argList.replaceAll("[()]", "").split(",")));

            if (methodScope.parametersLen != args.size()) {
                errors.add(new Error("Number of parameter doesnt match", "Method " + methodName, line));
            } else {
                boolean isAllParamMatch = true;
                for (int i = 0; i < methodScope.parametersLen; i++) {
                    IScope tempScope = currentScope;
                    Symbol relatedSymbol = null;
                    boolean symbolFound = false;

                    tempScope = currentScope;
                    while (!(tempScope instanceof GlobalScope)) {
                        for (var s : tempScope.scopes) {
                            if (s instanceof Symbol) {
                                if (((Symbol) s).name.equals(args.get(i))) {
                                    relatedSymbol = (Symbol) s;
                                    symbolFound = true;
                                    break;
                                }
                            }
                        }
                        if(symbolFound) break;
                        tempScope = tempScope.parent;
                    }

                    if (relatedSymbol == null) {
                        errors.add(new Error("Element not Found", "Element " + args.get(i), line));
                        break;
                    } else {
                        if (!((Symbol) methodScope.scopes.get(i)).type.equals(relatedSymbol.type)) {
                            isAllParamMatch = false;
                            break;
                        }
                    }
                }

                if (!isAllParamMatch) {
                    errors.add(new Error("Type or sequence of parameter doesnt match", "Method " + methodName, line));
                }
            }
        }
    }

    public void find_constructor_matching_error() {
        for (ISymbol classScope : scope.scopes)
            if (classScope instanceof ClassScope) {
                String className = ((ClassScope) classScope).name;
                for (ISymbol methosScope : ((ClassScope) classScope).scopes) {
                    if (methosScope instanceof MethodScope) {
                        String methodScopeName = ((MethodScope) methosScope).name;
                        if (methodScopeName.toLowerCase().contains("constructor")) {
                            if (!className.equals(methodScopeName.replace("Constructor_", ""))) {
                                errors.add(new Error("Constructor not match", "Name of constructor method " + methodScopeName + " does not match with class " + className, ((MethodScope) methosScope).line));
                            }
                        }
                    }
                }
            }
    }

    @Override
    public void enterAssignment(DustParser.AssignmentContext ctx) {
        if (ctx.getText().contains("[")) {
            IScope tempScope = scope;
            Symbol relatedArray = null;
            boolean arrayFound = false;

            while (!(tempScope instanceof GlobalScope)) {
                for (var s : tempScope.scopes) {
                    if (s instanceof Symbol && ((Symbol) s).field.contains("Array")) {
                        if (((Symbol) s).name.equals(getNameAndIndex(ctx.getText()))) {
                            relatedArray = (Symbol) s;
                            arrayFound = true;
                            break;
                        }
                    }
                }
                if (arrayFound) break;
                tempScope = tempScope.parent;
            }

            if (relatedArray == null) {
                errors.add(new Error("Array not Found", "Array " + getNameAndIndex(ctx.getText()), ctx.start.getLine()));
            } else {

                int arrSize = Integer.parseInt(getSize(((Symbol) relatedArray).field.replace(",Field", "")));
                int ctxArraySize = Integer.parseInt(getSizebyCtx(ctx.getText()));

                if (ctxArraySize > arrSize) {
                    errors.add(new Error("Out of Range", "The element " + ctxArraySize + " not exist in array " + relatedArray.name, ctx.start.getLine()));
                }
            }
        } else {
                System.out.println(ctx.getText());
                IScope tempScope = scope;
                Symbol relatedSymbol = null;
                boolean symbolFound = false;

                while (!(tempScope instanceof GlobalScope)) {
                    for (var s : tempScope.scopes) {
                        if (s instanceof Symbol) {
                            var name = ctx.getText().trim().split("[,\\+ ]");
                            if (((Symbol) s).name.equals(name)) {
                                relatedSymbol = (Symbol) s;
                                symbolFound = true;
                                break;
                            }
                        }
                    }
                    if (symbolFound) break;
                    tempScope = tempScope.parent;
                }

//            if(relatedSymbol == null) {
//                errors.add(new Error("Element not Found", "Element " + args.get(i), line));
//            }
            }


        super.enterAssignment(ctx);
    }

    public String getSize(String text) {
        return text.substring(text.indexOf(',') + 1);
    }

    public String getName(String text) {
        return text.substring(0, text.indexOf(','));
    }

    public String getNameAndIndex(String ctxToString) {
        String name;
        if (ctxToString.contains(".")) {
            name = ctxToString.substring(ctxToString.indexOf(".") + 1, ctxToString.indexOf('['));
        } else {
            name = ctxToString.substring(0, ctxToString.indexOf("["));
        }
        return name;
    }

    public String getSizebyCtx(String ctxToString) {
        return ctxToString.substring(ctxToString.indexOf("[") + 1, ctxToString.indexOf("]"));
    }


}