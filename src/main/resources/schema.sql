--
-- PostgreSQL database dump
--

\restrict hW2RYcw2bAwcb7VcUaN8swz46yUcSs3swMIJ5PhCPs6bbRzP09EdOP08qw058z2

-- Dumped from database version 18.3 (Homebrew)
-- Dumped by pg_dump version 18.3 (Homebrew)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: categoria; Type: TABLE; Schema: public; Owner: giuliapanarello
--

CREATE TABLE public.categoria (
    id_categoria integer NOT NULL,
    nome_categoria character varying(100) NOT NULL,
    descrizione character varying(1000) NOT NULL
);


ALTER TABLE public.categoria OWNER TO giuliapanarello;

--
-- Name: categoria_id_categoria_seq; Type: SEQUENCE; Schema: public; Owner: giuliapanarello
--

CREATE SEQUENCE public.categoria_id_categoria_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.categoria_id_categoria_seq OWNER TO giuliapanarello;

--
-- Name: categoria_id_categoria_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: giuliapanarello
--

ALTER SEQUENCE public.categoria_id_categoria_seq OWNED BY public.categoria.id_categoria;


--
-- Name: coupon; Type: TABLE; Schema: public; Owner: giuliapanarello
--

CREATE TABLE public.coupon (
    codice character varying(10) NOT NULL,
    sconto integer NOT NULL
);


ALTER TABLE public.coupon OWNER TO giuliapanarello;

--
-- Name: dettagli_ordine; Type: TABLE; Schema: public; Owner: giuliapanarello
--

CREATE TABLE public.dettagli_ordine (
    id_dettaglio integer NOT NULL,
    id_ordine integer NOT NULL,
    id_prodotto integer NOT NULL,
    quantita integer NOT NULL,
    prezzo_unitario numeric(10,2) NOT NULL,
    coupon character varying(10)
);


ALTER TABLE public.dettagli_ordine OWNER TO giuliapanarello;

--
-- Name: dettagli_ordine_id_dettaglio_seq; Type: SEQUENCE; Schema: public; Owner: giuliapanarello
--

CREATE SEQUENCE public.dettagli_ordine_id_dettaglio_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.dettagli_ordine_id_dettaglio_seq OWNER TO giuliapanarello;

--
-- Name: dettagli_ordine_id_dettaglio_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: giuliapanarello
--

ALTER SEQUENCE public.dettagli_ordine_id_dettaglio_seq OWNED BY public.dettagli_ordine.id_dettaglio;


--
-- Name: marchio; Type: TABLE; Schema: public; Owner: giuliapanarello
--

CREATE TABLE public.marchio (
    id_marchio integer NOT NULL,
    nome_marchio character varying(100) NOT NULL
);


ALTER TABLE public.marchio OWNER TO giuliapanarello;

--
-- Name: marchio_id_marchio_seq; Type: SEQUENCE; Schema: public; Owner: giuliapanarello
--

CREATE SEQUENCE public.marchio_id_marchio_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.marchio_id_marchio_seq OWNER TO giuliapanarello;

--
-- Name: marchio_id_marchio_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: giuliapanarello
--

ALTER SEQUENCE public.marchio_id_marchio_seq OWNED BY public.marchio.id_marchio;


--
-- Name: ordine; Type: TABLE; Schema: public; Owner: giuliapanarello
--

CREATE TABLE public.ordine (
    id_ordine integer NOT NULL,
    id_utente integer NOT NULL,
    data_ordine timestamp without time zone NOT NULL,
    stato_ordine character varying(45) DEFAULT 'In elaborazione'::character varying NOT NULL,
    totale_ordine numeric(10,2) NOT NULL,
    indirizzo_consegna character varying(100) NOT NULL,
    citta character varying(45) NOT NULL,
    stato character varying(45) NOT NULL
);


ALTER TABLE public.ordine OWNER TO giuliapanarello;

--
-- Name: ordine_id_ordine_seq; Type: SEQUENCE; Schema: public; Owner: giuliapanarello
--

CREATE SEQUENCE public.ordine_id_ordine_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.ordine_id_ordine_seq OWNER TO giuliapanarello;

--
-- Name: ordine_id_ordine_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: giuliapanarello
--

ALTER SEQUENCE public.ordine_id_ordine_seq OWNED BY public.ordine.id_ordine;


--
-- Name: prodotto; Type: TABLE; Schema: public; Owner: giuliapanarello
--

CREATE TABLE public.prodotto (
    id_prodotto integer NOT NULL,
    nome_prodotto character varying(255) NOT NULL,
    descrizione text NOT NULL,
    prezzo numeric(10,2) NOT NULL,
    quantita_disponibile integer NOT NULL,
    id_categoria integer NOT NULL,
    id_marchio integer NOT NULL,
    in_promozione boolean DEFAULT false NOT NULL,
    immagine character varying(955) NOT NULL,
    prezzo_scontato numeric(10,6) DEFAULT NULL::numeric,
    stato_prodotto boolean DEFAULT false NOT NULL
);


ALTER TABLE public.prodotto OWNER TO giuliapanarello;

--
-- Name: prodotto_id_prodotto_seq; Type: SEQUENCE; Schema: public; Owner: giuliapanarello
--

CREATE SEQUENCE public.prodotto_id_prodotto_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.prodotto_id_prodotto_seq OWNER TO giuliapanarello;

--
-- Name: prodotto_id_prodotto_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: giuliapanarello
--

ALTER SEQUENCE public.prodotto_id_prodotto_seq OWNED BY public.prodotto.id_prodotto;


--
-- Name: utente; Type: TABLE; Schema: public; Owner: giuliapanarello
--

CREATE TABLE public.utente (
    id_nome integer NOT NULL,
    nome character varying(100) NOT NULL,
    cognome character varying(100) NOT NULL,
    email character varying(100) NOT NULL,
    password character varying(255) NOT NULL,
    ruolo smallint DEFAULT 0 NOT NULL,
    telefono character varying(20) NOT NULL,
    stato_account character varying(50) DEFAULT 'attivo'::character varying NOT NULL,
    wallet numeric(10,2) DEFAULT 0.00 NOT NULL
);


ALTER TABLE public.utente OWNER TO giuliapanarello;

--
-- Name: utente_id_nome_seq; Type: SEQUENCE; Schema: public; Owner: giuliapanarello
--

CREATE SEQUENCE public.utente_id_nome_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.utente_id_nome_seq OWNER TO giuliapanarello;

--
-- Name: utente_id_nome_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: giuliapanarello
--

