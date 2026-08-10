package base;

import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import drivers.DriverFactory;

public class BaseTest {

    protected WebDriver driver;

    // OLD setup without TestNG context sharing
    // @BeforeMethod
    // public void setup() {
    //     driver = DriverFactory.initDriver();
    //     driver.manage().timeouts().implicitlyWait(java.time.Duration.ofSeconds(5));
    // }


    // NEW setup with TestNG context sharing for listeners/screenshots
    @BeforeMethod(alwaysRun = true)
    public void setup(ITestContext context) {

        driver = DriverFactory.initDriver();

        // share current driver with TestNG listener
        context.setAttribute("driver", driver);

        driver.manage().timeouts()
                .implicitlyWait(java.time.Duration.ofSeconds(5));
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        DriverFactory.quitDriver();
    }
}
