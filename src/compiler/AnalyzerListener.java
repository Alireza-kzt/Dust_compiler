package compiler;

import compiler.table.*;
import compiler.table.MethodScope;
import gen.DustListener;
import gen.DustParser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.Objects;


public class AnalyzerListener implements DustListener {
    IScope scope;
    Boolean isConstructor = false;

    public AnalyzerListener(GlobalScope globalScope) {
        this.scope = globalScope;
    }

    @Override
    public void enterProgram(DustParser.ProgramContext ctx) {
        scope.line = ctx.start.getLine();
    }

    @Override
    public void exitProgram(DustParser.ProgramContext ctx) {

    }

    @Override
    public void enterImportclass(DustParser.ImportclassContext ctx) {

    }

    @Override
    public void exitImportclass(DustParser.ImportclassContext ctx) {

    }

    @Override
    public void enterClassDef(DustParser.ClassDefContext ctx) {
        String className = ctx.CLASSNAME(0).getText();
        int classLine = ctx.start.getLine();
        String context = ctx.getText();

        int indexOfClassName = context.indexOf(className);
        int index = indexOfClassName + className.length();

        while (context.charAt(index) != ')') {
            index++;
        }

        String extended = context.substring(indexOfClassName + className.length() + 1, index);

        scope = scope.add(new ClassScope(className, classLine, extended));
    }


    @Override
    public void exitClassDef(DustParser.ClassDefContext ctx) {
        scope = scope.parent;
    }

    @Override
    public void enterClass_body(DustParser.Class_bodyContext ctx) {

    }

    @Override
    public void exitClass_body(DustParser.Class_bodyContext ctx) {

    }

    @Override
    public void enterVarDec(DustParser.VarDecContext ctx) {
        if (!isConstructor) {
            String fieldName = ctx.ID().getText();
            String fieldType = ctx.TYPE() != null ? ctx.TYPE().getText() : ctx.CLASSNAME().getText();
            Symbol symbol = new Symbol("Field", fieldName, fieldType);

            // Add the symbol to the current scope
            scope.add(symbol);
        }
    }

    @Override
    public void exitVarDec(DustParser.VarDecContext ctx) {

    }

    @Override
    public void enterArrayDec(DustParser.ArrayDecContext ctx) {
        String varName = ctx.ID().getText();
        String fieldType = ctx.TYPE() != null ? ctx.TYPE().getText() : ctx.CLASSNAME().getText();
        Symbol symbol = new Symbol("Field", varName, fieldType);

        // Add the symbol to the current scope
        scope.add(symbol);
    }

    @Override
    public void exitArrayDec(DustParser.ArrayDecContext ctx) {
    }

    @Override
    public void enterMethodDec(DustParser.MethodDecContext ctx) {
        String methodName = ctx.ID().getText();
        int line = ctx.start.getLine();
        String returnType = ctx.TYPE() != null ? ctx.TYPE().getText() : "void";

        scope = scope.add(new MethodScope(methodName, line, returnType));

        for (DustParser.ParameterContext x : ctx.parameter()) {
            for (DustParser.VarDecContext y : x.varDec()) {
                String fieldName = y.ID().getText();
                String fieldType = y.TYPE() != null ? y.TYPE().getText() : y.CLASSNAME().getText();
                scope.add(new Symbol("Param", fieldName, fieldType));
            }
        }
    }

    @Override
    public void exitMethodDec(DustParser.MethodDecContext ctx) {
        scope = scope.parent;
    }

    @Override
    public void enterConstructor(DustParser.ConstructorContext ctx) {
        isConstructor = true;
        String methodName = ctx.CLASSNAME().getText();
        int classLine = ctx.start.getLine();

        scope = scope.add(new MethodScope("Constructor_" + methodName, classLine, methodName));

        for (DustParser.ParameterContext x : ctx.parameter()) {
            for (DustParser.VarDecContext y : x.varDec()) {
                String fieldName = y.ID().getText();
                String fieldType = y.TYPE() != null ? y.TYPE().getText() : y.CLASSNAME().getText();
                scope.add(new Symbol("Param", fieldName, fieldType));
            }
        }
    }

    @Override
    public void exitConstructor(DustParser.ConstructorContext ctx) {
        isConstructor = false;
        scope = scope.parent;
    }

    @Override
    public void enterParameter(DustParser.ParameterContext ctx) {

    }