ALTER SEQUENCE public.utente_id_nome_seq OWNED BY public.utente.id_nome;


--
-- Name: categoria id_categoria; Type: DEFAULT; Schema: public; Owner: giuliapanarello
--

ALTER TABLE ONLY public.categoria ALTER COLUMN id_categoria SET DEFAULT nextval('public.categoria_id_categoria_seq'::regclass);


--
-- Name: dettagli_ordine id_dettaglio; Type: DEFAULT; Schema: public; Owner: giuliapanarello
--

ALTER TABLE ONLY public.dettagli_ordine ALTER COLUMN id_dettaglio SET DEFAULT nextval('public.dettagli_ordine_id_dettaglio_seq'::regclass);


--
-- Name: marchio id_marchio; Type: DEFAULT; Schema: public; Owner: giuliapanarello
--

ALTER TABLE ONLY public.marchio ALTER COLUMN id_marchio SET DEFAULT nextval('public.marchio_id_marchio_seq'::regclass);


--
-- Name: ordine id_ordine; Type: DEFAULT; Schema: public; Owner: giuliapanarello
--

ALTER TABLE ONLY public.ordine ALTER COLUMN id_ordine SET DEFAULT nextval('public.ordine_id_ordine_seq'::regclass);


--
-- Name: prodotto id_prodotto; Type: DEFAULT; Schema: public; Owner: giuliapanarello
--

ALTER TABLE ONLY public.prodotto ALTER COLUMN id_prodotto SET DEFAULT nextval('public.prodotto_id_prodotto_seq'::regclass);


--
-- Name: utente id_nome; Type: DEFAULT; Schema: public; Owner: giuliapanarello
--

ALTER TABLE ONLY public.utente ALTER COLUMN id_nome SET DEFAULT nextval('public.utente_id_nome_seq'::regclass);


--
-- Data for Name: categoria; Type: TABLE DATA; Schema: public; Owner: giuliapanarello
--

COPY public.categoria (id_categoria, nome_categoria, descrizione) FROM stdin;
2002	Capelli	Prodotti per la cura e lo styling dei capelli, come shampoo, balsami e maschere nutrienti.
2003	Cura del corpo	Prodotti per l’igiene quotidiana, come saponi, gel doccia, deodoranti, scrub e creme corpo.
2005	Profumi	Fragranze per uomo e donna, disponibili in diverse note e intensità.
2007	Makeup	Prodotti cosmetici per il trucco, inclusi fondotinta, ombretti e rossetti.
2009	Trattamenti anti-invecchiamento	Prodotti specifici per combattere i segni dell’invecchiamento della pelle.
2011	Cura del viso	Prodotti dedicati alla cura del viso, come tonici, maschere e sieri.
\.


--
-- Data for Name: coupon; Type: TABLE DATA; Schema: public; Owner: giuliapanarello
--

COPY public.coupon (codice, sconto) FROM stdin;
444466	40
999999998	60
A1B2C3D4E9	30
ABCD1234EF	15
ABCD1234GD	80
gsg	40
HGFEDCBA12	20
LKMNOPQRST	25
MNBVCXZ098	40
PLOIKUJ987	15
PQR456TYUI	30
ssssssssss	10
XYZ1234JKL	5
ZXY98765KL	20
T612300453	25
N12460224	35
E2ECPN001	25
T777915758	25
222	10
N77915913	35
T778710924	25
N78711074	35
T778995425	25
N78995584	35
T794905290	25
N94905442	35
T795000466	25
QWERTYUIOP	55
N95000621	35
INTUSER01	10
INTUSER02	20
INTUSER03	30
ITCOUPON01	10
\.


--
-- Data for Name: dettagli_ordine; Type: TABLE DATA; Schema: public; Owner: giuliapanarello
--

COPY public.dettagli_ordine (id_dettaglio, id_ordine, id_prodotto, quantita, prezzo_unitario, coupon) FROM stdin;
86	70	4005	2	20.00	ABCD1234EF
87	70	4008	2	10.00	\N
88	71	4008	1	10.00	222
89	72	4074	1	98.00	\N
90	73	4075	1	70.00	gsg
91	74	4023	1	19.00	\N
92	75	4004	1	18.53	\N
93	76	4001	1	30.00	\N
94	76	4075	2	70.00	XYZ1234JKL
96	78	4005	1	20.00	\N
97	79	4005	1	20.00	444466
98	80	4005	1	20.00	\N
99	81	4005	1	20.00	444466
100	82	4005	1	20.00	\N
101	83	4005	1	20.00	444466
102	84	4004	1	18.53	\N
103	85	4005	1	20.00	\N
104	86	4004	1	18.53	\N
105	87	4005	1	20.00	444466
106	88	4005	1	20.00	\N
107	89	4004	1	18.53	\N
108	90	4005	1	20.00	444466
109	91	4005	1	20.00	\N
110	92	4004	1	18.53	\N
111	93	4005	1	20.00	444466
112	94	4005	1	20.00	\N
113	95	4004	1	18.53	\N
114	96	4005	1	20.00	444466
115	97	4005	1	20.00	\N
116	98	4004	1	18.53	\N
117	99	4005	1	20.00	444466
442	436	4963	1	10.00	ITCOUPON01
\.


--
-- Data for Name: marchio; Type: TABLE DATA; Schema: public; Owner: giuliapanarello
--

COPY public.marchio (id_marchio, nome_marchio) FROM stdin;
3001	MAC Cosmetics
3002	NARS
3003	Fenty Beauty
3004	L'Oréal Professionnel
3005	Wella
3006	Dove
3007	Nivea
3008	Pantene
3009	Avene
3010	CeraVe
3011	Versace
3012	La Roche-Posay
3013	Bvlgari
3014	Neutrogena
3015	Clinique
3016	Dior
3017	Estée Lauder
3026	Lancôme
3027	YSL
3028	Gucci
\.


--
-- Data for Name: ordine; Type: TABLE DATA; Schema: public; Owner: giuliapanarello
--

