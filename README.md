# hamiltonian_comp

## Introduction
Much of what Trapeze does is around solving various versions of the travelling
salesman problem. Finding a [Hamiltonian path](https://en.wikipedia.org/wiki/Hamiltonian_path)
is a related but simpler problem.  The aim is to find a path in an undirected
graph that visits all nodes exactly once.

A few years back I ran a small programming competition at a conference of our
senior developers.  The goal was to find a Hamiltonian path for the eastern
states of the USA.  I gave them a partial implementation in Ruby and they were
then required to fill out the blanks.  None of the attendees had written Ruby
before.

More recently, we have started looking at implementing more modern versions of
our key algorithms.  Before embarking on this, I wanted to explore the suitability
of some of the more modern languages.  This repository captures our investigation.
This is not the most scientific language comparison, but nonetheless is interesting.

Each implementation allowed us to explore the strengths and weaknesses of the
language both in terms of expressive power, but also execution speed.

> Neil has just added Java to it.  Neil did the sensible thing of integrating
> timings into the actual code, and running the code multiple times.  There
> are two advantages to this approach: we take out the file reading aspect
> which is of less importance to us, we remove initial system startup time,
> and using multiple iterations ensures the measurement is more significant.
> I will refactor my examples to do the same.

## Ruby - the original implementation
Ruby is my personal goto language when I want to explore a concept and implement
something quickly.  It combines a well thought through (although admittedly
slightly quirky) syntax with a very powerful object library.  As a result
you can write in a single line what in other languages such as Java would
probably be many lines of code.

Ruby is interpreted.  Part of Ruby's power comes from the fact that it is a
strongly typed, but dynamically type checked language.  The downside to this
approach is that the language is quite slow.  Also, to make the language
thread-safe, the Ruby standard interpreter has the global interpreter lock
which ensures only one thread executes at a time, even when running on a
multi-core machine.

The implementation can be found in the [ruby](ruby/hamiltonian.rb) directory.
When I originally constructed the problem I actually created a neighbour's graph
for the whole mainland USA (excluding Alaska and Hawaii).  However, it took too
long to run, so I wrote some code to reduce the graph to the Eastern half.
I have left it in, as it helps explore a language and its ability to manipulate
hash maps and lists.

```console
$ time ruby hamiltonian.rb
[:wdc, :md, :wv, :ky, :tn, :va, :nc, :sc, :ga, :fl, :al, :ms, :la, :ar, :mo, :ia, :mn, :wi, :il, :in, :mi, :oh, :pa, :de, :nj, :ny, :ct, :ri, :ma, :vt, :nh, :me]

real	0m0.878s
user	0m0.867s
sys	0m0.009s
$
```

Strengths or Ruby:
- Expressive language
- Pure OO - everything is an object
- Widely used
- Mature
- Good debuggers available

Weaknesses:
- Slow
- Poor support for parallel execution

## Crystal
[Crystal](https://crystal-lang.org/) is a new language inspired by Ruby.  It aims
to provide a syntax that is similar to Ruby without being absolutely compatible.

The main difference to Ruby is that the language is compiled.  It is also statically
typed.  An interesting aspect of the language is that the compiler will reason about
what type a variable can be.  This means that in many instances the compiler is
able to guess the permissible types for a variable without the programmer
needing to spell this out.

The language has inbuilt support for concurrency using the
[Communicating Sequential Processes](https://en.wikipedia.org/wiki/Communicating_sequential_processes) model.
Unfortunately, under the wraps execution is still single threaded.  There is
an experimental branch in the Github repository that has true multi-threaded support.

Because it is a recent language, there is less tool support.  In particular, there
is currently limited debugging available.

```console
# crystal build hamiltonian.cr
# time ./hamiltonian
complete journey = wdc -> md -> wv -> ky -> tn -> va -> nc -> sc -> ga -> fl -> al -> ms -> la -> ar -> mo -> ia -> mn -> wi -> il -> in -> mi -> oh -> pa -> de -> nj -> ny -> ct -> ri -> ma -> vt -> nh -> me

real	0m0.239s
user	0m0.231s
sys	0m0.004s
```
This shows the language is pretty fast.

Strengths or Crystal:
- Expressive language
- Pure OO - everything is an object
- Very fast

Weaknesses:
- Small community so less support
- Tool chain still in development
- For now, true multi-threaded behaviour is experimental



## Elixir
[Elixir](https://elixir-lang.org/) is a functional programming language based
on Erlang.  Its syntax is rubyesque.  Key features of functional programming
languages are:

* everything is immutable; once a value has been assigned to a variable it
  cannot change
* instead of methods, we have functions; these take a set of inputs and return
  an output  
* instead of if-then-else statements, functional languages rely on pattern
  matching
* instead of loops, functional languages rely on recursion to achieve the same

People are excited about functional languages for two reasons: firstly,
it is easier to reason about whether a program is correct (at least according
to the functional programming disciples), and secondly, because of their structure
it is easier to produce concurrent programs.  This should be a significant advantage
in today's multi-core environment.

While I get all these advantages, as an OO programmer, I find the mental jump
to writing functional programs a big step.  It does not come naturally for me.

The following shows how to pre-compile and then run the code.  
```console
# elixirc hamiltonian.exc

# time elixir -e Hamiltonian.run
Solution: [:wdc, :md, :wv, :ky, :tn, :va, :nc, :sc, :ga, :fl, :al, :ms, :la, :ar, :mo, :ia, :mn, :wi, :il, :in, :mi, :oh, :pa, :de, :nj, :ny, :ct, :ri, :ma, :vt, :nh, :me]

real	0m0.485s
user	0m0.491s
sys	0m0.051s
```

It's faster than Ruby although not as much faster as I expected.  If Ruby would
support a pre-compile stage, I suspect the speed advantage would be even less.

Strengths or Elixir:
- Elixir/Erlang has a strong runtime environment making deploying large multi-core
  applications easy; this is why the Whatsapp backend is written in Erlang
- Very robust

Weaknesses:
- Functional programming takes getting used to
- Not as fast as compiled OO languages

## Java
Thanks to Neil for implementing this.

Java is one of the two most popular languages around today (the other being C#).
As such it felt important to include it in our evaluation since it is a very
useful reference point.

Compared to Ruby, Java's syntax is not as expressive leading to more lines of
code - although my Java friends tell me that will all change with Java 9.  While
it is fairly OO it does also have primitive types which does sometimes lead to
more complex code.  That said, it is blazingly fast, you will always be able to find
an answer to your questions on Stackoverflow, and there are lots of libraries for
it.

```console
# mvn clean package

... lots of output from the build processes

# cd target
# java -jar hamiltonian-path-1.0.0-SNAPSHOT.jar
Java Hamiltonian Path
Found Path
[wdc, md, wv, ky, tn, va, nc, sc, ga, fl, al, ms, la, ar, mo, ia, mn, wi, il, in, mi, oh, pa, de, nj, ny, ct, ri, ma, vt, nh, me]
Run 1 : Duration: 177ms
Found Path
[wdc, md, wv, ky, tn, va, nc, sc, ga, fl, al, ms, la, ar, mo, ia, mn, wi, il, in, mi, oh, pa, de, nj, ny, ct, ri, ma, vt, nh, me]
Run 2 : Duration: 135ms
Found Path
[wdc, md, wv, ky, tn, va, nc, sc, ga, fl, al, ms, la, ar, mo, ia, mn, wi, il, in, mi, oh, pa, de, nj, ny, ct, ri, ma, vt, nh, me]
Run 3 : Duration: 138ms
Found Path
[wdc, md, wv, ky, tn, va, nc, sc, ga, fl, al, ms, la, ar, mo, ia, mn, wi, il, in, mi, oh, pa, de, nj, ny, ct, ri, ma, vt, nh, me]
Run 4 : Duration: 133ms
Found Path
[wdc, md, wv, ky, tn, va, nc, sc, ga, fl, al, ms, la, ar, mo, ia, mn, wi, il, in, mi, oh, pa, de, nj, ny, ct, ri, ma, vt, nh, me]
Run 5 : Duration: 138ms

```

One interesting observation is that because of the way the JIT compiler works
the code gets faster as we repeat it.  Here is the output if I time the execution
using the UNIX time command:

```console
real	0m0.845s
user	0m0.985s
sys	0m0.027s
```

Allowing for the fact that we do the search 5 times, this puts the execution speed
at around 200ms per iteration, slightly ahead of Crystal.

Pros:
- maturity
- speed
- availability of 3rd party libraries
- great tool libraries including debuggers
- good concurrency support

Negatives:
- C++-derived syntax can be a bit cumbersome
- not pure OO
- quite restrictive static typing scheme
