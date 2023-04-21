package compiler;

import gen.DustListener;
import gen.DustParser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.ArrayList;

// Total = 19
// 9 -> Alireza
// 10 -> Mohammad

public class DustListenerImp implements DustListener {
    private int indentLevel = 0;
    private boolean inConstructor = false;
    private String indentSpace() {
        String tab = " ".repeat(4);
        return tab.repeat(indentLevel);
    }

    @Override
    public void enterProgram(DustParser.ProgramContext ctx) {
        System.out.println("program start{");
        indentLevel++;
    }

    @Override
    public void exitProgram(DustParser.ProgramContext ctx) {
        indentLevel--;
        System.out.println("}");
    }

    @Override
    public void enterImportclass(DustParser.ImportclassContext ctx) {
        String className = ctx.CLASSNAME().getText();
        System.out.printf("%simport class: %s\n", indentSpace(), className);
    }

    @Override
    public void exitImportclass(DustParser.ImportclassContext ctx) {

    }

    @Override
    public void enterClassDef(DustParser.ClassDefContext ctx) {
        // To Do
    }

    @Override
    public void exitClassDef(DustParser.ClassDefContext ctx) {
        // To Do
    }

    @Override
    public void enterClass_body(DustParser.Class_bodyContext ctx) {

    }

    @Override
    public void exitClass_body(DustParser.Class_bodyContext ctx) {

    }

    @Override
    public void enterVarDec(DustParser.VarDecContext ctx) {
        // To Do
    }

    @Override
    public void exitVarDec(DustParser.VarDecContext ctx) {

    }

    @Override
    public void enterArrayDec(DustParser.ArrayDecContext ctx) {
        // To Do
    }

    @Override
    public void exitArrayDec(DustParser.ArrayDecContext ctx) {

    }

    @Override
    public void enterMethodDec(DustParser.MethodDecContext ctx) {
        // To Do
    }

    @Override
    public void exitMethodDec(DustParser.MethodDecContext ctx) {
        // To Do
    }

    @Override
    public void enterConstructor(DustParser.ConstructorContext ctx) {
        inConstructor = true;
        ArrayList<String> listOfParams = new ArrayList<>();

        for (DustParser.ParameterContext param: ctx.parameter()) {
            for (DustParser.VarDecContext var: param.varDec()) {
                String name = "";

                if (var.CLASSNAME() == null)
                    name = var.TYPE().toString();
                else
                    name = var.CLASSNAME().toString();

                if (!name.equals("")
                        && var.ID() != null)
                {
                    String parameter = name + " " + var.ID().toString();
                    listOfParams.add(parameter);
                }
            }
        }

        String listOfParams_str = String.join(", ", listOfParams);
        System.out.print(indentSpace() + "class constructor: " + ctx.CLASSNAME().getText() + "{\n");

        indentLevel += 1;
        System.out.print(indentSpace() + "parameter list: [" + listOfParams_str + "]\n");
    }

    @Override
    public void exitConstructor(DustParser.ConstructorContext ctx) {
        // To Do
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
        // To Do
    }

    @Override
    public void exitIf_statment(DustParser.If_statmentContext ctx) {
        // To Do
    }

    @Override
    public void enterWhile_statment(DustParser.While_statmentContext ctx) {
        // To Do 
        // اسم تابع تغییر داده شود
    }

    @Override
    public void exitWhile_statment(DustParser.While_statmentContext ctx) {
        // To Do
    }

    @Override
    public void enterIf_else_statment(DustParser.If_else_statmentContext ctx) {

    }

    @Override
    public void exitIf_else_statment(DustParser.If_else_statmentContext ctx) {

    }

    @Override
    public void enterPrint_statment(DustParser.Print_statmentContext ctx) {

    }

    @Override
    public void exitPrint_statment(DustParser.Print_statmentContext ctx) {

    }

    @Override
    public void enterFor_statment(DustParser.For_statmentContext ctx) {
        // To Do
        // نیاز به بررسی الزامی بودن یا خیر
    }

    @Override
    public void exitFor_statment(DustParser.For_statmentContext ctx) {
        // To Do
    }

    @Override
    public void enterMethod_call(DustParser.Method_callContext ctx) {
        // To Do
    }

    @Override
    public void exitMethod_call(DustParser.Method_callContext ctx) {
        // To Do
    }

    @Override
    public void enterAssignment(DustParser.AssignmentContext ctx) {

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
