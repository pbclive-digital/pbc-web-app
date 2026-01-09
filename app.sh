#!/bin/zsh

## Referring static arguments
args=()
env=$2

## Execute the operation according to provided environment
case $env in
  "local")
    ./gradlew clean -Papp.env=dev :composeApp:wasmJsBrowserDistribution
    ;;
  "staging")
    ./gradlew clean -Papp.env=staging :composeApp:wasmJsBrowserDistribution
    ;;
  "prod")
    ./gradlew clean -Papp.env=prod :composeApp:wasmJsBrowserDistribution
    ;;
  *)
    ./gradlew clean -Papp.env=dev :composeApp:wasmJsBrowserDistribution
    ;;
esac