package at.coffeebeans.stdrand;

/**
 * Random data generator (32-bit version). This implementation will create reproducible random data based on a seed and
 * the noise and prime numbers given. Not using an interface to stick to primitives.<br/>
 * <br/>
 * <b>!!! Don't use this for security applications as this is NOT a real RNG!!!</b><br/>
 * * <br/>
 * * For 32-bit version see {@link BigRand64}
 */
public class BigRand32 {

    private int defaultSeed = 0;

    // distinctive and non boring bits - no extra requirements
    private int bitNoise1 = 0b01101000111000110001110110100100;
    private int bitNoise2 = 0b10110101001010010111101001001101;
    private int bitNoise3 = 0b00011011010101101100010011101001;

    // large primes with non boring bits for multiplication for more dimensions
    private int prime1 = 0b1011110101001011110010110101;
    private int prime2 = 0b0000011000111101011010001101;

    /**
     * Default constructor using default values.
     */
    public BigRand32() {

    }

    /**
     * Full constructor using custom values.
     */
    @SuppressWarnings("unused")
    public BigRand32(
            final int defaultSeed,
            final int bitNoise1,
            final int bitNoise2,
            final int bitNoise3,
            final int prime1,
            final int prime2
    ) {
        this.defaultSeed = defaultSeed;
        this.bitNoise1 = bitNoise1;
        this.bitNoise2 = bitNoise2;
        this.bitNoise3 = bitNoise3;
        this.prime1 = prime1;
        this.prime2 = prime2;
    }

    /**
     * Generate random number based on x and seed.
     *
     * @param x    Base number for calculation.
     * @param seed Does nothing if 0.
     * @return pseudo random number
     */
    @SuppressWarnings("DuplicatedCode")
    public int randSeeded(
            final int x,
            final int seed
    ) {
        int result = x;
        result *= bitNoise1;
        result += seed;
        result ^= (result >> 8);
        result += bitNoise2;
        result ^= (result << 8);
        result *= bitNoise3;
        result ^= (result >> 8);
        return result;
    }

    // ======================= extra dimensions =======================

    /**
     * Generate random number based on x, y and seed.
     *
     * @param x    Base number for calculation.
     * @param y    Doesn't influence result if 0.
     * @param seed Does nothing if 0.
     * @return pseudo random number
     */
    public int randSeeded(
            final int x,
            final int y,
            final int seed
    ) {
        return randSeeded(x + (prime1 * y), seed);
    }

    /**
     * Generate random number based on x, y, z and seed.
     *
     * @param x    Base number for calculation.
     * @param y    Doesn't influence result if 0.
     * @param z    Doesn't influence result if 0.
     * @param seed Does nothing if 0.
     * @return pseudo random number
     */
    public int randSeeded(
            final int x,
            final int y,
            final int z,
            final int seed
    ) {
        return randSeeded(x + (prime1 * y) + (prime2 * z), seed);
    }

    // ======================= default seed functions =======================

    /**
     * @see BigRand32#randSeeded(int, int) with seed=0
     */
    public int rand(
            final int x
    ) {
        return randSeeded(x, defaultSeed);
    }

    /**
     * @see BigRand32#randSeeded(int, int, int) with seed=0
     */
    public int rand(
            final int x,
            final int y
    ) {
        return randSeeded(x, y, defaultSeed);
    }

    /**
     * @see BigRand32#randSeeded(int, int, int, int) with seed=0
     */
    public int rand(
            final int x,
            final int y,
            final int z
    ) {
        return randSeeded(x, y, z, defaultSeed);
    }

    public static int toSeed(final String str) {
        if (null == str || str.length() < 1) {
            return 0;
        }
        int result = 0;
        for (int i = 0; i < str.length(); i++) {
            result += 0xfff + str.charAt(i);
        }
        return result;
    }
}
