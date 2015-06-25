package cn.creditease.godzilla.common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

public class DateUtil {
    /**
     * 获取昨天日期
     * @return
     */
    public static Date getYesterday() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        return calendar.getTime();
    }

	/**
	 * 返回指定日期加上月数后的日期<br/>
	 * 说明:某些特定日期数月后无此日期,则加月数后取最后一天,如（20140131加一个月后的日期为20130228）。
	 * 
	 * @param date   指定日期
	 * @param ms   所加月数
	 * @return 加上月数后的日期
	 */
	public static Date getNumMthDate(Date date, int ms) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, ms);
		return calendar.getTime();

	}
	
	/**
	 * 返回指定日期加上天数后的日期<br/>
	 * 
	 * @param date   指定日期
	 * @param ds   所加天数
	 * @return 加上天数后的日期
	 */
	public static Date getNumDayDate(Date date, int ds) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, ds);
		return calendar.getTime();
	}
	
	/**
	 * 日期相减
	 * @param beginDateStr
	 * @param endDateStr
	 * @return
	 * @throws java.text.ParseException 
	 */
	public static long getDaySub(String beginDateStr,String endDateStr) throws java.text.ParseException
    {
        long day=0;
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyyMMdd");    
        java.util.Date beginDate;
        java.util.Date endDate;
        
        beginDate = format.parse(beginDateStr);
        endDate= format.parse(endDateStr);    
        day=(endDate.getTime()-beginDate.getTime())/(24*60*60*1000);    
        //System.out.println("相隔的天数="+day);   
        
        return day;
    }
	
	/**
	 * 减去划扣日期 获得剩余天数
	 * @param executedate
	 * @return
	 * @throws ParseException 
	 */
	public static long getLastDay(String executedate) throws ParseException
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		String today = dateFormat.format(new Date());		
		String todayDay=today.substring(6,8);
		int resultInt = Integer.parseInt(executedate) - Integer.parseInt(todayDay);
		Date nextMonthDay = getNumMthDate(new Date(),1);
		String nextMonthDayString = dateFormat.format(nextMonthDay);
		if(resultInt >= 0)
		{
			return resultInt;
		}
		else
		{
			if(Integer.parseInt(executedate)<10)
			{
				executedate = "0"+executedate;
			}
			int endDate = Integer.parseInt((nextMonthDayString.substring(0,6))+executedate);
			return getDaySub(today, endDate+"");
		}
	}
	/**
	 * 计算下一个月的划扣日期---del
	 * 计算下一个定投日---add by leiwang10@creditease.cn
	 * @return
	 */
	public static String nowdateAddOneMonthAddDay(String day)
	{
		Calendar cal = Calendar.getInstance();//使用默认时区和语言环境获得一个日历。 
		//add by leiwanglei10@creditease.cn----------如果没有到达当月定投日，则显示当月定投日
		if(cal.get(cal.DAY_OF_MONTH) >= Integer.parseInt(day)){
			cal.add(Calendar.MONTH, 1);//加上一个月
		}
		//通过格式化输出日期   
		java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("yyyy.MM."); 
		
		java.text.SimpleDateFormat fyueFormat = new java.text.SimpleDateFormat("MM"); 
			
		//判断下个月是否为2月
		if("02".equals(fyueFormat.format(cal.getTime())) && Integer.parseInt(day) > 28)
		{
			fyueFormat = new java.text.SimpleDateFormat("yyyy"); 
			Calendar fyueCal = Calendar.getInstance();
			fyueCal.set(Calendar.YEAR,Integer.parseInt(fyueFormat.format(new Date())));   
			fyueCal.set(Calendar.MONTH,Calendar.FEBRUARY);//2月   
			int   maxDate   =   cal.getActualMaximum(Calendar.DATE);
			//System.out.println("2月多少天" + maxDate);
			//如果为2月，判断是否有29天，如果有，都将划扣日期变为28天
			if(maxDate > 28 && Integer.parseInt(day) > 28)
			{
				day = "28";
			}
		}
		
		String s = format.format(cal.getTime());
		if(day.length() == 1)
		{
			day = "0"+day;
		}
		return s+day;
	}

    /**
     * 获取下一个月
     * @param date
     * @return
     */
    public static Date getNextMonth(Date date)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MONTH, 1);
        return cal.getTime();
    }
    
    /**
     * 把Date类型转换为String类型数 格式yyyy-MM
     * @param date 时间
     * @return
     */
    public static String getMonthStr(Date date)
    {
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat(
                "yyyy-MM");
        String dateStr = format.format(date);
        return dateStr;
    }

    /**
     * 取日期字符串，不补0 格式yyyy-MM
     * @param date 时间
     * @return
     */
    public static String getDayStr(Date date)
    {
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat(
                "d");
        String dateStr = format.format(date);
        return dateStr;
    }

    /**
     * 把Date类型转换为String类型数 格式yyyy-MM-dd
     * @param date 时间
     * @return
     */
    public static String getDateStr(Date date)
    {
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat(
                "yyyy-MM-dd");
        String dateStr = format.format(date);
        return dateStr;
    }
    
    /**
     * 把Date类型转换为String类型数 yyyy/MM/dd
     * @param date 时间
     * @return
     */
    public static String getDateBiasStr(Date date)
    {
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat(
                "yyyy/MM/dd");
        String dateStr = format.format(date);
        return dateStr;
    }
    
    /**
     * 把Date类型转换为String类型数 格式yyyy-MM-dd HH:mi:ss
     * @param date 时间
     * @return
     */
    public static String getDateTimeStr(Date date)
    {
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        String dateStr = format.format(date);
        return dateStr;
    }
    
    /**
     * 把String类型转换为Date类型数 格式yyyy-MM-dd HH:mm:ss
     * @param str 时间
     * @return date 时间
     */
    public static Date getDateTimeFromStr(String str)
    {
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 把String类型转换为Date类型数 格式yyyy-MM-dd HH:mm:ss
     * @param str 时间
     * @return date 时间
     */
    public static Date getDateTimeFromStrNew(String str)
    {
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat(
                "yyyyMMddHHmmss");
        Date date = null;
        try {
            date = format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
    /**
	 * 将日期转成14位字符串
	 * @param date
	 * @return
	 */
	public static String dateToString(Date date)
	{
		String str = "";
		if(null != date && !"".equals(date))
		{
			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyyMMddHHmmss"); 
			str = sdf.format(date);
		}
		return str;
	}
	
	  public static XMLGregorianCalendar convertToXMLGregorianCalendar(Date date) {

	        GregorianCalendar cal = new GregorianCalendar();
	        cal.setTime(date);
	        XMLGregorianCalendar gc = null;
	        try {
	            gc = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal);
	        } catch (Exception e) {

	             e.printStackTrace();
	        }
	        return gc;
	    }
	 
	     public static Date convertToDate(XMLGregorianCalendar cal) throws Exception{
	         GregorianCalendar ca = cal.toGregorianCalendar();
	         return ca.getTime();
	     }
	     
	     public static Date addMillis(long millis) {
	 		Calendar calendar = Calendar.getInstance();
	 		calendar.setTimeInMillis(calendar.getTimeInMillis() + millis);
	 		return calendar.getTime();

	 	}
}
