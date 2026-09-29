package com.glamourglow.glamourglow.model.mo;

import net.jqwik.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CouponTest {

    @Provide
    Arbitrary<String> codiceCouponTest() {
        return Arbitraries.strings()
                .alpha()
                .ofMinLength(1)
                .ofMaxLength(20);
    }

    @Provide
    Arbitrary<Integer> scontoCouponTest() {
        return Arbitraries.integers()
                .between(0, 100);
    }

    @Property
    void testSetGetCodice(
            @ForAll("codiceCouponTest") String codice) {

        Coupon coupon = new Coupon();

        coupon.setCodice(codice);

        assertEquals(codice, coupon.getCodice());
    }

    @Property
    void testSetGetSconto(
            @ForAll("scontoCouponTest") int sconto) {

        Coupon coupon = new Coupon();

        coupon.setSconto(sconto);

        assertEquals(sconto, coupon.getSconto());
    }
}