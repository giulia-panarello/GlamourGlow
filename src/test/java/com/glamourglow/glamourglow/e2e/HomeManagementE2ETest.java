package com.glamourglow.glamourglow.e2e;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.WebElement;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class HomeManagementE2ETest {

    private static final String BASE_URL =
            "http://localhost:8080/GlamourGlow_war_exploded";

    private static final String TEST_EMAIL =
            "integration.home@glamourglow.test";

    private static final String TEST_PASSWORD =
            "test_password";

    private WebDriver driver;

    @BeforeEach
    void setUp() {
        driver = new ChromeDriver();
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
    //        driver.quit();
        }
    }

    @Test
    @Order(1)
    void paginaLogin_vieneVisualizzataCorrettamente() {

        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=HomeManagement.viewlogin");

        assertTrue(
                driver.getTitle().contains("Login")
        );

        assertTrue(
                driver.findElement(By.name("Email")).isDisplayed()
        );

        assertTrue(
                driver.findElement(By.name("Password")).isDisplayed()
        );

        assertTrue(
                driver.findElement(
                        By.cssSelector("button[type='submit']")
                ).isDisplayed()
        );
    }

    @Test
    @Order(2)
    void login_conCredenzialiCorrette_effettuaAccesso() {

        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=HomeManagement.viewlogin");

        driver.findElement(By.name("Email"))
                .sendKeys(TEST_EMAIL);

        driver.findElement(By.name("Password"))
                .sendKeys(TEST_PASSWORD);

        driver.findElement(
                By.cssSelector("button[type='submit']")
        ).click();

        assertTrue(
                driver.getPageSource().contains("GLAMOURGLOW")
        );
    }

    @Test
    @Order(3)
    void login_conPasswordErrata_mostraErrore() {

        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=HomeManagement.viewlogin");

        driver.findElement(By.name("Email"))
                .sendKeys(TEST_EMAIL);

        driver.findElement(By.name("Password"))
                .sendKeys("password_sbagliata");

        driver.findElement(
                By.cssSelector("button[type='submit']")
        ).click();

        assertTrue(
                driver.getPageSource()
                        .contains("Email o password errati!")
        );
    }

    @Test
    @Order(4)
    void logout_effettuaDisconnessione() {

        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=HomeManagement.viewlogin");

        driver.findElement(By.name("Email"))
                .sendKeys(TEST_EMAIL);

        driver.findElement(By.name("Password"))
                .sendKeys(TEST_PASSWORD);

        driver.findElement(
                By.cssSelector("button[type='submit']")
        ).click();

        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=HomeManagement.logout");

        assertTrue(
                driver.getPageSource()
                        .contains("HomeManagement.viewlogin")
        );
    }

    @Test
    @Order(5)
    void ricercaProdotto_mostraRisultato() {

        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=HomeManagement.viewhome");

        driver.findElement(By.name("Nomeprodotto"))
                .sendKeys("Pro Longwear Concealer");

        driver.findElement(
                By.cssSelector("button[type='submit']")
        ).click();

        assertTrue(
                driver.getPageSource()
                        .contains("Pro Longwear Concealer")
        );
    }

    @Test
    @Order(6)
    void ricercaProdotto_apreSchedaProdotto() {

        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=HomeManagement.viewhome");

        driver.findElement(By.name("Nomeprodotto"))
                .sendKeys("Pro Longwear Concealer");

        driver.findElement(
                By.cssSelector("button[type='submit']")
        ).click();

        driver.findElement(
                By.linkText("Pro Longwear Concealer")
        ).click();

        assertTrue(
                driver.getPageSource()
                        .contains("Pro Longwear Concealer")
        );
    }

    @Test
    @Order(10)
    void paginaRegistrazione_vieneVisualizzataCorrettamente() {

        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=HomeManagement.viewregistration");

        assertEquals(
                "Registrazione",
                driver.getTitle()
        );

        assertTrue(
                driver.findElement(By.name("Nome")).isDisplayed()
        );

        assertTrue(
                driver.findElement(By.name("Cognome")).isDisplayed()
        );

        assertTrue(
                driver.findElement(By.name("Email")).isDisplayed()
        );

        assertTrue(
                driver.findElement(By.name("Telefono")).isDisplayed()
        );

        assertTrue(
                driver.findElement(By.name("Password")).isDisplayed()
        );

        assertTrue(
                driver.findElement(
                        By.cssSelector("button[type='submit']")
                ).isDisplayed()
        );
    }
    @Test
    @Order(11)
    void registrazione_conEmailGiaEsistente_mostraErrore() {

        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=HomeManagement.viewregistration");

        driver.findElement(By.name("Nome"))
                .sendKeys("Mario");

        driver.findElement(By.name("Cognome"))
                .sendKeys("Rossi");

        driver.findElement(By.name("Email"))
                .sendKeys(TEST_EMAIL);

        driver.findElement(By.name("Telefono"))
                .sendKeys("3331234567");

        driver.findElement(By.name("Password"))
                .sendKeys("test_password");

        driver.findElement(By.cssSelector("button[type='submit']"))
                .click();

        WebElement messaggio = driver.findElement(By.cssSelector(".message"));

        assertEquals(
                "Utente già esistente!",
                messaggio.getText()
        );
    }

    @Test
    @Order(12)
    void registrazione_emailDuplicata_mantieneDatiInseriti() {

        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=HomeManagement.viewregistration");

        String nome = "Mario";
        String cognome = "Rossi";
        String telefono = "3331234567";

        driver.findElement(By.name("Nome"))
                .sendKeys(nome);

        driver.findElement(By.name("Cognome"))
                .sendKeys(cognome);

        driver.findElement(By.name("Email"))
                .sendKeys(TEST_EMAIL);

        driver.findElement(By.name("Telefono"))
                .sendKeys(telefono);

        driver.findElement(By.name("Password"))
                .sendKeys("password_test");

        driver.findElement(By.cssSelector("button[type='submit']"))
                .click();

        assertEquals(
                nome,
                driver.findElement(By.name("Nome")).getAttribute("value")
        );

        assertEquals(
                cognome,
                driver.findElement(By.name("Cognome")).getAttribute("value")
        );

        assertEquals(
                telefono,
                driver.findElement(By.name("Telefono")).getAttribute("value")
        );

        assertEquals(
                "Utente già esistente!",
                driver.findElement(By.cssSelector(".message")).getText()
        );
    }

    @Test
    @Order(13)
    void ricercaProdotto_nonEsistente() {

        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=HomeManagement.viewhome");

        WebElement search = driver.findElement(By.name("Nomeprodotto"));
        search.sendKeys("ProdottoCheNonEsiste123");

        driver.findElement(By.cssSelector("button[type='submit']")).click();

        assertTrue(
                driver.getPageSource().contains("ProdottoCheNonEsiste123")
                        || driver.getPageSource().contains("Nessun prodotto")
        );
    }

    @Test
    @Order(14)
    void ricercaProdotto_esistente() {

        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=HomeManagement.viewhome");

        String nomeProdotto = driver.findElement(
                By.cssSelector(".product h3 a")
        ).getText();

        assertFalse(nomeProdotto.isBlank());

        WebElement search = driver.findElement(
                By.name("Nomeprodotto")
        );

        search.sendKeys(nomeProdotto);

        driver.findElement(
                By.cssSelector(".search-bar button[type='submit']")
        ).click();

        assertTrue(
                driver.findElements(
                        By.xpath("//div[contains(@class,'product')]//h3/a[contains(text(),'"
                                + nomeProdotto + "')]")
                ).size() > 0
        );
    }


}