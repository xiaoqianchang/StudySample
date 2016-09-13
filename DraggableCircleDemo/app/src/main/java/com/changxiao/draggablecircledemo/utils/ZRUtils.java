package com.changxiao.draggablecircledemo.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;

import com.zritc.colorfulfund.base.ZRApplication;

import org.apache.http.impl.cookie.DateParseException;
import org.apache.http.impl.cookie.DateUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.UUID;

/**
 * Normal utils used for application.
 *
 * @author gufei
 * @version 1.0
 * @createDate 2015-11-03
 * @lastUpdate 2016-01-09
 */
public class ZRUtils {
    /**
     * yyyyMMddHHmmss
     */
    public static final String TIME_FORMAT = "yyyyMMddHHmmss";
    /**
     * yyyy-MM-dd HH:mm:ss
     */
    public static final String TIME_FORMAT2 = "yyyy-MM-dd HH:mm:ss";
    /**
     * yyyy-MM-dd
     */
    public static final String TIME_FORMAT3 = "yyyy-MM-dd";
    /**
     * yyyyMMdd
     */
    public static final String TIME_FORMAT4 = "yyyyMMdd";
    /**
     * yyyy-MM-dd HH:mm
     */
    public static final String TIME_FORMAT5 = "yyyy-MM-dd HH:mm";
    /**
     * yyyyMM
     */
    public static final String TIME_FORMAT6 = "yyyyMM";
    /**
     * MM-dd
     */
    public static final String TIME_FORMAT7 = "MM-dd";
    /**
     * HH:mm
     */
    public static final String TIME_FORMAT8 = "HH:mm";
    /**
     * HH:mm:ss
     */
    public static final String TIME_FORMAT9 = "HH:mm:ss";
    /**
     * MM-dd HH:mm
     */
    public static final String TIME_FORMAT10 = "MM-dd HH:mm";
    /**
     * yy-MM-dd HH:mm
     */
    public static final String TIME_FORMAT11 = "yy-MM-dd HH:mm";
    /**
     * mm:ss
     */
    public static final String TIME_FORMAT12 = "mm:ss";
    /**
     * MM-dd HH:mm
     */
    public static final String TIME_FORMAT13 = "MM月dd日 HH:mm";
    /**
     * MM月dd日
     */
    public static final String TIME_FORMAT14 = "MM月dd日";

    public static long getCurrentLongTime() {
        return System.currentTimeMillis();
    }

    public static String getCurrentTime(String format) {
        return formatTime(System.currentTimeMillis(), format);
    }

    public static String formatTimeWithOutTimeZoneFixed(long time, String format) {
        return DateUtils.formatDate(new Date(time), format);
    }

    public static long getTimeWithOutTimeZoneFixed(String time, String format) {
        try {
            return DateUtils.parseDate(time, new String[]{format}).getTime();
        } catch (DateParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * transferStringDateToLong
     * default formatDate {yyyy-MM-dd hh:mm:ss}
     *
     * @param date {2016-06-15 08:05:18}
     * @return
     * @throws ParseException
     */
    public static long transferStringDateToLong(String date) {
        try {
            if (TextUtils.isEmpty(date))
                return 0l;
            return transferStringDateToLong(TIME_FORMAT2, date);
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0l;
        }
    }

    /**
     * transferStringDateToLong
     *
     * @param formatDate{yyyy-MM-dd hh:mm:ss}
     * @param date                  {2016-06-15 08:05:18}
     * @return
     * @throws ParseException
     */
    public static long transferStringDateToLong(String formatDate, String date) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(formatDate);
            Date dt = sdf.parse(date);
            return dt.getTime();
        } catch (ParseException ex) {
            ex.printStackTrace();
            return 0l;
        }
    }

