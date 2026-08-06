# Step 7 — Build a Docker Image in the Pipeline

**Goal:** the final piece. Add a job that builds a **Docker image** of the app. Good news:
this is *easier* on GitHub than on GitLab.

---

## 7.1 — First, what the Dockerfile does

There's a `Dockerfile` in this repo already. Open it. It has two stages:
1. **build** — uses a Maven image to compile the jar
2. **run** — copies just the jar onto a slim Java runtime image

Try it **locally** first (if you have Docker installed):
```bash
docker build -t calculator-app .
docker run --rm calculator-app
```
You should see the calculator demo print from inside a container. That's your app, packaged
so it runs the same anywhere. ✅
//windows command
> Install Docker on your machine if you haven't — it's a core skill and many projects
> require you to demo it locally.

---

## 7.2 — Docker in the pipeline (GitHub makes this simple)

Here's the nice part: **GitHub's `ubuntu-latest` runners come with Docker already
installed.** So building an image in a job is just… running `docker build`. No special
"Docker-in-Docker" service like GitLab needs.

Add a third job:

```yaml
  build-docker-image:
    runs-on: ubuntu-latest
    needs: build-jar
    steps:
      - uses: actions/checkout@v4
      - run: docker build -t calculator-app .
```

That's it. `checkout` puts the Dockerfile + source on the runner, and `docker build` just
works because Docker is preinstalled.

---

## 7.3 — The complete workflow

Your full `.github/workflows/ci.yml` now:

```yaml
name: CI Pipeline

on:
  push:
    branches: [ main ]
  pull_request:
    branches: [ main ]

jobs:
  run-tests:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          distribution: temurin
          java-version: '21'
      - run: mvn test

  build-jar:
    runs-on: ubuntu-latest
    needs: run-tests
    steps:
      - uses: actions/checkout@v4
      - uses: actions/setup-java@v4
        with:
          distribution: temurin
          java-version: '21'
      - run: mvn package -DskipTests
      - uses: actions/upload-artifact@v4
        with:
          name: app-jar
          path: target/*-jar-with-dependencies.jar

  build-docker-image:
    runs-on: ubuntu-latest
    needs: build-jar
    steps:
      - uses: actions/checkout@v4
      - run: docker build -t calculator-app .
```

Push it:
```bash
git add .github/workflows/ci.yml Dockerfile
git commit -m "build docker image in pipeline"
git push
```

Watch three jobs run in order: **run-tests → build-jar → build-docker-image**. The last one
builds your container image. 🎉

---

## 7.4 — Optional: push the image to a registry

Right now the image is built then thrown away. To *keep* it, you'd push it to a registry
(GitHub has one: `ghcr.io`). That needs login credentials and is beyond this tutorial — but
know it exists. For learning, building the image proves the pipeline works.

---

✅ **Done when:** your workflow runs test → build → docker in order, and the last job builds
a Docker image. Next: **[Step 8 — Apply to your project](08-apply-to-your-project.md)**.
