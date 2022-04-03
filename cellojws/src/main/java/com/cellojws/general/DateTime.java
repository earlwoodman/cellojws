/*
 * Created on 2010-06-17
 *
 * Earl Woodman
 * St. John's, NL
 */

package com.cellojws.general;

import java.io.Serializable;
import java.util.Calendar;

public class DateTime implements Serializable
{

	private static final long serialVersionUID = 4L;

	public static final int	JANUARY		= 1;
	public static final int	FEBRUARY	= 2;
	public static final int	MARCH		= 3;
	public static final int	APRIL		= 4;
	public static final int	MAY			= 5;
	public static final int	JUNE		= 6;
	public static final int	JULY		= 7;
	public static final int	AUGUST		= 8;
	public static final int	SEPTEMBER	= 9;
	public static final int	OCTOBER		= 10;
	public static final int	NOVEMBER	= 11;
	public static final int	DECEMBER	= 12;

	private long	year;
	private long	month;
	private long	day;
	private long	hour;
	private long	minute;
	private long	second;

	private long millisecond;

	private boolean	newDay;
	private boolean	newMonth;
	private boolean	newHour;

	public static final int DAYS_IN_NON_LEAPYEAR = 365;

	private class Increment
	{
		private long	baseAmount;
		private long	overflow;

		public Increment()
		{
			baseAmount = 0;
			overflow = 0;
		}

		public long getBaseAmount()
		{
			return baseAmount;
		}

		public void setBaseAmount(final long baseAmount)
		{
			this.baseAmount = baseAmount;
		}

		public long getOverflow()
		{
			return overflow;
		}

		public void setOverflow(final long overflow)
		{
			this.overflow = overflow;
		}
	}

	private Increment genericIncrement(final long currentTime, final long time2Add, final int lowerBound,
			final int upper_bound)
	{
		final Increment inc = new Increment();
		// Non-overflow case
		if (currentTime + time2Add <= upper_bound)
		{
			inc.setBaseAmount(currentTime + time2Add);
			return inc;
		}

		// Overflow case
		int ret = 1;
		long amount_adding = time2Add;
		amount_adding -= (upper_bound - lowerBound - currentTime + 1);

		while (amount_adding > upper_bound)
		{
			amount_adding -= (upper_bound - lowerBound + 1);
			ret++;
		}

		inc.setBaseAmount(amount_adding);
		inc.setOverflow(ret);

		return inc;
	}

	public DateTime()
	{
		year = 2000;
		month = 1;
		day = 1;
		hour = 0;
		minute = 0;
		second = 0;
		millisecond = 0;

	}

	public DateTime(final int y, final int mo, final int d, final int h, final int mi, final int s)
	{
		set(y, mo, d, h, mi, s);
	}

	public DateTime(final int y, final int mo, final int d)
	{
		set(y, mo, d, 1, 1, 1);
	}

	public DateTime makeCopy()
	{
		DateTime date_time = new DateTime();

		date_time.year = this.year;
		date_time.month = this.month;
		date_time.day = this.day;
		date_time.hour = this.hour;
		date_time.minute = this.minute;
		date_time.second = this.second;
		date_time.newMonth = this.newMonth;
		date_time.newDay = this.newDay;
		date_time.newHour = this.newHour;
		date_time.millisecond = this.millisecond;

		return date_time;
	}

	public boolean isSameDay(final DateTime target)
	{
		if (target == null)
		{
			return false;
		}

		if (year == target.year && month == target.month && day == target.day)
		{
			return true;
		}

		return false;
	}

	public boolean isEarlier(final DateTime rhs)
	{
		if (year < rhs.year || (year == rhs.year && month < rhs.month)
				|| (year == rhs.year && month == rhs.month && day < rhs.day)
				|| (year == rhs.year && month == rhs.month && day == rhs.day && hour < rhs.hour)
				|| (year == rhs.year && month == rhs.month && day == rhs.day && hour == rhs.hour && minute < rhs.minute)
				|| (year == rhs.year && month == rhs.month && day == rhs.day && hour == rhs.hour && minute == rhs.minute
						&& second < rhs.second)
				|| (year == rhs.year && month == rhs.month && day == rhs.day && hour == rhs.hour && minute == rhs.minute
						&& second == rhs.second && millisecond < rhs.millisecond))
		{
			return true;
		}

		return false;
	}

	public void incrementMilliseconds(final long milliseconds)
	{
		Increment inc = genericIncrement(millisecond, milliseconds, 0, 999);
		millisecond = inc.getBaseAmount();
		incrementSeconds(inc.getOverflow());
	}

