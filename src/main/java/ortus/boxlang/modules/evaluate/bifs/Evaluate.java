package ortus.boxlang.modules.evaluate.bifs;

import ortus.boxlang.runtime.bifs.BIF;
import ortus.boxlang.runtime.bifs.BoxBIF;
import ortus.boxlang.runtime.context.IBoxContext;
import ortus.boxlang.runtime.scopes.ArgumentsScope;
import ortus.boxlang.runtime.scopes.Key;
import ortus.boxlang.runtime.types.Argument;

@BoxBIF
public class Evaluate extends BIF {

	private static final Key expressionKey = Key.of( "expression" );

	/**
	 * Constructor
	 */
	public Evaluate() {
		super();
		declaredArguments = new Argument[] {
		    new Argument( true, Argument.STRING, expressionKey )
		};
	}

	/**
	 * Evaluates one or more string expressions, dynamically, from left to right.
	 * (The results of an evaluation on the left can have meaning in an expression to the right.)
	 * <p>
	 * If a string expression contains a single- or double-quotation mark, the mark must be escaped.
	 *
	 * @param context   The context in which the BIF is being invoked.
	 * @param arguments Argument scope for the BIF.
	 *
	 * @argument expression Expression to evaluate. String expressions can be complex.
	 *
	 * @return Returns the result of evaluating the rightmost expression.
	 */
	public Object _invoke( IBoxContext context, ArgumentsScope arguments ) {
		String expression = arguments.getAsString( expressionKey );

		return runtime.executeStatement( expression, context );
	}

}
