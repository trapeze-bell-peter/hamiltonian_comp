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
our key algorithsm.  Before embarking on this, I wanted to explore the suitability
of some of the more modern languages.  This repository captures our investigation.
This is not the most scientific language comparison, but nonetheless is interesting.

Each implementation allowed us to explore the strengths and weaknesses of the
language both in terms of expressive power, but also execution speed.

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
- Widely used
- Mature
- Good debuggers available

Weaknesses:
- Slow
- Poor support for parallel execution -

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
Unfortunately, under the wraps execution is still single threaded.

Because it is a recent language, there is less support.  In particular, there
is currently no debugger available.
