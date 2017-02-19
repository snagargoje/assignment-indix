package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.indix.tv.TautologyVerifier;

public class TautologyVerifierTest extends TautologyVerifier{

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		System.out.println("setUpBeforeClass");
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		System.out.println("tearDownAfterClass");
	}

	@Before
	public void setUp() throws Exception {
		System.out.println("setUp");
	}

	@After
	public void tearDown() throws Exception {
		System.out.println("tearDown");
	}

	
	@Test
	public final void testIsVariableSetTrue() {
		assertTrue((7 & 4) != 0);
		assertTrue((2 & 4) == 0);
		assertTrue((6 & 4) != 0);
	}

	@Test
	public final void testCheckTautology() {
		assertEquals(checkTautology( "(!a | (a & a))"), "True");
		assertEquals(checkTautology( "(!a | (b & !a))"), "False");
		assertEquals(checkTautology( "(!a | a)"), "True");
		assertEquals(checkTautology( "((a & (!b | b)) | (c | !c) | (!a & (!b | b)))"), "True");
		
		assertEquals(checkTautology( "(a | b)"), "False");
		assertEquals(checkTautology( "( (a | b) & ( a | b) )"), "False");
		assertEquals(checkTautology( "( (a | b) | ( a | b) )"), "False");
		
	}

	@Test
	public final void testInfix2Prefix() {
		assertEquals(infix2Prefix("(!a | (a & a))"), "|!a&aa");
		assertEquals(infix2Prefix("(!a | (b & !a)))"), "|!a&b!a");
		assertEquals(infix2Prefix("(!a | a)"), "|!aa");
		assertEquals(infix2Prefix("((a & (!b | b)) | (!a & (!b | b)))"), "|&a|!bb&!a|!bb");

		assertEquals(infix2Prefix("     (!a | (a & a))"), "|!a&aa");
		assertEquals(infix2Prefix("(!a |      (b & !a)))"), "|!a&b!a");
		assertEquals(infix2Prefix("(!a | a)        "), "|!aa");
		assertEquals(infix2Prefix("((a & (!b | b))        | (!a & (!b | b)))"), "|&a|!bb&!a|!bb");

	}

}
