<h1>Insta App</h1>


<h2>Main Activity</h2>
<h3>Main Activity is the the launcher of the activity that firstly check if this device that the app is intalled in is already logged in or not.
 if it is already logged in it goes to Feed activiy and if it is not logged in it goes to show its UI components. Within Login components, the user can enter
 his username and password which are required. if the connection is got down there is an alert will be shown and if the username or the password is not correct
 there is also an alert will be shown</h3>
<img src="Images/login.jpg" width="300" height="600">
<br><br>



<h2>Register Activity</h2>
<h3>Within Register Activity, the user can enter his name, username, password, and reenter the password. if the connection is got down
there is an alert will be shown and if the username is already found in the firebase auth there is also an alert will be shown to go login</h3>
<img src="Images/register.jpg" width="300" height="600">
<br><br>


<h2>Feed Activity</h2>
<h3>The Feed Activity is a basic activity that contains all the posts that this user uploaded. if the user is connected to the internet, the backend code will 
get the posts from his path in firebase database and load them to the local database if they are not loaded, and if he is not connected, 
the code will get posts from the local database which is already loaded with the posts from firebase database</h3>

<div>
<img src="Images/feed.jpg" width="300" height="600">              
<img src="Images/feed menu.jpg" width="300" height="600">
</div>
<br><br>


<h2>Profile Activity</h2>
<h3>The Profile Activity is an empty activity that contains the info of that user, his profile pic, his name, bio, and username.
it contains an icon to go edit profile info</h3>
<img src="Images/my profile.jpg" width="300" height="600">
<br><br>

<h2>Edit Profile Activity</h2>
<h3>It goes to show when the user clicked on the edit icon in the Profile Activity. the user can change/set his profile pic, his bio, and his name.</h3>
<img src="Images/edit profile.jpg" width="300" height="600">
<br><br>

<h2>Visit Profile Activity</h2>
<h3>It goes to show when the user enters a token in the search bar in Feed Activity and this token must be correct and is already
 found in the database he can take it from his friend and this token is the UID of the user in firebase auth. 
it shows his profile with all the images that he uploaded before.</h3>
<img src="Images/visit profile.jpg" width="300" height="600">
<br><br>


<h2>Add Image Activity</h2>
<h3>it contains three main components, ImageView, two EditTexts for title and description, and adds a button to submit the adding process.
only the image URI is required.</h3>
<div>
<img src="Images/add post .jpg" width="300" height="600">              <img src="Images/add post.jpg" width="300" height="600">
</div>
<br><br>


