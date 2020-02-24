package page;

import base.BasePageUtil;
import com.thoughtworks.gauge.Step;
import org.openqa.selenium.WebElement;

import static constants.deneme.GelAl;
import static constants.deneme.hamburger;

public class deneme extends BasePageUtil  {
    public static WebElement tempAddress = null;
    public static String randomAddress = "";

    @Step("gel al tıkla")
    public void gelAl(){
        clickObjectBy(GelAl);
    }

    @Step("gel al tıkla hamburger")
    public void hamburger(){
        waitSeconds(2);
        clickObjectBy(hamburger);
        waitSeconds(10);
    }



}