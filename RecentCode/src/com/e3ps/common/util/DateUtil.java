/**
 * @(#) CommonUtil.java Copyright (c) jerred. All rights reserverd
 * @version 1.00
 * @since jdk 1.4.02
 * @createdate 2004. 3. 3.
 * @author Cho Sung Ok, jerred@bcline.com
 * @desc
 */

package com.e3ps.common.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.Vector;

import org.apache.commons.lang3.time.FastDateFormat;
import org.joda.time.DateTime;

import com.e3ps.common.log4j.Log4jPackages;
import com.e3ps.common.message.util.MessageUtil;
import com.ibm.icu.util.ChineseCalendar;

import wt.util.WTException;


public final class DateUtil {
	
	private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(Log4jPackages.COMMON.getName());
	
	private static FastDateFormat defaultDateFormat = null;
	private static FastDateFormat defaultTimeFormat = null;
	private static final String[] monthNames = { "JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC" };
	static {
		defaultDateFormat = getDefaultDateFormat();
		defaultTimeFormat = getDefaultTimeFormat();
	}
	
	
	public static final int LD_SUNDAY = 7;
	public static final int LD_SATURDAY = 6;
	public static final int LD_MONDAY = 1;
	public static final int LD_TUESDAY = 2;
	public static final int LD_WENDNESDAY = 3;
	public static final int LD_THURSDAY = 4;
	public static final int LD_FRIDAY = 5;
	
	public static LocalDate Lunar2Solar(LocalDate lunar) {
		ChineseCalendar cc = new ChineseCalendar();
		
		cc.set(ChineseCalendar.EXTENDED_YEAR, lunar.getYear()+2637);
		cc.set(ChineseCalendar.MONTH, lunar.getMonthValue()-1);
		cc.set(ChineseCalendar.DAY_OF_MONTH, lunar.getDayOfMonth());
		
		LocalDate solar = Instant.ofEpochMilli(cc.getTimeInMillis()).atZone(ZoneId.of("UTC")).toLocalDate();
		
		return solar;
	}
	
	public static LocalDate substituteHoliday_(LocalDate h) {
		if(h.getDayOfWeek().getValue() == LD_SUNDAY) {
			return h.plusDays(1);
		}
		return h;
	}
	
	public static boolean notHoliday(LocalDate h) {
		boolean chk = false;
		int v = h.getDayOfWeek().getValue();
		if(v != LD_SUNDAY && v != LD_SATURDAY) {
			chk = true;
		}
		return chk;
	}
	
