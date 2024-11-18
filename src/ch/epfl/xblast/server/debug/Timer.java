package ch.epfl.xblast.server.debug;

public final class Timer {

    private static long time = -1;
    private static long time1 = -1;
    private static int lapCount = 0;
    private static long max = -1;

    public static void Start() {
        time = System.currentTimeMillis();
        lapCount = 0;

        time1 = -1;
        System.out.println("Timer start");
    }

    public static void SetLapLimit(long milisec) {
        if (milisec > 0)
            max = milisec;
    }

    public static void RemoveLapLimit() {
        max = -1;
    }

    public static void Stop() {
        long diff = System.currentTimeMillis() - time;
        long diff1 = System.currentTimeMillis() - time1;

        if (time1 != -1)
            System.out.println(String.format(
                    "Stop %d : %3d seconds and %3d milliseconds since start, %3d seconds and %3d milliseconds since last stop.",
                    ++lapCount, diff / 1000, diff % 1000, diff1 / 1000,
                    diff1 % 1000));
        else
            System.out.println(String.format(
                    "Stop %d : %d seconds and %d milliseconds since start.",
                    ++lapCount, diff / 1000, diff % 1000));
        

        if (diff1 > max && max > 0 && time1 >0 ) {
            System.out.println(
                    String.format("this was a controlled crash at stop %d, a lap took longer than expected, so we crashed the app for you to follow the stack dump and figure out where it crashed exactly.",lapCount-1));
            
            

        }
        time1 = System.currentTimeMillis();
    }

}
