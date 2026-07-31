package ortus.boxlang.modules.evaluate;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ortus.boxlang.compiler.parser.BoxSourceType;
import ortus.boxlang.runtime.BoxRuntime;
import ortus.boxlang.runtime.context.IBoxContext;
import ortus.boxlang.runtime.context.ScriptingRequestBoxContext;
import ortus.boxlang.runtime.scopes.IScope;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.scopes.VariablesScope;
import ortus.boxlang.runtime.types.Array;

public class EvaluateTest {

	static BoxRuntime	instance;
	IBoxContext			context;
	IScope				variables;
	static Key			result	= new Key( "result" );

	@BeforeAll
	public static void setUp() {
		instance = BoxRuntime.getInstance( true );
	}

	@BeforeEach
	public void setupEach() {
		context		= new ScriptingRequestBoxContext( instance.getRuntimeContext() );
		variables	= context.getScopeNearby( VariablesScope.name );
	}

	@DisplayName( "It can test the evaluate function" )
	@Test
	public void testEvaluate() {
		// @formatter:off
		instance.executeSource(
			"""
			first = "boxlang"
			second = "BoxLang"
			op = "eq"

			result = evaluate( "first #op# second" );
			""",
			context
		);
		// @formatter:on

		assertEquals( true, variables.get( result ) );
	}

	@DisplayName( "Can transpile deserializeJSON()" )
	@Test
	public void testDeserializeJSON() {

		instance.executeSource(
		    """
		    include "/src/test/java/ortus/boxlang/modules/evaluate/includeMe.cfm";
		      """,
		    context, BoxSourceType.CFSCRIPT );
		assertThat( variables.get( result ) ).isInstanceOf( Array.class );
		assertThat( variables.getAsArray( result ).size() ).isEqualTo( 1 );
		assertThat( variables.getAsArray( result ).get( 0 ) ).isEqualTo( 1 );
	}

}
