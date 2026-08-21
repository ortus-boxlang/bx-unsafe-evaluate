package ortus.boxlang.modules.evaluate;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ortus.boxlang.runtime.BoxRuntime;
import ortus.boxlang.runtime.context.IBoxContext;
import ortus.boxlang.runtime.context.ScriptingRequestBoxContext;
import ortus.boxlang.runtime.scopes.IScope;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.scopes.VariablesScope;
import ortus.boxlang.runtime.types.util.MathUtil;

@Disable( "the test require 1.17 to pass, but the implementation doesn't care.  Disabling so it doesn't block out stable release.  RE-enable after 1.17 releases." )
public class PrecisionEvaluateTest {

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

	@DisplayName( "It can evaluate multiple expressions with precisionEvaluate" )
	@Test
	public void testPrecisionEvaluate() {
		instance.executeSource(
		    """
		    result = precisionEvaluate( "first = 1", "first + 1" );
		    """,
		    context
		);

		assertEquals( 2, variables.get( result ) );
	}

	@DisplayName( "Returns a BigDecimal" )
	@Test
	public void testPrecisionEvaluatesToBigDecimal() {
		instance.executeSource(
		    """
		    result = precisionEvaluate( "1/3 + 5" );
		    """,
		    context );
		assertThat( variables.get( result ) ).isInstanceOf( BigDecimal.class );

		instance.executeSource(
		    """
		    result = precisionEvaluate( "1/(7*12)" );
		    """,
		    context );
		assertThat( variables.get( result ) ).isInstanceOf( BigDecimal.class );

		instance.executeSource(
		    """
		    result = precisionEvaluate( "3 * 5" );
		    """,
		    context );
		assertThat( variables.get( result ) ).isInstanceOf( BigDecimal.class );
	}

	@DisplayName( "It accepts only valid math or it throws" )
	@Test
	public void testItAcceptsOnlyValidMath() {
		assertThrows( Throwable.class, () -> instance.executeSource(
		    """
		    result = precisionEvaluate( "a + b" );
		    """,
		    context )
		);

		assertThrows( Throwable.class, () -> instance.executeSource(
		    """
		    result = precisionEvaluate( "<cfexecute>" );
		    """,
		    context )
		);

		instance.executeSource(
		    """
		    result = precisionEvaluate( "9 MOD 2" );
		    """,
		    context );
		assertThat( variables.get( result ) ).isInstanceOf( BigDecimal.class );

		instance.executeSource(
		    """
		    result = precisionEvaluate( "((1 + 2) * 3) - 4 / (2 ^ 2)" );
		    """,
		    context );
		assertThat( variables.get( result ) ).isInstanceOf( BigDecimal.class );

		instance.executeSource(
		    """
		    result = precisionEvaluate( "2 * (3 + (4 / 2))" );
		    """,
		    context );
		assertThat( variables.get( result ) ).isInstanceOf( BigDecimal.class );

		instance.executeSource(
		    """
		    result = precisionEvaluate( "1E-05" );
		    """,
		    context );
		assertThat( variables.get( result ) ).isInstanceOf( BigDecimal.class );
		assertEquals( variables.get( result ), new BigDecimal( "0.00001", MathUtil.getMathContext() ) );
	}

}