COPY public.ordine (id_ordine, id_utente, data_ordine, stato_ordine, totale_ordine, indirizzo_consegna, citta, stato) FROM stdin;
71	3	2025-01-24 17:22:42	In elaborazione	9.00	via piangipane 14	ferrara	italia
72	3	2025-01-24 17:37:49	completo	98.00	via piangipane 14	ferrara	italia
73	3	2025-01-24 17:58:01	In elaborazione	42.00	via piangipane 14	ferrara	italia
74	4	2025-01-24 18:28:11	In elaborazione	19.00	via piangipane 14	ferrara	italia
75	3	2025-01-24 19:04:09	Completo	18.53	via piangipane 14	ferrara	italia
76	28	2025-01-25 09:31:38	Completato	163.00	via ss	ragusa	italia
78	1	2026-09-04 20:10:46	In elaborazione	20.00	Via Roma 10	Ferrara	Italia
79	27	2026-09-05 14:19:51	In elaborazione	12.00	Via Roma 10	Ferrara	Italia
80	1	2026-09-05 14:32:31	In elaborazione	20.00	Via Roma 10	Ferrara	Italia
81	27	2026-09-05 14:32:31	In elaborazione	12.00	Via Roma 10	Ferrara	Italia
82	1	2026-09-05 14:38:16	In elaborazione	20.00	Via Roma 10	Ferrara	Italia
83	27	2026-09-05 14:38:16	In elaborazione	12.00	Via Roma 10	Ferrara	Italia
84	1	2026-09-07 12:40:08	In elaborazione	18.53	Via Roma 10	Ferrara	Italia
85	1	2026-09-07 12:45:15	In elaborazione	20.00	Via Roma 10	Ferrara	Italia
86	1	2026-09-07 12:45:15	In elaborazione	18.53	Via Roma 10	Ferrara	Italia
87	27	2026-09-07 12:45:15	In elaborazione	12.00	Via Roma 10	Ferrara	Italia
88	1	2026-09-07 12:58:30	In elaborazione	20.00	Via Roma 10	Ferrara	Italia
89	1	2026-09-07 12:58:30	In elaborazione	18.53	Via Roma 10	Ferrara	Italia
90	27	2026-09-07 12:58:30	In elaborazione	12.00	Via Roma 10	Ferrara	Italia
91	1	2026-09-07 13:03:14	In elaborazione	20.00	Via Roma 10	Ferrara	Italia
92	1	2026-09-07 13:03:15	In elaborazione	18.53	Via Roma 10	Ferrara	Italia
93	27	2026-09-07 13:03:15	In elaborazione	12.00	Via Roma 10	Ferrara	Italia
94	1	2026-09-07 17:28:24	In elaborazione	20.00	Via Roma 10	Ferrara	Italia
95	1	2026-09-07 17:28:24	In elaborazione	18.53	Via Roma 10	Ferrara	Italia
96	27	2026-09-07 17:28:25	In elaborazione	12.00	Via Roma 10	Ferrara	Italia
97	1	2026-09-07 17:29:59	In elaborazione	20.00	Via Roma 10	Ferrara	Italia
98	1	2026-09-07 17:30:00	In elaborazione	18.53	Via Roma 10	Ferrara	Italia
70	27	2025-01-24 17:19:14	spedito	54.00	via piangipane 14	ferrara	italia
99	27	2026-09-07 17:30:00	In elaborazione	12.00	Via Roma 10	Ferrara	Italia
436	1225	2026-09-08 14:30:00	nuovo	10.00	Via Test 1	Milano	Italia
\.


--
-- Data for Name: prodotto; Type: TABLE DATA; Schema: public; Owner: giuliapanarello
--

