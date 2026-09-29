
package com.glamourglow.glamourglow.e2e;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import org.openqa.selenium.support.ui.Select;
import static org.junit.jupiter.api.Assertions.*;


public class AdminManagementE2ETest {

    private static final String BASE_URL =
            "http://localhost:8080/GlamourGlow_war_exploded";

    private WebDriver driver;

    @BeforeEach
    void setUp() {
        driver = new ChromeDriver();
    }

    @Test
    void utenteAutenticato_visualizzaGestioneCoupon() {

        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=HomeManagement.viewlogin");

        driver.findElement(By.name("Email"))
                .sendKeys("integration.home@glamourglow.test");

        driver.findElement(By.name("Password"))
                .sendKeys("test_password");

        driver.findElement(
                By.cssSelector("button[type='submit']")
        ).click();

        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=AdminManagement.gestionecoupon");

        assertTrue(
                driver.getPageSource().contains("Gestione Coupon")
        );

        assertTrue(
                driver.findElements(
                        By.cssSelector(".coupon-management-item")
                ).size() > 0
        );

        assertTrue(
                driver.findElement(
                        By.cssSelector("form.add-coupon")
                ).isDisplayed()
        );
    }


    @Test
    void utenteNonAutenticato_vieneReindirizzatoAlLoginPerGestioneCoupon() {
        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=AdminManagement.gestionecoupon");

        assertTrue(
                driver.getPageSource().contains("Accedi")
                        || driver.getPageSource().contains("Login")
                        || driver.findElements(
                        By.name("Email")
                ).size() > 0
        );
    }


    @Test
    void utenteNonAdmin_nonPuoModificareCoupon() {
        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=HomeManagement.viewlogin");

        driver.findElement(By.name("Email"))
                .sendKeys("integration.home@glamourglow.test");

        driver.findElement(By.name("Password"))
                .sendKeys("test_password");

        driver.findElement(
                By.cssSelector("button[type='submit']")
        ).click();

        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=AdminManagement.modificacoupon"
                + "&sconto=50"
                + "&codice=TESTNONADMIN"
                + "&vecchiocodice=TESTNONADMIN");

