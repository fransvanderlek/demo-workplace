package org.intelligentindustry.demoworkplace;

import org.apache.camel.main.Main;

public class MainApp {

	public static void main(String... args) throws Exception {
		
		System.out.println("Starting MainApp");
		
        Main main = new Main();
        main.configure().addRoutesBuilder(new DemoWorkplaceRouteBuilder());
        main.run(args);
     
    }
}
