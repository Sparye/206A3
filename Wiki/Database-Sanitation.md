## 15/09/2020
## Database Sanitation

Upon seeing the number of errors in the provided question database, our first instinct was to rewrite it.
This would involve fixing every error and replacing the delimiters with a rare string. 

We then saw a Piazza response by Nasser warning developers to avoid replacing the delimiters,
as the testing questions would be in the provided format.

This made us realise we would have to work with the database in its far-from-perfect, original form.
This was the original algorithm we developed for this purpose.


- the question is everything until the "("
- answer prefix is everything from there until the ")"
- answer is everything from there
- remove leading spaces, trailing spaces, trailing commas and full stops
- remove leading and trailing spaces AGAIN
- combine answer prefix and answer to give actual answer
- perform case-insensitive comparison with user attempt

This was later implemented as a function called toVarSet(), which took a database line as input
and returned an array containing the question, answer prefix, and answer.

### Checking correctness
The user's attempt is then compared with the array using isCorrect(), which:

- Splits the answer string along "/"
- Makes an array of all acceptable answers
- Will accept any answers in the array with or without the answer prefix
- Checks the prefix for correctness (if used)
- Is case-insensitive

If the answer is correct, this function returns true.