	public void incrementSeconds(final long seconds)
	{
		newDay = false;
		newHour = false;
		newMonth = false;

		Increment inc = genericIncrement(second, seconds, 0, 59);
		second = inc.getBaseAmount();
		incrementMinutes(inc.getOverflow());

	}

	public void incrementMinutes(final long minutes)
	{
		Increment inc = genericIncrement(minute, minutes, 0, 59);
		minute = inc.getBaseAmount();
		incrementHours(inc.getOverflow());
	}

	public void incrementHours(final long hours)
	{

		Increment inc = genericIncrement(hour, hours, 0, 23);
		hour = inc.getBaseAmount();

		final long days = inc.getOverflow();
		if (days > 0)
		{
			newHour = true;
			incrementDays(days);
		}

	}

	public void incrementYear(final long year)
	{
		if (year > 0)
		{
			this.year += year;
		}
	}

	public void incrementDays(final long days)
	{
		long days_to_sub = days;

		// Start by adding to this month's days first.
		long days_left_this_month = getDaysPerMonth(month) - day;
		if (days_left_this_month >= days_to_sub)
		{
			day += days_to_sub;
			return;
		}

		days_to_sub -= days_left_this_month;

		long month_to_check = month;
		int months_to_add = 1;
		int years_incremented = 0;

		while (days_to_sub > 0)
		{
			month_to_check++;
			if (month_to_check > 12)
			{
				month_to_check = JANUARY;
				years_incremented++;
			}

			if (days_to_sub > getDaysPerMonth(month_to_check, year + years_incremented))
			{
				months_to_add++;
				days_to_sub -= getDaysPerMonth(month_to_check, year + years_incremented);
			}
			else
			{
				day = days_to_sub;
				days_to_sub = 0;
			}
		}

		newDay = (days > 0);

		incrementMonths(months_to_add);

	}

	public void incrementMonths(final int months)
	{
		newMonth = (months > 0);

		Increment inc = genericIncrement(month, months, 1, 12);
		month = inc.getBaseAmount();
		year += inc.getOverflow();

	}

	public void set(final int y, final int mo, final int d, final int h, final int mi, final int s)
	{
		year = y;
		month = mo;
		day = d;
		hour = h;
		minute = mi;
		second = s;
		millisecond = 0;
	}

	public long getDifferenceDays(final DateTime target)
	{
		if (target != null)
		{
			long total_days = 0;

			// see if they are the same day
			if (year == target.year && month == target.month && day == target.day)
			{
				return 0;
			}

			boolean multiply_by_neg1 = false;
			DateTime earlier;
			DateTime later;
			// Now determine which one is more recent
			if (year < target.year || year == target.year && month < target.month
					|| year == target.year && month == target.month && day < target.day)
			{
				multiply_by_neg1 = true;
				earlier = this;
				later = target;
			}
			else
			{
				earlier = target;
				later = this;
			}

			// Add up all the days in the rest of the 'earlier' year
			if (earlier.year == later.year)
			{
				total_days = differenceForSameYear(earlier, later);
			}
			else
			{
				total_days = differenceForDifferentYears(earlier, later);
			}

			return (multiply_by_neg1 ? -total_days : total_days);
		}

		return 0;
	}

	private long differenceForSameYear(final DateTime earlier, final DateTime later) 
	{
		long total_days;
		if (earlier.month == later.month)
		{
			total_days = later.day - earlier.day;
		}
		else
		{
			total_days = getDifferenceDays(earlier.month, earlier.day, later.month, later.day, earlier.year);
		}
		return total_days;
	}

	private long differenceForDifferentYears(final DateTime earlier, final DateTime later) 
	{
		long total_days;
		total_days = getDifferenceDays(earlier.month, earlier.day, DECEMBER, getDaysPerMonth(DECEMBER),
				earlier.year);
		// Add up all years that are full years
		for (long i = earlier.year + 1; i < later.year; i++)
		{
			total_days += DAYS_IN_NON_LEAPYEAR + (isLeapYear(i) ? 1 : 0);
		}
		total_days += getDifferenceDays(JANUARY, 1, later.month, later.day, later.year) + 1;
		return total_days;
	}

	public static final long getFebruaryDays(final long year)
	{
		if (isLeapYear(year))
			return 29;

		return 28;
	}

	public static boolean isLeapYear(long year)
	{
		// Performance short-circuit
		if( year == 2016 || year == 2020 || year == 2024 ||				
			year == 2028 || year == 2032 || year == 2036 ||
			year == 2040 || year == 2044 )
		{
			return true;
		}
		
		return year % 4 == 0 && year % 100 != 0 || year % 100 == 0 && year % 400 == 0;

	}

