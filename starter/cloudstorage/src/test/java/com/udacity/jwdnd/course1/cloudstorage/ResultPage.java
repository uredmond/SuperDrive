package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class ResultPage {

    @FindBy(id="success-link")
    private WebElement successLink;

    @FindBy(id="fail-link")
    private WebElement failLink;

    public ResultPage(WebDriver webDriver) {
        PageFactory.initElements(webDriver, this);
    }

    public void homeFromSuccess() {
        successLink.click();
    }

    public void homeFromFail() {
        failLink.click();
    }

}
