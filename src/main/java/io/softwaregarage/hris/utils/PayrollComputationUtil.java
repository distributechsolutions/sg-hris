package io.softwaregarage.hris.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class PayrollComputationUtil {
    private static final Logger logger = LoggerFactory.getLogger(PayrollComputationUtil.class);
    private static PayrollComputationUtil INSTANCE;

    private PayrollComputationUtil() {
    }

    public synchronized static PayrollComputationUtil getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PayrollComputationUtil();
        }

        return INSTANCE;
    }

    public static String getPayrollFrequency(LocalDate startDate, LocalDate endDate) {
        String frequency = "";
        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);

        if (daysBetween >= 5 || daysBetween <= 7) {
            return "Weekly";
        }

        if (daysBetween >= 10 || daysBetween <= 14) {
            frequency = "Semi-Monthly";
        }

        if (daysBetween >= 28 || daysBetween <= 31) {
            frequency = "Monthly";
        }

        return frequency;
    }
}
