import java.util.Random;

/**
 * A class representing one people on the land
 */
public class People implements Comparable<People>{
    // how old a person is
    public int age;

    // how many patches ahead a person can see
    public int vision;

    // how much grain a person eats each time
    public int metabolism;

    // the amount of grain a person has
    public double wealth;

    // maximum age that a person can reach
    public int lifeExpectancy;

    // the direction which is most profitable for each person in the
    // surrounding patches in next step
    public String best_direction;

    // the position of a person on the patch
    public Position position;
    public Random random = new Random();

    // Set a person's information initially.
    // Set his/her age to be 0, position in the left-top of the patch
    // Other variables to be random values between the max and minimum
    // values set in the Parameters.
    public People(){
        this.age = 0;
        this.lifeExpectancy = Params.LIFE_EXPECTANCY_MIN +
                random.nextInt(Params.LIFE_EXPECTANCY_MAX
                        - Params.LIFE_EXPECTANCY_MIN +1);
        this.metabolism = 1 + random.nextInt(Params.METABOLISM_MAX);
        this.wealth = metabolism + random.nextInt(50);
        this.vision = 1 + random.nextInt(Params.VISION_MAX);
        this.position = new Position(0,0);
    }

    /**
     * Determine the direction which is most profitable for each person
     * in the surrounding patches
     * within the his/her vision
     *
     * @param patches
     *            the whole land made of patches
     * @return the best direction for the person
     */
    public void turn_toward_grain(Patch[][] patches){

        // Set the best direction be "up" initially
        String best_direction = "up";
        // Calculate the total amount of grain upwards and set it be the
        // best amount
        double best_amount = getGrainAhead(patches, "up");

        // check the amount of grain in other three directions
        // If one of them is larger than current best_amount, set it to be
        // the best_amount
        // And change its direction be best direction.
        if(getGrainAhead(patches, "down") > best_amount){
            best_direction = "down";
            best_amount = getGrainAhead(patches, "down");
        }

        if(getGrainAhead(patches, "left") > best_amount ){
            best_direction = "left";
            best_amount = getGrainAhead(patches, "left");
        }

        if(getGrainAhead(patches, "right")> best_amount){
            best_direction = "right";
            best_amount = getGrainAhead(patches, "right");
        }

        this.best_direction = best_direction;
    }

    /**
     * Calculate the total amount of grain in four directions within
     * the person's vision
     * @param patches
     *            the land that grain grows on
     * @param direction
     *            one specific direction from the direction list.
     *            the amount of grain on this direction within one person's
     *            version will be calculated.
     * @return total amount of grain on this direction in the land
     */
    public double getGrainAhead(Patch[][] patches, String direction){
        double total = 0;
        int how_far = 1;

        for(int i=1; i<= vision ; i++){
            if(direction.equals("up")){
                total += patches[position.row]
                      [module((position.column - how_far),Params.LAND_LENGTH)]
                      .grainHere;
            }else if(direction.equals("down")){
                total += patches[position.row]
                      [module((position.column + how_far),Params.LAND_LENGTH)]
                      .grainHere;
            }else if(direction.equals("left")){
                total += patches[module((position.row-how_far),
                      Params.LAND_WIDTH)][position.column].grainHere;
            }else if(direction.equals("right")){
                total += patches[module((position.row+how_far),
                      Params.LAND_WIDTH)][position.column].grainHere;
            }
            how_far ++;
        }

        return total;
    }

    /**
     * Let everyone move one step according to best direction.
     * Consume some grain according to metabolism after moving.
     * Check for death conditions:
     * If a person has no grain or older than the life expectancy then set
     * him/her "die".
     * @return whether this person has died
     */
    public boolean move_eat_age_die(){
        boolean hasDead = false;
        move();
        wealth -= metabolism;
        age++;
        if((wealth < 0)||(age >= lifeExpectancy)){
            hasDead = true;
        }
        return  hasDead;
    }

    //Change person's position after moving one step
    public void move(){
        if(best_direction.equals("up")){
            position.row = module((position.row -1), Params.LAND_LENGTH);
        }else if(best_direction.equals("down")){
            position.row = module((position.row +1), Params.LAND_LENGTH);
        }else if(best_direction.equals("left")){
            position.column = module((position.column -1), Params.LAND_WIDTH);
        }else if(best_direction.equals("right")){
            position.column = module((position.column +1), Params.LAND_WIDTH);
        }
    }


    //Check whether a location/position will cross border line of the patch
    //Set this position/location correctly if it is crossing the border line
    public int module(int x, int y ){
        int mod =0 ;
        if(x == 0){
            mod = 0;
        }else if (x < 0){
            mod = x+y;
        }else if (x > 0 && x < y){
            mod = x;
        }else if ( y>= x){
            mod = x - y;
        }
        return mod;
    }

    /**
     * @Override
     * Sorting the arraylist by wealth of people
     */
    public int compareTo (People comparePeo){
        double compareWealth = ((People) comparePeo).wealth;
        if(this.wealth > compareWealth){
            return 1;
        }
        else if (this.wealth == compareWealth)
            return 0;
        else
            return -1;
    }

}
