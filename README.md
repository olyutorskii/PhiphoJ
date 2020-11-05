# PhiphoJ #

![Java CI with Maven](https://github.com/olyutorskii/PhiphoJ/workflows/Java%20CI%20with%20Maven/badge.svg)
![CodeQL](https://github.com/olyutorskii/PhiphoJ/workflows/CodeQL/badge.svg)

-----------------------------------------------------------------------

## What is PhiphoJ ? ##

* **PhiphoJ** is a Java library
that supports primitive-type array based ring buffer.

* It's not generics container like java.util.Queue&lt;E&gt;.
There is no boxing/unboxing. It's GC-friendly.

* It provides fast random access in ring buffer.

* Ring buffers are also called Circular buffer, FIFO or Queue.


## How to build ##

* PhiphoJ needs to use [Maven 3.3.9+](https://maven.apache.org/)
and JDK 1.8+ to be built.

* PhiphoJ runtime does not depend on any other library at all.
Just compile Java sources under `src/main/java/`
if you don't use Maven nor JUnit.


## License ##

* Code is under [The MIT License][MIT].


## Project founder ##

* By [Olyutorskii](https://github.com/olyutorskii) at 2020


## Key technology ##

- [Circular buffer][CRB]


[MIT]: https://opensource.org/licenses/MIT
[CRB]: https://en.wikipedia.org/wiki/Circular_buffer


--- EOF ---
