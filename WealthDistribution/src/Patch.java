/**
 * A class representing a patch in "netlogo" which holds grain
 */
public class Patch {
    //the current amount of grain on this patch
    public double grainHere;

    // the maximum amount of grain this patch can hold
    public double maxGrainHere;

    // Set the current amount of grain and the maximum amount of grain on this
    // patch be 0 initially.
    public Patch(){
        this.maxGrainHere = 0;
        this.grainHere = 0;
    }

    /**
     * Patch procedure: the grain on each patch can grow in some cases
     *
     * @param maxGrainHere
     *            the maximum amount of grain one patch can hold
     */
    public void growGrain(double maxGrainHere){

        if(grainHere < maxGrainHere){
            //if a patch does not have it's maximum amount of grain
            //and the new amount of grain on a patch is not over its maximum
            //add num-grain-grown to its grain amount
            if(grainHere+Params.GRAIN_GROWN< maxGrainHere){
                grainHere += Params.GRAIN_GROWN;
            }

            //if the new amount of grain on a patch is over its maximum, set
            // it to its maximum
            else{
                grainHere  = maxGrainHere;
            }
        }
    }

    //Print out the maximum amount of grain this patch can hold
    public void get(){
        System.out.println("grain: " + maxGrainHere);
    }

}
