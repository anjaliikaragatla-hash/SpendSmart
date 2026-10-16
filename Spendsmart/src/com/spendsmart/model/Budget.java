package com.spendsmart.model;

import java.math.BigDecimal;
import java.time.YearMonth;

public class Budget {

    private YearMonth month;
    private BigDecimal limit;

    public Budget(YearMonth month, BigDecimal limit) {
        this.month = month;
        this.limit = limit;
    }

    public YearMonth getMonth() {
        return month;
    }

    public BigDecimal getLimit() {
        return limit;
    }

    public void setLimit(BigDecimal limit) {
        this.limit = limit;
    }

    @Override
    public String toString() {
        return "Budget{" +
                "month=" + month +
                ", limit=" + limit +
                '}';
    }
}