	public static LocalDate substituteHoliday(LocalDate h) {
		if(h.getDayOfWeek().getValue() == LD_SUNDAY) {
			return h.plusDays(1);
		}
		
		if(h.getDayOfWeek().getValue() == LD_SATURDAY) {
			return h.plusDays(2);
		}
		return h;
	}
	
	
	public static LocalDate loDate(int year, int mon, int day) {
		LocalDate d = LocalDate.of(year, mon, day);
		return d;
	}
	
	
	public static String convertLocalDate(LocalDate date, String format) {
		return date.format(DateTimeFormatter.ofPattern(format));
	}
	
	
	public static Set<String> holidaySet(int year, Set<String> set){
		Set<String> holidaySet = set;
		try {
			
			boolean chk = false;
			/* 양력 */
			chk = DateUtil.notHoliday(loDate(year, 1, 1));
			if(chk) {
				holidaySet.add(convertLocalDate(loDate(year, 1, 1), "YYYY/MM/dd")); 
			}
			chk = DateUtil.notHoliday(loDate(year, 2, 1)); 
			if(chk) {
				holidaySet.add(convertLocalDate(loDate(year, 3, 1), "YYYY/MM/dd"));
			}
			chk = DateUtil.notHoliday(loDate(year, 5, 5));
			if(chk) {
				holidaySet.add(convertLocalDate(loDate(year, 5, 5), "YYYY/MM/dd"));
			}
			chk = DateUtil.notHoliday(loDate(year, 6, 6));
			if(chk) {
				holidaySet.add(convertLocalDate(loDate(year, 6, 6), "YYYY/MM/dd"));
			}
			chk = DateUtil.notHoliday(loDate(year, 8, 15));
			if(chk) {
				holidaySet.add(convertLocalDate(loDate(year, 8, 15), "YYYY/MM/dd"));
			}
			chk = DateUtil.notHoliday(loDate(year, 10, 3));
			if(chk) {
				holidaySet.add(convertLocalDate(loDate(year, 10, 3), "YYYY/MM/dd"));
			}
			chk = DateUtil.notHoliday(loDate(year, 10, 3));
			if(chk) {
				holidaySet.add(convertLocalDate(loDate(year, 10, 9), "YYYY/MM/dd"));
			}
			chk = DateUtil.notHoliday(loDate(year, 12, 25));
			if(chk) {
				holidaySet.add(convertLocalDate(loDate(year, 12, 25), "YYYY/MM/dd"));
			}
			
			
			/* 음력 */
			chk = DateUtil.notHoliday(Lunar2Solar(loDate(year, 1, 1)).minusDays(1));
			if(chk) {
				holidaySet.add(convertLocalDate(Lunar2Solar(loDate(year, 1, 1)).minusDays(1), "YYYY/MM/dd"));
			}
			
			chk = DateUtil.notHoliday(Lunar2Solar(loDate(year, 1, 1)));
			if(chk) {
				holidaySet.add(convertLocalDate(Lunar2Solar(loDate(year, 1, 1)), "YYYY/MM/dd"));
			}
			
			chk = DateUtil.notHoliday(Lunar2Solar(loDate(year, 1, 2)));
			if(chk) {
				holidaySet.add(convertLocalDate(Lunar2Solar(loDate(year, 1, 2)), "YYYY/MM/dd"));
			}
			
			chk = DateUtil.notHoliday(Lunar2Solar(loDate(year, 4, 8)));
			if(chk) {
				holidaySet.add(convertLocalDate(Lunar2Solar(loDate(year, 4, 8)), "YYYY/MM/dd"));
			}
			
			chk = DateUtil.notHoliday(Lunar2Solar(loDate(year, 8, 14)));
			if(chk) {
				holidaySet.add(convertLocalDate(Lunar2Solar(loDate(year, 8, 14)), "YYYY/MM/dd"));
			}
			
			chk = DateUtil.notHoliday(Lunar2Solar(loDate(year, 8, 15)));
			if(chk) {
				holidaySet.add(convertLocalDate(Lunar2Solar(loDate(year, 8, 15)), "YYYY/MM/dd"));
			}
			
			chk = DateUtil.notHoliday(Lunar2Solar(loDate(year, 8, 16)));
			if(chk) {
				holidaySet.add(convertLocalDate(Lunar2Solar(loDate(year, 8, 16)), "YYYY/MM/dd"));
			}
			
			/* 양력이 주말일 경우 */
			holidaySet.add(convertLocalDate(substituteHoliday(loDate(year, 3, 1)), "YYYY/MM/dd"));
			holidaySet.add(convertLocalDate(substituteHoliday(loDate(year, 5, 5)), "YYYY/MM/dd"));
			holidaySet.add(convertLocalDate(substituteHoliday(loDate(year, 8, 15)), "YYYY/MM/dd"));
			holidaySet.add(convertLocalDate(substituteHoliday(loDate(year, 10, 3)), "YYYY/MM/dd"));
			holidaySet.add(convertLocalDate(substituteHoliday(loDate(year, 10, 9)), "YYYY/MM/dd"));
			
			
			/* 음력이 주말일 경우 */
			int type_11 = Lunar2Solar(loDate(year, 1, 1)).getDayOfWeek().getValue();
			int type_12 = Lunar2Solar(loDate(year, 1, 2)).getDayOfWeek().getValue();
			int type_814 = Lunar2Solar(loDate(year, 8, 14)).getDayOfWeek().getValue();
			int type_815 = Lunar2Solar(loDate(year, 8, 15)).getDayOfWeek().getValue();
			int type_816 = Lunar2Solar(loDate(year, 8, 16)).getDayOfWeek().getValue();
			
			int type_103 = loDate(year, 10, 3).getDayOfWeek().getValue();
			int type_109 = loDate(year, 10, 9).getDayOfWeek().getValue();
			
			
			if(type_11 == LD_SUNDAY || type_11 == LD_MONDAY || type_12 == LD_SUNDAY) {
				holidaySet.add(convertLocalDate(Lunar2Solar(LocalDate.of(year, 1, 3)), "YYYY/MM/dd").toString());
			}
			
			if(type_814 == LD_SUNDAY || type_815 == LD_SUNDAY || type_816 == LD_SUNDAY || type_814 == type_103 || type_816 == type_103 || type_814 == type_109 || type_816 == type_109) {
				holidaySet.add(convertLocalDate(Lunar2Solar(LocalDate.of(year, 8, 17)), "YYYY/MM/dd").toString());
			}
			
			//System.out.println(year + "년도 Holiday Count : " + holidaySet.size() + "일");
			
		}catch(Exception e){
			e.printStackTrace();
		}
		return holidaySet;
	}
	
	
	public static Set<String> holidaySet(int year){
		Set<String> set = new HashSet<String>();
		return holidaySet(year, set);
	}
	
	
	
	
	
	/**
	 * 객체 생성을 방지하기 위해서 디폴트 생성자를 Private로 선언
	 */
	private DateUtil() {}


    public static int getBrowserTimeZoneOffset() {
		int timezoneOffset = 540;
		return timezoneOffset;
    }

    public static TimeZone getTimeZone() {
    	return TimeZone.getTimeZone("JST");
    }
    
    /**
     * @return the defaultDateFormat
     */
    public static FastDateFormat getDefaultDateFormat() {
    	FastDateFormat defaultDateFormat = null;
		TimeZone zone1 = getTimeZone();
	    defaultDateFormat = FastDateFormat.getInstance("yyyy/MM/dd",zone1, MessageUtil.getLocale());
	    
		return defaultDateFormat;
    }

    /**
     * @return the defaultTimeFormat
     */
    public static FastDateFormat getDefaultTimeFormat() {
	    FastDateFormat defaultTimeFormat = null;
		TimeZone zone1 = getTimeZone();
		defaultTimeFormat = FastDateFormat.getInstance("HH:mm:ss",zone1, MessageUtil.getLocale());
	    
	    return defaultTimeFormat;
    }
	
	public static String getDateString(Date date, String type) {
		if (date == null) return "";

		if (type.equalsIgnoreCase ( "all" ) || type.equalsIgnoreCase ( "a" )) {
			return defaultDateFormat.format ( date ) + " " + defaultTimeFormat.format ( date );
		} else if (type.equalsIgnoreCase ( "date" ) || type.equalsIgnoreCase ( "d" )) {
			return defaultDateFormat.format ( date );
		} else if (type.equalsIgnoreCase ( "time" ) || type.equalsIgnoreCase ( "t" )) {
			return defaultTimeFormat.format ( date );
		} else {
			return date.toString ();
		}
	}
    
