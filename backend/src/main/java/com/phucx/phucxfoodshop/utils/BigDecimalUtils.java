package com.phucx.phucxfoodshop.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BigDecimalUtils {
    public static BigDecimal formatter(BigDecimal value) {
        return value.setScale(2, RoundingMode.CEILING);
    }
}
