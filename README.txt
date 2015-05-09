Harsha Yeddanapudy, Andrew Osgood, William Truong, Rohan Chandra
hyeddana, wtruong, aosgood, rchandra
CS32 Final Project: CAL
5/5/15

***********
Known Bugs
***********

From what we've tested, when a user tries to edit an event, an odd error occurs in which the event does not get modified in the database
or on the front end. We're not too sure as to why exactly this error occurs.

***********
Design
***********

One of the largest aspects of our project was our Ranker. Our Ranker determines based on an algorithm which timeslots during the day are optimal for all attendees for this event. It is a smart algorithm that finds based on particular parameters what is the best time for all members of an event to meet. This algorithm first assigns all time slots a starting value, and deduct points from this value through various checks. The time slots with the highest value are ranked above the rest and the top 3 are recommended directly to the user. Certain time slots have different starting values. For example, time slots 6 and 7 have a lower value since this is considered to be dinner time, and most users will often be busier during this time. When conflicts occur, a time slot's value will get deducted since it isn't the best time for all users. If a timeslot is available for ALL attendees, then that timeslot will be recommended as GREEN to the user on front end. If a timeslot is available for 50% of the attendees or less, then that timeslot will be recommended as YELLOW. If a user wants to ignore all suggestions, we provide an Override button to create that event for that particular timeslot, regardless of its ranking.

Since our project supports multiple users logged in at once and interacting with our database, we created various threads to handle multiple tasks without overloading our server and our application as well. A new CalendarThread is made each time a user interacts with its calendar, and this thread handles modifying the backend and communicating with the SparkHandler to update the front end. Our ContactsThread operates in the same manner but for contacts. Our HeartBeatThread is used to update users' events and contacts page periodically so that the front end is up to date with the back end with edits are made from other users (ex. a friend sends you a friend request, and your contacts page will update to show the friend request based on a heartbeat). The UserThread handles registration, and the GroupRetrievalThread handles the case of creating an event with a group, as this thread grabs all the members of this group from our database and updates their events to reflect the addition of this group event.

We've implemented the ability for our application to support multiple users at a time through the use of clients. When a user logs in, they are given a unique and random clientID. The client stores all content related to this user, including their events, friends, and the groups their in. When edits are made to the database for this user, the client's contents are updated accordingly so that we avoid costly calls to the database each time we want to update the user's front end. Think of our client and the data it stores as a cache that quickens the response time from front end to back end.

Our database constists of 7 tables: Users, Groups, Events, Friends, User_Events, User_Group, Group_Event. The Users table contains all information about a user (username, password, fullname). The Events table contains all information about an event, including attendees, duration, time and day, etc. The same logic applies to the rest of the tables.

The contacts GUI is generated through various dynamic functions in our javascript. The frontend grabs the friends and groups that this user is a part of on a particular heartbeat, and generates the proper HTML elements on the front end to reflect the information on our database. Adding/accepting/removing a friend spits the proper information (username1, username2, and a command) to the back end for it to update our database. The same logic applies for group interaction as well.

The calendar gui is based on a map of events, where each event on the calendar page is tied to a unique event ID so that we can properly parse information from the backend and have the front end reflect that information. The positioning of each event is done by adding negative margins to div events, which is based on the time and duration of even event.

All information regarding our application's interaction with the Google Calendar API can be found within the comments of our ServerCalls.java class

***********
How To Run Tests/Tests
***********

Our project does not contain any system tests since it is directly usable only through our GUI.

We maintain several JUNIT tests that test functions of certain classes including the Ranker, ServerCalls, Event, DatabaseHandler, and ClientHandler. All other classes operate directly with the front end, so JUNIT tests are lacking for those.

***********
Building/Running/Using My Programming
***********
To run our program, in the root of our CAL project directory, type into terminal:

./run --gui

Then visit localhost:1234/login and register for a new account. 

MAKING AN EVENT:
Once verified, click a time slot on the calendar page to create an event. You can add friends to an event, and groups as well (although currently, our functionality is that you can only add 1 group to an event). Events can be edited even after creation ONLY if you are the creator of the event by simply clicking the event again and editing any of its qualities. Clicking the minus button on the bottom left corner removes you from an event, and if you are the creator of the event, the event gets deleted from all other members. 

ADDING A FRIEND:
To add a friend, go to the contacts page and type in their username and click "Send Invitation". Only friends can be added to groups and events.

CREATING A GROUP:
To create a group, go to the contacts page and click the Groups tab. Once here, input a group name into the text box and create a group. Then, add appropriate members (only friends) to the group. To view members of a group, click the group's icon on the Groups tab of the contacts page. A dialog box will appear, and you can even add new members to existing groups in this window as well.

PULLING FROM GOOGLE:
At the top of the window, near the Contacts and Calendar buttons is a Google button. This button connects with your google account (if authorized by your permission) and pulls all your events into our database. This information is used to maintain a more accurate schedule per user, and thus have our ranker algorithm determine more optimal times.

***********
Checkstyle Errors
***********

Most of the checkstyle errors for our project are fixed. However, there are a few errors that we could not fix. Our event class takes in 8 parameters as part of its functionality instead of 7. In the ContactsThread class, we have a method that is 171 lines long, and we can't seem to cut it to down to less (although we could've gotten rid of comments, but those are esssential to understanding how that class and its function operates). Same thing occurs in the SparkHandler class. Our Ranker class uses a large amount of "magicnumbers" as part of its algorithm, and it became difficult for us to define a global constant for each of these numbers.s