    /**
     * 현재 날을 설정된 형식으로 출력한다.
     * @param timestamp
     * @param type all[a] | date[d] | time[t] 
     * @return java.lang.String
     */
    public static String getDateString(Timestamp timestamp, String type)
    {
        if(timestamp == null) return "";
        return getDateString(new Date(timestamp.getTime()), type);
    }
    /**
     *   주어진 Timestamp의 시간을 입력된 fomat형태로 RETURN
     *   @param  Timestamp time
     *   @param String format(ex:yyyy-MM-dd HH:mm:ss:SS)
     *   @return String str
     *   @since 2005.01
     */ 
     public static String getTimeFormat(Date time, String format) {
    	 SimpleDateFormat formatter;
    	 formatter = new SimpleDateFormat(format, MessageUtil.getLocale() );
    	 if(time == null){
    		 return "";
    	 }else{
    		 String str = formatter.format(time);
    		 return str;
    	 }
     }
     /**
    *   주어진 시간String을 주어진 FormatString형태로 인식하는 Timestamp로 변환한다.
    *   @param  timeString
    *   @param  formatString        
    *   @return Timestamp
    *   @since 2005.01
     */              
     public static Timestamp getTimestampFormat(String timeString, String formatString) throws Exception{
         SimpleDateFormat format = new SimpleDateFormat(formatString);
         Date date = format.parse(timeString);
         return new Timestamp(date.getTime());
     }
	
    /**
     * 현재 날을 설정된 형식으로 출력한다.
     * @param type all[a] | date[d] | time[t] 
     * @return
     * @throws WTException 
     */
    public static String getCurrentDateString(String type) {
		Date currentDate = new Date ();

		if (type.equalsIgnoreCase ( "all" ) || type.equalsIgnoreCase ( "a" )) {
			return defaultDateFormat.format ( currentDate ) + " " + defaultTimeFormat.format ( currentDate );
		} else if (type.equalsIgnoreCase ( "date" ) || type.equalsIgnoreCase ( "d" )) {
			return defaultDateFormat.format ( currentDate );
		} else if (type.equalsIgnoreCase ( "time" ) || type.equalsIgnoreCase ( "t" )) {
			return defaultTimeFormat.format ( currentDate );
		} else if (type.equalsIgnoreCase ( "year" ) || type.equalsIgnoreCase ( "y" )) {
			return new SimpleDateFormat ( "yyyy" , MessageUtil.getLocale() ).format ( currentDate );
		} else if (type.equalsIgnoreCase ( "month" ) || type.equalsIgnoreCase ( "m" )) {
			return new SimpleDateFormat ( "yyMM" , MessageUtil.getLocale() ).format ( currentDate );
		} else {
			return currentDate.toString ();
		}
	}
    
    public static String getCurrentDateString(SimpleDateFormat dateFormat) {
        Date currentDate = new Date ();
        return dateFormat.format ( currentDate );
    }
    
    public static Timestamp getCurrentTimestamp(){
        Date currentDate = new Date ();
        return new Timestamp(currentDate.getTime());
    }

	/**
	 * 웹페이지 리스트에 날짜를 표시하기 위해서 적당한 형태로 변경시켜주는 Method <br>
	 * 오늘과 같은 날짜일 경우에는 시간만 표시하고 <br>
	 * 오늘과 다른 날일 경우에는 날짜만 표시한다. <br>
	 * 
	 * @param date <code>java.lang.String</code> 변환할 날짜 정보 String
	 * @return <code>java.lang.String</code> 변환된 날짜 정보 String
	 */
	public static String getDateToWeb(Date date) {		
		if (date == null) return "";		
		String currentDate = defaultDateFormat.format ( new Date () );		
		String paramDate = defaultDateFormat.format ( date );	
		String paramTime = defaultTimeFormat.format ( date );		

		if (currentDate.equals ( paramDate )) {
			return paramTime;
		}
		else {
			return paramDate;
		}
		
		
	}

	public static boolean isToday(Date date) {
		String currentDate = defaultDateFormat.format ( new Date () );
		String paramDate = defaultDateFormat.format ( date );

		if (currentDate.equals ( paramDate )) return true;
		else return false;
	}

	public static String getDateString(Date date, SimpleDateFormat format) {
		return format.format ( date );
	}

	public static Date parseDateStr(String dateStr) throws ParseException {
		return defaultDateFormat.parse ( dateStr );
	}

	public static Date parseDateStr(SimpleDateFormat format, String dateStr)
		throws ParseException {
		return format.parse ( dateStr );
	}

	public static String getToDay() {
		String toDay = "";
		try {
			//SimpleDateFormat sdFormat = new SimpleDateFormat ( "yyyy/MM/dd" );
			toDay = defaultDateFormat.format ( Calendar.getInstance ().getTime () );
		} catch (Exception e) {
			System.err.println ( "Exception : " + e.getMessage () );
		}
		return toDay;
	}
	
	public static String getToDay(String format) {
		String toDay = "";
		try {
			SimpleDateFormat sdFormat = new SimpleDateFormat ( format );
			toDay = sdFormat.format ( Calendar.getInstance ().getTime () );
		} catch (Exception e) {
			System.err.println ( "Exception : " + e.getMessage () );
		}
		return toDay;
	}

	public static Timestamp convertDate(String str) {
        if(str == null || str.length() == 0) return null;
		Timestamp convertDate = ( new Timestamp ( defaultDateFormat.parse ( str , new ParsePosition ( 0 ) ).getTime () ) );
		return convertDate;
	}

	public static Timestamp convertStartDate(String str) {
        if(str == null || str.length() == 0) return null;
		Timestamp convertDate = ( new Timestamp ( defaultDateFormat.parse ( str , new ParsePosition ( 0 ) ).getTime () ) );
		return convertDate;
	}

