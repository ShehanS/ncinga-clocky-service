package com.ncinga.timer.utilities;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public abstract class Validator {
    public static long calculateDifferenceInDays(long currentEpoch, long givenEpoch) {
        Instant currentInstant = Instant.ofEpochMilli(currentEpoch);
        Instant givenInstant = Instant.ofEpochMilli(givenEpoch);
        long daysDifference = ChronoUnit.DAYS.between(givenInstant, currentInstant);
        return Math.abs(daysDifference);
    }
}