	public long getDaysPerMonth(final long month)
	{
		return getDaysPerMonth(month, this.year);
	}

	public static final long getDaysPerMonth(final long month, final long year)
	{
		switch ((int) month)
		{
			case JANUARY:
			case MARCH:
			case MAY:
			case JULY:
			case AUGUST:
			case OCTOBER:
			case DECEMBER:
				return 31;
			case FEBRUARY:
				return getFebruaryDays(year);
			case APRIL:
			case JUNE:
			case SEPTEMBER:
			case NOVEMBER:
				return 30;
			default:
				return 0;
		}

	}

	public static final long getDifferenceDays(final long earlierMonth, final long earlierDay, final long laterMonth,
			final long laterDay, final long year)
	{

		if (earlierMonth == laterMonth)
		{
			return laterDay - earlierDay;
		}
		else
		{
			long totalDays = 0;

			totalDays = (getDaysPerMonth(earlierMonth, year) - (earlierDay - 1)) + (laterDay - 1);

			for (long i = earlierMonth + 1; i < laterMonth; i++)
			{
				totalDays += getDaysPerMonth(i, year);
			}

			return totalDays;
		}

	}

	/**
	 * Not quite random. It takes a random day after choosing a random month so
	 * shorter months are more likely to be represented
	 * 
	 * @param year
	 * @return
	 */
	public static DateTime getRandomDateInYear(final int year)
	{
		final int month = Random.getRandIntInclusive(JANUARY, DECEMBER);
		int day = -1;
		while (day < 0 || day >= DateTime.getDaysPerMonth(month, year))
		{
			day = Random.getRandIntInclusive(1, 31);
		}

		return new DateTime(year, month, day, 0, 0, 0);
	}

	public static boolean isValidDate(final DateTime dateToTest)
	{
		if (dateToTest.month <= 12 && dateToTest.month >= 1)
		{
			if (dateToTest.day >= 0 && dateToTest.day < dateToTest.getDaysPerMonth(dateToTest.month))
			{
				if (dateToTest.hour >= 0 && dateToTest.hour < 24)
				{
					if (dateToTest.minute >= 0 && dateToTest.minute < 60)
					{
						if (dateToTest.second >= 0 && dateToTest.second < 60)
						{
							return true;
						}
					}
				}
			}
		}

		return false;
	}

	public String toString()
	{
		return toStringDateOnly();
	}

	public String toStringDateOnly()
	{
		final StringBuffer dateTime = new StringBuffer();
		dateTime.append(month);
		dateTime.append('/');
		dateTime.append(day);
		dateTime.append('/');
		dateTime.append(year);

		return dateTime.toString();
	}

	public String toStringMinsSeconds()
	{
		StringBuffer dateTime = new StringBuffer();

		dateTime.append(padTimePart(minute));
		dateTime.append(':');
		dateTime.append(padTimePart(second));

		return dateTime.toString();

	}

	public String toStringTimeOnly()
	{
		StringBuffer dateTime = new StringBuffer();

		String AMorPM;

		if (hour < 12)
		{
			dateTime.append(hour);
			AMorPM = "AM";
		}
		else
		{
			dateTime.append(hour - 12);
			AMorPM = "PM";
		}

		dateTime.append(':');
		dateTime.append(padTimePart(minute));

		dateTime.append(AMorPM);

		return dateTime.toString();
	}

	private String padTimePart(final long value)
	{
		String timePart = Integer.valueOf((int) value).toString();
		if (timePart.length() == 0)
		{
			timePart = "00";
		}
		else
			if (timePart.length() == 1)
			{
				timePart = "0" + timePart;
			}

		return timePart;
	}

	public long getYear()
	{
		return year;
	}

	public void setYear(final long year)
	{
		this.year = year;
	}

	public long getMonth()
	{
		return month;
	}

	public void setMonth(final long month)
	{
		this.month = month;
	}

	public long getDay()
	{
		return day;
	}

	public void setDay(final long day)
	{
		this.day = day;
	}

	public long getHour()
	{
		return hour;
	}

	public void setHour(final long hour)
	{
		this.hour = hour;
	}

	public long getMinute()
	{
		return minute;
	}

	public void setMinute(final long minute)
	{
		this.minute = minute;
	}

	public long getSecond()
	{
		return second;
	}

	public void setSecond(final long second)
	{
		this.second = second;
	}

	public long getMillisecond()
	{
		return millisecond;
	}

	public DateTime yesterday()
	{
		final DateTime yesterday = makeCopy();

		yesterday.decrementDay();

		return yesterday;
	}

