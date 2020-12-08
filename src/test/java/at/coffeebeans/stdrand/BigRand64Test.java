package at.coffeebeans.stdrand;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.SecureRandom;

import static org.assertj.core.api.Assertions.assertThat;

class BigRand64Test {

    private static final long[][] testRandDataX = {
            // x, seed, expected
            {0, 0, 5349809100916177756L},
            {1, 5, 2239916183854428949L},
            {1, 6, 1960997778908116092L},
    };
    private static final long[][] testRandDataXY = {
            // x, y, seed, expected
            {0, 0, 0, 5349809100916177756L}, // equals to x=0 with seed=0
            {1, 0, 5, 2239916183854428949L}, // equals to x=1 with seed=5
            {1, 0, 6, 1960997778908116092L}, // equals to x=1 with seed=6
            {1, 1, 6, 9200914567271084271L}, // equals to x=1 with seed=6
    };
    private static final long[][] testRandDataXYZ = {
            // x, y, z, seed, expected
            {0, 0, 0, 0, 5349809100916177756L}, // equals to x=0 with seed=0
            {1, 0, 0, 5, 2239916183854428949L}, // equals to x=1 with seed=5
            {1, 0, 0, 6, 1960997778908116092L}, // equals to x=1 with seed=6
            {1, 1, 0, 6, 9200914567271084271L}, // equals to x=1, y=1 with seed=6
            {1, 1, 1, 6, 1463478602993761526L},
    };

    private BigRand64 bean;

    @BeforeEach
    void setUp() {
        bean = new BigRand64();
    }

    @Test
    void testRand() {
        final long a = bean.rand(0);
        final long b = bean.rand(0, 0);
        final long c = bean.rand(0, 0, 0);
        assertThat(a).isEqualTo(b).isEqualTo(c);
    }

    @Test
    void testRandSeededX() {
        for (final long[] testRandDatum : testRandDataX) {
            assertThat(testRandDatum).withFailMessage("null test data").isNotNull();
            assertThat(testRandDatum.length).withFailMessage("invalid test data").isEqualTo(3);
            int i = 0;
            final long x = testRandDatum[i++];
            final long seed = testRandDatum[i++];
            final long expected = testRandDatum[i];
            final long actual = bean.randSeeded(x, seed);
            assertThat(actual)
                    .withFailMessage(String.format("x=%d, seed=%d, expected=%d, actual=%d", x, seed, expected, actual))
                    .isEqualTo(expected);
        }
    }

    @Test
    void testRandSeededXY() {
        for (final long[] testRandDatum : testRandDataXY) {
            assertThat(testRandDatum).withFailMessage("null test data").isNotNull();
            assertThat(testRandDatum.length).withFailMessage("invalid test data").isEqualTo(4);
            int i = 0;
            final long x = testRandDatum[i++];
            final long y = testRandDatum[i++];
            final long seed = testRandDatum[i++];
            final long expected = testRandDatum[i];
            final long actual = bean.randSeeded(x, y, seed);
            assertThat(actual)
                    .withFailMessage(
                            String.format("x=%d, y=%d, seed=%d, expected=%d, actual=%d", x, y, seed, expected, actual)
                    )
                    .isEqualTo(expected);
        }
    }

    @Test
    void testRandSeededXYZ() {
        for (final long[] testRandDatum : testRandDataXYZ) {
            assertThat(testRandDatum).withFailMessage("null test data").isNotNull();
            assertThat(testRandDatum.length).withFailMessage("invalid test data").isEqualTo(5);
            int i = 0;
            final long x = testRandDatum[i++];
            final long y = testRandDatum[i++];
            final long z = testRandDatum[i++];
            final long seed = testRandDatum[i++];
            final long expected = testRandDatum[i];
            final long actual = bean.randSeeded(x, y, z, seed);
            assertThat(actual)
                    .withFailMessage(
                            String.format("x=%d, y=%d, z=%d, seed=%d, expected=%d, actual=%d",
                                    x, y, z, seed, expected, actual
                            )
                    )
                    .isEqualTo(expected);
        }
    }

    @Test
    void testRandSeededWithRandNumbers() {
        final int count = 10_000_000;
        int cnt = 0;

        final long genStart = System.currentTimeMillis();
        final SeededXYZ64[] data = SeededXYZ64.generateRandomInstances3D(count);
        System.out.printf("test data generation took %dms%n", System.currentTimeMillis() - genStart);


        final long start = System.currentTimeMillis();
        for (final SeededXYZ64 datum : data) {
            bean.randSeeded(datum.getX(), datum.getY(), datum.getZ(), datum.getSeed());
            cnt++;
        }
        final long end = System.currentTimeMillis();

        assertThat(cnt).isEqualTo(count);
        final long duration = end - start;
        final double singleEntryDuration = (double) duration / (double) count;
        final long perSec = (long) (1.0 / singleEntryDuration);
        System.out.printf("%d entries in %dms (%d entries/s)%n", count, duration, perSec);
    }

}

class SeededXYZ64 {
    private static final SecureRandom RANDOM = new SecureRandom();
    private final long x;
    private final long y;
    private final long z;
    private final long seed;

    public SeededXYZ64(
            final long x,
            final long y,
            final long z,
            final long seed
    ) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.seed = seed;
    }

    public static SeededXYZ64[] generateRandomInstances3D(final int count) {
        final SeededXYZ64[] numbers = new SeededXYZ64[count];
        int i = 0;
        while (i < count) {
            final long r = RANDOM.nextLong();
            numbers[i] = new SeededXYZ64(r - 1, r - 2, r - 3, r - 4);
            i++;
        }
        assertThat(numbers).hasSize(count);
        return numbers;
    }

    public long getX() {
        return x;
    }

    public long getY() {
        return y;
    }

    public long getZ() {
        return z;
    }

    public long getSeed() {
        return seed;
    }
}