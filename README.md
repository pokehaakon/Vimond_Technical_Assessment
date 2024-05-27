# Solution for Vimond Technical Assessment
### Position: Senior Developer
### Name: Haakon Osmundsen Benning

## How to Build, Run and Test
* This project uses maven!
* Build using `mvn package`
* Run tests using `mvn test`
* Run the program with `java -jar target/Vimond_Technical_Assessment-1.0.jar`

## How to use
The program takes two lines from stdin, 
the first being the included intervals and the second the excluded intervals. 
The lines should consist of comma separated intervals on the form `x-y`, 
where `x` and `y` are integers. 
Negative integers are also supported, so `-5--1` is a valid interval.
The order of the interval also does not matter, and `1-2` and `2-1` are treated as the same.
Finally, either an empty line, or `(none)` for empty inputs. 

The output is the minimal covering with intervals of the elements in the 'included' 
intervals when removing the 'excluded' intervals. 
The intervals are sorted by their starting position.

## Complexity
The algorithm consists of three parts:
* Read and parse input
* Combine overlapping intervals
* Calculate the minimal covering

We let the inputs be `|include| = O(n), |exclude| = O(m)` 


### Read and parse input
As both reading and parsing are linear operation, this is an `O(n) + O(m)` operation

### Combine overlapping intervals
To combine the intervals in a list of intervals, 
we first sort the list (`O(n*log(n))`) and then iterate over it once (`O(n)`)
Therefor this takes time `O(n*log(n)) + O(m*log(m))`

### Calculate the minimal covering
To calculate the minimal covering we first use *Combine overlapping intervals* 
on both the lists of intervals.
Then we iterate over both the lists simultaneously with one index into each of the lists,
one of the indexes is always incremented, and so the loop is no longer than `O(n) + O(m)`

### Total
In total, we get <br>
`O(n) + O(m) + O(n*log(n)) + O(m*log(m)) + O(n) + O(m)` <br>
`= O(n*log(n)) + O(m*log(m))` <br>
And if we let `n + m = N` we get `O(N*log(N))` <br>
We are bounded by the sorting, and cannot expect any better time complexity than this.