	public static Timestamp convertEndDate(String str) {
        if(str == null || str.length() == 0) return null;
		Timestamp convertDate = ( new Timestamp ( defaultDateFormat.parse ( str , new ParsePosition ( 0 ) ).getTime () ) );
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(convertDate.getTime());
		cal.add(Calendar.DATE, 1);
		convertDate = new Timestamp(cal.getTimeInMillis());
		return convertDate;
	}
	
	/**
	 * 일년 간 달력의 월별 날짜수 배열을 구한다.
	 * @param year
	 * @return
	 */
	public static int[] getMonthDaysArray(int year) {
		int[] monthDayCaseOne = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		int[] monthDayCaseTwo = { 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

		if (year <= 1582) {
			if ((year % 4) == 0) {
				if (year == 4) {
					return monthDayCaseOne;
				}
				return monthDayCaseTwo;
			}
		} else {
			if (((year % 4) == 0) && ((year % 100) != 0)) {
				return monthDayCaseTwo;
			} else if ((year % 400) == 0) {
				return monthDayCaseTwo;
			}
		}
		return monthDayCaseOne;
	}

	/**
	 * 지정된 년도와 달에 따른 요일 편차를 구한다.
	 * @param year
	 * @param month
	 * @return
	 */
	public static int addWeekDays(int year, int month) {
		int[] b1 = { 3, 0, 3, 2, 3, 2, 3, 3, 2, 3, 2, 3 };
		int[] b2 = { 3, 1, 3, 2, 3, 2, 3, 3, 2, 3, 2, 3 };
		int i = 0;
		int rval = 0;

		if (year <= 1582) {
			if ((year % 4) == 0) {
				if (year == 4) {
					for (i = 0; i < month - 1; i++) rval += b1[i];
				} else {
					for (i = 0; i < month - 1; i++) rval += b2[i];
				}
			} else {
				for (i = 0; i < month - 1; i++) rval += b1[i];
			}
		} else {
			if (((year % 4) == 0) && ((year % 100) != 0)) {
				for (i = 0; i < month - 1; i++) rval += b2[i];
			} else if ((year % 400) == 0) {
				for (i = 0; i < month - 1; i++) rval += b2[i];
			} else {
				for (i = 0; i < month - 1; i++) rval += b1[i];
			}
		}

		return rval;
	}
	
	/**
	 * 지정한 년도의 총 날짜 수를 구한다.
	 * @param year
	 * @return
	 */
	public static int getDaysInYear(int year) {
		if (year > 1582) {
			if (year % 400 == 0) return 366;
			else if (year % 100 == 0) return 365;
			else if (year % 4 == 0) return 366;
			else return 365;
		} else if (year == 1582) {
			return 355;
		} else if (year > 4) {
			if (year % 4 == 0) return 366;
			else return 365;
		} else if (year > 0) {
			return 365;
		} else {
			return 0;
		}
	}
	
	/**
	 * 지정한 년도, 지정한 월의 총 날짜 수를 구한다.
	 * @param month
	 * @param year
	 * @return
	 */
	public static int getDaysInMonth(int year, int month) {
		if (month < 1 || month > 12) throw new RuntimeException("Invalid month: " + month);

		int[] dayInMonth = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };
		if (month != 2 && month >= 1 && month <= 12 && year != 1582) return dayInMonth[month - 1];
		if (month != 2 && month >= 1 && month <= 12 && year == 1582) {
			if (month != 10) return dayInMonth[month - 1];
			else  return dayInMonth[month - 1] - 10;
		}
		if (month != 2) return 0;

		// m == 2 (즉 2월)
		if (year > 1582) {
			if (year % 400 == 0) return 29;
			else if (year % 100 == 0) return 28;
			else if (year % 4 == 0) return 29;
			else return 28;
		} else if (year == 1582) {
			return 28;	
		} else if (year > 4) {
			if (year % 4 == 0) return 29;
			else return 28;
		} else if (year > 0) {
			return 28;	
		} else {
			throw new RuntimeException("Invalid year: " + year);
		}
	}

	/**
	 * 지정한 년도의 지정한 월의 첫날 부터 지정한 날 까지의 날짜 수를 구한다.
	 * @param day
	 * @param month
	 * @param year
	 * @return
	 */
	public static int getDaysFromMonthFirst(int year, int month, int day) {
		if (month < 1 || month > 12) throw new RuntimeException("Invalid month " + month + " in " + day + "/" + month + "/" + year);
		int max = getDaysInMonth(year, month);
		if (day >= 1 && day <= max) return day;
		else throw new RuntimeException("Invalid date " + day + " in " + day + "/" + month + "/" + year);
	}
	
	/**
	 * 지정한 년도의 첫날 부터 지정한 월의 지정한 날 까지의 날짜 수를 구한다.
	 * @param day
	 * @param month
	 * @param year
	 * @return
	 */
	public static int getDaysFromYearFirst(int year, int month, int day) {
		if (month < 1 || month > 12) throw new RuntimeException("Invalid month " + month + " in " + day + "/" + month + "/" + year);

		int max = getDaysInMonth(year, month);
		if (day >= 1 && day <= max) {
			int sum = day;
			for (int j = 1; j < month; j++) sum += getDaysInMonth(year, j);
			return sum;
		} else {
			throw new RuntimeException("Invalid date " + day + " in " + day + "/" + month + "/" + year);
		}
	}

