import java.util.ArrayList;
import java.util.Random;

/**
 * A Class represents the whole land, which is consisted of
 * patches, and people live on the land.
 */
public class Land {
    // The clock to record time
    public Clock clock;
    // The number of rich people on the land
    public int rich;
    // The number of middle-class people on the land
    public int middle;
    // The number of poor people on the land
    public int poor;
    // The width of the land
    public int width;
    // The length of the land
    public int length;
    // All people on the land
    public ArrayList<People> allPeople;
    // All patches on the land, the first index indicates the
    // row number, second index indicates the column number.
    public Patch[][] patches;

    /**
     * Create a new land, and initialise its width and length, and set up a
     * Clock to record the time.
     * @param width land width
     * @param length land length
     */
    public Land(int width, int length){

        this.width = width;
        this.length = length;
        this.allPeople = new ArrayList<People>();
        this.clock = new Clock();

    }

    /**
     * The whole land could be regarded as lots of patches. And set up initial
     * amounts of grain for each patch.
     */
    public void initialPatches() {
        // Initial all patches
        this.patches = new Patch[width][length];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < length; j++) {
                patches[i][j] = new Patch();
                patches[i][j].maxGrainHere = 0;
                Random random = new Random();
                // Give some patches maximum grain
                if (random.nextInt(100) < Params.PERCENT_BEST_LAND) {
                    patches[i][j].maxGrainHere = Params.MAX_GRAIN;
                    patches[i][j].grainHere = patches[i][j].maxGrainHere;
                }
            }

        }
    }

    /**
     * For every patch, spread a part of grain around its neighbors.
     */
    public void spreadPatches(){

        // For each patch, diffuse grain around, repeat 5 times.
        for(int x = 0; x < 5 ; x++){
            for(int i=0; i < width ; i++){
                for(int j=0; j < length; j++){
                    if(patches[i][j].maxGrainHere != 0){
                        patches[i][j].grainHere = patches[i][j].maxGrainHere;
                        diffuse(i,j,Params.DIFFUSE_RATE);
                    }
                }
            }
        }

        // Spread the grain around some more, repeat diffuse process 10 times.
        for(int x = 0; x < 10; x++){
            for(int i =0; i< width; i++){
                for(int j =0; j< length; j++){
                    diffuse(i,j,Params.DIFFUSE_RATE);
                }
            }

        }

        roundPatches();

    }

    /**
     * For the patch[i][j], diffuse a fixed proportion grain to its neighbours.
     * We could classify the patches into three types. One is the patch on the
     * land corner, which only has three neighbors. Another type is the patch
     * locates on the boundary of land, which has 5 neighbors. The rest patches
     * are the other type, which has 8 neighbors. Different types of patch will
     * leave different proportions of grain after diffusing.
     * @param i Row index of patch
     * @param j Column index of patch
     * @param diffuseRate proportion of grain will ne diffused.
     */
    public void diffuse(int i, int j, double diffuseRate){
        for (int x= i -1 ; x <= i+1; x++){
            for(int y = j-1; y <= j+1; y++){
                // Diffuse the grain to its neighbour.
                if(((x != i) || (y != j)) && ((x>=0)&&(x<width))
                        && ((y>=0)&&(y<width))) {
                    patches[x][y].grainHere +=
                            patches[i][j].grainHere
                                    *diffuseRate*Params.DIFFUSE_INSTANT;
                }
            }
        }
        // After diffusing process, we need define the remaining grain on the
        // patch. Patch on the corner has only three neighbor, and it will keep
        // 5 more parts grain which are supposed to diffuse to its neighbour.
        if(((i==0)&&(j==0))||((i==0)&&(j== length-1)) || ((i==width-1)&&(j==0))
                || ((i==width-1)&&(j==length-1))){
            patches[i][j].grainHere = patches[i][j].grainHere
                    *(1-diffuseRate + diffuseRate*Params.DIFFUSE_INSTANT*5) ;
        }
        // Patch on the land boundary has five neighbour, and it will keep 3
        // more parts grain which are supposed to diffuse to its neighbour.
        else if(((i==0) && (j!=0 && j!=length-1))||
                ((i!=0 && i!=width-1) && (j== length-1)) ||
                ((i==width-1) && (j!=0 && j!=length-1))||
                ((i!=0 && i!=width-1) && (j==0))){
            patches[i][j].grainHere = patches[i][j].grainHere *
                    (1-diffuseRate + diffuseRate*Params.DIFFUSE_INSTANT*3) ;
        }
        // The third type of patch has diffused grain to 8 neighbors.
        else{
            patches[i][j].grainHere = patches[i][j].grainHere *(1-diffuseRate);
        }
    }

    /**
     * Round the grain on the patch, cast the value to be an int type.
     */
    public void roundPatches(){
        for(int i =0; i < width; i++){
            for(int j=0; j < length; j++){
                patches[i][j].grainHere = Math.floor(patches[i][j].grainHere);
                patches[i][j].maxGrainHere = patches[i][j].grainHere;
            }
        }
    }

    /**
     * Initial a number of people on the land. The attributes of people
     * will be random.
     * @param num_people Number of initial people.
     */
    public void initialPeople(int num_people){
        Random random = new Random();
        for (int i = 0; i < num_people; i++){
            People createdPeople = new People();
            createdPeople.age = random.nextInt(createdPeople.lifeExpectancy);
            createdPeople.position.row = random.nextInt(width);
            createdPeople.position.column = random.nextInt(length);
            allPeople.add(createdPeople);
        }
    }

    /**
     * Update status of land within one tick.
     * @return A string includes the quantity information of each class.
     */
    public String update(String model){

        // Get the best direction of each people on the land, which contains
        // most grain.
        for(People people: allPeople){
            people.turn_toward_grain(patches);
        }

        // Harvest the grain on all patches.
        harvest();

        // Determine the running model according to configuration parameter
        if(model.equals("basic")){
            // Iterate all people in the land, check if there is people dead
            // after a series of actions(move, eat, age)
            ArrayList<People> newPeople = new ArrayList<People>();
            for(People people: allPeople){
                // A boolean indicate if the people is dead.
                boolean hasDead = people.move_eat_age_die();
                Random random = new Random();
                // If there are person dead, remove them from the land. For
                // each dead people, he will create a new random people on
                // his position.
                if(hasDead){
                    People newBorn = new People();
                    newBorn.position.row = people.position.row;
                    newBorn.position.column = people.position.column;
                    newPeople.add(newBorn);
                }else{
                    newPeople.add(people);
                }
                allPeople = newPeople;

            }
        }else if(model.equals("extension")){
            // Iterate all people in the land, check if there is people dead
            // after a series of actions(move, eat, age)
            ArrayList<People> newPeople = new ArrayList<People>();
            for(People people: allPeople){
                // A boolean indicate if the people is dead.
                boolean hasDead = people.move_eat_age_die();
                Random random = new Random();
                // If there are person dead, remove them from the land. For
                // each dead people, he will create a new born on his position.
                if(hasDead){
                    People newBorn = new People();
                    newBorn.position.row = people.position.row;
                    newBorn.position.column = people.position.column;

                    // new born will inherit a fixed proportion of wealth from
                    // the dead people.
                    double inheritWealth = 0;
                    if(people.wealth <= 0) inheritWealth = 0;
                    else{
                        inheritWealth = people.wealth *
                                Params.WEALTH_INHERIT_PERCENT;
                    }
                    newBorn.wealth = newBorn.wealth + inheritWealth;

                    // Set up a random metabolism within a range for the new
                    // born. The reason why plus 0.5 is we want to get a ceil
                    // value.
                    int metabolism1 = (int) (people.metabolism *
                         (random.nextInt(Params.METABOLISM_INHERIT_PERCENT_MAX
                                 - Params.METABOLISM_INHERIT_PERCENT_MIN) * 0.1
                                 + Params.METABOLISM_INHERIT_PERCENT_MIN * 0.1)
                            +0.5);
                    if (metabolism1 <=1) newBorn.metabolism=1;
                    else if(metabolism1 >= Params.METABOLISM_MAX){
                        newBorn.metabolism = Params.METABOLISM_MAX;
                    }
                    else{
                        newBorn.metabolism = metabolism1;
                    }

                    // Set up a random vision within a range for the new born.
                    // The reason why plus 0.5 is we want to get a ceil value.
                    int vision1 = (int) (people.vision *
                            (random.nextInt(Params.VISION_INHERIT_PERCENT_MAX
                                    - Params.VISION_INHERIT_PERCENT_MIN) * 0.1
                                    + Params.VISION_INHERIT_PERCENT_MIN * 0.1)
                            + 0.5);
                    if (vision1 <=1) newBorn.vision=1;
                    else if (vision1 >= Params.VISION_MAX){
                        newBorn.vision = Params.VISION_MAX;
                    }
                    else{
                        newBorn.vision = vision1;
                    }
                    newPeople.add(newBorn);
                }else {
                    newPeople.add(people);
                }
                allPeople = newPeople;

            }
        }else{
            System.out.println("Wrong spelling \"" +model+ "\" , please " +
                    "input \"basic\" or " + "\"extension\" as your " +
                    "command line arguements. ");
            System.exit(0);
        }


        // Classify the all people by wealth.
        String output = classify();

        // Grow grain for every interval.
        if(module(clock.time(),Params.GRAIN_GROWTH_INTERVAL) == 0){
            for(int i=0; i< width; i++){
                for(int j=0; j<length; j++){
                    double maxGrainHere2 = patches[i][j].maxGrainHere;
                    patches[i][j].growGrain(maxGrainHere2);
                }
            }
        }

        clock.tick();
        return output;
    }

    /**
     * For each people in the land, harvest the grain on his patch in the land.
     * If there are multiple people in one patch, divide the grain evenly among
     * people.  
     */
    public void harvest(){
        for(People people: allPeople){
            people.wealth = Math.floor(people.wealth +
                    (patches[people.position.row]
                            [people.position.column].grainHere)
                            /getPeopleInPatch(people));
        }

        // Set grain number on harvested patch as 0.
        for(People people: allPeople){
            patches[people.position.row][people.position.column].grainHere = 0;
        }
    }

    /**
     * Get the number of people in the Patch.
     * @param people People in that patch.
     * @return The number of people.
     */
    public int getPeopleInPatch(People people){
        int count = 0;
        for(People people1 :allPeople){
            if(people1.position.equals(people.position)){
                count++;
            }
        }

        return count;
    }

    /**
     * Classify all people in the land based on their wealth.
     * @return A string contains the quantity information of each class.
     */
    public String classify(){
        double max_wealth = getMaxWealth(allPeople);
        poor = 0;
        middle = 0;
        rich = 0;
        for(People people: allPeople){
            if(people.wealth <= max_wealth / 3){
                poor++;
            }else if(people.wealth <= max_wealth * 2/3){
                middle++;
            }else{
                rich++;
            }
        }

        // Define the format of return string.
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(poor);
        stringBuilder.append(',');
        stringBuilder.append(middle);
        stringBuilder.append(',');
        stringBuilder.append(rich);
        stringBuilder.append(',');
        String gini = String.format("%.3f", GiniIndex.gini(allPeople));
        stringBuilder.append(gini);
        stringBuilder.append('\n');
        return stringBuilder.toString();
    }

    /**
     * Get the maximum wealth among all people in the land.
     * @param allPeople People in the land.
     * @return The maximum wealth.
     */
    public double getMaxWealth(ArrayList<People> allPeople){
        double max_wealth = allPeople.get(0).wealth;
        for(People people: allPeople){
            if(people.wealth > max_wealth){
                max_wealth = people.wealth;
            }
        }
        return max_wealth;
    }

    /**
     * In our model, a person who across the left boundary will appear on the
     * right boundary. To simulate this kind of movement, module is a good
     * option. But the module method in Java library is not suitable, because
     * it could return a negative value, which is nonsense for a index in an
     * Array.Therefore, we define a new module method, which only return
     * positive value.
     * @param x Dividend
     * @param y Divisor
     * @return The remainder
     */
    public int module(int x, int y ){
        int mod;
        if (x>=0) {
            mod= x%y;
        }else {
            mod =x+y;
        }
        return mod;
    }

}
