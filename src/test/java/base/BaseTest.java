package base;

import com.thoughtworks.gauge.AfterScenario;
import com.thoughtworks.gauge.BeforeScenario;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.AutomationName;
import io.appium.java_client.remote.IOSMobileCapabilityType;
import io.appium.java_client.remote.MobileCapabilityType;
import io.appium.java_client.remote.MobilePlatform;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class BaseTest {

    public static AppiumDriver driver;
    public WebDriver waDriver;

    protected static String jsonString = null;
    //	ReadProperties currentUser = new ReadProperties();
    //protected final Logger log = Logger.getLogger(BaseTest.class);
    public static final String URL = "http://dpr-staging.dominos.com.tr/";
    //public static final String URL = System.getenv("URL");
    public static final String LOCALHUB = "http://127.0.0.1:4723/wd/hub";
    private static String currentPhone = "";

    @BeforeScenario
    public void setUp() throws Exception {
        System.out.println("*****************Test*****************");
        DesiredCapabilities capabilities = new DesiredCapabilities();
        String selectPlatform = "ios";
        System.out.println("getEnvKey::::" + System.getenv("key"));
        if (StringUtils.isEmpty(System.getenv("key"))) {
            System.out.println("LOCAL");
            if ("android".equalsIgnoreCase(selectPlatform)) {
                capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, "chrome");
                capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, MobilePlatform.ANDROID);
                capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "emulator");
                capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "UiAutomator2");
                ChromeOptions options = new ChromeOptions();
                options.addArguments("test-type");
                options.addArguments("ignore-certificate-errors");
                options.addArguments("disable-translate");
                options.addArguments("--disable-notifications");
                options.setExperimentalOption("androidPackage", "com.android.chrome");
                capabilities.setCapability(ChromeOptions.CAPABILITY, options);
                System.setProperty("webdriver.http.factory", "apache");
                driver = new AndroidDriver<>(new URL(LOCALHUB), capabilities);
                driver.get(URL);
                System.out.println(driver.manage().getCookies());
                JavascriptExecutor myExecutor = ((JavascriptExecutor) driver);
                myExecutor.executeScript("loadNewProductPage();");
                System.out.println(driver.manage().getCookies());
            } else if ("ios".equalsIgnoreCase(selectPlatform)) {
                capabilities = new DesiredCapabilities();
                capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "iOS");
                capabilities.setCapability(MobileCapabilityType.PLATFORM_VERSION, "12.1.4");
                capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, "Safari");
                capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "testinium's iPhone");
                capabilities.setCapability("automationName", "XCUITest");
                capabilities.setCapability(MobileCapabilityType.UDID, "7513a1c1a9f5a4ab95773bdc26cd62120daf89c7");
                DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
                desiredCapabilities.setCapability(MobileCapabilityType.BROWSER_NAME, "Safari");

                driver = new IOSDriver<>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
                driver.get(URL);
                waitUntilModanisaIconVisible();
                for (Cookie cookie : driver.manage().getCookies()) {
                    System.out.println("Cooooookie " + cookie);
                    System.out.println("Cookie name : " + cookie.getName() + " value : " + cookie.getValue());
                }   System.out.println("key cookie value is " + driver.manage().getCookieNamed("key"));
                //driver.manage().deleteCookieNamed("key");
              /*  JavascriptExecutor myExecutor = ((JavascriptExecutor) driver);
                myExecutor.executeScript("$.cookie('mcgk', 'PORE', { expires: 1, path: '/', domain: null })");
                myExecutor.executeScript("$.cookie('MODANISA_SID', null , { expires: -1, path: '/', domain: null })");
                myExecutor.executeScript("$.cookie('PHPSESSID', null, { expires: -1, path: '/', domain: null })");
                myExecutor.executeScript("location.reload()");*/


                driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);

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
                driver = new AndroidDriver<>(new URL("http://hub.testinium.io/wd/hub"), capabilities);
                driver.get(URL);
                String versionOfDevice = System.getenv("version");
                System.out.println("Version of running device is " + versionOfDevice);
               // driver.manage().deleteAllCookies();

              /*  driver.manage().addCookie(new Cookie("mcgk", System.getenv("cookie_value")));
                Cookie cookie = new Cookie("mntest","1");
                driver.manage().addCookie(cookie);
                System.out.println("Test cookie is " +driver.manage().getCookieNamed("mntest"));*/
               // JavascriptExecutor myExecutor = ((JavascriptExecutor) driver);
               // myExecutor.executeScript("loadNewProductPage();");


             /*   if (!versionOfDevice.equals("7.0")) {
                    driver.manage().addCookie(new Cookie("mcgk", "AXCV"));
                    System.out.println("Added cookie is LKFD");

                } else {
                    driver.manage().addCookie(new Cookie("mcgk", "QWYT"));

                }*/
            } else {
                System.out.println("IOS - Mobile Web");
                capabilities = new DesiredCapabilities();
                capabilities.setCapability("key", System.getenv("key"));
                capabilities.setCapability(CapabilityType.PLATFORM, Platform.MAC);
                capabilities.setCapability("xcodeOrgId", "PMLH8MF4G9");
                capabilities.setCapability("xcodeSigningId", "iPhone Developer");
                capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, AutomationName.IOS_XCUI_TEST);
                capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, BrowserType.SAFARI);
                capabilities.setCapability("cleanSession", true);
                capabilities.setCapability("ensureCleanSession", true);
                capabilities.setCapability("technologyPreview", true);
                driver = new IOSDriver<WebElement>(new URL("http://hub.testinium.io/wd/hub"), capabilities);
                driver.get(URL);
                try {
                    Thread.sleep(20000);
                } catch (InterruptedException e) {
                }

                waitUntilModanisaIconVisible();
                System.out.println("Remove cookie");
                for (Cookie cookie : driver.manage().getCookies()) {
                    driver.manage().deleteCookie(cookie);
                }

               // driver.manage().addCookie(new Cookie("mcgk", "QWYT"));


                for (Cookie cookie : driver.manage().getCookies()) {
                    System.out.println("Cookie name : " + cookie.getName() + " value : " + cookie.getValue());
                }

                driver.navigate().refresh();

                driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);


            }
        }
    }

    private void setCurrentPhone() {
        Map<String, Object> obj = (Map<String, Object>) ((RemoteWebDriver) driver).getCapabilities()
                .getCapability("desired");

        for (Entry<?, ?> s : obj.entrySet()) {
            //           log.info("'" + s.getKey() + "' - '" + s.getValue() + "'");
            if ("deviceName".equals(s.getKey())) {
                currentPhone = String.valueOf(s.getValue());
                break;
            }
        }
    }

    @AfterScenario
    public void tearDown() throws Exception {
        driver.quit();
    }

    public void waitUntilModanisaIconVisible() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, 10, 10);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("pageHeader-logo")));
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }


    public static String getCurrentPhone() {
        return currentPhone;
    }

    public static void setCurrentPhone(String currentPhone) {
        BaseTest.currentPhone = currentPhone;
    }

}
