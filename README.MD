# Introduction
This is a spring boot application to manage quotes.
This application allows you:
 - create/get a user account
 - add/get/update and delete quotes(include get random quote, get list of quotes, get list of sort quotes with limit)
 - add/get/update and delete vote

## Technology Stack
- Java 18
- Spring Boot
- In-memory database H2

## API Endpoints

The API provides the following endpoints:

### User
- POST /users: Creates a new user account. The request must include: name, email, password.
- GET /users/{id}: Gets a user account by id.

### Quote
- GET /quotes: Returns a list of all quotes.
- GET /quotes/sort: Returns a list of all quotes sorted by filed `score` in given order with given limit.
- GET /quotes/{id}: Gets a quote by id.
- POST /quotes: Creates a new quote. The request must include: content, user id.
- PUT /quotes/{id}: Updates an existing quote.
- DELETE /quotes/{id}: Deletes a quote(real delete from db, also delete all votes for quote).
- GET /quotes/random: Returns a random quote.

### Vote
- POST /votes: Adds a vote for a quote. The request must include: quote id, user id, type of vote.
- PUT /votes/{id}: Updates an existing vote.
- GET /votes/{id}: Returns a specific vote by id.
- DELETE /votes/{id}: Deletes a vote(set flag `isActive=false` in db).
- GET /votes/history/{quoteId}: Returns a history of votes for given quote(show all votes, including votes that have been deleted or are inactive).
- GET /votes/active/{quoteId}: Returns only all active votes for given quote.

## Score Calculation
The score for each quote is calculated based on the votes it receives. The rules for score calculation are as follows:

- Adding an upvote: +1
- Adding a downvote: -1
- Updating a vote from up to down: -2
- Updating a vote from down to up: +2
- Deleting an upvote: -1
- Deleting a downvote: +1

Note: The score is updated every time a vote is added, updated, or deleted.