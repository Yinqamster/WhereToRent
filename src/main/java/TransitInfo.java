import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TransitInfo {
    SearchApiResponse.POI poi;
    CalDistanceResponse.Route.Transit totalTransit;
    CalDistanceResponse.Route.Transit destination1Transit;
    CalDistanceResponse.Route.Transit destination2Transit;
    int distance;
    float distanceForKM;
    int taxiCost;
}
