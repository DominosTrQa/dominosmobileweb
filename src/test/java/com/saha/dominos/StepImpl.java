package com.saha.dominos;



import com.saha.dominos.helper.RandomString;
import com.saha.dominos.helper.StoreHelper;
import com.saha.dominos.model.SelectorInfo;
import com.thoughtworks.gauge.Step;
import io.appium.java_client.MobileBy;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import io.appium.java_client.android.nativekey.KeyEventMetaModifier;
import io.appium.java_client.touch.LongPressOptions;
import io.appium.java_client.touch.WaitOptions;
import io.appium.java_client.touch.offset.ElementOption;
import io.appium.java_client.touch.offset.PointOption;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Before;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.sql.Timestamp;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import static java.time.Duration.ofMillis;

public class StepImpl extends HookImpl {

    private Logger logger = LoggerFactory.getLogger(getClass());

    public List<MobileElement> findElements(By by) throws Exception {
        List<MobileElement> webElementList = null;
        try {
            webElementList = appiumFluentWait.until(new ExpectedCondition<List<MobileElement>>() {
                @Nullable
                @Override
                public List<MobileElement> apply(@Nullable WebDriver driver) {
                    List<MobileElement> elements = driver.findElements(by);
                    return elements.size() > 0 ? elements : null;
                }
            });
            if (webElementList == null) {
                throw new NullPointerException(String.format("by = %s Web element list not found", by.toString()));
            }
        } catch (Exception e) {
            throw e;
        }
        return webElementList;
    }

    public List<MobileElement> findElementsWithoutAssert(By by) {

        List<MobileElement> mobileElements = null;
        try {
            mobileElements = findElements(by);
        } catch (Exception e) {
        }
        return mobileElements;
    }

    public List<MobileElement> findElementsWithAssert(By by) {

        List<MobileElement> mobileElements = null;
        try {
            mobileElements = findElements(by);
        } catch (Exception e) {
            Assertions.fail("by = %s Elements not found ", by.toString());
            e.printStackTrace();
        }
        return mobileElements;
    }


    public MobileElement findElement(By by) throws Exception {
        MobileElement mobileElement;
        try {
            mobileElement = findElements(by).get(0);
        } catch (Exception e) {
            throw e;
        }
        return mobileElement;
    }

    public MobileElement findElementWithoutAssert(By by) {
        MobileElement mobileElement = null;
        try {
            mobileElement = findElement(by);
        } catch (Exception e) {
            //   e.printStackTrace();
        }
        return mobileElement;
    }

    public MobileElement findElementWithAssertion(By by) {
        MobileElement mobileElement = null;
        try {
            mobileElement = findElement(by);
        } catch (Exception e) {
            Assertions.fail("by = %s Element not found ", by.toString());
            e.printStackTrace();
        }
        return mobileElement;
    }

    public void sendKeysTextByAndroidKey(String text) {

        AndroidDriver androidDriver = (AndroidDriver) appiumDriver;
        char[] chars = text.toCharArray();
        String stringValue = "";
        for (char value: chars) {
            stringValue = String.valueOf(value);
            if(Character.isDigit(value)) {
                androidDriver.pressKey(new KeyEvent().withKey(AndroidKey.valueOf("DIGIT_" + String.valueOf(value))));
            }else if(Character.isLetter(value)){
                if (Character.isLowerCase(value)){
                    androidDriver.pressKey(new KeyEvent().withKey(AndroidKey.valueOf(stringValue.toUpperCase(Locale.ENGLISH))));
                }else {
                    androidDriver.pressKey(new KeyEvent().withKey(AndroidKey.valueOf(stringValue))
                            .withMetaModifier(KeyEventMetaModifier.SHIFT_ON));
                }
            }else if(stringValue.equals("@")){
                androidDriver.pressKey(new KeyEvent().withKey(AndroidKey.AT));
            }else if(stringValue.equals(".")){
                androidDriver.pressKey(new KeyEvent().withKey(AndroidKey.PERIOD));
            } else if(stringValue.equals(" ")){
                androidDriver.pressKey(new KeyEvent().withKey(AndroidKey.SPACE));
            }else {
                Assert.fail("Metod " + stringValue + " desteklemiyor.");
            }
        }
        logger.info(text + " texti AndroidKey yollanarak yazıldı.");
    }

    public void sendKeysTextWithIosKeyboard(String text){

        char[] chars = text.toCharArray();
        for (char charText: chars) {
            findElementWithAssertion(MobileBy.iOSClassChain(String.format("**/XCUIElementTypeKey[`name='%s'`]",
                    String.valueOf(charText).toLowerCase(Locale.ENGLISH)))).click();
            logger.info("" + charText);
        }
    }

