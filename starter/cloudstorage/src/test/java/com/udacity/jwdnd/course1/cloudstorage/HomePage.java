package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class HomePage {

    private final WebDriver driver;
    private WebDriverWait wait;
    private static final int timeOutInSeconds = 10;

    @FindBy(id="logout-button")
    private WebElement logoutButton;

    @FindBy(id="nav-notes-tab")
    private WebElement notesTab;

    @FindBy(id="nav-credentials-tab")
    private WebElement credentialsTab;

    @FindBy(id="addNoteButton")
    private WebElement addNoteButton;

    @FindBy(id="addCredentialButton")
    private WebElement addCredentialButton;

    @FindBy(id="note-title")
    private WebElement noteTitleField;

    @FindBy(id="note-description")
    private WebElement noteDescriptionField;

    @FindBy(id="credential-url")
    private WebElement credentialUrlField;

    @FindBy(id="credential-username")
    private WebElement credentialUsernameField;

    @FindBy(id="credential-password")
    private WebElement credentialPasswordField;

    @FindBy(id="noteSubmitButton")
    private WebElement noteSubmitButton;

    @FindBy(id="credentialSubmitButton")
    private WebElement credentialSubmitButton;

    @FindBy(id="noteDeleteButton")
    private WebElement noteDeleteButton;

    @FindBy(id="credentialDeleteButton")
    private WebElement credentialDeleteButton;

    @FindBy(id="noteEditButton")
    private WebElement noteEditButton;

    @FindBy(id="credentialEditButton")
    private WebElement credentialEditButton;

    @FindBy(xpath="//*[@id=\"userTable\"]/tbody/tr[1]/th")
    private WebElement noteTitle;

    @FindBy(xpath="//*[@id=\"credentialTable\"]/tbody/tr[1]/th")
    private WebElement credentialUrl;

    @FindBy(xpath="//*[@id=\"userTable\"]/tbody/tr")
    private List<WebElement> noteTableRows;

    @FindBy(xpath="//*[@id=\"credentialTable\"]/tbody/tr")
    private List<WebElement> credentialTableRows;


    public HomePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void logout() {
        logoutButton.click();
    }

    public void selectNoteTab() {
        notesTab.click();
        wait = new WebDriverWait(driver, timeOutInSeconds);
        wait.until(ExpectedConditions.elementToBeClickable(addNoteButton));
    }

    public void selectCredentialTab() {
        credentialsTab.click();
        wait = new WebDriverWait(driver, timeOutInSeconds);
        wait.until(ExpectedConditions.elementToBeClickable(addCredentialButton));
    }

    public void showNewNoteModal() {
        addNoteButton.click();
        wait = new WebDriverWait(driver,timeOutInSeconds);
        wait.until(ExpectedConditions.visibilityOf(noteTitleField));
    }

    public void showNewCredentialModal() {
        addCredentialButton.click();
        wait = new WebDriverWait(driver,timeOutInSeconds);
        wait.until(ExpectedConditions.visibilityOf(credentialUrlField));
    }

    public void showEditNoteModal() {
        noteEditButton.click();
        wait = new WebDriverWait(driver,timeOutInSeconds);
        wait.until(ExpectedConditions.visibilityOf(noteTitleField));
    }

    public void showEditCredentialModal() {
        credentialEditButton.click();
        wait = new WebDriverWait(driver,timeOutInSeconds);
        wait.until(ExpectedConditions.visibilityOf(credentialUrlField));
        wait.until(ExpectedConditions.visibilityOf(credentialUsernameField));
        wait.until(ExpectedConditions.visibilityOf(credentialPasswordField));
    }

    public void submitNote(String noteTitle, String noteDescription) {
        noteTitleField.clear();
        noteDescriptionField.clear();
        noteTitleField.sendKeys(noteTitle);
        noteDescriptionField.sendKeys(noteDescription);
        noteSubmitButton.click();
    }

    public void submitCredential(String url, String username, String password) {
        credentialUrlField.clear();
        credentialUsernameField.clear();
        credentialPasswordField.clear();
        credentialUrlField.sendKeys(url);
        credentialUsernameField.sendKeys(username);
        credentialPasswordField.sendKeys(password);
        credentialSubmitButton.click();
    }

    public String getNoteTitleText() {
        return noteTitle.getText();
    }

    public String getCredentialUrlText() {
        return credentialUrl.getText();
    }

    public String getCredentialPasswordText() {
        return credentialPasswordField.getText();
    }

    public void deleteNote() {
        noteDeleteButton.click();
    }

    public void deleteCredential() {
        credentialDeleteButton.click();
    }

    public List<WebElement> getNoteTableRows() {
        return noteTableRows;
    }

    public List<WebElement> getCredentialTableRows() {
        return credentialTableRows;
    }
}
