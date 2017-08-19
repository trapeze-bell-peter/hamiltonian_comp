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
something quickly.  It combines a well thought through (although admittedely
slightly quirky) syntax with a very powerful object library.  As a result
you can write in a single line what in other languages such as Java would
probably be many lines of code.

The implementation can be found in the ruby directory.
