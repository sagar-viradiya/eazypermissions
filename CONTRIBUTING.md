# Contributing to Eazy Permissions

First thing first, Thank you for taking time and deciding to contribute :tada:

## Before you start
At a high level, this project is divided into 5 modules(including sample module) as follow.

1. common - As you must have guessed this module contains common things shared by coroutinepermission and livedatapermission module.
2. coroutinepermission - Contains coroutines specific implementation for requesting and observing permission.
3. livedatapermission - Contains livedata specific implementation for requesting and observing permission.
4. dslpermission - Contains Kotlin DSL specific implementation for requesting and observing permission.
5. sample - Shows how to use library.

## How Can I Contribute?

### Reporting Bugs
Bugs are tracked as [GitHub issues](https://guides.github.com/features/issues/). Before opening issue for reporting bug please check existing list of issues as chances are it's already open by someone.
Explain the problem and include additional details to help maintainers reproduce the problem:

* **Use a clear and descriptive title** for the issue to identify the problem.
* **Describe the exact steps which reproduce the problem** in as many details as possible.
* **Provide specific examples to demonstrate the steps**. Include links to files or GitHub projects, or copy/pasteable snippets, which you use in those examples. If you're providing snippets in the issue, use [Markdown code blocks](https://help.github.com/articles/markdown-basics/#multiple-lines).
* **Describe the behavior you observed after following the steps** and point out what exactly is the problem with that behavior.
* **Explain which behavior you expected to see instead and why.**
* **Include screenshots and animated GIFs** to clearly demonstrate the problem.

### Suggesting Enhancements
Enhancement suggestions are tracked as [GitHub issues](https://guides.github.com/features/issues/). Create an issue and provide the following information:

* **Use a clear and descriptive title** for the issue to identify the suggestion.
* **Provide a step-by-step description of the suggested enhancement** in as many details as possible.
* **Provide specific examples to demonstrate the steps**. Include copy/pasteable snippets which you use in those examples, as [Markdown code blocks](https://help.github.com/articles/markdown-basics/#multiple-lines).
* **Describe the current behavior** and **explain which behavior you expected to see instead** and why.
* **Include screenshots and animated GIFs** which help you demonstrate the steps.
* **Explain why this enhancement would be useful**.

### Pull Requests
Below are the few points that you should care to have your contribution considered by the maintainers:

1. Please make sure to open PR against `master` branch. 
2. Commit message should have proper title and discription of all changes.
3. Follow the official kotlin code [styleguides](https://kotlinlang.org/docs/reference/coding-conventions.html)
4. After you submit your pull request, verify that all [status checks](https://help.github.com/articles/about-status-checks/) are passing <details><summary>What if the status checks are failing?</summary>If a status check is failing, and you believe that the failure is unrelated to your change, please leave a comment on the pull request explaining why you believe the failure is unrelated. A maintainer will re-run the status check for you.
