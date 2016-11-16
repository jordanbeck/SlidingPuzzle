# SlidingPuzzle

This project is to create a classic sliding puzzle game. It currently includes:
- Choosing a variety of difficulties
- Receiving a hint when you are stuck
- Having the puzzle solved for you when you give up
- Keep track of basic records for each difficulty (number of wins, least number of moves, best time)

There are two things about this project that may need a bit of explanation; Controllers and Stores. These are tools that I have worked on over my time as an Android engineer and I find them to be extremely handy.

## Controllers

I’m not a fan of Fragments. There are several reasons for this, but mainly I’ve found that managing multiple life-cycles brings with it many headaches. I’ve been working with my controller implementation for a while, but am only now attempting to formalize it. Unfortunately, it’s not ready yet, so this project only has a very simple implementation of controllers. Controllers should contain all interactions with any relevant views. If you use Fragments, they will fill a similar space with hopefully less overhead.

## Stores

Stores are a newer development for me. They are used in an attempt remove as much business logic from Android specific code allowing me to leverage more JUnit tests. For this specific project, I actually added a store for a view which was a first for me. But I found that it still worked remarkably well. I was able to test run an entire puzzle and a solution without ever creating an UI. Store should ideally be owned by a level higher than they are needed and either passed it or injected. I have also used singleton stores before, but there was no need for this project.