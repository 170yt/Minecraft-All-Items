package x170.all_items.game;

import x170.all_items.AllItems;

public abstract class Timer {
    private static boolean paused = true;

    public static void tick() {
        if (!paused) {
            AllItems.CONFIG.timerSeconds++;
        }
    }

    public static void pause() {
        paused = true;
        AllItems.CONFIG.save();
    }

    public static void resume() {
        paused = false;
    }

    public static boolean toggle() {
        if (paused) resume();
        else pause();
        return paused;
    }

    public static boolean isPaused() {
        return paused;
    }

    public static int getTimeSeconds() {
        return AllItems.CONFIG.timerSeconds;
    }

    public static String getTimeString() {
        int time = getTimeSeconds();
        if (time == 0) return "0s";

        String timeString = formatTime(time, true);
//        if (paused) timeString += " (paused)";
        return timeString;
    }

    public static String formatTime(int time, boolean showSeconds) {
        if (time <= 0) return showSeconds ? "0s" : "0m";

        int days = time / 86400;
        int hours = time / 3600 % 24;
        int minutes = time / 60 % 60;
        int seconds = time % 60;

        StringBuilder timeString = new StringBuilder();
        if (days > 0) timeString.append(days).append("d ").append(hours).append("h ").append(minutes).append("m");
        else if (hours > 0) timeString.append(hours).append("h ").append(minutes).append("m");
        else if (minutes > 0) timeString.append(minutes).append("m");

        if (showSeconds) timeString.append(" ").append(seconds).append("s");

        return timeString.isEmpty() ? "0m" : timeString.toString();
    }
}
