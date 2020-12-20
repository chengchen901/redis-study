package com.study.redis.apply.lesson2_othertype;

import com.study.redis.apply.RedisApplyApplicationTests;
import com.study.redis.apply.lesson2_othertype.geo.GeoNearbyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;

/**
 * @author Hash
 * @since 2020/12/12
 */
public class GeoTests extends RedisApplyApplicationTests {

    @Autowired
    GeoNearbyService geoNearbyService;

    @Test
    public void test1() throws InterruptedException {
        // 模拟三个人位置上报
        Point hashPlace = new Point(116.405315, 39.904999);
        geoNearbyService.add(hashPlace, "hash");
        geoNearbyService.add(new Point(116.405285, 39.904989), "xiaoqiao");	// 大乔
        geoNearbyService.add(new Point(116.405265, 39.904969), "daqiao");	// 小乔
        geoNearbyService.add(new Point(116.405065, 39.904959), "liuyan");	// 刘岩
        geoNearbyService.add(new Point(116.405165, 39.904759), "dilireba");	// 迪丽热巴

        // hash查找附近的人
        GeoResults<RedisGeoCommands.GeoLocation> geoResults = geoNearbyService.near(hashPlace, 100);
        for (GeoResult<RedisGeoCommands.GeoLocation> geoResult : geoResults) {
            RedisGeoCommands.GeoLocation content = geoResult.getContent();
            System.out.println(content.getName() + " :" + geoResult.getDistance().getValue());
        }
    }
}
