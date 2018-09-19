import java.io.File;
import java.io.PrintWriter;

/**
 * The driver of the simulation
 */

public class Simulate {

    /**
     * Create all components and start simulation.
     */
    public static void main(String[] args){
        PrintWriter printWriter = null;

        //simulate "set up" in NetLogo
        Land land = new Land(Params.LAND_WIDTH, Params.LAND_LENGTH);
        land.initialPatches();
        land.spreadPatches();
        land.initialPeople(Params.NUM_PEOPLE);
        String initialState =land.classify();


        try {
            printWriter = new PrintWriter(new File(Params.OUTPUT_FILE));
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("       Poor");
            stringBuilder.append(',');
            stringBuilder.append("     Middle");
            stringBuilder.append(',');
            stringBuilder.append("       Rich");
            stringBuilder.append(',');
            stringBuilder.append("  Gini Index");
            stringBuilder.append('\n');

            //write "set up" result in output file
            printWriter.write(stringBuilder.toString());
            printWriter.write(initialState);

            // print a prompt if did not select a model
            if(args.length == 0) {
                System.out.print("Please select your model");
                System.out.println(", input \"basic\" or \"extension\" " +
                        "as your command line arguements. ");
                System.exit(0);
            }

            String model = args[0];

            //simulate "go" in NetLogo
            while (land.clock.time() < Params.MODEL_RUN_TIME) {
                String output = land.update(model);

                //write "go" result after each tick in output file
                printWriter.write(output);
            }

            if (model.equals("basic")) {
                System.out.println("Basic simulation finished ! ");
            } else if (model.equals(("extension"))) {
                System.out.println("Extension simulation finished ! ");
            }
        }catch (Exception e){
            System.out.println("Please make sure the output path is valid");
            e.printStackTrace();

        }finally {
            try{
                printWriter.close();
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }


    }

}
