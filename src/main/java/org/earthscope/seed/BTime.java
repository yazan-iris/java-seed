package org.earthscope.seed;

import org.earthscope.seed.util.DateTimeUtil;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.time.*;
import java.time.chrono.IsoChronology;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.*;


public class BTime implements Comparable<BTime> {


	private ZonedDateTime zdt;

	private BTime(int year, int month, int dayOfMonth, int hour, int minute, int second, int nanoOfSecond,
                  ZoneId zone) {
		this(ZonedDateTime.of(year, month, dayOfMonth, hour, minute, second, nanoOfSecond, zone));
	}

	private BTime(int year, int dayOfYear, int hour, int minute, int second, int tenthMilliSecond) {
		if(tenthMilliSecond>=10000){
			throw new IllegalArgumentException("Expected a value between 0 and 100000 but received "+tenthMilliSecond);
		}

		ZonedDateTime dateTime = ZonedDateTime.of(year, 1, 1, hour,
				minute, second, tenthMilliSecond * 100000, ZoneId.of("UTC"));
		this.zdt = dateTime.withDayOfYear(dayOfYear);
	}

	private BTime(ZonedDateTime time) {
		Objects.requireNonNull(time);
		this.zdt = time.withZoneSameInstant(ZoneId.of("UTC"));
	}

	public boolean isBefore(BTime other){
		return zdt.isBefore(other.zdt);
	}

	public boolean isAfter(BTime other){
		return this.zdt.isAfter(other.zdt);
	}

	public int getYear() {
		return this.zdt.getYear();
	}

	public int getMonthOfYear() {
		return zdt.getMonthValue();
	}

	public int getDayOfYear() {
		return this.zdt.getDayOfYear();
	}

	public int getDayOfMonth() {
		return this.zdt.getDayOfMonth();
	}

	public ZoneId getZone() {
		return this.zdt.getZone();
	}

	public int getHour() {
		return this.zdt.getHour();
	}

	public int getMinute() {
		return this.zdt.getMinute();
	}

	public int getSecond() {
		return this.zdt.getSecond();
	}

	public int getTenthMilliSecond() {
		return getNano() / 100000;
	}

	public int getNano() {
		return this.zdt.getNano();
	}

	public BTime plusYears(int years) {
		return BTime.valueOf(this.zdt.plusYears(years));
	}

	public BTime plusDays(int days) {
		return BTime.valueOf(this.zdt.plusDays(days));
	}


	public BTime plusHours(int hours) {
		return BTime.valueOf(this.zdt.plusHours(hours));
	}

	public BTime plusMinutes(int minutes) {
		return BTime.valueOf(this.zdt.plusMinutes(minutes));
	}

	public BTime plusSeconds(int seconds) {
		return BTime.valueOf(this.zdt.plusSeconds(seconds));
	}

	public BTime plusMicroSecond(int microSecond) {
		return plusNanoSeconds(microSecond * 1000);
	}

	public BTime plusTenthMilliSeconds(int tenthMilliSecond) {
		return plusNanoSeconds(tenthMilliSecond * 10000);
	}

	public BTime plusNanoSeconds(long nanoSeconds) {
		return BTime.valueOf(this.zdt.plusNanos(nanoSeconds));
	}

	public LocalTime toLocalTime(){
		return this.zdt.toLocalTime();
	}
	public long toEpochSecond() {
		return zdt.toEpochSecond();
	}

	public int getMonthOfYear(int year, int dayOfYear) {
		boolean leap = IsoChronology.INSTANCE.isLeapYear(year);
		if (dayOfYear == 366 && leap == false) {
			throw new DateTimeException("Invalid date 'DayOfYear 366' as '" + year + "' is not a leap year");
		}
		Month moy = Month.of((dayOfYear - 1) / 31 + 1);
		int monthEnd = moy.firstDayOfYear(leap) + moy.length(leap) - 1;
		if (dayOfYear > monthEnd) {
			moy = moy.plus(1);
		}
		return moy.getValue();
	}

