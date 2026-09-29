package com.glamourglow.glamourglow.e2e;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserManagementE2ETest {

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
    void utenteAutenticato_visualizzaCarrello() {
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
                "/Dispatcher?controllerAction=UserManagement.viewcarrello");

        assertTrue(
                driver.findElement(
                        By.cssSelector(".cart-header")
                ).isDisplayed()
        );

        assertEquals(
                "Il Tuo Carrello",
                driver.findElement(
                        By.cssSelector(".cart-header")
                ).getText().trim()
        );
    }

    @Test
    @Order(2)
    void utenteNonAutenticato_accessoCarrello_ritornaAllaHome() {
        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=UserManagement.viewcarrello");

        assertTrue(
                driver.findElements(
                        By.cssSelector(".products-container")
                ).size() > 0
        );

        assertTrue(
                driver.getPageSource()
                        .contains("Fai il login per visualizzare il carrello")
        );
    }

    @Test
    @Order(3)
    void utenteAutenticato_prodottoAggiunto_compareNelCarrello() {
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
                By.linkText("Gentle Exfoliating Scrub")
        ).click();

        driver.findElement(By.id("quantita"))
                .sendKeys("1");

        driver.findElement(
                By.cssSelector("button.add-to-cart-button")
        ).click();

        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=UserManagement.viewcarrello");

        assertTrue(
                driver.getPageSource()
                        .contains("Gentle Exfoliating Scrub")
        );

        assertTrue(
                driver.findElements(
                        By.cssSelector(".cart-item")
                ).size() > 0
        );
    }

    @Test
    @Order(4)
    void carrello_mostraQuantitaCorrettaDelProdotto() {
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
                By.linkText("Gentle Exfoliating Scrub")
        ).click();

        Select quantita = new Select(
                driver.findElement(By.id("quantita"))
        );

        quantita.selectByValue("2");

        driver.findElement(
                By.cssSelector("button.add-to-cart-button")
        ).click();

        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=UserManagement.viewcarrello");

        assertTrue(
                driver.getPageSource()
                        .contains("Gentle Exfoliating Scrub")
        );

        WebElement quantitaCarrello = driver.findElement(
                By.cssSelector(".cart-item input[name='tmp']")
        );

        assertEquals(
                "2",
                quantitaCarrello.getAttribute("value")
        );
    }

    @Test
    @Order(5)
    void carrello_quantitaZero_rimuoveProdotto() {
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
                By.linkText("Gentle Exfoliating Scrub")
        ).click();

        Select quantita = new Select(
                driver.findElement(By.id("quantita"))
        );

        quantita.selectByValue("1");

        driver.findElement(
                By.cssSelector("button.add-to-cart-button")
        ).click();

        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=UserManagement.viewcarrello");

        By prodottoLocator = By.xpath(
                "//div[contains(@class,'cart-item')]" +
                        "[.//h3[contains(text(),'Gentle Exfoliating Scrub')]]"
        );

        WebElement prodotto = driver.findElement(prodottoLocator);

        assertTrue(prodotto.isDisplayed());

        WebElement elimina = prodotto.findElement(
                By.cssSelector("img[alt='Elimina']")
        );

        elimina.click();

        WebDriverWait wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(5)
        );

        wait.until(
                ExpectedConditions.numberOfElementsToBe(
                        prodottoLocator,
                        0
                )
        );

        assertTrue(
                driver.findElements(prodottoLocator).isEmpty()
        );
    }

    @Test
    @Order(6)
    void carrello_conProdotto_mostraLinkCheckout() {
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
                By.linkText("Gentle Exfoliating Scrub")
        ).click();

        Select quantita = new Select(
                driver.findElement(By.id("quantita"))
        );

        quantita.selectByValue("1");

        driver.findElement(
                By.cssSelector("button.add-to-cart-button")
        ).click();

        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=UserManagement.viewcarrello");

        WebElement checkout = driver.findElement(
                By.cssSelector(".checkout-button")
        );

        assertTrue(checkout.isDisplayed());

        assertEquals(
                "Procedi al Checkout",
                checkout.getText().trim()
        );
    }

    @Test
    @Order(7)
    void utenteAutenticato_puoAccedereAlCheckout() {
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
                By.linkText("Gentle Exfoliating Scrub")
        ).click();

        Select quantita = new Select(
                driver.findElement(By.id("quantita"))
        );

        quantita.selectByValue("1");

        driver.findElement(
                By.cssSelector("button.add-to-cart-button")
        ).click();

        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=UserManagement.viewcarrello");

        driver.findElement(
                By.cssSelector(".checkout-button")
        ).click();

        assertTrue(
                driver.findElement(
                        By.cssSelector(".prodotti-header")
                ).isDisplayed()
        );

        assertEquals(
                "Riepilogo Prodotti",
                driver.findElement(
                        By.cssSelector(".prodotti-header h1")
                ).getText().trim()
        );

        assertTrue(
                driver.findElement(
                        By.cssSelector(".totale")
                ).isDisplayed()
        );
    }

    @Test
    @Order(8)
    void checkout_mostraCampiIndirizzoSpedizione() {
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
                By.linkText("Gentle Exfoliating Scrub")
        ).click();

        Select quantita = new Select(
                driver.findElement(By.id("quantita"))
        );

        quantita.selectByValue("1");

        driver.findElement(
                By.cssSelector("button.add-to-cart-button")
        ).click();

        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=UserManagement.viewcheckout");

        WebElement indirizzo = driver.findElement(
                By.name("Indirizzo")
        );

        WebElement stato = driver.findElement(
                By.name("Stato")
        );

        WebElement citta = driver.findElement(
                By.name("Citta")
        );

        assertTrue(indirizzo.isDisplayed());
        assertTrue(stato.isDisplayed());
        assertTrue(citta.isDisplayed());

        assertNotNull(
                indirizzo.getAttribute("required")
        );

        assertNotNull(
                stato.getAttribute("required")
        );

        assertNotNull(
                citta.getAttribute("required")
        );

        assertTrue(
                driver.findElement(
                        By.cssSelector("button.checkout")
                ).isDisplayed()
        );
    }

    @Test
    @Order(9)
    void checkout_permetteDiCompilareIndirizzoSpedizione() {
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
                "/Dispatcher?controllerAction=UserManagement.viewcheckout");

        WebElement indirizzo = driver.findElement(
                By.name("Indirizzo")
        );

        WebElement stato = driver.findElement(
                By.name("Stato")
        );

        WebElement citta = driver.findElement(
                By.name("Citta")
        );

        indirizzo.sendKeys("Via Roma 10");
        stato.sendKeys("Italia");
        citta.sendKeys("Bologna");

        assertEquals(
                "Via Roma 10",
                indirizzo.getAttribute("value")
        );

        assertEquals(
                "Italia",
                stato.getAttribute("value")
        );

        assertEquals(
                "Bologna",
                citta.getAttribute("value")
        );


    }

    @Test
    @Order(10)
    void checkout_mostraProdottoNelRiepilogo() {
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

        String nomeProdotto = "Gentle Exfoliating Scrub";

        driver.findElement(
                By.linkText(nomeProdotto)
        ).click();

        Select quantita = new Select(
                driver.findElement(By.id("quantita"))
        );

        quantita.selectByValue("1");

        driver.findElement(
                By.cssSelector("button.add-to-cart-button")
        ).click();

        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=UserManagement.viewcheckout");

        WebElement prodotto = driver.findElement(
                By.cssSelector(".item .product-name")
        );

        assertEquals(
                nomeProdotto,
                prodotto.getText()
        );

        WebElement quantitaProdotto = driver.findElement(
                By.cssSelector(".item .quantity")
        );

        assertTrue(
                quantitaProdotto.getText().contains("1")
        );

    }

    @Test
    @Order(11)
    void checkout_mostraTotaleCorretto() {
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

        String nomeProdotto = "Gentle Exfoliating Scrub";

        driver.findElement(
                By.linkText(nomeProdotto)
        ).click();

        Select quantita = new Select(
                driver.findElement(By.id("quantita"))
        );

        quantita.selectByValue("1");

        driver.findElement(
                By.cssSelector("button.add-to-cart-button")
        ).click();

        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=UserManagement.viewcheckout");

        WebElement totale = driver.findElement(
                By.cssSelector(".total")
        );

        assertTrue(totale.isDisplayed());

        assertNotEquals(
                "0,00 €",
                totale.getText().trim()
        );
    }

    @Test
    @Order(12)
    void checkout_couponNonValido_mostraMessaggio() {
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
                By.linkText("Gentle Exfoliating Scrub")
        ).click();

        Select quantita = new Select(
                driver.findElement(By.id("quantita"))
        );

        quantita.selectByValue("1");

        driver.findElement(
                By.cssSelector("button.add-to-cart-button")
        ).click();

        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=UserManagement.viewcheckout");

        WebElement coupon = driver.findElement(
                By.name("coupon")
        );

        coupon.sendKeys("COUPON_TEST_INESISTENTE_999");

        WebElement formCoupon = coupon.findElement(
                By.xpath("./ancestor::form")
        );

        formCoupon.submit();

        WebElement messaggio = driver.findElement(
                By.cssSelector(".messaggio")
        );

        assertTrue(messaggio.isDisplayed());

        assertEquals(
                "Coupon non valido, poichè non esiste!",
                messaggio.getText().trim()
        );

    }

    @Test
    @Order(13)
    void utenteAutenticato_puoAccedereAllaRicaricaSaldo() {
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
                "/Dispatcher?controllerAction=UserManagement.ricaricasaldo");

        assertTrue(
                driver.getCurrentUrl().contains("UserManagement.ricaricasaldo")
                        || driver.getCurrentUrl().contains("ricarica")
        );

        assertTrue(
                driver.findElements(
                        By.cssSelector("input")
                ).size() > 0
        );
    }
    @Test
    @Order(14)
    void utenteAutenticato_puoRicaricareIlSaldo() {
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
                "/Dispatcher?controllerAction=UserManagement.ricaricasaldo");

        WebElement saldo = driver.findElement(
                By.name("saldo")
        );

        saldo.sendKeys("10");

        saldo.findElement(
                By.xpath("./ancestor::form")
        ).submit();

        assertTrue(
                driver.getPageSource().contains("Saldo ricaricato")
        );
    }

    @Test
    @Order(15)
    void utenteNonAutenticato_ricaricaSaldo_restituisce404() {
        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=UserManagement.ricaricasaldo");


        assertTrue(
                driver.getTitle().contains("404")
        );


    }



    @Test
    @Order(16)
    void ricaricaSaldo_importoNegativo_nonVieneAccettato() {
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
                "/Dispatcher?controllerAction=UserManagement.ricaricasaldo");

        WebElement saldo = driver.findElement(
                By.id("saldo")
        );

        // Imposta direttamente un valore negativo
        // per verificare la validazione HTML5 min="0".
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].value = '-10';",
                saldo
        );

        Boolean valido = (Boolean) ((JavascriptExecutor) driver)
                .executeScript(
                        "return arguments[0].checkValidity();",
                        saldo
                );

        assertFalse(valido);
    }


    @Test
    @Order(17)
    void utenteAutenticato_puoVisualizzareStoricoOrdini() {
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
                "/Dispatcher?controllerAction=UserManagement.viewordini");

        WebElement container = driver.findElement(
                By.cssSelector(".orders-container")
        );

        assertTrue(container.isDisplayed());

        WebElement titolo = driver.findElement(
                By.cssSelector(".order-header")
        );

        assertEquals(
                "Storico Ordini",
                titolo.getText().trim()
        );
    }


    @Test
    @Order(18)
    void storicoOrdini_mostraOrdiniOPpMessaggioVuoto() {
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
                "/Dispatcher?controllerAction=UserManagement.viewordini");

        List<WebElement> ordini = driver.findElements(
                By.cssSelector(".order-management-item")
        );

        if (!ordini.isEmpty()) {
            assertTrue(ordini.get(0).isDisplayed());

            assertFalse(
                    driver.findElement(By.cssSelector(".order-status"))
                            .getText()
                            .isBlank()
            );

            assertFalse(
                    driver.findElement(By.cssSelector(".date"))
                            .getText()
                            .isBlank()
            );

            assertFalse(
                    driver.findElement(By.cssSelector(".total-price"))
                            .getText()
                            .isBlank()
            );
        } else {
            WebElement messaggio = driver.findElement(
                    By.cssSelector(".empty-orders")
            );

            assertEquals(
                    "Nessun ordine effettuato.",
                    messaggio.getText().trim()
            );
        }
    }

    @Test
    @Order(19)
    void utenteNonAutenticato_accessoCheckout_tornaAllaHome() {
        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=UserManagement.viewcheckout");

        assertTrue(
                driver.findElements(By.cssSelector(".products-container")).size() > 0
                        || driver.getPageSource().contains("Fai il login")
        );
    }


    @Test
    @Order(20)
    void checkout_conProdotto_mostraRiepilogoCompleto() {
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
                By.linkText("Gentle Exfoliating Scrub")
        ).click();

        WebElement quantita = driver.findElement(
                By.id("quantita")
        );

        new Select(quantita).selectByValue("1");

        driver.findElement(
                By.cssSelector("button.add-to-cart-button")
        ).click();

        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=UserManagement.viewcheckout");

        WebElement prodotto = driver.findElement(
                By.cssSelector(".item .product-name")
        );

        WebElement qta = driver.findElement(
                By.cssSelector(".item .quantity")
        );

        WebElement totale = driver.findElement(
                By.cssSelector(".total")
        );

        assertEquals(
                "Gentle Exfoliating Scrub",
                prodotto.getText().trim()
        );

        assertTrue(qta.getText().contains("1"));
        assertFalse(totale.getText().isBlank());
    }




    @Test
    @Order(22)
    void checkout_quantitaSuperioreDisponibilita_mostraMessaggio() {

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

        // Apro il prodotto 4012, disponibilità = 100
        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=ProductManagement.viewprodotto&id=4012");

        WebElement quantita = driver.findElement(By.id("quantita"));

        // Aggiungo temporaneamente l'opzione 101 al select
        ((JavascriptExecutor) driver).executeScript(
                "var option = document.createElement('option');" +
                        "option.value = '101';" +
                        "option.text = '101';" +
                        "arguments[0].appendChild(option);" +
                        "arguments[0].value = '101';",
                quantita
        );

        assertEquals(
                "101",
                quantita.getAttribute("value")
        );

        // Invio il form
        WebElement form = quantita.findElement(
                By.xpath("./ancestor::form")
        );

        form.submit();

        // Controllo che il cookie del prodotto 4012 abbia quantità 101
        boolean cookieQuantita101 = driver.manage()
                .getCookies()
                .stream()
                .anyMatch(cookie ->
                        cookie.getName().endsWith("%4012")
                                && "101".equals(cookie.getValue())
                );

        assertTrue(
                cookieQuantita101,
                "Il cookie del prodotto 4012 con quantità 101 non è stato creato."
        );

        // Apro il checkout
        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=UserManagement.viewcheckout");

        assertTrue(
                driver.getPageSource()
                        .contains("Non sono disponibili 101 quantità!")
        );
    }




    @Test
    @Order(23)
    void verificacoupon_couponGiaUsato_mostraMessaggio() {

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

        // Apro il prodotto
        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=ProductManagement.viewprodotto&id=4012");

        // Aggiungo quantità 1
        WebElement quantita = driver.findElement(By.id("quantita"));
        new Select(quantita).selectByValue("1");

        driver.findElement(
                By.cssSelector("button.add-to-cart-button")
        ).click();

        // Apro il checkout
        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=UserManagement.viewcheckout");

        // Aspetto che il campo coupon sia presente
        WebDriverWait wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(5)
        );

        WebElement coupon = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.name("coupon")
                )
        );

        // Inserisco un coupon già utilizzato
        coupon.sendKeys("222");

        // Recupero il form del coupon
        WebElement formCoupon = coupon.findElement(
                By.xpath("./ancestor::form")
        );

        formCoupon.submit();

        // Verifico il messaggio
        WebElement messaggio = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.cssSelector(".messaggio")
                )
        );

        assertEquals(
                "Coupon non valido, poichè è già stato usato!",
                messaggio.getText().trim()
        );
    }






    @Test
    @Order(24)
    void verificacoupon_couponGiaInserito_mostraMessaggio() {

        WebDriverWait wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(5)
        );

        // =========================
        // LOGIN
        // =========================
        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=HomeManagement.viewlogin");

        driver.findElement(By.name("Email"))
                .sendKeys(TEST_EMAIL);

        driver.findElement(By.name("Password"))
                .sendKeys(TEST_PASSWORD);

        driver.findElement(
                By.cssSelector("button[type='submit']")
        ).click();

        // =========================
        // APRO IL PRODOTTO 4012
        // =========================
        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=ProductManagement.viewprodotto&id=4012");

        WebElement quantita = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.id("quantita")
                )
        );

        new Select(quantita).selectByValue("1");

        // Invio direttamente il form "Aggiungi al carrello"
        WebElement formProdotto = quantita.findElement(
                By.xpath("./ancestor::form")
        );

        formProdotto.submit();

        // =========================
        // APRO IL CHECKOUT
        // =========================
        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=UserManagement.viewcheckout");

        // Verifico che il checkout sia effettivamente aperto
        wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.cssSelector(".items-container")
                )
        );

        // =========================
        // CAMPO COUPON
        // =========================
        WebElement coupon = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.name("coupon")
                )
        );

        // =========================
        // CREO DUE PARAMETRI coupon
        // CON LO STESSO VALORE
        // =========================
        coupon.clear();
        coupon.sendKeys("999999998");

        WebElement formCoupon = coupon.findElement(
                By.xpath("./ancestor::form")
        );

        ((JavascriptExecutor) driver).executeScript(
                "var input = document.createElement('input');" +
                        "input.type = 'hidden';" +
                        "input.name = 'coupon';" +
                        "input.value = '999999998';" +
                        "arguments[0].appendChild(input);",
                formCoupon
        );

        // Controllo che esistano effettivamente
        // due input con name=coupon
        assertEquals(
                2,
                formCoupon.findElements(By.name("coupon")).size()
        );

        // =========================
        // INVIO IL FORM
        // =========================
        formCoupon.submit();

        // =========================
        // VERIFICO IL MESSAGGIO
        // =========================
        WebElement messaggio = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.cssSelector(".messaggio")
                )
        );

        assertEquals(
                "Coupon non valido, poichè già inserito!",
                messaggio.getText().trim()
        );
    }



    @Test
    @Order(25)
    void verificacoupon_couponVuoto_nonMostraErrore() {

        WebDriverWait wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(5)
        );

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

        // Apro il prodotto 4012
        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=ProductManagement.viewprodotto&id=4012");

        WebElement quantita = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.id("quantita")
                )
        );

        new Select(quantita).selectByValue("1");

        WebElement formProdotto = quantita.findElement(
                By.xpath("./ancestor::form")
        );

        formProdotto.submit();

        // Apro il checkout
        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=UserManagement.viewcheckout");

        // Aspetto il campo coupon
        WebElement coupon = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.name("coupon")
                )
        );

        // Invio un coupon vuoto
        coupon.clear();

        WebElement formCoupon = coupon.findElement(
                By.xpath("./ancestor::form")
        );

        formCoupon.submit();

        // Il checkout deve essere ancora presente
        wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.cssSelector(".items-container")
                )
        );

        // Non deve comparire un messaggio di errore
        assertTrue(
                driver.findElements(By.cssSelector(".messaggio")).isEmpty()
                        || driver.findElement(By.cssSelector(".messaggio"))
                        .getText()
                        .trim()
                        .isEmpty()
        );
    }


    @Test
    @Order(26)
    void verificacoupon_utenteNonLoggato_nonAccedeAlCheckout() {

        // Apro direttamente il checkout senza effettuare il login
        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=UserManagement.viewcheckout");

        // L'utente non autenticato viene riportato alla home
        assertTrue(
                driver.getPageSource().contains("Fai il login per visualizzare il carrello")
        );
    }



    @Test
    @Order(27)
    void aggiungicarrello_utenteNonLoggato_restituisce404() {

        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=UserManagement.aggiungicarrello" +
                "&quantita=1&idprod=4012");

        assertTrue(
                driver.getPageSource().contains("HTTP Status 404")
                        || driver.getPageSource().contains("JSP file [/jsp/null.jsp] not found"),
                "Era atteso un errore 404 per l'utente non autenticato."
        );
    }


    @Test
    @Order(28)
    void aggiungicarrello_parametriMancanti_mostraCarrello() {

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

        // Chiamata senza quantita e idprod
        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=UserManagement.aggiungicarrello");

        WebDriverWait wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(5)
        );

        WebElement cartHeader = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.cssSelector(".cart-header")
                )
        );

        assertEquals(
                "Il Tuo Carrello",
                cartHeader.getText().trim()
        );
    }





    @Test
    @Order(29)
    void acquisto_creditoInsufficiente_mostraMessaggio() {

        WebDriverWait wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(5)
        );

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

        // Apro il prodotto 4012
        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=ProductManagement.viewprodotto&id=4012");

        // Seleziono 80 pezzi
        WebElement quantita = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.id("quantita")
                )
        );

        new Select(quantita).selectByValue("80");

        assertEquals("80", quantita.getAttribute("value"));

        // Aggiungo il prodotto al carrello
        WebElement formProdotto = quantita.findElement(
                By.xpath("./ancestor::form")
        );

        formProdotto.submit();

        // Apro il checkout
        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=UserManagement.viewcheckout");

        // Verifico che il campo indirizzo sia presente
        wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.name("Indirizzo")
                )
        );

        // Compilo i dati di spedizione
        driver.findElement(By.name("Indirizzo"))
                .sendKeys("Via Test 1");

        driver.findElement(By.name("Stato"))
                .sendKeys("Italia");

        driver.findElement(By.name("Citta"))
                .sendKeys("Bologna");

        // Invio l'acquisto
        driver.findElement(
                By.cssSelector("button.checkout")
        ).click();

        // Verifico il messaggio di credito insufficiente
        WebElement messaggio = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.cssSelector(".messaggio")
                )
        );

        assertEquals(
                "Impossibile procedere con il checkout, credito non disponibile!",
                messaggio.getText().trim()
        );
    }



    @Test
    @Order(30)
    void acquisto_utenteNonLoggato_restituisce404() {

        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=UserManagement.acquisto" +
                "&Indirizzo=Via%20Test%201" +
                "&Stato=Italia" +
                "&Citta=Bologna");

        assertTrue(
                driver.getPageSource().contains("HTTP Status 404"),
                "Era atteso un errore 404 per l'utente non autenticato."
        );

        assertTrue(
                driver.getPageSource().contains(
                        "JSP file [/jsp/UserManagement/login.jsp] not found"
                ),
                "Era atteso il riferimento alla JSP di login inesistente."
        );
    }


    @Test
    @Order(31)
    void viewordini_utenteNonLoggato_reindirizzaAlLogin() {

        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=UserManagement.viewordini");

        WebDriverWait wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(5)
        );

        wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.name("Email")
                )
        );

        assertEquals(
                "Login",
                driver.getTitle()
        );

        assertTrue(
                driver.findElement(By.name("Email")).isDisplayed(),
                "Il campo Email della pagina di login dovrebbe essere visibile."
        );

        assertTrue(
                driver.findElement(By.name("Password")).isDisplayed(),
                "Il campo Password della pagina di login dovrebbe essere visibile."
        );
    }




    @Test
    @Order(32)
    void ricaricasaldo_utenteNonLoggato_restituisce404() {

        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=UserManagement.ricaricasaldo");

        assertTrue(
                driver.getPageSource().contains("HTTP Status 404"),
                "Era atteso un errore 404 per l'utente non autenticato."
        );

        assertTrue(
                driver.getPageSource().contains(
                        "JSP file [/jsp/UserManagement/login.jsp] not found"
                ),
                "Era atteso il riferimento alla JSP di login inesistente."
        );
    }



    @Test
    @Order(33)
    void ricarica_utenteNonLoggato_restituisce404() {

        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=UserManagement.ricarica" +
                "&saldo=10");

        assertTrue(
                driver.getPageSource().contains("HTTP Status 404"),
                "Era atteso un errore 404 per l'utente non autenticato."
        );

        assertTrue(
                driver.getPageSource().contains(
                        "JSP file [/jsp/UserManagement/login.jsp] not found"
                ),
                "Era atteso il riferimento alla JSP di login inesistente."
        );
    }



    @Test
    @Order(34)
    void ricarica_utenteAutenticato_importoValido_mostraMessaggio() {

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
                "/Dispatcher?controllerAction=UserManagement.ricarica" +
                "&saldo=10");

        assertTrue(
                driver.getPageSource().contains("Saldo ricaricato"),
                "Era atteso il messaggio 'Saldo ricaricato'."
        );
    }





    @Test
    @Order(35)
    void acquisto_utenteAutenticato_conCreditoSufficiente_completatoConSuccesso() {

        WebDriverWait wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(5)
        );

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

        // Aggiunge il prodotto 4012 al carrello
        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=ProductManagement.viewprodotto&id=4012");

        WebElement quantita = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.id("quantita")
                )
        );

        new Select(quantita).selectByValue("1");

        WebElement formProdotto = quantita.findElement(
                By.xpath("./ancestor::form")
        );

        formProdotto.submit();

        // Checkout
        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=UserManagement.viewcheckout");

        wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.name("Indirizzo")
                )
        );

        driver.findElement(By.name("Indirizzo"))
                .sendKeys("Via Test 1");

        driver.findElement(By.name("Stato"))
                .sendKeys("Italia");

        driver.findElement(By.name("Citta"))
                .sendKeys("Bologna");

        driver.findElement(
                By.cssSelector("button.checkout")
        ).click();

        // Verifica acquisto completato
        wait.until(
                ExpectedConditions.textToBePresentInElementLocated(
                        By.tagName("body"),
                        "Acquisto effettuato con successo"
                )
        );

        assertTrue(
                driver.getPageSource().contains(
                        "Acquisto effettuato con successo"
                ),
                "Era atteso il messaggio di acquisto completato."
        );
    }




    @Test
    @Order(36)
    void acquisto_completato_carrelloRisultaVuoto() {

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

        // Apre il prodotto 4005
        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=ProductManagement.viewprodotto&id=4005");

        WebDriverWait wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(5)
        );

        WebElement quantita = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.id("quantita")
                )
        );

        new Select(quantita).selectByValue("1");

        WebElement formProdotto = quantita.findElement(
                By.xpath("./ancestor::form")
        );

        formProdotto.submit();

        // Checkout
        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=UserManagement.viewcheckout");

        wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.name("Indirizzo")
                )
        );

        driver.findElement(By.name("Indirizzo"))
                .sendKeys("Via Test 1");

        driver.findElement(By.name("Stato"))
                .sendKeys("Italia");

        driver.findElement(By.name("Citta"))
                .sendKeys("Bologna");

        driver.findElement(
                By.cssSelector("button.checkout")
        ).click();

        // Verifica acquisto completato
        wait.until(
                ExpectedConditions.textToBePresentInElementLocated(
                        By.tagName("body"),
                        "Acquisto effettuato con successo"
                )
        );

        // Apre il carrello
        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=UserManagement.viewcarrello");

        // Il carrello deve essere vuoto
        assertTrue(
                driver.getPageSource().contains(
                        "Il tuo carrello è vuoto."
                ),
                "Il carrello non risulta vuoto dopo l'acquisto."
        );
    }



    @Test
    @Order(37)
    void carrello_modificaQuantita_mostraNuovaQuantita() {

        WebDriverWait wait = new WebDriverWait(
                driver,
                Duration.ofSeconds(5)
        );

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

        // Aggiunge il prodotto 4005 al carrello
        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=ProductManagement.viewprodotto&id=4005");

        WebElement quantita = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.id("quantita")
                )
        );

        new Select(quantita).selectByValue("1");

        WebElement formProdotto = quantita.findElement(
                By.xpath("./ancestor::form")
        );

        formProdotto.submit();

        // Apre il carrello
        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=UserManagement.viewcarrello");

        WebElement quantitaCarrello = wait.until(
                ExpectedConditions.presenceOfElementLocated(
                        By.cssSelector("input[name='tmp']")
                )
        );

        // Modifica la quantità da 1 a 2
        quantitaCarrello.clear();
        quantitaCarrello.sendKeys("2");

        // Invio della modifica
        quantitaCarrello.sendKeys(Keys.ENTER);

        // Verifica che la quantità sia diventata 2
        wait.until(
                ExpectedConditions.attributeToBe(
                        By.cssSelector("input[name='tmp']"),
                        "value",
                        "2"
                )
        );

        assertEquals(
                "2",
                driver.findElement(
                        By.cssSelector("input[name='tmp']")
                ).getAttribute("value")
        );
    }






}