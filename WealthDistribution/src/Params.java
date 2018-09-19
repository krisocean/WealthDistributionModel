/**
 * Parameters that influence the behavior of the system
 */
public class Params {

    // the output path
    public static String OUTPUT_FILE = "/users/ChrisXiong/desktop/output.csv";

    //the width of the land
    public static int LAND_WIDTH = 50;

    //the length of the land
    public static int LAND_LENGTH = 50;

    //maximum amount any patch can hold
    public static int MAX_GRAIN = 50;

    //sets how much grain is grown each time GRAIN-GROWTH-INTERVAL
    // allows grain to be grown
    public static int GRAIN_GROWN = 4;

    //sets the diffuse variable
    public static double DIFFUSE_RATE = 0.25;

    //1/8 of diffuse variable
    public static double DIFFUSE_INSTANT = 0.125;

    //determines the initial number of people
    public static int NUM_PEOPLE = 250;

    //the shortest number of ticks that a person can possibly live
    public static int LIFE_EXPECTANCY_MIN = 1;

    //the longest number of ticks that a person can possibly live
    public static int LIFE_EXPECTANCY_MAX = 83;

    //sets the highest possible amount of grain that a person could
    // eat per clock tick
    public static int METABOLISM_MAX = 15;

    //is the farthest possible distance (how many patches ahead) that
    // any person could see
    public static int VISION_MAX = 5;

    //determines how often grain grows
    public static int GRAIN_GROWTH_INTERVAL = 1;

    //determines the initial density of patches that are seeded with
    // the maximum amount of grain
    public static double  PERCENT_BEST_LAND = 10;

    //ticks that the model will run
    public static int MODEL_RUN_TIME = 500;

    //a percentage of the wealth of the parent that could be inherited
    public static final double WEALTH_INHERIT_PERCENT = 0.9;

    //sets the highest possible amount (percentage*10) of metabolism
    // that a newborn could inherit from the parent
    public static final int METABOLISM_INHERIT_PERCENT_MAX = 17;

    //sets the lowest possible amount (percentage*10) of metabolism
    // that a newborn could inherit from the parent
    public static final int METABOLISM_INHERIT_PERCENT_MIN = 3;

    //sets the highest possible amount (percentage*10) of vision that a
    // newborn could inherit from the parent
    public static final int VISION_INHERIT_PERCENT_MAX = 17;

    //sets the lowest possible amount (percentage*10) of vision that a
    // newborn could inherit from the parent
    public static final int VISION_INHERIT_PERCENT_MIN = 3;


}
