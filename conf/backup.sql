--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: box_label; Type: TABLE; Schema: public; Owner: pulse; Tablespace: 
--

CREATE TABLE box_label (
    id character varying(255) NOT NULL,
    lot_no character varying(255) NOT NULL,
    quantity integer NOT NULL,
    production_date date NOT NULL,
    product_uid uuid NOT NULL
);


ALTER TABLE box_label OWNER TO pulse;

--
-- Name: label; Type: TABLE; Schema: public; Owner: pulse; Tablespace: 
--

CREATE TABLE label (
    pallet_label_id character varying(255) NOT NULL,
    box_label_id character varying(255) NOT NULL
);


ALTER TABLE label OWNER TO pulse;

--
-- Name: network; Type: TABLE; Schema: public; Owner: pulse; Tablespace: 
--

CREATE TABLE network (
    id character varying(255) NOT NULL,
    name character varying(255) NOT NULL,
    address character varying(255) NOT NULL
);


ALTER TABLE network OWNER TO pulse;

--
-- Name: order; Type: TABLE; Schema: public; Owner: pulse; Tablespace: 
--

CREATE TABLE "order" (
    id character varying(255) NOT NULL,
    order_date date,
    due_date date,
    quantity integer,
    network_id character varying(255),
    partner_id character varying(255),
    product_uid uuid
);


ALTER TABLE "order" OWNER TO pulse;

--
-- Name: pallet_label; Type: TABLE; Schema: public; Owner: pulse; Tablespace: 
--

CREATE TABLE pallet_label (
    id character varying(255) NOT NULL,
    lot_no character varying(255) NOT NULL,
    quantity integer NOT NULL,
    box_quantity integer NOT NULL,
    dispatch_date date NOT NULL,
    order_id character varying(255) NOT NULL,
    number numeric
);


ALTER TABLE pallet_label OWNER TO pulse;

--
-- Name: partner; Type: TABLE; Schema: public; Owner: pulse; Tablespace: 
--

CREATE TABLE partner (
    uid uuid NOT NULL,
    id character varying(255) NOT NULL,
    name character varying(255) NOT NULL,
    business_id character varying(255) NOT NULL,
    address character varying(255),
    network_id character varying(255) NOT NULL
);


ALTER TABLE partner OWNER TO pulse;

--
-- Name: product; Type: TABLE; Schema: public; Owner: pulse; Tablespace: 
--

CREATE TABLE product (
    uid uuid NOT NULL,
    id character varying(255) NOT NULL,
    name character varying(255) NOT NULL,
    revision character varying(255) NOT NULL,
    weight double precision NOT NULL,
    weight_unit_id character varying(255)
);


ALTER TABLE product OWNER TO pulse;

--
-- Name: unit; Type: TABLE; Schema: public; Owner: pulse; Tablespace: 
--

CREATE TABLE unit (
    id character varying(255) NOT NULL,
    name character varying(255) NOT NULL,
    symbol character varying(255)
);


ALTER TABLE unit OWNER TO pulse;

--
-- Data for Name: box_label; Type: TABLE DATA; Schema: public; Owner: pulse
--