    @Override
    public void exitParameter(DustParser.ParameterContext ctx) {

    }

    @Override
    public void enterStatement(DustParser.StatementContext ctx) {

    }

    @Override
    public void exitStatement(DustParser.StatementContext ctx) {

    }

    @Override
    public void enterReturn_statment(DustParser.Return_statmentContext ctx) {

    }

    @Override
    public void exitReturn_statment(DustParser.Return_statmentContext ctx) {

    }

    @Override
    public void enterCondition_list(DustParser.Condition_listContext ctx) {

    }

    @Override
    public void exitCondition_list(DustParser.Condition_listContext ctx) {

    }

    @Override
    public void enterCondition(DustParser.ConditionContext ctx) {

    }

    @Override
    public void exitCondition(DustParser.ConditionContext ctx) {

    }

    @Override
    public void enterIf_statment(DustParser.If_statmentContext ctx) {
        int line = ctx.start.getLine();

        scope = scope.add(new BlockScope(Keywords.__if__, line));


    }

    @Override
    public void exitIf_statment(DustParser.If_statmentContext ctx) {
        scope = scope.parent;
    }

    @Override
    public void enterWhile_statment(DustParser.While_statmentContext ctx) {
        int line = ctx.start.getLine();

        scope = scope.add(new BlockScope(Keywords.__while__, line));
    }

    @Override
    public void exitWhile_statment(DustParser.While_statmentContext ctx) {
        scope = scope.parent;
    }

    @Override
    public void enterIf_else_statment(DustParser.If_else_statmentContext ctx) {
        int line = ctx.start.getLine();

        scope = scope.add(new BlockScope(Keywords.__elif__, line));
    }

    @Override
    public void exitIf_else_statment(DustParser.If_else_statmentContext ctx) {
        scope = scope.parent;
    }

    @Override
    public void enterPrint_statment(DustParser.Print_statmentContext ctx) {

    }

    @Override
    public void exitPrint_statment(DustParser.Print_statmentContext ctx) {

    }

    @Override
    public void enterFor_statment(DustParser.For_statmentContext ctx) {
        int line = ctx.start.getLine();

        scope = scope.add(new BlockScope(Keywords.__for__, line));
    }

    @Override
    public void exitFor_statment(DustParser.For_statmentContext ctx) {
        scope = scope.parent;
    }

    @Override
    public void enterMethod_call(DustParser.Method_callContext ctx) {

    }

    @Override
    public void exitMethod_call(DustParser.Method_callContext ctx) {

    }

    @Override
    public void enterAssignment(DustParser.AssignmentContext ctx) {
        scope.assignment(ctx.getText());
    }

    @Override
    public void exitAssignment(DustParser.AssignmentContext ctx) {

    }

    @Override
    public void enterExp(DustParser.ExpContext ctx) {

    }

    @Override
    public void exitExp(DustParser.ExpContext ctx) {

    }

    @Override
    public void enterPrefixexp(DustParser.PrefixexpContext ctx) {

    }

    @Override
    public void exitPrefixexp(DustParser.PrefixexpContext ctx) {

    }

    @Override
    public void enterArgs(DustParser.ArgsContext ctx) {

    }

    @Override
    public void exitArgs(DustParser.ArgsContext ctx) {

    }

    @Override
    public void enterExplist(DustParser.ExplistContext ctx) {

    }

    @Override
    public void exitExplist(DustParser.ExplistContext ctx) {

    }

    @Override
    public void enterArithmetic_operator(DustParser.Arithmetic_operatorContext ctx) {

    }

    @Override
    public void exitArithmetic_operator(DustParser.Arithmetic_operatorContext ctx) {

    }

    @Override
    public void enterRelational_operators(DustParser.Relational_operatorsContext ctx) {

    }

    @Override
    public void exitRelational_operators(DustParser.Relational_operatorsContext ctx) {

    }

    @Override
    public void enterAssignment_operators(DustParser.Assignment_operatorsContext ctx) {

    }

    @Override
    public void exitAssignment_operators(DustParser.Assignment_operatorsContext ctx) {

    }

    @Override
    public void visitTerminal(TerminalNode terminalNode) {

    }

    @Override
    public void visitErrorNode(ErrorNode errorNode) {

    }

    @Override
    public void enterEveryRule(ParserRuleContext parserRuleContext) {

    }

    @Override
    public void exitEveryRule(ParserRuleContext parserRuleContext) {

    }
}
