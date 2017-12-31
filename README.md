# ReToNfa
1. Construct an NFA for the given regular expression using Thomson’s
algorithm.
<br>
Objective: learner will be able to design and implement a RE to NFA converter
<br>
Prerequisite: learner should be able to draw NFA manually for the given Regular
Expression using Thompsons algorithm
<br>
Pre-lab exercise: String and File Handling in C or Java.
<br>
Procedure - Thompson’s Algorithm:
<br>
Use the regular expression definition rules as a basis for the construction:
<br>1. Case epsilon : The NFA representing the empty string is:
<br>0 epsilon 1
<br>2. Case a : If the regular expression is just a single character ‘a’, then
<br>the corresponding NFA is :
<br>0 a 1
<br>3. Case a|b : The union operator is represented by a choice of transitions
<br>from a node
<br>0 a 1 0 b 1
<br>4. Case ab : Concatenation simply involves connecting one NFA to the other
<br>0 a 1 1 b 2
<br>5. Case a* : The Kleene closure must allow for taking zero or more instances of the
<br>letter
<br>0 epsilon 1 1 a 2 2 epsilon 3
<br>0 epsilon 3 2 epsilon 1
