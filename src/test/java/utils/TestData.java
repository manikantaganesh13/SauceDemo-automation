package utils;


import java.math.BigDecimal;
import java.util.Map;

public class TestData {
//    public static final String PASSWORD = "secret_sauce";
      public static BigDecimal TAX_RATE = new BigDecimal("0.08");


    public static final Map<String, BigDecimal> PRICES = Map.of(
            "backpack", new BigDecimal("29.99"),
            "bike-light", new BigDecimal("9.99"),
            "bolt-t-shirt", new BigDecimal("15.99"),
            "fleece-jacket", new BigDecimal("49.99"),
            "onesie", new BigDecimal("7.99")
    );
}