package com.example.pracprj1backend.util;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;

public class AppUtil {
    public static String getAgo(LocalDateTime a, LocalDateTime b) {
        // period : 연,월,일 기준
        // duration : 시간 기준

        if (inserted.isBefore(b.minusYears(1))) {
            Period between = Period.between(inserted.toLocalDate(), b.toLocalDate());
            return between.get(ChronoUnit.YEARS) + "년 전";

        } else if (inserted.isBefore(b.minusMonths(1))) {
            Period between = Period.between(inserted.toLocalDate(), b.toLocalDate());
            return between.get(ChronoUnit.MONTHS) + "달 전";

        } else if (inserted.isBefore(b.minusDays(1))) {
            Period between = Period.between(inserted.toLocalDate(), b.toLocalDate());
            return between.get(ChronoUnit.DAYS) + "일 전";

            // Duration은 getSeconds() 로 계산 (나노밀리세컨드)
        } else if (inserted.isBefore(b.minusHours(1))) {
            Duration between = Duration.between(inserted, b);
            return (between.getSeconds() / 60 / 60) + "시간 전";

        } else if (inserted.isBefore(b.minusMinutes(1))) {
            Duration between = Duration.between(inserted, b);
            return (between.getSeconds() / 60) + "분 전";
        } else {
            Duration between = Duration.between(inserted, b);
            return between.getSeconds() + "초 전";
        }
    }
}