	public DateTime tomorrow()
	{
		final DateTime tomorrow = makeCopy();

		tomorrow.incrementDays(1);

		return tomorrow;
	}

	// Decrements by one day
	public void decrementDay()
	{
		if (day > 1)
		{
			day--;
			return;
		}

		// Ok, it's the first day of the month
		// What month is it?
		if (month != JANUARY)
		{
			month--;
			day = getDaysPerMonth(month);
			return;
		}

		// It's the first day of the year
		month = DECEMBER;
		day = getDaysPerMonth(DECEMBER);
		year--;

	}

	public void decrementHour()
	{
		if (hour > 0)
		{
			hour--;
		}
		else
		{
			hour = 23;
			decrementDay();
		}
	}

	public void decrementMinute()
	{
		if (minute > 0)
		{
			minute--;
		}
		else
		{
			minute = 59;
			decrementHour();
		}
	}

	public DateTime getESTEquivalent(final float diff)
	{
		// We are assuming that timezones are only ever on the hour or half-hour

		final DateTime newDate = this.makeCopy();

		if (diff > 0)
		{
			// The place is behind EST
			newDate.incrementHours((int) Math.floor(diff));
			// And now the fraction
			if (diff != Math.floor(diff))
			{
				newDate.incrementMinutes(30);
			}
		}
		else
		{
			// The place is ahead of EST
			for (int i = 0; i < Math.abs(Math.floor(diff)); i++)
			{
				newDate.decrementHour();
			}

			if (diff != Math.ceil(diff))
			{
				for (int i = 0; i < 30; i++)
				{
					newDate.decrementMinute();
				}
			}
		}

		return newDate;
	}

	public String toStringMonthYear()
	{
		final StringBuffer buff = new StringBuffer();

		addMonthToBuffer(buff);

		buff.append(Integer.valueOf((int) year).toString());

		return buff.toString();
	}

	public String toStringDayMonth()
	{
		final StringBuffer buff = new StringBuffer();

		buff.append(Integer.valueOf((int) month).toString());
		
		buff.append("/");
		
		buff.append(Integer.valueOf((int) day).toString());
		
		return buff.toString();
	}

	public String toStringFormatted()
	{
		final StringBuffer buff = new StringBuffer();

		addMonthToBuffer(buff);

		buff.append(Integer.valueOf((int) day).toString());
		buff.append(", ");
		buff.append(Integer.valueOf((int) year).toString());

		return buff.toString();
	}

	private void addMonthToBuffer(final StringBuffer buff)
	{
		switch ((int) month)
		{
			case JANUARY:
				buff.append("January ");
				break;
			case FEBRUARY:
				buff.append("February ");
				break;
			case MARCH:
				buff.append("March ");
				break;
			case APRIL:
				buff.append("April ");
				break;
			case MAY:
				buff.append("May ");
				break;
			case JUNE:
				buff.append("June ");
				break;
			case JULY:
				buff.append("July ");
				break;
			case AUGUST:
				buff.append("August ");
				break;
			case SEPTEMBER:
				buff.append("September ");
				break;
			case OCTOBER:
				buff.append("October ");
				break;
			case NOVEMBER:
				buff.append("November ");
				break;
			case DECEMBER:
				buff.append("December ");
				break;
		}
	}

	/**
	 * Returns a string representation of the given month
	 * 
	 * @param month
	 * @return
	 */
	public static String getMonthByName(final int month)
	{
		switch (month)
		{
			case JANUARY:
				return "January";
			case FEBRUARY:
				return "February";
			case MARCH:
				return "March";
			case APRIL:
				return "April";
			case MAY:
				return "May";
			case JUNE:
				return "June";
			case JULY:
				return "July";
			case AUGUST:
				return "August";
			case SEPTEMBER:
				return "September";
			case OCTOBER:
				return "October";
			case NOVEMBER:
				return "November";
			case DECEMBER:
				return "December";
		}

		return "";
	}

	public long getDifferenceMillis(final DateTime other)
	{
		final long thisTimeInMillis = getTimeInMillis();

		final long otherTimeInMillis = getTimeInMillis();

		return otherTimeInMillis - thisTimeInMillis;
	}

	private long getTimeInMillis()
	{
		long millis = getYear() * getMillisPerYear() + getDay() * getMillisPerDay() + getHour() * getMillisPerHour()
				+ getMinute() * getMillisPerMinute() + getSecond() * 1000;

		for (int i = 1; i < getMonth(); i++)
		{
			millis += getMillisPerMonth(i);
		}

		return millis;
	}

	private long getMillisPerYear()
	{
		return 0;
	}

