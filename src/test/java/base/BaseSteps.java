package base;

import WebAutomationBase.helper.ElementHelper;
import WebAutomationBase.helper.StoreHelper;
import WebAutomationBase.model.ElementInfo;
import com.google.common.collect.ImmutableMap;
import com.thoughtworks.gauge.Step;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.LoggerFactory;
import org.slf4j.impl.Log4jLoggerAdapter;

import java.sql.Timestamp;import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class BaseSteps extends BaseTest{

    public static int DEFAULT_MAX_ITERATION_COUNT = 10;
    public static int DEFAULT_MILLISECOND_WAIT_AMOUNT = 100;

    private static Log4jLoggerAdapter logger = (Log4jLoggerAdapter) LoggerFactory
            .getLogger(BaseSteps.class);

    private static String SAVED_ATTRIBUTE;

    private Actions actions = new Actions(webDriver);

    public static final int DEFAULT_WAIT = 10;
    public static final int MIN_WAIT = 5;
    public static final int MAX_WAIT = 20;

    private WebElement findElement(String key){
        ElementInfo elementInfo = StoreHelper.INSTANCE.findElementInfoByKey(key);
        By infoParam = ElementHelper.getElementInfoToBy(elementInfo);
        WebDriverWait webDriverWait = new WebDriverWait(webDriver, 0);
        WebElement webElement = webDriverWait
                .until(ExpectedConditions.presenceOfElementLocated(infoParam));
        ((JavascriptExecutor) webDriver).executeScript(
                "arguments[0].scrollIntoView({behavior: 'smooth', block: 'center', inline: 'center'})",
                webElement);
        return webElement;
    }

    private List<WebElement> findElements(String key){
        ElementInfo elementInfo = StoreHelper.INSTANCE.findElementInfoByKey(key);
        By infoParam = ElementHelper.getElementInfoToBy(elementInfo);
        return webDriver.findElements(infoParam);
    }

    private void clickElement(WebElement element){
        element.click();
    }

    private void clickElementBy(String key){
        findElement(key).click();
    }

    private void hoverElement(WebElement element){
        actions.moveToElement(element).build().perform();
    }

    private void hoverElementBy(String key){
        WebElement webElement = findElement(key);
        actions.moveToElement(webElement).build().perform();
    }

    private void sendKeyESC(String key){
        findElement(key).sendKeys(Keys.ESCAPE);

    }

    private boolean isDisplayed(WebElement element){
        return element.isDisplayed();
    }

    private boolean isDisplayedBy(By by){
        return webDriver.findElement(by).isDisplayed();
    }

    private String getPageSource(){
        return webDriver.switchTo().alert().getText();
    }

    public static String getSavedAttribute(){
        return SAVED_ATTRIBUTE;
    }

    public String randomString(int stringLength){

        Random random = new Random();
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUWVXYZabcdefghijklmnopqrstuwvxyz0123456789".toCharArray();
        String stringRandom = "";
        for (int i = 0; i < stringLength; i++) {

            stringRandom = stringRandom + String.valueOf(chars[random.nextInt(chars.length)]);
        }

        return stringRandom;
    }

    public WebElement findElementWithKey(String key){
        return findElement(key);
    }

    public String getElementText(String key){
        return findElement(key).getText();
    }

    public String getElementAttributeValue(String key, String attribute){
        return findElement(key).getAttribute(attribute);
    }

    @Step("Print page source")
    public void printPageSource(){
        System.out.println(getPageSource());
    }

    public void javaScriptClicker(WebDriver driver, WebElement element){

        JavascriptExecutor jse = ((JavascriptExecutor) driver);
        jse.executeScript("var evt = document.createEvent('MouseEvents');"
                + "evt.initMouseEvent('click',true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0,null);"
                + "arguments[0].dispatchEvent(evt);", element);
    }

    @Step({"Wait <value> seconds",
            "<int> saniye bekle"})
    public void waitBySeconds(int seconds){
        try {
            logger.info(seconds + " saniye bekleniyor.");
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Step({"Wait <value> milliseconds",
            "<long> milisaniye bekle"})
    public void waitByMilliSeconds(long milliseconds){
        try {
            logger.info(milliseconds + " milisaniye bekleniyor.");
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Step({"Wait for element then click <key>",
            "Elementi bekle ve sonra t??kla <key>"})
    public void checkElementExistsThenClick(String key){
        getElementWithKeyIfExists(key);
        clickElement(key);
    }

    @Step({"Click to element <key>",
            "Elementine t??kla <key>"})
    public void clickElement(String key){
        if (!key.equals("")) {
            WebElement element = findElement(key);
          //  hoverElement(element);
           // waitByMilliSeconds(500);
            clickElement(element);
            logger.info(key + " elementine t??kland??.");
        }
    }

    @Step({"Wait for element then click with hover <key>",
            "Elementi bekle ve sonra hover ile t??kla <key>"})
    public void checkElementExistsThenClickWithHover(String key){
        getElementWithKeyIfExists(key);
        clickWithHoverElement(key);
    }

    @Step({"Click to element with hover <key>",
            "Elementine hover ile t??kla <key>"})
    public void clickWithHoverElement(String key){
        if (!key.equals("")) {
            WebElement element = findElement(key);
              hoverElement(element);
            // waitByMilliSeconds(500);
            clickElement(element);
            logger.info(key + " elementine t??kland??.");
        }
    }

    @Step({"Click to element <key> with focus",
            "<key> elementine focus ile t??kla"})
    public void clickElementWithFocus(String key){
        actions.moveToElement(findElement(key));
        actions.click();
        actions.build().perform();
        logger.info(key + " elementine focus ile t??kland??.");
    }

    @Step({"Check if element <key> exists",
            "Wait for element to load with key <key>",
            "Element var m?? kontrol et <key>",
            "Elementin y??klenmesini bekle <key>"})
    public WebElement getElementWithKeyIfExists(String key){
        WebElement webElement;
        int loopCount = 0;
        while (loopCount < DEFAULT_MAX_ITERATION_COUNT) {
            try {
                webElement = findElementWithKey(key);
                logger.info(key + " elementi bulundu.");
                return webElement;
            } catch (WebDriverException e) {
            }
            loopCount++;
            waitByMilliSeconds(DEFAULT_MILLISECOND_WAIT_AMOUNT);
        }
        Assert.fail("Element: '" + key + "' doesn't exist.");
        return null;
    }


    @Step({"Go to <url> address",
            "<url> adresine git"})
    public void goToUrl(String url){
        webDriver.get(url);
        logger.info(url + " adresine gidiliyor.");
    }

    @Step({"Wait for element to load with css <css>",
            "Elementin y??klenmesini bekle css <css>"})
    public void waitElementLoadWithCss(String css){
        int loopCount = 0;
        while (loopCount < DEFAULT_MAX_ITERATION_COUNT) {
            if (webDriver.findElements(By.cssSelector(css)).size() > 0) {
                logger.info(css + " elementi bulundu.");
                return;
            }
            loopCount++;
            waitByMilliSeconds(DEFAULT_MILLISECOND_WAIT_AMOUNT);
        }
        Assert.fail("Element: '" + css + "' doesn't exist.");
    }

    @Step({"Wait for element to load with xpath <xpath>",
            "Elementinin y??klenmesini bekle xpath <xpath>"})
    public void waitElementLoadWithXpath(String xpath){
        int loopCount = 0;
        while (loopCount < DEFAULT_MAX_ITERATION_COUNT) {
            if (webDriver.findElements(By.xpath(xpath)).size() > 0) {
                logger.info(xpath + " elementi bulundu.");
                return;
            }
            loopCount++;
            waitByMilliSeconds(DEFAULT_MILLISECOND_WAIT_AMOUNT);
        }
        Assert.fail("Element: '" + xpath + "' doesn't exist.");
    }

    @Step({"Check if element <key> exists else print message <message>",
            "Element <key> var m?? kontrol et yoksa hata mesaj?? ver <message>"})
    public void getElementWithKeyIfExistsWithMessage(String key, String message){
        ElementInfo elementInfo = StoreHelper.INSTANCE.findElementInfoByKey(key);
        By by = ElementHelper.getElementInfoToBy(elementInfo);

        int loopCount = 0;
        while (loopCount < DEFAULT_MAX_ITERATION_COUNT) {
            if (webDriver.findElements(by).size() > 0) {
                logger.info(key + " elementi bulundu.");
                return;
            }
            loopCount++;
            waitByMilliSeconds(DEFAULT_MILLISECOND_WAIT_AMOUNT);
        }
        Assert.fail(message);
    }

    @Step({"Check if element <key> not exists",
            "Element yok mu kontrol et <key>"})
    public void checkElementNotExists(String key){
        ElementInfo elementInfo = StoreHelper.INSTANCE.findElementInfoByKey(key);
        By by = ElementHelper.getElementInfoToBy(elementInfo);

        int loopCount = 0;
        while (loopCount < DEFAULT_MAX_ITERATION_COUNT) {
            if (webDriver.findElements(by).size() == 0) {
                logger.info(key + " elementinin olmad?????? kontrol edildi.");
                return;
            }
            loopCount++;
            waitByMilliSeconds(DEFAULT_MILLISECOND_WAIT_AMOUNT);
        }
        Assert.fail("Element '" + key + "' still exist.");
    }

    @Step({"Upload file in project <path> to element <key>",
            "Proje i??indeki <path> dosyay?? <key> elemente upload et"})
    public void uploadFile(String path, String key){
        String pathString = System.getProperty("user.dir") + "/";
        pathString = pathString + path;
        findElement(key).sendKeys(pathString);
        logger.info(path + " dosyas?? " + key + " elementine y??klendi.");
    }

    @Step({"Write value <text> to element <key>",
            "<text> textini <key> elemente yaz"})
    public void sendKeys(String text, String key){
        if (!key.equals("")) {
            findElement(key).sendKeys(text);
            logger.info(key + " elementine " + text + " texti yaz??ld??.");
        }
    }

    @Step({"Click with javascript to css <css>",
            "Javascript ile css t??kla <css>"})
    public void javascriptClickerWithCss(String css){
        Assert.assertTrue("Element bulunamad??", isDisplayedBy(By.cssSelector(css)));
        javaScriptClicker(webDriver, webDriver.findElement(By.cssSelector(css)));
        logger.info("Javascript ile " + css + " t??kland??.");
    }

    @Step({"Click with javascript to xpath <xpath>",
            "Javascript ile xpath t??kla <xpath>"})
    public void javascriptClickerWithXpath(String xpath){
        Assert.assertTrue("Element bulunamad??", isDisplayedBy(By.xpath(xpath)));
        javaScriptClicker(webDriver, webDriver.findElement(By.xpath(xpath)));
        logger.info("Javascript ile " + xpath + " t??kland??.");
    }

    @Step({"Check if current URL contains the value <expectedURL>",
            "??uanki URL <url> de??erini i??eriyor mu kontrol et"})
    public void checkURLContainsRepeat(String expectedURL){
        int loopCount = 0;
        String actualURL = "";
        while (loopCount < DEFAULT_MAX_ITERATION_COUNT) {
            actualURL = webDriver.getCurrentUrl();

            if (actualURL != null && actualURL.contains(expectedURL)) {
                logger.info("??uanki URL" + expectedURL + " de??erini i??eriyor.");
                return;
            }
            loopCount++;
            waitByMilliSeconds(DEFAULT_MILLISECOND_WAIT_AMOUNT);
        }
        Assert.fail(
                "Actual URL doesn't match the expected." + "Expected: " + expectedURL + ", Actual: "
                        + actualURL);
    }

    @Step({"Send TAB key to element <key>",
            "Elemente TAB keyi yolla <key>"})
    public void sendKeyToElementTAB(String key){
        findElement(key).sendKeys(Keys.TAB);
        logger.info(key + " elementine TAB keyi yolland??.");
    }

    @Step({"Send BACKSPACE key to element <key>",
            "Elemente BACKSPACE keyi yolla <key>"})
    public void sendKeyToElementBACKSPACE(String key){
        findElement(key).sendKeys(Keys.BACK_SPACE);
        logger.info(key + " elementine BACKSPACE keyi yolland??.");
    }

    @Step({"Send ESCAPE key to element <key>",
            "Elemente ESCAPE keyi yolla <key>"})
    public void sendKeyToElementESCAPE(String key){
        findElement(key).sendKeys(Keys.ESCAPE);
        logger.info(key + " elementine ESCAPE keyi yolland??.");
    }

    @Step({"Check if element <key> has attribute <attribute>",
            "<key> elementi <attribute> niteli??ine sahip mi"})
    public void checkElementAttributeExists(String key, String attribute){
        WebElement element = findElement(key);
        int loopCount = 0;
        while (loopCount < DEFAULT_MAX_ITERATION_COUNT) {
            if (element.getAttribute(attribute) != null) {
                logger.info(key + " elementi " + attribute + " niteli??ine sahip.");
                return;
            }
            loopCount++;
            waitByMilliSeconds(DEFAULT_MILLISECOND_WAIT_AMOUNT);
        }
        Assert.fail("Element DOESN't have the attribute: '" + attribute + "'");
    }

    @Step({"Check if element <key> not have attribute <attribute>",
            "<key> elementi <attribute> niteli??ine sahip de??il mi"})
    public void checkElementAttributeNotExists(String key, String attribute){
        WebElement element = findElement(key);

        int loopCount = 0;

        while (loopCount < DEFAULT_MAX_ITERATION_COUNT) {
            if (element.getAttribute(attribute) == null) {
                logger.info(key + " elementi " + attribute + " niteli??ine sahip olmad?????? kontrol edildi.");
                return;
            }
            loopCount++;
            waitByMilliSeconds(DEFAULT_MILLISECOND_WAIT_AMOUNT);
        }
        Assert.fail("Element STILL have the attribute: '" + attribute + "'");
    }

    @Step({"Check if <key> element's attribute <attribute> equals to the value <expectedValue>",
            "<key> elementinin <attribute> niteli??i <value> de??erine sahip mi"})
    public void checkElementAttributeEquals(String key, String attribute, String expectedValue){
        WebElement element = findElement(key);

        String actualValue;
        int loopCount = 0;
        while (loopCount < DEFAULT_MAX_ITERATION_COUNT) {
            actualValue = element.getAttribute(attribute).trim();
            if (actualValue.equals(expectedValue)) {
                logger.info(
                        key + " elementinin " + attribute + " niteli??i " + expectedValue + " de??erine sahip.");
                return;
            }
            loopCount++;
            waitByMilliSeconds(DEFAULT_MILLISECOND_WAIT_AMOUNT);
        }
        Assert.fail("Element's attribute value doesn't match expected value");
    }

    @Step({"Check if <key> element's attribute <attribute> contains the value <expectedValue>",
            "<key> elementinin <attribute> niteli??i <value> de??erini i??eriyor mu"})
    public void checkElementAttributeContains(String key, String attribute, String expectedValue){
        WebElement element = findElement(key);

        String actualValue;
        int loopCount = 0;
        while (loopCount < DEFAULT_MAX_ITERATION_COUNT) {
            actualValue = element.getAttribute(attribute).trim();
            if (actualValue.contains(expectedValue)) {
                return;
            }
            loopCount++;
            waitByMilliSeconds(DEFAULT_MILLISECOND_WAIT_AMOUNT);
        }
        Assert.fail("Element's attribute value doesn't contain expected value");
    }

    @Step({"Write <value> to <attributeName> of element <key>",
            "<value> de??erini <attribute> niteli??ine <key> elementi i??in yaz"})
    public void setElementAttribute(String value, String attributeName, String key){
        String attributeValue = findElement(key).getAttribute(attributeName);
        findElement(key).sendKeys(attributeValue, value);
    }

    @Step({"Write <value> to <attributeName> of element <key> with Js",
            "<value> de??erini <attribute> niteli??ine <key> elementi i??in JS ile yaz"})
    public void setElementAttributeWithJs(String value, String attributeName, String key){
        WebElement webElement = findElement(key);
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        js.executeScript("arguments[0].setAttribute('" + attributeName + "', '" + value + "')",
                webElement);
    }

    @Step({"Clear text of element <key>",
            "<key> elementinin text alan??n?? temizle"})
    public void clearInputArea(String key){
        findElement(key).clear();
    }

    @Step({"Clear text of element <key> with BACKSPACE",
            "<key> elementinin text alan??n?? BACKSPACE ile temizle"})
    public void clearInputAreaWithBackspace(String key){
        WebElement element = findElement(key);
        element.clear();
        element.sendKeys("a");
        actions.sendKeys(Keys.BACK_SPACE).build().perform();
    }

    @Step({"Save attribute <attribute> value of element <key>",
            "<attribute> niteli??ini sakla <key> elementi i??in"})
    public void saveAttributeValueOfElement(String attribute, String key){
        SAVED_ATTRIBUTE = findElement(key).getAttribute(attribute);
        System.out.println("Saved attribute value is: " + SAVED_ATTRIBUTE);
    }

    @Step({"Write saved attribute value to element <key>",
            "Kaydedilmi?? niteli??i <key> elementine yaz"})
    public void writeSavedAttributeToElement(String key){
        findElement(key).sendKeys(SAVED_ATTRIBUTE);
    }

    @Step({"Check if element <key> contains text <expectedText>",
            "<key> elementi <text> de??erini i??eriyor mu kontrol et"})
    public void checkElementContainsText(String key, String expectedText){
        Boolean containsText = findElement(key).getText().contains(expectedText);
        Assert.assertTrue("Expected text is not contained", containsText);
    }

    @Step({"Write random value to element <key>",
            "<key> elementine random de??er yaz"})
    public void writeRandomValueToElement(String key){
        findElement(key).sendKeys(randomString(15));
    }

    @Step({"Write random value to element <key> starting with <text>",
            "<key> elementine <text> de??eri ile ba??layan random de??er yaz"})
    public void writeRandomValueToElement(String key, String startingText){
        String randomText = startingText + randomString(15);
        findElement(key).sendKeys(randomText);
    }

    @Step({"Print element text by css <css>",
            "Elementin text de??erini yazd??r css <css>"})
    public void printElementText(String css){
        System.out.println(webDriver.findElement(By.cssSelector(css)).getText());
    }

    @Step({"Write value <string> to element <key> with focus",
            "<string> de??erini <key> elementine focus ile yaz"})
    public void sendKeysWithFocus(String text, String key){
        actions.moveToElement(findElement(key));
        actions.click();
        actions.sendKeys(text);
        actions.build().perform();
    }

    @Step({"Refresh page",
            "Sayfa yenilenir"})
    public void refreshPage(){
        webDriver.navigate().refresh();
        waitByMilliSeconds(5);
    }

    @Step({"Change page zoom to <value>%",
            "Sayfan??n zoom de??erini de??i??tir <value>%"})
    public void chromeZoomOut(String value){
        JavascriptExecutor jsExec = (JavascriptExecutor) webDriver;
        jsExec.executeScript("document.body.style.zoom = '" + value + "%'");
    }

    @Step({"Open new tab",
            "Yeni sekme a??"})
    public void chromeOpenNewTab(){
        ((JavascriptExecutor) webDriver).executeScript("window.open()");
    }

    @Step({"Focus on tab number <number>",
            "<number> numaral?? sekmeye odaklan"})//Starting from 1
    public void chromeFocusTabWithNumber(int number){
        ArrayList<String> tabs = new ArrayList<>(webDriver.getWindowHandles());
        webDriver.switchTo().window(tabs.get(number - 1));
    }

    @Step({"Focus on last tab",
            "Son sekmeye odaklan"})
    public void chromeFocusLastTab(){
        ArrayList<String> tabs = new ArrayList<>(webDriver.getWindowHandles());
        webDriver.switchTo().window(tabs.get(tabs.size() - 1));
    }

    @Step({"Focus on frame with <key>",
            "Frame'e odaklan <key>"})
    public void chromeFocusFrameWithNumber(String key){
        WebElement webElement = findElement(key);
        webDriver.switchTo().frame(webElement);
    }

    @Step({"Accept Chrome alert popup",
            "Chrome uyar?? popup'??n?? kabul et"})
    public void acceptChromeAlertPopup(){
        webDriver.switchTo().alert().accept();
    }


    //----------------------SONRADAN YAZILANLAR-----------------------------------

    private JavascriptExecutor getJSExecutor() {
        return (JavascriptExecutor) webDriver;
    }
    private Object executeJS(String script, boolean wait) {
        return wait ? getJSExecutor().executeScript(script, "") : getJSExecutor().executeAsyncScript(script, "");
    }
    private void scrollTo(int x, int y) {
        String script = String.format("window.scrollTo(%d, %d);", x, y);
        executeJS(script, true);
    }
    public WebElement scrollToElementToBeVisible(String key) {
        ElementInfo elementInfo = StoreHelper.INSTANCE.findElementInfoByKey(key);
        WebElement webElement =webDriver.findElement(ElementHelper.getElementInfoToBy(elementInfo));
        if (webElement != null) {
            scrollTo(webElement.getLocation().getX(), webElement.getLocation().getY() - 70);
        }
        return webElement;
    }
    @Step({"<key> alan??na kayd??r"})
    public void scrollToElement(String key) {
        scrollToElementToBeVisible(key);
    }


    @Step({"<key> alan??na js ile kayd??r"})
    public void scrollToElementWithJs(String key) {
        ElementInfo elementInfo = StoreHelper.INSTANCE.findElementInfoByKey(key);
        WebElement element = webDriver.findElement(ElementHelper.getElementInfoToBy(elementInfo));
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    @Step({"<key> alan??na js ile t??kla"})
    public void clickToElementWithJs(String key) {
        ElementInfo elementInfo = StoreHelper.INSTANCE.findElementInfoByKey(key);
        WebElement element = webDriver.findElement(ElementHelper.getElementInfoToBy(elementInfo));
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", element);
        logger.info(key + " elementine t??kland??.");
    }
    @Step({"Asag??ya js ile kaydir"})
    public void scrollWithJs(){
        ((JavascriptExecutor) webDriver).executeScript("window.scrollBy(0,250)");
        //((JavascriptExecutor) webDriver).executeScript("arguments[0].window.scrollBy(0,250);");
        logger.info(" a??a???? do??ru kayd??r??ld??.");
    }

    @Step({"<length> uzunlugunda random bir kelime ??ret ve <saveKey> olarak sakla"})
    public void createRandomString(int length, String saveKey) {
        StoreHelper.INSTANCE.saveValue(saveKey,  randomString(length ));
    }



    @Step({"<key> li elementi bul ve de??erini <saveKey> saklanan degeri yazdir",
            "Find element by <key> and compare saved key <saveKey>"})
    public void equalsSendTextByKey(String key, String saveKey) throws InterruptedException {
        WebElement element = null;
        int waitVar = 0;
        element = findElementWithKey(key);
        while (true) {
            if (element.isDisplayed()) {
                logger.info("WebElement is found at: " + waitVar + " second.");
                element.clear();
                StoreHelper.INSTANCE.getValue(saveKey);
                element.sendKeys(StoreHelper.INSTANCE.getValue(saveKey));

                break;
            } else {
                waitVar = waitVar + 1;
                Thread.sleep(1000);
                if (waitVar == 20) {
                    throw new NullPointerException(String.format("by = %s Web element list not found"));
                } else {
                }
            }
        }
    }

    private Long getTimestamp() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return (timestamp.getTime());
    }
    @Step({"<key> li elementi bul, temizle ve rasgele  email de??erini yaz",
            "Find element by <key> clear and send keys  random email"})
    public void RandomeMail(String key){
        Long timestamp = getTimestamp();
        WebElement webElement = findElementWithKey(key);
        webElement.clear();
        webElement.sendKeys("testotomasyon" + timestamp + "@testinium.com");

    }

    @Step({"<key> li elementi bul, temizle ve rasgele isim de??erini yaz",
            "Find element by <key> clear and send keys random isim"})
    public void RandomeName(String key){
        Long timestamp = getTimestamp();
        WebElement webElement = findElementWithKey(key);
        webElement.clear();
        webElement.sendKeys("testotomasyon" + timestamp + "@testinium.com");

    }

    @Step("Rastgele telefon no ??ret")
    public String rastgelTelNoGelsin( ) {
        Vector<Integer> array = new Vector<Integer>();
        Random randomGenerator = new Random();
        array.add(new Integer(1 + randomGenerator.nextInt(9)));
        for (int i=1;i<9;i++) array.add(randomGenerator.nextInt(10));
        int t1 = 0;
        for (int i=0;i<9;i+=2) t1 += array.elementAt(i);
        int t2 = 0;
        for (int i=1;i<8;i+=2) t2 += array.elementAt(i);
        int x = ((t1 * 7 )- t2) % 10;
        array.add(new Integer(x));
        x=0;
        for(int i=0;i<10;i++) x+= array.elementAt(i);
        x= x % 10;
        array.add(new Integer(x));
        String res = "";
        for(int i=0;i<10;i++) res = res + Integer.toString(array.elementAt(i));
        return res;
    }
    @Step("Telefon noyu <key> elementine yaz")
    public void setRandomTelno(String key){
        String rastgeleTcno= rastgelTelNoGelsin();
        sendKeys(rastgeleTcno,key);
    }
    public static String randomNum(int stringLength) {
        Random random = new Random();
        char[] chars = "1234567890".toCharArray();
        String stringRandom = "";
        for (int i = 0; i < stringLength; i++) {
            stringRandom = stringRandom + String.valueOf(chars[random.nextInt(chars.length)]);
        }
        return stringRandom;
    }

    @Step("<key> li elementine random telefon numaras?? yaz")
    public void randomTel(String key){

        String phoneNum = "98"+randomNum(7);
        findElementWithKey(key).sendKeys(phoneNum);

    }

    @Step({"<key> li elementi bul ve de??erini <saveKey> olarak sakla",
            "Find element by <key> and save text <saveKey>"})
    public void saveTextByKey(String key, String saveKey) throws InterruptedException {
        Thread.sleep(1000);
        StoreHelper.INSTANCE.saveValue(saveKey, findElementWithKey(key).getText());
        Thread.sleep(2000);

    }
    @Step({"<key> li elementi bul ve de??erini <saveKey> saklanan de??eri i??eriyor mu kontrol et",
            "Find element by <key> and compare saved key <saveKey>"})
    public void equalsSaveTextByKeyContain(String key, String saveKey) {
        Assert.assertTrue(StoreHelper.INSTANCE.getValue(saveKey).contains(findElementWithKey(key).getText()));
    }


    @Step({"<key> li elementi bul ve de??erini <saveKey> saklanan de??er ile kar????la??t??r ve de??i??iklik oldugunu dogrula",
            "Find element by <key> and compare saved key <saveKey>"})
    public void equalsSaveTextByKeyNotequal(String key, String saveKey) {
        Assert.assertNotEquals(StoreHelper.INSTANCE.getValue(saveKey), findElementWithKey(key).getText());
    }

    @Step({"<key> li elementi bul, temizle ve <text> de??erini yaz",
            "Find element by <key> clear and send keys <text>"})
    public void sendKeysByKey(String key, String text) {

        WebElement webElement = findElementWithKey(key);
        webElement.clear();
        webElement.sendKeys(text);
    }

    @Step({"<key> li elementi bul, temizle",
            "Find element by <key> clear "})
    public void sendKeysByKey2(String key) {
        WebElement webElement = findElementWithKey(key);
        webElement.clear();
    }

    @Step({"<key> li elementi bul ve de??erini <saveKey> saklanan de??er ile kar????la??t??r",
            "Find element by <key> and compare saved key <saveKey>"})
    public void equalsSaveTextByKey(String key, String saveKey) {
        Assert.assertEquals(StoreHelper.INSTANCE.getValue(saveKey), findElementWithKey(key).getText());
    }

    public String randomName(int stringLength){
        Random random = new Random();
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUWVXYZabcdefghijklmnopqrstuwvxyz".toCharArray();
        String stringRandom = "";
        for (int i = 0; i < stringLength; i++) {
            stringRandom = stringRandom + String.valueOf(chars[random.nextInt(chars.length)]);
        }
        return stringRandom;
    }
    @Step({"<key> elementine random isim yaz"})
    public void writeRandomNameToElement(String key){
        findElement(key).sendKeys(randomName(3));
    }

    @Step({"<key> li elementin de??eri <text> e e??itli??ini kontrol et",
            "Find element by <key> and text equals <text>"})
    public void equalsTextByKey(String key, String text) {
        Assert.assertEquals(text, findElementWithKey(key).getText());
    }



    @Step({"Elementin y??klenmesini bekle ve t??kla <key>"})
    public WebElement getElementWithKeyIfExists2(String key){
        WebElement webElement;
        int loopCount = 0;
        while (loopCount < DEFAULT_MAX_ITERATION_COUNT) {
            try {
                webElement = findElementWithKey(key);
                logger.info(key + " elementi bulundu.");
                actions.moveToElement(findElement(key));
                actions.click();
                actions.build().perform();
                logger.info(key + " elementine focus ile t??kland??.");
                return webElement;
            } catch (WebDriverException e) {
            }
            loopCount++;
            waitByMilliSeconds(DEFAULT_MILLISECOND_WAIT_AMOUNT);
        }
        Assert.fail("Element: '" + key + "' doesn't exist.");
        return null;
    }

    @Step({"Wait for element then click <key> try",
            "Elementi bekle ve sonra t??kla <key> try"})
    public void checkElementExistsThenClickTryCatch(String key){
        try {
            clickElement(key);
        } catch (Exception e){
            logger.info("Tek test ??ubesi var");
        }

    }

    int waitVar = 0;
    @Step("<key> li elementi bulana kadar bekle ve <text> de??erini yazd??r")
    public void waitUntilFindElementAndSendKeysWithkey(String key, String text) throws InterruptedException {
        WebElement element = null;
        waitVar = 0;
        element = findElementWithKey(key);
        while (true) {
            if (element.isDisplayed()) {
                logger.info("WebElement is found at: " + waitVar + " second.");
                element.sendKeys(text);
                break;
            } else {
                waitVar = waitVar + 1;
                Thread.sleep(1000);
                if (waitVar == 20) {
                    throw new NullPointerException(String.format("by = %s Web element list not found"));
                } else {
                }
            }
        }
    }


    @Step("<key> li elementi bul ve <text> de??erini tek tek yaz")
    public void sendKeysValueOfClear(String key, String text){
        WebElement me = findElementWithKey(key);
        me.clear();
        for(int i = 0 ; i<text.length();i++){
            char c  = text.charAt(i);
            me.sendKeys(String.valueOf(c));
        }
        System.out.println("'" + text + "' texti '" + key + "' elementine yaz??ld??");

    }

    @Step({"<key> li elementi bul, temizle ve rasgele  email de??erini yaz, emaili <savekey> olarak sakla",
    })
    public void RandomeMailAndSave(String key, String saveKey){
        Long timestamp = getTimestamp();
        WebElement webElement = findElementWithKey(key);
        webElement.clear();
        webElement.sendKeys("testTestinium" + timestamp + "@otomasyon.com");
        String randomMail= "testTestinium" + timestamp + "@otomasyon.com";
        StoreHelper.INSTANCE.saveValue(saveKey,randomMail);

    }

    @Step({"<key> li elementi bul ve varsa dokun", "Click element by <key> if exist"})
    public void existTapByKey(String key) {

        WebElement element = null;
        try {
            element = findElementWithKey(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (element != null) {
            element.click();
        }
    }

    @Step("<key> li buton varsa t??kla")
    public void existButtonClick(String key){
        try {
            waitBySeconds(2);
            clickElement(key);
        }catch (Exception e){
            logger.info("Buton yok veya aktif de??il!!");
        }
    }

    @Step("<key> li adres tipi se??ilir")
    public void firstServiceTypeChange(String key){
        if(findElements(key).size()>0){
            getElementWithKeyIfExists(key);
            clickElement(key);
            waitBySeconds(2);
            //Gel Al
            if(findElements("sadeceAcikSubeleriGosterCheckbox").size()>0){
                getElementWithKeyIfExists("secinizDropdown");
                clickElement("secinizDropdown");
                getElementWithKeyIfExists("ilIstanbul");
                clickElement("ilIstanbul");
                waitBySeconds(2);
                getElementWithKeyIfExists("secinizDropdown");
                clickElement("secinizDropdown");
                getElementWithKeyIfExists("ilceAdalar");
                clickElement("ilceAdalar");
                waitBySeconds(4);
                getElementWithKeyIfExists("secinizDropdown");
                clickElement("secinizDropdown");
                getElementWithKeyIfExists("mahalleGelAlBurgazada");
                clickElement("mahalleGelAlBurgazada");
                waitBySeconds(4);
                getElementWithKeyIfExists("sadeceAcikSubeleriGosterCheckbox");
                clickElement("sadeceAcikSubeleriGosterCheckbox");
                getElementWithKeyIfExists("gelAlSubeleriGosterButon");
                clickElement("gelAlSubeleriGosterButon");
                getElementWithKeyIfExists("gelAlListelemeIlkSube");
                clickElement("gelAlListelemeIlkSube");
                waitBySeconds(2);
                getElementWithKeyIfExists("seciliSubeIleDevamEtButon");
                clickElement("seciliSubeIleDevamEtButon");
                waitBySeconds(3);
            }
            //Adrese Teslim
            else if(findElements("adreseTeslimGeriButonu").size()>0){
                getElementWithKeyIfExists("secinizDropdown");
                clickElement("secinizDropdown");
                waitBySeconds(4);
                getElementWithKeyIfExists("ilIstanbul");
                clickElement("ilIstanbul");
                waitBySeconds(4);
                getElementWithKeyIfExists("secinizDropdown");
                clickElement("secinizDropdown");
                waitBySeconds(4);
                getElementWithKeyIfExists("ilceAdalar");
                clickElement("ilceAdalar");
                waitBySeconds(4);
                try {
                    getElementWithKeyIfExists("secinizDropdown");
                    clickElement("secinizDropdown");
                    waitBySeconds(4);
                    getElementWithKeyIfExists("mahalleBurgazada");
                    clickElement("mahalleBurgazada");
                    waitBySeconds(4);
                } catch (Exception e){
                    
                }

                if(findElements("secinizDropdown").size()>0){
                    getElementWithKeyIfExists("secinizDropdown");
                    clickElement("secinizDropdown");
                    waitBySeconds(4);
                    getElementWithKeyIfExists("adreseTeslimAyisigiSokak");
                    clickElement("adreseTeslimAyisigiSokak");
                }
                waitBySeconds(4);
                getElementWithKeyIfExists("seciliAdresIleDevamEtbuton");
                clickElement("seciliAdresIleDevamEtbuton");
            }
            else if(findElements("kayitliAdresleriniz").size() > 0) {
                getElementWithKeyIfExists("menuAdreslerimListeIlkAdres");
                clickElement("menuAdreslerimListeIlkAdres");
                waitBySeconds(4);
                getElementWithKeyIfExists("kay??tl??AdreslerSeciliAdresIleDevamEtButon");
                clickElement("kay??tl??AdreslerSeciliAdresIleDevamEtButon");
                waitBySeconds(4);
            }
            else {
            }
        }
        else {
        }
    }

    @Step("Se??imi <text> olarak de??i??tir")
    public void change(String text){

        switch (text){

            case "GelAl":

                waitBySeconds(2);

                if(webDriver.findElements(By.xpath("//span[contains(text(),'Adrese Teslim')]")).size()>0) {

                    getElementWithKeyIfExists("anasayfaServisTipiSecimiButon");
                    clickElement("anasayfaServisTipiSecimiButon");

                    getElementWithKeyIfExists("gelAlButon");
                    clickElement("gelAlButon");
                } else {

                }
                break;

            case "AdreseTeslim":

                waitBySeconds(2);

                if(webDriver.findElements(By.xpath("//span[contains(text(),'Gel Al')]")).size()>0)
                {
                    getElementWithKeyIfExists("anasayfaServisTipiSecimiButon");
                    clickElement("anasayfaServisTipiSecimiButon");

                    waitBySeconds(4);

                    getElementWithKeyIfExists("adreseTeslimButon");
                    clickElement("adreseTeslimButon");

                    waitBySeconds(4);

                    getElementWithKeyIfExists("secinizDropdown");
                    clickElement("secinizDropdown");

                    waitBySeconds(4);

                    getElementWithKeyIfExists("ilIstanbul");
                    clickElement("ilIstanbul");

                    waitBySeconds(4);

                    getElementWithKeyIfExists("secinizDropdown");
                    clickElement("secinizDropdown");

                    waitBySeconds(4);

                    getElementWithKeyIfExists("ilceAdalar");
                    clickElement("ilceAdalar");

                    waitBySeconds(4);

                    getElementWithKeyIfExists("secinizDropdown");
                    clickElement("secinizDropdown");

                    waitBySeconds(4);

                    getElementWithKeyIfExists("mahalleBurgazada");
                    clickElement("mahalleBurgazada");

                    waitBySeconds(4);

                    getElementWithKeyIfExists("seciliAdresIleDevamEtbuton");
                    clickElement("seciliAdresIleDevamEtbuton");
                }
                else {

                }
                break;

            case "GelAlAdresSecimli":

                waitBySeconds(2);

                if(webDriver.findElements(By.xpath("//span[contains(text(),'Adrese Teslim')]")).size()>0) {

                    getElementWithKeyIfExists("secinizDropdown");
                    clickElement("secinizDropdown");

                    getElementWithKeyIfExists("il??stanbul");
                    clickElement("il??stanbul");

                    waitBySeconds(2);

                    getElementWithKeyIfExists("secinizDropdown");
                    clickElement("secinizDropdown");

                    getElementWithKeyIfExists("ilceAdalar");
                    clickElement("ilceAdalar");

                    waitBySeconds(4);

                    getElementWithKeyIfExists("secinizDropdown");
                    clickElement("secinizDropdown");

                    getElementWithKeyIfExists("mahalleGelAlBurgazada");
                    clickElement("mahalleGelAlBurgazada");

                    waitBySeconds(4);

                    getElementWithKeyIfExists("sadeceAcikSubeleriGosterCheckbox");
                    clickElement("sadeceAcikSubeleriGosterCheckbox");

                    getElementWithKeyIfExists("gelAlSubeleriGosterButon");
                    clickElement("gelAlSubeleriGosterButon");

                    getElementWithKeyIfExists("gelAlListelemeIlkSube");
                    clickElement("gelAlListelemeIlkSube");

                    waitBySeconds(2);

                    getElementWithKeyIfExists("seciliSubeIleDevamEtButon");
                    clickElement("seciliSubeIleDevamEtButon");

                    waitBySeconds(3);

                } else {

                }
                break;

            default:
                break;
        }
    }

    @Step({"Hide keyboard",
           "Klavyeyi kapat"})
    public static void HideKeyboard(){
        webDriver.hideKeyboard();
    }

    @Step("Kampanya urun secimi yap??l??r")
    public void kampanyaSec(){
        waitBySeconds(10);
        if(findElements("kampanyalarSecinizDropdown").size() > 0){
            logger.info("Kampanya 1. ??r??n se??iliyor!!!");
            waitBySeconds(4);
            getElementWithKeyIfExists("kampanyalarSecinizDropdown");
            clickElement("kampanyalarSecinizDropdown");
            //scrollToElementWithJs("kampanyalarSecinizBirinciDropdown");
            //clickToElementWithJs("kampanyalarSecinizBirinciDropdown");
            waitBySeconds(2);
            getElementWithKeyIfExists("kampanyalarIlkUrunIlkPizzaiOS");
            clickElement("kampanyalarIlkUrunIlkPizzaiOS");
            waitBySeconds(2);

        }
        if(findElements("kampanyalarSecinizDropdown").size() > 0){
            logger.info("Kampanya 2. ??r??n se??iliyor!!!");
            waitBySeconds(4);
            getElementWithKeyIfExists("kampanyalarSecinizDropdown");
            clickElement("kampanyalarSecinizDropdown");
            waitBySeconds(2);
            getElementWithKeyIfExists("kampanyalarIk??nc??UrunIlkPizza");
            clickElement("kampanyalarIk??nc??UrunIlkPizza");
            waitBySeconds(2);

        }
        if(findElements("kampanyalarSecinizDropdown").size() > 0){
            logger.info("Kampanya 3. ??r??n se??iliyor!!!");
            waitBySeconds(4);
            scrollToElementWithJs("kampanyalarSecinizDropdown");
            getElementWithKeyIfExists("kampanyalarSecinizDropdown");
            clickElement("kampanyalarSecinizDropdown");
            waitBySeconds(2);
            getElementWithKeyIfExists("kampanyalarUcuncuUrunIlkPizza");
            clickElement("kampanyalarUcuncuUrunIlkPizza");
            waitBySeconds(2);

        }
        if(findElements("kampanyalarSecinizDropdown").size() > 0){
            logger.info("Kampanya 4. ??r??n se??iliyor!!!");
            waitBySeconds(4);
            scrollToElementWithJs("kampanyalarSecinizDropdown");
            getElementWithKeyIfExists("kampanyalarSecinizDropdown");
            clickElement("kampanyalarSecinizDropdown");
            waitBySeconds(2);
            getElementWithKeyIfExists("kampanyalarDorduncuUrunIlkPizza");
            clickElement("kampanyalarDorduncuUrunIlkPizza");
            waitBySeconds(2);

        }
        if(findElements("kampanyalarSecinizDropdown").size() > 0){
            logger.info("Kampanya 5. ??r??n se??iliyor!!!");
            waitBySeconds(4);
            scrollToElementWithJs("kampanyalarSecinizDropdown");
            getElementWithKeyIfExists("kampanyalarSecinizDropdown");
            clickElement("kampanyalarSecinizDropdown");
            waitBySeconds(2);
            getElementWithKeyIfExists("kampanyalarBesinciUrunIlkPizza");
            clickElement("kampanyalarBesinciUrunIlkPizza");
            waitBySeconds(2);

        }
    }

    @Step("Kampanya urun secimi yap??l??r Android")
    public void kampanyaSecAndroid(){
        waitBySeconds(10);
        if(findElements("kampanyalarSecinizDropdown").size() > 0){
            logger.info("Kampanya 1. ??r??n se??iliyor!!!");
            waitBySeconds(4);
            getElementWithKeyIfExists("kampanyalarSecinizBirinciDropdown");
            clickElement("kampanyalarSecinizBirinciDropdown");
            //scrollToElementWithJs("kampanyalarSecinizBirinciDropdown");
            //clickToElementWithJs("kampanyalarSecinizBirinciDropdown");
            waitBySeconds(2);
            getElementWithKeyIfExists("kampanyalarIlkUrunIlkPizza");
            clickElement("kampanyalarIlkUrunIlkPizza");
            waitBySeconds(2);

        }
        if(findElements("kampanyalarSecinizDropdown").size() > 0){
            logger.info("Kampanya 2. ??r??n se??iliyor!!!");
            waitBySeconds(4);
            getElementWithKeyIfExists("kampanyalarSecinizIkinciDropdown");
            clickElement("kampanyalarSecinizIkinciDropdown");
            waitBySeconds(2);
            getElementWithKeyIfExists("kampanyalarIk??nc??UrunIlkPizza");
            clickElement("kampanyalarIk??nc??UrunIlkPizza");
            waitBySeconds(2);

        }
        if(findElements("kampanyalarSecinizDropdown").size() > 0){
            logger.info("Kampanya 3. ??r??n se??iliyor!!!");
            waitBySeconds(4);
            scrollToElementWithJs("kampanyalarSecinizIkinciDropdown");
            getElementWithKeyIfExists("kampanyalarSecinizUcuncuDropdown");
            clickElement("kampanyalarSecinizUcuncuDropdown");
            waitBySeconds(2);
            getElementWithKeyIfExists("kampanyalarUcuncuUrunIlkPizza");
            clickElement("kampanyalarUcuncuUrunIlkPizza");
            waitBySeconds(2);

        }
        if(findElements("kampanyalarSecinizDropdown").size() > 0){
            logger.info("Kampanya 4. ??r??n se??iliyor!!!");
            waitBySeconds(4);
            scrollToElementWithJs("kampanyalarSecinizDorduncuDropdown");
            getElementWithKeyIfExists("kampanyalarSecinizDorduncuDropdown");
            clickElement("kampanyalarSecinizDorduncuDropdown");
            waitBySeconds(2);
            getElementWithKeyIfExists("kampanyalarDorduncuUrunIlkPizza");
            clickElement("kampanyalarDorduncuUrunIlkPizza");
            waitBySeconds(2);

        }
        if(findElements("kampanyalarSecinizDropdown").size() > 0){
            logger.info("Kampanya 5. ??r??n se??iliyor!!!");
            waitBySeconds(4);
            scrollToElementWithJs("kampanyalarSecinizBesinciDropdown");
            getElementWithKeyIfExists("kampanyalarSecinizBesinciDropdown");
            clickElement("kampanyalarSecinizBesinciDropdown");
            waitBySeconds(2);
            getElementWithKeyIfExists("kampanyalarBesinciUrunIlkPizza");
            clickElement("kampanyalarBesinciUrunIlkPizza");
            waitBySeconds(2);

        }


    }

    @Step("<key> key yolla elemente <text>")
    public void sendKeysWithNumpad(String key, String text){
        WebElement webElement = findElement(key);
        char[] textArray = text.toCharArray();
        for(int i = 0; i < textArray.length; i++){
            webElement.sendKeys(Keys.valueOf("NUMPAD" + String.valueOf(textArray[i])));
        }
        logger.info("Elemente " + text + " texti yaz??ld??.");
    }

    @Step("Pizza sepete eklenir veya kampanyayali ??r??n sepete eklenir Android")
    public void pizzaAndCampaignSelection() {
        waitBySeconds(5);
        if (findElements("kampanyaliUrunlereGitButton").size() > 0) {
            logger.info("Kampanyal?? ??r??nlere gidiliyor");
            waitBySeconds(4);
            getElementWithKeyIfExists("kampanyaliUrunlereGitButton");
            clickElement("kampanyaliUrunlereGitButton");
            waitBySeconds(2);
            getElementWithKeyIfExists("ilkKampanyaSec");
            clickElement("ilkKampanyaSec");
            waitBySeconds(2);
            kampanyaSecAndroid();
            waitBySeconds(2);
            getElementWithKeyIfExists("kampanyaSepeteEkleButon");
            clickElement("kampanyaSepeteEkleButon");
            waitBySeconds(10);
            existTapByKey("digitalOfferIstemiyorum");

        }
        if (findElements("sepeteEkleButon").size() > 0) {
            logger.info("Pizza sepete ekleniyor");
            waitBySeconds(4);
            getElementWithKeyIfExists("sepeteEkleButon");
            clickElement("sepeteEkleButon");
            waitBySeconds(10);
            existTapByKey("digitalOfferIstemiyorum");
        }
    }

    @Step("<text> Giris dizayn secimi yapilir")
    public void girisDizaynSecimi(String text) {
        waitBySeconds(5);
        if (findElements("adreseTeslimButon").size() > 0 ){
            logger.info("Eski Dizayna gidiliyor");
            switch (text) {
                case "girisYap":
                    waitBySeconds(2);
                    getElementWithKeyIfExists("girisYapButon");
                    clickElement("girisYapButon");
                    break;
                case "UyeOl":
                    waitBySeconds(2);
                    getElementWithKeyIfExists("uyeOlButon");
                    clickElement("uyeOlButon");
                    break;
            }
        } else if (findElements("yeniDizaynUyeOlmadanDevamEt").size() > 0 ) {
            logger.info("Yeni dizayna gidiliyor ");
            waitBySeconds(2);
            switch (text) {
                case "girisYap":
                    waitBySeconds(2);
                    getElementWithKeyIfExists("girisYapButon");
                    clickElement("girisYapButon");
                    break;
                case "UyeOl":
                    waitBySeconds(2);
                    getElementWithKeyIfExists("uyeOlButon");
                    clickElement("uyeOlButon");
                    break;
            }
        }

    }



    @Step("Tarih secimi yapilir")
    public void tarihSecimi() throws InterruptedException{

        String select_day;
        String selectDate;
        webDriver.findElement(By.xpath("//div[@class='react-datepicker-wrapper']")).click();


        Date d = new Date(1);
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMMM-yyyy");
        String date = formatter.format(d);
        String splitter[] = date.split("-");
        String month_year = splitter[1];
        String day = splitter[0];
        System.out.println(month_year);
        System.out.println(day);


        selectDate(month_year, day);

    }
            public void selectDate (String month_year, String select_day) throws InterruptedException
            {
                List<WebElement> elements = webDriver.findElements(By.xpath("//div[@class='react-datepicker__week'][3]"));

                for (int i = 0; i < elements.size(); i++) {
                    System.out.println(elements.get(i).getText());

//Selecting the month
                    if (elements.get(i).getText().equals(month_year)) {

//Selecting the date
                        List<WebElement> days = webDriver.findElements(By.xpath("//div[@role='button']"));

                        for (WebElement d : days) {
                            System.out.println(d.getText());
                            if (d.getText().equals(select_day)) {
                                d.click();
                                return;
                            }
                        }

                    }

                }
                webDriver.findElement(By.xpath("//div[@role='button']")).click();
                selectDate(month_year, select_day);

            }


    @Step("Telefon dogrulama kodu girilir")
    public void otpTarihFormati() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("MMddHH");
        String formatDateTime = now.format(format);
        int formatDateTimeInt = Integer.parseInt(formatDateTime);
        int newDateTimeInt = formatDateTimeInt + 3;
        String newDateTime = String.valueOf(newDateTimeInt);
        logger.info(formatDateTime);
        System.out.println(formatDateTimeInt);
        logger.info(newDateTime);
        getElementWithKeyIfExists("otpInput1");
        clickElement("otpInput1");
        WebElement webElement = findElementWithKey("otpInput1");
        webElement.clear();
        webElement.sendKeys(newDateTime);
    }




}