COPY public.prodotto (id_prodotto, nome_prodotto, descrizione, prezzo, quantita_disponibile, id_categoria, id_marchio, in_promozione, immagine, prezzo_scontato, stato_prodotto) FROM stdin;
4003	Lipstick - Ruby W	Rossetto opaco iconico in una tonalità rossa classica.	20.00	150	2007	3001	t	rossettomac.jpg	15.000000	t
4004	Eye Shadow - Soft Brown	Ombretto matte in una calda tonalità marrone.	18.53	120	2007	3001	f	ombrmac.jpg	0.000000	f
4005	Pro Longwear Concealer	Correttore a lunga durata con copertura media.	28.00	90	2007	3001	t	correttoremac.jpg	20.000000	f
4006	Pro-V Daily Moisture Shampoo	Shampoo idratante quotidiano per capelli morbidi e setosi.	10.00	150	2002	3008	t	shampoopant.jpg	5.000000	t
4007	Pro-V Repair & Protect Conditioner	Balsamo riparatore per capelli danneggiati e secchi.	12.00	120	2002	3008	f	conditionerpant.jpg	0.000000	f
4008	Intense Hydration Hair Mask	Maschera per capelli intensamente idratante, ideale per capelli secchi.	15.00	80	2002	3008	t	maskpant.jpg	10.000000	f
4009	Smooth & Sleek Serum	Siero lisciante per un finish lucido e setoso.	18.00	100	2002	3008	t	serumpant.jpg	15.000000	f
4010	Fresh Foaming Cleanser	Detergente schiumoso per una pulizia delicata e profonda.	10.00	200	2003	3014	t	detneu.jpg	5.000000	f
4011	Hydrating Body Wash	Gel doccia idratante per una pelle morbida e setosa.	12.00	150	2003	3014	f	gelneu.jpg	\N	f
4012	Gentle Exfoliating Scrub	Scrub esfoliante delicato per rimuovere le cellule morte.	15.00	100	2003	3014	t	scrubneu.jpg	12.000000	f
4013	Oil-Free Acne Wash	Detergente viso senza olio, specifico per pelli acneiche.	9.00	180	2003	3014	f	oilneu.jpg	\N	f
4014	Moisturizing Hand Soap	Sapone liquido idratante per mani morbide e pulite.	8.00	250	2003	3014	t	handneu.jpg	5.500000	f
4015	Toleriane Ultra Fluid	Crema idratante fluida per pelli sensibili, senza profumo.	30.00	100	2011	3012	t	toleroche.jpg	25.900000	f
4016	Effaclar Purifying Gel	Gel detergente purificante per pelli grasse e acneiche.	15.00	80	2011	3012	f	detroche.jpg	\N	f
4017	Hydraphase Intense	Crema idratante intensiva con acido ialuronico.	32.00	60	2011	3012	t	serumroche.jpg	27.400000	f
4018	Anthelios Melt-in Cream	Crema solare viso ad alta protezione, non grassa.	28.00	120	2011	3012	f	spfroche.jpg	\N	f
4019	Cicaplast Baume B5	Balsamo riparatore per pelli sensibili e irritate.	20.00	150	2011	3012	t	cicaroche.jpg	15.700000	f
4020	Pigmentclar Serum	Siero anti-macchie con un effetto illuminante.	45.00	50	2011	3012	f	pigmroche.jpg	\N	f
4021	Redermic C Eyes	Crema contorno occhi antiossidante e anti-invecchiamento.	40.00	90	2011	3012	t	vitaminaroche.jpg	35.500000	f
4022	Pro Filt'r Soft Matte Foundation	Fondotinta a lunga durata con finitura opaca, adatto a tutti i tipi di pelle.	34.00	120	2007	3003	t	fondofenty.jpg	26.800000	f
4023	Gloss Bomb Universal Lip Luminizer	Lucidalabbra con effetto luminoso universale, adatto a tutte le carnagioni.	19.00	150	2007	3003	f	glossfenty.jpg	\N	f
4024	Match Stix Matte Skinstick	Stick multiuso per contouring, blush e illuminante, in diverse tonalità.	25.00	100	2007	3003	t	contfenty.jpg	17.300000	f
4025	Killawatt Freestyle Highlighter	Illuminante duo con colori ad alta intensità per viso e occhi.	36.00	80	2007	3003	f	illfenty.jpg	\N	f
4026	Flyliner Longwear Liquid Eyeliner	Eyeliner liquido waterproof a lunga durata con punta ultra-fine.	22.00	90	2007	3003	t	eyefenty.jpg	18.900000	f
4027	Radiant Creamy Concealer	Correttore idratante e a lunga durata, perfetto per coprire occhiaie e imperfezioni.	30.00	100	2007	3002	f	corrnars.jpg	\N	f
4028	Sheer Glow Foundation	Fondotinta luminoso e leggero che migliora l'aspetto della pelle.	40.00	150	2007	3002	t	fondnars.jpg	36.600000	f
4029	Velvet Matte Lip Pencil	Matita labbra vellutata con una finitura opaca.	25.00	200	2007	3002	f	matitanars.jpg	\N	f
4030	Blush Orgasm	Blush iconico dal finish luminoso e tonalità universale.	32.00	120	2007	3002	t	blushnars.jpg	28.600000	f
4031	Climax Mascara	Mascara volumizzante per ciglia straordinarie.	28.00	90	2007	3002	f	mascaranars.jpg	\N	f
4032	Elvive Total Repair 5 Shampoo	Shampoo riparatore per capelli danneggiati, arricchito con Pro-Keratina.	8.99	300	2002	3004	t	elviveshampoo.jpg	5.800000	f
4033	Elvive Extraordinary Oil	Olio per capelli secchi, dona morbidezza e lucentezza senza appesantire.	12.50	200	2002	3004	f	oilelvive.jpg	\N	f
4034	EverPure Sulfate-Free Conditioner	Balsamo senza solfati per capelli colorati, protegge e idrata.	10.99	250	2002	3004	t	conditionerelvive.jpg	6.890000	f
4035	Elnett Satin Hairspray	Lacca per capelli ad alta tenuta, protegge lo styling e si elimina con un colpo di spazzola.	6.99	400	2002	3004	f	laccaelvive.jpg	\N	f
4036	Elvive Dream Lengths Mask	Maschera ristrutturante per capelli lunghi, rinforza e previene le doppie punte.	9.99	150	2002	3004	f	maskelvive.jpg	\N	f
4037	Wella Fusion Intense Repair Shampoo	Shampoo riparatore intensivo per capelli danneggiati, protegge e rafforza le fibre capillari.	14.50	180	2002	3005	t	maskwella.jpg	12.800000	f
4038	Wella Invigo Nutri-Enrich Conditioner	Balsamo nutriente per capelli secchi o stressati, con bacche di Goji e vitamina E.	11.99	220	2002	3005	f	condiwella.jpg	\N	f
4039	Wella Color Brilliance Mask	Maschera per capelli colorati, intensifica e prolunga la brillantezza del colore.	15.99	150	2002	3005	t	colorwella.jpg	10.980000	f
4040	Wella EIMI Perfect Setting Spray	Spray per lo styling, dona volume e lucentezza, proteggendo i capelli dal calore.	9.99	300	2002	3005	f	sugarwella.jpg	\N	f
4041	Wella Elements Renewing Leave-in Spray	Spray senza risciacquo, ristruttura e protegge i capelli fragili, senza solfati e parabeni.	13.50	250	2002	3005	f	elementswella.jpg	\N	f
4042	Dove Deep Moisture Body Wash	Detergente per il corpo idratante, lascia la pelle morbida e setosa dopo ogni doccia.	5.99	150	2003	3006	f	dovecrema.jpg	\N	f
4043	Dove Exfoliating Body Scrub	Scrub esfoliante per il corpo con granuli fini, rimuove delicatamente le cellule morte della pelle.	7.50	120	2003	3006	t	srubdove.jpg	5.800000	f
4044	Dove Nourishing Body Lotion	Lozione corpo nutriente, idrata profondamente e dona alla pelle un aspetto sano e luminoso.	6.99	180	2003	3006	f	bodydove.jpg	\N	f
4045	Dove DermaSpa Uplifted+ Body Oil	Olio corpo rassodante e tonificante, aiuta a migliorare l'elasticità della pelle.	9.99	80	2003	3006	t	dermadove.jpg	7.800000	f
4046	Dove Silk Glow Body Wash	Bagnoschiuma setoso che lascia la pelle morbida e radiosa, con una fragranza leggera e rilassante.	6.50	200	2003	3006	f	bagnodove.jpg	\N	f
4047	Nivea Rich Nourishing Body Lotion	Lozione per il corpo idratante con olio di mandorla, dona idratazione profonda per 48 ore.	6.99	200	2003	3007	f	cremanivea.jpg	\N	f
4002	Powder Blush	Fard in polvere setosa per un colorito radioso.	25.00	80	2007	3001	t	fardmac.jpg	20.000000	f
4048	Nivea Firming Q10 Plus Body Lotion	Lozione corpo rassodante con coenzima Q10, migliora l'elasticità della pelle in 2 settimane.	8.50	150	2003	3007	f	firmingnivea.jpg	\N	f
4049	Nivea In-Shower Body Moisturiser	Idratante corpo da utilizzare sotto la doccia, per una pelle morbida e liscia.	5.99	180	2003	3007	f	showernivea.jpg	\N	f
4050	Nivea Smooth Sensation Body Cream	Crema corpo nutriente, dona alla pelle un aspetto liscio e setoso.	7.20	170	2003	3007	f	drynivea.jpg	\N	f
4051	Nivea Aloe & Hydration Body Lotion	Lozione corpo con estratto di aloe vera, idrata intensamente per una pelle rinfrescata.	6.50	190	2003	3007	f	aloenivea.jpg	\N	f
4052	Avene Hydrance Optimale Riche	Crema viso idratante per pelli sensibili e secche, mantiene la pelle idratata a lungo.	22.99	120	2011	3009	f	hydraavene.jpg	\N	f
4053	Avene Cleanance Gel	Gel detergente per il viso per pelli grasse e a tendenza acneica, purifica e riduce il sebo in eccesso.	15.50	150	2011	3009	t	gelavene.jpg	10.000000	f
4054	Avene Cicalfate Restorative Cream	Crema riparatrice per pelli irritate o danneggiate, lenisce e favorisce la rigenerazione della pelle.	12.99	200	2011	3009	f	cicalavene.jpg	\N	f
4055	Avene Tolerance Extreme Emulsion	Emulsione leggera per il viso, specifica per pelli ultra sensibili e allergiche.	24.99	80	2011	3009	t	toleranceavene.jpg	20.900000	f
4056	Avene Antirougeurs Fort	Trattamento concentrato per ridurre i rossori localizzati sul viso, ideale per pelli sensibili.	29.99	90	2011	3009	f	antiroavene.jpg	\N	f
4057	CeraVe Hydrating Cleanser	Detergente viso idratante che rimuove impurità senza alterare la barriera protettiva della pelle.	10.99	180	2011	3010	f	hydracerave.jpg	\N	f
4058	CeraVe Facial Moisturizing Lotion	Lozione viso leggera per idratazione continua per tutto il giorno, con protezione SPF 25.	12.50	120	2011	3010	t	feuchcerave.jpg	10.000000	f
4059	CeraVe Foaming Facial Cleanser	Detergente schiumogeno per il viso, ideale per pelli normali e grasse, purifica e rinfresca.	9.99	150	2011	3010	f	foamingcerave.jpg	\N	f
4060	CeraVe Eye Repair Cream	Crema contorno occhi per ridurre borse e occhiaie, con acido ialuronico e ceramidi.	13.99	100	2011	3010	t	repaircerave.jpg	9.900000	f
4061	CeraVe PM Facial Moisturizing Lotion	Lozione viso notte idratante, rigenera la pelle durante il sonno con niacinamide e ceramidi.	15.99	90	2011	3010	f	lotioncerave.jpg	\N	f
4062	Versace Eros Eau de Toilette	Una fragranza maschile fresca e seducente con note di menta, mela verde e limone.	85.00	120	2005	3011	f	erosversace.jpg	\N	f
4063	Versace Pour Homme Eau de Toilette	Profumo aromatico per uomo con note di bergamotto, neroli, limone e salvia sclarea.	79.99	100	2005	3011	f	ethosversace.jpg	\N	f
4064	Versace Dylan Blue Eau de Toilette	Una fragranza maschile intensa e moderna con note di agrumi, ambra e patchouli.	89.50	80	2005	3011	t	bluversace.jpg	70.000000	f
4065	Versace Bright Crystal Eau de Toilette	Fragranza femminile leggera e floreale con note di melograno, magnolia e peonia.	75.00	150	2005	3011	f	crystalversace.jpg	\N	f
4066	Versace Crystal Noir Eau de Parfum	Profumo femminile ricco e misterioso con note di gardenia, ambra e muschio.	95.00	90	2005	3011	t	noirversace.jpg	80.700000	f
4067	Versace Yellow Diamond Eau de Toilette	Una fragranza floreale e luminosa con note di limone, fresia e muschio.	70.99	130	2005	3011	f	gialloversace.jpg	\N	f
4068	Bvlgari Man in Black Eau de Parfum	Una fragranza maschile audace e carismatica con note di rum, spezie e cuoio.	95.00	110	2005	3013	f	blackbvlgari.jpg	\N	f
4069	Bvlgari Man Wood Essence Eau de Parfum	Profumo maschile fresco e legnoso con note di agrumi, coriandolo e cipresso.	89.50	95	2005	3013	t	verdebvlgari.jpg	75.900000	f
4070	Bvlgari Man Glacial Essence Eau de Parfum	Fragranza maschile vibrante e rinfrescante con note di ginepro, legno di sandalo e cedro.	92.00	120	2005	3013	f	azzbvlgari.jpg	\N	f
4071	Bvlgari Omnia Crystalline Eau de Toilette	Fragranza femminile delicata e pura con note di bambù, loto e muschio bianco.	78.00	140	2005	3013	f	omniabulgari.jpg	\N	f
4072	Bvlgari Rose Goldea Eau de Parfum	Profumo femminile floreale e sensuale con note di rosa, melograno e muschio.	99.00	85	2005	3013	t	goldeabvlgari.jpg	85.000000	f
4073	Bvlgari Jasmin Noir Eau de Parfum	Fragranza femminile intensa e misteriosa con note di gelsomino, mandorla e liquirizia.	110.00	70	2005	3013	t	jasmine.jpg	80.000000	f
4074	Clinique Smart Custom-Repair Serum	Siero anti-età intelligente che ripara visibilmente rughe, tono e texture della pelle.	98.00	50	2009	3015	f	smartclinique.jpg	\N	f
4075	Clinique Repairwear Laser Focus	Trattamento anti-età con tripla azione, riduce visibilmente le linee sottili e migliora la luminosità della pelle.	82.00	60	2009	3015	t	focusclinique.jpg	70.000000	f
4076	Clinique Superdefense Night Recovery Moisturizer	Crema notte anti-età che ripristina la pelle durante la notte per un aspetto giovane al risveglio.	70.00	80	2009	3015	f	nightclinique.jpg	\N	f
4077	Clinique Even Better Clinical Dark Spot Corrector	Siero correttivo anti-età che aiuta a ridurre le macchie scure e uniformare il tono della pelle.	90.00	45	2009	3015	t	betterclinique.jpg	60.000000	f
4078	Dior Sauvage Eau de Toilette	Una fragranza fresca con note di bergamotto e ambroxan, per un uomo audace e moderno.	95.00	100	2005	3016	f	sauvagedior.jpg	\N	f
4079	Dior Homme Eau de Toilette	Una fragranza legnosa e speziata con note di cedro e vetiver, per un uomo elegante e sicuro di sé.	88.00	80	2005	3016	f	hommedior.jpg	\N	f
4080	Fahrenheit by Dior	Un profumo caldo e avvolgente con note di cuoio e legno, perfetto per un uomo sicuro di sé.	105.00	70	2005	3016	f	faherdior.jpg	\N	f
4081	J'adore Eau de Parfum	Una fragranza femminile con note di fiori bianchi e ylang-ylang, elegante e sensuale.	110.00	90	2005	3016	f	jadoredior.jpg	\N	f
4082	Miss Dior Blooming Bouquet	Una fragranza floreale fresca e leggera con note di peonia e rosa di Damasco.	95.00	85	2005	3016	f	missdior.jpg	\N	f
4083	Dior Addict Eau de Parfum	Una fragranza intensa con note di vaniglia e gelsomino, per una donna audace e passionale.	120.00	60	2005	3016	f	bludior.jpg	\N	f
4084	Estée Lauder Advanced Night Repair	Siero anti-età riparatore notturno che aiuta a ridurre i segni dell'invecchiamento e a migliorare la luminosità della pelle.	120.00	50	2009	3017	f	repairestee.jpg	\N	f
4085	Estée Lauder Revitalizing Supreme+ Global Anti-Aging Creme	Crema multi-azione anti-invecchiamento che riduce rughe e linee sottili, donando alla pelle un aspetto più giovane e fresco.	95.00	70	2009	3017	t	supremeestee.jpg	80.000000	f
4086	Estée Lauder Perfectionist Pro Rapid Firm + Lift Treatment	Trattamento lifting e rassodante rapido che aiuta a migliorare l'elasticità della pelle, riducendo la visibilità delle rughe.	115.00	40	2009	3017	f	perfectestee.jpg	\N	f
4713	E2E Test Shampoo	Prodotto creato durante il test E2E	20.00	50	2002	3005	f	e2e-test.jpg	0.000000	f
4087	Estée Lauder Resilience Multi-Effect Tri-Peptide Face and Neck Creme	Crema viso e collo anti-invecchiamento con tripeptidi che aiuta a rassodare e a tonificare la pelle per un aspetto più giovane.	105.00	60	2009	3017	f	multiestee.jpg	\N	f
4088	Lancôme Advanced Génifique Serum	Siero attivatore di giovinezza che aiuta a ridurre i segni visibili dell'invecchiamento, migliorando la luminosità e la texture della pelle.	99.00	100	2009	3026	f	advalancome.jpg	\N	f
4089	Lancôme Renergie Lift Multi-Action Ultra Cream	Crema viso multi-azione anti-invecchiamento che rassoda, tonifica e riduce le rughe, migliorando l'elasticità della pelle.	120.00	80	2009	3026	t	liftlncome.jpg	90.900000	f
4090	Lancôme Absolue Revitalizing & Brightening Soft Cream	Crema morbida rivitalizzante che aiuta a ridurre i segni dell'invecchiamento e dona un aspetto radioso e giovane alla pelle.	150.00	60	2009	3026	f	absoluelancome.jpg	\N	f
4091	Lancôme Visionnaire Advanced Skin Corrector	Trattamento correttivo avanzato che riduce linee sottili, rughe e pori visibili, migliorando la texture della pelle.	89.00	90	2009	3026	t	nuitlancome.jpg	79.000000	f
4092	YSL La Nuit de L'Homme	Profumo maschile con note speziate e orientali, perfetto per la sera. Esalta la sensualità e la profondità con il cardamomo, il cedro e la lavanda.	85.00	150	2005	3027	f	nuitysl.jpg	\N	f
4093	YSL L'Homme Eau de Toilette	Fragranza maschile elegante con note fresche e legnose, un equilibrio perfetto di carisma e freschezza. Adatto per uso quotidiano.	78.00	120	2005	3027	t	homeysl.jpg	60.000000	f
4094	YSL Y Eau de Parfum	Fragranza maschile intensa e audace con note di bergamotto, salvia e vetiver. Perfetta per uomini sicuri e moderni.	95.00	110	2005	3027	f	ysl.jpg	\N	f
4095	YSL Black Opium	Profumo femminile con note dolci e sensuali di caffè, vaniglia e fiori bianchi. Perfetto per la donna audace e moderna.	98.00	130	2005	3027	t	opiumysl.jpg	80.850000	f
4096	YSL Libre Eau de Parfum	Fragranza femminile con note di lavanda, fiori d'arancio e vaniglia. Un profumo iconico per una donna forte e libera.	105.00	90	2005	3027	f	oroysl.jpg	\N	f
4097	YSL Mon Paris	Profumo femminile fruttato e floreale, con note di fragola, lampone e peonia. Un omaggio alla passione parigina.	110.00	80	2005	3027	t	roseysl.jpg	90.000000	f
4098	Gucci Guilty Pour Homme	Fragranza maschile iconica con note di limone italiano, lavanda e patchouli. Perfetto per l'uomo audace e carismatico.	85.00	100	2005	3028	t	nerogucci.jpg	70.990000	f
4099	Gucci By Gucci Pour Homme	Profumo maschile elegante e sofisticato con note legnose e cipriate. Ideale per un uomo moderno e raffinato.	92.00	80	2005	3028	f	homegucci.jpg	\N	f
4100	Gucci Made to Measure	Fragranza maschile lussuosa e su misura con note di bergamotto, fiori d'arancio e ambra. Perfetta per un uomo di stile.	105.00	120	2005	3028	t	blugucci.jpg	90.000000	f
4101	Gucci Bloom	Profumo femminile floreale con note di gelsomino, tuberosa e caprifoglio. Un omaggio alla bellezza naturale e alla femminilità.	98.00	150	2005	3028	f	bloomgucci.jpg	\N	f
4102	Gucci Guilty Pour Femme	Fragranza femminile seducente con note di pepe rosa, litchi e patchouli. Perfetto per una donna libera e audace.	102.00	110	2005	3028	t	bronzegucci.jpg	95.600000	f
4103	Gucci Flora Gorgeous Gardenia	Profumo femminile delicato e floreale con note di gardenia, fiore di pero e patchouli. Ideale per la donna romantica e solare.	95.00	90	2005	3028	f	floragucci.jpg	\N	f
4118	crema	crema viso	20.00	100	2011	3007	t	aloenivea.jpg	10.000000	t
4119	Sapone	sapone per mani	5.00	100	2003	3001	f	bagnodove.jpg	0.000000	t
4120	crema mani	crema profumata	5.00	100	2003	3007	t	aloenivea.jpg	3.000000	f
4946	Integration Home Promo Product	Prodotto di test HomeManagement in promozione	50.00	10	2002	3001	t	integration-home-promo.jpg	40.000000	f
4947	Integration Home Normal Product	Prodotto di test HomeManagement normale	30.00	10	2002	3001	f	integration-home-normal.jpg	30.000000	f
4951	Integration Product Management Test	Prodotto creato per integration test ProductManagement	50.00	10	2007	3001	f		50.000000	f
4963	Integration Test Product	Integration test product	10.00	10	2007	3001	f		10.000000	t
4122	Prodotto Integration 1788612913199	Prodotto creato durante integration test	30.00	50	2007	3001	t	integration-test.jpg	25.000000	f
4123	Prodotto Integration Test	Prodotto creato durante integration test	12.50	25	2007	3001	f	integration-test.jpg	12.500000	t
4124	Prodotto Integration 1788777916004	Prodotto creato durante integration test	30.00	50	2007	3001	t	integration-test.jpg	25.000000	f
4125	Prodotto Integration Test	Prodotto creato durante integration test	12.50	25	2007	3001	f	integration-test.jpg	12.500000	t
4126	Prodotto Integration 1788778711168	Prodotto creato durante integration test	30.00	50	2007	3001	t	integration-test.jpg	25.000000	f
4127	Prodotto Integration Test	Prodotto creato durante integration test	12.50	25	2007	3001	f	integration-test.jpg	12.500000	t
4128	Prodotto Integration 1788778995678	Prodotto creato durante integration test	30.00	50	2007	3001	t	integration-test.jpg	25.000000	f
4129	Prodotto Integration Test	Prodotto creato durante integration test	12.50	25	2007	3001	f	integration-test.jpg	12.500000	t
4130	Prodotto Integration 1788794905536	Prodotto creato durante integration test	30.00	50	2007	3001	t	integration-test.jpg	25.000000	f
4131	Prodotto Integration Test	Prodotto creato durante integration test	12.50	25	2007	3001	f	integration-test.jpg	12.500000	t
4132	Prodotto Integration 1788795000719	Prodotto creato durante integration test	30.00	50	2007	3001	t	integration-test.jpg	25.000000	f
4001	Studio Integration Test	Fondotinta liquido a copertura totale, resistente e opaco.	40.00	80	2007	3001	f	fondotintamac.jpg	40.000000	t
4133	Prodotto Integration Test	Prodotto creato durante integration test	12.50	25	2007	3001	f	integration-test.jpg	12.500000	t
4921	Integration User Management Product	Prodotto creato per integration test	50.00	10	2007	3001	f	integration-test.jpg	50.000000	f
\.


