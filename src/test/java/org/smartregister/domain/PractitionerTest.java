package org.smartregister.domain;

import junit.framework.TestCase;
import org.junit.Assert;

public class PractitionerTest extends TestCase {

	private Practitioner practitioner;

	@Override
	public void setUp() throws Exception {
		super.setUp();

		practitioner = new Practitioner();
	}

	public void testGetIdentifier() {
		String identifier = "identifier";
		practitioner.setIdentifier(identifier);

		Assert.assertEquals(identifier, practitioner.getIdentifier());
	}

	public void testSetIdentifier() {
		String identifier = "identifier";
		practitioner.setIdentifier(identifier);

		Assert.assertEquals(identifier, practitioner.getIdentifier());
	}

	public void testGetActive() {
		boolean active = true;
		practitioner.setActive(active);

		Assert.assertEquals(active, practitioner.getActive());
	}

	public void testSetActive() {
		boolean active = true;
		practitioner.setActive(active);

		Assert.assertEquals(active, practitioner.getActive());
	}

	public void testTestGetName() {
		String name = "name";
		practitioner.setName(name);

		Assert.assertEquals(name, practitioner.getName());
	}

	public void testTestSetName() {
		String name = "name";
		practitioner.setName(name);

		Assert.assertEquals(name, practitioner.getName());
	}

	public void testGetUserId() {
		String userId = "userId";
		practitioner.setUserId(userId);

		Assert.assertEquals(userId, practitioner.getUserId());
	}

	public void testSetUserId() {
		String userId = "userId";
		practitioner.setUserId(userId);

		Assert.assertEquals(userId, practitioner.getUserId());
	}

	public void testGetUsername() {
		String username = "username";
		practitioner.setUsername(username);

		Assert.assertEquals(username, practitioner.getUsername());
	}

	public void testSetUsername() {
		String username = "username";
		practitioner.setUsername(username);

		Assert.assertEquals(username, practitioner.getUsername());
	}
}
