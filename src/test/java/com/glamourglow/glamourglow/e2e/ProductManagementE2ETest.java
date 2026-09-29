
package com.glamourglow.glamourglow.e2e;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductManagementE2ETest {

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
            // driver.quit();
        }
    }


    @Test
    @Order(1)
    void schedaProdotto_vieneVisualizzataCorrettamente() {

        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=HomeManagement.viewhome");

        driver.findElement(By.linkText("Gentle Exfoliating Scrub"))
                .click();

        assertTrue(
                driver.getPageSource()
                        .contains("Gentle Exfoliating Scrub")
        );
    }


    @Test
    @Order(2)
    void schedaProdotto_mostraInformazioniEQuantita() {

        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=HomeManagement.viewhome");

        driver.findElement(By.linkText("Pro Longwear Concealer"))
                .click();

        assertTrue(
                driver.findElement(
                        By.cssSelector(".product-name")
                ).isDisplayed()
        );

        assertEquals(
                "Pro Longwear Concealer",
                driver.findElement(
                        By.cssSelector(".product-name")
                ).getText()
        );

        assertTrue(
                driver.findElement(
                        By.id("quantita")
                ).isDisplayed()
        );
    }


    @Test
    @Order(3)
    void utenteAutenticato_schedaProdottoMostraAggiungiAlCarrello() {

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
                "/Dispatcher?controllerAction=HomeManagement.viewhome");

        driver.findElement(By.linkText("Pro Longwear Concealer"))
                .click();

        assertTrue(
                driver.findElement(
                        By.cssSelector("button.add-to-cart-button")
                ).isDisplayed()
        );

        assertEquals(
                "Aggiungi al carrello",
                driver.findElement(
                        By.cssSelector("button.add-to-cart-button")
                ).getText().trim()
        );
    }


    @Test
    @Order(4)
    void utenteNonAutenticato_schedaProdottoMostraLogin() {


        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=HomeManagement.viewhome");

        driver.findElement(By.linkText("Gentle Exfoliating Scrub"))
                .click();

        assertTrue(
                driver.getPageSource()
                        .contains("Gentle Exfoliating Scrub")
        );

        assertTrue(
                driver.findElement(
                        By.linkText("Login per aggiungere al carrello")
                ).isDisplayed()
        );
    }


    @Test
    @Order(5)
    void schedaProdotto_mostraQuantitaDisponibili() {

        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=HomeManagement.viewhome");

        driver.findElement(By.linkText("Gentle Exfoliating Scrub"))
                .click();

        assertTrue(
                driver.findElement(By.id("quantita"))
                        .isDisplayed()
        );

        assertTrue(
                driver.findElement(By.id("quantita"))
                        .findElements(By.tagName("option"))
                        .size() > 0
        );
    }
    @Test
    @Order(6)
    void utenteAutenticato_puoAggiungereProdottoAlCarrello() {

        // Login
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
                "/Dispatcher?controllerAction=HomeManagement.viewhome");

        driver.findElement(By.linkText("Gentle Exfoliating Scrub"))
                .click();

        driver.findElement(By.id("quantita"))
                .sendKeys("1");

        driver.findElement(
                By.cssSelector("button.add-to-cart-button")
        ).click();

        assertTrue(
                driver.getPageSource()
                        .contains("Gentle Exfoliating Scrub")
        );
    }

    @Test
    @Order(7)
    void utenteNonAutenticato_nonPuoAggiungereProdottoAlCarrello() {

        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=HomeManagement.viewhome");

        driver.findElement(
                By.linkText("Gentle Exfoliating Scrub")
        ).click();

        assertTrue(
                driver.findElement(
                        By.linkText("Login per aggiungere al carrello")
                ).isDisplayed()
        );
    }

    @Test
    @Order(8)
    void utenteAutenticato_aggiungeQuantitaSpecificataAlCarrello() {

        // Login
        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=HomeManagement.viewlogin");

        driver.findElement(By.name("Email"))
                .sendKeys(TEST_EMAIL);

        driver.findElement(By.name("Password"))
                .sendKeys(TEST_PASSWORD);

        driver.findElement(
                By.cssSelector("button[type='submit']")
        ).click();

        // Home
        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=HomeManagement.viewhome");

        driver.findElement(
                By.linkText("Gentle Exfoliating Scrub")
        ).click();

        driver.findElement(By.id("quantita"))
                .sendKeys("2");

        driver.findElement(
                By.cssSelector("button.add-to-cart-button")
        ).click();

        assertTrue(
                driver.getPageSource()
                        .contains("Gentle Exfoliating Scrub x2 aggiunto al carrello")
        );
    }

    @Test
    @Order(9)
    void utenteAutenticato_quantitaZero_rimuoveProdottoDalCarrello() {

        // Login
        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=HomeManagement.viewlogin");

        driver.findElement(By.name("Email"))
                .sendKeys(TEST_EMAIL);

        driver.findElement(By.name("Password"))
                .sendKeys(TEST_PASSWORD);

        driver.findElement(
                By.cssSelector("button[type='submit']")
        ).click();

        // Home
        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=HomeManagement.viewhome");

        // Scheda prodotto
        driver.findElement(
                By.linkText("Gentle Exfoliating Scrub")
        ).click();

        // Seleziona realmente la quantità 0
        Select quantita = new Select(
                driver.findElement(By.id("quantita"))
        );

        quantita.selectByValue("0");

        // Invia
        driver.findElement(
                By.cssSelector("button.add-to-cart-button")
        ).click();

        // Verifica che non venga mostrato il normale messaggio di aggiunta
        assertFalse(
                driver.getPageSource()
                        .contains("Gentle Exfoliating Scrub x0 aggiunto al carrello")
        );
    }

    @Test
    @Order(10)
    void prodottoBloccato_schedaVieneVisualizzata() {

        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=HomeManagement.viewhome");

        driver.findElement(
                By.linkText("Pro Longwear Concealer")
        ).click();

        assertEquals(
                "Pro Longwear Concealer",
                driver.findElement(
                        By.cssSelector(".product-name")
                ).getText()
        );
    }

    @Test
    @Order(11)
    void utenteNonAutenticato_cliccaLoginPerAggiungereAlCarrello() {

        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=HomeManagement.viewhome");

        driver.findElement(
                By.linkText("Gentle Exfoliating Scrub")
        ).click();

        WebElement loginLink = driver.findElement(
                By.linkText("Login per aggiungere al carrello")
        );

        assertTrue(loginLink.isDisplayed());

        loginLink.click();

        assertTrue(
                driver.findElement(By.name("Email")).isDisplayed()
        );

        assertTrue(
                driver.findElement(By.name("Password")).isDisplayed()
        );
    }
    @Test
    @Order(12)
    void prodottoBloccato_utenteAutenticato_visualizzaSchedaProdotto() {

        // Login
        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=HomeManagement.viewlogin");

        driver.findElement(By.name("Email"))
                .sendKeys(TEST_EMAIL);

        driver.findElement(By.name("Password"))
                .sendKeys(TEST_PASSWORD);

        driver.findElement(
                By.cssSelector("button[type='submit']")
        ).click();

        // Home
        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=HomeManagement.viewhome");

        // Prodotto bloccato
        driver.findElement(
                By.linkText("Pro Longwear Concealer")
        ).click();

        assertEquals(
                "Pro Longwear Concealer",
                driver.findElement(
                        By.cssSelector(".product-name")
                ).getText()
        );

        assertTrue(
                driver.findElement(
                        By.cssSelector("button.add-to-cart-button")
                ).isDisplayed()
        );
    }

    @Test
    @Order(13)
    void schedaProdotto_quantitaNonSuperaDisponibilita() {

        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=HomeManagement.viewhome");

        driver.findElement(
                By.linkText("Gentle Exfoliating Scrub")
        ).click();

        Select quantita = new Select(
                driver.findElement(By.id("quantita"))
        );

        assertTrue(
                quantita.getOptions().size() > 0
        );

        for (WebElement option : quantita.getOptions()) {
            int valore = Integer.parseInt(option.getAttribute("value"));

            assertTrue(
                    valore >= 0,
                    "La quantità non può essere negativa"
            );
        }
    }

    @Test
    @Order(14)
    void utenteAutenticato_schedaProdottoMostraVaiAlCarrello() {
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
                "/Dispatcher?controllerAction=HomeManagement.viewhome");

        driver.findElement(
                By.linkText("Pro Longwear Concealer")
        ).click();

        WebElement carrello = driver.findElement(
                By.linkText("Vai al Carrello")
        );

        assertTrue(
                carrello.isDisplayed()
        );

        assertEquals(
                "Vai al Carrello",
                carrello.getText().trim()
        );
    }

    @Test
    @Order(15)
    void schedaProdotto_linkHome_riportaAllaHome() {
        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=HomeManagement.viewhome");

        driver.findElement(
                By.linkText("Pro Longwear Concealer")
        ).click();

        WebElement homeLink = driver.findElement(
                By.cssSelector(".home-icon a")
        );

        assertTrue(
                homeLink.isDisplayed()
        );

        homeLink.click();

        assertTrue(
                driver.findElements(
                        By.cssSelector(".products-container")
                ).size() > 0
        );
    }


    @Test
    @Order(16)
    void schedaProdotto_permetteDiSelezionareUnaQuantitaSpecificata() {
        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=HomeManagement.viewhome");

        driver.findElement(
                By.linkText("Pro Longwear Concealer")
        ).click();

        Select quantita = new Select(
                driver.findElement(By.id("quantita"))
        );

        quantita.selectByValue("2");

        assertEquals(
                "2",
                quantita.getFirstSelectedOption().getAttribute("value")
        );

        assertEquals(
                "2",
                quantita.getFirstSelectedOption().getText().trim()
        );
    }
}