--
-- Data for Name: utente; Type: TABLE DATA; Schema: public; Owner: giuliapanarello
--

COPY public.utente (id_nome, nome, cognome, email, password, ruolo, telefono, stato_account, wallet) FROM stdin;
2	Marco	Bianchi	marco.bianchi@email.com	hashed_password2	0	2345678901	bloccato	0.00
5	Anna	Gallo	anna.gallo@email.com	hashed_password5	0	5678901234	bloccato	0.00
6	Laura	Fabbri	laura.fabbri@email.com	hashed_password6	0	6789012345	attivo	0.00
7	Stefano	Rinaldi	stefano.rinaldi@email.com	hashed_password7	0	7890123456	attivo	0.00
8	Francesca	De Luca	francesca.deluca@email.com	hashed_password8	1	8901234567	bloccato	57.30
9	Matteo	Conti	matteo.conti@email.com	hashed_password9	0	9012345678	attivo	0.00
10	Elena	Ferrari	elena.ferrari@email.com	hashed_password10	0	0123456789	attivo	0.00
12	Martina	Esposito	martina.esposito@email.com	hashed_password12	0	2345678901	attivo	0.00
13	Alessandro	Moretti	alessandro.moretti@email.com	hashed_password13	0	3456789012	attivo	0.00
14	Giorgia	Marino	giorgia.marino@email.com	hashed_password14	0	4567890123	bloccato	0.00
15	Nicola	Rocca	nicola.rocca@email.com	hashed_password15	1	5678901234	attivo	0.00
16	Federica	Barbieri	federica.barbieri@email.com	hashed_password16	0	6789012345	attivo	0.00
17	Roberto	Lombardi	roberto.lombardi@email.com	hashed_password17	0	7890123456	attivo	0.00
18	Cecilia	Sanna	cecilia.sanna@email.com	hashed_password18	1	8901234567	attivo	0.00
19	Lorenzo	Palmieri	lorenzo.palmieri@email.com	hashed_password19	0	9012345678	attivo	0.00
20	Sofia	Colombo	sofia.colombo@email.com	hashed_password20	0	0123456789	attivo	0.00
28	Matteo	Panarello	matteo.panarello@yahoo.it	hashed_password_89	0	3385882367	attivo	237.00
3	Sara	Verdi	sara.verdi@email.com	hashed_password3	1	3456789012	attivo	44.67
1	Giulia	Rossi	giulia.rossi@email.com	hashed_password1	0	1234567890	attivo	56.00
27	Giulia	Panarello	giulia.panarello@yahoo.it	hashed_password90	0	3322985501	attivo	34.00
4	Luca	Neri	integration.user.1788795000606@email.com	hashed_password4	0	4567890123	attivo	21.00
11	Davide	Giorgio	davide.integration.1788795000784@email.com	hashed_password11	1	1234567890	attivo	0.00
476	Integration	integration.product.user@glamourglow.test	test	Product	0	3333333333	attivo	100.00
477	Integration	integration.product.user@glamourglow.test	test	Product	0	3333333333	attivo	100.00
478	Integration	integration.product.user@glamourglow.test	test	Product	0	3333333333	attivo	100.00
479	Integration	integration.product.user@glamourglow.test	test	Product	0	3333333333	attivo	100.00
480	Integration	integration.product.user@glamourglow.test	test	Product	0	3333333333	attivo	100.00
481	Integration	integration.product.user@glamourglow.test	test	Product	0	3333333333	attivo	100.00
482	Integration	integration.product.user@glamourglow.test	test	Product	0	3333333333	attivo	100.00
483	Integration	integration.product.user@glamourglow.test	test	Product	0	3333333333	attivo	100.00
484	Integration	integration.product.user@glamourglow.test	test	Product	0	3333333333	attivo	100.00
485	Integration	integration.product.user@glamourglow.test	test	Product	0	3333333333	attivo	100.00
486	Integration	integration.product.user@glamourglow.test	test	Product	0	3333333333	attivo	100.00
487	Integration	integration.product.user@glamourglow.test	test	Product	0	3333333333	attivo	100.00
1170	Integration	User	integration.user.management@glamourglow.test	test	0	3333333333	attivo	100.00
1177	Test	Integration	test.integration.1790693706860@email.com	test_password	0	3331234567	attivo	0.00
1101	Test	Integration	test.integration.1790603147123@email.com	test_password	0	3331234567	attivo	0.00
1196	Home	Integration	integration.home@glamourglow.test	test_password	0	3331111111	attivo	100.00
1197	Home	Blocked	integration.home.blocked@glamourglow.test	test_password	0	3332222222	bloccato	100.00
1201	Product	Integration	integration.product.user@glamourglow.test	test	0	3333333333	attivo	100.00
1224	Admin	Test	integration.admin@glamourglow.test	admin	1	0000000000	attivo	100.00
1225	Integration	User	integration.user@glamourglow.test	user	0	0000000000	attivo	100.00
\.


