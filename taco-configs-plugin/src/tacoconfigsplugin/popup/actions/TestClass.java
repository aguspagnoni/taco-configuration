package tacoconfigsplugin.popup.actions;

public class TestClass {
	
	public void test_find() throws VizException {
		setConfigKeyRelevantClasses(""ejercicios.AVLTree,ejercicios.AVLNode"");
		setConfigKeyRelevancyAnalysis(true);
		setConfigKeyCheckNullDereference(true);
		setConfigKeyCheckArithmeticException(false);
		setConfigKeyUseJavaArithmetic(true);
		setConfigKeySkolemizeInstanceInvariant(false);
		setConfigKeySkolemizeInstanceAbstraction(false);
		setConfigKeyRemoveQuantifiers(true);
		// Infer-Scope
		setConfigKeyInferScope(true);
		setConfigKeyTypeScopes(""ejercicios.AVLTree:1,ejercicios.AVLNode:6"");
		setConfigKeyLoopUnroll(4);
		// SBP+BOUND
		setConfigKeyUseJavaSBP(false);
		setConfigKeyUseTightUpperBounds(false);
		// JUNIT
		setConfigKeyGenerateUnitTestCase(true);

		check(GENERIC_PROPERTIES,"find_0", false);
	}

	public void test_insert() throws VizException {
		setConfigKeyRelevantClasses(""ejercicios.AVLTree,ejercicios.AVLNode"");
		setConfigKeyRelevancyAnalysis(true);
		setConfigKeyCheckNullDereference(true);
		setConfigKeyCheckArithmeticException(false);
		setConfigKeyUseJavaArithmetic(true);
		setConfigKeySkolemizeInstanceInvariant(false);
		setConfigKeySkolemizeInstanceAbstraction(false);
		setConfigKeyRemoveQuantifiers(true);
		// Infer-Scope
		setConfigKeyInferScope(true);
		setConfigKeyTypeScopes(""ejercicios.AVLTree:1,ejercicios.AVLNode:6"");
		setConfigKeyLoopUnroll(4);
		// SBP+BOUND
		setConfigKeyUseJavaSBP(false);
		setConfigKeyUseTightUpperBounds(false);
		// JUNIT
		setConfigKeyGenerateUnitTestCase(true);

		simulate(GENERIC_PROPERTIES, "insert_0");
	}

	@Override
	protected String getClassToCheck() {
		return "ejercicios.AVLTree";
	}
}
