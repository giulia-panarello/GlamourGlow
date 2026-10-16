# SPECIFICHE DI PROGETTO — GLAMOURGLOW

> **Progetto per l'esame di Ingegneria del Software Avanzata - Giulia Panarello**  

---

## 1. Introduzione e Obiettivi del Sistema

* **Dominio Applicativo:** Piattaforma e-commerce dedicata alla vendita di prodotti di cosmetica e beauty.
* **Scopo del Sistema:** Offrire un'esperienza d'acquisto completa e intuitiva per i clienti, integrando un sistema di Wallet con ricarica e Coupon promozionali, unita a uno strumento di gestione centralizzato per gli amministratori.
* **Modello Architetturale:** Piattaforma strutturata secondo il pattern Model-View-Controller (MVC) e distribuita in un'architettura monolitica a due livelli, in cui il Web Tier gestisce l'interfaccia e la logica di business, mentre il Data Tier garantisce la persistenza del database, entrambi isolati in container Docker.

---

## 2. Requisiti Funzionali (RF)

### 2.1 Modulo 1: Vetrina, Ricerca & Dettaglio Prodotto
* **RF-1.1 (Homepage & Vetrina):** Il sistema deve visualizzare i prodotti a catalogo organizzandoli in modo chiaro per categoria e marchio.
* **RF-1.2 (Barra di Ricerca e Filtri):** Il sistema deve fornire una barra di ricerca dotata di filtri veloci per la selezione rapida dei prodotti.
* **RF-1.3 (Dettaglio Prodotto):** Il sistema deve mostrare la scheda informativa di ciascun prodotto con descrizione, selezione della quantità desiderata e tasto di inserimento rapido d'acquisto.

### 2.2 Modulo 2: Carrello, Checkout & Wallet
* **RF-2.1 (Carrello e Checkout):** Il sistema deve consentire l'inserimento dei prodotti nel carrello, la consultazione del riepilogo dei costi e la conferma finale dell'acquisto.
* **RF-2.2 (Sconti Coupon):** Il sistema deve permettere l'inserimento di codici promozionali per ricalcolare e scontare il totale dell'ordine.
* **RF-2.3 (Sezione Wallet & Ricarica):** Il sistema deve mettere a disposizione un Wallet per ogni utente e consentire la ricarica rapida del saldo qualora i fondi risultino insufficienti durante il checkout.

### 2.3 Modulo 3: Autenticazione & Area Utente
* **RF-3.1 (Login & Registrazione):** Il sistema deve gestire la registrazione di nuovi clienti e l'autenticazione sicura degli account esistenti.
* **RF-3.2 (Area Personale):** Il sistema deve consentire all'utente di consultare lo storico degli ordini effettuati e gestire i dati del proprio profilo.
* **RF-3.3 (Monitoraggio Credito):** Il sistema deve mostrare in tempo reale il saldo disponibile all'interno del Wallet personale.

### 2.4 Modulo 4: Pannello Amministratore
* **RF-4.1 (Gestione Prodotti):** L'amministratore deve poter eseguire operazioni CRUD (inserimento, modifica, eliminazione) sui prodotti a catalogo.
* **RF-4.2 (Gestione Ordini):** L'amministratore deve poter visualizzare tutti gli ordini ricevuti e modificarne lo stato evolutivo (es. in lavorazione, spedito, consegnato, annullato).
* **RF-4.3 (Gestione Utenti & Coupon):** L'amministratore deve poter amministrare gli account dei clienti e creare nuovi codici sconto Coupon.

---

## 3. Requisiti Non Funzionali (RNF) e Sicurezza

* **RNF-1 (Sicurezza ed Evitamento Vulnerabilità):** Utilizzo obbligatorio di Prepared Statements nel layer DAO per azzerare il rischio di attacchi SQL Injection.
* **RNF-2 (Gestione delle Sessioni):** L'applicazione riconosce l'utente dopo il login e mantiene attiva la sua sessione HTTP in modo sicuro, evitando la perdita del carrello o dell'accesso al profilo durante la navigazione.
* **RNF-3 (Persistenza dei Dati):** Salvataggio permanente delle informazioni su volume dedicato PostgreSQL (`postgres_data`).
* **RNF-4 (Efficienza di Avvio):** Avvio deterministico dell'intero stack applicativo entro 384 ms previa validazione della readiness del database.

---

## 4. Specifiche di Deployment e Infrastruttura Container