--
-- Name: categoria_id_categoria_seq; Type: SEQUENCE SET; Schema: public; Owner: giuliapanarello
--

SELECT pg_catalog.setval('public.categoria_id_categoria_seq', 2021, true);


--
-- Name: dettagli_ordine_id_dettaglio_seq; Type: SEQUENCE SET; Schema: public; Owner: giuliapanarello
--

SELECT pg_catalog.setval('public.dettagli_ordine_id_dettaglio_seq', 442, true);


--
-- Name: marchio_id_marchio_seq; Type: SEQUENCE SET; Schema: public; Owner: giuliapanarello
--

SELECT pg_catalog.setval('public.marchio_id_marchio_seq', 3029, true);


--
-- Name: ordine_id_ordine_seq; Type: SEQUENCE SET; Schema: public; Owner: giuliapanarello
--

SELECT pg_catalog.setval('public.ordine_id_ordine_seq', 436, true);


--
-- Name: prodotto_id_prodotto_seq; Type: SEQUENCE SET; Schema: public; Owner: giuliapanarello
--

SELECT pg_catalog.setval('public.prodotto_id_prodotto_seq', 4963, true);


--
-- Name: utente_id_nome_seq; Type: SEQUENCE SET; Schema: public; Owner: giuliapanarello
--

