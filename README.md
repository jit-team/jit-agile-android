# Project setup

1. Clone repository
2. Create folder `keys` in project directory
3. Create file `keystore.properties` in `project/keys` with signing data


	keyAlias=<alias>
	keyPassword=<password>
	storeFile=<path to jks file>
	storePassword=<password>

4. Create Firebase Project
5. Create two android apps with your own id i.e. `com.example.agile`, one with suffix `.dev`
6. Download `google-services.json` from Firebase Console and put it in to `project/app`
7. Enable in Firebase Project username/password authentication
8. Enable Firestore Database
9. Project requires Firebase Cloud Functions, clone this repo and deploy functions in your Firebase project [https://github.com/jitsolutions/jit-agile-backend]()
10. After all steps build app!