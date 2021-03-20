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
		System.setProperty("env", "functional_test");
	}

	@Override
	protected String getConfigFile() {
		return "test-mule-config.xml";
	}

	@Inject
	private TestLastReplayIdObject assertThatObject;

	@Test
	public void testReplayIdExternalSource() {
		 assertThat(assertThatObject.getValueFromProperty(), is("ray_arnado_external_source_test_only_for_default"));

	}

}
