package at.coffeebeans.stdrand;

/**
 * Random data generator (64-bit version). This implementation will create reproducible random data based on a seed and
 * the noise and prime numbers given. Not using an interface to stick to primitives.<br/>
 * <br/>
 * <b>!!! Don't use this for security applications as this is NOT a real RNG!!!</b><br/>
 * <br/>
 * For 32-bit version see {@link BigRand32}
 */
public class BigRand64 {

    public static final long DEFAULT_SEED = 0;

    // distinctive and non boring bits - no extra requirements
    public static final  long BIT_NOISE_1 = 0b0110100011100011000111011010010001101000111000110001110110100100L;
    public static final  long BIT_NOISE_2 = 0b1011010100101001011110100100110110110101001010010111101001001101L;
    public static final  long BIT_NOISE_3 = 0b0001101101010110110001001110100100011011010101101100010011101001L;

    // large primes with non boring bits for multiplication for more dimensions
    public static final  long PRIME_1 = 0b0000101011111001010001000010000101011100000100000100000011110111L;
    public static final  long PRIME_2 = 0b0000000011010011110000100001101111001111010001100110110110100001L;

    private final long defaultSeed;
    private final long bitNoise1;
    private final long bitNoise2;
    private final long bitNoise3;
    private final long prime1;
    private final long prime2;

    /**
     * Default constructor using default values.
     */
    public BigRand64() {
        this.defaultSeed = DEFAULT_SEED;
        this.bitNoise1 = BIT_NOISE_1;
        this.bitNoise2 = BIT_NOISE_2;
        this.bitNoise3 = BIT_NOISE_3;
        this.prime1 = PRIME_1;
        this.prime2 = PRIME_2;
    }

    /**
     * Full constructor using custom values.
     */
    @SuppressWarnings("unused")
    public BigRand64(
            final long defaultSeed,
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
    public long randSeeded(
            final long x,
            final long seed
    ) {
        long result = x;
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
    public long randSeeded(
            final long x,
            final long y,
            final long seed
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
    public long randSeeded(
            final long x,
            final long y,
            final long z,
            final long seed
    ) {
        return randSeeded(x + (prime1 * y) + (prime2 * z), seed);
    }

    // ======================= default seed functions =======================

    /**
     * @see BigRand64#randSeeded(long, long, long) with seed=0
     */
    public long rand(
            final long x
    ) {
        return randSeeded(x, defaultSeed);
    }

    /**
     * @see BigRand64#randSeeded(long, long, long) with seed=0
     */
    public long rand(
            final long x,
            final long y
    ) {
        return randSeeded(x, y, defaultSeed);
    }

    /**
     * @see BigRand64#randSeeded(long, long, long, long) with seed=0
     */
    public long rand(
            final long x,
            final long y,
            final long z
    ) {
        return randSeeded(x, y, z, defaultSeed);
    }

    public static long toSeed(final String str) {
        if (null == str || str.length() < 1) {
            return 0L;
        }
        long result = 0L;
        for (int i = 0; i < str.length(); i++) {
            result += 0xffff + str.charAt(i);
        }
        return result;
    }
}
