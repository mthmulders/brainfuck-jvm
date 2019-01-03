This program adds the numbers 2 and 5

Store the number 2 in the first slot
+ +
>

Store the number 5 in the second slot
+ + + + +

Move back and forth between first and second slot
[
    While "moving" ones from the second to the first slot
    < + > -
]

The first slot now has '7' in it but we need to output the ASCII value for that
The ASCII value is 48 higher than the number we have

The second slot is now empty
Store 8 in it
It act as a counter so we can add 6 * 8 to the first slot
+ + + + + + + +

Again move back and forth between first and second slot
[
    while adding 6 to the first slot
    < + + + + + +
    before reducing the counter in the second slot
    > -
]

Go back to the first slot and print it
< .