	/**
	 * 2000년 1월 1일 부터 지정한 년, 월, 일 까지의 날짜 수를 구한다.
	 * 2000년 1월 1일 이전의 경우에는 음수를 리턴한다.
	 * @param year
	 * @param month
	 * @param day
	 * @return
	 */
	public static int getDaysFrom21Century(int year, int month, int day) {
		if (year >= 2000) {
			int sum = getDaysFromYearFirst(year, month, day);
			for (int j = year - 1; j >= 2000; j--) sum += getDaysInYear(j);
			return sum - 1;
		} else if (year > 0 && year < 2000) {
			int sum = getDaysFromYearFirst(year, month, day);
			for (int j = 1999; j >= year; j--) sum -= getDaysInYear(year);
			return sum - 1;
		} else {
			throw new RuntimeException("Invalid year " + year + " in " + day + "/" + month + "/" + year);
		}
	}
	
	/**
	 * 지정한 년도의 지정한 월의 첫날 부터 지정한 날 까지의 날짜 수를 구한다.
	 * @param s
	 * @return
	 */
	public static int getDaysFromMonthFirst(String s) {
		int d, m, y;
		if (s.length() == 8) {
			y = Integer.parseInt(s.substring(0, 4));
			m = Integer.parseInt(s.substring(4, 6));
			d = Integer.parseInt(s.substring(6));
			return getDaysFromMonthFirst(y, m, d);
		} else if (s.length() == 10) {
			y = Integer.parseInt(s.substring(0, 4));
			m = Integer.parseInt(s.substring(5, 7));
			d = Integer.parseInt(s.substring(8));
			return getDaysFromMonthFirst(y, m, d);
		} else if (s.length() == 11) {
			d = Integer.parseInt(s.substring(0, 2));
			String strM = s.substring(3, 6).toUpperCase();
			m = 0;
			for (int j = 1; j <= 12; j++) {
				if (strM.equals(monthNames[j-1])) {
					m = j;
					break;
				}
			}
			if (m < 1 || m > 12) throw new RuntimeException("Invalid month name: " + strM + " in " + s);
			y = Integer.parseInt(s.substring(7));
			return getDaysFromMonthFirst(y, m, d);
		} else {
			throw new RuntimeException("Invalid date format: " + s);
		}
	}
	
	/**
	 * 지정한 년도의 첫날 부터 지정한 월의 지정한 날 까지의 날짜 수를 구한다.
	 * @param s
	 * @return
	 */
	public static int getDaysFromYearFirst(String s) {
		int d, m, y;
		if (s.length() == 8) {
			y = Integer.parseInt(s.substring(0, 4));
			m = Integer.parseInt(s.substring(4, 6));
			d = Integer.parseInt(s.substring(6));
			return getDaysFromYearFirst(y, m, d);
		} else if (s.length() == 10) {
			y = Integer.parseInt(s.substring(0, 4));
			m = Integer.parseInt(s.substring(5, 7));
			d = Integer.parseInt(s.substring(8));
			return getDaysFromYearFirst(y, m, d);
		} else if (s.length() == 11) {
			d = Integer.parseInt(s.substring(0, 2));
			String strM = s.substring(3, 6).toUpperCase();
			m = 0;
			for (int j = 1; j <= 12; j++) {
				if (strM.equals(monthNames[j-1])) {
					m = j;
					break;
				}
			}
			if (m < 1 || m > 12) throw new RuntimeException("Invalid month name: " + strM + " in " + s);
			y = Integer.parseInt(s.substring(7));
			return getDaysFromYearFirst(y, m, d);
		} else {
			throw new RuntimeException("Invalid date format: " + s);
		}
	}
	
	/**
	 * 2000년 1월 1일 부터 지정한 년, 월, 일 까지의 날짜 수를 구한다.
	 * 2000년 1월 1일 이전의 경우에는 음수를 리턴한다.
	 * @param s
	 * @return
	 */
	public static int getDaysFrom21Century(String s) {
	    int d, m, y;
	    if (s.length() == 8) {
	        y = Integer.parseInt(s.substring(0, 4));
	        m = Integer.parseInt(s.substring(4, 6));
	        d = Integer.parseInt(s.substring(6));
	        return getDaysFrom21Century(y, m, d);
	    } else if (s.length() == 10) {
	        y = Integer.parseInt(s.substring(0, 4));
	        m = Integer.parseInt(s.substring(5, 7));
	        d = Integer.parseInt(s.substring(8));
	        return getDaysFrom21Century(y, m, d);
	    } else if (s.length() == 11) {
	        d = Integer.parseInt(s.substring(0, 2));
	        String strM = s.substring(3, 6).toUpperCase();
	        m = 0;
	        for (int j = 1; j <= 12; j++) {
	            if (strM.equals(monthNames[j-1])) {
	                m = j;
	                break;
	            }
	        }
	        if (m < 1 || m > 12) throw new RuntimeException("Invalid month name: " + strM + " in " + s);
	        y = Integer.parseInt(s.substring(7));
	        return getDaysFrom21Century(y, m, d);
	    } else {
	    	throw new RuntimeException("Invalid date format: " + s);
	    }
	}

	/**
	 * (양 끝 제외) 날짜 차이 구하기
	 * @param s1
	 * @param s2
	 * @return
	 */
	public static int getDaysBetween(String s1, String s2) {
		int y1 = getDaysFrom21Century(s1);
		int y2 = getDaysFrom21Century(s2);
		return y1 - y2 - 1;
	}
	
	/**
	 * 날짜 차이 구하기
	 * @param s1
	 * @param s2
	 * @return
	 */
	public static int getDaysDiff(String s1, String s2) {
		int y1 = getDaysFrom21Century(s1);
		int y2 = getDaysFrom21Century(s2);
		return y1 - y2;
	}

	/**
	 * (양 끝 포함) 날짜 차이 구하기
	 * @param s1
	 * @param s2
	 * @return
	 */
	public static int getDaysFromTo(String s1, String s2) {
		int y1 = getDaysFrom21Century(s1);
		int y2 = getDaysFrom21Century(s2);
		return y1 - y2 + 1;
	}