* **Architettura a Container Separati:** L'applicazione web (Apache Tomcat 10.1 con Java 17) e il database (PostgreSQL 15) eseguono in due ambienti virtuali Docker separati e isolati.
* **Configurazione Automatica del Database:** Al primo avvio, il database crea automaticamente tutte le tabelle e popola i dati necessari eseguendo lo script `schema.sql`.
* **Avvio Sincronizzato (Zero Race Conditions):** L'applicazione web attende che il database sia completamente avviato e pronto a ricevere connessioni prima di partire (`depends_on: db: condition: service_healthy`), evitando errori di avvio.
* **Rete Interna Dedicata:** La connessione tra applicazione e database avviene tramite una rete virtuale interna sicura, utilizzando l'hostname `db`.

---

## 5. Vincoli Tecnologici e Strumenti Utilizzati

### Stack Tecnologico (Runtime)
* **Backend:** Java 17 / Jakarta EE (Servlet, DAO Pattern, JDBC).
* **Frontend:** JavaServer Pages (JSP), HTML5, CSS3, JavaScript.
* **Web Container:** Apache Tomcat 10.1.
* **Database:** PostgreSQL 15.
* **Containerizzazione:** Docker & Docker Compose per il deployment orchestrato.

### Strumenti di Sviluppo e Modellazione
* **IDE:** IntelliJ IDEA.
* **Controllo Versione:** Git e piattaforma GitHub per il versionamento.
* **Modellazione UML e Statecharts:** Mermaid.js per la generazione dei diagrammi comportamentali e strutturali.

---

## 6. Modellazione Dinamica con Statecharts

### 6.1 Ciclo di Vita dell'Ordine e Checkout
```mermaid
stateDiagram-v2
    direction TB

    [*] --> GestioneOrdine

    state GestioneOrdine {
        [*] --> CarrelloVuoto

        CarrelloVuoto --> BozzaCarrello : aggiungiAlCarrello(idProdotto, quantita) [quantita > 0] / salvaCarrello()
        BozzaCarrello --> BozzaCarrello : aggiungiAlCarrello(idProdotto, quantita) / salvaCarrello()

        BozzaCarrello --> InCheckout : avviaCheckout() [prodottiDisponibili]

        state InCheckout {
            [*] --> VerificaDati
            VerificaDati --> VerificaDati : applicaCoupon(codice) / calcolaSconto()
        }

        InCheckout --> BozzaCarrello : confermaOrdine() [saldoInsufficiente]
        InCheckout --> Pagato : confermaOrdine() [saldoSufficiente] / addebitaWallet()
        InCheckout --> Annullato : annullaCheckout() / rilasciaRisorse()

        Pagato --> InLavorazione : gestisciOrdine() [inLavorazione] / aggiornaStato()
        InLavorazione --> Spedito : spedisciOrdine() [spedito] / inviaTracciamento()
        InLavorazione --> Annullato : annullaOrdine() [annullato] / rimborsaWallet()
        Spedito --> Consegnato : consegnaOrdine() [consegnato] / aggiornaStato()

        Consegnato --> [*]
        Annullato --> [*]
    }

```
### 6.2 Sessione Utente e Autenticazione
```mermaid

stateDiagram-v2
    direction TB

    [*] --> SessioneUtente

    state SessioneUtente {
        [*] --> SessioneAnonima

        SessioneAnonima --> SessioneAutenticata : registrazione(datiUtente) [emailNonEsistente] / creaUtenteESessione()
        SessioneAnonima --> SessioneAutenticata : accedi(email, password) [credenzialiValide] / creaSessione()

        SessioneAutenticata --> SessioneAnonima : disconnetti() / distruggiSessione()

        state SessioneAutenticata {
            state H <<history>>
            [*] --> H
            H --> RuoloCliente

            RuoloCliente --> RuoloCliente : ricaricaWallet(importo) / incrementaSaldo()
            RuoloCliente --> RuoloAmministratore : verificaRuolo() [isAmministratore]
            RuoloAmministratore --> RuoloCliente : tornaAllaVetrina() [isCliente]
        }
    }
```

### 6.3 Gestione Catalogo
```mermaid

stateDiagram-v2
    direction TB

    [*] --> GestioneCatalogo

    state GestioneCatalogo {
        [*] --> ConsultaCatalogo

        ConsultaCatalogo --> DettaglioProdotto : selezionaProdotto(idProdotto) / caricaSchedaProdotto()
        DettaglioProdotto --> ConsultaCatalogo : tornaAlCatalogo() / ripristinaFiltri()
        DettaglioProdotto --> ConsultaCatalogo : cercaProdotti(chiave) / applicaFiltri()

        ConsultaCatalogo --> ModificaProdotto : selezionaModifica(idProdotto) [isAmministratore] / caricaFormModifica()
        ModificaProdotto --> ConsultaCatalogo : salvaProdotto(datiProdotto) / aggiornaDatabase()
    }
```
## 7. Modellazione Dinamica con DFD

