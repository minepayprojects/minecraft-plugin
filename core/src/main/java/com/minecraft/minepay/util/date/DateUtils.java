package com.minecraft.minepay.util.date;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateUtils {

    public static String formatTime(int i) {

        if (i >= 60) {

            int minutes = i / 60;
            int seconds = i - minutes * 60;

            if (seconds == 0) {
                if (minutes > 1) {
                    return minutes + " minutos";
                } else {
                    return minutes + " minuto";
                }
            }

            String min = "minuto";
            String second = "segundo";

            if (minutes > 1) {
                min = min + "s";
            }

            if (seconds > 1) {
                second = second + "s";
            }

            return minutes + " " + min + " e " + seconds + " " + second;
        }

        if (i > 1) {
            return i + " segundos";
        }

        return i + " segundo";
    }

    public static String format(int i) {
        int minutes = i / 60;
        int hours = minutes / 60;
        int seconds = i % 60;

        minutes = (minutes - 60 * hours);

        return (hours > 0 ? hours + ":" : "") + (minutes < 10 && minutes != 0 && hours > 0 ? "0" : minutes == 0 && hours > 0 ? "0" : "") + minutes + ":" + (seconds < 10 ? "0" : "") + seconds;
    }

    public static String toMillis(final double d) {

        final String string = String.valueOf(d);
        final StringBuilder sb = new StringBuilder();

        boolean stop = false;
        char[] charArray;

        for (int length = (charArray = string.toCharArray()).length, i = 0; i < length; ++i) {
            final char c = charArray[i];

            if (stop) {
                return sb.append(c).toString();
            }

            if (c == '.') {
                stop = true;
            }

            sb.append(c);
        }

        return sb.toString();
    }

    public static String getDifferenceFormat(long time) {

        if (time <= 0L) {
            return "";
        }

        final long day = TimeUnit.SECONDS.toDays(time);
        final long hours = TimeUnit.SECONDS.toHours(time) - day * 24L;
        final long minutes = TimeUnit.SECONDS.toMinutes(time) - TimeUnit.SECONDS.toHours(time) * 60L;
        final long seconds = TimeUnit.SECONDS.toSeconds(time) - TimeUnit.SECONDS.toMinutes(time) * 60L;

        final StringBuilder sb = new StringBuilder();

        if (day > 0L) {
            sb.append(day).append(" ").append("dia").append((day > 1L) ? "s" : "").append(" ");
        }
        if (hours > 0L) {
            sb.append(hours).append(" ").append("hora").append((hours > 1L) ? "s" : "").append(" ");
        }
        if (minutes > 0L) {
            sb.append(minutes).append(" ").append("minuto").append((minutes > 1L) ? "s" : "").append(" ");
        }
        if (seconds > 0L) {
            sb.append(seconds).append(" ").append("segundo").append((seconds > 1L) ? "s" : "");
        }

        return sb.toString();
    }

    public static String formatDifference(long time) {
        long timeLeft = time - System.currentTimeMillis();
        long seconds = timeLeft / 1000L;

        return fromLong(seconds, Style.NORMAL);
    }

    public static String formatDifference(long time, Style style) {
        long timeLeft = time - System.currentTimeMillis();
        long seconds = timeLeft / 1000L;

        return fromLong(seconds, style);
    }

    public static String formatDifference(long highTime, long lowTime) {
        long timeLeft = highTime - lowTime;
        long seconds = timeLeft / 1000L;

        return fromLong(seconds, Style.NORMAL);
    }

    public static long parseDateDiff(final String time, final boolean future) throws Exception {

        if (time.equalsIgnoreCase("n") || time.equalsIgnoreCase("never")) {
            return -1L;
        }

        final Pattern timePattern = Pattern.compile("(?:([0-9]+)\\s*y[a-z]*[,\\s]*)?(?:([0-9]+)\\s*mo[a-z]*[,\\s]*)?(?:([0-9]+)\\s*w[a-z]*[,\\s]*)?(?:([0-9]+)\\s*d[a-z]*[,\\s]*)?(?:([0-9]+)\\s*h[a-z]*[,\\s]*)?(?:([0-9]+)\\s*m[a-z]*[,\\s]*)?(?:([0-9]+)\\s*(?:s[a-z]*)?)?", 2);
        final Matcher m = timePattern.matcher(time);
        int years = 0;
        int months = 0;
        int weeks = 0;
        int days = 0;
        int hours = 0;
        int minutes = 0;
        int seconds = 0;
        boolean found = false;

        while (m.find()) {

            if (m.group() != null) {
                if (m.group().isEmpty()) {
                    continue;
                }

                for (int i = 0; i < m.groupCount(); ++i) {
                    if (m.group(i) != null && !m.group(i).isEmpty()) {
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    continue;
                }

                if (m.group(1) != null && !m.group(1).isEmpty()) {
                    years = Integer.parseInt(m.group(1));
                }
                if (m.group(2) != null && !m.group(2).isEmpty()) {
                    months = Integer.parseInt(m.group(2));
                }
                if (m.group(3) != null && !m.group(3).isEmpty()) {
                    weeks = Integer.parseInt(m.group(3));
                }
                if (m.group(4) != null && !m.group(4).isEmpty()) {
                    days = Integer.parseInt(m.group(4));
                }
                if (m.group(5) != null && !m.group(5).isEmpty()) {
                    hours = Integer.parseInt(m.group(5));
                }
                if (m.group(6) != null && !m.group(6).isEmpty()) {
                    minutes = Integer.parseInt(m.group(6));
                }
                if (m.group(7) != null && !m.group(7).isEmpty()) {
                    seconds = Integer.parseInt(m.group(7));
                    break;
                }

                break;
            }
        }

        if (!found) {
            throw new Exception("Illegal Time");
        }
        if (years > 20) {
            throw new Exception("Illegal Time");
        }

        final Calendar c = new GregorianCalendar();

        if (years > 0) {
            c.add(Calendar.YEAR, years * (future ? 1 : -1));
        }

        if (months > 0) {
            c.add(Calendar.MONTH, months * (future ? 1 : -1));
        }

        if (weeks > 0) {
            c.add(Calendar.WEEK_OF_YEAR, weeks * (future ? 1 : -1));
        }

        if (days > 0) {
            c.add(Calendar.DATE, days * (future ? 1 : -1));
        }

        if (hours > 0) {
            c.add(Calendar.HOUR_OF_DAY, hours * (future ? 1 : -1));
        }

        if (minutes > 0) {
            c.add(Calendar.MINUTE, minutes * (future ? 1 : -1));
        }

        if (seconds > 0) {
            c.add(Calendar.SECOND, seconds * (future ? 1 : -1));
        }

        return c.getTimeInMillis();
    }

    public static String compareTwoDates(long date1, long date2) {
        Calendar c = new GregorianCalendar();
        c.setTimeInMillis(date1);
        Calendar now = new GregorianCalendar();
        now.setTimeInMillis(date2);

        return formatDateDiff(now, c);
    }

    public static String spigotFormat(long date) {
        Calendar c = new GregorianCalendar();
        c.setTimeInMillis(date);
        Calendar now = new GregorianCalendar();

        return formatDateDiff(now, c);
    }

    protected static String formatDateDiff(Calendar fromDate, Calendar toDate) {
        boolean future = false;

        if (toDate.equals(fromDate)) {
            return "now";

        } else {
            if (toDate.after(fromDate)) {
                future = true;
            }

            StringBuilder sb = new StringBuilder();
            int[] types = new int[]{1, 2, 5, 11, 12, 13};

            String[] names = new String[]{"ano", "anos", "mês", "meses", "dia", "dias", "hora", "horas", "minuto", "minutos", "segundo", "segundos"};
            int accuracy = 0;

            for (int i = 0; i < types.length && accuracy <= 2; ++i) {
                int diff = dateDiff(types[i], fromDate, toDate, future);

                if (diff > 0) {
                    ++accuracy;
                    sb.append(" ").append(diff).append(" ").append(names[i * 2 + (diff > 1 ? 1 : 0)]);
                }
            }

            return sb.isEmpty() ? "now" : sb.toString().trim();
        }
    }

    protected static int dateDiff(int type, Calendar fromDate, Calendar toDate, boolean future) {
        int year = 1;
        int fromYear = fromDate.get(Calendar.YEAR);
        int toYear = toDate.get(Calendar.YEAR);

        if (Math.abs(fromYear - toYear) > 100000) {
            toDate.set(Calendar.YEAR, fromYear + (future ? 100000 : -100000));
        }

        int diff = 0;

        long savedDate;
        for (savedDate = fromDate.getTimeInMillis(); future && !fromDate.after(toDate) || !future && !fromDate.before(toDate); ++diff) {
            savedDate = fromDate.getTimeInMillis();
            fromDate.add(type, future ? 1 : -1);
        }

        --diff;

        fromDate.setTimeInMillis(savedDate);

        return diff;
    }

    protected static String fromLong(long length, Style style) {

        if (style.equals(Style.NORMAL)) {

            final int days = (int) TimeUnit.SECONDS.toDays(length);
            final long hours = TimeUnit.SECONDS.toHours(length) - days * 24L;
            final long minutes = TimeUnit.SECONDS.toMinutes(length) - TimeUnit.SECONDS.toHours(length) * 60L;
            final long seconds = TimeUnit.SECONDS.toSeconds(length) - TimeUnit.SECONDS.toMinutes(length) * 60L;

            String totalDay = days + ((days == 1) ? " dia " : " dias ");
            String totalHours = hours + ((hours == 1L) ? " hora " : " horas ");
            String totalMinutes = minutes + ((minutes == 1L) ? " minuto " : " minutos ");
            String totalSeconds = seconds + ((seconds == 1L) ? " segundo" : " segundos");

            if (days == 0) {
                totalDay = "";
            }
            if (hours == 0L) {
                totalHours = "";
            }
            if (minutes == 0L) {
                totalMinutes = "";
            }
            if (seconds == 0L) {
                totalSeconds = "";
            }

            String restingTime = totalDay + totalHours + totalMinutes + totalSeconds;
            restingTime = restingTime.trim();

            if (restingTime.isEmpty()) {
                restingTime = "0 segundos";
            }

            return restingTime;

        } else {

            final int days = (int) TimeUnit.SECONDS.toDays(length);
            final long hours = TimeUnit.SECONDS.toHours(length) - days * 24L;
            final long minutes = TimeUnit.SECONDS.toMinutes(length) - TimeUnit.SECONDS.toHours(length) * 60L;
            final long seconds = TimeUnit.SECONDS.toSeconds(length) - TimeUnit.SECONDS.toMinutes(length) * 60L;
            String totalDay = days + ("d ");
            String totalHours = hours + ("h ");
            String totalMinutes = minutes + ("min ");
            String totalSeconds = seconds + ("s");

            if (days == 0) {
                totalDay = "";
            }
            if (hours == 0L) {
                totalHours = "";
            }
            if (minutes == 0L) {
                totalMinutes = "";
            }
            if (seconds == 0L) {
                totalSeconds = "";
            }

            String restingTime = totalDay + totalHours + totalMinutes + totalSeconds;

            restingTime = restingTime.trim();

            if (restingTime.isEmpty()) {
                restingTime = "0s";
            }

            return restingTime;
        }
    }

    public enum Style {

        NORMAL, SIMPLIFIED;
    }
}