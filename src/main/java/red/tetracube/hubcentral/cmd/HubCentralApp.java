package red.tetracube.hubcentral.cmd;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class HubCentralApp {
 
    public static void main(String... args) {
        Quarkus.run(args);
    }

}