SELECT pg_catalog.setval('public.utente_id_nome_seq', 1225, true);


--
-- Name: categoria categoria_pkey; Type: CONSTRAINT; Schema: public; Owner: giuliapanarello
--

ALTER TABLE ONLY public.categoria
    ADD CONSTRAINT categoria_pkey PRIMARY KEY (id_categoria);


--
-- Name: coupon coupon_pkey; Type: CONSTRAINT; Schema: public; Owner: giuliapanarello
--

ALTER TABLE ONLY public.coupon
    ADD CONSTRAINT coupon_pkey PRIMARY KEY (codice);


--
-- Name: dettagli_ordine dettagli_ordine_pkey; Type: CONSTRAINT; Schema: public; Owner: giuliapanarello
--

ALTER TABLE ONLY public.dettagli_ordine
    ADD CONSTRAINT dettagli_ordine_pkey PRIMARY KEY (id_dettaglio);


--
-- Name: marchio marchio_pkey; Type: CONSTRAINT; Schema: public; Owner: giuliapanarello
--

ALTER TABLE ONLY public.marchio
    ADD CONSTRAINT marchio_pkey PRIMARY KEY (id_marchio);


--
-- Name: ordine ordine_pkey; Type: CONSTRAINT; Schema: public; Owner: giuliapanarello
--

