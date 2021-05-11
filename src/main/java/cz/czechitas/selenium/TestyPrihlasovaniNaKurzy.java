package cz.czechitas.selenium;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.sql.Date;
import java.text.DateFormat;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class TestyPrihlasovaniNaKurzy {

    WebDriver prohlizec;
    String PrihlasovaciHeslo = "Test22";
    String NoveHeslo = "Test11";
    @BeforeEach
    public void setUp() {
//      System.setProperty("webdriver.gecko.driver", System.getProperty("user.home") + "/Java-Training/Selenium/geckodriver");
        System.setProperty("webdriver.gecko.driver", "C:\\Java-Training\\Selenium\\geckodriver.exe");
        prohlizec = new FirefoxDriver();
        prohlizec.manage().timeouts().implicitlyWait(600, TimeUnit.SECONDS);
    }

    //TlacitkoUSpesnePrihlasen se mi nedari lokalizovat lepsim Xpath :-(
    @Test
    public void RodicSePrihlasiDoAplikace() {
        prohlizec.navigate().to("https://cz-test-jedna.herokuapp.com/");
        PrihlaseniUzivatele("mar.buga@seznam.cz",PrihlasovaciHeslo);
        WebElement tlacitkoUspesnePrihlasen = prohlizec.findElement(By.xpath("//div[contains(@class,'nav-item dropdown')]/span"));
        Assertions.assertEquals("Přihlášen",tlacitkoUspesnePrihlasen.getText());

    }

    @Test
    public void RodicPrihlasiKurzPredPrihlasenimDoAplikace() {
        prohlizec.navigate().to("https://cz-test-jedna.herokuapp.com/");

        VyberKurzuAlgoritmizace();
        PrihlaseniUzivatele("mar.buga@seznam.cz",PrihlasovaciHeslo);
        NovaPrihlaskaNaKurz();

        WebElement PrihlasenyZak = prohlizec.findElement(By.xpath("//div/h1"));
        WebElement PrihlasenyKurz = prohlizec.findElement(By.xpath("//div/h4"));

        Assertions.assertEquals("Marek Beran", PrihlasenyZak.getText());
        Assertions.assertEquals("Termín konání: 05.06. - 11.06.2021\n" +
                "Základy algoritmizace", PrihlasenyKurz.getText());
    }

    @Test
    public void RodicPrihlasiKurzPredPrihlasenimDoAplikace_verzePoUpraveZadani() {
        prohlizec.navigate().to("https://cz-test-jedna.herokuapp.com/");

        VyberKurzuAlgoritmizace();
        PrihlaseniUzivatele("mar.buga@seznam.cz",PrihlasovaciHeslo);
        NovaPrihlaskaNaKurz();

        List<WebElement> ZalozkaNavigace = prohlizec.findElements(By.xpath("//div/a[contains(@class,'nav-item nav-link')]"));
        WebElement ZalozkaPrihlasky = ZalozkaNavigace.get(1);
        ZalozkaPrihlasky.click();
        WebElement JmenoPrihlaseneho = prohlizec.findElement(By.xpath("//tr/td[contains(@class,'dtr-control sorting_1')]"));
        WebElement Kategorie = prohlizec.findElement(By.xpath("//tr[contains(@class,'odd')]/td[2]"));


        Assertions.assertEquals("Marek Beran", JmenoPrihlaseneho.getText());
        Assertions.assertEquals("Základy algoritmizace", Kategorie.getText());

    }

    @Test
    public void RodicSePrihlasiDoAplikaceAPrihlasiKurz(){
        prohlizec.navigate().to("https://cz-test-jedna.herokuapp.com/");
        PrihlaseniUzivatele("mar.buga@seznam.cz", PrihlasovaciHeslo);
        WebElement TlacitkoVytvorNovouPrihlasku = prohlizec.findElement(By.cssSelector(".btn-info"));
        TlacitkoVytvorNovouPrihlasku.click();
        VyberKurzuAlgoritmizace();
        NovaPrihlaskaNaKurz();

        WebElement PrihlasenyZak = prohlizec.findElement(By.xpath("//div/h1"));
        WebElement PrihlasenyKurz = prohlizec.findElement(By.xpath("//div/h4"));

        Assertions.assertEquals("Marek Beran", PrihlasenyZak.getText());
        Assertions.assertEquals("Termín konání: 05.06. - 11.06.2021\n" +
                "Základy algoritmizace", PrihlasenyKurz.getText());

    }

    @Test
    public void RodicSePrihlasiDoAplikaceAPrihlasiKurz_VerzePoUpraveZadani(){
        prohlizec.navigate().to("https://cz-test-jedna.herokuapp.com/");
        PrihlaseniUzivatele("mar.buga@seznam.cz", PrihlasovaciHeslo);
        WebElement TlacitkoVytvorNovouPrihlasku = prohlizec.findElement(By.cssSelector(".btn-info"));
        TlacitkoVytvorNovouPrihlasku.click();
        VyberKurzuAlgoritmizace();
        NovaPrihlaskaNaKurz();

        List<WebElement> ZalozkaNavigace = prohlizec.findElements(By.xpath("//div/a[contains(@class,'nav-item nav-link')]"));
        WebElement ZalozkaPrihlasky = ZalozkaNavigace.get(1);
        ZalozkaPrihlasky.click();
        WebElement JmenoPrihlaseneho = prohlizec.findElement(By.xpath("//tr/td[contains(@class,'dtr-control sorting_1')]"));
        WebElement Kategorie = prohlizec.findElement(By.xpath("//tr[contains(@class,'odd')]/td[2]"));


        Assertions.assertEquals("Marek Beran", JmenoPrihlaseneho.getText());
        Assertions.assertEquals("Základy algoritmizace", Kategorie.getText());

    }

    @Test
    public void RodicSiZmeniHeslo() {

        ZmenaPrihlasovacihoHEsla();

        WebElement VyskakovaciOkno = prohlizec.findElement(By.xpath("//div/div[contains(@class,'toast-message')]"));
        Assertions.assertEquals("Údaje byly úspěšně uloženy",VyskakovaciOkno.getText());
    }

    @Test
    public void OvereniPlatnostiNovehoHesla() {
        prohlizec.navigate().to("https://cz-test-jedna.herokuapp.com/");
        PrihlaseniUzivatele("mar.buga@seznam.cz",NoveHeslo);
        WebElement tlacitkoUspesnePrihlasen = prohlizec.findElement(By.xpath("/html/body/div/header/nav/div/div[2]/div/span"));
        Assertions.assertEquals("Přihlášen",tlacitkoUspesnePrihlasen.getText());

    }

    public void ZmenaPrihlasovacihoHEsla() {
        prohlizec.navigate().to("https://cz-test-jedna.herokuapp.com/");
        PrihlaseniUzivatele("mar.buga@seznam.cz",PrihlasovaciHeslo);

        List<WebElement> Prihlaseni = prohlizec.findElements(By.xpath("//div/a[contains(@class,'dropdown-toggle')]"));
        WebElement PrihlasenyRodic = Prihlaseni.get(1);
        PrihlasenyRodic.click();
        List<WebElement> SeznamVolbyProfil = prohlizec.findElements(By.xpath("//div/a[contains(@class,'dropdown-item ')]"));
        WebElement VolbaProfil = SeznamVolbyProfil.get(1);

        VolbaProfil.click();
        WebElement PoleHeslo = prohlizec.findElement(By.id("password"));
        PoleHeslo.sendKeys(NoveHeslo);
        WebElement PoleKontrolaHesla = prohlizec.findElement(By.id("password-confirm"));
        PoleKontrolaHesla.sendKeys(NoveHeslo);
        WebElement TlacitkoZmenit = prohlizec.findElement(By.xpath("//div/button[@class ='btn btn-primary']"));
        TlacitkoZmenit.click();
    }

    public void VyberKurzuAlgoritmizace() {
        List<WebElement> ViceInfoOKurzech = prohlizec.findElements(By.xpath("//div/a[contains(@class,'btn btn-sm align-self-center btn-primary')]"));
        WebElement PrvniKurz = ViceInfoOKurzech.get(0);
        PrvniKurz.click();
        List<WebElement> Prihlasky = prohlizec.findElements(By.xpath("//div/a[contains(@class,'btn btn-sm align-self-center btn-primary')]"));
        WebElement PrihlaskaNaKurzAlgoritmizace = Prihlasky.get(0);
        PrihlaskaNaKurzAlgoritmizace.click();
    }

    public void NovaPrihlaskaNaKurz(){

        WebElement PoleTermin = prohlizec.findElement(By.xpath("//div/div[contains(@class,'filter-option-inner-inner')]"));
        PoleTermin.click();
        WebElement PrvniTermin = prohlizec.findElement(By.id("bs-select-1-0"));
        PrvniTermin.click();

        ZadejUdajeZaka("Marek","Beran", "10.02.1999");

        List<WebElement> SeznamPoliProPlatbuACheckboxy = prohlizec.findElements(By.xpath("//span/label[contains(@class,'custom-control-label')]"));
        WebElement TlacitkoPlatbaHotove = SeznamPoliProPlatbuACheckboxy.get(3);
        TlacitkoPlatbaHotove.click();
        WebElement CheckboxVseobecnePodminky = SeznamPoliProPlatbuACheckboxy.get(5);
        CheckboxVseobecnePodminky.click();

        WebElement TlacitkoVytvorPrihlasku = prohlizec.findElement(By.cssSelector("input.btn"));
        TlacitkoVytvorPrihlasku.click();
    }

    public void ZadejUdajeZaka (String Jmeno, String Prijmeni, String DatumNarozeni){
        WebElement JmenoZaka = prohlizec.findElement(By.id("forename"));
        JmenoZaka.sendKeys(Jmeno);
        WebElement PrijmeniZaka = prohlizec.findElement(By.id("surname"));
        PrijmeniZaka.sendKeys(Prijmeni);
        WebElement DatumNarozeniZaka = prohlizec.findElement(By.id("birthday"));
        DatumNarozeniZaka.sendKeys(DatumNarozeni);
    }

    public void PrihlaseniUzivatele(String EmailovyKlient, String HesloUzivatele) {

        WebElement tlacitkoPrihlasit = prohlizec.findElement(By.xpath("//div/div[contains(@class,'nav navbar-nav navbar-right ml-auto')]")) ;
        tlacitkoPrihlasit.click();
        WebElement poleEmail = prohlizec.findElement(By.id("email"));
        poleEmail.sendKeys(EmailovyKlient);
        WebElement poleHeslo = prohlizec.findElement(By.id("password"));
        poleHeslo.sendKeys(HesloUzivatele);
        WebElement tlacitkoSubmit = prohlizec.findElement(By.xpath("//div/button[contains(@class,'btn btn-primary')]"));
        tlacitkoSubmit.click();
    }

    @AfterEach
    public void tearDown() {
        prohlizec.close();
    }
}