COPY box_label (id, lot_no, quantity, production_date, product_uid) FROM stdin;
20190408001	A0002	50	2019-04-09	6f652a99-76c6-46b4-86fc-9bbc16da8174
20190408002	A0002	50	2019-04-09	6f652a99-76c6-46b4-86fc-9bbc16da8174
20190408003	A0002	50	2019-04-10	6f652a99-76c6-46b4-86fc-9bbc16da8174
20190408004	A0002	50	2019-04-10	6f652a99-76c6-46b4-86fc-9bbc16da8174
20190408005	A0002	50	2019-04-11	6f652a99-76c6-46b4-86fc-9bbc16da8174
20190408006	A0002	50	2019-04-11	6f652a99-76c6-46b4-86fc-9bbc16da8174
20190408007	A0002	50	2019-04-12	6f652a99-76c6-46b4-86fc-9bbc16da8174
20190408008	A0002	50	2019-04-12	6f652a99-76c6-46b4-86fc-9bbc16da8174
20190408009	A0002	50	2019-04-13	6f652a99-76c6-46b4-86fc-9bbc16da8174
20190408010	A0002	50	2019-04-13	6f652a99-76c6-46b4-86fc-9bbc16da8174
20190408011	A0003	50	2019-04-14	6f652a99-76c6-46b4-86fc-9bbc16da8174
20190408012	A0003	50	2019-04-14	6f652a99-76c6-46b4-86fc-9bbc16da8174
20190408013	A0003	50	2019-04-15	6f652a99-76c6-46b4-86fc-9bbc16da8174
20190408014	A0003	50	2019-04-15	6f652a99-76c6-46b4-86fc-9bbc16da8174
20190408015	A0003	50	2019-04-16	6f652a99-76c6-46b4-86fc-9bbc16da8174
20190408016	A0003	50	2019-04-16	6f652a99-76c6-46b4-86fc-9bbc16da8174
20190408017	A0003	50	2019-04-17	6f652a99-76c6-46b4-86fc-9bbc16da8174
20190408018	A0003	50	2019-04-17	6f652a99-76c6-46b4-86fc-9bbc16da8174
20190401009	A0001	50	2019-04-06	6f652a99-76c6-46b4-86fc-9bbc16da8174
20190401010	A0001	50	2019-04-06	6f652a99-76c6-46b4-86fc-9bbc16da8174
20190408019	A0003	50	2019-04-18	6f652a99-76c6-46b4-86fc-9bbc16da8174
20190408020	A0003	50	2019-04-19	6f652a99-76c6-46b4-86fc-9bbc16da8174
20190401001	A0001	50	2019-04-02	6f652a99-76c6-46b4-86fc-9bbc16da8174
20190401002	A0001	50	2019-04-02	6f652a99-76c6-46b4-86fc-9bbc16da8174
20190401003	A0001	50	2019-04-03	6f652a99-76c6-46b4-86fc-9bbc16da8174
20190401004	A0001	50	2019-04-03	6f652a99-76c6-46b4-86fc-9bbc16da8174
20190401005	A0001	50	2019-04-04	6f652a99-76c6-46b4-86fc-9bbc16da8174
20190401006	A0001	50	2019-04-04	6f652a99-76c6-46b4-86fc-9bbc16da8174
20190401007	A0001	50	2019-04-05	6f652a99-76c6-46b4-86fc-9bbc16da8174
20190401008	A0001	50	2019-04-05	6f652a99-76c6-46b4-86fc-9bbc16da8174
\.


--
-- Data for Name: label; Type: TABLE DATA; Schema: public; Owner: pulse
--

COPY label (pallet_label_id, box_label_id) FROM stdin;
20190401001000	20190408001
20190401001000	20190408002
20190401001000	20190408003
20190401001000	20190408004
20190401001000	20190408005
\.


--
-- Data for Name: network; Type: TABLE DATA; Schema: public; Owner: pulse
--

COPY network (id, name, address) FROM stdin;
WRC	우리 자동차	경기도 평택시 우리동 1-1
\.


--
-- Data for Name: order; Type: TABLE DATA; Schema: public; Owner: pulse
--

COPY "order" (id, order_date, due_date, quantity, network_id, partner_id, product_uid) FROM stdin;
20190401001	2019-04-01	2019-04-10	500	WRC	AJA	6f652a99-76c6-46b4-86fc-9bbc16da8174
20190401002	2019-04-08	2019-04-25	1000	WRC	AJA	6f652a99-76c6-46b4-86fc-9bbc16da8174
\.


--
-- Data for Name: pallet_label; Type: TABLE DATA; Schema: public; Owner: pulse
--

COPY pallet_label (id, lot_no, quantity, box_quantity, dispatch_date, order_id, number) FROM stdin;
20190401001000	A0002	250	5	2019-04-25	20190401001	1
\.


--
-- Data for Name: partner; Type: TABLE DATA; Schema: public; Owner: pulse
--

COPY partner (uid, id, name, business_id, address, network_id) FROM stdin;
df112a4e-42c3-4a7f-94cd-91e13afd7169	AJA	안전 에어백	111-11-11111	경기도 안성시 안전동 1-1	WRC
\.


--
-- Data for Name: product; Type: TABLE DATA; Schema: public; Owner: pulse
--

COPY product (uid, id, name, revision, weight, weight_unit_id) FROM stdin;
6f652a99-76c6-46b4-86fc-9bbc16da8174	AJA001	안전 에어백1	A01	1.19999999999999996	kg
\.


--
-- Data for Name: unit; Type: TABLE DATA; Schema: public; Owner: pulse
--

