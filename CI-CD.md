# Branching and Deployment Strategy

This document outlines the branching model and the automated release and deployment process used for this project.

## Branching Model

We follow a simple branching model based on a single main branch.

1. **`main` Branch:**
    * This is the primary development branch.
    * It **must** always contain stable, production-ready or near-production-ready code.
    * Direct commits to `main` are discouraged; changes should be integrated via Pull Requests.

2. **Development Branches:**
    * **Feature Branches:** For developing new features. Branched from `main` (e.g., `feature/add-user-authentication`).
    * **Bugfix Branches:** For fixing bugs. Branched from `main` (e.g., `fix/resolve-login-issue`).
    * These branches should be kept short-lived and focused on a single task.

3. **Pull Requests (PRs):**
    * All changes intended for `main` (from feature or bugfix branches) **must** go through a Pull Request.
    * PRs require at least one approval from a team member.
    * Automated checks (like tests, linting, and builds) must pass before a PR can be merged.

## Release Process (Automated Versioning)

We use `googleapis/release-please-action` to automate versioning and changelog generation based on Conventional Commits.

1. **Commit Messages:** Developers **must** follow
   the [Conventional Commits specification](https://www.conventionalcommits.org/) (e.g., `feat: ...`, `fix: ...`,
   `chore: ...`).
2. **Release PR Trigger:** When commits land on the `main` branch:
    * The `release-please` action runs automatically.
    * It analyzes the commit history since the last tag.
    * It creates or updates a special "Release PR". This PR proposes the next semantic version number (e.g., `v1.2.3`)
      and includes an automatically generated `CHANGELOG.md` section based on the conventional commit messages.
3. **Tagging:**
    * When the "Release PR" is reviewed and merged into `main`, the `release-please` action automatically:
        * Creates a Git tag with the proposed version (e.g., `v1.2.3`).
        * Creates a corresponding GitHub Release associated with that tag, including the generated changelog notes.

## Deployment Workflow

The creation of a new version tag triggers the deployment workflow.

1. **Trigger:** The workflow starts automatically when a tag matching the pattern `v*.*.*` (e.g., `v1.2.3`) is pushed to
   the repository.
2. **Build:** The workflow builds the application artifacts (e.g., executables, container images).
3. **Deployment Stages:** The deployment generates a GitHub release that contains changelog and artifact

This strategy ensures that:

* The `main` branch remains stable.
* Versioning is consistent and automated.
* Deployments are triggered by formal releases (tags).
* There is a controlled promotion process through environments, mixing automation (for initial testing) with manual
  gates (for critical environments).