const functions = require("firebase-functions");

// The Firebase Admin SDK to access Firestore.
const admin = require("firebase-admin");
admin.initializeApp();

// Create and Deploy Your First Cloud Functions
// https://firebase.google.com/docs/functions/write-firebase-functions

exports.helloWorld = functions.https.onRequest((request, response) => {
  functions.logger.info("Hello logs!", {structuredData: true});
  response.send("Doomo app");
});

exports.addUserToFirestore = functions.auth.user().onCreate((user) => {
  // Code run every time is created
  const usersRef = admin.firestore().collection("users");
  return usersRef.doc(user.uid).set({
    name: user.displayName,
    email: user.email,
    address: "",
    sound: true,
    notification: true,
    image: "",
  });
});
