package com.closedevice.fastapp.util;

import org.joda.time.DateTime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtil {
    public final static String PATTERN = "yyyy-MM-dd HH:mm:ss";
    public final static String DEF_FORMAT = "yyyy-MM-dd HH:mm";
    public final static String YEAR_MONTH_DAY = "yyyy-MM-dd";
    public final static String YESTODAY = "昨天";
    public final static String BEFORE_YESTODAY = "前天";
    public final static String WEEK_AGO = "一周前";
    public final static String MONTH_AGO = "一月前";

    private final static ThreadLocal<SimpleDateFormat> DATE_FORMATER = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    private final static ThreadLocal<SimpleDateFormat> DATE_FORMATER_SHORT = new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };


    public static String getNow(String format) {
        if (null == format || "".equals(format)) {
            format = PATTERN;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        String date = sdf.format(new Date());
        return date;
    }

    public static boolean isToday(long time) {
        return getNow(YEAR_MONTH_DAY).equals(getDate(time, YEAR_MONTH_DAY));
    }


    public static Date stringToDate(String sdate, String format) {
        if (null == format || "".equals(format)) {
            format = PATTERN;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = sdf.parse(sdate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String dateToString(Date date, String format) {
        if (null == format || "".equals(format)) {
            format = PATTERN;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format);

        return sdf.format(date);
    }

    public static String dateToString(Date date, String format, Locale locale) {
        if (null == format || "".equals(format)) {
            format = PATTERN;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(format, locale);

        return sdf.format(date);
    }

    public static Date longToDate(long lDate) {
        Date date = new Date(lDate);
        return date;
    }

    public static Date cstToDate(String dateStr) {
        DateFormat df = new SimpleDateFormat(
                "EEE MMM dd HH:mm:ss '+0800' yyyy", Locale.CHINA);
        Date date = null;
        try {
            date = df.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String getDate(String sdate, String format) {
        return getDate(toDate(sdate).getTime(), format);
    }


    public static String getDate(long times, String format) {
        if (times == 0)
            return "";
        Date date = longToDate(times);
        return dateToString(date, format);
    }

    public static Date getDate(int year, int month, int weekInMonth,
                               int dayInWeek) {
        Calendar date = Calendar.getInstance();
        date.clear();
        date.set(Calendar.YEAR, year);
        date.set(Calendar.MONTH, month - 1);
        date.set(Calendar.DAY_OF_WEEK_IN_MONTH, weekInMonth);
        date.set(Calendar.DAY_OF_WEEK, dayInWeek + 1);
        return date.getTime();
    }

    public static Date getDate(int month, int weekInMonth, int dayInWeek) {
        Calendar date = Calendar.getInstance();
        int year = date.get(Calendar.YEAR);
        date.clear();
        date.set(Calendar.YEAR, year);
        date.set(Calendar.MONTH, month - 1);
        date.set(Calendar.DAY_OF_WEEK_IN_MONTH, weekInMonth);
        date.set(Calendar.DAY_OF_WEEK, dayInWeek + 1);
        return date.getTime();
    }

    public static String getDate(int month, int weekInMonth, int dayInWeek,
                                 String format) {
        Date date = getDate(month, weekInMonth, dayInWeek);
        return getDate(date.getTime(), format);
    }

    public final static long ONE_MINUTE = 1000 * 60;
    public final static long ONE_HOUR = ONE_MINUTE * 60;
    public final static long ONE_DAY = ONE_HOUR * 24;

    public static String getFormatTime(String sdate) {
        Date time = null;
        if (TimeZoneUtil.isInEasternEightZones()) {
            time = toDate(sdate);
        } else {
            time = TimeZoneUtil.transformTime(toDate(sdate),
                    TimeZone.getTimeZone("GMT+08"), TimeZone.getDefault());
        }

        if (time == null)
            return "unkonwn";

        return getFormatTime(time.getTime());
    }

    public static long getDate(String date) {
        return toDate(date).getTime();
    }

    private static Date toDate(String sdate) {
        try {
            return DATE_FORMATER.get().parse(sdate);
        } catch (ParseException e) {
            return new Date();
        }
    }

    public static String getFormatDate(String sdate, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(sdate);
    }

    public static String getFormatTime(long time) {
        long current = System.currentTimeMillis();
        long inteval = current - time;
        if (isToday(time)) {
            if (inteval < ONE_MINUTE) {
                return "刚刚";
            } else if (inteval < ONE_HOUR) {
                return inteval / ONE_MINUTE + "分钟前";
            } else if (inteval < ONE_DAY) {
                return inteval / ONE_HOUR + "小时前";
            }
            return "";
        } else {
            if (isTheSameYear(time)) {
                if (isYestoday(time)) {
                    return YESTODAY;
                } else if (isTheDayBeforeYestoday(time)) {
                    return BEFORE_YESTODAY;
                } else if (isWeekAgo(time)) {
                    return WEEK_AGO;
                } else if (isMonthAgo(time)) {
                    return MONTH_AGO;
                } else {
                    return getDate(time, "MM/dd");
                }
            } else {
                return getDate(time, "yyyy/MM/dd");
            }
        }
    }

    private static boolean isYestoday(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        cal.add(Calendar.DAY_OF_YEAR, 1);
        return isToday(cal.getTimeInMillis());
    }

    private static boolean isTheDayBeforeYestoday(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        cal.add(Calendar.DAY_OF_YEAR, 2);
        return isToday(cal.getTimeInMillis());
    }


    public static boolean isTheDayAfterDay(long serverTime, long startTime) {
        Calendar serCal = Calendar.getInstance();
        Calendar actCal = Calendar.getInstance();

        serCal.setTimeInMillis(serverTime);
        actCal.setTimeInMillis(startTime);

        int serDay = serCal.get(Calendar.DAY_OF_YEAR);
        int actDay = actCal.get(Calendar.DAY_OF_YEAR);
        int durationDay = (serDay - actDay);
        if (durationDay == 1) {
            int serHour = serCal.get(Calendar.HOUR_OF_DAY);
            return serHour >= 12;
        } else if (durationDay > 1) {
            return true;
        }
        return false;


    }


    private static boolean isWeekAgo(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        cal.add(Calendar.DAY_OF_YEAR, 6);
        return isToday(cal.getTimeInMillis());
    }

    private static boolean isMonthAgo(long time) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        cal.add(Calendar.DAY_OF_YEAR, 30);
        return isToday(cal.getTimeInMillis());
    }


    private static boolean isTheSameYear(long time) {
        DateTime dt = new DateTime(time);
        DateTime now = new DateTime();
        return dt.getYear() == now.getYear();
    }

    public static String getIntervalHours(long begin, long end) {
        if (end < begin)
            return "";
        long between = (end - begin) / 1000;
        long day = between / (24 * 3600);
        long hour = between % (24 * 3600) / 3600;
        long minute = between % 3600 / 60;
        long second = between % 60 / 60;
        return (day == 0 ? "" : day + "天 ") + (hour == 0 ? "" : hour + "小时 ")
                + (minute == 0 ? "" : minute + "分 ")
                + (second == 0 ? "" : second + "秒");
    }

    public static long getHours(long finishTime, long endTime) {
        long time = finishTime - endTime;
        if (time > 0) {
            return time / (3600 * 1000);
        }
        return 0;
    }

    public static boolean isSameTimeWithoutSecond(long time, long time2) {
        return getDate(time, DEF_FORMAT).equals(
                getDate(time2, DEF_FORMAT));
    }

    public static long getDurationMinutes(long begin, long end) {
        if (end < begin)
            return 0;
        long between = (end - begin) / 1000;
        long minutes = between / 60;
        return minutes;
    }

    public static String getDurationMinutes(long minutes) {
        if (minutes <= 0)
            return "";
        long day = minutes / (24 * 60);
        long hour = minutes % (24 * 60) / 60;
        long minute = minutes % 60;
        StringBuilder buf = new StringBuilder();
        buf.append((day == 0 ? "" : day + "天 "));
        buf.append((hour == 0 ? "" : hour + "小时 "));
        buf.append((minute == 0 ? "" : minute + "分 "));
        return buf.toString();
    }

    public static String[] months() {
        List<String> keys = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat matterZn = new SimpleDateFormat("yyyy年MM月");
        int currentMonth = cal.get(Calendar.MONTH);
        for (int i = 0; i <= currentMonth; i++) {
            cal.add(Calendar.MONTH, -i);
            String s1 = matterZn.format(cal.getTime());
            keys.add(s1);
            cal = Calendar.getInstance();
        }

        return keys.toArray(new String[]{});

    }

}
