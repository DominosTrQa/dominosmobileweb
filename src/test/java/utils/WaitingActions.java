package utils;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import base.BaseTest;

public class WaitingActions extends BaseTest{

    public WebDriver waDriver;

    public WaitingActions() {
        waDriver = driver;
    }

    static JavascriptExecutor jsDriver = null;

    public void ajaxComplete() {

        jsDriver = (JavascriptExecutor) waDriver;
        jsDriver.executeScript("var callback = arguments[arguments.length - 1];" + "var xhr = new XMLHttpRequest();"
                + "xhr.open('GET', '/Ajax_call', true);" + "xhr.onreadystatechange = function() {"
                + "  if (xhr.readyState == 4) {" + "    callback(xhr.responseText);" + "  }" + "};" + "xhr.send();");
    }

    public void pageLoadComplete() {
        jsDriver = (JavascriptExecutor) waDriver;
        ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {

            public Boolean apply(WebDriver driver) {
                return jsDriver.executeScript("return document.readyState", true).toString().equals("complete");
            }
        };
        try {
            Thread.sleep(1000);
            WebDriverWait wait = new WebDriverWait(waDriver, 200);
            wait.until(expectation);
        } catch (Throwable error) {
        }
    }

    public void waitForAngularLoad() {
        long start = System.nanoTime();

        jsDriver = (JavascriptExecutor) waDriver;
        ExpectedCondition<Boolean> expectation = new ExpectedCondition<Boolean>() {

            public Boolean apply(WebDriver driver) {
                return jsDriver.executeScript(
                        "return angular.element(document).injector().get('$http').pendingRequests.length === 0", true)
                        .toString().equals("true");
            }
        };
        try {
            WebDriverWait wait = new WebDriverWait(waDriver, 300);
            wait.until(expectation);
        } catch (Throwable error) {
        }
        long end = System.nanoTime();
        System.out.println("Angular wait " + (end - start) / 1000000000.0);
    }

    public void jQueryComplete() {

        jsDriver = (JavascriptExecutor) waDriver;
        int sleepIndex = 0;
        try {
            jsDriver.executeScript("window.jQuery");
            if (jsDriver.executeScript("return jQuery.active").toString().equals("0")) {
                System.out.println("Page Is loaded.");
                return;
            }

            for (int i = 0; i < 25; i++) {
                System.out.println("i " + i);
                if (jsDriver.executeScript("return jQuery.active").toString().equals("0")) {
                    System.out.println("for dongusune girdi ve bekliyor");
                    sleepIndex=i;
                    break;
                }
                Thread.sleep(1000);
            }
        } catch (Exception e) {

        }
        System.out.println("jQuery wait "+sleepIndex+" sn.");
        System.out.println("jQueryComplete metodunun sonu");
    }
}

