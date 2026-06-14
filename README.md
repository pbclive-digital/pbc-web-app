# PBC-Web-App
This is a web application built on top of Compose Multiplatform - Web. This catering user functionalities for Pittsburgh Buddhist Center. This web application compatible with
all modern web/mobile browsers.
* [composeApp](./composeApp/src) is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
    - [commonMain](./composeApp/src/commonMain/kotlin) is for code that’s common for all targets.
    - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.

## Prerequisites
* Java 21

## Technologies & Tools
* Kotlin -- v2.3.0
* Gradle -- v9.1.0
* Firebase - Integration

## Build
Build the code by running following command. This build is for full gradle build.
<br />
````shell
./gradlew clean --no-build-cache build
````

### Build and Run Web Application
To build and run the development version of the web app, use the run configuration from the run widget
in your IDE's toolbar or run it directly from the terminal:
- for the Wasm target (faster, modern browsers):
    - on macOS/Linux
      ```shell
      ./gradlew :composeApp:wasmJsBrowserDevelopmentRun
      ```
    - on Windows
      ```shell
      .\gradlew.bat :composeApp:wasmJsBrowserDevelopmentRun
      ```

## New Development
In this application development, we are following trunk-base approach for branching. Therefore, if there is any new development or bug-fx or hot-fix
available, our development branch will be `main` branch.
* Develop or address the new change in a branch took from `main` branch. Use `feature/<feature or changes name>` naming convention to name feature branches. 
* Once change is tested locally, pushed that branch to remote and create a pull request to main branch in github. 

## Release and Deploy
This application use release naming pattern and this has two deployment environments. One is staging and the other one is for production. 

### Release Naming
Application release naming pattern is built with release year, release month, and release index for releasing month.
````
Pattern : <release-year>.<month>.<release-index> 
Example : 2026.1.0
````

### Deploy to Staging
Github-pages used as `staging` environment of this application. Once new changes merged to `main` branch, that automatically
deployed to github-pages using github-action.

#### Steps to follow in new staging-release
1. From feature branch update the application version and set application environment to `staging` in `gradle.properties` file.
2. Create a pull-request from feature branch to `main` branch.
3. Merge feature branch to `main` branch after having required approvals.
4. Let github-action to execute from `main` branch to deploy to `staging` environment.

### Deploy to Production
Firebase Hosting is using as production environment for this application. Once changes are testing in staging environment, use firebase cli
to deploy changes to production from `main` branch.

#### Steps to follow in new production-release
1. Create a new release branch from main branch as `release/<release-app-version>`. Eg: `release/2026.1.0`
2. Update the application environment to `prod` in `gradle.properties` file.
3. Generate the web distribution using following command
   ````shell
   ./gradlew clean --no-build-cache
   ./gradlew composeApp:wasmJsBrowserDistribution
   ````
4. Use `Firebase-CLI` to execute the production deployment.
    ````shell
   firebase deploy
   ````
5. Create a pull request from release branch to `main` branch.
6. Merge changes to `main` branch after having required approvals.
7. Checkout to `main` branch and pull changes to local.

## Notes
* All tool version is configured in `pbc-web-app/gradle/libs.versions.toml`.