    public MobileElement findElementByKeyWithoutAssert(String key) {
        SelectorInfo selectorInfo = selector.getSelectorInfo(key);

        MobileElement mobileElement = null;
        try {
            mobileElement = selectorInfo.getIndex() > 0 ? findElements(selectorInfo.getBy())
                    .get(selectorInfo.getIndex()) : findElement(selectorInfo.getBy());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mobileElement;
    }

    public MobileElement findElementByKey(String key) {
        SelectorInfo selectorInfo = selector.getSelectorInfo(key);

        MobileElement mobileElement = null;
        try {
            mobileElement = selectorInfo.getIndex() > 0 ? findElements(selectorInfo.getBy())
                    .get(selectorInfo.getIndex()) : findElement(selectorInfo.getBy());
        } catch (Exception e) {
            Assertions.fail("key = %s by = %s Element not found ", key, selectorInfo.getBy().toString());
            e.printStackTrace();
        }
        return mobileElement;
    }


    public List<MobileElement> findElemenstByKeyWithoutAssert(String key) {
        SelectorInfo selectorInfo = selector.getSelectorInfo(key);
        List<MobileElement> mobileElements = null;
        try {
            mobileElements = findElements(selectorInfo.getBy());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mobileElements;
    }

    public List<MobileElement> findElemenstByKey(String key) {
        SelectorInfo selectorInfo = selector.getSelectorInfo(key);
        List<MobileElement> mobileElements = null;
        try {
            mobileElements = findElements(selectorInfo.getBy());
        } catch (Exception e) {
            Assertions.fail("key = %s by = %s Elements not found ", key, selectorInfo.getBy().toString());
            e.printStackTrace();
        }
        return mobileElements;
    }


    @Step({"Değeri <text> e eşit olan elementli bul ve tıkla",
            "Find element text equals <text> and click"})
    public void clickByText(String text) {
        findElementWithAssertion(By.xpath(".//*[contains(@text,'" + text + "')]")).click();
    }

    @Step({"İçeriği <value> e eşit olan elementli bul ve tıkla",
            "Find element value equals <value> and click"})
    public void clickByValue(String value) {
        findElementWithAssertion(MobileBy.xpath(".//*[contains(@value,'" + value + "')]")).click();
    }

    @Step({"Değeri <text> e eşit olan <index>. elementi bul ve tıkla"})
    public void clickByText(String text, int index) {
        findElementWithAssertion(By.xpath("(.//*[contains(@text,'" + text + "')])[" + index + "]")).click();
    }

    @Step({"İçeriği <value> e eşit olan <index>. elementi bul ve tıkla"})
    public void clickByValue(String value, int index) {
        findElementWithAssertion(MobileBy.xpath("(.//*[contains(@value,'" + value + "')])[" + index + "]")).click();
    }

    @Step("Uygulamanın açıldığını kontrol et")
    public void checkApp() throws InterruptedException {
        logger.info("Uygulamanin acildigini kontrol et");
        if (appiumDriver instanceof AndroidDriver) {
            existClickByKey("reddetButonu");
            clickBybackButton();
            waitBySecond(10);
        } else {
            existClickByKey("reddetButonu");
            waitBySecond(10);
        }

    }

    @Step("Kullanıcı girişi yapılmışsa çıkış yap")
    public void logout() throws InterruptedException {
        waitBySecond(3);
        if (!appiumDriver.getPageSource().contains("Giriş Yap")) {
            logger.info("Giris yap yok");
            clickByKey("profilSekmesiBtn");
            waitBySecond(3);
            swipe(2);
            clickByKeyWithSwipe("profilAyarGit");
            waitBySecond(3);
            swipe(2);
            clickByKeyWithSwipe("cikisButonu");
            clickByKey("popupCikisYapButonu");
            waitBySecond(5);
            getPageSourceFindWord("Giriş Yap");
        }
    }


    @Step({"<key> li elementi bul ve tıkla", "Click element by <key>"})
    public void clickByKey(String key) {
        doesElementExistByKey(key, 5);
        doesElementClickableByKey(key,5);
        //       logger.info("appiumDriver.getPageSource().contains(key)");
        findElementByKey(key).click();
//        MobileElement me = findElementByKey(key);
//        tapElementWithCoordinate(me.getLocation().x, me.getLocation().y);
        logger.info(key + "elemte tıkladı");



    }

    @Step({"<key> li element sayfada bulunuyor mu kontrol et"})
    public void existElement(String key) {
        Assert.assertTrue("Element sayfada bulunamadı !", findElementByKey(key).isEnabled());
    }

    @Step("giris yap butonuna tikla")
    public void clickLoginButton() throws InterruptedException {

        newWebDriverWait(30, 1000);

        findElementWithAssertion(By.id("com.ttech.android.onlineislem:id/buttonPasswordContinue")).click();

        waitBySecond(5);
        existClickByKey("dinamikKartlarıKapatmaIkonu");
    }


    @Step({"<key> li elementi bul ve varsa tıkla", "Click element by <key> if exist"})
    public void existClickByKey(String key) {

        MobileElement element;

        element = findElementByKeyWithoutAssert(key);

        if (element != null) {
            System.out.println("  varsa tıklaya girdi");
            Point elementPoint = ((MobileElement) element).getCenter();
            TouchAction action = new TouchAction(appiumDriver);
            action.tap(PointOption.point(elementPoint.x, elementPoint.y)).perform();
        }
    }

    @Step({"<key> li elementi bul ve varsa dokun", "Click element by <key> if exist"})
    public void existTapByKey(String key) {

        WebElement element = null;
        try {
            element = findElementByKey(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (element != null) {
            element.click();
        }
    }

    @Step({"sayfadaki <X> <Y>  alana dokun"})
    public void coordinateTap(int X, int Y){

        Dimension dimension = appiumDriver.manage().window().getSize();
        int width = dimension.width;
        int height = dimension.height;

        TouchAction action = new TouchAction(appiumDriver);
        action.tap(PointOption.point((width*X)/100,(height*Y)/100))
                .release().perform();

    }


    @Step({"<key> li elementi bul, temizle ve <text> değerini yaz",
            "Find element by <key> clear and send keys <text>"})
    public void sendKeysByKey(String key, String text) {
        MobileElement webElement = findElementByKey(key);
        webElement.clear();
        // webElement.sendKeys(Keys.DELETE);
        //webElement.clear();
        webElement.setValue(text);
    }

    @Step("<Key> li elementi bul ve temizle")
    public void ClearText(String key) {
        MobileElement webElement = findElementByKey(key);
        webElement.clear();

    }


    @Step({"<key> li elementi bul ve <text> değerini yaz",
            "Find element by <key> and send keys <text>"})
    public void sendKeysByKeyNotClear(String key, String text) {
        doesElementExistByKey(key, 5);
        findElementByKey(key).sendKeys(text);

    }

    @Step("<text> değerini Android klavye ile yaz")
    public void sendKeysWithAndroidKey(String text) {
        sendKeysTextByAndroidKey(text);

    }

    @Step("<text> değerini ios klavye ile yaz")
    public void sendKeysWithIosKeyboard(String text) {
        sendKeysTextWithIosKeyboard(text);

    }

    @Step("<key> li elementi bul ve <text> değerini tek tek yaz")
    public void sendKeysValueOfClear(String key, String text){
        MobileElement me = findElementByKey(key);
        me.clear();
        for(int i = 0 ; i<text.length();i++){
            char c  = text.charAt(i);
            me.sendKeys(String.valueOf(c));
        }
        System.out.println("'" + text + "' written to '" + key + "' element.");

    }

    @Step({"<key> li elementi bul ve değerini <saveKey> olarak sakla",
            "Find element by <key> and save text <saveKey>"})
    public void saveTextByKey(String key, String saveKey) {
        StoreHelper.INSTANCE.saveValue(saveKey, findElementByKey(key).getText());
    }

    @Step({"<key> li elementi bul ve değerini <saveKey> saklanan değer ile karşılaştır",
            "Find element by <key> and compare saved key <saveKey>"})
    public void equalsSaveTextByKey(String key, String saveKey) {
        Assert.assertEquals(StoreHelper.INSTANCE.getValue(saveKey), findElementByKey(key).getText());
    }


    @Step({"<key> li ve değeri <text> e eşit olan elementli bul ve tıkla",
            "Find element by <key> text equals <text> and click"})
    public void clickByIdWithContains(String key, String text) {
        List<MobileElement> elements = findElemenstByKey(key);
        for (MobileElement element : elements) {
            logger.info("Text !!!" + element.getText());
            if (element.getText().toLowerCase().contains(text.toLowerCase())) {
                element.click();
                break;
            }
        }
    }

    @Step({"<key> li ve değeri <text> e eşit olan elementli bulana kadar swipe et ve tıkla",
            "Find element by <key> text equals <text> swipe and click"})
    public void clickByKeyWithSwipe(String key, String text) throws InterruptedException {
        boolean find = false;
        int maxRetryCount = 10;
        while (!find && maxRetryCount > 0) {
            List<MobileElement> elements = findElemenstByKey(key);
            for (MobileElement element : elements) {
                if (element.getText().contains(text)) {
                    element.click();
                    find = true;
                    break;
                }
            }
            if (!find) {
                maxRetryCount--;
                if (appiumDriver instanceof AndroidDriver) {
                    swipeUpAccordingToPhoneSize();
                    waitBySecond(1);
                } else {
                    swipeDownAccordingToPhoneSize();
                    waitBySecond(1);
                }
            }
        }
    }

    @Step({"<key> li elementi bulana kadar swipe et ve tıkla",
            "Find element by <key>  swipe and click"})
    public void clickByKeyWithSwipe(String key) throws InterruptedException {
        int maxRetryCount = 10;
        while (maxRetryCount > 0) {
            List<MobileElement> elements = findElemenstByKey(key);
            if (elements.size() > 0) {
                if (elements.get(0).isDisplayed() == false) {
                    maxRetryCount--;
                    swipeDownAccordingToPhoneSize();
                    waitBySecond(1);

                } else {
                    elements.get(0).click();
                    logger.info(key + " elementine tıklandı");
                    break;
                }
            } else {
                maxRetryCount--;
                swipeDownAccordingToPhoneSize();
                waitBySecond(1);
            }

        }
    }


    private int getScreenWidth() {
        return appiumDriver.manage().window().getSize().width;
    }

    private int getScreenHeight() {
        return appiumDriver.manage().window().getSize().height;
    }

    private int getScreenWithRateToPercent(int percent) {
        return getScreenWidth() * percent / 100;
    }

    private int getScreenHeightRateToPercent(int percent) {
        return getScreenHeight() * percent / 100;
    }


    public void swipeDownAccordingToPhoneSize(int startXLocation, int startYLocation, int endXLocation, int endYLocation) {
        startXLocation = getScreenWithRateToPercent(startXLocation);
        startYLocation = getScreenHeightRateToPercent(startYLocation);
        endXLocation = getScreenWithRateToPercent(endXLocation);
        endYLocation = getScreenHeightRateToPercent(endYLocation);

        new TouchAction(appiumDriver)
                .press(PointOption.point(startXLocation, startYLocation))
                .waitAction(WaitOptions.waitOptions(ofMillis(1000)))
                .moveTo(PointOption.point(endXLocation, endYLocation))
                .release()
                .perform();
    }

    @Step({"<key> id'li elementi bulana kadar <times> swipe yap ",
            "Find element by <key>  <times> swipe "})
    public void swipeDownUntilSeeTheElement(String element, int limit) throws InterruptedException {
        for (int i = 0; i < limit; i++) {
            List<MobileElement> meList = findElementsWithoutAssert(By.id(element));
            meList = meList != null ? meList : new ArrayList<MobileElement>();

            if (meList.size() > 0 &&
                    meList.get(0).getLocation().x <= getScreenWidth() &&
                    meList.get(0).getLocation().y <= getScreenHeight()) {
                break;
            } else {
                swipeDownAccordingToPhoneSize(50, 80, 50, 30);
                waitBySecond(1);

                break;
            }
        }
    }


    @Step({"<key> li elementi bulana kadar swipe et",
            "Find element by <key>  swipe "})
    public void findByKeyWithSwipe(String key) {
        int maxRetryCount = 10;
        while (maxRetryCount > 0) {
            List<MobileElement> elements = findElemenstByKey(key);
            if (elements.size() > 0) {
                if (elements.get(0).isDisplayed() == false) {
                    maxRetryCount--;

                    swipeDownAccordingToPhoneSize();

                } else {
                    System.out.println(key + " element bulundu");
                    break;
                }
            } else {
                maxRetryCount--;
                swipeDownAccordingToPhoneSize();

            }

        }
    }


    @Step(" <yön> yönüne swipe et")
    public void swipe(String yon) {

        Dimension d = appiumDriver.manage().window().getSize();
        int height = d.height;
        int width = d.width;

        if (yon.equals("SAĞ")) {

            int swipeStartWidth = (width * 80) / 100;
            int swipeEndWidth = (width * 30) / 100;

            int swipeStartHeight = height / 2;
            int swipeEndHeight = height / 2;

            //appiumDriver.swipe(swipeStartWidth, swipeStartHeight, swipeEndWidth, swipeEndHeight, 1000);
            new TouchAction(appiumDriver)
                    .press(PointOption.point(swipeStartWidth, swipeStartHeight))
                    .waitAction(WaitOptions.waitOptions(ofMillis(1000)))
                    .moveTo(PointOption.point(swipeEndWidth, swipeEndHeight))
                    .release()
                    .perform();
        } else if (yon.equals("SOL")) {

            int swipeStartWidth = (width * 30) / 100;
            int swipeEndWidth = (width * 80) / 100;

            int swipeStartHeight = height / 2;
            int swipeEndHeight = height / 2;

            //appiumDriver.swipe(swipeStartWidth, swipeStartHeight, swipeEndWidth, swipeEndHeight, 1000);

            new TouchAction(appiumDriver)
                    .press(PointOption.point(swipeStartWidth, swipeStartHeight))
                    .waitAction(WaitOptions.waitOptions(ofMillis(1000)))
                    .moveTo(PointOption.point(swipeEndWidth, swipeEndHeight))
                    .release()
                    .perform();

        }
    }

    @Step({"<key> li elementi bul yoksa <message> mesajını hata olarak göster",
            "Find element by <key> if not exist show error message <message>"})
    public void isElementExist(String key, String message) {
        Assert.assertTrue(message, findElementByKey(key) != null);
    }

    @Step({"<key> li elementin değeri <text> e içerdiğini kontrol et",
            "Find element by <key> and text contains <text>"})
    public void containsTextByKey(String key, String text) {
        By by = selector.getElementInfoToBy(key);
        Assert.assertTrue(appiumFluentWait.until(new ExpectedCondition<Boolean>() {
            private String currentValue = null;

            @Override
            public Boolean apply(WebDriver driver) {
                try {
                    currentValue = driver.findElement(by).getAttribute("value");
                    return currentValue.contains(text);
                } catch (Exception e) {
                    return false;
                }
            }

            @Override
            public String toString() {
                return String.format("text contains be \"%s\". Current text: \"%s\"", text, currentValue);
            }
        }));
    }

    @Step({"<key> li elementin değeri <text> e eşitliğini kontrol et",
            "Find element by <key> and text equals <text>"})
    public void equalsTextByKey(String key, String text) {
        Assert.assertTrue(appiumFluentWait.until(
                ExpectedConditions.textToBe(selector.getElementInfoToBy(key), text)));
    }

    @Step({"<seconds> saniye bekle", "Wait <second> seconds"})
    public void waitBySecond(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void swipeUpAccordingToPhoneSize() {
        if (appiumDriver instanceof AndroidDriver) {
            Dimension d = appiumDriver.manage().window().getSize();
            int height = d.height;
            int width = d.width;
            System.out.println(width + "  " + height);

            int swipeStartWidth = width / 2, swipeEndWidth = width / 2;
            int swipeStartHeight = (height * 20) / 100;
            int swipeEndHeight = (height * 60) / 100;
            //appiumDriver.swipe(swipeStartWidth, swipeStartHeight, swipeEndWidth, swipeEndHeight, 1000);
            new TouchAction((AndroidDriver)appiumDriver)
                    .press(PointOption.point(swipeStartWidth, swipeEndHeight))
                    .waitAction(WaitOptions.waitOptions(ofMillis(2000)))
                    .moveTo(PointOption.point(swipeEndWidth, swipeStartHeight))
                    .release()
                    .perform();
        } else {
            Dimension d = appiumDriver.manage().window().getSize();
            int height = d.height;
            int width = d.width;

            int swipeStartWidth = width / 2, swipeEndWidth = width / 2;
            int swipeStartHeight = (height * 35) / 100;
            int swipeEndHeight = (height * 75) / 100;
            //appiumDriver.swipe(swipeStartWidth, swipeStartHeight, swipeEndWidth, swipeEndHeight, 1000);
            new TouchAction(appiumDriver)
                    .press(PointOption.point(swipeStartWidth, swipeEndHeight))
                    .waitAction(WaitOptions.waitOptions(ofMillis(2000)))
                    .moveTo(PointOption.point(swipeEndWidth,swipeStartHeight ))
                    .release()
                    .perform();
        }
    }


    public void swipeDownAccordingToPhoneSize() {
        if (appiumDriver instanceof AndroidDriver) {
            Dimension d = appiumDriver.manage().window().getSize();
            int height = d.height;
            int width = d.width;

            int swipeStartWidth = width / 2, swipeEndWidth = width / 2;
            int swipeStartHeight = (height * 60) / 100;
            int swipeEndHeight = (height * 20) / 100;
            //appiumDriver.swipe(swipeStartWidth, swipeStartHeight, swipeEndWidth, swipeEndHeight, 1000);
            new TouchAction(appiumDriver)
                    .press(PointOption.point(swipeStartWidth, swipeStartHeight))
                    .waitAction(WaitOptions.waitOptions(ofMillis(1000)))
                    .moveTo(PointOption.point(swipeEndWidth, swipeEndHeight))
                    .release()
                    .perform();
        } else {
            Dimension d = appiumDriver.manage().window().getSize();
            int height = d.height;
            int width = d.width;

            int swipeStartWidth = width / 2, swipeEndWidth = width / 2;
            int swipeStartHeight = (height * 70) / 100;
            int swipeEndHeight = (height * 30) / 100;
            // appiumDriver.swipe(swipeStartWidth, swipeStartHeight, swipeEndWidth, swipeEndHeight, 1000);
            new TouchAction(appiumDriver)
                    .press(PointOption.point(swipeStartWidth, swipeStartHeight))
                    .waitAction(WaitOptions.waitOptions(ofMillis(1000)))
                    .moveTo(PointOption.point(swipeEndWidth, swipeEndHeight))
                    .release()
                    .perform();
        }
    }

    public boolean isElementPresent(By by) {
        return findElementWithoutAssert(by) != null;
    }


    @Step({"<times> kere aşağıya kaydır", "Swipe times <times>"})
    public void swipe(int times) throws InterruptedException {
        for (int i = 0; i < times; i++) {
            swipeDownAccordingToPhoneSize();
            waitBySecond(3);

            System.out.println("-----------------------------------------------------------------");
            System.out.println("SWİPE EDİLDİ");
            System.out.println("-----------------------------------------------------------------");

        }
    }

    @Step({"Sayfanın  ortasından itibaren  <times> kere aşağıya kaydır", "Middle  on page  and Swipe times <times>"})
    public void MiddlePageswipe(int times) {
        for (int i = 0; i < times; i++) {
            if (appiumDriver instanceof AndroidDriver) {
                Dimension d = appiumDriver.manage().window().getSize();
                int width = d.width;

                int swipeStartWidth = width / 2, swipeEndWidth = width / 2;
                int swipeStartHeight = (400 * 90) / 100;
                int swipeEndHeight = (400 * 50) / 100;
                //appiumDriver.swipe(swipeStartWidth, swipeStartHeight, swipeEndWidth, swipeEndHeight, 1000);
                new TouchAction(appiumDriver)
                        .press(PointOption.point(swipeStartWidth, swipeStartHeight))
                        .waitAction(WaitOptions.waitOptions(ofMillis(1000)))
                        .moveTo(PointOption.point(swipeEndWidth, swipeEndHeight))
                        .release()
                        .perform();
            } else {
                Dimension d = appiumDriver.manage().window().getSize();
                // int height = d.height;
                int width = d.width;

                int swipeStartWidth = width / 2, swipeEndWidth = width / 2;
                int swipeStartHeight = (400 * 90) / 100;
                int swipeEndHeight = (400 * 40) / 100;
                // appiumDriver.swipe(swipeStartWidth, swipeStartHeight, swipeEndWidth, swipeEndHeight, 1000);
                new TouchAction(appiumDriver)
                        .press(PointOption.point(swipeStartWidth, swipeStartHeight))
                        .waitAction(WaitOptions.waitOptions(ofMillis(1000)))
                        .moveTo(PointOption.point(swipeEndWidth, swipeEndHeight))
                        .release()
                        .perform();
            }
        }
        waitBySecond(3);

        System.out.println("-----------------------------------------------------------------");
        System.out.println("SWİPE EDİLDİ");
        System.out.println("-----------------------------------------------------------------");

    }







    @Step({"<times> kere yukarı doğru kaydır", "Swipe up times <times>"})
    public void swipeUP(int times) throws InterruptedException {
        for (int i = 0; i < times; i++) {
            swipeUpAccordingToPhoneSize();
            waitBySecond(1);

            System.out.println("-----------------------------------------------------------------");
            System.out.println("SWİPE EDİLDİ");
            System.out.println("-----------------------------------------------------------------");

        }
    }


    @Before
    public void setUp() throws Exception {

    }

    @Step({"Klavyeyi kapat", "Hide keyboard"})
    public void hideAndroidKeyboard() {
        try {

            if (localAndroid == false) {
                findElementWithoutAssert(By.xpath("//XCUIElementTypeButton[@name='Toolbar Done Button']")).click();
            } else {
                appiumDriver.hideKeyboard();
            }

        } catch (Exception ex) {
            logger.error("Klavye kapatılamadı ", ex.getMessage());
        }
    }

    @Step({"<key> değerini sayfa üzerinde olup olmadığını kontrol et."})
    public void getPageSourceFindWord(String key) {
        logger.info("Page sources   !!!!!   "+appiumDriver.getPageSource());
        Assert.assertTrue(key + " sayfa üzerinde bulunamadı.",
                appiumDriver.getPageSource().contains(key));

        logger.info(key + " sayfa üzerinde bulundu");
    }


    @Step({"<length> uzunlugunda random bir kelime üret ve <saveKey> olarak sakla"})
    public void createRandomNumber(int length, String saveKey) {
        StoreHelper.INSTANCE.saveValue(saveKey, new RandomString(length).nextString());
    }

    @Step("geri butonuna bas")
    public void clickBybackButton() {
        if (!localAndroid) {
            backPage();
        } else {
            ((AndroidDriver) appiumDriver).pressKey(new KeyEvent().withKey(AndroidKey.BACK));
        }

    }

    @Step("Tamam butonuna tıkla")
    public void clickOKButton() {

        newWebDriverWait(30, 1000);

        MobileElement me = (MobileElement) appiumFluentWait.until(ExpectedConditions.presenceOfElementLocated(By.id("com.ttech.android.onlineislem:id/btn_confirm")));
        me.click();
    }


    @Step("Login  kontrol")
    public void LoginControl() throws InterruptedException {
        if (isElementPresent(By.id("com.ttech.android.onlineislem:id/textViewLoginMsisdn"))) {


            findElementWithoutAssert(By.id("com.ttech.android.onlineislem:id/textViewLoginMsisdn")).click();
            findElementWithoutAssert(By.id("com.ttech.android.onlineislem:id/textViewGoToProfile")).click();
            waitBySecond(2);
            swipe(1);
            waitBySecond(10);
            findElementWithoutAssert(By.xpath("//android.widget.TextView[@content-desc='Çıkış']")).click();
            findElementWithoutAssert(By.id("com.ttech.android.onlineislem:id/buttonPositive")).click();
            existClickByKey("ızınVer");

        } else {

            logger.info("Sisteme login olma işlemi başladı");

        }
    }

    @Step("Anasayfa widget kontrolü")
    public void maninPagewidgetControl() throws InterruptedException {
        waitBySecond(3);
        if (isElementPresent(By.id("com.ttech.android.onlineislem:id/image_selocan_right"))) {

            findElementWithoutAssert(By.id("com.ttech.android.onlineislem:id/navigation_myaccount")).click();
            waitBySecond(2);
            findElementWithoutAssert(By.id("com.ttech.android.onlineislem:id/navigation_shop")).click();
            waitBySecond(2);
            findElementWithoutAssert(By.id("com.ttech.android.onlineislem:id/navigation_support")).click();
            waitBySecond(2);
            findElementWithoutAssert(By.id("com.ttech.android.onlineislem:id/imageViewProfilIcon")).click();
            waitBySecond(2);
            findElementWithoutAssert(By.id("com.ttech.android.onlineislem:id/linearLayoutNotificationsn")).click();

        } else {
            logger.info("Widget görülmedi !!!!!");
        }
    }

    @Step("Çıkış yap butonuna tıkla")
    public void Clicklogout() throws InterruptedException {
        waitBySecond(2);
        swipe(1);
        findElementWithoutAssert(By.xpath("//android.widget.TextView[@content-desc='Çıkış']")).click();
        findElementWithoutAssert(By.id("com.ttech.android.onlineislem:id/buttonPositive")).click();
        waitBySecond(5);
        existClickByKey("ızınVer");

    }


    @Step("Yeni şifre <text> ve yeni şifre tekrar <textrepeat>  alanlarına tex değerlerini yaz")
    public void writeNewPassword(String text, String textrepeat) {

        newWebDriverWait(30, 1000);
        objectTextandClick(By.id("com.ttech.android.onlineislem:id/editTextPassword"), text);
        newWebDriverWait(20, 1000);
        clickBybackButton();
        newWebDriverWait(30, 1000);
        objectTextandClick(By.id("com.ttech.android.onlineislem:id/editTextPasswordRepeat"), textrepeat);
        clickBybackButton();

    }


    @Step("<StartX>,<StartY> oranlarından <EndX>,<EndY> oranlarına <times> kere swipe et")
    public void pointToPointSwipe(int startX, int startY, int endX, int endY, int count) {
        Dimension d = appiumDriver.manage().window().getSize();
        int height = d.height;
        int width = d.width;

        startX = (width * startX) / 100;
        startY = (height * startY) / 100;
        endX = (width * endX) / 100;
        endY = (height * endY) / 100;


        for (int i = 0; i < count; i++) {
            //appiumDriver.swipe(startX, startY, endX, endY, 1000);
            TouchAction action = new TouchAction(appiumDriver);
            action.press(PointOption.point(startX, startY))
                    .waitAction(WaitOptions.waitOptions(ofMillis(1000)))
                    .moveTo(PointOption.point(endX, endY))
                    .release().perform();
        }
    }

    @Step("uygulamayı yeniden başlat")
    public void restart() throws InterruptedException {


        appiumDriver.closeApp();
        appiumDriver.launchApp();
        logger.info("uygulama yeniden başlatıldı");
        waitBySecond(5);
        existClickByKey("ızınVer");

    }


    @Step("Galeriden fotograf seç")
    public void selectPhotoFromGallery() throws InterruptedException {

        String deviceName = getCapability("deviceName");

        System.out.println("----- CİHAZDANDAN GELEN DEVİCE NAME: (" + deviceName + ")-------");
        if (deviceName.contains("CB512EHLX6")) {
            logger.info("SONY telefonu galerisine girdi");
            existClickByKey("ızınVer");
            waitBySecond(5);
            clickByKey("samsungGalleryPhoto");
            clickByKey("samsungPhoto");
        } else if (deviceName.contains("0f810c2a")) {
            logger.info("Samsung J5 telefonu galerisine girdi");
            waitBySecond(5);
            clickByKey("samsungGallery");
            waitBySecond(5);
            clickByKey("samsungJ5Photo");
        } else if (deviceName.contains("9889db324d434f4637")) {
            logger.info("Samsung S8 galerisine girdi");
            // existClickByKey("samsungChoose");
            //existClickByKey("ızınVer");
            waitBySecond(5);
            clickByKey("samsungGalleryPhoto");
            clickByKey("samsungPhoto");
        } else if (deviceName.contains("5200fbe7f4688403")) {
            logger.info("Samsung A5 telefonu galerisine girdi");
            // existClickByKey("samsungChoose");
            // existClickByKey("ızınVer");
            waitBySecond(5);
            clickByKey("samsungGalleryPhoto");
            existClickByKey("samsungGalleryPhoto");
            clickByKey("samsungPhoto");
        } else if (deviceName.contains("ad051602404b854bca")) {
            logger.info("Samsung S7 Edge telefonu galerisine girdi");
            //existClickByKey("samsungChoose");
            //existClickByKey("ızınVer");
            waitBySecond(5);
            clickByKey("samsungGalleryPhoto");
            clickByKey("samsungPhoto");

        } else if (deviceName.contains("ce03171382675e0f01")) {
            logger.info("Galaxy s8 (7.0) telefonu galerisine girdi");
            existClickByKey("ızınVer");
            waitBySecond(5);
            existClickByKey("samsungChoose");
            clickByKey("samsungGalleryPhoto");
            clickByKey("samsungPhoto");
        } else {
            logger.info("iphone galeriye girdi");
            existClickByKey("ızınVer");
            waitBySecond(5);
            clickByKey("ıphoneGaleri");
            existClickByKey("ızınVer");
            clickByKey("ıphoneGaleriPhotoChoose");
        }
    }

    @Step("pop-up izin ver")
    public void closePopupAndCookie() throws InterruptedException {
        waitBySecond(2);

        contextText("NATIVE_APP");

        new WebDriverWait(appiumDriver, 30).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@text='İZİN VER']")));

        if (isElementPresent(By.xpath("//*[@text='İZİN VER']"))
        ) {
            logger.info("Notification pop-up kapatıldı !!!!!!");
            waitBySecond(2);
            contextText("NATIVE_APP");
            findElementWithoutAssert(By.xpath("//*[@text='İZİN VER']")).click();


        } else {

            logger.info("Pop-up görülmedi");

        }


    }


    @Step("5S için <key> elementini bulana kadar yukarı kaydır")
    public void swipeFor5S(String element) {

        logger.info(" // BROWSERNAME: " + getCapability("deviceName") + " //");


        if (getCapability("deviceName").contains("5S")) {
            logger.info("5S için if koşulları gerçekleştiriliyor...");
            int maxRetryCount = 10;
            while (maxRetryCount > 0) {
                List<MobileElement> elements = findElemenstByKey(element);
                if (elements.size() > 0) {
                    if (!elements.get(0).isDisplayed()) {
                        maxRetryCount--;
                        swipeUpAccordingToPhoneSize();
                    } else {
                        elements.get(0).click();
                        logger.info(element + " elementine tıklandı");
                        break;
                    }
                } else {
                    maxRetryCount--;
                    swipeUpAccordingToPhoneSize();
                }
            }
        }

    }

    @Step("deneme checkbox")
    public void denemeCheckbox() {
        if (appiumDriver instanceof AndroidDriver) {

            contextText("WEBVIEW");
            findElementsWithoutAssert(By.cssSelector("label>i")).get(0).click();
            contextText("NATIVE_APP");

        } else {
            findElementWithoutAssert(By.xpath("//XCUIElementTypeStaticText[@name='okudum kabul ediyorum.']")).click();

        }

    }


    @Step("Debug step")
    public void debugStep() {

        logger.info("debug");
    }

    private void newWebDriverWait(int sec, int mil) {
        new WebDriverWait(appiumDriver, sec, mil);
    }


    private void backPage() {
        appiumDriver.navigate().back();
    }


    private void objectTextandClick(By by, String text) {
        MobileElement me = (MobileElement) appiumFluentWait.until(ExpectedConditions.presenceOfElementLocated(by));
        me.click();
        me.setValue(text);

    }

    private void contextText(String text) {

        appiumDriver.context(appiumDriver.getContextHandles().stream().filter(s -> s.contains(text)).findFirst().get());

    }

    private String getCapability(String text) {
        return appiumDriver.getCapabilities().getCapability(text).toString();

    }

    public boolean doesElementExistByKey(String key, int time) {
        SelectorInfo selectorInfo = selector.getSelectorInfo(key);
        try {
            WebDriverWait elementExist = new WebDriverWait(appiumDriver, time);
            elementExist.until(ExpectedConditions.visibilityOfElementLocated(selectorInfo.getBy()));
            return true;
        } catch (Exception e) {
            logger.info(key + " aranan elementi bulamadı");
            return false;
        }

    }

    public boolean doesElementClickableByKey(String key, int time) {
        SelectorInfo selectorInfo = selector.getSelectorInfo(key);
        try {
            WebDriverWait elementExist = new WebDriverWait(appiumDriver, time);
            elementExist.until(ExpectedConditions.elementToBeClickable(selectorInfo.getBy()));
            return true;
        } catch (Exception e) {
            logger.info(key + " aranan elementi bulamadı");
            return false;
        }

    }


    @Step("<key>,<startPointX>,<finishPointX> kaydır baakalım")
    public void sliderSwipe(String key, int startPointX, int finishPointX) {

        int coordinateX = appiumDriver.manage().window().getSize().width;
        int pointY = findElementByKey(key).getCenter().y;
        int firstPointX = (coordinateX * startPointX) / 100;
        int lastPointX = (coordinateX * finishPointX) / 100;

        TouchAction action = new TouchAction(appiumDriver);
        action
                .press(PointOption.point(firstPointX, pointY))
                .waitAction(WaitOptions.waitOptions(ofMillis(2000)))
                .moveTo(PointOption.point(lastPointX, pointY))
                .release().perform();

    }
    @Step("Verilen <x> <y> koordinatına tıkla")
    public void tapElementWithCoordinate(int x, int y) {
        TouchAction a2 = new TouchAction(appiumDriver);
        a2.tap(PointOption.point(x, y)).perform();
    }

    @Step("<key> li elementin  merkezine tıkla ")
    public void tapElementWithKey(String key) {

        Point point = findElementByKey(key).getCenter();
        TouchAction a2 = new TouchAction(appiumDriver);
        a2.tap(PointOption.point(point.x, point.y)).perform();
    }

    @Step("<key> li element varsa  <x> <y> koordinatına tıkla ")
    public void tapElementWithKeyCoordinate(String key,int x, int y) {

        logger.info("element varsa verilen koordinata tıkla ya girdi");
        MobileElement mobileElement;

        mobileElement = findElementByKeyWithoutAssert(key);

        if (mobileElement != null) {

            Point point = mobileElement.getLocation();
            logger.info(point.x + "  " + point.y);
            Dimension dimension = mobileElement.getSize();
            logger.info(dimension.width + "  " + dimension.height);
            TouchAction a2 = new TouchAction(appiumDriver);
            a2.tap(PointOption.point(point.x + (dimension.width * x)/100, point.y + (dimension.height * y)/100)).perform();
        }
    }

    @Step("<key> li elementin  merkezine  press ile çift tıkla ")
    public void pressElementWithKey(String key) {

        Point point = findElementByKey(key).getCenter();
        TouchAction a2 = new TouchAction(appiumDriver);
        a2.press(PointOption.point(point.x, point.y)).waitAction(WaitOptions.waitOptions(Duration.ofMillis(300)))
                .press(PointOption.point(point.x, point.y)).release().perform();

    }


    @Step("<key> li elementin  merkezine double tıkla ")
    public void pressElementWithKey2(String key) {
        Actions actions = new Actions(appiumDriver);
        actions.moveToElement(findElementByKey(key));
        actions.doubleClick();
        actions.perform();
        appiumDriver.getKeyboard();

    }

    @Step("<key> li elementi rasgele sec")
    public void chooseRandomProduct(String key){

        List<MobileElement> productList = new ArrayList<>();
        List<MobileElement> elements = findElemenstByKey(key);
        int elementsSize = elements.size();
        int height = appiumDriver.manage().window().getSize().height;
        for(int i=0;i< elementsSize;i++){
            MobileElement element = elements.get(i);
            int y = element.getCenter().getY();
            if( y>0 && y<(height-100)){
                productList.add(element);
            }
        }
        Random random = new Random();
        int randomNumber = random.nextInt(productList.size());
        productList.get(randomNumber).click();
    }




    @Step("<key> li elemente kadar <text> textine sahip değilse ve <timeout> saniyede bulamazsa swipe yappp")
    public void swipeAndFindwithKey(String key, String text, int timeout) {


        MobileElement sktYil1 = null;
        SelectorInfo selectorInfo = selector.getSelectorInfo(key);
        WebDriverWait wait = new WebDriverWait(appiumDriver, timeout);
        int count = 0;
        while (true) {
            count++;
            try {
                sktYil1 = (MobileElement) wait.until(ExpectedConditions.visibilityOfElementLocated(selectorInfo.getBy()));
                if (text.equals("") || sktYil1.getText().trim().equals(text)) {
                    break;
                }
            } catch (Exception e) {
                logger.info("Bulamadı");

            }
            if (count == 8) {

                Assert.fail("Element bulunamadı");
            }

            Dimension dimension = appiumDriver.manage().window().getSize();
            int startX1 = dimension.width / 2;
            int startY1 = (dimension.height * 75) / 100;
            int secondX1 = dimension.width / 2;
            int secondY1 = (dimension.height * 30) / 100;

            TouchAction action2 = new TouchAction(appiumDriver);

            action2
                    .press(PointOption.point(startX1, startY1))
                    .waitAction(WaitOptions.waitOptions(ofMillis(2000)))
                    .moveTo(PointOption.point(secondX1, secondY1))
                    .release()
                    .perform();

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }


    @Step("<key>li elementi bulana kadar <limit> kere swipe yap ve elementi bul")
    public void swipeKeyy(String key,int limit) throws InterruptedException {


        boolean isAppear = false;

        int windowHeight = this.getScreenHeight();
        for(int i = 0; i < limit; ++i) {
            try {

                Point elementLocation = findElementByKeyWithoutAssert(key).getLocation();
                logger.info(elementLocation.x + "  " + elementLocation.y);
                Dimension elementDimension = findElementByKeyWithoutAssert(key).getSize();
                logger.info(elementDimension.width + "  " + elementDimension.height);
                // logger.info(appiumDriver.getPageSource());
                if (elementDimension.height > 20) {
                    isAppear = true;
                    logger.info("aranan elementi buldu");
                    break;
                }
            } catch (Exception e) {
                System.out.println("Element ekranda görülmedi. Tekrar swipe ediliyor");
            }
            System.out.println("Element ekranda görülmedi. Tekrar swipe ediliyor");

            swipeUpAccordingToPhoneSize();
            waitBySecond(1);
        }

    }

    @Step("Urun stokta var mi kontrol et")
    public void stokKontrol() throws Exception {

        logger.info("Stok kontrolüne girdi");
        MobileElement element;
        element = findElementByKeyWithoutAssert("haberdarEt");
        waitBySecond(5);
        if(element != null){
            clickBybackButton();
            waitBySecond(2);
            swipeUpAccordingToPhoneSize();
            waitBySecond(5);
            clickByKey("hemenAlButonu");

        }
        logger.info("ürün stokta bulunuyor");

    }
    private Long getTimestamp() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return (timestamp.getTime());
    }

    @Step({"<key> li elementi bul, temizle ve rasgele  email değerini yaz",
            "Find element by <key> clear and send keys  random email"})
    public void RandomeMail(String key){
        Long timestamp = getTimestamp();

        MobileElement webElement = findElementByKey(key);
        webElement.clear();
        webElement.setValue("testotomasyon" + timestamp + "@sahabt.com");
    }
    @Step("Cinsiyet alanı kontrolü yap ve rasgele bir cinsiyet  seç")
    public void cinsiyetSec(){
        if (isElementPresent(By.id("com.koton.app:id/tv_man"))){
            logger.info("Cinsiyet alanı görüldü");
            List<MobileElement> productList = new ArrayList<>();
            List<MobileElement> elements = appiumDriver.findElements(By.xpath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.RelativeLayout/android.widget.RelativeLayout/android.widget.LinearLayout/android.widget.TextView"));
            int elementsSize = elements.size();
            int height = appiumDriver.manage().window().getSize().height;
            for(int i=0;i< elementsSize;i++){
                MobileElement element = elements.get(i);
                int y = element.getCenter().getY();
                if( y>0 && y<(height-100)){
                    productList.add(element);
                }
            }
            Random random = new Random();
            int randomNumber = random.nextInt(productList.size());
            productList.get(randomNumber).click();
        }else{
            logger.info("Cinsiyet alanı görülmedi !!!!!!!!!!!");
        }
    }

    @Step("Login kontrol")
    public void loginControl() throws InterruptedException {
        if (appiumDriver instanceof AndroidDriver){
            logger.info("Telefon işletim sistemi android !!!!!!!");
            if (isElementPresent(By.id("com.koton.app:id/tv_login"))){
                logger.info("Login alanı görünür olmadı");
            }else{
                logger.info("Login alanı görünür oldu");
                swipe(3);

                appiumDriver.findElement(By.id("com.koton.app:id/ll_logout")).click();
                waitBySecond(3);
                logger.info("Login kontrolü yapıldı ve logout işlemi gerçekleşti");

            }
        }else{
            logger.info("Telefon işletim sistemi ios!!!!!!!");
            if (isElementPresent(By.xpath("//XCUIElementTypeApplication[@name='Koton']/XCUIElementTypeWindow[1]/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeOther/XCUIElementTypeTable/XCUIElementTypeOther"))){
                logger.info("İos Login alanı görünür oldu");
                swipe(4);
                appiumDriver.findElement(By.xpath("//XCUIElementTypeStaticText[@name='Çıkış Yap']")).click();
                waitBySecond(3);
                logger.info("Login kontrolü yapıldı ve logout işlemi gerçekleşti");
            }else{
                logger.info("İos Login alanı görünür olmadı");

            }
        }

    }


    @Step("Sepet kontrülü yap")
    public void basketControl() throws InterruptedException {
        WebElement basket = appiumDriver.findElement(By.id("com.koton.app:id/tv_cart_count_badge"));
        int intBasket = 0;
        String strBasket = basket.getText();

        if (StringUtils.isNotEmpty(strBasket)) {
            intBasket = Integer.parseInt(strBasket);

            logger.info("Sepetteki ürün sayısının" + intBasket + " olduğu görüldü.");
            if (intBasket > 0) {
                appiumDriver.findElement(By.id("com.koton.app:id/iv_cart")).click();
                logger.info("Sepet simgesine tıklandı.");
                appiumDriver.findElement(By.id("com.koton.app:id/tv_edit_btn")).click();
                logger.info("düzenle butonuna tıklandı");
                waitBySecond(3);
                List<MobileElement> deleteButtons = appiumDriver.findElements(By.id("com.koton.app:id/ll_delete"));
                int elementsSize = deleteButtons.size();
                logger.info("Sepetteki ürün sayısın ======"+deleteButtons.size());
                for (int i=0;i< elementsSize;i++){
                    MobileElement element = deleteButtons.get(i);
                    element.click();
                }


            }

        }




    }

    @Step("Dropdown dan key <key> alanını seç")
    public void selectDropdawnoNKey(String key){
        appiumDriver.findElement(By.className("XCUIElementTypePickerWheel")).sendKeys(key);
    }


    @Step({"<key> li elementi bul basılı tut"})
    public void basiliTut(String key){
        MobileElement mobileElement = findElementByKey(key);

        TouchAction action = new TouchAction<>(appiumDriver);
        action
                .longPress(LongPressOptions.longPressOptions()
                        .withElement(ElementOption.element(mobileElement)).withDuration(Duration.ofSeconds(4)))
                .release()
                .perform();

    }

    @Step({"<key> li elementi bul <times> kere tıkla"})
    public void bulTikla(String key, int times){
        MobileElement mobileElement = findElementByKey(key);
        for (int i = 0; i < times; i++) {

            mobileElement.click();
        }
    }

    @Step({"<key> li elementi bul ve <times> kere sil"})
    public void clickAndDelete(String key,int times) {
        MobileElement mobileElement = findElementByKey(key);
        for (int i = 0; i < times; i++){
            mobileElement.sendKeys(Keys.BACK_SPACE);
        }
    }


}


