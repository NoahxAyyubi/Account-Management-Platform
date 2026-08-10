# SDET Jenkins CI Notes

## What The SDET Owns

An SDET usually does not install the company Jenkins server from scratch. The SDET is usually responsible for making the tests CI-ready:

- Tag tests as smoke or regression.
- Keep smoke tests fast and stable.
- Make tests run from command line with Maven.
- Add clear pass/fail assertions.
- Publish useful failure evidence such as JUnit reports, screenshots, and logs.
- Help define the quality gate in the Jenkinsfile.

## What Jenkins Owns

Jenkins is the CI server. It watches for a trigger, checks out the GitHub repo, reads the Jenkinsfile, and runs the stages.

Common triggers:

- GitHub webhook: runs CI when code is pushed.
- Cron trigger: runs on a schedule, such as nightly regression.
- Manual build: tester or developer clicks Build Now.

## What GitHub Owns

GitHub stores the repo and sends Jenkins a webhook event when a push or pull request happens.

The webhook is not written inside the Jenkinsfile. It is configured in GitHub and Jenkins.

## Quality Gate

A quality gate means the pipeline must pass an important checkpoint before deeper testing continues.

For this project:

1. Start demo app.
2. Run API smoke tests.
3. Run UI smoke tests.
4. If smoke fails, stop and mark build failed.
5. Run regression only during nightly builds or manual full builds.

## Current Jenkinsfile Behavior

Push or manual smoke build:

```text
Build modules
Start demo app
API smoke
UI smoke
Stop demo app
Publish reports
```

Nightly or manual full build:

```text
Build modules
Start demo app
API smoke
UI smoke
API regression
UI regression
Stop demo app
Publish reports
```

## Jenkins UI Map

Jenkinsfile does not create a visible job by itself. A Jenkins job must be created once in Jenkins UI.

For local learning, the job can use:

```text
Definition: Pipeline script
```

That means the script lives inside Jenkins UI.

For repo-based CI, the job should use:

```text
Definition: Pipeline script from SCM
SCM: Git
Repository URL: GitHub repo URL
Branch: branch name
Script Path: Jenkinsfile
```

That means Jenkins checks out the repo and reads the Jenkinsfile from GitHub.

After the job exists, Jenkins remembers the job config. On each build, it reads the current pipeline script again. If the job is configured from SCM, Jenkins reads the latest Jenkinsfile from the selected Git branch.

## Where To Look After A Build

```text
Stage View
```

Shows which pipeline stages passed, failed, or were skipped.

```text
Console Output
```

This is the raw build log. It shows every shell command Jenkins ran and the exact error when something fails.

```text
Test Result / Latest Test Result
```

This is the Jenkins JUnit report. It comes from Maven Surefire XML files.

```text
Artifacts
```

This is where archived files live, such as screenshots, application logs, Allure result files, and generated Allure HTML when available.

## Cron Cheat Sheet

Jenkins uses cron-style schedules.

```text
H 2 * * *
```

Means:

```text
H      Jenkins chooses a stable minute
2      during the 2 AM hour
*      every day of the month
*      every month
*      every day of the week
```

So `H 2 * * *` means run once every night sometime between 2:00 AM and 2:59 AM.

Jenkins uses `H` instead of a hardcoded minute like `0` so many jobs do not all start at the exact same second.

## Interview Version

I made the automation suite CI-ready by separating smoke and regression tests using test groups. Smoke tests acted as the quality gate and ran first because they validated the highest-risk API and UI flows quickly. Regression tests were reserved for the nightly pipeline because they were broader and more expensive. The Jenkinsfile started the local test application, executed Maven API and UI suites headlessly, published JUnit reports, archived logs/screenshots, and cleaned up the app process after every run.
