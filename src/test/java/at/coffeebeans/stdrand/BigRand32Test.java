package at.coffeebeans.stdrand;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.SecureRandom;

import static org.assertj.core.api.Assertions.assertThat;

class BigRand32Test {

    private static final int[][] TEST_RAND_SEEDED_X = {
            // x, seed, expected
            {0, 0, 1329773404},
            {1, 5, 1725346581},
            {1, 6, 1655829628},
    };
    private static final int[][] TEST_RAND_SEEDED_XY = {
            // x, y, seed, expected
            {0, 0, 0, 1329773404}, // equals to x=0 with seed=0
            {1, 0, 5, 1725346581}, // equals to x=1 with seed=5
            {1, 0, 6, 1655829628}, // equals to x=1 with seed=6
            {1, 1, 6, 1716393388},
    };
    private static final int[][] TEST_RAND_SEEDED_XYZ = {
            // x, y, z, seed, expected
            {0, 0, 0, 0, 1329773404}, // equals to x=0 with seed=0
            {1, 0, 0, 5, 1725346581}, // equals to x=1 with seed=5
            {1, 0, 0, 6, 1655829628}, // equals to x=1 with seed=6
            {1, 1, 0, 6, 1716393388}, // equals to x=1, y=1 with seed=6
            {1, 1, 1, 6, 795614444}, // equals to x=1, y=1 with seed=6
    };

    private BigRand32 bean;

    @BeforeEach
    void setUp() {
        bean = new BigRand32();
    }

    @Test
    void testRand() {
        final int a = bean.rand(0);
        final int b = bean.rand(0, 0);
        final int c = bean.rand(0, 0, 0);
        assertThat(a).isEqualTo(b).isEqualTo(c);
    }

    @Test
    void testRandSeededX() {
        for (final int[] testRandDatum : TEST_RAND_SEEDED_X) {
            assertThat(testRandDatum).withFailMessage("null test data").isNotNull();
            assertThat(testRandDatum.length).withFailMessage("invalid test data").isEqualTo(3);
            int i = 0;
            final int x = testRandDatum[i++];
            final int seed = testRandDatum[i++];
            final int expected = testRandDatum[i];
            final int actual = bean.randSeeded(x, seed);
            assertThat(actual)
                    .withFailMessage(String.format("x=%d, seed=%d, expected=%d, actual=%d", x, seed, expected, actual))
                    .isEqualTo(expected);
        }
    }

    @Test
    void testRandSeededXY() {
        for (final int[] testRandDatum : TEST_RAND_SEEDED_XY) {
            assertThat(testRandDatum).withFailMessage("null test data").isNotNull();
            assertThat(testRandDatum.length).withFailMessage("invalid test data").isEqualTo(4);
            int i = 0;
            final int x = testRandDatum[i++];
            final int y = testRandDatum[i++];
            final int seed = testRandDatum[i++];
            final int expected = testRandDatum[i];
            final int actual = bean.randSeeded(x, y, seed);
            assertThat(actual)
                    .withFailMessage(
                            String.format("x=%d, y=%d, seed=%d, expected=%d, actual=%d", x, y, seed, expected, actual)
                    )
                    .isEqualTo(expected);
        }
    }

    @Test
    void testRandSeededXYZ() {
        for (final int[] testRandDatum : TEST_RAND_SEEDED_XYZ) {
            assertThat(testRandDatum).withFailMessage("null test data").isNotNull();
            assertThat(testRandDatum.length).withFailMessage("invalid test data").isEqualTo(5);
            int i = 0;
            final int x = testRandDatum[i++];
            final int y = testRandDatum[i++];
            final int z = testRandDatum[i++];
            final int seed = testRandDatum[i++];
            final int expected = testRandDatum[i];
            final int actual = bean.randSeeded(x, y, z, seed);
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
        final SeededXYZ32[] data = SeededXYZ32.generateRandomInstances3D(count);
        System.out.printf("test data generation took %dms%n", System.currentTimeMillis() - genStart);


        final long start = System.currentTimeMillis();
        for (final SeededXYZ32 datum : data) {
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

class SeededXYZ32 {
    private static final SecureRandom RANDOM = new SecureRandom();
    private final int x;
    private final int y;
    private final int z;
    private final int seed;

    public SeededXYZ32(
            final int x,
            final int y,
            final int z,
            final int seed
    ) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.seed = seed;
    }

    public static SeededXYZ32[] generateRandomInstances3D(final int count) {
        final SeededXYZ32[] numbers = new SeededXYZ32[count];
        int i = 0;
        while (i < count) {
            final int r = RANDOM.nextInt();
            numbers[i] = new SeededXYZ32(r - 1, r - 2, r - 3, r - 4);
            i++;
        }
        assertThat(numbers).hasSize(count);
        return numbers;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public int getSeed() {
        return seed;
    }
}
