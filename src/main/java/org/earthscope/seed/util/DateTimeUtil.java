package org.earthscope.seed.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAccessor;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class DateTimeUtil {
    private static List<String> dateTimePatterns = Arrays.asList("M[M]['/']['-']['.']d[d]['/']['-']['.']yyyy[' ']['T'][' ']h[h]:mm:ss[' ']a[' ']['('][VV][X][x][Z][z][O][')']", "M[M]['/']['-']['.']d[d]['/']['-']['.']yyyy[' ']['T'][' ']H[H]:mm:ss[' ']['('][VV][X][x][Z][z][O][')']", "d[d]['/']['-']['.']M[M]['/']['-']['.']yyyy[' ']['T'][' ']h[h]:mm:ss[' ']a[' ']['('][VV][X][x][Z][z][O][')']", "d[d]['/']['-']['.']M[M]['/']['-']['.']yyyy[' ']['T'][' ']H[H]:mm:ss[' ']['('][VV][X][x][Z][z][O][')']", "yyyy['/']['-']['.']M[M]['/']['-']['.']d[d][' ']['T'][' ']H[H]:mm:ss[.SSS][' ']['('][VV][X][x][Z][z][O][')']", "yyyy['/']['-']['.']M[M]['/']['-']['.']d[d][' ']['T'][' ']H[H]:mm:ss[.SSSSSS][' ']['('][VV][X][x][Z][z][O][')']", "yyyy['/']['-']['.']M[M]['/']['-']['.']d[d][' ']['T'][' ']H[H]:mm:ss[.SS][' ']['('][VV][X][x][Z][z][O][')']", "yyyy['/']['-']['.']M[M]['/']['-']['.']d[d][' ']['T'][' ']H[H]:mm:ss[.S][' ']['('][VV][X][x][Z][z][O][')']", "yyyy['/']['-']['.']M[M]['/']['-']['.']d[d][' ']['T'][' ']h[h]:mm:ss[' ']a[' ']['('][VV][X][x][Z][z][O][')']", "M[M]['/']['-']['.']yyyy['/']['-']['.']d[d][' ']['T'][' ']H[H]:mm:ss[' ']['('][VV][X][x][Z][z][O][')']", "M[M]['/']['-']['.']yyyy['/']['-']['.']d[d][' ']['T'][' ']h[h]:mm:ss[' ']a[' ']['('][VV][X][x][Z][z][O][')']", "d[d]['/']['-']['.']MMM['/']['-']['.']yyyy[' ']['T'][' ']H[H]:mm:ss[' ']['('][VV][X][x][Z][z][O][')']", "d[d]['/']['-']['.']MMM['/']['-']['.']yy[' ']['T'][' ']H[H]:mm:ss[' ']['('][VV][X][x][Z][z][O][')']", "d[d]['/']['-']['.']MMM['/']['-']['.']yyyy[' ']['T'][' ']h[h]:mm:ss[' ']a[' ']['('][VV][X][x][Z][z][O][')']", "d[d]['/']['-']['.']MMM['/']['-']['.']yy[' ']['T'][' ']h[h]:mm:ss[' ']a[' ']['('][VV][X][x][Z][z][O][')']", "MMM['/']['-']['.']d[d]['/']['-']['.']yyyy[' ']['T'][' ']H[H]:mm:ss[' ']['('][VV][X][x][Z][z][O][')']", "MMM['/']['-']['.']d[d]['/']['-']['.']yy[' ']['T'][' ']H[H]:mm:ss[' ']['('][VV][X][x][Z][z][O][')']", "MMM['/']['-']['.']d[d]['/']['-']['.']yyyy[' ']['T'][' ']h[h]:mm:ss[' ']a[' ']['('][VV][X][x][Z][z][O][')']", "MMM['/']['-']['.']d[d]['/']['-']['.']yy[' ']['T'][' ']h[h]:mm:ss[' ']a[' ']['('][VV][X][x][Z][z][O][')']", "M[M]['/']['-']['.']d[d]['/']['-']['.']yyyy", "M[M]['/']['-']['.']d[d]['/']['-']['.']yyyy", "d[d]['/']['-']['.']M[M]['/']['-']['.']yyyy", "d[d]['/']['-']['.']M[M]['/']['-']['.']yyyy", "yyyy['/']['-']['.']M[M]['/']['-']['.']d[d]", "yyyy['/']['-']['.']M[M]['/']['-']['.']d[d]", "M[M]['/']['-']['.']yyyy['/']['-']['.']d[d]", "M[M]['/']['-']['.']yyyy['/']['-']['.']d[d]", "d[d]['/']['-']['.']MMM['/']['-']['.']yyyy", "d[d]['/']['-']['.']MMM['/']['-']['.']yyyy", "d[d]['/']['-']['.']MMM['/']['-']['.']yy", "d[d]['/']['-']['.']MMM['/']['-']['.']yyyy", "d[d]['/']['-']['.']MMM['/']['-']['.']yy", "MMM['/']['-']['.']d[d]['/']['-']['.']yyyy", "MMM['/']['-']['.']d[d]['/']['-']['.']yy", "MMM['/']['-']['.']d[d]['/']['-']['.']yyyy", "MMM['/']['-']['.']d[d]['/']['-']['.']yy", "yyyy,DDD,HH:mm:ss");

    public DateTimeUtil() {
    }

    public static ZonedDateTime parseAny(String time) {
        if (time != null && !time.trim().isEmpty()) {
            ZoneId timeZone = ZoneId.of("UTC");
            Iterator var2 = dateTimePatterns.iterator();

            while(var2.hasNext()) {
                String pattern = (String)var2.next();

                try {
                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern).withZone(ZoneId.of("UTC"));
                    TemporalAccessor dt = dtf.parseBest(time, ZonedDateTime::from, LocalDateTime::from, OffsetDateTime::from, Instant::from, LocalDate::from);
                    if (dt instanceof ZonedDateTime) {
                        return (ZonedDateTime)dt;
                    }

                    if (dt instanceof LocalDateTime) {
                        return ((LocalDateTime)dt).atZone(timeZone);
                    }

                    if (dt instanceof OffsetDateTime) {
                        return ((OffsetDateTime)dt).atZoneSameInstant(timeZone);
                    }

                    if (dt instanceof Instant) {
                        return ((Instant)dt).atZone(timeZone);
                    }

                    if (dt instanceof LocalDate) {
                        return ((LocalDate)dt).atStartOfDay(timeZone);
                    }
                } catch (DateTimeParseException var6) {
                }
            }

            throw new DateTimeParseException("Cannot parse date-time", time, 0);
        } else {
            return null;
        }
    }


    public static String format(Instant instant) {
        return format((Instant)instant, (String)null);
    }

    public static String format(Instant instant, String pattern) {
        return instant == null ? null : format(ZonedDateTime.ofInstant(instant, ZoneOffset.UTC), pattern);
    }

    public static String format(ZonedDateTime time) {
        return format(time, "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    }

    public static String format(ZonedDateTime time, String pattern) {
        if (time == null) {
            return null;
        } else {
            return pattern == null ? format(time) : DateTimeFormatter.ofPattern(pattern).format(time);
        }
    }
}
