# Blesh Android SDK-Lite 5 Developers Guide

**Version:** *5.0.0-rc1*

This document describes integration of the Blesh Android SDK-Lite with your Android application.

## Introduction

Blesh Android SDK-Lite collects location information from a device on which the Android application is installed. Blesh Ads Platform uses this data for creating and enhancing audiences, serving targeted ads, and insights generation.

> **Note:** Lite edition of the Blesh Android SDK does not display ads. Its primary use-cases are enhancing audiences and aiding insights generation.

## Changelog

  * **5.0.0-rc1** *(Released 12/6/2019)*
    * Added initialization support
    * Added callback handler for handling changes in the location permission
    * Supported server-side HTTP compression

## Requirements

In order to integrate the Blesh Android SDK-Lite make sure you are:

  * Targeting Android version 4.1 (API level 16) or higher
  * Enabling Firebase. You may need to add a valid `google-services.json` to your project
  * Registered on the *Blesh Publisher Portal*
    * You may need to create a *Blesh Ads Platform Access Key* for the Android platform

> **Note:** Compile SDK Version and Target SDK Version of Blesh Android SDK-Lite is 29.

## Integration

### 1. Adding the Blesh Android SDK-Lite

The Blesh Android SDK-Lite can be added either by using Gradle or Maven.

#### 1.1. Adding the Blesh Android SDK-Lite with Gradle

Referencing the `sdk` package in the JCenter repository `com.blesh.sdk` with version `5.0.0-rc1` in the `build.gradle` will be sufficient to add the Blesh Android SDK to your project.

**Steps to add:**

1. Add the dependency to the `build.gradle` file

```gradle
apply plugin: 'com.android.application'
// ...

buildscript {
    repositories {
        jcenter()
        // ...
    }
}

dependencies {
    // ...
    implementation 'com.blesh.sdk:sdk:5.0.0-rc1'
    // ...
}
```

2. Sync dependencies with your project

### 2. Adding Credentials and Application Permissions

Blesh Android SDK-Lite requires **Blesh Ads Platform Access Key**. You may need to create one for the Android platform at the *Blesh Publisher Portal*. If you do not have an account at the *Blesh Publisher Portal* please contact us at technology@blesh.com. If you have your key you can provide it in your `AndroidManifest.xml` file.

In order to properly initialize the SDK, you need to use internet and access network state permissions as below.

**Example manifest file:**

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.your.application">

    <application
        android:name=".YourApplication">
        <!-- ... -->

        <meta-data
            android:name="com.blesh.sdk.secretKey"
            android:value="YOUR-SECRET-KEY" />

    </application>

    <!-- ... -->
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />

</manifest>
```

## Usage

### 1. Configuring the Blesh Android SDK-Lite

Before starting the Blesh Android SDK-Lite, it needs to be configured with the `onCreate` method in your application class. `configure` method of the `BleshSdk` requires an instance of your application and optionally the Blesh Android SDK-Lite configuration.

<div style="page-break-after: always;"></div>

#### Java

`BleshSdk` contains following overrides of the `configure` method:

```java
void configure(Application application);
void configure(Application application, SdkConfiguration configuration):
```

* `configuration` parameter allows you to configure the behaviour of the Blesh Android SDK-Lite. The `SdkConfiguration` class contains the following:

| Property   | Type | Description                                                                       | Example |
|------------|------|-----------------------------------------------------------------------------------|---------|
| TestMode   | boolean | Use the SDK in the test mode (true) or use the SDK in the production mode (false) | false   |

> **Note:** `TestMode` is off by default. You can enable this mode during your integration tests. Production environment will not be effected when this flag is set to `true`.

##### Example: Simple Configuration

You can start the Blesh Android SDK-Lite by simply providing the application instance:

```java
public class YourApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // ... rest of the code ...

        BleshSdk.configure(this);
    }
}
```

### 2 Starting the Blesh Android SDK-Lite

After configuring the Blesh Android SDK-Lite, you can start it in one of your activities.


#### Java

`BleshSdk` contains following overrides of the `start` method:

```java
void start();
void start(ApplicationUser applicationUser);
void start(ApplicationUser applicationUser, OnSdkStartCompleted callback);
void start(OnSdkStartCompleted callback):
```

* `callback` parameter allows you to execute your business logic after the Blesh Android SDK-Lite initialization is succeeded, skipped or failed.

<div style="page-break-after: always;"></div>

* `applicationUser` parameter allows you to enchance the audience data by providing information about the primary user (subscriber) of your application. You can give any information which makes the subscriber unique in your application's understanding. The `ApplicationUser` class contains the following:

| Property    | Type                     | Description                                  | Example              |
|-------------|--------------------------|----------------------------------------------|----------------------|
| UserId      | String                  | Optional unique identifier of the user       | 42                   |
| Gender      | ApplicationUserGender                | Optional gender of the user | ApplicationUserGender.FEMALE               |
| YearOfBirth | Integer                     | Optional year of birth of the user           | 1999                 |
| Email       | String                  | Optional email address of the user           | jane.doe@example.com |
| PhoneNumber | String                  | Optional mobile phone number of the user     | +905550000000        |
| Other       | Map | Optional extra information for the user      | null                  |

> **Note:** `Email` and `PhoneNumber` details are never sent in plain-text to the *Blesh Ads Platform*. These values are always irreversibly hashed so that no personally identifiable information is stored.

> **Note:** If you use an advertising ID provider other than Google Play Services, you can pass them using `IDFA_Provider` and `IDFA_Override` keys in the `Other` map.

##### Example: Simple Initialization

You can start the Blesh Android SDK-Lite by simply calling the start method in your MainActivity:

```java
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ... rest of your code

        BleshSdk.start();
    }
}
```

##### Example: Simple Initialization with callback

```java
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ... rest of your code

        BleshSdk.start(new OnSdkStartCompleted() {
            @Override
            public void handle(SdkStartState sdkStartState) {
                Log.i("YourApp", "BleshSDK state: " + sdkStartState.name());
            }
        });
    }
}
```

##### Example: Initialization for Custom Advertising ID Provider

```java
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ... rest of your code

        Map<String, String> other = new HashMap<>();
        other.put("IDFA_Provider", "gms");
        other.put("IDFA_Override", "93f6e226-ad9a-430e-b366-a4c839f0aa12");

        ApplicationUser user = new ApplicationUser.Builder()
                .userId("42")
                .gender(ApplicationUserGender.FEMALE)
                .yearOfBirth(2000)
                .other(other)
                .build();

        BleshSdk.start(user);
    }
}
```

<div style="page-break-after: always;"></div>

##### Example: Complete Initialization

```java
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ... rest of your code

        ApplicationUser user = new ApplicationUser.Builder()
                .userId("42")
                .email("test@example.com")
                .gender(ApplicationUserGender.FEMALE)
                .yearOfBirth(2000)
                .build();

        BleshSdk.start(user, new OnSdkStartCompleted() {
            @Override
            public void handle(SdkStartState sdkStartState) {
                Log.i("DemoApp", "BleshSDK: " + sdkStartState.name());
            }
        });
    }
}
```

<div style="page-break-after: always;"></div>

### 3. Notifying the Blesh Android SDK-Lite About Changes in Permissions

Blesh Android SDK-Lite does not ask the user for permissions. Your application needs to ask location permissions. See "[Adding Credentials and Application Permissions](#2-adding-credentials-and-application-permissions)" section for more information.

When the location permission changes, your application should call the `onRequestPermissionsResult` method of `BleshSdk` with the new status in your Activity as below.

#### Java

```java
@Override
public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    BleshSdk.onRequestPermissionsResult(requestCode, permissions, grantResults);
}
```

#### Kotlin

```kotlin
override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
    BleshSDK.onRequestPermissionsResult(requestCode, permissions, grantResults)
}
```