	private int getDayOfMonth(int year, int dayOfYear) {
		boolean leap = IsoChronology.INSTANCE.isLeapYear(year);
		if (dayOfYear == 366 && leap == false) {
			throw new DateTimeException("Invalid date 'DayOfYear 366' as '" + year + "' is not a leap year");
		}
		Month moy = Month.of((dayOfYear - 1) / 31 + 1);
		int monthEnd = moy.firstDayOfYear(leap) + moy.length(leap) - 1;
		if (dayOfYear > monthEnd) {
			moy = moy.plus(1);
		}
		return dayOfYear - moy.firstDayOfYear(leap) + 1;
	}

	public static BTime now() {
		return BTime.valueOf(ZonedDateTime.now(ZoneOffset.UTC));
	}

	public static BTime valueOf(int year, int dayOfYear) {
		return BTime.valueOf(year, dayOfYear, 0, 0, 0, 0);
	}

	public static BTime valueOf(int year, int dayOfYear, int hour, int minute, int second, int tenthMilliSecond) {
		return new BTime(year,dayOfYear,hour,minute,second,tenthMilliSecond);
	}

	public static BTime valueOf(ZonedDateTime date) {
		if (date == null) {
			return null;
		}
		return new BTime(date);
	}

	public static BTime valueOf(Date date) {
		if (date == null) {
			return null;
		}
		Instant instant = date.toInstant();

		int tenthMilliSecond = instant.getNano() / 10000;
		return BTime.valueOf(instant.get(ChronoField.YEAR), instant.get(ChronoField.DAY_OF_YEAR),
				instant.get(ChronoField.HOUR_OF_DAY), instant.get(ChronoField.MINUTE_OF_HOUR),
				instant.get(ChronoField.SECOND_OF_MINUTE), tenthMilliSecond);
	}

	public static BTime valueOf(String source) throws SeedException {
		Objects.requireNonNull(source);
		source = source.trim();
		if(source.isEmpty()){
			return null;
		}
		return valueOf(DateTimeUtil.parseAny(source));
	}

	public static BTime valueOf(byte[] bytes) throws SeedException {
		if (bytes == null) {
			throw new IllegalArgumentException("Cannot build BTime object from NULL.");
		}
		return valueOf(bytes, 0);
	}
	public static BTime valueOf(byte[] bytes, int offset) throws SeedException {
		if (bytes == null) {
			throw new IllegalArgumentException("Cannot build BTime object from NULL.");
		}
		if(bytes.length-offset<10){
			throw new SeedException("Expected 10 bytes but received {}", bytes.length-offset);
		}
		try {
			String btime = new String(bytes, StandardCharsets.US_ASCII).trim();
			String[] array = btime.split(",[ ]*");
			if (bytes.length == 0) {
				return null;
			}
			if (array[0].length() < 4) {
				throw new SeedException("Invalid time format: [" + btime + "]");
			}

			int year = Integer.parseInt(array[0]);
			if (array.length < 2) {
				return BTime.valueOf(year, 0, 0, 0, 0, 0);
			}

			int dayOfYear = Integer.parseInt(array[1]);
			if (array.length < 3) {
				return BTime.valueOf(year, dayOfYear, 0, 0, 0, 0);
			}
			int hour = Integer.parseInt(new String(bytes, 9, 2, StandardCharsets.US_ASCII));
			if (bytes.length < 14) {
				return BTime.valueOf(year, dayOfYear, hour, 0, 0, 0);
			}
			int minute = Integer.parseInt(new String(bytes, 12, 2, StandardCharsets.US_ASCII));
			if (bytes.length < 17) {
				return BTime.valueOf(year, dayOfYear, hour, minute, 0, 0);
			}
			int second = Integer.parseInt(new String(bytes, 15, 2, StandardCharsets.US_ASCII));
			if (bytes.length < 22) {
				return BTime.valueOf(year, dayOfYear, hour, minute, second, 0);
			}
			int tenthMilliSecond = Integer.parseInt(new String(bytes, 18, 4, StandardCharsets.US_ASCII));
			return BTime.valueOf(year, dayOfYear, hour, minute, second, tenthMilliSecond);
		} catch (NumberFormatException e) {
			throw new SeedException("invalid time " + new String(bytes), e);
		}

	}

