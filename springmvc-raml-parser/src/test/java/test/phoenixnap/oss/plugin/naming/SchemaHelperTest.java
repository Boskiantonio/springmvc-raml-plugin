/*
 * Copyright 2002-2016 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package test.phoenixnap.oss.plugin.naming;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Iterator;
import java.util.Map;

import org.junit.Test;
import org.raml.model.ParamType;
import org.raml.model.parameter.QueryParameter;

import test.phoenixnap.oss.plugin.naming.testclasses.ThreeElementClass;

import com.phoenixnap.oss.ramlapisync.naming.SchemaHelper;

/**
 * Unit tests for the methods within SchemaHelper
 * 
 * @author Kurt Paris
 * @since 0.0.1
 *
 */
public class SchemaHelperTest {

	@SuppressWarnings("unused")
	private void testMethod_with_primitive(int primitive) {
		// do nothing.
	}

	@SuppressWarnings("unused")
	private void testMethod_with_boxedPrimitive(Integer boxedPrimitive) {
		// do nothing.
	}

	@SuppressWarnings("unused")
	private void testMethod_with_classWith3Elements(ThreeElementClass elementClass) {
		// do nothing.
	}

	/**
	 * Gets a parameter from a method within this test class to be ableto recreate scenarios which are hard or
	 * impossible to mock
	 * @param methodName
	 * @param parameterIndex
	 * @return
	 */
	private Parameter getParameterFromSampleMethod(String methodName, int parameterIndex) {
		Method[] methods = SchemaHelperTest.class.getDeclaredMethods();
		for (Method method : methods) {
			if (method.getName().equals(methodName)) {
				return method.getParameters()[parameterIndex];
			}
		}
		throw new IllegalArgumentException(methodName + "[" + parameterIndex + "] does not exist.");
	}

	@Test
	public void test_convertClassToQueryParameters_emptyOrVoid() {
		assertEquals("Expect empty Map", 0, SchemaHelper.convertClassToQueryParameters(null, null).size());
	}

	@Test
	public void test_convertParameterToQueryParameter_boxedPrimitive() {
		String methodName = "testMethod_with_boxedPrimitive";
		String comment = "testComment";
		Map<String, QueryParameter> queryParameters = SchemaHelper.convertParameterToQueryParameter(
				getParameterFromSampleMethod(methodName, 0), comment);
		assertEquals("Expect Map with 1 Elements", 1, queryParameters.size());
		QueryParameter queryParameter = queryParameters.values().iterator().next();
		validateQueryParameter(queryParameter, "boxedPrimitive", ParamType.INTEGER);
		assertEquals("Check javadoc as description", comment, queryParameter.getDescription());
		assertEquals("Expect Name as map key", queryParameter, queryParameters.get(queryParameter.getDisplayName()));
	}

	@Test
	public void test_convertParameterToQueryParameter_primitive() {
		String methodName = "testMethod_with_primitive";
		Map<String, QueryParameter> queryParameters = SchemaHelper.convertParameterToQueryParameter(
				getParameterFromSampleMethod(methodName, 0), null);
		assertEquals("Expect Map with 1 Elements", 1, queryParameters.size());
		QueryParameter queryParameter = queryParameters.values().iterator().next();
		validateQueryParameter(queryParameter, "primitive", ParamType.INTEGER);
		assertEquals("Expect Name as map key", queryParameter, queryParameters.get(queryParameter.getDisplayName()));
	}

	@Test(expected = IllegalArgumentException.class)
	public void test_convertParameterToQueryParameter_classWith3Elements() {
		String methodName = "testMethod_with_classWith3Elements";
		SchemaHelper.convertParameterToQueryParameter(getParameterFromSampleMethod(methodName, 0), null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void test_convertClassToQueryParameters_boxedPrimitive() {
		String methodName = "testMethod_with_boxedPrimitive";
		SchemaHelper.convertClassToQueryParameters(getParameterFromSampleMethod(methodName, 0), null);
	}

	@Test
	public void test_convertClassToQueryParameters_classWith3Elements() {
		String methodName = "testMethod_with_classWith3Elements";
		QueryParameter queryParameter;
		Map<String, QueryParameter> queryParameters = SchemaHelper.convertClassToQueryParameters(
				getParameterFromSampleMethod(methodName, 0), null);
		assertEquals("Expect Map with three Elements", 3, queryParameters.size());
		Iterator<QueryParameter> qpIterator = queryParameters.values().iterator();
		queryParameter = qpIterator.next();
		validateQueryParameter(queryParameter, "element1", ParamType.INTEGER);
		assertEquals("Expect Name as map key", queryParameter, queryParameters.get(queryParameter.getDisplayName()));

		queryParameter = qpIterator.next();
		validateQueryParameter(queryParameter, "element2", ParamType.INTEGER);
		assertEquals("Expect Name as map key", queryParameter, queryParameters.get(queryParameter.getDisplayName()));

		queryParameter = qpIterator.next();
		validateQueryParameter(queryParameter, "element3", ParamType.STRING);
		assertEquals("Expect Name as map key", queryParameter, queryParameters.get(queryParameter.getDisplayName()));
	}

	/**
	 * Asserts values within a query parameter
	 * @param name
	 * @param queryParameters
	 * @param queryParameter
	 */
	private void validateQueryParameter(QueryParameter queryParameter, String name, ParamType expectedType) {
		assertEquals("Expect Correct Type for element: " + name, expectedType, queryParameter.getType());
		assertEquals("Expect Name", name, queryParameter.getDisplayName());

	}

}
