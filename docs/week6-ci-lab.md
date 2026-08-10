# Week 6 CI/CD Lab

## Problem

Automation coverage existed, but execution depended too heavily on QA manually running tests. Feedback arrived late, and automation was not consistently influencing merge decisions.

This lab turns a small set of API and UI tests into a CI quality gate.

## Demo Application

The local test target is:

```bash
tmobile-demo-app
```

Start it before running integration tests:

```bash
bash scripts/start-demo-app.sh
```

Stop it afterward:

```bash
bash scripts/stop-demo-app.sh
```

Default URL:

```text
http://localhost:3000
```

## Manual Before CI

Before CI, the workflow is slower:

```text
developer changes code
QA later runs Maven manually
QA reviews failures after the fact
```

That creates delayed feedback because test execution depends on someone remembering to run it.

## CI Workflow

The Week 6 target workflow is:

```text
Git push / pull request
Jenkins checkout
build test modules
start demo application
run 4 smoke tests
publish reports/artifacts
pass/fail quality gate
```

## Smoke Suite

Smoke tests are fast, critical checks that answer:

```text
Is this build healthy enough to continue?
```

This lab has exactly four smoke tests:

- API valid login
- API retrieve subscription `1001`
- UI valid login/dashboard
- UI subscription table validation

## Regression Suite

Regression tests are broader checks that answer:

```text
Did the new change break previously working behavior?
```

Regression does not mean "tests that run before code is pushed." Regression means broader validation after change.

This lab has exactly two regression tests:

- API plan change
- UI plan change flow

## Maven Commands

Build the API module:

```bash
mvn -f rest-api-tests/pom.xml -DskipTests test
```

Build the Selenium module:

```bash
mvn -f selenium-ui-tests/pom.xml -DskipTests test
```

Run API smoke:

```bash
mvn -f rest-api-tests/pom.xml test -Dtest=TMobileDemoApiTest -Dgroups=smoke -Ddemo.baseUrl=http://localhost:3000
```

Run API regression:

```bash
mvn -f rest-api-tests/pom.xml test -Dtest=TMobileDemoApiTest -Dgroups=regression -Ddemo.baseUrl=http://localhost:3000
```

Run UI smoke headlessly:

```bash
mvn -f selenium-ui-tests/pom.xml test -Dtest=TMobileDemoUiTest -Dgroups=smoke -Dheadless=true -Ddemo.baseUrl=http://localhost:3000
```

Run UI regression headlessly:

```bash
mvn -f selenium-ui-tests/pom.xml test -Dtest=TMobileDemoUiTest -Dgroups=regression -Dheadless=true -Ddemo.baseUrl=http://localhost:3000
```

Run all four smoke tests:

```bash
mvn -f rest-api-tests/pom.xml test -Dtest=TMobileDemoApiTest -Dgroups=smoke -Ddemo.baseUrl=http://localhost:3000
mvn -f selenium-ui-tests/pom.xml test -Dtest=TMobileDemoUiTest -Dgroups=smoke -Dheadless=true -Ddemo.baseUrl=http://localhost:3000
```

Run both regression tests:

```bash
mvn -f rest-api-tests/pom.xml test -Dtest=TMobileDemoApiTest -Dgroups=regression -Ddemo.baseUrl=http://localhost:3000
mvn -f selenium-ui-tests/pom.xml test -Dtest=TMobileDemoUiTest -Dgroups=regression -Dheadless=true -Ddemo.baseUrl=http://localhost:3000
```

## Jenkins

The root `Jenkinsfile` is only a pipeline definition. Jenkins still needs to be installed and connected manually.

Later, you will:

- install/run Jenkins
- create a Pipeline or Multibranch Pipeline job
- connect the repository
- point Jenkins to the `Jenkinsfile`
- configure source-control trigger/webhook later
- run `Build Now` once manually
- later test automatic triggering
- configure a nightly regression job or use the `TEST_SUITE=regression` parameter
- inspect reports
- intentionally cause one smoke failure
- diagnose it
- fix it
- rerun it

## Failure Classification

Product failure:
Application behavior violates the expected requirement.

Automation failure:
Test logic, assertion, locator, framework, or test data is incorrect.

Environment failure:
Application unavailable, configuration wrong, dependency unavailable, browser issue, runner issue, or database unavailable.

## Intentional Failure Practice

Do not commit a failing test. For practice only:

1. Open `rest-api-tests/src/test/java/test/TMobileDemoApiTest.java`.
2. Temporarily change the expected plan in the retrieve-subscription smoke test from `Essentials` to `BROKEN_PLAN`.
3. Run the smoke suite and observe the failure.
4. Restore `Essentials`.
5. Rerun the smoke suite and confirm green.

## Interview Anchor

Situation:
Automation coverage was growing, but feedback was still arriving too late because execution depended too much on QA running suites manually.

Task:
Turn automation into a practical quality gate that could influence merge decisions earlier.

Action:
Separate four critical smoke checks from two broader regression checks, run smoke through Jenkins on change activity, keep regression available for a nightly cadence, preserve reports and artifacts, and investigate failed pipeline runs by classifying them as product, automation, or environment issues.

Result:
Automation becomes part of the delivery decision instead of post-hoc verification, giving the team earlier visibility into risky changes.
