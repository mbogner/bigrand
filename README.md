# bigrand

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
[![Build Status](https://travis-ci.org/mbogner/bigrand.svg?branch=develop)](https://travis-ci.org/mbogner/bigrand)
[![codecov](https://codecov.io/gh/mbogner/bigrand/branch/develop/graph/badge.svg)](https://codecov.io/gh/mbogner/bigrand)

Library to create a big amount of random numbers in a reproducible way. Don't use this for anything security related!
This only generates pseudo random numbers based on simple calculations and bit shifts. It is amazingly fast generating
random data for different seeds.

This library is based on an idea from a GDC talk found on youtube (https://www.youtube.com/watch?v=LWFzPP8ZbdU) to
generate random numbers based on a seeded function instead of using real random. This approach is much faster and can
calculate every number in the sequence whenever you need it without calculating all predecessors or relying on
cryptographical functions like md5 or sha which are more complex and much slower. As every number can be generated in a
very fast and reliable way there is no real need for a cache. So it has very low memory usage as well.

bigrand can be used if you need to create a big amount of random numbers that should not have an obvious pattern. I
wrote the algorithm based on int32 and int64. There is an implementation based on integer and one based on long.
Choosing correct numbers and primes isn't that easy but from my experiments the results were good enough for both. To
allow custom numbers both implementations also have a full constructor with which you are able to choose the noise and
prime numbers on your own.

In the tests I create big amount of really random numbers with SecureRandom and then run the algorithm for the same
amount based on the random numbers generated before. The time difference is very obvious if I didn't miss an important
part.

Feel free to create merge requests if you have ideas for optimizations.

## Build

Checkout the project with git clone, cd to the checked out project root and run

`./gradlew clean build`

### Dependencies

Zero dependencies strategy.

This project is based on Java 8 and has no build or runtime dependencies. I also tested the project with Java 11 without
any problems.

There is just a dependency to junit and assertj for tests and assertions.