import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;
@Data
public class CalDistanceResponse {
    int status;
//    String infocode;
    String info;
    int count;
    Route route;


    @Data
    public static class Route {
        String origin;
        String destination;
        int distance;
        @JsonProperty("taxi_cost")
        int taxiCost;
        List<Transit> transits;

        @AllArgsConstructor
        @NoArgsConstructor
        @Data
        @ToString
        public static class Transit{
            float cost;
            int duration;
            int nightflag;
            @JsonProperty("walking_distance")
            int walkingDistance;
            float durationForHour;

        }
    }
}