ALTER TABLE ONLY public.ordine
    ADD CONSTRAINT ordine_pkey PRIMARY KEY (id_ordine);


--
-- Name: prodotto prodotto_pkey; Type: CONSTRAINT; Schema: public; Owner: giuliapanarello
--

ALTER TABLE ONLY public.prodotto
    ADD CONSTRAINT prodotto_pkey PRIMARY KEY (id_prodotto);


--
-- Name: utente utente_pkey; Type: CONSTRAINT; Schema: public; Owner: giuliapanarello
--

ALTER TABLE ONLY public.utente
    ADD CONSTRAINT utente_pkey PRIMARY KEY (id_nome);


--
-- Name: dettagli_ordine dettagli_ordine_coupon_fkey; Type: FK CONSTRAINT; Schema: public; Owner: giuliapanarello
--

ALTER TABLE ONLY public.dettagli_ordine
    ADD CONSTRAINT dettagli_ordine_coupon_fkey FOREIGN KEY (coupon) REFERENCES public.coupon(codice) ON UPDATE CASCADE;


--
-- Name: dettagli_ordine dettagli_ordine_id_ordine_fkey; Type: FK CONSTRAINT; Schema: public; Owner: giuliapanarello
--

ALTER TABLE ONLY public.dettagli_ordine
    ADD CONSTRAINT dettagli_ordine_id_ordine_fkey FOREIGN KEY (id_ordine) REFERENCES public.ordine(id_ordine) ON UPDATE CASCADE;


--
-- Name: dettagli_ordine dettagli_ordine_id_prodotto_fkey; Type: FK CONSTRAINT; Schema: public; Owner: giuliapanarello
--

ALTER TABLE ONLY public.dettagli_ordine
    ADD CONSTRAINT dettagli_ordine_id_prodotto_fkey FOREIGN KEY (id_prodotto) REFERENCES public.prodotto(id_prodotto) ON UPDATE CASCADE;


--
-- Name: ordine ordine_id_utente_fkey; Type: FK CONSTRAINT; Schema: public; Owner: giuliapanarello
--

ALTER TABLE ONLY public.ordine
    ADD CONSTRAINT ordine_id_utente_fkey FOREIGN KEY (id_utente) REFERENCES public.utente(id_nome) ON UPDATE CASCADE ON DELETE RESTRICT;


--
-- Name: prodotto prodotto_id_categoria_fkey; Type: FK CONSTRAINT; Schema: public; Owner: giuliapanarello
--

ALTER TABLE ONLY public.prodotto
    ADD CONSTRAINT prodotto_id_categoria_fkey FOREIGN KEY (id_categoria) REFERENCES public.categoria(id_categoria) ON UPDATE CASCADE;


--
-- Name: prodotto prodotto_id_marchio_fkey; Type: FK CONSTRAINT; Schema: public; Owner: giuliapanarello
--

ALTER TABLE ONLY public.prodotto
    ADD CONSTRAINT prodotto_id_marchio_fkey FOREIGN KEY (id_marchio) REFERENCES public.marchio(id_marchio) ON UPDATE CASCADE;


--
-- PostgreSQL database dump complete
--

\unrestrict hW2RYcw2bAwcb7VcUaN8swz46yUcSs3swMIJ5PhCPs6bbRzP09EdOP08qw058z2