COPY unit (id, name, symbol) FROM stdin;
kg	킬로그램	kg
\.


--
-- Name: box_label_pkey; Type: CONSTRAINT; Schema: public; Owner: pulse; Tablespace: 
--

ALTER TABLE ONLY box_label
    ADD CONSTRAINT box_label_pkey PRIMARY KEY (id);


--
-- Name: label_pkey; Type: CONSTRAINT; Schema: public; Owner: pulse; Tablespace: 
--

ALTER TABLE ONLY label
    ADD CONSTRAINT label_pkey PRIMARY KEY (pallet_label_id, box_label_id);


--
-- Name: network_pkey; Type: CONSTRAINT; Schema: public; Owner: pulse; Tablespace: 
--

ALTER TABLE ONLY network
    ADD CONSTRAINT network_pkey PRIMARY KEY (id);


--
-- Name: order_pkey; Type: CONSTRAINT; Schema: public; Owner: pulse; Tablespace: 
--

ALTER TABLE ONLY "order"
    ADD CONSTRAINT order_pkey PRIMARY KEY (id);


--
-- Name: pallet_label_pkey; Type: CONSTRAINT; Schema: public; Owner: pulse; Tablespace: 
--

ALTER TABLE ONLY pallet_label
    ADD CONSTRAINT pallet_label_pkey PRIMARY KEY (id);


--
-- Name: partner_pkey; Type: CONSTRAINT; Schema: public; Owner: pulse; Tablespace: 
--

ALTER TABLE ONLY partner
    ADD CONSTRAINT partner_pkey PRIMARY KEY (id);


--
-- Name: partner_uid_key; Type: CONSTRAINT; Schema: public; Owner: pulse; Tablespace: 
--

ALTER TABLE ONLY partner
    ADD CONSTRAINT partner_uid_key UNIQUE (uid);


--
-- Name: product_pkey; Type: CONSTRAINT; Schema: public; Owner: pulse; Tablespace: 
--

ALTER TABLE ONLY product
    ADD CONSTRAINT product_pkey PRIMARY KEY (uid);


--
-- Name: unit_pkey; Type: CONSTRAINT; Schema: public; Owner: pulse; Tablespace: 
--

ALTER TABLE ONLY unit
    ADD CONSTRAINT unit_pkey PRIMARY KEY (id);


--
-- Name: box_label_product_uid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: pulse
--

ALTER TABLE ONLY box_label
    ADD CONSTRAINT box_label_product_uid_fkey FOREIGN KEY (product_uid) REFERENCES product(uid);


--
-- Name: label_box_label_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: pulse
--

ALTER TABLE ONLY label
    ADD CONSTRAINT label_box_label_id_fkey FOREIGN KEY (box_label_id) REFERENCES box_label(id);


--
-- Name: label_pallet_label_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: pulse
--

ALTER TABLE ONLY label
    ADD CONSTRAINT label_pallet_label_id_fkey FOREIGN KEY (pallet_label_id) REFERENCES pallet_label(id);


--
-- Name: order_network_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: pulse
--

ALTER TABLE ONLY "order"
    ADD CONSTRAINT order_network_id_fkey FOREIGN KEY (network_id) REFERENCES network(id);


--
-- Name: order_partner_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: pulse
--

ALTER TABLE ONLY "order"
    ADD CONSTRAINT order_partner_id_fkey FOREIGN KEY (partner_id) REFERENCES partner(id);


--
-- Name: order_product_uid_fkey; Type: FK CONSTRAINT; Schema: public; Owner: pulse
--

ALTER TABLE ONLY "order"
    ADD CONSTRAINT order_product_uid_fkey FOREIGN KEY (product_uid) REFERENCES product(uid);


--
-- Name: pallet_label_order_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: pulse
--

ALTER TABLE ONLY pallet_label
    ADD CONSTRAINT pallet_label_order_id_fkey FOREIGN KEY (order_id) REFERENCES "order"(id);


--
-- Name: partner_network_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: pulse
--

ALTER TABLE ONLY partner
    ADD CONSTRAINT partner_network_id_fkey FOREIGN KEY (network_id) REFERENCES network(id);


--
-- Name: product_weight_unit_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: pulse
--

ALTER TABLE ONLY product
    ADD CONSTRAINT product_weight_unit_id_fkey FOREIGN KEY (weight_unit_id) REFERENCES unit(id);


--
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- PostgreSQL database dump complete
--

