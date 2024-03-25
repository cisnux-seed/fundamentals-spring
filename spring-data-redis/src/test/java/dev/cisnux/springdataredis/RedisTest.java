package dev.cisnux.springdataredis;

import dev.cisnux.springdataredis.entity.Product;
import dev.cisnux.springdataredis.repisotory.ProductRepository;
import dev.cisnux.springdataredis.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.dao.DataAccessException;
import org.springframework.data.geo.*;
import org.springframework.data.redis.RedisSystemException;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.support.collections.DefaultRedisMap;
import org.springframework.data.redis.support.collections.RedisList;
import org.springframework.data.redis.support.collections.RedisSet;
import org.springframework.data.redis.support.collections.RedisZSet;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;


@SpringBootTest
public class RedisTest {
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private ProductService productService;

    @Test
    void redisTemplate() {
        assertNotNull(redisTemplate);
    }

    @Test
    void string() throws InterruptedException {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();

        ops.set("name", "fajra", Duration.ofSeconds(2));
        assertEquals("fajra", ops.get("name"));

        Thread.sleep(Duration.ofSeconds(3));
        assertNull(ops.get("name"));
    }

    @Test
    void list() {
        ListOperations<String, String> ops = redisTemplate.opsForList();
        ops.rightPush("names", "cisnux");
        ops.rightPush("names", "fajra");
        ops.rightPush("names", "risqulla");

        assertEquals("cisnux", ops.leftPop("names"));
        assertEquals("fajra", ops.leftPop("names"));
        assertEquals("risqulla", ops.leftPop("names"));
    }

    @Test
    void set() {
        SetOperations<String, String> ops = redisTemplate.opsForSet();
        ops.add("students", "fajra");
        ops.add("students", "fajra");
        ops.add("students", "fajra");
        ops.add("students", "cisnux");
        ops.add("students", "cisnux");
        ops.add("students", "cisnux");
        ops.add("students", "risqulla");
        ops.add("students", "risqulla");

        assertNotNull(ops.members("students"));
        assertEquals(3, ops.members("students").size());
        assertThat(ops.members("students"), hasItems("fajra", "cisnux", "risqulla"));

        redisTemplate.delete("students");

        assertTrue(ops.members("students").isEmpty());
    }

    // sorted set
    @Test
    void zset() {
        ZSetOperations<String, String> operations = redisTemplate.opsForZSet();

        operations.add("score", "cisnux", 100);
        operations.add("score", "fajra", 85);
        operations.add("score", "risqulla", 90);

        assertEquals("cisnux", operations.popMax("score").getValue());
        assertEquals("risqulla", operations.popMax("score").getValue());
        assertEquals("fajra", operations.popMax("score").getValue());
    }

    @Test
    void hash() {
        HashOperations<String, String, String> ops = redisTemplate.opsForHash();
//        ops.put("user:1", "id", "1");
//        ops.put("user:1", "name", "fajra");
//        ops.put("user:1", "email", "fajra@gmail.com");

        Map<String, String> map = new HashMap<>();
        map.put("id", "1");
        map.put("name", "fajra");
        map.put("email", "fajra@gmail.com");

        ops.putAll("user:1", map);

        assertEquals("1", ops.get("user:1", "id"));
        assertEquals("fajra", ops.get("user:1", "name"));
        assertEquals("fajra@gmail.com", ops.get("user:1", "email"));

        redisTemplate.delete("user:1");

        assertFalse(ops.hasKey("students", "id"));
    }

    @Test
    void geo() {
        GeoOperations<String, String> operations = redisTemplate.opsForGeo();

        operations.add("sellers", new Point(106.822695, -6.177456), "Toko A");
        operations.add("sellers", new Point(106.821016, -6.174598), "Toko B");

        Distance distance = operations.distance("sellers", "Toko A", "Toko B", Metrics.KILOMETERS);
        assertEquals(0.3682, distance.getValue());

        GeoResults<RedisGeoCommands.GeoLocation<String>> sellers =
                operations.search("sellers", new Circle(
                        // starting point
                        new Point(106.821922, -6.175491),
                        new Distance(5, Metrics.KILOMETERS)
                ));

        assertNotNull(sellers);
        assertEquals(2, sellers.getContent().size());
        assertEquals("Toko A", sellers.getContent().get(0).getContent().getName());
        assertEquals("Toko B", sellers.getContent().get(1).getContent().getName());
    }


