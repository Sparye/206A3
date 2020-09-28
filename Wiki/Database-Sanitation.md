## 15/09/2020
## database sanitation

Upon seeing the number of errors in the provided question database, our first instinct was to rewrite it.
This would involve fixing every error and replacing the delimiters with a rare string such as <??>. 
However, we saw a Piazza response by Nasser warning developers to avoid replacing the delimiters,
as the testing questions would be in the provided format.

Consequently, we realised we would have to work with the database in its far-from-perfect, original form.
This was the original algorithm we developed for this purpose.

[18:39]
- the question is everything until the "("
[18:39]
- answer prefix is everything from there until the ")"
[18:39]
- answer is everything from there
[18:40]
- remove leading spaces, trailing spaces, trailing commas and full stops
[18:40]
- remove leading and trailing spaces AGAIN
[18:41]
- combine answer prefix and answer to give actual answer
[18:41]
- perform case-insensitive comparison with user attempt

This was later implemented as a function called toVarSet(), which took a database line as input
and returned an array containing the question, answer prefix, and answer.