	private long getMillisPerMonth(final int month)
	{
		return getMillisPerDay() * getDaysPerMonth(month);
	}

	private long getMillisPerDay()
	{
		return getMillisPerMinute() * 60;
	}

	private long getMillisPerMinute()
	{
		return 60000;
	}

	public long getMillisSinceLastHour()
	{
		return getMinute() * 60000 + getSecond() * 60 + getMillisecond();
	}

	public static long getMillisPerHour()
	{
		return 3600000;
	}

	public boolean isWinterNorthernHemisphere()
	{
		return getMonth() == JANUARY || getMonth() == FEBRUARY || (getMonth() == MARCH && getDay() < 20)
				|| (getMonth() == DECEMBER && getDay() > 21);
	}

	public float progressToWinterNorthernHemisphere()
	{
		final int daysFall = DAYS_IN_NON_LEAPYEAR / 4;

		final DateTime lastDayOfFall = makeCopy();
		lastDayOfFall.setMonth(DECEMBER);
		lastDayOfFall.setDay(21);

		final long days = lastDayOfFall.getDifferenceDays(this);

		if (days > 0)
		{
			return (daysFall - days) * 1.0F / daysFall;
		}

		return 0;
	}

	public static int getMonthByName(String month)
	{
		if (month == null)
		{
			return -1;
		}

		String lowerMonth = month.toLowerCase();

		if ("january".equals(lowerMonth))
		{
			return JANUARY;
		}
		if ("february".equals(lowerMonth))
		{
			return FEBRUARY;
		}
		if ("march".equals(lowerMonth))
		{
			return MARCH;
		}
		if ("april".equals(lowerMonth))
		{
			return APRIL;
		}
		if ("may".equals(lowerMonth))
		{
			return MAY;
		}
		if ("june".equals(lowerMonth))
		{
			return JUNE;
		}
		if ("july".equals(lowerMonth))
		{
			return JULY;
		}
		if ("august".equals(lowerMonth))
		{
			return AUGUST;
		}
		if ("september".equals(lowerMonth))
		{
			return SEPTEMBER;
		}
		if ("october".equals(lowerMonth))
		{
			return OCTOBER;
		}
		if ("november".equals(lowerMonth))
		{
			return NOVEMBER;
		}
		if ("december".equals(lowerMonth))
		{
			return DECEMBER;
		}

		return -1;
	}

	@Override
	public boolean equals(Object arg0)
	{
		final DateTime arg = (DateTime) arg0;
		return this.millisecond == arg.millisecond && this.second == arg.second && this.minute == arg.minute
				&& this.hour == arg.hour && this.day == arg.day && this.month == arg.month && this.year == arg.year;
	}

	public static int getDayOfWeekMonthStarts(final int month, final int year)
	{
		final Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.MONTH, month - 1);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.YEAR, year);
		return calendar.get(Calendar.DAY_OF_WEEK) - 1;

	}

	/**
	 * Uses isEarlier() to figure this out, by copying the target and changing
	 * the year to match the year for "this".
	 * 
	 * Better than writing custom code here.
	 * 
	 * @param target
	 * @return
	 */
	public boolean isEarlierIgnoreYear(DateTime target)
	{
		final DateTime temp = target.makeCopy();

		temp.setYear(getYear());

		return isEarlier(temp);
	}

	public String getMonthSlashDay()
	{
		String month = Long.toString(getMonth());
		String day = Long.toString(getDay());

		return month + "/" + day;
	}

	/**
	 * If the this object is one day after the target date, this method returns true
	 * 
	 * @param targetDate
	 * @return
	 */
	public boolean isDayAfter(DateTime targetDate)
	{
		// New Years Eve / New Years Day
		if (this.getYear() == targetDate.getYear() + 1)
		{
			if (this.getMonth() == JANUARY && this.getDay() == 1 && targetDate.getMonth() == DECEMBER
					&& targetDate.getDay() == getDaysPerMonth(DECEMBER))
			{
				return true;
			}
		}

		// Any other day of year
		if (this.getYear() == targetDate.getYear())
		{
			if (this.getMonth() == targetDate.getMonth())
			{
				if (this.getDay() == targetDate.getDay() + 1)
				{
					return true;
				}
			}
			else
				if (this.getMonth() == targetDate.getMonth() + 1)
				{
					if (this.getDay() == 1
							&& targetDate.getDay() == targetDate.getDaysPerMonth(targetDate.getMonth()))
					{
						return true;
					}
				}
		}

		return false;
	}

	public long getDaysThisMonth() 
	{
		return getDaysPerMonth(this.month, this.year);
	}

}
