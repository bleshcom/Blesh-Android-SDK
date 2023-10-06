# Blesh Android SDK 5 Developers Guide

**Version:** *5.5.0*

This document describes integration of the Blesh Android SDK with your Android application.

## Introduction

Blesh Android SDK collects location information from a device on which the Android application is installed. Blesh Ads Platform uses this data for creating and enhancing audiences, serving targeted ads, and insights generation.

## Table of Contents

- [Blesh Android SDK 5 Developers Guide](#blesh-android-sdk-5-developers-guide)
  - [Introduction](#introduction)
  - [Table of Contents](#table-of-contents)
  - [Changelog](#changelog)
  - [Requirements](#requirements)
  - [Integration](#integration)
    - [1. Adding the Blesh Android SDK](#1-adding-the-blesh-android-sdk)
      - [1.1. Adding the Blesh Android SDK with Gradle](#11-adding-the-blesh-android-sdk-with-gradle)
    - [2. Adding Credentials and Application Permissions](#2-adding-credentials-and-application-permissions)
      - [Secret Key](#secret-key)
      - [Notification Icon](#notification-icon)
      - [Notification Color](#notification-color)
      - [Permissions](#permissions)
  - [Usage](#usage)
    - [1. Configuring the Blesh Android SDK](#1-configuring-the-blesh-android-sdk)
      - [Java](#java)
        - [Example: Simple Configuration](#example-simple-configuration)
        - [Example: Providing Secret Key with Configuration](#example-providing-secret-key-with-configuration)
    - [2 Starting the Blesh Android SDK](#2-starting-the-blesh-android-sdk)
      - [Java](#java-1)
        - [Example: Simple Initialization](#example-simple-initialization)
        - [Example: Simple Initialization with callback](#example-simple-initialization-with-callback)
        - [Example: Initialization for Custom Advertising ID Provider](#example-initialization-for-custom-advertising-id-provider)
        - [Example: Complete Initialization](#example-complete-initialization)
    - [3. Notifying the Blesh Android SDK About Changes in Permissions](#3-notifying-the-blesh-android-sdk-about-changes-in-permissions)
      - [Java](#java-2)
      - [Kotlin](#kotlin)
    - [4. Controlling Push Notification Campaign Rendering](#4-controlling-push-notification-campaign-rendering)
      - [Java](#java-3)
    - [5. Getting Notified When a Blesh Campaign Is Displayed](#5-getting-notified-when-a-blesh-campaign-is-displayed)
      - [Java](#java-4)
    - [6. Enabling Remote Push Notifications](#6-enabling-remote-push-notifications)
      - [Java](#java-5)
        - [Example: Passing The Current Registration Token](#example-passing-the-current-registration-token)
        - [Example: Monitoring The Registration Token Generation](#example-monitoring-the-registration-token-generation)
        - [Example: Passing The Remote Message To Blesh SDK](#example-passing-the-remote-message-to-blesh-sdk)


## Changelog

  * **5.5.0** *(Released 2023-10-06)*
    * Set minimum Android version to 5
    * Supported text components in interstitials
    * Improved interstitial layout
    * Supported intersitial background layer interactions
    * Added an ability to load remote ads
    * Improved APNs support

  * **5.4.9** *(Released 2023-07-26)*
    * Added helper methods to identify Blesh notifications

  * **5.4.8** *(Released 2023-07-06)*
    * Built with AGP 7.0.4 for backwards compatibility

  * **5.4.7** *(Released 2023-07-04)*
    * Compiled with Kotlin 1.5.10 for backwards compatibility

  * **5.4.6** *(Released 2023-06-09)*
    * Added compatibility methods for Activity Results API

  * **5.4.5** *(Released 2023-03-18)*
    * Improved push notification only content compatibility

  * **5.4.4** *(Released 2022-11-10)*
    * Improved remote push notification message handling

  * **5.4.3** *(Released 2022-08-17)*
    * Allowed entry of access key via configuration builder

  * **5.4.2** *(Released 2022-07-07)*
    * Added callbacks for restart and stop methods
    * Added demo application

  * **5.4.1** *(Released 2022-07-03)*
    * Supported remote push notifications with SDK start

  * **5.4.0** *(Released 2022-07-03)*
    * Added beacon scanning
    * Added remote push notifications
    * Added motion activity transitions

  * **5.3.0** *(Released 2022-04-12)*
    * Added in-app behavior tracking
    * Added support for Android 12 (API Level 31)

  * **5.2.8** *(Released 2021-08-02)*
    * Added support for Android 11 (API Level 30)

  * **5.2.7** *(Released 2020-07-27)*
    * Enhanced rendering

  * **5.2.6** *(Released 2020-02-25)*
    * Updated third party dependencies

  * **5.2.5** *(Released 2020-01-30)*
    * Updated third party dependencies

  * **5.2.4** *(Released 2020-01-23)*
    * Updated third party dependencies

  * **5.2.3** *(Released 2020-01-17)*
    * Added defensive checks for activities and broadcast receivers

  * **5.2.2** *(Released 2020-01-11)*
    * Added more campaign callbacks

  * **5.2.1** *(Released 2020-01-08)*
    * Improved compatibility with shared libraries

  * **5.2.0** *(Released 2020-01-07)*
    * Added WiFi support

  * **5.1.0** *(Released 2019-12-23)*
    * Added push notification campaigns
    * Added geofence support

  * **5.0.0** *(Released 2019-12-06)*
    * Added initialization support
    * Added callback handler for handling changes in the location permission
    * Supported server-side HTTP compression

## Requirements

Compile SDK Version and Target SDK Version of Blesh Android SDK is 31. In order to integrate the Blesh Android SDK make sure you are:

  * Targeting Android version 5 (API level 21) or higher
  * Registered on the [Blesh Publisher Portal](https://publisher.blesh.com)
    * You may need to create a *Blesh Ads Platform Access Key* for the Android platform

> **Note:** BleshSDK uses AndroidX libraries. You may need to use the "Migrate to AndroidX" feature in your IDE and add the definitions below to your gradle.properties file of your project.

```gradle
android.enableJetifier=true
android.useAndroidX=true
```

Please refer to [Migrating to AndroidX](https://developer.android.com/jetpack/androidx/migrate) for more information.

## Integration

### 1. Adding the Blesh Android SDK

The Blesh Android SDK can be added either by using Gradle or Maven.

#### 1.1. Adding the Blesh Android SDK with Gradle

Referencing the `sdk` package in the Blesh Maven repository `com.blesh.sdk` in the `build.gradle` will be sufficient to add the Blesh Android SDK to your project.

**Steps to add:**

1. Add the dependency to the `build.gradle` file

```gradle
apply plugin: 'com.android.application'
// ...

repositories {
    maven { url 'https://artifact.blesh.com/repository/releases/' }
    // ...
}

dependencies {
    implementation "com.blesh.sdk:sdk:5.5.0"
    // ...
}
```

2. Sync dependencies with your project

### 2. Adding Credentials and Application Permissions

#### Secret Key

Blesh Android SDK requires **Blesh Ads Platform Access Key**. You may need to create one for the Android platform at the *Blesh Publisher Portal*. If you do not have an account at the *Blesh Publisher Portal* please contact us at technology@blesh.com. If you have your key you can provide it in your `AndroidManifest.xml` file with the `com.blesh.sdk.secretKey` key. 

Alternatively, you can provide the **Blesh Ads Platform Access Key** via the SDK `configure` method as documented in the next section.

#### Notification Icon

Push notifications are rendered with the Blesh logo by default. You can customize this logo by providing a resource with the `com.blesh.sdk.notificationIcon` key.

#### Notification Color

Push notifications are rendered with the `#351F78` color by default. You can customize this logo by providing a resource with the `com.blesh.sdk.notificationColor` key.

#### Permissions

In order to properly initialize the SDK, you need to use internet and access network/wifi state permissions.

**Example manifest file:**

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.your.application">

    <!-- ... -->
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_BACKGROUND_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
    <uses-permission android:name="android.permission.ACTIVITY_RECOGNITION" />
    <uses-permission android:name="com.google.android.gms.permission.ACTIVITY_RECOGNITION" />
    <uses-permission android:name="android.permission.BLUETOOTH" android:maxSdkVersion="30" />
    <uses-permission android:name="android.permission.BLUETOOTH_ADMIN" android:maxSdkVersion="30" />
    <uses-permission android:name="android.permission.BLUETOOTH_SCAN" />
    
    <!-- ... -->

    <application
        android:name=".YourApplication">
        <!-- ... -->

        <!-- Secret key -->
        <meta-data
            android:name="com.blesh.sdk.secretKey"
            android:value="YOUR-SECRET-KEY" />

        <!-- Custom notification icon -->
        <meta-data
            android:name="com.blesh.sdk.notificationIcon"
            android:resource="@drawable/compatibleIcon" />

        <!-- Custom notification color -->
        <meta-data
            android:name="com.blesh.sdk.notificationColor"
            android:resource="@color/colorPrimary" />

    </application>

</manifest>
```

## Usage

### 1. Configuring the Blesh Android SDK

Before starting the Blesh Android SDK, it needs to be configured with the `onCreate` method in your application class. `configure` method of the `BleshSdk` requires an instance of your application and optionally the Blesh Android SDK configuration.

#### Java

`BleshSdk` contains following overrides of the `configure` method:

```java
void configure(Application application);
void configure(Application application, SdkConfiguration configuration):
```

* `configuration` parameter allows you to configure the behaviour of the Blesh Android SDK. The `SdkConfiguration` class contains the following:

| Property   | Type    | Description                                                                         |
|------------|---------|-------------------------------------------------------------------------------------|
| SecretKey  | String  | Use the provided secret key. This overrides the key provided at AndroidManifest.xml |
| TestMode   | Boolean | Use the SDK in the test mode (true) or use the SDK in the production mode (false)   |

> **Note:** `TestMode` is off by default. You can enable this mode during your integration tests. Production environment will not be effected when this flag is set to `true`.

##### Example: Simple Configuration

You can start the Blesh Android SDK by simply providing the application instance:

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

##### Example: Providing Secret Key with Configuration

```java
public class YourApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // ... rest of the code ...

        BleshSdk.configure(
          this,
          new SdkConfiguration.Builder()
                .testMode(false)
                .secretKey("YOUR_SECRET_KEY_HERE")
                .build());
    }
}
```

### 2 Starting the Blesh Android SDK

After configuring the Blesh Android SDK, you can start it in one of your activities.

#### Java

`BleshSdk` contains following overrides of the `start` method:

```java
void start();
void start(ApplicationUser applicationUser);
void start(ApplicationUser applicationUser, OnSdkStartCompleted callback);
void start(OnSdkStartCompleted callback):
```

* `callback` parameter allows you to execute your business logic after the Blesh Android SDK initialization is succeeded, skipped or failed.

* `applicationUser` parameter allows you to enchance the audience data by providing information about the primary user (subscriber) of your application. You can give any information which makes the subscriber unique in your application's understanding. The `ApplicationUser` class contains the following:

| Property    | Type                   | Description                               | Example                       |
|-------------|------------------------|-------------------------------------------|-------------------------------|
| UserId      | String                 | Optional unique identifier of the user    | 42                            |
| Gender      | ApplicationUserGender  | Optional gender of the user               | ApplicationUserGender.FEMALE  |
| YearOfBirth | Integer                | Optional year of birth of the user        | 1999                          |
| Email       | String                 | Optional email address of the user        | jane.doe@example.com          |
| PhoneNumber | String                 | Optional mobile phone number of the user  | +905550000000                 |
| Other       | Map                    | Optional extra information for the user   | null                          |

> **Note:** `Email` and `PhoneNumber` details are never sent in plain-text to the *Blesh Ads Platform*. These values are always irreversibly hashed so that no personally identifiable information is stored.

> **Note:** If you use an advertising ID provider other than Google Play Services, you can pass them using `IDFA_Provider` and `IDFA_Override` keys in the `Other` map.

##### Example: Simple Initialization

You can start the Blesh Android SDK by simply calling the start method in your MainActivity:

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

### 3. Notifying the Blesh Android SDK About Changes in Permissions

Blesh Android SDK does not ask the user for permissions. Your application needs to ask location permissions. See "[Adding Credentials and Application Permissions](#2-adding-credentials-and-application-permissions)" section for more information.

When the location permission changes, your application should call the `onRequestPermissionsResult` method of `BleshSdk` with the new status in your Activity as below.

Alternatively, for applications utilizing the Activity Results API you can call the  `onPermissionsUpdated` method of `BleshSdk` on the `ActivityResultCallback`.

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

### 4. Controlling Push Notification Campaign Rendering

Blesh Android SDK supports notifying applications prior to displaying a push notification campaign. At this stage, you can also control whether or not to display the push notification and its campaign content. You can set your handler using the `onCampaignNotificationReceived` property of `BleshSdk`.

#### Java

```java
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ... rest of your code

        BleshSdk.setOnCampaignNotificationReceived(campaignId -> {
            // ... your application logic
            Log.d("MyApplication", "Received a campaign with id " + campaignId + " from Blesh");

            return true; // return "false" to deny Blesh SDK from displaying this campaign and push notification
        });

        BleshSdk.start();
    }
}
```

### 5. Getting Notified When a Blesh Campaign Is Displayed

If you implement the `OnCampaignDisplayed` interface and assign the object to the `onCampaignDisplayed` property of `BleshSdk` then you can get notified whenever a Blesh campaign is displayed to the user.

#### Java

```java
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ... rest of your code

        BleshSdk.setOnCampaignDisplayed((campaignId, contentId, notificationId) -> {
            // ... your application logic
            Log.d("MyApplication", "Displayed a campaign with id " + campaignId + " from Blesh");
        });

        BleshSdk.start();
    }
}
```

### 6. Enabling Remote Push Notifications

Blesh Android SDK currently supports remote push notifications throught Firebase Cloud Messaging ([FCM](https://firebase.google.com/docs/cloud-messaging)).

> **Note:** Firebase certificates need to be registered on the [Blesh Publisher Portal](https://publisher.blesh.com).

To be able to receive remote notifications:

1. Firebase device registration token should be passed to Blesh SDK via `BleshSdk.setPushNotificationToken`
2. Firebase remote messages should be handled by the Blesh SDK via `BleshSdk.handleRemoteMessage`

Please refer to the [FCM Android Documentation](https://firebase.google.com/docs/cloud-messaging/android/client#sample-register) for more information.

#### Java

##### Example: Passing The Current Registration Token

```java
FirebaseMessaging.getInstance().getToken()
  .addOnCompleteListener(new OnCompleteListener<String>() {
      @Override
      public void onComplete(@NonNull Task<String> task) {
          // ... rest of your code

          if (!task.isSuccessful()) {
              Log.e("Fetching FCM registration token failed" + task.getException());
              return;
          }

          // Get new FCM registration token
          String token = task.getResult();

          // Notify the Blesh SDK
          BleshSdk.setPushNotificationToken(token);

          // ... rest of your code
      }
  });
```

##### Example: Monitoring The Registration Token Generation

Given a class that extends the `FirebaseMessagingService` class, the following override of the `onNewToken` can be written:

```java
public class MyMessagingService extends FirebaseMessagingService {
    // ... rest of your code

    @Override
    public void onNewToken(String token) {
        // ... rest of your code

        BleshSdk.setPushNotificationToken(token);

        // ... rest of your code
    }

    // ... rest of your code
}
````

##### Example: Passing The Remote Message To Blesh SDK

Given a class that extends the `FirebaseMessagingService` class, the following override of the `onMessageReceived` can be written:

```java
public class MyMessagingService extends FirebaseMessagingService {
    // ... rest of your code

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ... rest of your code

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            // Notify BleshSDK
            BleshSdk.handleRemoteMessage(remoteMessage.getData());
        }

        // ... rest of your code
    }

    // ... rest of your code
}
````

