package org.apache.commons.mail;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class EmailTest {
	
	private static final String[] TEST_EMAILS = {"abc@bc.com", "a.b@c.com", "abcdefghijklmnopqrst@abcdefghijklmnopqrst.com"};
	
	// Concrete Email Class For Testing
	private EmailConcrete email;
	
	@Before
	public void setUpEmailTest() throws Exception {
		email = new EmailConcrete();
	}
	
	@After
	public void tearDownEmailTest() throws Exception {}
	
	//	Test Methods
	@Test
	public void testAddBcc() throws Exception {
		email.addBcc(TEST_EMAILS);
		
		assertEquals(3, email.getBccAddresses().size());
	}
	
	@Test
	public void testAddCc() throws Exception {
        String testEmail = TEST_EMAILS[0];
        Email result = email.addCc(testEmail);
        assertEquals(result, email);
        assertTrue(email.getCcAddresses().size() == 1);
        assertEquals(email.getCcAddresses().get(0).getAddress(), testEmail);
	}
	
	@Test
	public void testAddHeader() throws Exception {
		email.addHeader("Important!", "001");
		assertEquals(email.headers.size(), 1);
	}
	
	@Test
    public void testAddHeaderThrowsIllegalArgumentException() throws Exception {
	    try {
	        email.addHeader("Important!", null);
	        fail("Expected IllegalArgumentException was not thrown");
	    } catch (IllegalArgumentException e) {
	        assertEquals("value can not be null or empty", e.getMessage());
	    }
	    assertEquals(email.headers.size(), 0);
	}
	
	@Test
	public void testGetHostName() throws Exception {
	    String expectedHostName = TEST_EMAILS[2];
	    email.setHostName(expectedHostName);
	    String actualHostName = email.getHostName();
	    assertEquals(expectedHostName, actualHostName);
	    
	}
	
	@Test
	public void testGetHostNameNullSession() throws Exception {
	    assertNull(email.getHostName());
	}

	@Test
	public void testGetSentDateDefault() throws Exception {	
	    assertEquals(email.getSentDate(), new Date());
	}
	
	@Test
	public void testGetSentDate() throws Exception {
		
		Date date = new Date();
		email.setSentDate(date);
	    assertEquals(email.getSentDate(), date);
	}
		
	@Test
	public void testSetFromWithValidEmail() throws EmailException {
	    String validEmail = TEST_EMAILS[0];
	    email.setFrom(validEmail);
	    assertEquals(validEmail, email.getFromAddress().getAddress());
	}
	
	@Test
	public void testAddReplyTo() throws Exception {
		String validEmail = TEST_EMAILS[1];
	    email.addReplyTo(validEmail, "Tony Soprano");
	    assertEquals(email.replyList.size(), 1);
	}
	
	@Test
	public void testGetSocketConnectionTimeout() throws Exception {
	    // Setup
	    int expectedTimeout = 60000;
	    
	    email.setSocketConnectionTimeout(expectedTimeout);

	    // Execution
	    int actualTimeout = email.getSocketConnectionTimeout();

	    // Assertion
	    assertEquals(expectedTimeout, actualTimeout);
	}
	
	
    @Test
    public void testGetMailSession() throws EmailException {
        email.setHostName(TEST_EMAILS[2]);

        assertNotNull(email.getMailSession());
    } 
    
    @Test(expected = EmailException.class)
    public void testGetMailSessionEmptyHostname() throws EmailException {
        email.setHostName(null);
        email.getMailSession();
    }
    
    @Test
    public void testBuildMimeMessage() throws Exception {
    	// Set Hostname
    	email.setHostName(TEST_EMAILS[1]);
    	
        // Set up the email object
        email.addTo("recipient@example.com");
        email.setFrom("sender@example.com");
        email.setSubject("Test Subject");
        
        // Add headers
        email.addHeader("X-Priority", "1 (Highest)");
        email.addHeader("X-Mailer", "MyApp Mailer");
        email.addHeader("X-MyHeader", "My Value");

        // Build the MimeMessage
        email.buildMimeMessage();
    }
    
    @Test
    public void testBuildMimeMessageCc() throws Exception {
    	// Set Hostname
    	email.setHostName(TEST_EMAILS[1]);
    	email.addCc(TEST_EMAILS[2]);
    	
        // Set up the email object
        email.addTo("recipient@example.com");
        email.setFrom("sender@example.com");
        email.setSubject("Test Subject");

        // Build the MimeMessage
        email.buildMimeMessage();
    }

    @Test
    public void testBuildMimeMessageBcc() throws Exception {
    	// Set Hostname
    	email.setHostName(TEST_EMAILS[1]);
    	email.addBcc(TEST_EMAILS[0]);
    	
        // Set up the email object
        email.addTo("recipient@example.com");
        email.setFrom("sender@example.com");
        email.setSubject("Test Subject");

        // Build the MimeMessage
        email.buildMimeMessage();
    }
    
    @Test
    public void testBuildMimeMessageReply() throws Exception {
    	// Set Hostname
    	email.setHostName(TEST_EMAILS[1]);
    	email.addReplyTo(TEST_EMAILS[2]);
    	
        // Set up the email object
        email.addTo("recipient@example.com");
        email.setFrom("sender@example.com");
        email.setSubject("Test Subject");

        // Build the MimeMessage
        email.buildMimeMessage();
    }
    
    @Test(expected = EmailException.class)
    public void testBuildMimeMessageNoBody() throws EmailException {
    	// Set Hostname
    	email.setHostName(TEST_EMAILS[1]);

        // Build the MimeMessage
        email.buildMimeMessage();
    }
        
    @Test(expected = EmailException.class)
    public void testBuildMimeMessageNoRecepient() throws EmailException {
    	// Set Hostname
    	email.setHostName(TEST_EMAILS[1]);
    	
    	email.setFrom("sender@example.com");
    	email.setSubject("Test Subject");

        // Build the MimeMessage
        email.buildMimeMessage();
    }
    
    @Test(expected =IllegalStateException.class)
    public void testBuildMimeMessageDuplicates() throws Exception {
    	// Set Hostname
    	email.setHostName(TEST_EMAILS[1]);
    	email.addReplyTo(TEST_EMAILS[2]);
    	
        // Set up the email object
        email.addTo("recipient@example.com");
        email.setFrom("sender@example.com");
        email.setSubject("Test Subject");

        // Build the MimeMessage
        email.buildMimeMessage();
        email.buildMimeMessage();
    }
    
    
}