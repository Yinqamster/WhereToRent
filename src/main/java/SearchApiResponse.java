import lombok.*;

import java.util.List;

@Data
public class SearchApiResponse {
    int status;
    String infocode;
    String info;
    int count;
    List<POI> pois;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    @EqualsAndHashCode
    public static class POI {
        String id;
        String adname;
        String name;
        String address;
        String location;
    }
}