        assertTrue(
                driver.getPageSource().contains("GlamourGlow")
                        || driver.getPageSource().contains("Home")
        );
    }



    @Test
    void utenteNonAdmin_nonPuoAggiungereCoupon() {
        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=HomeManagement.viewlogin");

        driver.findElement(By.name("Email"))
                .sendKeys("integration.home@glamourglow.test");

        driver.findElement(By.name("Password"))
                .sendKeys("test_password");

        driver.findElement(
                By.cssSelector("button[type='submit']")
        ).click();

        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=AdminManagement.addCoupon"
                + "&sconto=25"
                + "&codice=TESTNONADMIN");

        assertTrue(
                driver.getPageSource().contains("GlamourGlow")
                        || driver.getPageSource().contains("Home")
        );
    }



    @Test
    void admin_puoModificareCoupon() {

        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=HomeManagement.viewlogin");

        driver.findElement(By.name("Email"))
                .sendKeys("sara.verdi@email.com");

        driver.findElement(By.name("Password"))
                .sendKeys("hashed_password3");

        driver.findElement(
                By.cssSelector("button[type='submit']")
        ).click();

        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=AdminManagement.gestionecoupon");

        WebElement coupon = driver.findElements(
                        By.cssSelector(".coupon-management-item")
                )
                .stream()
                .filter(item -> item.findElement(
                        By.cssSelector("input[name='codice']")
                ).getAttribute("value").equals("222"))
                .findFirst()
                .orElseThrow();

        WebElement codice = coupon.findElement(
                By.cssSelector("input[name='codice']")
        );

        codice.clear();
        codice.sendKeys("222");

        WebElement sconto = coupon.findElement(
                By.cssSelector("input[name='sconto']")
        );

        ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript(
                        "arguments[0].value = '11';",
                        sconto
                );

        coupon.findElement(
                By.cssSelector("button[type='submit']")
        ).click();

        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=AdminManagement.gestionecoupon");

        WebElement couponModificato = driver.findElements(
                        By.cssSelector(".coupon-management-item")
                )
                .stream()
                .filter(item -> item.findElement(
                        By.cssSelector("input[name='codice']")
                ).getAttribute("value").equals("222"))
                .findFirst()
                .orElseThrow();

        assertEquals(
                "11",
                couponModificato.findElement(
                        By.cssSelector("input[name='sconto']")
                ).getAttribute("value")
        );

        // Ripristino del coupon da 11 a 10.
        WebElement scontoRipristino = couponModificato.findElement(
                By.cssSelector("input[name='sconto']")
        );

        ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript(
                        "arguments[0].value = '10';",
                        scontoRipristino
                );

        couponModificato.findElement(
                By.cssSelector("button[type='submit']")
        ).click();

        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=AdminManagement.gestionecoupon");

        WebElement couponRipristinato = driver.findElements(
                        By.cssSelector(".coupon-management-item")
                )
                .stream()
                .filter(item -> item.findElement(
                        By.cssSelector("input[name='codice']")
                ).getAttribute("value").equals("222"))
                .findFirst()
                .orElseThrow();

        assertEquals(
                "10",
                couponRipristinato.findElement(
                        By.cssSelector("input[name='sconto']")
                ).getAttribute("value")
        );
    }




    @Test
    void admin_nonPuoUsareCodiceCouponGiaEsistente() {
        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=HomeManagement.viewlogin");

        driver.findElement(By.name("Email"))
                .sendKeys("sara.verdi@email.com");

        driver.findElement(By.name("Password"))
                .sendKeys("hashed_password3");

        driver.findElement(
                By.cssSelector("button[type='submit']")
        ).click();

        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=AdminManagement.modificacoupon"
                + "&sconto=20"
                + "&codice=444466"
                + "&vecchiocodice=222");

        assertTrue(
                driver.getPageSource().contains(
                        "Codice già in uso ad altro coupon!"
                )
        );
    }



    @Test
    void admin_puoAggiungereNuovoCoupon() {
        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=HomeManagement.viewlogin");

        driver.findElement(By.name("Email"))
                .sendKeys("sara.verdi@email.com");

        driver.findElement(By.name("Password"))
                .sendKeys("hashed_password3");

        driver.findElement(
                By.cssSelector("button[type='submit']")
        ).click();

        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=AdminManagement.addCoupon"
                + "&sconto=25"
                + "&codice=E2ECPN001");

        assertTrue(
                driver.getPageSource().contains("Gestione Coupon")
        );

        assertTrue(
                driver.getPageSource().contains("E2ECPN001")
        );

        assertTrue(
                driver.getPageSource().contains("25")
        );
    }


    @Test
    void admin_nonPuoAggiungereCouponConCodiceEsistente() {
        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=HomeManagement.viewlogin");

        driver.findElement(By.name("Email"))
                .sendKeys("sara.verdi@email.com");

        driver.findElement(By.name("Password"))
                .sendKeys("hashed_password3");

        driver.findElement(
                By.cssSelector("button[type='submit']")
        ).click();

        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=AdminManagement.addCoupon"
                + "&sconto=30"
                + "&codice=222");

        assertTrue(
                driver.getPageSource().contains(
                        "Codice già in uso ad altro coupon!"
                )
        );
    }


    @Test
    void admin_visualizzaGestioneProdotti() {
        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=HomeManagement.viewlogin");

        driver.findElement(By.name("Email"))
                .sendKeys("sara.verdi@email.com");

        driver.findElement(By.name("Password"))
                .sendKeys("hashed_password3");

        driver.findElement(
                By.cssSelector("button[type='submit']")
        ).click();

        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=AdminManagement.gestioneprodotti");

        assertTrue(
                driver.getPageSource().contains("Gestione Prodotti")
        );
    }


    @Test
    void admin_puoModificareProdotto() {
        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=HomeManagement.viewlogin");

        driver.findElement(By.name("Email"))
                .sendKeys("sara.verdi@email.com");

        driver.findElement(By.name("Password"))
                .sendKeys("hashed_password3");

        driver.findElement(
                By.cssSelector("button[type='submit']")
        ).click();

        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=AdminManagement.modificaprodotto"
                + "&idprodotto=4002"
                + "&nomeprodotto=Powder%20Blush"
                + "&prezzo=26"
                + "&quantita=80"
                + "&promozione=true"
                + "&prezzo_scontato=20"
                + "&stato=false");

        assertTrue(
                driver.getPageSource().contains(
                        "Prodotto 4002 modificato con successo!"
                )
        );

        assertTrue(
                driver.getPageSource().contains("Gestione Prodotti")
        );
    }


    @Test
    void admin_ripristinaProdottoModificato() {
        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=HomeManagement.viewlogin");

        driver.findElement(By.name("Email"))
                .sendKeys("sara.verdi@email.com");

        driver.findElement(By.name("Password"))
                .sendKeys("hashed_password3");

        driver.findElement(
                By.cssSelector("button[type='submit']")
        ).click();

        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=AdminManagement.modificaprodotto"
                + "&idprodotto=4002"
                + "&nomeprodotto=Powder%20Blush"
                + "&prezzo=25"
                + "&quantita=80"
                + "&promozione=true"
                + "&prezzo_scontato=20"
                + "&stato=false");

        assertTrue(
                driver.getPageSource().contains(
                        "Prodotto 4002 modificato con successo!"
                )
        );

        assertTrue(
                driver.getPageSource().contains("Gestione Prodotti")
        );
    }


    @Test
    void admin_puoAggiungereNuovoProdotto() {
        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=HomeManagement.viewlogin");

        driver.findElement(By.name("Email"))
                .sendKeys("sara.verdi@email.com");

        driver.findElement(By.name("Password"))
                .sendKeys("hashed_password3");

        driver.findElement(
                By.cssSelector("button[type='submit']")
        ).click();

        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=AdminManagement.aggiungiprodotti"
                + "&nomeprodotto=E2E%20Test%20Shampoo"
                + "&descrizione=Prodotto%20creato%20durante%20il%20test%20E2E"
                + "&prezzo=20"
                + "&quantita=50"
                + "&immagine=e2e-test.jpg"
                + "&categoria=2002"
                + "&promozione=false"
                + "&prezzo_scontato=0"
                + "&stato=false"
                + "&marchio=3005");

        assertTrue(
                driver.getPageSource().contains(
                        "Prodotto aggiunto con successo!"
                )
        );

        assertTrue(
                driver.getPageSource().contains("Gestione Prodotti")
        );
    }


    @Test
    void admin_puoBloccareProdotto() {
        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=HomeManagement.viewlogin");

        driver.findElement(By.name("Email"))
                .sendKeys("sara.verdi@email.com");

        driver.findElement(By.name("Password"))
                .sendKeys("hashed_password3");

        driver.findElement(
                By.cssSelector("button[type='submit']")
        ).click();

        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=AdminManagement.modificaprodotto"
                + "&idprodotto=4002"
                + "&nomeprodotto=Powder%20Blush"
                + "&prezzo=25"
                + "&quantita=80"
                + "&promozione=true"
                + "&prezzo_scontato=20"
                + "&stato=true");

        assertTrue(
                driver.getPageSource().contains(
                        "Prodotto 4002 modificato con successo!"
                )
        );

        assertTrue(
                driver.getPageSource().contains("Gestione Prodotti")
        );
    }


    @Test
    void admin_puoRiattivareProdotto() {
        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=HomeManagement.viewlogin");

        driver.findElement(By.name("Email"))
                .sendKeys("sara.verdi@email.com");

        driver.findElement(By.name("Password"))
                .sendKeys("hashed_password3");

        driver.findElement(
                By.cssSelector("button[type='submit']")
        ).click();

        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=AdminManagement.modificaprodotto"
                + "&idprodotto=4002"
                + "&nomeprodotto=Powder%20Blush"
                + "&prezzo=25"
                + "&quantita=80"
                + "&promozione=true"
                + "&prezzo_scontato=20"
                + "&stato=false");

        assertTrue(
                driver.getPageSource().contains(
                        "Prodotto 4002 modificato con successo!"
                )
        );
    }

    @Test
    void admin_visualizzaGestioneOrdini() {
        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=HomeManagement.viewlogin");

        driver.findElement(By.name("Email"))
                .sendKeys("sara.verdi@email.com");

        driver.findElement(By.name("Password"))
                .sendKeys("hashed_password3");

        driver.findElement(
                By.cssSelector("button[type='submit']")
        ).click();

        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=AdminManagement.gestioneordini");

        assertTrue(
                driver.getPageSource().contains("Storico Ordini")
        );

        assertTrue(
                driver.findElements(
                        By.cssSelector(".order-management-item")
                ).size() > 0
        );
    }



    @Test
    void admin_puoModificareStatoOrdine() {
        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=HomeManagement.viewlogin");

        driver.findElement(By.name("Email"))
                .sendKeys("sara.verdi@email.com");

        driver.findElement(By.name("Password"))
                .sendKeys("hashed_password3");

        driver.findElement(
                By.cssSelector("button[type='submit']")
        ).click();

        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=AdminManagement.gestioneordini");

        WebElement ordine = driver.findElements(
                        By.cssSelector(".order-management-item")
                ).stream()
                .filter(item -> item.getText().contains("Ordine #99"))
                .findFirst()
                .orElseThrow();

        WebElement stato = ordine.findElement(
                By.name("stato")
        );

        stato.clear();
        stato.sendKeys("Spedito");

        ordine.findElement(
                By.cssSelector("button[type='submit']")
        ).click();

        assertTrue(
                driver.getPageSource().contains("Ordine #99")
        );

        WebElement ordineAggiornato = driver.findElements(
                        By.cssSelector(".order-management-item")
                ).stream()
                .filter(item -> item.getText().contains("Ordine #99"))
                .findFirst()
                .orElseThrow();

        assertEquals(
                "Spedito",
                ordineAggiornato.findElement(By.name("stato")).getAttribute("value")
        );
    }


    @Test
    void admin_ripristinaStatoOrdine() {
        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=HomeManagement.viewlogin");

        driver.findElement(By.name("Email"))
                .sendKeys("sara.verdi@email.com");

        driver.findElement(By.name("Password"))
                .sendKeys("hashed_password3");

        driver.findElement(
                By.cssSelector("button[type='submit']")
        ).click();

        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=AdminManagement.gestioneordini");

        WebElement ordine = driver.findElements(
                        By.cssSelector(".order-management-item")
                ).stream()
                .filter(item -> item.getText().contains("Ordine #99"))
                .findFirst()
                .orElseThrow();

        WebElement stato = ordine.findElement(
                By.name("stato")
        );

        stato.clear();
        stato.sendKeys("In elaborazione");

        ordine.findElement(
                By.cssSelector("button[type='submit']")
        ).click();

        WebElement ordineRipristinato = driver.findElements(
                        By.cssSelector(".order-management-item")
                ).stream()
                .filter(item -> item.getText().contains("Ordine #99"))
                .findFirst()
                .orElseThrow();

        assertEquals(
                "In elaborazione",
                ordineRipristinato.findElement(By.name("stato")).getAttribute("value")
        );
    }

    @Test
    void utenteNonAutenticato_vieneReindirizzatoAlLoginPerGestioneOrdini() {
        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=AdminManagement.gestioneordini");

        assertTrue(
                driver.getPageSource().contains("Accedi")
                        || driver.getPageSource().contains("Email")
        );
    }


    @Test
    void admin_visualizzaGestioneUtenti() {
        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=HomeManagement.viewlogin");

        driver.findElement(By.name("Email"))
                .sendKeys("sara.verdi@email.com");

        driver.findElement(By.name("Password"))
                .sendKeys("hashed_password3");

        driver.findElement(
                By.cssSelector("button[type='submit']")
        ).click();

        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=AdminManagement.gestioneutente");

        assertTrue(
                driver.getPageSource().contains("Gestione Utenti")
        );

        assertTrue(
                driver.findElements(
                        By.cssSelector(".user-management-item")
                ).size() > 0
        );
    }




    @Test
    void admin_puoBloccareUtente() {
        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=HomeManagement.viewlogin");

        driver.findElement(By.name("Email"))
                .sendKeys("sara.verdi@email.com");

        driver.findElement(By.name("Password"))
                .sendKeys("hashed_password3");

        driver.findElement(
                By.cssSelector("button[type='submit']")
        ).click();

        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=AdminManagement.gestioneutente");

        WebElement utente = driver.findElements(
                        By.cssSelector(".user-management-item")
                ).stream()
                .filter(item -> item.findElement(
                                By.name("email")
                        ).getAttribute("value")
                        .equals("integration.home@glamourglow.test"))
                .findFirst()
                .orElseThrow();

        Select stato = new Select(
                utente.findElement(By.name("stato"))
        );

        stato.selectByValue("bloccato");

        assertEquals(
                "bloccato",
                stato.getFirstSelectedOption().getAttribute("value")
        );

        utente.findElement(
                By.cssSelector("button[type='submit']")
        ).click();

        // Ricarica la pagina di gestione utenti
        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=AdminManagement.gestioneutente");

        WebElement utenteAggiornato = driver.findElements(
                        By.cssSelector(".user-management-item")
                ).stream()
                .filter(item -> item.findElement(
                                By.name("email")
                        ).getAttribute("value")
                        .equals("integration.home@glamourglow.test"))
                .findFirst()
                .orElseThrow();

        String statoDopoModifica =
                new Select(
                        utenteAggiornato.findElement(By.name("stato"))
                ).getFirstSelectedOption().getAttribute("value");

        assertEquals(
                "bloccato",
                statoDopoModifica
        );
    }


    @Test
    void admin_puoRiattivareUtente() {
        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=HomeManagement.viewlogin");

        driver.findElement(By.name("Email"))
                .sendKeys("sara.verdi@email.com");

        driver.findElement(By.name("Password"))
                .sendKeys("hashed_password3");

        driver.findElement(
                By.cssSelector("button[type='submit']")
        ).click();

        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=AdminManagement.gestioneutente");

        WebElement utente = driver.findElements(
                        By.cssSelector(".user-management-item")
                ).stream()
                .filter(item -> item.findElement(
                                By.name("email")
                        ).getAttribute("value")
                        .equals("integration.home@glamourglow.test"))
                .findFirst()
                .orElseThrow();

        Select stato = new Select(
                utente.findElement(By.name("stato"))
        );

        stato.selectByValue("attivo");

        assertEquals(
                "attivo",
                stato.getFirstSelectedOption().getAttribute("value")
        );

        utente.findElement(
                By.cssSelector("button[type='submit']")
        ).click();

        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=AdminManagement.gestioneutente");

        WebElement utenteAggiornato = driver.findElements(
                        By.cssSelector(".user-management-item")
                ).stream()
                .filter(item -> item.findElement(
                                By.name("email")
                        ).getAttribute("value")
                        .equals("integration.home@glamourglow.test"))
                .findFirst()
                .orElseThrow();

        assertEquals(
                "attivo",
                new Select(
                        utenteAggiornato.findElement(By.name("stato")
                        )).getFirstSelectedOption().getAttribute("value")
        );
    }


    @Test
    void admin_puoModificareRuoloUtente() {
        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=HomeManagement.viewlogin");

        driver.findElement(By.name("Email"))
                .sendKeys("sara.verdi@email.com");

        driver.findElement(By.name("Password"))
                .sendKeys("hashed_password3");

        driver.findElement(
                By.cssSelector("button[type='submit']")
        ).click();

        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=AdminManagement.gestioneutente");

        WebElement utente = driver.findElements(
                        By.cssSelector(".user-management-item")
                ).stream()
                .filter(item -> item.findElement(
                                By.name("email")
                        ).getAttribute("value")
                        .equals("integration.home@glamourglow.test"))
                .findFirst()
                .orElseThrow();

        Select ruolo = new Select(
                utente.findElement(By.name("ruolo"))
        );

        ruolo.selectByValue("true");

        assertEquals(
                "true",
                ruolo.getFirstSelectedOption().getAttribute("value")
        );

        utente.findElement(
                By.cssSelector("button[type='submit']")
        ).click();

        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=AdminManagement.gestioneutente");

        WebElement utenteAggiornato = driver.findElements(
                        By.cssSelector(".user-management-item")
                ).stream()
                .filter(item -> item.findElement(
                                By.name("email")
                        ).getAttribute("value")
                        .equals("integration.home@glamourglow.test"))
                .findFirst()
                .orElseThrow();

        assertEquals(
                "true",
                new Select(
                        utenteAggiornato.findElement(By.name("ruolo")
                        )).getFirstSelectedOption().getAttribute("value")
        );
    }


    @Test
    void admin_puoRipristinareRuoloUtente() {
        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=HomeManagement.viewlogin");

        driver.findElement(By.name("Email"))
                .sendKeys("sara.verdi@email.com");

        driver.findElement(By.name("Password"))
                .sendKeys("hashed_password3");

        driver.findElement(
                By.cssSelector("button[type='submit']")
        ).click();

        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=AdminManagement.gestioneutente");

        WebElement utente = driver.findElements(
                        By.cssSelector(".user-management-item")
                ).stream()
                .filter(item -> item.findElement(
                                By.name("email")
                        ).getAttribute("value")
                        .equals("integration.home@glamourglow.test"))
                .findFirst()
                .orElseThrow();

        Select ruolo = new Select(
                utente.findElement(By.name("ruolo")
                ));

        ruolo.selectByValue("false");

        assertEquals(
                "false",
                ruolo.getFirstSelectedOption().getAttribute("value")
        );

        utente.findElement(
                By.cssSelector("button[type='submit']")
        ).click();

        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=AdminManagement.gestioneutente");

        WebElement utenteAggiornato = driver.findElements(
                        By.cssSelector(".user-management-item")
                ).stream()
                .filter(item -> item.findElement(
                                By.name("email")
                        ).getAttribute("value")
                        .equals("integration.home@glamourglow.test"))
                .findFirst()
                .orElseThrow();

        assertEquals(
                "false",
                new Select(
                        utenteAggiornato.findElement(By.name("ruolo")
                        )).getFirstSelectedOption().getAttribute("value")
        );
    }




    @Test
    void admin_nonPuoModificareUtenteConEmailGiaEsistente() {
        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=HomeManagement.viewlogin");

        driver.findElement(By.name("Email"))
                .sendKeys("sara.verdi@email.com");

        driver.findElement(By.name("Password"))
                .sendKeys("hashed_password3");

        driver.findElement(
                By.cssSelector("button[type='submit']")
        ).click();

        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=AdminManagement.gestioneutente");

        WebElement utente = driver.findElements(
                        By.cssSelector(".user-management-item")
                ).stream()
                .filter(item -> item.findElement(
                                By.name("email")
                        ).getAttribute("value")
                        .equals("integration.home@glamourglow.test"))
                .findFirst()
                .orElseThrow();

        utente.findElement(By.name("email"))
                .clear();

        utente.findElement(By.name("email"))
                .sendKeys("sara.verdi@email.com");

        utente.findElement(
                By.cssSelector("button[type='submit']")
        ).click();

        WebElement messaggio = driver.findElement(
                By.cssSelector(".message")
        );

        assertEquals(
                "Impossibile modificare email",
                messaggio.getText()
        );

        WebElement utenteAggiornato = driver.findElements(
                        By.cssSelector(".user-management-item")
                ).stream()
                .filter(item -> item.findElement(
                                By.name("email")
                        ).getAttribute("value")
                        .equals("integration.home@glamourglow.test"))
                .findFirst()
                .orElseThrow();

        assertEquals(
                "integration.home@glamourglow.test",
                utenteAggiornato.findElement(
                        By.name("email")
                ).getAttribute("value")
        );
    }


    @Test
    void utenteNonAdmin_nonPuoModificareUtente() {
        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=HomeManagement.viewlogin");

        driver.findElement(By.name("Email"))
                .sendKeys("integration.home@glamourglow.test");

        driver.findElement(By.name("Password"))
                .sendKeys("test");

        driver.findElement(
                By.cssSelector("button[type='submit']")
        ).click();

        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=AdminManagement.gestioneutente");

        assertFalse(
                driver.getPageSource().contains("Gestione Utenti"),
                "Un utente non amministratore non dovrebbe visualizzare la gestione utenti."
        );

        assertFalse(
                driver.findElements(
                        By.cssSelector(".user-management-item")
                ).size() > 0,
                "Un utente non amministratore non dovrebbe visualizzare l'elenco degli utenti."
        );
    }



    @Test
    void admin_puoModificareEmailUtente() {
        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=HomeManagement.viewlogin");

        driver.findElement(By.name("Email"))
                .sendKeys("sara.verdi@email.com");

        driver.findElement(By.name("Password"))
                .sendKeys("hashed_password3");

        driver.findElement(
                By.cssSelector("button[type='submit']")
        ).click();

        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=AdminManagement.gestioneutente");

        WebElement utente = driver.findElements(
                        By.cssSelector(".user-management-item")
                ).stream()
                .filter(item -> item.findElement(
                                By.name("email")
                        ).getAttribute("value")
                        .equals("integration.home@glamourglow.test"))
                .findFirst()
                .orElseThrow();

        WebElement email = utente.findElement(
                By.name("email")
        );

        email.clear();
        email.sendKeys("integration.email.test@glamourglow.test");

        utente.findElement(
                By.cssSelector("button[type='submit']")
        ).click();

        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=AdminManagement.gestioneutente");

        WebElement utenteAggiornato = driver.findElements(
                        By.cssSelector(".user-management-item")
                ).stream()
                .filter(item -> item.findElement(
                                By.name("email")
                        ).getAttribute("value")
                        .equals("integration.email.test@glamourglow.test"))
                .findFirst()
                .orElseThrow();

        assertEquals(
                "integration.email.test@glamourglow.test",
                utenteAggiornato.findElement(
                        By.name("email")
                ).getAttribute("value")
        );

        WebElement emailRipristino = utenteAggiornato.findElement(
                By.name("email")
        );

        emailRipristino.clear();
        emailRipristino.sendKeys("integration.home@glamourglow.test");

        utenteAggiornato.findElement(
                By.cssSelector("button[type='submit']")
        ).click();
    }


    @Test
    void utenteNonAutenticato_vieneReindirizzatoAllaHomePerGestioneProdotti() {
        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=AdminManagement.gestioneprodotti");

        assertFalse(
                driver.getPageSource().contains("Gestione Prodotti"),
                "Un utente non autenticato non dovrebbe visualizzare la gestione prodotti."
        );

        assertFalse(
                driver.findElements(
                        By.cssSelector(".product-management-item")
                ).size() > 0,
                "Un utente non autenticato non dovrebbe visualizzare i prodotti amministrabili."
        );
    }



    @Test
    void utenteNonAdmin_nonPuoAccedereAllaGestioneProdotti() {
        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=HomeManagement.viewlogin");

        driver.findElement(By.name("Email"))
                .sendKeys("integration.home@glamourglow.test");

        driver.findElement(By.name("Password"))
                .sendKeys("test");

        driver.findElement(
                By.cssSelector("button[type='submit']")
        ).click();

        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=AdminManagement.gestioneprodotti");

        assertFalse(
                driver.getPageSource().contains("Gestione Prodotti"),
                "Un utente non amministratore non dovrebbe visualizzare la gestione prodotti."
        );

        assertFalse(
                driver.findElements(
                        By.cssSelector(".product-management-item")
                ).size() > 0,
                "Un utente non amministratore non dovrebbe visualizzare i prodotti amministrabili."
        );
    }



    @Test
    void utenteNonAdmin_nonPuoModificareProdotto() {
        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=HomeManagement.viewlogin");

        driver.findElement(By.name("Email"))
                .sendKeys("integration.home@glamourglow.test");

        driver.findElement(By.name("Password"))
                .sendKeys("test");

        driver.findElement(
                By.cssSelector("button[type='submit']")
        ).click();

        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=AdminManagement.modificaprodotto"
                + "&idprodotto=4002"
                + "&nomeprodotto=Powder%20Blush"
                + "&prezzo=99"
                + "&quantita=80"
                + "&promozione=true"
                + "&prezzo_scontato=20"
                + "&stato=false");

        assertFalse(
                driver.getPageSource().contains(
                        "Prodotto 4002 modificato con successo!"
                )
        );

        assertFalse(
                driver.getPageSource().contains("Gestione Prodotti")
        );
    }



    @Test
    void utenteNonAdmin_nonPuoAggiungereProdotto() {

        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=HomeManagement.viewlogin");

        driver.findElement(By.name("Email"))
                .sendKeys("integration.home@glamourglow.test");

        driver.findElement(By.name("Password"))
                .sendKeys("test");

        driver.findElement(
                By.cssSelector("button[type='submit']")
        ).click();

        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=AdminManagement.aggiungiprodotti"
                + "&nomeprodotto=Prodotto%20Non%20Autorizzato"
                + "&descrizione=Test"
                + "&prezzo=10"
                + "&quantita=10"
                + "&immagine=test.jpg"
                + "&categoria=2002"
                + "&promozione=false"
                + "&prezzo_scontato=0"
                + "&stato=false"
                + "&marchio=3005");

        assertFalse(
                driver.getPageSource().contains(
                        "Prodotto aggiunto con successo!"
                )
        );

        assertFalse(
                driver.getPageSource().contains("Gestione Prodotti")
        );

        assertTrue(
                driver.getPageSource().contains("GlamourGlow")
        );
    }



    @Test
    void utenteNonAutenticato_vieneReindirizzatoAllaHomePerGestioneUtenti() {

        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=AdminManagement.gestioneutente");

        assertFalse(
                driver.getPageSource().contains("Gestione Utenti")
        );

        assertTrue(
                driver.getPageSource().contains("GlamourGlow"),
                "L'utente non autenticato dovrebbe essere riportato alla Home."
        );
    }




    @Test
    void utenteNonAdmin_puoVisualizzareGestioneOrdini() {

        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=HomeManagement.viewlogin");

        driver.findElement(By.name("Email"))
                .sendKeys("integration.home@glamourglow.test");

        driver.findElement(By.name("Password"))
                .sendKeys("test_password");

        driver.findElement(
                By.cssSelector("button[type='submit']")
        ).click();

        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=AdminManagement.gestioneordini");

        assertTrue(
                driver.getPageSource().contains("Storico Ordini")
                        || driver.findElements(
                        By.cssSelector(".order-management-item")
                ).size() > 0,
                "Un utente autenticato dovrebbe poter visualizzare lo storico ordini."
        );
    }


    @Test
    void utenteNonAdmin_nonPuoModificareStatoOrdine() {

        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=HomeManagement.viewlogin");

        driver.findElement(By.name("Email"))
                .sendKeys("integration.home@glamourglow.test");

        driver.findElement(By.name("Password"))
                .sendKeys("test_password");

        driver.findElement(
                By.cssSelector("button[type='submit']")
        ).click();

        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=AdminManagement.modificaordini"
                + "&id=99"
                + "&stato=Spedito");

        // Un utente non admin non deve poter modificare l'ordine.
        assertFalse(
                driver.getPageSource().contains("Storico Ordini"),
                "Un utente non admin non dovrebbe accedere alla modifica degli ordini."
        );

        // Verifico che l'ordine non sia stato modificato.
        driver.get(BASE_URL +
                "/Dispatcher?controllerAction=AdminManagement.gestioneordini");

        WebElement ordine = driver.findElements(
                        By.cssSelector(".order-management-item")
                )
                .stream()
                .filter(item -> item.getText().contains("Ordine #99"))
                .findFirst()
                .orElseThrow();

        assertEquals(
                "In elaborazione",
                ordine.findElement(By.name("stato"))
                        .getAttribute("value")
        );
    }



}

