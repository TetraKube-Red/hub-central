package red.tetracube.hubcentral.api.payloads;

import java.util.List;

public class HubPayload {
    
    public record Reply(
        String slug,
        String name,
        List<RoomPayload> rooms
    ) {

    }

    public record RoomPayload(
        String slug,
        String name
    ) {
        
    }

}