    public static String formatTime(long time, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.CHINA);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(time);
        return dateFormat.format(calendar.getTime());
    }

    public static long getTime(String time, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.CHINA);
        try {
            Date date = dateFormat.parse(time);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            return calendar.getTimeInMillis();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String formatDate(String srcFormat, String destFormat,
                                    String dateString) {
        SimpleDateFormat src = new SimpleDateFormat(srcFormat, Locale.CHINA);
        SimpleDateFormat dest = new SimpleDateFormat(destFormat, Locale.CHINA);
        try {
            return dest.format(src.parse(dateString));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param time
	 *            {Aug 28, 2014 9:36:37 AM}
	 * @param format
	 *            {yyyy-MM-dd HH:mm:ss}
     * @return
     */
    public static String formatDate2(String time, String format) {
        try {
            if (null == time || "null".equals(time) || "".equals(time)) {
                return "";
            }
            SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy K:m:s a",
                    Locale.ENGLISH);
            Date d2 = sdf.parse(time);
            SimpleDateFormat sdf1 = new SimpleDateFormat(
                    "EEE MMM dd hh:mm:ss z yyyy", Locale.ENGLISH);
            SimpleDateFormat sdf2 = new SimpleDateFormat(format, Locale.CHINA);
            return sdf2.format(sdf1.parse(d2.toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String formatDate3(String time, String format) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            Date date = sdf.parse(time);
            return sdf.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public final static java.sql.Date string2Date(String dateString)
            throws Exception {
        java.sql.Date dateTime;
        try {
            DateFormat dateFormat;
            dateFormat = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss",
                    Locale.ENGLISH);
            dateFormat.setLenient(false);
            Date timeDate = dateFormat.parse(dateString);// util类型
            dateTime = new java.sql.Date(timeDate.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return dateTime;
    }

    /**
     * yyyy-mm-dd hh:mm
     *
     * @param date
     * @return
     */
    public final static String formatMyDate(String date) {
        try {
            String yy = date.split("-")[0];
            String mm = date.split("-")[1];
            String temp = date.split("-")[2];
            String dd = temp.substring(0, 2);
            String hr = temp.substring(3, 8);
            return yy + "年" + mm + "月" + dd + "日 " + hr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 判断某个时间是否在一个指定时间区间内
     *
     * @param strDate
     * @param strDateBegin
     * @param strDateEnd
     * @return
     */
    public static boolean isInDates(String strDate, String strDateBegin,
                                    String strDateEnd) {
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                Locale.CHINA);
        Date myDate = null;
        Date dateBegin = null;
        Date dateEnd = null;
        try {
            myDate = sd.parse(strDate);
            dateBegin = sd.parse(strDateBegin);
            dateEnd = sd.parse(strDateEnd);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        strDate = String.valueOf(myDate);
        strDateBegin = String.valueOf(dateBegin);
        strDateEnd = String.valueOf(dateEnd);

        int strDateH = Integer.parseInt(strDate.substring(11, 13));
        int strDateM = Integer.parseInt(strDate.substring(14, 16));
        int strDateS = Integer.parseInt(strDate.substring(17, 19));

        int strDateBeginH = Integer.parseInt(strDateBegin.substring(11, 13));
        int strDateBeginM = Integer.parseInt(strDateBegin.substring(14, 16));
        int strDateBeginS = Integer.parseInt(strDateBegin.substring(17, 19));

        int strDateEndH = Integer.parseInt(strDateEnd.substring(11, 13));
        int strDateEndM = Integer.parseInt(strDateEnd.substring(14, 16));
        int strDateEndS = Integer.parseInt(strDateEnd.substring(17, 19));

        if ((strDateH >= strDateBeginH && strDateH <= strDateEndH)) {
            if (strDateH > strDateBeginH && strDateH < strDateEndH) {
                return true;
            } else if (strDateH == strDateBeginH && strDateM > strDateBeginM
                    && strDateH < strDateEndH) {
                return true;
            } else if (strDateH == strDateBeginH && strDateM == strDateBeginM
                    && strDateS > strDateBeginS && strDateH < strDateEndH) {
                return true;
            } else if (strDateH == strDateBeginH && strDateM == strDateBeginM
                    && strDateS == strDateBeginS && strDateH < strDateEndH) {
                return true;
            } else if (strDateH > strDateBeginH && strDateH == strDateEndH
                    && strDateM < strDateEndM) {
                return true;
            } else if (strDateH > strDateBeginH && strDateH == strDateEndH
                    && strDateM == strDateEndM && strDateS < strDateEndS) {
                return true;
            } else if (strDateH > strDateBeginH && strDateH == strDateEndH
                    && strDateM == strDateEndM && strDateS == strDateEndS) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 实现留言时间(几秒前，几分钟前，几小时前....)
     *
     * @param time
     * @return
     */
    public static String calTimePast(String time) {
        try {
            SimpleDateFormat df = new SimpleDateFormat(TIME_FORMAT2);
            Date now = df.parse(ZRUtils.getCurrentTime(TIME_FORMAT2));
            Date date = df.parse(time);
            long l = now.getTime() - date.getTime();
            long day = l / (24 * 60 * 60 * 1000);
            long hour = (l / (60 * 60 * 1000) - day * 24);
            long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
            long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);

            SimpleDateFormat sdf = new SimpleDateFormat(TIME_FORMAT2);
            long create = sdf.parse(time).getTime();
            Calendar nowCal = Calendar.getInstance();
            long ms = 1000 * (nowCal.get(Calendar.HOUR_OF_DAY) * 3600
                    + nowCal.get(Calendar.MINUTE) * 60 + nowCal
                    .get(Calendar.SECOND));// 毫秒数
            long msNow = nowCal.getTimeInMillis();
            StringBuffer sb = new StringBuffer();
            if (msNow - create < ms) {
                if (hour > 0)
                    sb.append("今天" + time.substring(10, 16));
                else if (min > 0 && hour == 0)
                    sb.append(min + "分钟前");
                else if (s > 5 && hour == 0 && min < 60)
                    sb.append(s + "秒前");
                else if (s <= 5) {
                    sb.append("刚刚更新");
                } else
                    sb.append(time);
            } else {
                sb.append(time.substring(5, time.length() - 3));
            }
            return sb.toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }


    /***********************************IM聊天时间*************************************************/
    /**
     * 5分钟的差值
     */
    private static final long INTERVAL_IN_MILLISECONDS = 5 * 60 * 1000;

    public static String getTimestampString_(Date messageDate) {

        boolean isChinese = true;
        String format = null;
        long messageTime = messageDate.getTime();
        if (isSameDay(messageTime)) { // 今天
            Calendar calendar = GregorianCalendar.getInstance();
            calendar.setTime(messageDate);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);

            format = "HH:mm";
        } else {
            if (isChinese) {
                format = "yyyy/MM/dd";
            } else {
                format = "yyyy-MM-dd";
            }
        }

        if (isChinese) {
            return new SimpleDateFormat(format, Locale.CHINA).format(messageDate);
        } else {
            return new SimpleDateFormat(format, Locale.US).format(messageDate);
        }
    }

    /**
     * ChatMessageAdapter time
     *
     * @param messageDate
     * @return
     */
    public static String getTimestampString(Date messageDate) {

        boolean isChinese = true;
        String format = null;
        long messageTime = messageDate.getTime();
        if (isSameDay(messageTime)) { // 今天
            Calendar calendar = GregorianCalendar.getInstance();
            calendar.setTime(messageDate);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);

            format = "HH:mm";

//            if (hour > 17) {
//                if (isChinese) {
//                    format = "晚上 hh:mm";
//                }
//
//            } else if (hour >= 0 && hour <= 6) {
//                if (isChinese) {
//                    format = "凌晨 hh:mm";
//                }
//            } else if (hour > 11 && hour <= 17) {
//                if (isChinese) {
//                    format = "下午 hh:mm";
//                }
//
//            } else {
//                if (isChinese) {
//                    format = "上午 hh:mm";
//                }
//            }
        }
//        else if (isYesterday(messageTime)) {
//            if (isChinese) {
//                format = "昨天 HH:mm";
//            } else {
//                format = "MM-dd HH:mm";
//            }
//
//        }
        else {
            if (isChinese) {
                format = "yyyy/MM/dd HH:mm";
            } else {
                format = "yyyy-MM-dd HH:mm";
            }
        }

        if (isChinese) {
            return new SimpleDateFormat(format, Locale.CHINA).format(messageDate);
        } else {
            return new SimpleDateFormat(format, Locale.US).format(messageDate);
        }
    }

    /**
     * yyyy-MM-dd
     *
     * @param messageDate
     * @return
     */
    public static String getTimestampStringYear(Date messageDate) {

        boolean isChinese = true;
        String format = null;
        long messageTime = messageDate.getTime();
        if (isSameDay(messageTime)) { // 今天
            Calendar calendar = GregorianCalendar.getInstance();
            calendar.setTime(messageDate);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);

            format = "HH:mm";

            if (isChinese) {
                format = "今天 hh:mm";
            }
        }
        else if (isYesterday(messageTime)) {
            if (isChinese) {
                format = "昨天 HH:mm";
            } else {
                format = "MM-dd HH:mm";
            }

        }
        else {
            if (isChinese) {
                format = "yyyy-MM-dd";
            } else {
                format = "yyyy-MM-dd HH:mm";
            }
        }

        if (isChinese) {
            return new SimpleDateFormat(format, Locale.CHINA).format(messageDate);
        } else {
            return new SimpleDateFormat(format, Locale.US).format(messageDate);
        }
    }

    /**
	 * MM-dd HH:mm
	 *
	 * @param messageDate
	 * @return
	 */
	public static String getTimestampStringForMonth(Date messageDate) {

		boolean isChinese = true;
		String format = null;
		long messageTime = messageDate.getTime();
		if (isSameDay(messageTime)) { // 今天
			Calendar calendar = GregorianCalendar.getInstance();
			calendar.setTime(messageDate);
			int hour = calendar.get(Calendar.HOUR_OF_DAY);

			format = "HH:mm";

			if (isChinese) {
				format = "今天 hh:mm";
			}
		}
		else if (isYesterday(messageTime)) {
			if (isChinese) {
				format = "昨天 HH:mm";
			} else {
				format = "MM-dd HH:mm";
			}

		}
		else {
			if (isChinese) {
				format = "MM-dd HH:mm";
			} else {
				format = "yyyy-MM-dd HH:mm";
			}
		}

		if (isChinese) {
			return new SimpleDateFormat(format, Locale.CHINA).format(messageDate);
		} else {
			return new SimpleDateFormat(format, Locale.US).format(messageDate);
		}
	}

	/**
	 * MM-dd HH:mm
	 *
	 * @param messageDate
	 * @return
	 */
	public static String getTimestampStringForDetail(Date messageDate, String str) {

		boolean isChinese = true;
		String format = null;
		long messageTime = messageDate.getTime();
		if (isSameDay(messageTime)) { // 今天
			Calendar calendar = GregorianCalendar.getInstance();
			calendar.setTime(messageDate);
			int hour = calendar.get(Calendar.HOUR_OF_DAY);

			format = "HH:mm";

			if (isChinese) {
				format = "今天 hh:mm " + str;
			}
		}
		else if (isYesterday(messageTime)) {
			if (isChinese) {
				format = "昨天 HH:mm " + str;
			} else {
				format = "MM-dd HH:mm";
			}

		}
		else {
			if (isChinese) {
				format = "yyyy-MM-dd " + str;
			} else {
				format = "yyyy-MM-dd HH:mm";
			}
		}

		if (isChinese) {
			return new SimpleDateFormat(format, Locale.CHINA).format(messageDate);
		} else {
			return new SimpleDateFormat(format, Locale.US).format(messageDate);
		}
	}

    /**
     * 计算时间差
     *
     * @param time1
     * @param time2
     * @return
     */
    public static boolean isCloseEnough(long time1, long time2) {
        long delta = time1 - time2;
        if (delta < 0) {
            delta = -delta;
        }
        return delta < INTERVAL_IN_MILLISECONDS;
    }

    private static boolean isSameDay(long inputTime) {

        TimeInfo tStartAndEndTime = getTodayStartAndEndTime();
        if (inputTime > tStartAndEndTime.getStartTime() && inputTime < tStartAndEndTime.getEndTime())
            return true;
        return false;
    }

    private static boolean isYesterday(long inputTime) {
        TimeInfo yStartAndEndTime = getYesterdayStartAndEndTime();
        if (inputTime > yStartAndEndTime.getStartTime() && inputTime < yStartAndEndTime.getEndTime())
            return true;
        return false;
    }

    public static TimeInfo getYesterdayStartAndEndTime() {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.add(Calendar.DATE, -1);
        calendar1.set(Calendar.HOUR_OF_DAY, 0);
        calendar1.set(Calendar.MINUTE, 0);
        calendar1.set(Calendar.SECOND, 0);
        calendar1.set(Calendar.MILLISECOND, 0);

        Date startDate = calendar1.getTime();
        long startTime = startDate.getTime();

        Calendar calendar2 = Calendar.getInstance();
        calendar2.add(Calendar.DATE, -1);
        calendar2.set(Calendar.HOUR_OF_DAY, 23);
        calendar2.set(Calendar.MINUTE, 59);
        calendar2.set(Calendar.SECOND, 59);
        calendar2.set(Calendar.MILLISECOND, 999);
        Date endDate = calendar2.getTime();
        long endTime = endDate.getTime();
        TimeInfo info = new TimeInfo();
        info.setStartTime(startTime);
        info.setEndTime(endTime);
        return info;
    }

    public static TimeInfo getTodayStartAndEndTime() {
        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(Calendar.HOUR_OF_DAY, 0);
        calendar1.set(Calendar.MINUTE, 0);
        calendar1.set(Calendar.SECOND, 0);
        calendar1.set(Calendar.MILLISECOND, 0);
        Date startDate = calendar1.getTime();
        long startTime = startDate.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss S");

        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(Calendar.HOUR_OF_DAY, 23);
        calendar2.set(Calendar.MINUTE, 59);
        calendar2.set(Calendar.SECOND, 59);
        calendar2.set(Calendar.MILLISECOND, 999);
        Date endDate = calendar2.getTime();
        long endTime = endDate.getTime();
        TimeInfo info = new TimeInfo();
        info.setStartTime(startTime);
        info.setEndTime(endTime);
        return info;
    }

    public static class TimeInfo {

        private long startTime;
        private long endTime;

        public long getStartTime() {
            return this.startTime;
        }

        public void setStartTime(long paramLong) {
            this.startTime = paramLong;
        }

        public long getEndTime() {
            return this.endTime;
        }

        public void setEndTime(long paramLong) {
            this.endTime = paramLong;
        }
    }
    /**********************************************************************************************/


    /**
     * 转换时间显示
     *
     * @param time 毫秒
     * @return
     */
    public static String generateTime(long time) {
        int totalSeconds = (int) (time / 1000);
        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        return hours > 0 ? String.format("%02d:%02d:%02d", hours, minutes,
                seconds) : String.format("%02d:%02d", minutes, seconds);
    }

    /**
     * 计算时差
     *
     * @param starttime
     * @param endtime
     * @return
     */
    public static long calTimeDur(String starttime, String endtime) {
        long l = 0l;
        try {
            SimpleDateFormat df = new SimpleDateFormat(TIME_FORMAT2);
            Date now = df.parse(starttime);
            Date date = df.parse(endtime);
            l = now.getTime() - date.getTime();
            return l;
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
	 * 由毫秒得到分钟数
	 * @param milliseconds
	 * @return
	 */
	public static int getMinutesByMilliseconds(long milliseconds) {
		int minutes = 0;
		int totalSeconds = (int) (milliseconds / 1000);
		minutes = totalSeconds / 60;
		return minutes;
	}

	/**
     * 格式化双精度数据
     *
     * @param d
     * @return
     */
    public static String getDecimalFormat(double d) {
        try {
            DecimalFormat _df = new DecimalFormat("######0.00");
            return _df.format(d);
        } catch (Exception e) {
            e.printStackTrace();
            return "0.00";
        }
    }

    /***
     * format card number
     * 6226 **** **** 8888
     * @param str
     * @return
     */
    public static String getFormatCardNum(String str) {
        String panNumber = str;
        String flgStr = " **** **** ";
        int leastCardNum = 8;
        int cardNumPart = 4;
        if (str.length() > leastCardNum) {
            String firstFourStr = "****";//panNumber.substring(0, cardNumPart);
            String lastFourStr = panNumber.substring(panNumber.length()
                    - cardNumPart, panNumber.length());
            return firstFourStr + flgStr + lastFourStr;
        }
        return panNumber;
    }

    public static String getFormatCardNumForMyOrder(String str) {
        String panNumber = str;
        String flgStr = "**** ";
        int leastCardNum = 8;
        int cardNumPart = 4;
        if (str.length() > leastCardNum) {
            String lastFourStr = panNumber.substring(panNumber.length()
                    - cardNumPart, panNumber.length());
            return flgStr + lastFourStr;
        }
        return panNumber;

    }

    public static String getLastFourCardNum(String str) {
        String panNumber = str;
        int leastCardNum = 8;
        int cardNumPart = 4;
        if (str.length() > leastCardNum) {
            String lastFourStr = panNumber.substring(panNumber.length()
                    - cardNumPart, panNumber.length());
            return lastFourStr;
        }
        return panNumber;
    }

    public static String getFormatPhone(String str) {
        String phoneNumber = str;
        String flgStr = "*****";
        if (str.length() > 10) {
            String preStr = phoneNumber.substring(0, 3);
            String lastStr = phoneNumber.substring(8, 11);
            return preStr + flgStr + lastStr;
        }
        return phoneNumber;
    }

    public static String getFormatCurrency(String rmb) {
        return getFormatCurrency(rmb, 1f);
    }

    public static String getFormatCurrency(String rmb, double scale) {
        return getFormatCurrency(rmb, scale, true);
    }

    public static String getFormatCurrency(String rmb, boolean withUnits) {
        return getFormatCurrency(rmb, 1f, withUnits);
    }

    public static String getFormatCurrency(String rmb, double scale,
                                           boolean withUnit) {
        BigDecimal creditValueBigDec = new BigDecimal(rmb);
        creditValueBigDec = creditValueBigDec.multiply(new BigDecimal(scale));
        NumberFormat format = NumberFormat.getNumberInstance(Locale.CHINA);
        format.setMinimumFractionDigits(2);
        format.setMaximumFractionDigits(2);
        if (withUnit) {
            return format.format(creditValueBigDec.doubleValue()) + "元";
        } else {
            return format.format(creditValueBigDec.doubleValue());
        }
    }

    public static String getFormatDecimal(String format) {
        try {
            BigDecimal input = new BigDecimal(format);
            if (input.scale() > 2) {
                return input.setScale(2, RoundingMode.FLOOR).toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return format;
    }

    /*
     * Valid string value, convert which valued as "null" or null to ""
     */
    public static String validStringFormat(String format) {
        if (null == format || "null".equals(format)) {
            format = "";
        }
        return format;
    }

    public static String getMD5(String content) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(content.getBytes());
            String result = getHashString(digest);
            ZRLog.e("getMD5 contentLength:" + content.length() + ", md5 = "
                    + result);
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getHashString(MessageDigest digest) {
        StringBuilder builder = new StringBuilder();
        for (byte b : digest.digest()) {
            builder.append(Integer.toHexString((b >> 4) & 0xf));
            builder.append(Integer.toHexString(b & 0xf));
        }
        return builder.toString();
    }

	public static String getImgUrl(String oriUrl, String folder) {
		int index = -1;
		if (TextUtils.isEmpty(oriUrl) || TextUtils.isEmpty(folder)
				|| -1 == (index = oriUrl.lastIndexOf("/"))) {
			return oriUrl;
		}
		return oriUrl.substring(0, index) + "/" + folder
				+ oriUrl.substring(index);
	}

	public static boolean isAppInstalled(Context context, String packageName) {
		try {
			context.getPackageManager().getPackageInfo(packageName,
					PackageManager.GET_ACTIVITIES);
			return true;
		} catch (NameNotFoundException e) {
			return false;
		}
	}

	public static Bitmap getScaledBitmap(Bitmap src, int width, int height) {
		if (null == src) {
			return null;
		}
		if (width == src.getWidth() && height == src.getHeight()) {
			return src;
		}
		Bitmap result = Bitmap.createScaledBitmap(src, width, height, true);
		if (result != src) {
			src.recycle();
		}
		return result;
	}

    /**
     * 是否有可用网络
     *
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo network = cm.getActiveNetworkInfo();
        if (network != null) {
            return network.isAvailable();
        }
        return false;
    }

    /**
     * Wifi是否可用
     *
     * @param context
     * @return
     */
    public static boolean isWifiEnable(Context context) {
        WifiManager wifiManager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        return wifiManager.isWifiEnabled();
    }

    /**
     * 判断wifi是否连接
     *
     * @param inContext
     * @return
     */
    public static boolean isWiFiActive(Context inContext) {
        Context context = inContext.getApplicationContext();
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean iswifiActivity = false;
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (NetworkInfo element : info) {
                    if (element.getTypeName().equals("WIFI")
                            && element.isConnected()) {
                        iswifiActivity = true;
                        break;
                    }
                }
            }
        }
        return iswifiActivity;
    }

    /**
     * URL检查<br>
     * <br>
     *
     * @param pInput 要检查的字符串<br>
     * @return boolean 返回检查结果<br>
     */
    public static boolean isUrl(String pInput) {
        if (pInput == null) {
            return false;
        }
        boolean res = false;
        if (pInput.startsWith("http://") || pInput.startsWith("https://"))
            res = true;
        return res;
    }

    /**
     * 比较版本号的大小,前者大则返回一个正数,后者大返回一个负数,相等则返回0
     *
     * @param version1
     * @param version2
     * @return
     */
    public static int compareVersion(String version1, String version2)
            throws Exception {
        if (version1 == null || version2 == null) {
            throw new Exception("compareVersion error:illegal params.");
        }
        String[] versionArray1 = version1.split("\\.");// 注意此处为正则匹配，不能用"."；
        String[] versionArray2 = version2.split("\\.");
        int idx = 0;
        int minLength = Math.min(versionArray1.length, versionArray2.length);// 取最小长度值
        int diff = 0;
        while (idx < minLength
                && (diff = versionArray1[idx].length()
                - versionArray2[idx].length()) == 0// 先比较长度
                && (diff = versionArray1[idx].compareTo(versionArray2[idx])) == 0) {// 再比较字符
            ++idx;
        }
        // 如果已经分出大小，则直接返回，如果未分出大小，则再比较位数，有子版本的为大；
        diff = (diff != 0) ? diff : versionArray1.length - versionArray2.length;
        return diff;
    }

    public static String UUID() {
        UUID uuid = UUID.randomUUID();
        String s = uuid.toString().replaceAll("-", "");
        return s;
    }

    public static String getRunningActivityName(){
        ActivityManager activityManager=(ActivityManager) ZRApplication.applicationContext.getSystemService(Context.ACTIVITY_SERVICE);
        String runningActivity=activityManager.getRunningTasks(1).get(0).topActivity.getClassName();
        return runningActivity;
    }

    /**
     * 获取当前时间的年
     *
     * @return
     */
    public static int getCurrentYear() {
        Calendar now = Calendar.getInstance();
        return now.get(Calendar.YEAR);
    }

    /**
     * 获取当前时间的月
     *
     * @return
     */
    public static int getCurrentMonth() {
        Calendar now = Calendar.getInstance();
        return now.get(Calendar.MONTH) + 1;
    }
}
