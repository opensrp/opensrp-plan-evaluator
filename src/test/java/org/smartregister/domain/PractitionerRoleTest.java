package org.smartregister.domain;

import junit.framework.TestCase;
import org.junit.Assert;

public class PractitionerRoleTest extends TestCase {

	private PractitionerRole practitionerRole;

	public void setUp() throws Exception {
		super.setUp();
		practitionerRole = new PractitionerRole();
	}

	public void testGetIdentifier() {
		String identifier = "identifier";
		practitionerRole.setIdentifier(identifier);

		Assert.assertEquals(identifier, practitionerRole.getIdentifier());
	}

	public void testSetIdentifier() {
		String identifier = "identifier";
		practitionerRole.setIdentifier(identifier);

		Assert.assertEquals(identifier, practitionerRole.getIdentifier());
	}

	public void testGetActive() {
		boolean active = true;
		practitionerRole.setActive(active);

		Assert.assertEquals(active, practitionerRole.getActive());
	}

	public void testSetActive() {
		boolean active = true;
		practitionerRole.setActive(active);

		Assert.assertEquals(active, practitionerRole.getActive());
	}

	public void testGetOrganizationIdentifier() {
		String organizationIdentifier = "organizationIdentifier";
		practitionerRole.setOrganizationIdentifier(organizationIdentifier);

		Assert.assertEquals(organizationIdentifier, practitionerRole.getOrganizationIdentifier());
	}

	public void testSetOrganizationIdentifier() {
		String organizationIdentifier = "organizationIdentifier";
		practitionerRole.setOrganizationIdentifier(organizationIdentifier);

		Assert.assertEquals(organizationIdentifier, practitionerRole.getOrganizationIdentifier());
	}

	public void testGetPractitionerIdentifier() {
		String practitionerIdentifier = "practitionerIdentifier";
		practitionerRole.setPractitionerIdentifier(practitionerIdentifier);

		Assert.assertEquals(practitionerIdentifier, practitionerRole.getPractitionerIdentifier());
	}

	public void testSetPractitionerIdentifier() {
		String practitionerIdentifier = "practitionerIdentifier";
		practitionerRole.setPractitionerIdentifier(practitionerIdentifier);

		Assert.assertEquals(practitionerIdentifier, practitionerRole.getPractitionerIdentifier());
	}

	public void testGetCode() {
		PractitionerRoleCode code = new PractitionerRoleCode();
		code.setText("code");

		practitionerRole.setCode(code);

		Assert.assertEquals(code, practitionerRole.getCode());
		//Assert.assertEquals(code.getText(), practitionerRole.getCode().getText());
	}

	public void testSetCode() {
		PractitionerRoleCode code = new PractitionerRoleCode();
		code.setText("code");

		practitionerRole.setCode(code);

		Assert.assertEquals(code, practitionerRole.getCode());
		//Assert.assertEquals(code.getText(), practitionerRole.getCode().getText());
	}
}
