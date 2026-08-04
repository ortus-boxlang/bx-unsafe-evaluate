package ortus.boxlang.modules.evaluate.bifs;

import java.util.Map;

import ortus.boxlang.compiler.parser.BoxSourceType;
import ortus.boxlang.runtime.bifs.BIF;
import ortus.boxlang.runtime.bifs.BoxBIF;
import ortus.boxlang.runtime.context.IBoxContext;
import ortus.boxlang.runtime.scopes.ArgumentsScope;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.types.Argument;

@BoxBIF( description = "Evaluate expressions using BigDecimal precision arithmetic" )
public class PrecisionEvaluate extends BIF {

	/**
	 * Constructor
	 */
	public PrecisionEvaluate() {
		super();
		declaredArguments = new Argument[] {
		    new Argument( true, Argument.STRING, Key.expression )
		};
	}

	/**
	 * Evaluates one or more string expressions dynamically from left to right using BigDecimal precision arithmetic.
	 * Note, this is provided for compat. It acutally works the same as evaluate() and will use the same high precision
	 * math setting that Boxlang is configured with. BoxLang will always use high precision math by default.
	 *
	 * @param context   The context in which the BIF is being invoked.
	 * @param arguments Argument scope for the BIF.
	 *
	 * @argument.expression Expression to evaluate. String expressions can be complex.
	 *
	 * @return Returns the result of evaluating the rightmost expression.
	 */
	public Object _invoke( IBoxContext context, ArgumentsScope arguments ) {
		BoxSourceType	sourceType	= Evaluate.getSourceType( context );
		Object			result		= null;
		for ( Map.Entry<Key, Object> argument : arguments.entrySet() ) {
			if ( argument.getKey().equals( BIF.__isMemberExecution ) || argument.getKey().equals( BIF.__functionName ) ) {
				continue;
			}
			result = runtime.executeStatement( argument.getValue().toString(), context, sourceType );
		}
		return result;
	}

}