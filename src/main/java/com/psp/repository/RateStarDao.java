package com.psp.repository;

import com.psp.model.RateStar;


import java.util.List;

public interface RateStarDao {
    RateStar saveRate(RateStar rate);
    boolean deleteRateById(long rateId);
    List<RateStar> getRateList();
    RateStar updateRate(RateStar rate);
    RateStar getRateById(long rateId);
}
