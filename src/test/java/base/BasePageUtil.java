package base;

import com.thoughtworks.gauge.Step;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.HasInputDevices;
import org.openqa.selenium.interactions.Mouse;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import utils.WaitingActions;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BasePageUtil extends BaseTest {

    public static final int DEFAULT_WAIT = 10;

    public static final int MIN_WAIT = 5;

    public static final int MAX_WAIT = 20;

    public WaitingActions wa;

    deleteOrderUtil deleteorder = new deleteOrderUtil();
    public static JavascriptExecutor js;

    public void waitAllRequests() {
        wa = new WaitingActions();
        wa.pageLoadComplete();
        wa.jQueryComplete();
        wa.waitForAngularLoad();
        wa.ajaxComplete();
    }

    public void waitAllRequestsExceptAjax() {
      /*  wa = new WaitingActions();
        wa.pageLoadComplete();
        wa.jQueryComplete();
        wa.waitForAngularLoad();*/
    }

    @Step("\"<url>\"   adresine git")
    public void openUrl(String url) {
        driver.get(super.URL + url);
    }

    @Step("\"<text>\" yazisina tikla")
    public WebElement clickObjectByText(String text) {
        WebElement element = getElementBy(By.xpath("//*[contains(text(), '" + text + "')]"));
        element.click();
        return element;
    }

    @Step("\"<id>\" id nesnesi altindaki \"<text>\" yazisina tikla")
    public WebElement clickTextInElement(String id, String text) {
        WebElement element = getElementBy(By.xpath("//*[@id='" + id + "']//*[contains(text(), '" + text + "')]"));
        element.click();
        return element;
    }

    @Step("ekranda <id> id nesnesini gormen gerekiyor")
    public void objectControlById(String id) {
        Assert.assertTrue(id + " id'li eleman sayfada görüntülenemedi", isElementPresentAndDisplayWithWait(By.id(id)));
    }

    @Step("ekranda <text> yazisini gormen gerekiyor")
    public void textControl(String text) {
        Assert.assertTrue("Ekranda ilgili text görüntülenemedi.", isTextPresent(text));
    }

    @Step("ekranda <className> className nesnesini gormen gerekiyor")
    public void objectControlByclassName(String className) {
        Assert.assertTrue(className + " className'li eleman sayfada görüntülenemedi", waitForElementByClassName(className, DEFAULT_WAIT).isDisplayed());
    }

    @Step("<id> id'li alana form datası gönderme islemini yap.")
    public void submitObjectById(String id) {
        submitObjectBy(By.id(id));
    }

    @Step("\"<classname>\" class'li alana form datasi gönderme islemini yap.")
    public void submitObjectByClassName(String classname) {
        submitObjectBy(By.className(classname));
    }

    public WebElement submitObjectBy(By by) {
        WebElement element = getElementBy(by);
        element.submit();
        return element;
    }

    @Step("\"<x>\" ve \"<y>\" koordinatlarında sayfada scroll işlemi yap")
    protected void scrollTo(int x, int y) {
        String jsStmt = String.format("window.scrollTo(%d, %d);", x, y);
        executeJS(jsStmt, true);
    }

    protected Object executeJS(String jsStmt, boolean wait) {
        return wait ? getJSExecutor().executeScript(jsStmt, "") : getJSExecutor().executeAsyncScript(jsStmt, "");
    }

    @Step("\"<JavaScriptCode>\" unu \"<wait>\" boolean değeri ile çalıştır")
    public void executeJavaScript(String JavaScriptCode, boolean wait) {
        executeJS(JavaScriptCode, wait);
    }

    protected JavascriptExecutor getJSExecutor() {
        return (JavascriptExecutor) driver;
    }


    public WebElement setObjectBy(By by, String value) {

        // JavascriptExecutor myExecutor = ((JavascriptExecutor) driver);
        // myExecutor.executeScript("arguments[0].value='" + value + "';", driver.findElement(by));

        WebElement element = driver.findElement(by);
        JavascriptExecutor myExecutor = ((JavascriptExecutor) driver);
        myExecutor.executeScript("arguments[0].value='" + value + "';", element);

        //driver.executeScript("document.getElementsByClassName('LoginRegisterPage-phone').setAttribute('value',"+value+")");

       /* WebElement element = findElement(by);
        element.clear();

        for (int i = 0; i < value.length(); i++){
            char c = value.charAt(i);
            String s = new StringBuilder().append(c).toString();

        }*/
        /*findElement(by).click();
        findElement(by).clear();
        findElement(by).sendKeys(value);*/
        //driver.getKeyboard().sendKeys(value);
        return driver.findElement(by);
    }

    @Step("\"<name>\" name alanina \"<value>\" yaz")
    public WebElement setObjectByName(String name, String value) {
        return setObjectBy(By.name(name), value);
    }

    @Step("\"<id>\" id alanina \"<value>\" yaz")
    public WebElement setObjectById(String id, String value) {
        return setObjectBy(By.id(id), value);
    }

    @Step("\"<css>\" css alanina \"<value>\" yazdin")
    public WebElement setObjectByCssSelector(String cssSelector, String value) {
        return setObjectBy(By.cssSelector(cssSelector), value);
    }

    @Step("\"<xpath>\" xpath alanina \"<value>\" yazdin")
    public WebElement setObjectByXpath(String xpath, String value) {
        return setObjectBy(By.xpath(xpath), value);
    }

    @Step("\"<xpath>\" className alanina \"<value>\" yazdin")
    public WebElement setObjectByClassName(String className, String value) {
        return setObjectBy(By.className(className), value);
    }

    public WebElement selectValueObjectBy(By by, String value) {
        WebElement element = getElementBy(by);
        new Select(element).selectByValue(value);
        //  new Select(element).selectByVisibleText(value);
        return element;
    }

    @Step("\"<id>\" id nesnesinde \"<value>\" degerini sec")
    public WebElement selectValueObjectById(String id, String value) {
        WebElement element = selectValueObjectBy(By.id(id), value);
        return element;
    }

    @Step("\"name\" name nesnesinde \"<value>\" degerini sec")
    public WebElement selectValueObjectByName(String name, String value) {
        WebElement element = selectValueObjectBy(By.name(name), value);
        return element;
    }

    @Step("\"<id>\" id'li elemanı \"<seconds>\" saniye kadar bekle")
    public WebElement waitForElementById(String id, int seconds) {
        WebElement element = waitForElement(seconds, By.id(id));
        return element;
    }

    @Step("\"<class>\" class isimli elemanı \"<seconds>\" saniye kadar bekle")
    public WebElement waitForElementByClassName(String classname, int seconds) {
        WebElement element = waitForElement(seconds, By.className(classname));
        return element;
    }

    @Step("\"<xpath>\" xpath'li elemanı \"<xpath>\" saniye kadar bekle")
    public WebElement waitForElementByXpath(String xpath, int seconds) {
        WebElement element = waitForElement(seconds, By.xpath(xpath));
        return element;
    }

    @Step("\"<css>\" css seçimindeki elemanı \"<seconds>\" saniye kadar bekle")
    public WebElement waitForElementByCss(String css, int seconds) {
        WebElement element = waitForElement(seconds, By.cssSelector(css));
        return element;
    }

    @Step("\"<linkText>\" linkText'li elemanı \"<seconds>\" saniye kadar bekle")
    public WebElement waitForElementByLinkText(String linkText, int seconds) {
        WebElement element = waitForElement(seconds, By.linkText(linkText));
        return element;
    }

    @Step("\"<partialLinkText>\" linkText içeren elemanı \"<seconds>\" saniye kadar bekle")
    public WebElement waitForElementByPartialLinkText(String partialLinkText, int seconds) {
        WebElement element = waitForElement(seconds, By.partialLinkText(partialLinkText));
        return element;
    }

    @Step("\"<id>\" id nesnesinde \"<index>\" indexini sec")
    public WebElement selectIndexObjectById(String name, int index) {
        waitForElement(MAX_WAIT, By.id(name));
        WebElement element = driver.findElement(By.id(name));
        new Select(element).selectByIndex(index);
        return element;
    }

    @Step("\"<className>\" className nesnesinde \"<index>\" indexini sec")
    public WebElement selectIndexObjectByClassName(String name, int index) {
        waitForElement(MAX_WAIT, By.className(name));
        WebElement element = driver.findElement(By.className(name));
        new Select(element).selectByIndex(index);
        return element;
    }

    @Step("\"<by>\" elemanına tıkla")
    public WebElement clickObjectByBy(By by) {
        return clickObjectBy(by);
    }

    @Step("\"<id>\" id nesnesine tikla")
    public WebElement clickObjectById(String id) {
        return clickObjectBy(By.id(id));
    }

    @Step("\"<name>\" name nesnesine tikla")
    public WebElement clickObjectByName(String name) {
        return clickObjectBy(By.name(name));
    }

    @Step("\"<css>\" css nesnesine tikla")
    public WebElement clickObjectByCss(String css) {
        return clickObjectBy(By.cssSelector(css));
    }

    @Step("<by> elementine <text> değerini yaz")

    public void setTextToElement(By by, String text) {
        findElement(by).sendKeys(text);
    }

    @Step("\"<className>\" className nesnesine tikla")
    public WebElement clickObjectByClassName(String className) {
        return clickObjectBy(By.className(className));
    }

    @Step("\"<linkText>\" linkine tikla")
    public WebElement clickObjectByLinkText(String linkText) {
        return clickObjectBy(By.linkText(linkText));
    }

    @Step("\"<xpath>\" xpath nesnesine tikla")
    public WebElement clickObjectByXpath(String xpath) {
        return clickObjectBy(By.xpath(xpath));
    }

    @Step("\"<id>\" id nesnesi varsa tikla")
    public WebElement clickObjectIfExist(String id) {
        if (isExistElement(3, By.id(id)))
            return clickObjectById(id);
        return null;
    }

    @Step("\"<class>\" class nesnesi varsa tikla")
    public WebElement clickObjectIfExistClass(String classname) {
        if (isExistElement(10, By.className(classname)))
            return clickObjectByClassName(classname);
        return null;
    }

    @Step("<id> id nesnesi varsa <id> id nesnesine tikla")
    public WebElement clickObjectIfExistById(String id, String idclick) {
        if (isExistElement(3, By.id(id)))
            return clickObjectById(idclick);
        return null;
    }

    public WebElement clickObjectBy(By by) {
        WebElement element = getElementBy(by);
        element.click();
        return element;
    }

    public WebElement getElementBy(By by) {
        untilElementAppear(by, MIN_WAIT);
        return driver.findElement(by);
    }

    public WebElement getElementById(String id) {
        return getElementBy(By.id(id));
    }

    public WebElement getElementByClassName(String className) {
        return getElementBy(By.className(className));
    }

    public List<WebElement> getElementsBy(By by) {
        untilElementAppear(by, MAX_WAIT);
        return driver.findElements(by);
    }

    public List<WebElement> getElementsById(String id) {
        return getElementsBy(By.id(id));
    }

    public List<WebElement> getElementsByCss(String css) {
        return getElementsBy(By.cssSelector(css));
    }

    public List<WebElement> getElementsByClassName(String className) {
        return getElementsBy(By.className(className));
    }

    public List<WebElement> getSelectOptionListById(String id) {
        Select select = new Select(getElementBy(By.id(id)));
        return select.getOptions();
    }

    public List<WebElement> getSelectOptionListByName(String name) {
        Select select = new Select(getElementBy(By.name(name)));
        return select.getOptions();
    }

    public boolean isDisplayBy(By by) {
        return driver.findElement(by).isDisplayed();
    }

    public boolean isDisplayByPassException(By by) {
        try {
            return driver.findElement(by).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isDisplayById(String id) {
        try {
            return driver.findElement(By.id(id)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isDisplayByCss(String css) {
        return driver.findElement(By.cssSelector(css)).isDisplayed();
    }

    public boolean isDisplayByName(String name) {
        return driver.findElement(By.name(name)).isDisplayed();
    }

    public boolean isDisplayByClassName(String className) {
        return driver.findElement(By.className(className)).isDisplayed();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl().trim().toString();
    }

    @Step("Geri git")
    public void goBack() {
        driver.navigate().back();
    }

    @Step("\"<time>\" ms bekle")
    public void sleep(Integer time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
        }
    }

    public void moveMouse(By by, By validateDisplayWebObject, int count) {
        WebDriverWait wait = new WebDriverWait(driver, MAX_WAIT);
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        int i = 0;
        do {
            WebElement we = driver.findElement(by);
            Locatable hoverItem = (Locatable) we;
            Mouse mouse = ((HasInputDevices) driver).getMouse();
            mouse.mouseMove(hoverItem.getCoordinates());
            sleep(1000);
            if (count == i++)
                break;
        } while (!isDisplayByPassException(validateDisplayWebObject));
    }

    public void moveMouseWithJQuery(String id, By validateDisplayWebObject, int count) {
        WebDriverWait wait = new WebDriverWait(driver, MAX_WAIT);
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(id)));
        int i = 0;
        do {
            ((JavascriptExecutor) driver).executeScript("$('" + id + "').mouseover();");
            sleep(1000);
            if (count == i++)
                break;
            System.out.println(i + ". mouseover deneme...");
        } while (!isDisplayByPassException(validateDisplayWebObject));
    }

    public void moveMouseWithJavascript(String id, By validateDisplayWebObject, int count) {
        WebDriverWait wait = new WebDriverWait(driver, MAX_WAIT);
        WebElement element = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(id)));
        int i = 0;
        do {
            ((JavascriptExecutor) driver).executeScript("$('" + id + "').mouseover();");
            String strJavaScript = "var element = arguments[0];" + "var mouseEventObj = document.createEvent('MouseEvents');" + "mouseEventObj.initEvent( 'mouseover', true, true );" + "element.dispatchEvent(mouseEventObj);";
            executeJavascript(strJavaScript, getElementBy(By.id(id)));
            sleep(1000);
            if (count == i++)
                break;
            System.out.println(i + ". mouseover deneme...");
        } while (!isDisplayByPassException(validateDisplayWebObject));
    }

    public void executeJavascript(String script, Object... obj) {
        ((JavascriptExecutor) driver).executeScript(script, obj);
    }

    public void callPage(String page) {
        driver.get(getCurrentUrl() + page);
    }

    public boolean isTextPresent(By by, String text) {
        try {
            return driver.findElement(by).getText().contains(text);
        } catch (NullPointerException e) {
            return false;
        }
    }

    @Step("Sayfada <text> cumlesinin gorunurlugunu kontrol et.")
    public boolean isTextPresent(String text) {
        try {
            return driver.getPageSource().contains(text);
        } catch (NullPointerException e) {
            return false;
        }
    }

    @Step("PageSource")
    public void getPageSource() {
        System.out.println("PageSource:" + driver.getPageSource());
    }

    /**
     * bekleme gerektirmeyen o an sayfada olması veya olmaması
     * durumu kont.
     * @param by
     * @return
     */
    /**
     * bekleme gerektirmeyen o an sayfada olması veya olmaması
     * durumu kont.
     *
     * @param by
     * @return
     */
    public boolean isElementPresent(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * ios safaride alınan hatadan dolayı eklendi.
     * @param by
     * @return
     */
    /**
     * ios safaride alınan hatadan dolayı eklendi.
     *
     * @param by
     * @return
     */
    public boolean isElementPresentWithWait(By by) {
        try {
            untilElementAppear(by, MIN_WAIT);
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public boolean isElementPresent(By by, WebElement element) {
        try {
            element.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public boolean isElementPresentWithWait(By by, WebElement element) {
        try {
            untilElementAppear(by, MAX_WAIT);
            element.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public boolean areElementsPresent(By by) {
        try {
            untilElementAppear(by, MAX_WAIT);
            driver.findElements(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public boolean isElementPresentAndDisplay(By by) {
        try {
            return driver.findElement(by).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public boolean isElementPresentAndDisplayWithWait(By by) {
        try {
            untilElementAppear(by, MIN_WAIT);
            return driver.findElement(by).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public boolean isElementPresentAndDisplay(By by, WebElement element) {
        try {
            untilElementAppear(by, MAX_WAIT);
            return element.findElement(by).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public boolean isElementPresentAndDisplayWithWait(By by, WebElement element) {
        try {
            return element.findElement(by).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    @Step("<by> by elementin olup olmadığını kontrol et yoksa <message> hata mesajını bastır")
    public void isExisByIdt(By by,String message){
        Assert.assertTrue(message,isElementPresentAndDisplay(by));
    }

    @Step("<className> className'li eleman sitede var mı ?")
    public void isElementPresentAndDisplayWithClassName(String className) {
        Assert.assertTrue("eleman sitede bulunmaadı", isElementPresentAndDisplayWithWait(By.className(className)));
    }

    @Step("<id> id'li eleman sitede var mı ?")
    public void isElementPresentAndDisplayWithId(String id) {
        Assert.assertTrue("eleman sitede bulunmaadı", isElementPresentAndDisplayWithWait(By.className(id)));
    }



    public void javaScriptClickerByCss(WebDriver driver, String css) {
        WebElement element = getElementBy(By.cssSelector(css));
        JavascriptExecutor jse = ((JavascriptExecutor) driver);
        jse.executeScript("var evt = document.createEvent('MouseEvents');" + "evt.initMouseEvent('click',true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0,null);" + "arguments[0].dispatchEvent(evt);", element);
    }

    public void waitForElement(WebDriver driver, int seconds, By elementBy) {
        WebDriverWait wait = new WebDriverWait(driver, seconds, 1000);
        wait.until(ExpectedConditions.presenceOfElementLocated(elementBy));
    }

    public static String Md5(String password) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(password.getBytes());
        byte byteData[] = md.digest();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < byteData.length; i++) {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }

    public boolean isClickable(WebElement element) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, 5);
            wait.until(ExpectedConditions.elementToBeClickable(element));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void moveToElement(WebElement element) {
        Actions action = new Actions(driver);
        action.moveToElement(element).perform();
    }

    public Object getRandomContent(List<?> contentList) {
        Random rand = new Random();
        int n = rand.nextInt(contentList.size());
        return contentList.get(n);
    }

    public boolean isExistElement(int sec, By by) {
        try {
            waitForElement(sec, by);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public WebElement waitForElement(int seconds, By elementBy) {
        WebDriverWait wait = new WebDriverWait(driver, seconds, 1000);
        return wait.until(ExpectedConditions.presenceOfElementLocated(elementBy));
    }

    public List<WebElement> waitForElements(int seconds, By elementBy) {
        WebDriverWait wait = new WebDriverWait(driver, seconds, 30);
        List<WebElement> element = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(elementBy));
        return element;
    }

    /**
     * The expected qualification visibility
     *
     * @param by
     * @param waitTime
     * @return
     */
    /**
     * The expected qualification visibility
     *
     * @param by
     * @param waitTime
     * @return
     */
    protected boolean isElementDisplayedWait(By by, int waitTime) {
        try {
            return waitForElementPresent(by, waitTime).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * wait for web element present
     *
     * @param by
     * @param timeOutInSeconds
     * @return
     */
    /**
     * wait for web element present
     *
     * @param by
     * @param timeOutInSeconds
     * @return
     */
    protected WebElement waitForElementPresent(By by, int timeOutInSeconds) {
        WebElement element;
        try {
            WebDriverWait waitSeconds = (WebDriverWait) new WebDriverWait(driver, timeOutInSeconds).ignoring(NoSuchElementException.class);
            element = waitSeconds.until(ExpectedConditions.presenceOfElementLocated(by));
            return element;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Expects the element appears
     *
     * @param by
     */
    /**
     * Expects the element appears
     *
     * @param by
     */
    protected void untilElementAppear(By by, int seconds) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, seconds, 30);
            wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    @Step("Select <by> <option>")
    public void selectOptionClick(By by, String value) {
        Select dropdown = new Select(findElement(by));
        dropdown.selectByVisibleText(value);
    }

    @Step("\"<className>\" 'li dropdown'dan <value> değerini seç")
    public void selectOptionByClassName(String className, String value) {
        untilElementAppear(By.className(className), MAX_WAIT);
        selectOptionClick(By.className(className), value);
    }

    public WebElement findElement(By by, int... index) {
        return driver.findElement(by);
    }

    public void waitSeconds(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // methoda aldıgı değer ile 0 arasında random sayı üretir
    public int random(int number) {
        int result = new Random().nextInt(number);
        return result;
    }

    @Step("<id> id'li eleman listesindenden rastgele birine tıkla")
    public void randomSelectionById(String id) {
        getElementsById(id).get(random(getElementsById(id).size())).click();
    }

    @Step("<className> className'li eleman listesinedeki rastgele bir elemente scroll ol ve tıkla")
    public void randomSelectionAndScrollByClassName(String className) {
        untilElementAppear(By.className(className), MAX_WAIT);
        WebElement element = getElementsBy(By.className(className)).get(random(getElementsBy(By.className(className)).size()));
        scrollElementIntoMiddle(element);
        element.click();
    }

    @Step("\"<className>\" class'li eleman listesindenden rastgele birine tıkla")
    public void randomSelectionByClassName(String classname) {
        getElementsByClassName(classname).get(random(getElementsByClassName(classname).size())).click();
    }

    @Step("<css> css'li eleman listesindenden rastgele birine tıkla")
    public void randomSelectionByCss(String css) {
        getElementsByCss(css).get(random(getElementsByCss(css).size())).click();
    }

    @Step("<id> id'li elemanın koordinatlarında sayfada scroll işlemi yap")
    protected void scrollTo(String id) {
        WebElement element = getElementById(id);
        String jsStmt = String.format("window.scrollTo(%d, %d);", element.getLocation().x, element.getLocation().y);
        executeJS(jsStmt, true);
    }

    @Step("\"<className>\" classname'li elemana scroll ol")
    public void scrollToElement(String className) {
        untilElementAppear(By.className(className), MAX_WAIT);
        scrollElementIntoMiddle(getElementBy(By.className(className)));
        sleep(1000);
    }

    @Step("\"<className>\" classname'li elemana js ile scroll ol")
    public void scrollToElementJS(String className) {
        untilElementAppear(By.className(className), MAX_WAIT);
        if (isElementPresentAndDisplayWithWait(By.className("fixedBottomBanner-close"))) {
            //System.out.println(getElementBy(By.className("fixedBottomBanner-close")).toString());
            clickObjectBy(By.className("fixedBottomBanner-close"));
        }

        if (isElementPresentAndDisplayWithWait(By.id("onesignal-popover-cancel-button"))) {
            //System.out.println(getElementBy(By.className("onesignal-popover-cancel-button")).toString());
            clickObjectBy(By.id("onesignal-popover-cancel-button"));
        }
        if (isElementPresentAndDisplayWithWait(By.className("cookieNotification-close"))) {
            clickObjectBy(By.className("cookieNotification-close"));
        }

        ScrollToElementWithJavascript(getElementBy(By.className(className)));
        sleep(1000);
    }

    @Step("\"<id>\" id li elemana js ile scroll ol")
    public void scrollToElementJSByid(String id) {
        untilElementAppear(By.className(id), 10);
        if (isElementPresentAndDisplayWithWait(By.className("fixedBottomBanner-close"))) {
            //System.out.println(getElementBy(By.className("fixedBottomBanner-close")).toString());
            clickObjectBy(By.className("fixedBottomBanner-close"));
        }

        if (isElementPresentAndDisplayWithWait(By.id("onesignal-popover-cancel-button"))) {
            //System.out.println(getElementBy(By.className("onesignal-popover-cancel-button")).toString());
            clickObjectBy(By.id("onesignal-popover-cancel-button"));
        }
        if (isElementPresentAndDisplayWithWait(By.className("cookieNotification-close"))) {
            clickObjectBy(By.className("cookieNotification-close"));
        }

        ScrollToElementWithJavascript(getElementBy(By.id(id)));
    }

    @Step("\"<id>\" id'li elemana scroll ol")
    public void scrollToElementById(String id) {
        untilElementAppear(By.id(id), MAX_WAIT);
        scrollElementIntoMiddle(getElementBy(By.id(id)));
        sleep(1000);
    }

    public void scrollElementIntoMiddle(WebElement element) {
        Actions actions = new Actions(driver);
        actions.moveToElement(element);
        actions.perform();
      /*TouchAction touchAction = new TouchAction(driver);
        touchAction.press(element.getLocation().getX(), element.getLocation().getY()).waitAction(2000).moveTo(element.getLocation().getX(), element.getLocation().getY()+100).release();
        touchAction.perform();
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView();", element);*/
    }


    public void scrollElementIntoTop(WebElement element) {
        String scrollElementIntoMiddle = "var viewPortHeight = Math.max(document.documentElement.clientHeight, window.innerHeight || 0);" + "var elementTop = arguments[0].getBoundingClientRect().top;" + "window.scrollBy(0, elementTop);";
        ((JavascriptExecutor) driver).executeScript(scrollElementIntoMiddle, element);
    }

    public void scrollElementIntoMiddleByJS(WebElement element) {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].scrollIntoView();", element);

      /*  String scrollElementIntoMiddle = "var viewPortHeight = Math.max(document.documentElement.clientHeight, window.innerHeight || 0);" + "var elementTop = arguments[0].getBoundingClientRect().top;" + "window.scrollBy(0, elementTop-(viewPortHeight/2));";
        ((JavascriptExecutor) driver).executeScript(scrollElementIntoMiddle, element);*/
    }


    @Step("\"<className>\" className listesinde \"<text>\" text'i olmalı.")
    public void checkTextInClassNameList(String className, String text) {
        Assert.assertTrue("'" + className + "' className listesi bulunamadı!", isElementPresentWithWait(By.className(className)));
        boolean condition = false;
        for (WebElement element : getElementsBy(By.className(className))) {
            if (element.getText().contains(text)) {
                condition = true;
                break;
            }
        }
        Assert.assertTrue("'" + text + "' texti '" + className + "' className listesinde bulunamadı!", condition);
    }

    public void arraySortIncreasing(ArrayList<Double> list) {
        Collections.sort(list);
        System.out.println("ListSortIncreasing");
    }

    /**
     * Arraylist sıralama
     */
    /**
     * Arraylist sıralama
     */
    public void arraySortDescending(ArrayList<Double> list) {
        Collections.sort(list, Collections.reverseOrder());
        System.out.println("ListSortDescending");
    }

    public ArrayList<Double> createDoubleList(String className) {
        ArrayList<Double> tempList = new ArrayList<>();
        for (WebElement element : getElementsBy(By.className(className))) {
            System.out.println("Price:" + Double.parseDouble(element.getText().replaceAll("[^0-9.]", "")));
            tempList.add(Double.parseDouble(element.getText().replaceAll("[^0-9.]", "")));
        }
        System.out.println("createDoubleList");
        return tempList;
    }

    //Sipari? Numaran?z: 24511079
    @Step("<className> nolu siparişi sil")
    public void orderdeletion(String className) {
        String order = getElementBy(By.className(className)).getText();
        order = order.substring(order.indexOf(": ") + 2, order.length());
        System.out.println("Order ID: " + order);
        deleteorder.deleteAnOrder(order);
    }

    @Step("Scroll yap")
    public void scrollJs() {
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        jse.executeScript("window.scrollBy(0,250)", "");
    }

    @Step("Scroll")
    public void scrollJs500() {
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        jse.executeScript("window.scrollBy(0,500)", "");
    }

    //Sipari? Numaran?z: 24511079
    @Step("<text> nolu siparişi sillll")
    public void orderDeletion(String text) {
        System.out.println("Order ID: " + text);
        deleteorder.deleteAnOrder(text);
    }

    public static String generateRandomString(int length) {
        // 9
        String alphabet = "0123456789";
        // 10
        int n = alphabet.length();
        String result = "";
        // 11
        Random r = new Random();
        for (int i = 0; i < length; i++) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(result);
            stringBuilder.append(alphabet.charAt(r.nextInt(n)));
            result = stringBuilder.toString();
        }
        return result;
    }

    public void ScrollToElementWithJavascript(WebElement element) {
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        jse.executeScript("arguments[0].scrollIntoView(true);", element);

    }

    public static boolean scroll_Page(WebElement webelement, int scrollPoints) {
        try {
            Actions dragger = new Actions(driver);
            // drag downwards
            int numberOfPixelsToDragTheScrollbarDown = 10;
            for (int i = 10; i < scrollPoints; i = i + numberOfPixelsToDragTheScrollbarDown) {
                dragger.moveToElement(webelement).clickAndHold().moveByOffset(0, numberOfPixelsToDragTheScrollbarDown).release(webelement).build().perform();
            }
            Thread.sleep(500);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String pattern(String patternedStr) {
        Pattern p = Pattern.compile("((\\d+)([\\\\.,])(\\d+))");
        Matcher m = p.matcher(patternedStr);

        Pattern p2 = Pattern.compile("((\\d+)([\\\\.,])(\\d+)([\\\\.,])(\\d+))");
        Matcher m2 = p2.matcher(patternedStr);

        if (m2.find()) {
            do {
                patternedStr = m2.group();
                if (patternedStr.contains(".")) {
                    patternedStr = patternedStr.replace(".", "");
                }
                patternedStr = patternedStr.replace(",", ".");
                System.out.println(" Patterned String " + patternedStr);

            } while (m2.find());
        } else {
            if (m.find()) {
                do {
                    patternedStr = m.group();
                    patternedStr = patternedStr.replace(",", ".");
                    System.out.println(" Patterned String " + patternedStr);
                } while (m.find());
            }

        }

        return patternedStr;
    }

    public void closeAllPopups() {
      /*  if (isElementPresentAndDisplayWithWait(By.className("fixedBottomBanner-close"))) {
            clickObjectBy(By.className("fixedBottomBanner-close"));
        }

        if (isElementPresentAndDisplayWithWait(By.id("onesignal-popover-cancel-button"))) {
            clickObjectBy(By.id("onesignal-popover-cancel-button"));
        }*/
        if (isElementPresentAndDisplayWithWait(By.className("cookieNotification-close"))) {
            clickObjectBy(By.className("cookieNotification-close"));
        }
    }
    public void makeDisplayNone(By by){
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].setAttribute('style', 'display:none')",getElementBy(by));
    }

}