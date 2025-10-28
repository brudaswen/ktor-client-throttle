# Ktor Client Throttle Plugin

[![Maven Central](https://img.shields.io/maven-central/v/de.brudaswen.ktor.client.throttle/ktor-client-throttle?style=flat-square)](https://search.maven.org/artifact/de.brudaswen.ktor.client.throttle/ktor-client-throttle)
[![Snapshot](https://img.shields.io/maven-metadata/v?metadataUrl=https%3A%2F%2Fcentral.sonatype.com%2Frepository%2Fmaven-snapshots%2Fde%2Fbrudaswen%2Fktor%2Fclient%2Fthrottle%2Fktor-client-throttle%2Fmaven-metadata.xml&style=flat-square&label=snapshot)](https://oss.sonatype.org/#nexus-search;gav~de.brudaswen.ktor.client.throttle~ktor-client-throttle~~~)
[![CI Status](https://img.shields.io/github/actions/workflow/status/brudaswen/ktor-client-throttle/ci-main.yml?style=flat-square)](https://github.com/brudaswen/ktor-client-throttle/actions/workflows/ci-main.yml)
[![Codecov](https://img.shields.io/codecov/c/github/brudaswen/ktor-client-throttle?style=flat-square)](https://codecov.io/gh/brudaswen/ktor-client-throttle)
[![License](https://img.shields.io/github/license/brudaswen/ktor-client-throttle?style=flat-square)](https://mit-license.org/)

The `Throttle` plugin allows you to limit the number of requests within a certain time period.

## Gradle Dependencies

```toml
[versions]
ktor-client-throttle = "1.0.0"

[libraries]
ktor-client-throttle = { group = "de.brudaswen.ktor.client.throttle", name = "ktor-client-throttle", version.ref = "ktor-client-throttle" }
```

```kotlin
// From Gradle version catalog
implementation(libs.ktor.client.throttle)

// Manually
implementation("de.brudaswen.ktor.client.throttle:ktor-client-throttle:1.0.0")
```

## Usage

## License

```
MIT License

Copyright (c) 2025

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
