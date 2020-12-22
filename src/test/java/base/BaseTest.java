package base;

import com.thoughtworks.gauge.AfterScenario;
import com.thoughtworks.gauge.BeforeScenario;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.AutomationName;
import io.appium.java_client.remote.MobileCapabilityType;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class BaseTest {

    public static AppiumDriver webDriver;

    @BeforeScenario
    public static void setUp() throws MalformedURLException, Exception {
        System.out.println("*****************Test*****************");
        String selectPlatform = "ios";
        DesiredCapabilities capabilities = new DesiredCapabilities();


        if (StringUtils.isEmpty(System.getenv("key")))
        {


            System.out.println("LOCAL");
            if ("android".equalsIgnoreCase(selectPlatform)) {
                System.out.println("android");
                Map<String, Object> prefs = new HashMap<String, Object>();
                prefs.put("profile.default_content_setting_values.notifications", 2);
                capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, BrowserType.CHROME);
                capabilities.setCapability(MobileCapabilityType.PLATFORM, Platform.ANDROID);
                capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
                capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Galaxy A71");
                //capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "uiautomator2");
                //capabilities.setCapability(MobileCapabilityType.UDID, "52108da8eab3c393");
                capabilities.setCapability(MobileCapabilityType.VERSION, "10");

                URL url = new URL("http://127.0.0.1:4723/wd/hub");
                webDriver = new AndroidDriver(url,capabilities);
            //    webDriver.get("http://dpe-preprod.dominos.com.tr/");
                Thread.sleep(5000);

        }else if ("ios".equalsIgnoreCase(selectPlatform)) {
                //capabilities = new DesiredCapabilities();
                capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "iOS");
                capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, "11.1.1");
                capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, "Safari");
                capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "iPhone");
               // capabilities.setCapability("automationName", "XCUITest");
                capabilities.setCapability(MobileCapabilityType.UDID, "2d328345dcc44fb8a7cd661c4ea06fd9291225a3");
                DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
               // desiredCapabilities.setCapability(MobileCapabilityType.BROWSER_NAME, "Safari");
                capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, AutomationName.IOS_XCUI_TEST);
                capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, BrowserType.SAFARI);

                webDriver = new IOSDriver<>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
                //webDriver.get("http://dpe-preprod.dominos.com.tr/");

                for (Cookie cookie : webDriver.manage().getCookies()) {
                    System.out.println("Cooooookie " + cookie);
                    System.out.println("Cookie name : " + cookie.getName() + " value : " + cookie.getValue());
                }   System.out.println("key cookie value is " + webDriver.manage().getCookieNamed("key"));
                //driver.manage().deleteCookieNamed("key");

                webDriver.manage().timeouts().implicitlyWait(45, TimeUnit.SECONDS);

            }

        } else {

            String cookieValue = System.getenv("cookie_value");
            System.out.println("cookie value from testinium is " + cookieValue);
            System.out.println("TESTINIUM");
            if ("ANDROID".equals(System.getenv("platform"))) {
                ChromeOptions options = new ChromeOptions();
                capabilities.setCapability(CapabilityType.PLATFORM, Platform.ANDROID);
                capabilities.setCapability("key", System.getenv("key"));
                capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, "chrome");
                capabilities.setCapability("rotatable", true);
                options.addArguments("test-type");
                options.addArguments("ignore-certificate-errors");
                options.addArguments("disable-translate");
                options.addArguments("--dns-prefetch-disable");
                options.addArguments("--disable-notifications");
                options.addArguments("--host-resolver-rules=MAP *.useinsider.* 127.0.0.1");
                capabilities.setCapability(ChromeOptions.CAPABILITY, options);
                //    capabilities.setCapability("appPackage", "com.android.chrome");
                //  capabilities.setCapability("appActivity", "com.google.android.apps.chrome.Main");
                capabilities.setCapability("unicodeKeyboard", true);
                capabilities.setCapability("resetKeyboard", true);
                capabilities.setCapability("noReset", true);
                webDriver = new AndroidDriver<>(new URL("http://hub.testinium.io/wd/hub"), capabilities);
             //   webDriver.get("http://dpe-preprod.dominos.com.tr/");
                String versionOfDevice = System.getenv("version");
                System.out.println("Version of running device is " + versionOfDevice);
                // driver.manage().deleteAllCookies();

                }
             else {
                System.out.println("IOS - Mobile Web");
                capabilities.setCapability("key", System.getenv("key"));
                capabilities.setCapability(CapabilityType.PLATFORM, Platform.MAC);
                capabilities.setCapability("xcodeOrgId", "PMLH8MF4G9");
                capabilities.setCapability("xcodeSigningId", "iPhone Developer");
                capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, AutomationName.IOS_XCUI_TEST);
                //capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, BrowserType.SAFARI);
                capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, "Safari");
                capabilities.setCapability("cleanSession", true);
                capabilities.setCapability("ensureCleanSession", true);
                capabilities.setCapability("technologyPreview", true);
                webDriver = new IOSDriver<WebElement>(new URL("http://hub.testinium.io/wd/hub"), capabilities);
              //  webDriver.get("http://dpe-preprod.dominos.com.tr/");
                try {
                    Thread.sleep(20000);
                } catch (InterruptedException e) {
                }


                // driver.manage().addCookie(new Cookie("mcgk", "QWYT"));


                for (Cookie cookie : webDriver.manage().getCookies()) {
                    System.out.println("Cookie name : " + cookie.getName() + " value : " + cookie.getValue());
                }

            webDriver.navigate().refresh();

            webDriver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);


                   }
            }
        }



    @AfterScenario
    public void tearDown() throws Exception {
        webDriver.quit();
    }


}



