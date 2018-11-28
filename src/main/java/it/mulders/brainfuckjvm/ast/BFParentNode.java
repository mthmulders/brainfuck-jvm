package it.mulders.brainfuckjvm.ast;

/**
 * Marker interface for those nodes in the AST that have children.
 */
public interface BFParentNode {
    BFCommandNode[] getChildNodes();
}
