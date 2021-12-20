package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;
	public String baseURL;

	@BeforeAll
	static void beforeAll() {
		WebDriverManager.chromedriver().setup();
	}

	@BeforeEach
	public void beforeEach() {
		baseURL = "http://localhost:" + port;
		driver = new ChromeDriver();
	}

	@AfterEach
	public void afterEach() {
		if (driver != null) {
			driver.quit();
		}
	}

	@Test
	public void unauthorizedUser_viewsPages_canViewLoginAndSignupOnly() {
		driver.get(baseURL + "/login");
		assertEquals("Login", driver.getTitle());
		driver.get(baseURL + "/signup");
		assertEquals("Signup", driver.getTitle());
		driver.get(baseURL + "/home");
		assertNotEquals("Home", driver.getTitle());
	}

	@Test
	public void authorizedUser_viewsHomepageAndLogsOut_CannotViewHomepage() {
		signupAndLogin();
		assertEquals("Home", driver.getTitle());

		HomePage home = new HomePage(driver);
		home.logout();

		driver.get(baseURL + "/home");
		assertNotEquals("Home", driver.getTitle());
	}

	@Test
	public void createNote_noteIsDisplayed() {
		signupAndLogin();
		HomePage home = new HomePage(driver);
		// create note
		home.selectNoteTab();
		home.showNewNoteModal();
		String title = "myTitle";
		String description = "myDescription";
		home.submitNote(title, description);
		// verify note is displayed
		assertEquals("Result", driver.getTitle());
		ResultPage result = new ResultPage(driver);
		result.homeFromSuccess();
		assertEquals("Home", driver.getTitle());
		home.selectNoteTab();
		assertEquals(1, home.getNoteTableRows().size());
		String actualTitle = home.getNoteTitleText();
		assertEquals(title, actualTitle);
		// clean up
		home.deleteNote();
	}

	@Test
	public void editNote_changeIsDisplayed() {
		signupAndLogin();
		HomePage home = new HomePage(driver);
		// create note
		home.selectNoteTab();
		home.showNewNoteModal();
		String title = "myTitle";
		String description = "myDescription";
		home.submitNote(title, description);
		ResultPage result = new ResultPage(driver);
		result.homeFromSuccess();
		// edit note
		home.selectNoteTab();
		home.showEditNoteModal();
		String newTitle = "myNewTitle";
		String newDescription = "myNewDescription";
		home.submitNote(newTitle, newDescription);
		// verify edited note is displayed
		assertEquals("Result", driver.getTitle());
		ResultPage resultAfterEdit = new ResultPage(driver);
		resultAfterEdit.homeFromSuccess();
		assertEquals("Home", driver.getTitle());
		home.selectNoteTab();
		assertEquals(1, home.getNoteTableRows().size());
		String actualTitle = home.getNoteTitleText();
		assertEquals(newTitle, actualTitle);
		// clean up
		home.deleteNote();
	}

	@Test
	public void deleteNote_noteIsDeleted() {
		signupAndLogin();
		HomePage home = new HomePage(driver);
		// create note
		home.selectNoteTab();
		home.showNewNoteModal();
		String title = "myTitle";
		String description = "myDescription";
		home.submitNote(title, description);
		ResultPage result = new ResultPage(driver);
		result.homeFromSuccess();
		// delete note
		home.selectNoteTab();
		home.deleteNote();
		// verify note is deleted
		assertTrue(home.getNoteTableRows().isEmpty());
	}

	@Test
	public void createCredential_credentialIsDisplayed() {
		signupAndLogin();
		HomePage home = new HomePage(driver);
		// create credential
		home.selectCredentialTab();
		home.showNewCredentialModal();
		String url = "myUrl";
		String username = "myUsername";
		String password = "myPassword";
		home.submitCredential(url, username, password);
		// verify credential is displayed and password is encrypted
		assertEquals("Result", driver.getTitle());
		ResultPage result = new ResultPage(driver);
		result.homeFromSuccess();
		assertEquals("Home", driver.getTitle());
		home.selectCredentialTab();
		assertEquals(1, home.getCredentialTableRows().size());
		String actualUrl = home.getCredentialUrlText();
		assertEquals(url, actualUrl);
		String actualPassword = home.getCredentialPasswordText();
		assertNotEquals(password, actualPassword);
		// clean up
		home.deleteCredential();
	}

	@Test
	public void editCredential_changeIsDisplayed() {
		signupAndLogin();
		HomePage home = new HomePage(driver);
		// create credential
		home.selectCredentialTab();
		home.showNewCredentialModal();
		String url = "myUrl";
		String username = "myUsername";
		String password = "myPassword";
		home.submitCredential(url, username, password);
		ResultPage result = new ResultPage(driver);
		result.homeFromSuccess();
		// verify editable credential password is decrypted
		home.selectCredentialTab();
		home.showEditCredentialModal();
		String decryptedPassword = home.getCredentialPasswordText();
		// edit credential
		String newUrl = "myNewUrl";
		String newUsername = "myNewUsername";
		String newPassword = "myNewPassword";
		home.submitCredential(newUrl, newUsername, newPassword);
		assertEquals("Result", driver.getTitle());
		ResultPage resultAfterEdit = new ResultPage(driver);
		resultAfterEdit.homeFromSuccess();
		assertEquals("Home", driver.getTitle());
		home.selectCredentialTab();
		assertEquals(1, home.getCredentialTableRows().size());
		String actualUrl = home.getCredentialUrlText();
		assertEquals(newUrl, actualUrl);
		// clean up
		home.deleteCredential();
	}

	@Test
	public void deleteCredential_credentialIsDeleted() {
		signupAndLogin();
		HomePage home = new HomePage(driver);
		// create credential
		home.selectCredentialTab();
		home.showNewCredentialModal();
		String url = "myUrl";
		String username = "myUsername";
		String password = "myPassword";
		home.submitCredential(url, username, password);
		ResultPage result = new ResultPage(driver);
		result.homeFromSuccess();
		// delete credential
		home.selectCredentialTab();
		home.deleteCredential();
		// verify credential is deleted
		assertTrue(home.getCredentialTableRows().isEmpty());
	}

	private void signupAndLogin() {
		String username = "123";
		String password = "456";
		signup(username, password);
		login(username, password);
	}

	private void signup(String username, String password) {
		driver.get(baseURL + "/signup");
		SignupPage signup = new SignupPage(driver);
		signup.signup("abc", "def", username, password);
	}

	private void login(String username, String password) {
		driver.get(baseURL + "/login");
		LoginPage login = new LoginPage(driver);
		login.login(username, password);
	}

}