	/**
	 * 지정한 년도, 지정한 월에 지정한 요일이 들어 있는 회수를 구한다.
	 * @param year
	 * @param month
	 * @param weekDay
	 * @return
	 */
	public static int getWeekdaysInMonth(int year, int month, int weekDay ) {
		int week = ((weekDay - 1) % 7);
		int days = getDaysInMonth(year, month);
		int sum = 6;   // 2000년 1월 1일은 토요일
		if (year >= 2000) {
			for (int i = 2000; i < year; i++) sum += getDaysInYear(i);
		} else {
			for (int i = year; i < 2000; i++) sum -= getDaysInYear(i);
		} 
		for (int i = 1; i < month; i++) sum += getDaysInMonth(year, i);


		int firstWeekDay = sum % 7;
		if (firstWeekDay < 0) firstWeekDay += 7;

		int n = firstWeekDay + days;
		int counter = (n / 7) + (((n % 7) > week) ? 1 : 0);
		if (firstWeekDay > week) counter--;

		return counter;
	}

	/**
	 * 지정한 년도의 지정한 달에 지정한 요일이 들어 있는 회수를 구한다.
	 * @param year
	 * @param month
	 * @param weekName
	 * @return
	 */
	public static int getWeekdaysInMonth(int year, int month, String weekName) {
		StringBuffer weekNames1 = new StringBuffer("일월화수목금토");
		StringBuffer weekNames2 = new StringBuffer("日月火水木金土");
		String[] weekNames3 = { "sun", "mon", "tue", "wed", "thu", "fri", "sat" };

		int n = weekNames1.indexOf(weekName);
		if (n < 0) n = weekNames2.indexOf(weekName);
		if (n < 0) {
			String str = weekName.toLowerCase();
			for (int i = 0; i < weekNames3.length; i++) {
				if (str.equals(weekNames3[i])) {
					n = i;
					break;
				}
			}
		}

		if (n < 0) throw new RuntimeException("Invalid week name: " + weekName);

		return getWeekdaysInMonth(year, month, n + 1);
	}

	/**
	 * 2000년 1월 1일 기준을 n일 경과한 날짜 구하기
	 * @param elapsed
	 * @return	yyyy-mm-dd 양식의 String 타입
	 */
	public static String getDateStringFrom21Century(int elapsed) {
		int y = 2000;
		int m = 1;
		int d = 1;

		int n = elapsed + 1;
		int j = 2000;
		if (n > 0) {
			while (n > getDaysInYear(j)) {
				n -= getDaysInYear(j); 
				j++;
			}
			y = j;

			int i = 1;
			while (n > getDaysInMonth(y, i)) {
				n -= getDaysInMonth(y, i); 
				i++;
			}
			m = i;
			d = n;
		} else if (n < 0) {
			while (n < 0) {
				n += getDaysInYear(j - 1); 
				j--;
			}
			y = j;

			int i = 1;
			while (n > getDaysInMonth(y, i)) {
				n -= getDaysInMonth(y, i); 
				i++;
			}
			m = i;
			d = n;
		}

		String strY = "" + y;
		String strM = "";
		String strD = "";

		if (m < 10) strM = "0" + m;
		else strM = "" + m;

		if (d < 10) strD = "0" + d;
		else strD = "" + d;

		return strY + "/" + strM + "/" + strD;
	}
  
	/**
	 * 지정한 날짜를 offset일 경과한 날짜 구하기
	 * @param year
	 * @param month
	 * @param day
	 * @param offset
	 * @return	yyyy-mm-dd 양식의 String 타입
	 */
	public static String addDate(int year, int month, int day, int offset) {
		int z = getDaysFrom21Century(year, month, day);
		int n = z + offset;
		return getDateStringFrom21Century(n);
	}
	
	/**
	 * 지정한 날짜를 offset일 경과한 날짜 구하기
	 * @param date
	 * @param offset
	 * @return	yyyy-mm-dd 양식의 String 타입
	 */
	public static String addDate(String date, int offset) {
		int z = getDaysFrom21Century(date);
		int n = z + offset;
		return getDateStringFrom21Century(n);
	}
    
	/**
	 * 지정한 날짜를 offset일 경과한 날짜 구하기
	 * @param date
	 * @param offset
	 * @return	yyyy-mm-dd 양식의 String 타입
	 */
	public static String offDate(String date, int offset) {
		int z = getDaysFrom21Century(date);
		int n = z - offset;
		return getDateStringFrom21Century(n);
	}
	
    public static Timestamp getOneMonthBefore(int offset){
        Timestamp stamp = null;
        String today = getDateString(getCurrentTimestamp(), new SimpleDateFormat("yyyy/MM/dd"));
//        ("today : " + today);
        int z = getDaysFrom21Century(today);
        int n = z - offset;
        String oneMonthBefore = getDateStringFrom21Century(n);
//        ("oneMonthBefore : " + oneMonthBefore);
        stamp = convertDate(oneMonthBefore);
        return stamp;
    }
    
    public static int getDuration(Date date1, Date date2){
    	Date pre = null;
    	Date after = null;
    	//date1.before(date2);
    	if(date1==null || date2==null){
    		return 0;
    	}
    	
    	pre = date1;
		after = date2;
		/*
    	if(date1.getTime() < date2.getTime()){
    		pre = date1;
    		after = date2;
    	}else{
    		after =date1;
    		pre =date2;
    	}*/
    	
    	long oneDayMillis = 24*60*60*1000;
    	int day = (int)((after.getTime() - pre.getTime())/oneDayMillis);
    	return day;
    }
    
