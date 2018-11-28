package it.mulders.brainfuckjvm.parser;

import it.mulders.brainfuckjvm.ast.BFCommandNode;
import it.mulders.brainfuckjvm.ast.BFParentNode;
import it.mulders.brainfuckjvm.ast.BFRootNode;
import lombok.extern.slf4j.Slf4j;

import java.io.*;

import static java.lang.String.format;

@Slf4j
public class BFVisualizer {
    private static final String BG_COLOR = "white";
    private static final String FG_COLOR = "black";
    private static final Double INTERNODE_DISTANCE = 1.0;

    public void dumpTree(final String sourceFileName, final String outputFileName, final BFRootNode rootNode) {
        final ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        try (final PrintWriter output = new PrintWriter(bytes)) {
            output.println(format("digraph %s {", outputFileName));
            output.println(format("\tgraph [ bgcolor=%s, nodesep=%f, ranksep=%f ]", BG_COLOR, INTERNODE_DISTANCE, INTERNODE_DISTANCE));
            output.println(format("\tnode [ fontname=\"Fira Code, serif\", style=rounded, shape=box, fontcolor=%s, color=%s ]", FG_COLOR, FG_COLOR));
            output.println(String.format("\t%s [ label = \"%s\" ]", rootNode.hashCode(), rootNode.toString()));
            dumpTree(output, rootNode);
            output.println("}");
        }

        try (final OutputStream output = new FileOutputStream(new File(outputFileName + ".dot"))) {
            output.write(bytes.toByteArray());
        } catch (IOException ioe) {
            log.error("Could not dump Brainfuck AST", ioe);
        }
    }

    private void dumpTree(final PrintWriter output, final BFParentNode parent) {
        log.trace("Dumping AST for parent node {} ({})", parent.toString(), parent.hashCode());
        for (BFCommandNode child : parent.getChildNodes()) {
            if (child == parent) {
                log.trace("   Not adding child node since it terminates the JUMP");
                return;
            }

            log.trace("   Adding child node {} ({})", child.toString(), child.hashCode());
            output.println(String.format("\t%s [ label = \"%s\" ]", child.hashCode(), child.toString()));
            output.println(String.format("\t%s -> %s", parent.hashCode(), child.hashCode()));

            if (child instanceof BFParentNode) {
                dumpTree(output, (BFParentNode) child);
            }
        }
    }
}
