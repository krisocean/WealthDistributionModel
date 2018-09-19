import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;

/**
 * A class calculating the gini index at a particular time
 */
public class GiniIndex {

    public GiniIndex() {
        // TODO Auto-generated constructor stub
    }

    /**
     * calculate the gini index
     *
     * @param allPeople
     *            the list that contains all the people on the land
     * @return the gini index at a particular time
     */
    public static double gini(ArrayList<People> allPeople) {

        // sort the list by ascending wealth
        Collections.sort(allPeople);

        // compute the sum of the wealth of all people
        double totalWealth = 0;
        for (People people : allPeople) {
            totalWealth = totalWealth + people.wealth;
        }

        double wealthSubSum = 0;
        double giniReserve = 0;
        int index = 0;
        double part1 = 0;
        double part2 = 0;
        double difference = 0;

        // recompute lorenz-points and the value of gini-index-reserve
        for (People people : allPeople) {
            wealthSubSum = wealthSubSum + people.wealth;
            index = index + 1;

            part1 = divide2(index, Params.NUM_PEOPLE, 10);
            part2 = divide2(wealthSubSum, totalWealth, 10);

            difference = subtract2(part1, part2);
            giniReserve = add2(giniReserve, difference);
        }

        double giniIndex = divide2(giniReserve, Params.NUM_PEOPLE, 10);
        giniIndex = giniIndex / 0.5;

        return giniIndex;
    }

    /**
     * provide accurate divide
     *
     * @param dividend
     *
     * @param divisor
     *
     * @param the scale of fractional part
     *
     * @return quotient
     */
    public static double divide2(double num1, double num2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("The scale must be a " +
                    "positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(num1));
        BigDecimal b2 = new BigDecimal(Double.toString(num2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * provide accurate subtract
     *
     * @param minuend
     *
     * @param subtrahend
     *
     * @return difference
     */
    public static double subtract2(double num1, double num2) {
        BigDecimal b1 = new BigDecimal(Double.toString(num1));
        BigDecimal b2 = new BigDecimal(Double.toString(num2));
        return b1.subtract(b2).doubleValue();
    }

    /**
     * provide accurate subtract
     *
     * @param summend
     *
     * @param addend
     *
     * @return sum
     */
    public static double add2(double num1, double num2) {
        BigDecimal b1 = new BigDecimal(Double.toString(num1));
        BigDecimal b2 = new BigDecimal(Double.toString(num2));
        return b1.add(b2).doubleValue();
    }

}
