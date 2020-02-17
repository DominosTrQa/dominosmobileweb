package com.saha.dominos;

import com.saha.dominos.selector.Selector;
import com.saha.dominos.selector.SelectorFactory;
import com.saha.dominos.selector.SelectorType;
import com.thoughtworks.gauge.AfterScenario;
import com.thoughtworks.gauge.BeforeScenario;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.AndroidMobileCapabilityType;
import io.appium.java_client.remote.IOSMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.remote.MobilePlatform;
import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.FluentWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class HookImpl {
    private Logger logger = LoggerFactory.getLogger(getClass());
    protected static AppiumDriver<MobileElement> appiumDriver;
    protected static FluentWait<AppiumDriver<MobileElement>> appiumFluentWait;
    protected boolean localAndroid = false;
    protected static Selector selector ;


    @BeforeScenario
    public void beforeScenario() throws MalformedURLException {
        System.out.println("************Test Başlıyor*************");
        if (StringUtils.isEmpty(System.getenv("key"))) {
            if (localAndroid) {
                logger.info("Local Browser");
                DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
                desiredCapabilities
                       .setCapability(MobileCapabilityType.PLATFORM, MobilePlatform.ANDROID);
                desiredCapabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Galaxy S6 edge");
                desiredCapabilities.setCapability(MobileCapabilityType.UDID, "05157df53167bb14");

                desiredCapabilities
                        .setCapability(AndroidMobileCapabilityType.APP_PACKAGE,
                                "com.app");
                desiredCapabilities
                        .setCapability(AndroidMobileCapabilityType.APP_ACTIVITY,
                                "com.inomera.koton.ui.SplashActivity");
                desiredCapabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "uiautomator2");
                desiredCapabilities
                        .setCapability(MobileCapabilityType.NO_RESET, true);
                desiredCapabilities
                        .setCapability(MobileCapabilityType.FULL_RESET, false);
                desiredCapabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 3000);
                desiredCapabilities.setCapability("unicodeKeyboard", true);
                desiredCapabilities.setCapability("resetKeyboard", true);
                URL url = new URL("http://127.0.0.1:4723/wd/hub");
                appiumDriver = new AndroidDriver(url, desiredCapabilities);
            } else {
                DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
                desiredCapabilities
                        .setCapability(MobileCapabilityType.PLATFORM_NAME, MobilePlatform.IOS);
                desiredCapabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "XCUITest");
                desiredCapabilities
                        .setCapability(MobileCapabilityType.UDID, "7513a1c1a9f5a4ab95773bdc26cd62120daf89c7");
                desiredCapabilities
                        .setCapability(IOSMobileCapabilityType.BUNDLE_ID, "com.apple.mobilesafari");
                desiredCapabilities
                        .setCapability(MobileCapabilityType.DEVICE_NAME, "testinium's iPhone");

                desiredCapabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, "12.1.4");
                desiredCapabilities.setCapability(MobileCapabilityType.NO_RESET, true);
                desiredCapabilities.setCapability(MobileCapabilityType.FULL_RESET, false);
                desiredCapabilities.setCapability("connectHardwareKeyboard", false);

                desiredCapabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 60000);

                desiredCapabilities.setCapability("sendKeyStrategy","setValue");

                URL url = new URL("http://127.0.0.1:4723/wd/hub");
                appiumDriver = new IOSDriver(url, desiredCapabilities);


            }
        } else {
            String hubURL = "http://hub.testinium.io/wd/hub";
            DesiredCapabilities capabilities = new DesiredCapabilities();
            System.out.println("key:" + System.getenv("key"));
            System.out.println("platform" + System.getenv("platform"));
            System.out.println("version" + System.getenv("version"));
            if (System.getenv("platform").equals("ANDROID")) {
                capabilities.setCapability("key", System.getenv("key"));
                capabilities
                        .setCapability(AndroidMobileCapabilityType.APP_PACKAGE,
                                "com.koton.app");
                capabilities
                        .setCapability(AndroidMobileCapabilityType.APP_ACTIVITY,
                                "com.inomera.koton.ui.SplashActivity");
                 capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME,"uiautomator2");
                capabilities.setCapability(MobileCapabilityType.NO_RESET, true);
                capabilities.setCapability(MobileCapabilityType.FULL_RESET, false);
                capabilities.setCapability("unicodeKeyboard", false);
                capabilities.setCapability("resetKeyboard", false);
                appiumDriver = new AndroidDriver(new URL(hubURL), capabilities);
                localAndroid = true;
            } else {
               // capabilities.setCapability(CapabilityType.PLATFORM, Platform.MAC);
                capabilities.setCapability("usePrebuiltWDA", true);
                //capabilities.setCapability("appium:maxTypeFrequency", 5);
                capabilities.setCapability("key", System.getenv("key"));
                capabilities.setCapability("waitForAppScript", "$.delay(1000);");
                capabilities.setCapability("bundleId", "com.koton");
                capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT,60000);

                capabilities.setCapability("usePrebuiltWDA",true); //TimeOut
                appiumDriver = new IOSDriver(new URL(hubURL), capabilities);
                localAndroid = false;
            }
        }
        selector = SelectorFactory
                .createElementHelper(localAndroid ? SelectorType.ANDROID : SelectorType.IOS);
        //appiumDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        appiumFluentWait = new FluentWait<AppiumDriver<MobileElement>>(appiumDriver);
       appiumFluentWait.withTimeout(Duration.ofSeconds(30))
                .pollingEvery(Duration.ofMillis(450))
                .ignoring(NoSuchElementException.class);
    }

    @AfterScenario
    public void afterScenario() {
        if(appiumDriver != null)
            appiumDriver.quit();
    }

}
