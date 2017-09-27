import controllers.FlowController;
import entities.DeploymentPackage;
import entities.ResultsListener;
import entities.parsing.Node;

import java.util.Scanner;

public class Main {


    public static void main(String[] args){
        FlowController flowController = new FlowController();
        Scanner scanner = new Scanner(System.in);

        System.out.println("After Starting thread");
        while(scanner.hasNext()){
            String line = scanner.next();
            if(line.equals("k")){
                startStuff(flowController);
            }
            System.out.println(line);
        }

    }

    public static void startStuff(FlowController flowController){
        System.out.println("Starting thread");
        flowController.sendPackage(null, new ResultsListener<String>() {
            public void onCompletion(String result) {
                System.out.println(result);
            }

            public void onFailure(Throwable throwable) {
                System.out.println(throwable.getMessage());
            }
        });
    }

}
