
import com.alibaba.fastjson.JSONObject;

import java.util.*;
import java.util.stream.Collectors;

public class FindPlaceForRent {
    public static String destination1;
    public static String destination2;


    public Set<SearchApiResponse.POI> getAllSubwayStations() {
        Set<SearchApiResponse.POI> result = new HashSet<>();
        Map<String, String> params = new HashMap<>();
        params.put("key", Common.KEY);
        params.put("types", Common.SUBWAY_CODE);
        params.put("city", Common.SHANG_HAI_CODE);

        for (int i = 0; i < Common.SHANG_HAI_DISTRICT.size(); i++) {
            System.out.println("get subway station for " + Common.SHANG_HAI_DISTRICT.get(i));
            params.put("keywords", Common.SHANG_HAI_DISTRICT.get(i));
            params.put("page", "0");
            String resp;
            int count = 0;
            int page = 0;
            SearchApiResponse respObject = new SearchApiResponse();
            try {
                resp = HTTPClient.doGetWithRetry(Common.SEARCH_PLACE, params);
                respObject = JSONObject.parseObject(resp, SearchApiResponse.class);
                result.addAll(respObject.getPois().stream().filter(poi -> poi.getId().startsWith("BV")).collect(Collectors.toSet()));
                count = respObject.count;
                System.out.println("count = " + count);
            } catch (Exception e) {
                e.printStackTrace();
            }


            while (count > 0) {
                page += 1;
                params.put("page", String.valueOf(page));
                try {
                    resp = HTTPClient.doGetWithRetry(Common.SEARCH_PLACE, params);
                    respObject = JSONObject.parseObject(resp, SearchApiResponse.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (respObject == null || respObject.getPois() == null) {
                    page -= 1;
                    continue;
                }
                result.addAll(respObject.getPois().stream().filter(poi -> poi.getId().startsWith("BV")).collect(Collectors.toSet()));
                System.out.println(result.size());
                count = respObject.count;
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }


        System.out.println(result.size());
        return result;
    }

    public void calculate(Set<SearchApiResponse.POI> poiList) {
        List<TransitInfo> result = new ArrayList<>();
        poiList.forEach(poi -> {
            try {
                Map<String, String> params = new HashMap<>();
                params.put("key", Common.KEY);
                params.put("origin", poi.location);
                params.put("destination", destination1);
                params.put("city", Common.SHANG_HAI_CODE);
                params.put("strategy", "0");
                params.put("time", "8:00");
                String resp = HTTPClient.doGetWithRetry(Common.CAL_DISTANCE, params);
                CalDistanceResponse respObject1 = JSONObject.parseObject(resp, CalDistanceResponse.class);

                params.put("destination", destination2);
                resp = HTTPClient.doGetWithRetry(Common.CAL_DISTANCE, params);
                CalDistanceResponse respObject2 = JSONObject.parseObject(resp, CalDistanceResponse.class);

                if (respObject1 == null || respObject2 == null) return;

                TransitInfo transitInfo = new TransitInfo();
                transitInfo.setPoi(poi);
                CalDistanceResponse.Route.Transit shangpu = respObject1.getRoute().getTransits().get(0);
                shangpu.setDurationForHour((float) shangpu.getDuration() / 3600);
                transitInfo.setDestination1Transit(shangpu);
                CalDistanceResponse.Route.Transit zhongjian = respObject2.getRoute().getTransits().get(0);
                zhongjian.setDurationForHour((float) zhongjian.getDuration() / 3600);
                transitInfo.setDestination2Transit(zhongjian);
                CalDistanceResponse.Route.Transit totalTransit = new CalDistanceResponse.Route.Transit();
                totalTransit.setCost(
                        respObject1.getRoute().getTransits().get(0).getCost() + respObject2.getRoute().getTransits().get(0).getCost()
                );
                int totalDuration = respObject1.getRoute().getTransits().get(0).getDuration() + respObject2.getRoute().getTransits().get(0).getDuration();
                totalTransit.setDuration(totalDuration);
                totalTransit.setDurationForHour((float) totalDuration / 3600);
                totalTransit.setWalkingDistance(
                        respObject1.getRoute().getTransits().get(0).getWalkingDistance() + respObject2.getRoute().getTransits().get(0).getWalkingDistance()
                );
                transitInfo.setTotalTransit(totalTransit);
                int totalDistance = respObject1.getRoute().getDistance() + respObject2.getRoute().getDistance();
                transitInfo.setDistance(totalDistance);
                transitInfo.setDistanceForKM((float) totalDistance / 1000);
                transitInfo.setTaxiCost(
                        respObject1.getRoute().getTaxiCost() + respObject2.getRoute().getTaxiCost()
                );
                result.add(transitInfo);
                System.out.println(result.size());
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                System.out.println("Exception!");
                System.out.println(poi.toString());
            }
        });

        result.sort(Comparator.comparingInt(o -> o.getTotalTransit().getDuration()));

        result.forEach(System.out::println);
    }

    public static void main(String[] args) {
        //fill the longitude and latitude for the two company, split with ','. eg: 121.507759,31.315992
        FindPlaceForRent.destination1 = "";
        FindPlaceForRent.destination2 = "";
        FindPlaceForRent client = new FindPlaceForRent();
        client.calculate(client.getAllSubwayStations());

    }
}
