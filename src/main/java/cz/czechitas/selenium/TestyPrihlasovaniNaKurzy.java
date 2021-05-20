package cz.czechitas.selenium;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class TestyPrihlasovaniNaKurzy {

    WebDriver prohlizec;

    private static final String URL_APLIKACE = "https://cz-test-dva.herokuapp.com/";

    String prihlasovaciHeslo = "Test22";
    String noveHeslo = "Test11";

    @BeforeEach
    public void setUp() {
//      System.setProperty("webdriver.gecko.driver", System.getProperty("user.home") + "/Java-Training/Selenium/geckodriver");
        System.setProperty("webdriver.gecko.driver", "C:\\Java-Training\\Selenium\\geckodriver.exe");
        prohlizec = new FirefoxDriver();
        prohlizec.manage().timeouts().implicitlyWait(600, TimeUnit.SECONDS);
    }

    //TlacitkoUSpesnePrihlasen se mi nedari lokalizovat lepsim Xpath :-(
    @Test
    public void rodicSeMusiBytSchopenPrihlasitDoAplikace() {
        prohlizec.navigate().to(URL_APLIKACE);
        prihlaseniUzivatele("mar.buga@seznam.cz", prihlasovaciHeslo);

        WebElement tlacitkoUspesnePrihlasen = prohlizec.findElement(By.xpath("//div[contains(@class,'nav-item dropdown')]/span"));
        Assertions.assertEquals("Přihlášen", tlacitkoUspesnePrihlasen.getText());

    }

    @Test
    public void rodicMusiBytSchopenPrihlasitKurzPredPrihlasenimDoAplikace() {
        prohlizec.navigate().to(URL_APLIKACE);

        vyberKurzuAlgoritmizace();
        prihlaseniUzivatele("mar.buga@seznam.cz", prihlasovaciHeslo);
        vyplnUdajeNovePrihlasky();

        WebElement prihlasenyZak = prohlizec.findElement(By.xpath("//div/h1"));
        WebElement prihlasenyKurz = prohlizec.findElement(By.xpath("//div/h4"));
        Assertions.assertEquals("Marek Beran", prihlasenyZak.getText());
        Assertions.assertEquals("Termín konání: 05.06. - 11.06.2021\n" +
                "Základy algoritmizace", prihlasenyKurz.getText());
    }

    @Test
    public void rodicMusiBytSchopenPrihlasitKurzPredPrihlasenimDoAplikace_verzePoUpraveZadani() {
        prohlizec.navigate().to(URL_APLIKACE);

        vyberKurzuAlgoritmizace();
        prihlaseniUzivatele("mar.buga@seznam.cz", prihlasovaciHeslo);
        vyplnUdajeNovePrihlasky();

        List<WebElement> zalozkaNavigace = prohlizec.findElements(By.xpath("//div/a[contains(@class,'nav-item nav-link')]"));
        WebElement zalozkaPrihlasky = zalozkaNavigace.get(1);
        zalozkaPrihlasky.click();

        WebElement jmenoPrihlaseneho = prohlizec.findElement(By.xpath("//tr/td[contains(@class,'dtr-control sorting_1')]"));
        WebElement kategorie = prohlizec.findElement(By.xpath("//tr[contains(@class,'odd')]/td[2]"));
        Assertions.assertEquals("Marek Beran", jmenoPrihlaseneho.getText());
        Assertions.assertEquals("Základy algoritmizace", kategorie.getText());
    }

    @Test
    public void rodicSeMusiBytSchopenPrihlasitDoAplikaceANaslednePrihlasitKurz() {
        prohlizec.navigate().to(URL_APLIKACE);

        prihlaseniUzivatele("mar.buga@seznam.cz", prihlasovaciHeslo);
        vytvorNovouPrihlasku();
        vyberKurzuAlgoritmizace();
        vyplnUdajeNovePrihlasky();

        WebElement prihlasenyZak = prohlizec.findElement(By.xpath("//div/h1"));
        WebElement prihlasenyKurz = prohlizec.findElement(By.xpath("//div/h4"));
        Assertions.assertEquals("Marek Beran", prihlasenyZak.getText());
        Assertions.assertEquals("Termín konání: 05.06. - 11.06.2021\n" +
                "Základy algoritmizace", prihlasenyKurz.getText());
    }

    public void vytvorNovouPrihlasku() {
        WebElement tlacitkoVytvorNovouPrihlasku = prohlizec.findElement(By.cssSelector(".btn-info"));
        tlacitkoVytvorNovouPrihlasku.click();
    }

    @Test
    public void rodicSeMusiBytSchopenPrihlasitDoAplikaceANaslednePrihlasitKurz_poUpraveZadani() {
        prohlizec.navigate().to(URL_APLIKACE);

        prihlaseniUzivatele("mar.buga@seznam.cz", prihlasovaciHeslo);
        vytvorNovouPrihlasku();
        vyberKurzuAlgoritmizace();
        vyplnUdajeNovePrihlasky();

        List<WebElement> zalozkaNavigace = prohlizec.findElements(By.xpath("//div/a[contains(@class,'nav-item nav-link')]"));
        WebElement zalozkaPrihlasky = zalozkaNavigace.get(1);
        zalozkaPrihlasky.click();

        WebElement jmenoPrihlaseneho = prohlizec.findElement(By.xpath("//tr/td[contains(@class,'dtr-control sorting_1')]"));
        WebElement kategorie = prohlizec.findElement(By.xpath("//tr[contains(@class,'odd')]/td[2]"));
        Assertions.assertEquals("Marek Beran", jmenoPrihlaseneho.getText());
        Assertions.assertEquals("Základy algoritmizace", kategorie.getText());
    }

    @Test
    public void rodicSiMusiBytSchopenZmenitHeslo() {
        zmenaPrihlasovacihoHesla();

        WebElement vyskakovaciOkno = prohlizec.findElement(By.xpath("//div/div[contains(@class,'toast-message')]"));
        Assertions.assertEquals("Údaje byly úspěšně uloženy", vyskakovaciOkno.getText());
    }

    @Test
    public void rodicSeMusiBytSchopenPrihlasitPoZmeneHesla() {
        prohlizec.navigate().to("https://cz-test-jedna.herokuapp.com/");
        prihlaseniUzivatele("mar.buga@seznam.cz", noveHeslo);
        WebElement tlacitkoUspesnePrihlasen = prohlizec.findElement(By.xpath("//div[contains(@class,'nav-item dropdown')]/span"));
        Assertions.assertEquals("Přihlášen", tlacitkoUspesnePrihlasen.getText());
    }

    public void zmenaPrihlasovacihoHesla() {
        prohlizec.navigate().to(URL_APLIKACE);
        prihlaseniUzivatele("mar.buga@seznam.cz", prihlasovaciHeslo);

        List<WebElement> prihlaseni = prohlizec.findElements(By.xpath("//div/a[contains(@class,'dropdown-toggle')]"));
        WebElement prihlasenyRodic = prihlaseni.get(1);
        prihlasenyRodic.click();

        List<WebElement> seznamVolbyProfil = prohlizec.findElements(By.xpath("//div/a[contains(@class,'dropdown-item ')]"));
        WebElement volbaProfil = seznamVolbyProfil.get(1);

        volbaProfil.click();
        vyplnPoleHeslo(noveHeslo);

        WebElement poleKontrolaHesla = prohlizec.findElement(By.id("password-confirm"));
        poleKontrolaHesla.sendKeys(noveHeslo);

        WebElement tlacitkoZmenit = prohlizec.findElement(By.xpath("//div/button[@class ='btn btn-primary']"));
        tlacitkoZmenit.click();
    }

    public void vyberKurzuAlgoritmizace() {
        List<WebElement> viceInfoOKurzech = prohlizec.findElements(By.xpath("//div/a[contains(@class,'btn btn-sm align-self-center btn-primary')]"));
        WebElement prvniKurz = viceInfoOKurzech.get(0);
        prvniKurz.click();

        List<WebElement> prihlasky = prohlizec.findElements(By.xpath("//div/a[contains(@class,'btn btn-sm align-self-center btn-primary')]"));
        WebElement prihlaskaNaKurzAlgoritmizace = prihlasky.get(0);
        prihlaskaNaKurzAlgoritmizace.click();
    }

    public void vyplnUdajeNovePrihlasky() {
        vyberPrvniTerminKurzu();
        zadejUdajeZaka("Marek", "Beran", "10.02.1999");
        vyberPlatbuHotove();
        zatrhniSouhlasPodminek();
        odesliVyplnenouPrihlasku();
    }

    public void odesliVyplnenouPrihlasku() {
        WebElement tlacitkoVytvorPrihlasku = prohlizec.findElement(By.cssSelector("input.btn"));
        tlacitkoVytvorPrihlasku.click();
    }

    public void vyberPrvniTerminKurzu() {
        WebElement poleTermin = prohlizec.findElement(By.xpath("//div/div[contains(@class,'filter-option-inner-inner')]"));
        poleTermin.click();

        WebElement prvniTermin = prohlizec.findElement(By.id("bs-select-1-0"));
        prvniTermin.click();
    }

    public void zadejUdajeZaka(String jmeno, String prijmeni, String datumNarozeni) {
        zadejJmenoZaka(jmeno);
        zadejPrijmeniZaka(prijmeni);
        zadejDatumNarozeniZaka(datumNarozeni);
    }

    public void zadejDatumNarozeniZaka(String datumNarozeni) {
        WebElement datumNarozeniZaka = prohlizec.findElement(By.id("birthday"));
        datumNarozeniZaka.sendKeys(datumNarozeni);
    }

    public void zadejPrijmeniZaka(String prijmeni) {
        WebElement prijmeniZaka = prohlizec.findElement(By.id("surname"));
        prijmeniZaka.sendKeys(prijmeni + System.currentTimeMillis());
    }

    public void zadejJmenoZaka(String jmeno) {
        WebElement JmenoZaka = prohlizec.findElement(By.id("forename"));
        JmenoZaka.sendKeys(jmeno);
    }

    public void prihlaseniUzivatele(String emailovyKlient, String hesloUzivatele) {
        WebElement tlacitkoPrihlasit = prohlizec.findElement(By.xpath("//div/div[contains(@class,'nav navbar-nav navbar-right ml-auto')]"));
        tlacitkoPrihlasit.click();

        vyplnPoleEmail(emailovyKlient);
        vyplnPoleHeslo(hesloUzivatele);

        WebElement tlacitkoSubmit = prohlizec.findElement(By.xpath("//div/button[contains(@class,'btn btn-primary')]"));
        tlacitkoSubmit.click();
    }

    private void vyplnPoleHeslo(String hesloUzivatele) {
        WebElement poleHeslo = prohlizec.findElement(By.id("password"));
        poleHeslo.sendKeys(hesloUzivatele);
    }

    public void vyplnPoleEmail(String emailovyKlient) {
        WebElement poleEmail = prohlizec.findElement(By.id("email"));
        poleEmail.sendKeys(emailovyKlient);
    }

    public void vyberPlatbuHotove() {
        List<WebElement> seznamPoliProPlatbuACheckboxy = prohlizec.findElements(By.xpath("//span/label[contains(@class,'custom-control-label')]"));
        WebElement tlacitkoPlatbaHotove = seznamPoliProPlatbuACheckboxy.get(3);
        tlacitkoPlatbaHotove.click();
    }

    public void zatrhniSouhlasPodminek() {
        List<WebElement> seznamPoliProPlatbuACheckboxy = prohlizec.findElements(By.xpath("//span/label[contains(@class,'custom-control-label')]"));
        WebElement checkboxVseobecnePodminky = seznamPoliProPlatbuACheckboxy.get(5);
        checkboxVseobecnePodminky.click();
    }

    @AfterEach
    public void tearDown() {
        prohlizec.close();
    }
}