	@Override
	public String toString() {
		return format();
	}

	public String toSeedString() {
		return this.zdt.getYear() +
				"," + String.format("%03d", this.zdt.getDayOfYear()) +
				"," + String.format("%02d", this.zdt.getHour()) +
				":" + String.format("%02d", this.zdt.getMinute()) +
				":" + String.format("%02d", this.zdt.getSecond()) +
				"." + String.format("%04d", this.zdt.getNano()/100000);
	}

	public String format(){
		return DateTimeUtil.format(zdt, "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'");
	}
	public static XMLGregorianCalendar toCalendar(BTime bTime) throws DatatypeConfigurationException {
		GregorianCalendar c = new GregorianCalendar();
		c.setTimeZone(TimeZone.getTimeZone("GMT"));
		c.set(Calendar.YEAR, bTime.getYear());

		c.set(Calendar.DAY_OF_YEAR, bTime.getDayOfYear());
		c.set(Calendar.HOUR_OF_DAY, bTime.getHour());
		c.set(Calendar.MINUTE, bTime.getMinute());
		c.set(Calendar.SECOND, bTime.getSecond());
		c.set(Calendar.MILLISECOND, bTime.getTenthMilliSecond());
		return DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
	}

	public static BTime toBTime(XMLGregorianCalendar xmlCal) throws DatatypeConfigurationException {
		if (xmlCal == null) {
			return null;
		}
		if (xmlCal.getTimezone() == DatatypeConstants.FIELD_UNDEFINED) {
			xmlCal.setTimezone(0);
		}
		// xmlCal.normalize();
		GregorianCalendar original = xmlCal.toGregorianCalendar();

		ZonedDateTime zdt = original.toZonedDateTime();
		ZonedDateTime converted = zdt.withZoneSameInstant(ZoneId.of("GMT"));

		return BTime.valueOf(converted.getYear(), converted.getDayOfYear(), converted.getHour(), converted.getMinute(),
				converted.getSecond(), converted.get(ChronoField.MILLI_OF_SECOND));
	}

	public ZonedDateTime toZonedDateTime() {
		return zdt;
	}

	public Instant toInstant() {
		return toZonedDateTime().toInstant();
	}

	public String toString(String pattern) {
		ZonedDateTime z = toZonedDateTime();
		return toString(z, pattern);
	}

	private static String toString(ZonedDateTime dateTime, String pattern) {
		if (dateTime == null) {
			return null;
		}

		DateTimeFormatter format = DateTimeFormatter.ofPattern(pattern).withZone(ZoneId.of("UTC"));
		return format.format(dateTime);

	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		BTime bTime = (BTime) o;
		System.out.println(zdt+"    <<>>      "+bTime.zdt);
		return Objects.equals(zdt, bTime.zdt);
	}

	@Override
	public int hashCode() {
		return Objects.hash(zdt);
	}

	@Override
	public int compareTo(BTime o) {
		if (o == null) {
			return -1;
		}
		return this.compareTo(o);
	}

	public static ByteOrder determineByteOrder(byte[]bytes, int offset) throws SeedException {
		Objects.requireNonNull(bytes);
		if(offset<0||bytes.length-offset<10){
			throw new SeedException("Expected 10 bytes but received {}", bytes.length-offset);
		}
		int year = ((short)(bytes[offset] & 255 | (bytes[offset + 1] & 255) << 8))& '\uffff';//le
		if (year >= 1900 && year <= 2050) {
			return ByteOrder.LITTLE_ENDIAN;
		}

		year = ((short)((bytes[offset] & 255) << 8 | bytes[offset + 1] & 255))& '\uffff';//be
		if (year >= 1900 && year <= 2050) {
			return ByteOrder.BIG_ENDIAN;
		}
		throw new SeedException("Couldn't determine byte-order! {}", year);
	}
}
