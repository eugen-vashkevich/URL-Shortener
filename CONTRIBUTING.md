# Contribution Guidelines

## How to create a feature request or bug report

Before submitting a new issue, please search for an open or closed issue that relates to your submission to avoid duplicates.

- To create a new product request use [a new issue template](https://github.com/eugen-vashkevich/URL-Shortener/issues/new?template=a-new-issue-template.md).
- To create a new bug report use [a bug report template](https://github.com/eugen-vashkevich/URL-Shortener/issues/new?template=a-new-bug-report.md).

Please follow the rules when creating a new issue:
- Use a clear and descriptive title;
- Describe the task comprehensively (see the hints in the issue template);

## How to contribute code and create PR

The only way to make the changes in the codebase is to merge the PR via the review process.

Make sure there's an issue describing the changes you're making. Every PR must address the issue (newly created or existing).

Changes for the corresponding PR should be made in a separate branch. When you're ready to integrate the changes, you have to create a new PR targeting the `main` branch pass the review process.

### Branch naming

A short-lived branches must follow the naming pattern:

```
<#issue>-<short-description>
```

where:
- `<#issue>` - is the number of the corresponding issue that the PR addresses.
- `<short-description>` - is a short description of the issue or bug you're addressing. It could be a copy of the issue title sometimes.

  Example:

  ```
  2-create-contributing-guidelines
  ```

### Commit Message Guidelines

Please follow the guidelines regarding commit messages:
- Every commit message must start with a capitalized verb in the Present Simple tense;
- The commit message should clearly describe what work has been done;
- Every commit should be compilable if it makes sense;
- Every commit should contain a small bunch of logically-related changes, that allows easy cherry-picking.

  Examples:

  ```
  Adds an info about a user.
  Updates API documentation: remove useless section.
  ```

### Submitting a Pull Request

Please follow the rules when creating a new PR:
- To create a new PR use [a new PR template](.github/PULL_REQUEST_TEMPLATE/pull-request-template.md);
- Use a clear and descriptive title;
- Describe the PR comprehensively (see the hints in the PR template);
- Submit changes related only to the corresponding issue;
- The code you have added/changed should have corresponding tests if that makes sense;
- The PR mustn't introduce any new regression in the existing tests;
- The PR must pass [GitHub CI/CD checks](https://docs.github.com/en/actions).

### Code Style Guide

The code must follow the code style guides:
- For Java, to keep the source code consistent and readable, follow the [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html).
- Before submitting any changes, you must run the `google-java-formatter` to automatically format your code. You can run the `google-java-formatter` via CLT or via the IDEA. [Find out](https://github.com/google/google-java-format) how to set up `google-java-formatter` for your IDE or CLT.

## How to run project locally

1. Ensure you have a `~/.env` file with the following environment variables. Adjust their values to suit your local
   development configuration:

   ```
   POSTGRES_USER=test # Change <test> to the name of the DB's user
   POSTGRES_DB=master_db # Change <master_db> to the name of the master DB
   POSTGRES_HOST_PORT=5432 # It's ok to leave the default port
   POSTGRES_HOST=master-database # Replace <master-database> with your host to connect to the PostgreSQL database
   DB_PASSWORD=password # Replace <password> with your password to connect to the PostgreSQL database
   DOCKER_URL_SHORTENER_WRITE_SERVICE_LISTEN_HOST_PORT=8080 # It's ok to leave the default port 
   DOCKER_URL_SHORTENER_READ_SERVICE_LISTEN_HOST_PORT=8081 # It's ok to leave the default port
   POSTGRES_HOST_PORT_REPLICA=5433 # It's ok to leave this port
   POSTGRES_REPLICATION_USER=repl_user # It's ok to leave this name
   POSTGRES_REPLICATION_PASSWORD=password # Replace <password> with your password to connect to the PostgreSQL database
   POSTGRES_HOST_REPLICA=replica-database # Replace <master-database> with your host to connect to the PostgreSQL database
   REDIS_HOST=redis # Replace <redis> with your host to connect to the Redis
   REDIS_HOST_PORT=6379  # It's ok to leave this port
   REDIS_PASSWORD=password  # Replace <password> with your password to connect to the Redis

   ```

1. Run [docker compose](https://docs.docker.com/reference/cli/docker/compose/) as:
   ```sh
   docker compose --env-file ~/.env up --build
   ```

#### Important notes on variables:

1. `REDIS_HOST`, `POSTGRES_HOST` and `POSTGRES_HOST_REPLICA`: These are the names of the Docker Compose services. They are used for
   internal communication between containers. Do not change them to IP addresses or other names unless you change the
   corresponding service names in `compose.yml`.
1. Passwords: Always use strong passwords and make sure they match the ones you configured in the PostgreSQL
   initialization scripts (if you changed them).
1. This is an example `.env` file content to help you quickly test and launch services. Be very careful when changing
   their values, as they dictate the interaction between all your services.