    /**
     * 오늘날짜의 년을 반환한다.
     *
     * @return 오늘날짜의 년(yyyy)을 나타내는 문자열
     */
    public static String getYear()
    {
        SimpleDateFormat dateForm = new SimpleDateFormat("yyyy");
        return dateForm.format(new Date());
    }

    /**
     * 오늘날짜의 월을 반환한다.
     *
     * @return 오늘날짜의 월(MM)을 나타내는 문자열
     */
    public static String getMonth()
    {
        SimpleDateFormat dateForm = new SimpleDateFormat("MM");
        return dateForm.format(new Date());
    }
    
    public static String getTodayDate(String format) {
        
        String result = "";
        //20110413130543
        //20110413131626
        
        Calendar calendar = Calendar.getInstance();
        TimeZone timezone = TimeZone.getTimeZone("Asia/Seoul");
        calendar.setTimeZone(timezone);
//         ("현재 타임존 : " + calendar.getTimeZone().getDisplayName() );
//         ("현재 타임존 : " + calendar );
        
        java.util.Date dateNow = calendar.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        formatter.setTimeZone(timezone);
        
        //yyyyMMddHHmmss

        result = formatter.format(dateNow);
        
        return result;
    }
    
//시간 더하기
    public static String getPlusHour(String createDate, int plus){
    	
    	StringTokenizer stk = new StringTokenizer(createDate, " ");
    	int[] day = new int[3];
    	int[] time = new int[3];
    	String pm = "오전";
      	String part1 = stk.nextToken();
      	String part2 = stk.nextToken();
      	
      	stk = new StringTokenizer(part1, "/");
      	day[0] = Integer.parseInt(stk.nextToken());
      	day[1] = Integer.parseInt(stk.nextToken());
      	day[2] = Integer.parseInt(stk.nextToken());
      	
      	stk = new StringTokenizer(part2, ":");
      	time[0] = Integer.parseInt(stk.nextToken());
      	time[1] = Integer.parseInt(stk.nextToken());
      	time[2] = Integer.parseInt(stk.nextToken());
		
      	if((time[0] + plus) >= 12 && (time[0] + plus) < 24){
      		pm = "오후";
      	}
      	
        Calendar cal = Calendar.getInstance();
        cal.set(day[0], day[1], day[2], time[0], time[1], time[2]);
        cal.add(Calendar.HOUR_OF_DAY, + plus);  
        
        StringBuffer sb = new StringBuffer();
        sb.append(cal.get(Calendar.YEAR));
        sb.append("/");
        if(cal.get(Calendar.MONTH) < 10)
        	sb.append("0");
        sb.append(cal.get(Calendar.MONTH));  
        sb.append("/");
        if(cal.get(Calendar.DATE) < 10)
        	sb.append("0");
        sb.append(cal.get(Calendar.DATE));
        sb.append("&nbsp;");
        sb.append(pm);
        if(cal.get(Calendar.HOUR) < 10  && cal.get(Calendar.HOUR) != 00 && cal.get(Calendar.HOUR) != 0)
        	sb.append("0");
        if(cal.get(Calendar.HOUR) == 00){
        	sb.append("12");
        }else{
        	sb.append(cal.get(Calendar.HOUR));
        }
        sb.append(":");
        if(cal.get(Calendar.MINUTE) < 10)
        	sb.append("0");
        sb.append(cal.get(Calendar.MINUTE));  
        sb.append(":");
        if(cal.get(Calendar.SECOND) < 10)
        	sb.append("0");
        sb.append(cal.get(Calendar.SECOND));
    	
        return sb.toString();
    }
    
    
    public static Timestamp convertStartDate(String timeString, String format) {

        if (timeString == null || timeString.length() == 0) {
           return null;
        }
        
        Date date = new Date();
		try {
			date = getTimestampFormat(timeString, format);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
        return convertStartDate(date);

     }
    
    
    /**
     * @Desc :
     * @Method Name : convertEndDate
     * @param String timeString
     * @param SimpleDateFormat format
     * @return Timestamp
     */
    public static Timestamp convertEndDate(String timeString, String format) {
       if (timeString == null || timeString.length() == 0) {
          return null;
       }
       
        Date date = new Date();
		try {
			date = getTimestampFormat(timeString, format);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
       return convertEndDate(date);
    }
    
    
    /**
     * @Desc :
     * @Method Name : convertStartDate
     * @param Date date
     * @return Timestamp
     */
    public static Timestamp convertStartDate(Date date) {
       Calendar cal = Calendar.getInstance();
       cal.setTime(date);

       // Set time fields to zero
       cal.set(Calendar.HOUR_OF_DAY, 0);
       cal.set(Calendar.MINUTE, 0);
       cal.set(Calendar.SECOND, 0);
       cal.set(Calendar.MILLISECOND, 0);

       // Put it back in the Date object

       Timestamp stamp = new Timestamp(cal.getTimeInMillis());
       return stamp;
    }
    
    /**
     * @Desc :
     * @Method Name : convertEndDate
     * @param Date date
     * @return Timestamp
     */
    public static Timestamp convertEndDate(Date date) {
       Calendar cal = Calendar.getInstance();

       cal.setTime(date);

       // Set time fields to zero
       cal.set(Calendar.HOUR_OF_DAY, 23);
       cal.set(Calendar.MINUTE, 59);
       cal.set(Calendar.SECOND, 59);
       cal.set(Calendar.MILLISECOND, 999);

       // Put it back in the Date object

       Timestamp stamp = new Timestamp(cal.getTimeInMillis());
       return stamp;
    }
	
	/**
	    * 
	    * <pre>
	    * @desc  : 기간으로 계획종료일 조정
	    * @author : hwang
	    * @date   : 2015. 5. 22.오전 11:32:20
	    * </pre>
	    * @method : getPlanEndDate
	    * @param start
	    * @param duration
	    * @return
	    */
	   public static Timestamp getPlanEndDate(Timestamp start, int duration) throws Exception{
	         
		   Calendar eCal = Calendar.getInstance();
		   eCal.setTime(start);
		   eCal.add(Calendar.DATE, duration - 1);

 
 /*HoliDayUtil util = HoliDayUtil.getInstance();*/
 
		   Calendar sCal = Calendar.getInstance();
		   sCal.setTime(start);
		   while (!sCal.after(eCal)) {
			/*
			 * if (ProjectUtil.isHoliday(sCal)) { eCal.add(Calendar.DATE, 1); }
			 */
             sCal.add(Calendar.DATE, 1);
		   }
         
		   return convertEndDate(eCal.getTime());
	   }
	   
	   
	   public static int getHoliDayDuration(Timestamp start, Timestamp end) throws Exception{
		   return getHoliDayDuration(start, end, 0);
	   }
	   
	   /**
	    * 
	    * <pre>
	    * @desc  : 
	    * @author : hwang
	    * @date   : 2015. 5. 18.?�후 8:48:11
	    * </pre>
	    * @method : getDuration
	    * @param start
	    * @param end
	    * @param gap
	    * @return
	    */
	   public static int getHoliDayDuration(Timestamp start, Timestamp end, int gap) throws Exception{ 
	      
		  int day = 0;
	      
	      Calendar sCal = Calendar.getInstance();
	      if(start != null){
	         sCal.setTime(start);
	      }

	      if(end == null){
	         end = add(start, gap);
	      }
	      
	      Calendar eCal = Calendar.getInstance();
	      eCal.setTime(end);

	      while (!sCal.after(eCal)) {
			/*
			 * if (!ProjectUtil.isHoliday(sCal)) { day++; }
			 */
	         sCal.add(Calendar.DATE, 1);
	      }


	      return day;
	   }
		   
	   /**
	    * @Desc :
	    * @Method Name : add
	    * @param Timestamp start
	    * @param int day
	    * @return Timestamp
	    */
	   public static Timestamp add(Timestamp start, int day) {
	      return add(start, day, false);
	   }
	   
	   /**
	    * 
	    * <pre>
	    * @desc  : 
	    * @author : hwang
	    * @date   : 2015. 9. 24.오후 5:23:36
	    * </pre>
	    * @method : add
	    * @param start
	    * @param day
	    * @return
	    */
	   public static Timestamp add(Timestamp start, int day, boolean isPjt) {
	      Calendar sCal = Calendar.getInstance();
	      sCal.setTime(start);

	      Calendar eCal = Calendar.getInstance();
	      eCal.setTime(start);
	      eCal.add(Calendar.DATE, day - 1);

//		      int weekDay = 0;
	      while (!sCal.after(eCal)) {
	         if (isPjt && sCal.get(Calendar.DAY_OF_WEEK) != 1 && sCal.get(Calendar.DAY_OF_WEEK) != 2) {
	            eCal.add(Calendar.DATE, 1);
	         }
	         sCal.add(Calendar.DATE, 1);
	      }

	      sCal.add(Calendar.DATE, 1);
	      
	      return convertEndDate(eCal.getTime());

	   }
	   
	   
	   public static Timestamp getDelayTime(Timestamp time, int delayDay) {
	      long oneDay = 86400000L;

	      long delayTimelong = time.getTime() + delayDay * oneDay;

	      Timestamp delayTime = new Timestamp(delayTimelong);

	      return delayTime;
	   }

		public static String getStringValue(Timestamp time) {
			return getStringValue(time, 10);
		}

		public static String getStringValue(Timestamp time, int end) {
			if (time == null) {
				return null;
			}
			return time.toString().substring(0, end);
		}

		public static Timestamp getToDayTimestamp() throws Exception{
			String format = "yyyy-MM-dd HH:mm:ss:SS";
			String today = DateUtil.getTodayDate(format);
			return DateUtil.getTimestampFormat(today, format);
		}
	  
		// 이전 날짜 구하기		beforeDay = -1 : 하루 전날
		public static String getBeforeDate(int beforeDay){
			Calendar day = Calendar.getInstance();
			day.add(Calendar.DATE , beforeDay);	
			String beforeDate = new SimpleDateFormat("yyyy-MM-dd").format(day.getTime());
			return beforeDate;
		}
		
		public static Timestamp convertKORDate(Date date) {
	       Calendar cal = Calendar.getInstance();
	       cal.setTime(date);

	       // Set time fields to zero
	       cal.set(Calendar.HOUR_OF_DAY, 9);
	       cal.set(Calendar.MINUTE, 0);
	       cal.set(Calendar.SECOND, 0);
	       cal.set(Calendar.MILLISECOND, 0);

	       // Put it back in the Date object

	       Timestamp stamp = new Timestamp(cal.getTimeInMillis());
	       return stamp;
	    }
		
		public static void main(String[] args) {
			
			TimeZone tz = DateUtil.getTimeZone();
			LOGGER.info(String.valueOf(tz.getRawOffset()));
	    	
			String str = "2020-10-14T15:00:00.000Z";
			//String str = "2020/10/14";
			DateTime dt = new DateTime(str);
			Timestamp stamp = new Timestamp(dt.getMillis());
			LOGGER.info(String.valueOf(stamp));
			//LOGGER.info(defaultDateFormat.parse ( str , new ParsePosition ( 0 ) ));
		}
		
		
		
		
		
		
		
		
		
}

