package com.mulesoft.external.provider;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.Matchers.equalTo;
import org.mule.functional.junit4.MuleArtifactFunctionalTestCase;

import javax.inject.Inject;

import org.junit.Test;

public class ExternalPropertiesProviderOperationsTestCase extends MuleArtifactFunctionalTestCase {

	/**
	 * Specifies the mule config xml with the flows that are going to be executed in
	 * the tests, this file lives in the test resources.
	 */

	static {
		System.setProperty("env", "test");
	}

	@Override
	protected String getConfigFile() {
		return "test-mule-config.xml";
	}

	@Inject
	private TestObject testObject;

	@Inject
	private TestObject propertyTestObject;

	@Test
	public void customPropertyProviderSuccessfullyConfigured() {

		System.out.println("********** propertyTestObject.getValueFromProperty()***********="
				+ propertyTestObject.getValueFromProperty());
		 assertThat(testObject.getValueFromProperty(), is("200"));

	}

}
