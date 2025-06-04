# Contributing to “deploymentconfig-2-deployment”

Thank you for your interest in contributing! 🙌  
Below you'll find guidelines for reporting bugs, requesting features, and submitting changes to this project.

---

## Table of Contents

1. [Reporting a Bug](#1-reporting-a-bug)  
2. [Requesting a New Feature](#2-requesting-a-new-feature)  
3. [Creating a Pull Request](#3-creating-a-pull-request)  
4. [Coding Style Guidelines](#4-coding-style-guidelines)  
5. [Review and Merge Process](#5-review-and-merge-process)  
6. [Additional Best Practices](#6-additional-best-practices)  

---

## 1. Reporting a Bug

1. Check the [existing issues](https://github.com/scanalesespinoza/deploymentconfig-2-deployment/issues) to see if someone has already reported the problem.
2. If not, click **New Issue** and select the "Bug report" template.
3. Provide the following information:
   - **Clear description**: What is the unexpected behavior?
   - **Steps to reproduce**: Commands, environment, etc.
   - **Expected vs. actual behavior**.
   - **Additional details**: Logs, screenshots, versions of Kubernetes/OpenShift, OS, etc.

> 📝 The more details you include, the faster we can help resolve the issue.

---

## 2. Requesting a New Feature

1. Check existing issues to see if there is a similar feature request.
2. If not, click **New Issue** and select the "Feature request" template.
3. Provide:
   - **Descriptive title**.
   - **Motivation**: Why is this feature needed? What problem does it solve?
   - **Implementation details (optional)**: Proposed technical approach, examples, mockups.

> 💡 Aim for the simplest solution ("minimum viable") that addresses your use case.

---

## 3. Creating a Pull Request

1. **Fork** this repository and clone it locally:
   ```bash
   git clone https://github.com/your-username/deploymentconfig-2-deployment.git
   cd deploymentconfig-2-deployment
   ```
2. **Create a new branch** for your changes:
   ```bash
   git checkout -b fix/brief-description
   ```
3. Make your changes (code, documentation, tests, etc.) following the guidelines below.
4. **Run local tests** (if applicable) to ensure everything passes.
5. **Commit your changes** with clear, atomic messages:
   ```bash
   git add .
   git commit -m "fix: correct DeploymentConfig to Deployment validation"
   ```
6. **Push your branch** to your fork:
   ```bash
   git push origin fix/brief-description
   ```
7. Open a **Pull Request** against the `main` branch of this repository, using the provided template. Describe what you changed and why.

---

## 4. Coding Style Guidelines

- **Language**: This project is written in Go (adjust if using a different language).
- **Formatting**: Follow the official style guides:
  - In Go: use `gofmt` and `golint`.
  - In YAML/JSON: use 2 spaces for indentation (no tabs).
- **Commit messages**: Follow Conventional Commits:
  ```
  <type>(<scope>): <short description>
  ```
  Examples:
  - `feat(cmd): add --dry-run option`
  - `fix(k8s): correct DeploymentConfig name`
  - `docs: update README examples`
  - `refactor: extract validateConfig() function`
- **Documentation**: Document each new feature or endpoint in the README or appropriate `docs/` files.

---

## 5. Review and Merge Process

1. **PR Review**:  
   - Project maintainers will review your PR, may request changes, or approve it.
   - Respond to all review comments to move towards merging.

2. **Merge**:  
   - Once approved, a maintainer will merge using “Squash and merge” (to combine commits) or “Rebase and merge” as appropriate.
   - A new release will be published if the change warrants it.

---

## 6. Additional Best Practices

- **Automated tests**: If you add new logic, include unit/functional tests when possible.
- **Update documentation**: If behavior changes, update the CHANGELOG.md or relevant docs.
- **Communication**: For major changes or breaking compatibility, open a discussion issue before starting.
- **Respect and patience**: Treat everyone with kindness, especially newcomers to the project.

Thank you for your contribution! 🚀  
— The `deploymentconfig-2-deployment` Team
