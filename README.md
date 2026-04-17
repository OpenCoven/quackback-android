# Quackback Android SDK

The official Android SDK for [Quackback](https://quackback.com) — embed your feedback widget in any Android app with a single call.

## Requirements

- Android API 24+
- AndroidX

## Installation

Add the dependency to your module's `build.gradle.kts`:

```kotlin
dependencies {
    implementation("com.quackback:sdk:0.1.0")
}
```

Or with Groovy DSL (`build.gradle`):

```groovy
dependencies {
    implementation 'com.quackback:sdk:0.1.0'
}
```

## Quick Start

### 1. Configure in your Application class

```kotlin
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Quackback.configure(
            context = this,
            config = QuackbackConfig(instanceUrl = "https://feedback.yourapp.com")
        )
    }
}
```

### 2. Identify the current user

```kotlin
// With user attributes — simplest option, works out of the box
Quackback.identify(
    userId = "user_123",
    email = "user@example.com",
    name = "Jane Smith"
)

// Or with a server-signed token — recommended for production
Quackback.identify(ssoToken = "your-sso-token")

// Or anonymously — the widget prompts for an email inline
Quackback.identify()
```

### 3. Show the floating launcher button

```kotlin
// In your Activity
Quackback.showLauncher()
```

### 4. Open programmatically

```kotlin
// Open the default board
Quackback.open()

// Open a specific board by slug
Quackback.open(board = "feature-requests")
```

## API

| Method | Description |
|---|---|
| `configure(context, config, identity?)` | Initialize the SDK. Pass an optional `Identity` to bundle identification into the same call. Call once in `Application.onCreate()`. |
| `identify()` | Start an anonymous session. The widget prompts for an email inline the first time the user posts. |
| `identify(userId, email, name?, avatarURL?)` | Identify the current user with attributes. |
| `identify(ssoToken)` | Identify with a server-signed SSO token. Blocks impersonation. |
| `logout()` | Clear the current user session. |
| `metadata(patch)` | Attach session metadata (`Map<String, String?>`) to feedback. Pass `null` values to remove keys. |
| `open(view?, title?, board?)` | Open the panel. Use `OpenView.NEW_POST` with `title` to pre-fill the new-post form, `OpenView.CHANGELOG` for changelog, etc. |

### Identity

Pass an `Identity` value to `configure(context, config, identity)` to bundle identification at setup time:

```kotlin
Quackback.configure(this, config, identity = Identity.User(id = "u_123", email = "a@b.com", name = "Ada"))
Quackback.configure(this, config, identity = Identity.SsoToken("jwt..."))
// Omit the `identity` parameter for anonymous sessions — it's the default.

// Session metadata — attach context to feedback submissions
Quackback.metadata(mapOf("page" to "/settings", "app_version" to "2.4.1"))

// Open with deep-link options
Quackback.open(view = OpenView.NEW_POST, title = "Bug: crash on save", board = "bugs")
Quackback.open(view = OpenView.CHANGELOG)
```
| `open(board?)` | Open the feedback panel, optionally to a specific board slug. |
| `close()` | Close the feedback panel. |
| `showLauncher()` | Install the floating launcher button on the current activity. |
| `hideLauncher()` | Remove the floating launcher button. |
| `on(event, handler)` | Subscribe to an event. Returns an `EventToken`. |
| `off(token)` | Unsubscribe using the token returned by `on`. |
| `destroy()` | Tear down all SDK state (useful in tests or on sign-out). |

## QuackbackConfig

| Property | Type | Default | Description |
|---|---|---|---|
| `instanceUrl` | `String` | required | Base URL of your Quackback instance. |
| `theme` | `QuackbackTheme` | `SYSTEM` | `LIGHT`, `DARK`, or `SYSTEM` (follows device setting). |
| `placement` | `QuackbackPosition` | `BOTTOM_RIGHT` | Position of the launcher button: `BOTTOM_RIGHT` or `BOTTOM_LEFT`. |
| `buttonColor` | `String?` | `null` | Hex color for the launcher button (e.g. `"#2563EB"`). |
| `locale` | `String?` | `null` | BCP 47 locale tag to override the widget language (e.g. `"fr"`). |

## Events

Subscribe to widget events using `Quackback.on`:

| Event | Payload keys | Description |
|---|---|---|
| `QuackbackEvent.READY` | — | Widget has loaded and is ready. |
| `QuackbackEvent.VOTE` | `postId`, `type` | User voted on a post. |
| `QuackbackEvent.SUBMIT` | `postId`, `title` | User submitted a new post. |
| `QuackbackEvent.CLOSE` | — | User closed the widget. |
| `QuackbackEvent.NAVIGATE` | `board`, `postId` | User navigated within the widget. |

```kotlin
val token = Quackback.on(QuackbackEvent.SUBMIT) { data ->
    println("New post submitted: ${data["title"]}")
}

// Later, to unsubscribe:
Quackback.off(token)
```

## License

MIT
