package com.indix.tv;

import java.util.Arrays;
import java.util.Stack;

/**
 * A program to verify if given statement is tautology or not. 
 * @author sanagargoje
 *
 */
public class TautologyVerifier {

	private static final char BASE_CHAR = 'a';
	private static final char CLOSE_BRACKET = ')';
	private static final char OPEN_BRACKET = '(';
	private static final char NOT = '!';
	private static final char OR = '|';
	private static final char AND = '&';
	private static final String TRUE = "True";
	private static final String FALSE = "False";

	static char[] expr;
	static int position;
	static int[] lookupVarSeq = new int[26];
	static int state;
	static int varCount;

	/**
	 * Check of the given variable is set to true for current expression check.
	 * @param Character variable to be check
	 * @return Boolean True if variable is set, else false
	 */
	static boolean isVariableSetTrue(char type) {
		return (state & (1 << lookupVarSeq[type - BASE_CHAR])) != 0;
	}

	/**
	 * Check if given expression is a tautology or not.
	 * 
	 * @param String Expression in infix format
	 * @return String Returns "True" if tautology, else "False"
	 */
	public String checkTautology(String str) {
		expr = this.infix2Prefix(str).toCharArray();
		lookupVarSeq = new int[26];
		Arrays.fill(lookupVarSeq, -1);
		position = 0;
		varCount = 0;
		Node root = parseExpTree();
		// System.out.println("Root type is "+root.type);
		// System.out.println("Root left is "+root.left.type);
		// System.out.println("Root right is "+root.right.type);

		for (state = (1 << varCount) - 1; state >= 0; state--) {
			// System.out.println("Trying combination for given variables:
			// "+state);
			if (!root.isCurrentNodeValueTrue()) {
				return FALSE;
			}
		}
		return TRUE;
	}

	static Node parseExpTree() {
		Node node = new Node();
		node.type = expr[position++];
		switch (node.type) {
		case AND:
		case OR:
			node.left = parseExpTree();
			node.right = parseExpTree();
			break;
		case NOT:
			node.left = parseExpTree();
			break;
		default:
			if (lookupVarSeq[node.type - BASE_CHAR] == -1) {
				lookupVarSeq[node.type - BASE_CHAR] = varCount++;
			}
		}
		return node;
	}

	/**
	 * Convert Infix to Prefix notation form.
	 * 
	 * @param String infix expr
	 * @return String prefix expr
	 */
	public String infix2Prefix(String str) {
		Stack<Character> stack = new Stack<Character>();
		StringBuilder prefix = new StringBuilder();
		for (int i = str.length() - 1; i >= 0; i--) {
			char c = str.charAt(i);
			if (c == ' ') {
				// ignore white spaces
				continue;
			}
			if (Character.isLetter(c)) {
				// If input is letter, append it to output.
				prefix = prefix.append(((Character) c).toString());
			} else if (c == OPEN_BRACKET) {
				// If the input is a open brace, pop elements in stack one by
				// one until we encounter open brace.
				// Discard braces while writing to output buffer.
				while (true) {
					Character tmpc = (Character) stack.pop();
					if (tmpc != CLOSE_BRACKET) {
						prefix = prefix.append(tmpc);
					} else {
						break;
					}
				}
			} else if (c == CLOSE_BRACKET) {
				// If input is closing brace, push it to the stack.
				stack.push(c);
			} else if (c == NOT) {
				// If input is NOT, append it to output.
				prefix = prefix.append(c);
			} else {
				// If the input is Operator, push to stack.
				stack.push(c);
			}
		}
		return prefix.reverse().toString();
	}

	public static void main(String[] args) {
		String infix2 = "(!a | (a & a))";
		String infix3 = "(!a | (b & !a))";
		String infix4 = "(!a | a)";
		String infix5 = "((a & (!b | b)) | (c | !c) | (!a & (!b | b)))";
		TautologyVerifier tv = new TautologyVerifier();
		System.out.println(tv.checkTautology((infix2)));
		System.out.println(tv.checkTautology((infix3)));
		System.out.println(tv.checkTautology((infix4)));
		System.out.println(tv.checkTautology((infix5)));
	}
}

/**
 * Node object, used for expressing tree structure of the given expression.
 * 
 * @author sanagargoje
 */
class Node {
	private static final char NOT = '!';
	private static final char OR = '|';
	private static final char AND = '&';
	char type;
	Node left, right;

	/**
	 * Check boolean value for current node.
	 * @return Boolean True if value for current node (expression) is true, else false
	 */
	boolean isCurrentNodeValueTrue() {
		switch (type) {
		case AND:
			return left.isCurrentNodeValueTrue() && right.isCurrentNodeValueTrue();
		case OR:
			return left.isCurrentNodeValueTrue() || right.isCurrentNodeValueTrue();
		case NOT:
			return !left.isCurrentNodeValueTrue();
		default:
			return TautologyVerifier.isVariableSetTrue(type);
		}
	}
}
