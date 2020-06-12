package com.psp.service;

import com.psp.model.RateStar;
import com.psp.repository.RateStarDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class RateStarService {
    @Autowired
    RateStarDao rateStarDao;

    public RateStar saveRate(RateStar rate){return rateStarDao.saveRate(rate);}
    public boolean deleteRateById(long rateId){return rateStarDao.deleteRateById(rateId);}
    public List<RateStar> getRateList(){return rateStarDao.getRateList();}
    public RateStar updateRate(RateStar rate){return rateStarDao.updateRate(rate);}
    public RateStar getRateById(long rateId){return rateStarDao.getRateById(rateId);}
}
