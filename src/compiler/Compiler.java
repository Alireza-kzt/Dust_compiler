package compiler;

import compiler.table.GlobalScope;
import gen.DustLexer;
import gen.DustParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;


import java.io.IOException;

public class Compiler {
    public static void main(String[] args) throws IOException {
//        var path = "C:\\Users\\user\\IdeaProjects\\Dust\\sample\\input1.txt";
        var path = "/run/media/mohammad_bq/DE45A6D28900F54E/7th semester/Compiler/phase-2/Dust_compiler/sample/input3.txt";
        var stream = CharStreams.fromFileName(path);
        var lexer = new DustLexer(stream);
        var tokens = new CommonTokenStream(lexer);
        var parser = new DustParser(tokens);
        parser.setBuildParseTree(true);
        var tree = parser.program();
        var walker = new ParseTreeWalker();
        GlobalScope globalScope = new GlobalScope();
        var listener = new AnalyzerListener(globalScope);
        walker.walk(listener, tree);
        globalScope.print();
    }
}