    // to get the number of unique items
    @Test
    void hyperLogLog() {
        HyperLogLogOperations<String, String> operations = redisTemplate.opsForHyperLogLog();

        operations.add("traffics", "cisnux", "fajra", "risqulla");
        operations.add("traffics", "cisnux", "fajra cisnux", "cisnux fajra");
        operations.add("traffics", "fajra cisnux", "cisnux fajra", "cisnux risqulla");

        assertEquals(6L, operations.size("traffics"));
    }

    @Test
    void transactions() {
        redisTemplate.execute(new SessionCallback<>() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                // begin
                operations.multi();

                operations.opsForValue().set("test1", "fajra", Duration.ofSeconds(2));
                operations.opsForValue().set("test2", "cisnux", Duration.ofSeconds(2));

                // commit
                operations.exec();
                return null;
            }
        });

        assertEquals("fajra", redisTemplate.opsForValue().get("test1"));
        assertEquals("cisnux", redisTemplate.opsForValue().get("test2"));
    }

    @Test
    void pipeline() {
        List<Object> statuses = redisTemplate.executePipelined(new SessionCallback<>() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                operations.opsForValue().set("test1", "cisnux", Duration.ofSeconds(2));
                operations.opsForValue().set("test2", "cisnux", Duration.ofSeconds(2));
                operations.opsForValue().set("test3", "cisnux", Duration.ofSeconds(2));
                operations.opsForValue().set("test4", "cisnux", Duration.ofSeconds(2));
                return null;
            }
        });

        System.out.println(statuses);
        assertThat(statuses, hasSize(4));
        assertThat(statuses, hasItem(true));
        assertThat(statuses, not(hasItem(false)));
    }

    @Test
    void publishStream() {
        StreamOperations<String, String, String> ops = redisTemplate.opsForStream();

        MapRecord<String, String, String> record = MapRecord.create("stream-1", Map.of(
                "name", "fajra risqulla",
                "country", "indonesia"
        ));

        for (int i = 0; i < 10; i++) {
            ops.add(record);
        }
    }

    @Test
    void subscribeStream() {
        StreamOperations<String, Object, Object> operations = redisTemplate.opsForStream();

        try {
            operations.createGroup("stream-1", "sample-group");
        } catch (RedisSystemException exception) {
            // group has already existed
        }

        List<MapRecord<String, Object, Object>> records = operations.read(Consumer.from("sample-group", "sample-1"),
//                StreamOffset.create("stream-1", ReadOffset.from("2")));
                StreamOffset.create("stream-1", ReadOffset.lastConsumed()));

        assertNotNull(records);
        System.out.println(records);
        records.forEach(System.out::println);
    }

    @Test
    void pubSub() {
        redisTemplate.getConnectionFactory().getConnection().subscribe((message, _) -> {
            String event = new String(message.getBody());
            System.out.println(STR."Receive message : \{event}");
        }, "my-channel".getBytes());

        for (int i = 0; i < 10; i++) {
            redisTemplate.convertAndSend("my-channel", STR."Hello World : \{i}");
        }
    }

    @Test
    void redisList() {
        List<String> list = RedisList.create("names", redisTemplate);
        list.add("fajra");
        list.add("risqulla");
        list.add("cisnux");
        assertThat(list, hasItems("fajra", "risqulla", "cisnux"));

        List<String> result = redisTemplate.opsForList().range("names", 0, -1);
        assertThat(result, hasItems("fajra", "risqulla", "cisnux"));
    }

    @Test
    void redisSet() {
        Set<String> set = RedisSet.create("traffic", redisTemplate);
        set.addAll(Set.of("fajra", "risqulla", "cisnux"));
        set.addAll(Set.of("fajra", "csx", "fajra risqulla"));
        set.addAll(Set.of("fajra cisnux", "csx", "fajra risqulla"));
        assertThat(set, hasItems("fajra", "risqulla", "csx", "fajra risqulla", "fajra cisnux"));

        Set<String> members = redisTemplate.opsForSet().members("traffic");
        assertThat(members, hasItems("fajra", "risqulla", "csx", "fajra risqulla", "fajra cisnux"));
    }

    @Test
    void redisZSet() {
        // sorted set
        RedisZSet<String> set = RedisZSet.create("winner", redisTemplate);
        set.add("fajra", 100);
        set.add("cisnux", 85);
        set.add("risqulla", 90);
        assertThat(set, hasItems("fajra", "cisnux", "risqulla"));

        Set<String> winner = redisTemplate.opsForZSet().range("winner", 0, -1);
        assertThat(winner, hasItems("fajra", "cisnux", "risqulla"));

        assertEquals("fajra", set.popLast());
        assertEquals("risqulla", set.popLast());
        assertEquals("cisnux", set.popLast());
    }

    @Test
    void redisMap() {
        Map<String, String> map = new DefaultRedisMap<>("user:1", redisTemplate);
        map.put("name", "fajra");
        map.put("address", "Indonesia");
        assertThat(map, hasEntry("name", "fajra"));
        assertThat(map, hasEntry("address", "Indonesia"));

        Map<Object, Object> entries = redisTemplate.opsForHash().entries("user:1");
        assertThat(entries, hasEntry("name", "fajra"));
        assertThat(entries, hasEntry("address", "Indonesia"));
    }

    @Test
    void repository() {
        Product product = Product.builder()
                .id("1")
                .name("Mie Ayam Goreng")
                .price(20_000L)
                .build();
        productRepository.save(product);

        productRepository.findById("1").ifPresent(product2 -> assertEquals(product, product2));

        Map<Object, Object> map = redisTemplate.opsForHash().entries("products:1");
        assertEquals(product.getId(), map.get("id"));
        assertEquals(product.getName(), map.get("name"));
        assertEquals(product.getPrice().toString(), map.get("price"));
    }

    @Test
    void ttl() throws InterruptedException {
        Product product = Product.builder()
                .id("1")
                .name("Mie Ayam Goreng")
                .price(20_000L)
                // 3 seconds
                .ttl(3L)
                .build();
        productRepository.save(product);

        assertTrue(productRepository.findById("1").isPresent());

        Thread.sleep(Duration.ofSeconds(5));
        assertFalse(productRepository.findById("1").isPresent());
    }

    @Test
    void cache() {
        Cache cache = cacheManager.getCache("scores");
        assertNotNull(cache);
        cache.put("fajra", 100);
        cache.put("cisnux", 95);

        assertEquals(100, cache.get("fajra", Integer.class));
        assertEquals(95, cache.get("cisnux", Integer.class));

        cache.evict("fajra");
        cache.evict("cisnux");

        assertNull(cache.get("fajra"));
        assertNull(cache.get("cisnux"));
    }

    @Test
    void cacheable() {
        // call it once
        Product product = productService.getProduct("001");
        assertEquals("001", product.getId());

        // nothing happen
        Product product2 = productService.getProduct("001");
        assertEquals(product, product2);

        // call it once
        Product product3 = productService.getProduct("002");
        assertEquals(product, product2);
        assertNotEquals(product, product3);
    }

    @Test
    void cachePut() {
        Product product = Product.builder().id("P002").name("asal").price(100L).build();
        productService.save(product);

        Product product2 = productService.getProduct("P002");
        System.out.println(product);
        System.out.println(product2);
        assertEquals(product, product2);
    }

    @Test
    void cacheEvict() {
        Product product = productService.getProduct("003");
        assertEquals("003", product.getId());

        productService.remove("003");

        Product product2 = productService.getProduct("003");
        assertEquals(product, product2);
    }
}
