package Runner;

import Controller.*;
import Model.*;
import View.*;

/**
 * Created by Jasper Lankhorst on 17-11-2016.
 */
public class Run {

    public static void main(String[] args) {
        int portNumber = 80;

        if (args.length > 0) {
            try {
                portNumber = Integer.parseInt(args[0]);
            } catch (Exception parseException) {
                System.err.println("No valid portnumber found: returning to basic port 8081");
                portNumber = 80; //assign one if none given.
            }
        }

        Model model = new Model();
        View view = new View();
        Controller controller = new Controller(model, view, portNumber);
    }
}