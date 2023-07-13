package compiler;

import compiler.table.*;
import gen.DustParser;

import java.util.*;

public class CodeAnalysis extends AnalyzerListener {
    public ArrayList<Error> errors = new ArrayList<>();
    public ArrayList<DustParser.Method_callContext> methodCalls = new ArrayList<>();

    public Set<String> ArrayNames = new HashSet<>();
    public ArrayList<Integer> ArrayOutOfRangeErrorLine = new ArrayList<>();

    public CodeAnalysis(GlobalScope globalScope) {
        super(globalScope);
    }

    public void printErrors() {
        find_constructor_matching_error();

        for (var call : methodCalls) check_method_call_has_error(call);


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
        methodCalls.add(ctx);
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

    public void check_method_call_has_error(DustParser.Method_callContext ctx) {
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
                for (ISymbol param : methodScope.scopes) {
                    for (var arg : args) {
                        if (param instanceof Symbol) {
                            if (!((Symbol) param).is_defined.equals("False")) {
                                if (!typeOf(arg).isEmpty()) {
                                    if (((Symbol) param).type.equals(typeOf(arg))) {
                                        isAllParamMatch = false;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
                if (!isAllParamMatch) {
                    errors.add(new Error("Type of parameter doesnt match", "Method " + methodName, line));
                }
            }
        }
    }

    private String typeOf(String arg) {
        String type = "";

        if (arg.contains("\"") || arg.contains("\'")) {
            type = "string";
        } else {

            try {
                Integer.parseInt(arg);
                type = "int";
            } catch (Exception e) {
                type = "";
            }

            if (arg.contains(".")) {
                try {
                    Float.parseFloat(arg);
                    type = "float";
                } catch (Exception e) {
                    type = "";
                }
            }

            if (arg.equals("true") || arg.equals("false")) {
                type = "bool";
            }


        }

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

    public String getSize(String text) {
        return text.substring(text.indexOf(',')+1);
    }

    public String getName(String text) {
        return text.substring(0, text.indexOf(','));
    }

    public String getNameAndIndex(String ctxToString) {
        String name;
        if (ctxToString.contains(".")) {
            name = ctxToString.substring(ctxToString.indexOf(".")+1, ctxToString.indexOf('['));
        }
        else {
            name = ctxToString.substring(0, ctxToString.indexOf("["));
        }
        String size = ctxToString.substring(ctxToString.indexOf("[")+1, ctxToString.indexOf("]"));
        return name+','+size;
    }

    @Override
    public void enterAssignment(DustParser.AssignmentContext ctx) {
        IScope tempScope = scope;
//        Set<String> names = new HashSet<>();
        HashMap<String, Integer> arrNameSizeDict= new HashMap<>();

        while (!(tempScope instanceof GlobalScope)) {
            for (var s : tempScope.scopes) {
//                ((Symbol) s).field.replace(",Field", "");
                if (s instanceof Symbol && ((Symbol) s).field.contains("Array")) {
                    String sizeOfArray = getSize(((Symbol) s).field.replace(",Field", ""));
                    String nameOfArray = ((Symbol) s).name;
                    System.out.println(sizeOfArray + " " + nameOfArray);
//                    if (!ArrayNames.contains(nameOfArray)) {
//                        ArrayNames.add(nameOfArray);
//                        arrNameSizeDict.put(nameOfArray, Integer.parseInt(sizeOfArray));
//                    }
//                    if (ctx.getText().contains("[")) {
//                        String name = getName(getNameAndIndex(ctx.getText()));
//                        String index = getSize(getNameAndIndex(ctx.getText()));
//                        System.out.println(name + " " +
//                                            index + " | " + sizeOfArray + " " +
//                                            ctx.start.getLine() + " " + (Integer.parseInt(index) < Integer.parseInt(sizeOfArray)));
//                    }
                }
            }
            tempScope = tempScope.parent;
        }
//        for (String key : arrNameSizeDict.keySet()) {
//            int value = arrNameSizeDict.get(key);
//            System.out.println(key + " : " + value);
//        }
    }
}