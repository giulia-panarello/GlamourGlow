--
-- PostgreSQL database dump
--

\restrict qlX2VBDrMSGy7kdh8bGlwsdhveHhB9U92ksfpzEGsqcYolAc7ztARc4ZTfoYqxe

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

\unrestrict qlX2VBDrMSGy7kdh8bGlwsdhveHhB9U92ksfpzEGsqcYolAc7ztARc4ZTfoYqxe

