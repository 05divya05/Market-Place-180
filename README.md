# CS-180-Project

Market Place Project - Contains Item Posts
====================================

Project Description:
---------------------
This project implements fully functional marketplace platform where users can act as buyers and sellers. 
The platform supports user registration, item listing, item searching, messaging, payment processing, and user ratings.

Option Selected: Market Place

Team Members & Submissions:
---------------------------
1. Divya Vemireddy -
2. Yihang Li - 
3. Sultan Al Qahtani - 
   GitHub Repository

Compilation & Execution Instructions:
-------------------------------------
The Market Place is designed to run using the AppGUI and Server Java files. 
To compile, start the server to handle requests and then execute the AppGUI file. 

Detailed Description of Classes:
-------------------------------
## ChatPanel

**Functionality:** The `ChatPanel` is the centeral class for managing messaging data, sellers and buyers will be able to chat 
with each other regarding the items. It provides methods to refresh the conversation, send new messages, and be able to exit 
to go back to the market place. 

**Testing:** The `ChatPanelTest` tests for basic messaging functions, if the user is able to message a certain user, sending this
data to a messaging file to store that data. Ensures that the user is able to go back to the marketplace to continue browsing. 

## UserProfile 

**Functionality:** The `UserProfile` class stores and manages the user data, it includes the username, password, and email. 
This will allow the items that the users are buying or selling to be associated with their user data and thus can be 
indivdually storing, creating, and maintaining it on their profile. This will be written to a file, and will be easily retrieved.

**Testing:** The `UserProfileTest` will validate whether the program is able to write this information to a file and be retrieved, 
it runs through by storing mock user data and simulating the common operations of logging in and having a profile. 

## 



