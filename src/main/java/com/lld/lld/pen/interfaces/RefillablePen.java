package com.lld.lld.pen.interfaces;

import com.lld.lld.pen.models.Refill;

public interface RefillablePen {
    public void changeRefill(Refill refill);
    public Refill getRefill();
    public Boolean isRefillable(); 
}
