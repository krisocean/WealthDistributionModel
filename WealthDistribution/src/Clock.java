/**
 * A class representing a clock ticks since the last setup
 */
public class Clock {

    //the total number of clock ticks since the last setup
    public static int time;

    /**
     * create a new clock with 0 ticks
     */
    public Clock(){
        time = 0;
    }

    /**
     * Add one tick to the number of clock ticks since the last setup
     */
    public void tick(){
        time++;
    }

    /**
     * @return the number of clock ticks since the last setup
     */
    public int time(){
        return time;
    }
}
