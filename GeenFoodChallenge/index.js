
// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// });
//https://firebase.google.com/docs/reference/admin/node/admin.database.Database#ref
'use strict';
const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();

exports.pledgeCount = functions.database.ref('/Pledges/{municipality}/{uid}').onCreate((snapshot , context) => {

	var uid =  context.params.uid;
	//path to database
	var countRef = admin.database().ref("GlobalData/NumberOfPledges");
	var co2Ref = admin.database().ref("GlobalData/TotalCo2Pledged");
	//get the value of co2Pleged and parse to int because it is saved as a string.
	const co2Pledged  = snapshot.val().co2Pledged;

	co2Ref.transaction((current) => {
		
		return (current || 0) + co2Pledged; //update total co2
	});
	countRef.transaction((current) => {
        return (current || 0) + 1; // update users
      });
	return null;
});

exports.deletePledge = functions.database.ref('/Pledges/{municipality}/{uid}').onDelete((snapshot , context) => {

	var uid = context.params.uid;

	var countRef = admin.database().ref("GlobalData/NumberOfPledges");
	var co2Ref = admin.database().ref("GlobalData/TotalCo2Pledged");

	const co2Pledged = snapshot.val().co2Pledged;
	co2Ref.transaction((current) => {
		
		return (current || 0) - co2Pledged;
	});
	countRef.transaction((current) => {
        return (current || 0) - 1;
      });
	return null;
});

exports.updatePledges = functions.database.ref('/Pledges/{municipality}/{uid}').onUpdate((change , context) => {

	var oldPledge = change.before.val().co2Pledged;
	var newPledge = change.after.val().co2Pledged;

	var uid =  context.params.uid;
	
	var countRef = admin.database().ref("GlobalData/NumberOfPledges");
	var co2Ref = admin.database().ref("GlobalData/TotalCo2Pledged");

	co2Ref.transaction((current) => {
		
		return (current || 0)  + newPledge - oldPledge;
	});
	return null;
});


