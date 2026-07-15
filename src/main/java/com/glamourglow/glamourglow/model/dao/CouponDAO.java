package com.glamourglow.glamourglow.model.dao;

import com.glamourglow.glamourglow.model.mo.Coupon;

import java.util.List;

public interface CouponDAO {
    Coupon findByCode(String s);

    List<Coupon> findAll();

    void ModificaCoupon(String vecchiocodice, String codice, int sconto);
    void creaCoupon(String codice, int sconto);
}
