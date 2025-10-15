---
apply: always
---

# AI Rules for mamKaca

MamKaca.pl to aplikacja webowa wspierająca osoby w procesie wychodzenia z uzależnienia od alkoholu. Głównym celem jest budowanie zdrowych nawyków poprzez codzienne śledzenie trzeźwości oraz dostarczanie wsparcia i porad w trudnych momentach.


## BACKEND

### Guidelines for JAVA

#### SPRING_BOOT

- Use Spring Boot for simplified configuration and rapid development with sensible defaults
- Prefer constructor-based dependency injection over `@Autowired`
- Avoid hardcoding values that may change externally, use configuration parameters instead
- For complex logic, use Spring profiles and configuration parameters to control which beans are injected instead of hardcoded conditionals
- If a well-known library simplifies the solution, suggest using it instead of generating a custom implementation
- Use DTOs as immutable `record` types
- Use Bean Validation annotations (e.g., `@Size`, `@Email`, etc.) instead of manual validation logic
- Use `@Valid` on request parameters annotated with `@RequestBody`
- Use custom exceptions for business-related scenarios
- Centralize exception handling with `@ControllerAdvice` and return a consistent error DTO: `{{error_dto}}`
- REST controllers should handle only routing and I/O mapping, not business logic
- Use SLF4J for logging instead of `System.out.println`
- Prefer using lambdas and streams over imperative loops and conditionals where appropriate
- Use `Optional` to avoid `NullPointerException`


## DATABASE

### Guidelines for SQL

#### POSTGRES

- Use connection pooling to manage database connections efficiently
- Implement JSONB columns for semi-structured data instead of creating many tables for {{flexible_data}}
- Use materialized views for complex, frequently accessed read-only data


## CODING_PRACTICES

### Guidelines for VERSION_CONTROL

#### GIT

- Use conventional commits to create meaningful commit messages
- Use feature branches with descriptive names following {{branch_naming_convention}}
- Write meaningful commit messages that explain why changes were made, not just what
- Keep commits focused on single logical changes to facilitate code review and bisection
- Use interactive rebase to clean up history before merging feature branches
- Leverage git hooks to enforce code quality checks before commits and pushes

#### GITHUB

- Use pull request templates to standardize information provided for code reviews
- Implement branch protection rules for {{protected_branches}} to enforce quality checks
- Configure required status checks to prevent merging code that fails tests or linting
- Use GitHub Actions for CI/CD workflows to automate testing and deployment
- Implement CODEOWNERS files to automatically assign reviewers based on code paths
- Use GitHub Projects for tracking work items and connecting them to code changes


### Guidelines for DOCUMENTATION

#### SWAGGER

- Define comprehensive schemas for all request and response objects
- Use semantic versioning in API paths to maintain backward compatibility
- Implement detailed descriptions for endpoints, parameters, and {{domain_specific_concepts}}
- Configure security schemes to document authentication and authorization requirements
- Use tags to group related endpoints by resource or functional area
- Implement examples for all endpoints to facilitate easier integration by consumers

#### DOC_UPDATES

- Update relevant documentation in /docs when modifying features
- Keep README.md in sync with new capabilities
- Maintain changelog entries in CHANGELOG.md


### Guidelines for SUPPORT_LEVEL

#### SUPPORT_EXPERT

- Favor elegant, maintainable solutions over verbose code. Assume understanding of language idioms and design patterns.
- Highlight potential performance implications and optimization opportunities in suggested code.
- Frame solutions within broader architectural contexts and suggest design alternatives when appropriate.
- Focus comments on 'why' not 'what' - assume code readability through well-named functions and variables.
- Proactively address edge cases, race conditions, and security considerations without being prompted.
- When debugging, provide targeted diagnostic approaches rather than shotgun solutions.
- Suggest comprehensive testing strategies rather than just example tests, including considerations for mocking, test organization, and coverage.


