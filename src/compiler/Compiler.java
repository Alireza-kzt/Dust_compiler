package compiler;

import gen.DustLexer;
import gen.DustParser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;


import java.io.IOException;
import java.nio.file.Paths;

public class Compiler {
    public static void main(String[] args) throws IOException {
        var path = "C:\\Users\\user\\IdeaProjects\\Dust\\sample\\input1.txt";
        var stream = CharStreams.fromFileName(path);
        var lexer = new DustLexer(stream);
        var tokens = new CommonTokenStream(lexer);
        var parser = new DustParser(tokens);
        parser.setBuildParseTree(true);
        var tree = parser.program();
        var walker = new ParseTreeWalker();
        var listener = new DustListenerImp();
        walker.walk(listener, tree);
    }
}