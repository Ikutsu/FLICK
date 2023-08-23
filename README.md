<p align="center"><img src="docs/assets/images/icon.png" width="200"></p>
<h1 align="center"><b>FLICK</b></h1>
<h3 align="center">Build your own IM application. Fast and simple!</h3>

## About
Provide out of the box way to create your own social media applications with different services.
 
## Features
### Beta
- [x] Authentication
- [x] Friend list
- [x] Chat
- [x] Search friend
- [x] Friend request
- [x] Notification
- [x] Local database
- [x] Latest message
- [x] Unread message count
- [x] Dark mode

### Future features
- [ ] Profile
- [ ] Group chat
- [ ] Feed
- [ ] Preference Settings
- [ ] Light mode
- [ ] Call (Audio/Video)
- [ ] Emotes
- [ ] Message Withdrawal
- [ ] Custom Theme
- [ ] Notification Channel
- [ ] i18n 

## Services
- Public server - Beta
- Own server using [Ktor](https://ktor.io/) - Beta
- Google [Firebase](https://firebase.google.com/) - Planning
- MongoDB [Realm](https://realm.io/) - Planning

## Frameworks
### Common
- Architecture — MVVM
- Database - Locally stored using [Room](https://developer.android.com/training/data-storage/room), Key-value stored using [DataStore](https://developer.android.com/topic/libraries/architecture/datastore)
- UI/UX - Pure [Jetpack compose](https://developer.android.com/jetpack/compose), designed with [Figma](https://www.figma.com/)
- Dependency injection - [Hilt](https://developer.android.com/training/dependency-injection/hilt-android)

### Public server
- Server - Host on [AWS](https://aws.amazon.com/) EC2
- Database - Remotely stored using [MongoDB](https://www.mongodb.com/)
- Network - [Ktor](https://ktor.io/)
- API - REST API

## Building
1. Clone the repository
2. Create a `local.properties` file in the root of the project
3. Open the project in Android Studio and open the 'local.properties' file you just created
4. Add the following lines to the file:
```
sdk.dir=<path to your Android SDK>
serverIP=<your server IP> # Example: 127.0.0.1:8080 or www.example.com
```
5. Press the run button to build and run the app