### 7.1 DFD DI PRIMO LIVELLO 
```mermaid
graph TD
    classDef entity fill:#e1f5fe,stroke:#0288d1,stroke-width:2px,rx:0,ry:0;
    classDef process fill:#fff3e0,stroke:#f57c00,stroke-width:2px,rx:30,ry:30;
    classDef store fill:#e8f5e9,stroke:#388e3c,stroke-width:2px;

    %% Entità Esterne
    Cliente[ENTITÀ: Cliente]:::entity
    Admin[ENTITÀ: Amministratore]:::entity

    %% Processi
    P1((1.0 VETRINA & RICERCA)):::process
    P2((2.0 AUTENTICAZIONE & AREA UTENTE)):::process
    P3((3.0 CARRELLO, CHECKOUT & WALLET)):::process
    P4((4.0 PANNELLO AMMINISTRATORE)):::process

    %% Archivi Dati
    D1[(D1: Utenti & Wallet)]:::store
    D2[(D2: Prodotti & Stock)]:::store
    D3[(D3: Ordini)]:::store
    D4[(D4: Coupon)]:::store

    %% Flussi Cliente
    Cliente -->|Filtri & Ricerca| P1
    P1 -->|Catalogo & Dettaglio Prodotto| Cliente

    Cliente -->|Credenziali & Ricarica| P2
    P2 -->|Esito Login & Saldo| Cliente

    Cliente -->|Prodotti Scelti & Coupon| P3
    P3 -->|Conferma Ordine & Ricevuta| Cliente

    %% Flussi Admin
    Admin -->|Gestione Prodotti & Coupon| P4
    P4 -->|Report & Esito Operazioni| Admin

    %% Flussi verso/da Archivi (Solo Frecce Singole)
    D2 -->|Dati Prodotti & Prezzi| P1

    D1 -->|Dati Utente & Saldo| P2
    P2 -->|Aggiornamento Credito| D1

    D2 -->|Listino & Giacenza Stock| P3
    D4 -->|Dati Sconto Coupon| P3
    D1 -->|Verifica Saldo| P3
    P3 -->|Addebito Credito| D1
    P3 -->|Scalo Stock Magazzino| D2
    P3 -->|Registrazione Nuovo Ordine| D3

    P4 -->|Modifica Catalogo| D2
    P4 -->|Inserimento Coupon| D4
    P4 -->|Gestione Utenti| D1
    P4 -->|Update Stato Ordini| D3
```
### 7.2 DFD di Livello 2 (Raffinamento Checkout & Wallet)
```mermaid
graph TD
    classDef entity fill:#e1f5fe,stroke:#0288d1,stroke-width:2px,rx:0,ry:0;
    classDef process fill:#fff3e0,stroke:#f57c00,stroke-width:2px,rx:30,ry:30;
    classDef store fill:#e8f5e9,stroke:#388e3c,stroke-width:2px;

    %% Entità Esterna
    Cliente[ENTITÀ: Cliente]:::entity

    %% Sotto-processi del Checkout
    P3_1((3.1 Gestione Carrello<br/>& Prezzo Lordo)):::process
    P3_2((3.2 Verifica & Applicazione<br/>Sconto Coupon)):::process
    P3_3((3.3 Verifica & Addebito<br/>Saldo Wallet)):::process
    P3_4((3.4 Finalizzazione &<br/>Emissione Ricevuta)):::process

    %% Archivi Dati
    D2[(D2: Prodotti & Stock)]:::store
    D4[(D4: Coupon)]:::store
    D1[(D1: Utenti & Wallet)]:::store
    D3[(D3: Ordini)]:::store

    %% 1. FASE CARRELLO & PREZZO LORDO
    Cliente -->|Selezione Prodotti & Quantità| P3_1
    D2 -->|Prezzi Unitari & Giacenza| P3_1
    P3_1 -->|Importo Lordo| P3_2

    %% 2. FASE APPLICAZIONE COUPON
    Cliente -->|Codice Coupon| P3_2
    D4 -->|Dati Sconto & Validità| P3_2
    P3_2 -->|Importo Totale Netto| P3_3

    %% 3. FASE WALLET & PAGAMENTO
    Cliente -->|Richiesta Ricarica Fondo| P3_3
    D1 -->|Saldo Disponibile| P3_3
    P3_3 -->|Addebito Saldo| D1
    P3_3 -->|Esito Pagamento OK| P3_4

    %% 4. FASE FINALIZZAZIONE & SCRITTURA DB
    P3_4 -->|Scalo Quantità Stock| D2
    P3_4 -->|Registrazione Dati Ordine| D3
    P3_4 -->|Ricevuta & Conferma Acquisto| Cliente

```
### 7.3 DFD Livello 2 — Vetrina, Ricerca & Dettaglio
```mermaid
graph TD
    classDef entity fill:#e1f5fe,stroke:#0288d1,stroke-width:2px,rx:0,ry:0;
    classDef process fill:#fff3e0,stroke:#f57c00,stroke-width:2px,rx:30,ry:30;
    classDef store fill:#e8f5e9,stroke:#388e3c,stroke-width:2px;

    Cliente[ENTITÀ: Cliente]:::entity

    P1_1((1.1 Filtro Categoria<br/>& Brand)):::process
    P1_2((1.2 Ricerca Testuale<br/>& Parole Chiave)):::process
    P1_3((1.3 Generazione Scheda<br/>Dettaglio Prodotto)):::process

    D2[(D2: Prodotti & Stock)]:::store

    %% Flussi Filtri
    Cliente -->|Selezione Categoria / Brand| P1_1
    D2 -->|Elenco Categorie & Marche| P1_1
    P1_1 -->|Prodotti Filtrati| P1_3

    %% Flussi Ricerca Testuale
    Cliente -->|Stringa di Ricerca| P1_2
    D2 -->|Indice Prodotti & Titoli| P1_2
    P1_2 -->|Risultati Ricerca| P1_3

    %% Flussi Scheda Prodotto
    D2 -->|Scheda Tecnica, Prezzo & Stock| P1_3
    P1_3 -->|Pagina Dettaglio Prodotto| Cliente
```
### 7.4 DFD Livello 2 — Autenticazione & Area Utente
```mermaid
graph TD
    classDef entity fill:#e1f5fe,stroke:#0288d1,stroke-width:2px,rx:0,ry:0;
    classDef process fill:#fff3e0,stroke:#f57c00,stroke-width:2px,rx:30,ry:30;
    classDef store fill:#e8f5e9,stroke:#388e3c,stroke-width:2px;

    Cliente[ENTITÀ: Cliente]:::entity

    P2_1((2.1 Validazione<br/>Credenziali Login)):::process
    P2_2((2.2 Gestione Sessione<br/>& Saldo Wallet)):::process
    P2_3((2.3 Estrazione Storico<br/>Ordini Utente)):::process

    D1[(D1: Utenti & Wallet)]:::store
    D3[(D3: Ordini)]:::store

    %% Login
    Cliente -->|Email & Password| P2_1
    D1 -->|Hash Password & Ruolo| P2_1
    P2_1 -->|Esito Login OK| P2_2

    %% Sessione e Credito
    Cliente -->|Richiesta Saldo / Ricarica| P2_2
    D1 -->|Saldo Wallet Attuale| P2_2
    P2_2 -->|Update Ricarica Saldo| D1
    P2_2 -->|Dati Profilo & Saldo| Cliente

    %% Storico Ordini
    Cliente -->|Richiesta Storico Acquisti| P2_3
    D3 -->|Registro Ordini Utente| P2_3
    P2_3 -->|Storico Ordini & Stati| Cliente
```
### 7.5 DFD Livello 2 — Pannello Amministratore
```mermaid
graph TD
    classDef entity fill:#e1f5fe,stroke:#0288d1,stroke-width:2px,rx:0,ry:0;
    classDef process fill:#fff3e0,stroke:#f57c00,stroke-width:2px,rx:30,ry:30;
    classDef store fill:#e8f5e9,stroke:#388e3c,stroke-width:2px;

    Admin[ENTITÀ: Amministratore]:::entity

    P4_1((4.1 Gestione Catalogo<br/>& Giacenze Magazzino)):::process
    P4_2((4.2 Generazione<br/>Codici Coupon)):::process
    P4_3((4.3 Gestione & Cambio<br/>Stato Ordini)):::process

    D2[(D2: Prodotti & Stock)]:::store
    D4[(D4: Coupon)]:::store
    D3[(D3: Ordini)]:::store

    %% CRUD Prodotti
    Admin -->|Nuovo Prodotto / Modifica Prezzo| P4_1
    P4_1 -->|Scrittura / Update Catalogo| D2
    P4_1 -->|Esito Operazione Catalogo| Admin

    %% CRUD Coupon
    Admin -->|Codice, % Sconto & Scadenza| P4_2
    P4_2 -->|Inserimento Nuovo Coupon| D4
    P4_2 -->|Conferma Attivazione Coupon| Admin

    %% Gestione Ordini
    Admin -->|ID Ordine & Nuovo Stato| P4_3
    D3 -->|Stato Attuale Ordine| P4_3
    P4_3 -->|Update Stato Ordine| D3
    P4_3 -->|Conferma Aggiornamento| Admin
```