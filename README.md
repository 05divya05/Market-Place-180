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
**File Structure**
Place all `.java` files in the same directory. These include:
- Client.java
- Server.java
- MainFrame.java
- LoginPanel.java
- MarketPanel.java
- ChatPanel.java
- ChatListDialog.java
- CreateItemDialog.java
- EditItemDialog.java
- Item.java
- ItemInterface.java
- ItemListing.java
- ItemListingInterface.java
- ItemManager.java
- Message.java
- MessageInterface.java
- RatingManager.java
- UserManager.java
- UserManagerInterface.java
- UserProfile.java
- Users.java

**Data Files**
The following files will be created automatically when the program runs:
- users.txt
- items.txt
- itemListing.txt
- messages.txt
- ratings.txt

### Compilation

Open a terminal or command prompt. Navigate to the folder containing all `.java` files.

Compile all Java files using:

javac *.java

### Start Server
Then run the server using:

java Server.java

The Server will output:

Server on 4242

### Running Client

Finally, run the client using:

java MainFrame.java



Detailed Description of Classes:
-------------------------------

# Functionality
## UserProfile 

**Functionality:** The `UserProfile` class stores and manages the user data, it includes the username, password, and email. 
This will allow the items that the users are buying or selling to be associated with their user data and thus can be 
indivdually storing, creating, and maintaining it on their profile. This will be written to a file, and will be easily retrieved.

**Testing:** The `UserProfileTest` will validate whether the program is able to write this information to a file and be retrieved, 
it runs through by storing mock user data and simulating the common operations of logging in and having a profile.

## Client

**Functionality:**  
The `Client` class manages the connection between the client-side application and the server.  
It opens a TCP socket, sends text commands to the server, and receives responses.  
It provides three main methods:
- `send(String s)`: Sends a single command string to the server.
- `read()`: Reads one line of response from the server.
- `readBlock()`: Reads multiple lines until receiving the sentinel string `"END"` from the server.

This class serves as the communication bridge between the user interface and the server's backend logic. It abstracts the socket details so the GUI and other classes can focus only on sending and receiving data.

**Testing:**  
Testing was done manually by connecting the client to a running server and verifying:
- Commands were correctly sent (REGISTER, LOGIN, GET_ITEMS, etc.).
- Responses were received properly.
- `readBlock()` stopped correctly when "END" was received.

Because this class deals with network I/O and depends on an active server connection, automated unit testing was not implemented. Instead, integration testing with the live server was used to verify correctness.

## Item

**Functionality**
- The Item class represents individual marketplace listings.
- It stores attributes such as name, description, price, category, seller, and image path.
- Provides getters and setters for all fields.
- Implements a saveToFile method to append the item's data into items.txt in CSV format.

**Testing**
- No direct test cases for the Item class.
- The functionality of Item is indirectly tested through ItemManagerTest.
- Methods like addItem, editItem, and deleteItem in ItemManager depend on correct behavior of Item class operations.

## ItemInterface

**Functionality**
- ItemInterface defines the core methods that any item in the marketplace must implement.
- Provides getter and setter signatures for name, description, price, category, and image path.
- Includes a saveToFile method for persistence operations.
- Ensures that classes like Item.java provide a consistent API for accessing and modifying item data.

**Testing**
- No direct test cases for ItemInterface.
- The interface is validated indirectly through ItemManagerTest.
- Classes that implement this interface (such as Item) are tested when the ItemManager operations are tested.

## ItemListing

**Functionality**
- Represents an item listed in the marketplace including seller, title, description, category, image path, price, and quantity.
- Provides getter and setter methods for all fields except seller (which is final).
- Supports conversion to a CSV line format using toFile method for saving item data.
- Provides fromFile method to create an ItemListing from a CSV line, enabling easy loading of saved items.

**Testing**
- No direct test cases for ItemListing.
- This class is indirectly tested through ItemManagerTest, which performs operations involving creating, editing, and retrieving item listings.

## ItemListingInterface

**Functionality**
- Defines the contract for managing ItemListing objects in the system.
- Specifies methods for:
   - Adding a new item.
   - Editing an existing item.
   - Deleting an item.
   - Retrieving all items.
   - Finding a specific item by seller and title.
- Helps ensure consistent implementation across any class managing item listings.

**Testing**
- No direct test cases for ItemListingInterface.
- Methods defined in this interface are tested indirectly through the ItemManagerTest, which verifies the behavior of the ItemManager class that implements this interface.

## ItemManager

**Functionality**
- Manages all marketplace items stored in the text file itemListing.txt.
- Maintains an in-memory list (cache) of ItemListing objects for fast access.
- Provides methods to:
   - Load items from the file at startup.
   - Save updated items back to the file.
   - Add new items.
   - Edit existing items.
   - Delete items.
   - Retrieve all items.
   - Find a specific item based on seller and title.

- Synchronization is used on all public methods to ensure thread safety, as multiple threads might access or modify the cache concurrently.

**Testing**
- No direct test cases for ItemManager itself.
- Behavior tested indirectly through ItemManagerTest.
- Since ItemManager implements the ItemListingInterface, the following methods are verified in the test:
   - addItem
   - editItem
   - deleteItem
   - getAll
   - find

## Message

**Functionality**
- Handles sending, storing, and retrieving chat messages between users.
- Saves all conversations to a plain text file (messages.txt).
- Each conversation is labeled with a unique key generated by sorting the two usernames.
- Supports appending new messages, loading message history, and listing all chat threads for a given user.
- Ensures thread safety using synchronized methods.

**Key Methods**
- send(from, to, content)
   - Appends a new message to the messages.txt file.
   - Automatically creates a conversation key if it does not already exist.
- loadHistory(user1, user2)
   - Retrieves all message lines exchanged between two users.
- listChats(user)
   - Returns all chat keys (user pairs) that include the given user.

**Testing**
- No automated test cases for this class.
- Manual testing included:
   - Sending messages between different users and verifying correct storage.
   - Loading previous chat histories to confirm data consistency.
   - Listing chat threads and checking correct key generation and retrieval.

## MessageInterface

**Functionality**
- Defines the essential methods for a messaging system used to send and retrieve chat messages between users.
- Establishes contracts for how messages are saved and accessed from persistent storage.

**Key Methods**
- send(from, to, content)
   - Sends a message from one user to another.
   - Ensures the message is saved in the appropriate chat thread.
- loadHistory(user1, user2)
   - Retrieves all past messages between two specified users.
   - Returns the messages in chronological order.
- listChats(user)
   - Lists all existing chat threads that involve the specified user.

**Testing**
- This is an interface and does not include testing directly.
- Methods are implemented and tested through the Message class.

## RatingManager

**Functionality**
- Manages the buyer-to-seller rating system for the marketplace.
- Sends rating data to the server and retrieves seller rating statistics.
- Helps display seller reputation information in the UI.

**Key Methods**
- addRating(buyer, seller, score)
   - Sends a command to the server to add a rating for the specified seller.
   - Returns true if the rating was successfully recorded.
- getAverage(seller)
   - Requests the average rating score for a given seller.
   - Returns -1 if the seller has not yet received any ratings.

**Testing**
- No dedicated unit tests.
- This class’s functionality is indirectly tested through usage in MarketPanel (when buyers rate sellers after purchases).

## Server

**Functionality**
- Listens for incoming client connections on port 4242.
- Creates a new thread for each client using the Handler inner class.
- Supports all main marketplace features:
   - User registration and login.
   - Balance retrieval and updates.
   - Adding, editing, and deleting item listings.
   - Browsing items for sale.
   - Buying items and updating inventory and balances.
   - Sending and receiving chat messages.
   - Recording and retrieving seller ratings.

**Key Methods**
- Handler.run()
   - Listens for incoming client commands and parses them.
   - Executes appropriate server-side logic based on the command.
   - Sends back results or data to the client.

**Supported Commands**
- REGISTER, LOGIN, BALANCE
- ADD_ITEM, EDIT_ITEM, DELETE_ITEM, GET_ITEMS
- BUY_ITEM
- SEND_MESSAGE, LIST_CHATS, LOAD_CHAT
- ADD_RATING, GET_RATING

**Testing**
- No direct unit tests.
- Tested manually through the client application (AppGUI) by performing full workflows:
   - Registering and logging in users.
   - Creating, editing, and deleting item listings.
   - Messaging between users.
   - Buying items and verifying balance and inventory updates.
   - Rating sellers and confirming average rating retrieval.

## UserManager

**Functionality**
- Manages user accounts stored in users.txt with fields: username, email, password, balance.
- Provides methods for user registration, login, account deletion, and balance management.
- Validates email format and checks for existing usernames or emails before registration.

**Key Methods**
- register()
   - Adds a new user with starting balance $100. Performs basic input validation.
- login()
   - Verifies username, email, and password against stored records.
- deleteAccount()
   - Removes a user account if credentials match.
- getBalance()
   - Retrieves the current balance of the specified user.
- setBalance()
   - Updates the balance for a given user and saves changes to file.
- userExists() / emailExists()
   - Checks whether a username or email is already registered.

**Testing**
- No direct unit test class.
- Functionality was tested manually via the GUI:
   - Registering new users with valid and invalid inputs.
   - Logging in with correct and incorrect credentials.
   - Attempting to register duplicate usernames/emails.
   - Buying items and confirming balance updates.

## UserManagerInterface

**Functionality**
- Defines the contract for user account management, backed by users.txt.
- Specifies required methods for registration, authentication, deletion, balance handling, and duplicate checks.

**Key Methods**
- register(username, email, password)
   - Registers a new user with validation rules.
- login(username, email, password)
   - Authenticates user credentials.
- deleteAccount(username, email, password)
   - Removes a user account if credentials match.
- getBalance(username)
   - Retrieves the balance for the specified user.
- setBalance(username, newBal)
   - Updates the user’s balance.
- userExists(username)
   - Checks if a username is taken.
- emailExists(email)
   - Checks if an email is already registered.

**Testing**
- No direct test cases for the interface.
- All methods are implemented and indirectly tested via UserManager and the full application workflow (login, register, buying items, etc.).

## Users

**Functionality**
- Interface defining user profile operations.
- Provides methods for managing user credentials and listing the items a user is selling.

**Key Methods**
- getUsername()
   - Returns the current username.
- setUsername(newUsername)
   - Updates the username in memory.
- getPassword()
   - Returns the current password.
- setPassword(newPassword)
   - Updates the password in memory.
- getEmail()
   - Returns the current email address.
- setEmail(newEmail)
   - Updates the email address in memory.
- saveToFile()
   - Appends the user profile to *users.txt* for persistence.
- checkPassword(password)
   - Validates whether the input password matches the stored password.
- getUserItems()
   - Returns a list of Item objects currently sold by this user (loaded from *items.txt*).

**Testing**
- No direct test cases.
- Implementing classes (e.g., *UserProfile*) may be indirectly tested through account creation, login, and item listing tests.

# GUI
## MarketPanel

**Functionality**
- Displays the main marketplace view for the logged-in user.
- Shows a toolbar for search keywords and category filtering.
- Displays the current user’s balance and a scrollable list of item listings.
- Sellers can create, edit, or delete their own listings.
- Buyers can select quantity, purchase items, or send messages to sellers.
- Allows direct access to user messaging via a "Messages" button.
- Dynamically refreshes the market item list and balance when actions occur.

**Key Methods**
- refreshBalance()
   - Sends BALANCE request to the server and updates the balance label.
- refreshMarket(keyword, category)
   - Retrieves all item listings from the server, filters by keyword and category, and populates the item list.
- createItemPanel(...)
   - Constructs a UI panel for each item, showing details and appropriate action buttons depending on whether the user is the seller or buyer.

**Testing**
- No automated test cases (GUI-based class).
- Manual testing included:
   - Creating, editing, and deleting item listings.
   - Searching and filtering items.
   - Buying items and verifying balance updates.
   - Sending and receiving messages through the chat system.

## CreateItemDialog

**Functionality:**  
The `CreateItemDialog` class provides a modal Swing form that allows a seller to enter all the required details for a new marketplace listing.  
Users can input the item title, description, price, category, quantity, and optionally select an image file to associate with the product.  
After entering the information and clicking the **Create** button, the data is validated and an `ADD_ITEM` command is sent to the server.  
If the server confirms success, the dialog closes and the item is added to the marketplace.

**Key UI Features:**
- Text fields for title, description, price, and quantity.
- A dropdown (`JComboBox`) for category selection.
- A **Browse** button that opens a file chooser to select an image.
- A **Create** button to submit the new item.

**Testing:**  
This class was tested manually by creating different item listings with varying values, including edge cases like missing fields or invalid price/quantity inputs.  
Since it involves GUI interactions and server communication, automated test cases were not used.  
Functionality was verified by confirming that items appeared correctly in the marketplace after creation and that appropriate error messages were shown for invalid inputs.

## EditItemDialog

**Functionality**
- The EditItemDialog class provides a modal form that allows a seller to modify the details of an existing item listing.
- When opened, it pre-fills the form fields with the current data of the selected item.
- After editing, the seller can save the changes, which sends an EDIT_ITEM command to the server.
- If the update is successful, the dialog closes; otherwise, an error message is displayed.

**Key UI Features**
- Text fields for editing description and price.
- A dropdown (JComboBox) for selecting a new category.
- A text field displaying the current image path with a Browse button for selecting a new image.
- A dropdown for selecting the updated quantity.
- A Save button to submit the changes.

**Testing**
- Manual testing was performed by editing various item listings with valid and invalid inputs to ensure correct behavior.
- Checked that invalid inputs such as non-numeric price or missing fields trigger appropriate warnings.
- Verified that successful edits immediately update the marketplace listing after refreshing.
- Automated test cases were not developed for this dialog due to its interactive GUI nature and reliance on live server responses.

## ChatPanel

**Functionality:** The `ChatPanel` is the centeral class for managing messaging data, sellers and buyers will be able to chat
with each other regarding the items. It provides methods to refresh the conversation, send new messages, and be able to exit
to go back to the market place.

**Testing:** The `ChatPanelTest` tests for basic messaging functions, if the user is able to message a certain user, sending this
data to a messaging file to store that data. Ensures that the user is able to go back to the marketplace to continue browsing. 

## ChatListDialog

**Functionality:**  
The ChatListDialog class provides a modal window that displays all chat conversations for the current user.
It queries the server using the "LIST_CHATS" command and retrieves chat keys that include the current user and other peers.
The dialog presents the peer usernames in a scrollable list. Double-clicking on a username opens the corresponding chat in a ChatPanel and closes the dialog.
This allows users to easily access ongoing conversations.

**Testing:**  
ChatListDialog was tested manually by verifying that all expected chat peers appear in the list after calling the server.
Tests included opening chats through double-clicking, checking that the dialog closes properly when a chat is opened, and ensuring that duplicate usernames do not appear.
Additionally, edge cases were tested where no chats exist or where only one conversation is available. No automated unit tests were created due to the graphical nature of the class.

## LoginPanel

**Functionality**
- Serves as the login and registration screen for the marketplace client.
- Allows users to input their username, email, and password.
- Provides two main actions:
   - **Login**
      - Sends a LOGIN command to the server with the provided credentials.
      - If successful, retrieves the user's balance and switches to the main marketplace screen.
   - **Register**
      - Sends a REGISTER command to the server with the provided information.
      - Displays a message indicating success or failure.

- Uses a combination of BorderLayout and GridLayout for clear and compact form layout.
- Utilizes JOptionPane for immediate user feedback on login/register success or failure.

**Testing**
- No automated test cases.
- Functionality verified through manual GUI testing:
   - Valid and invalid login attempts.
   - Valid and invalid registration attempts.
   - Server communication for LOGIN and REGISTER commands.

## MainFrame

**Functionality**
- Serves as the main application window for the marketplace client.
- Manages multiple screens using a **CardLayout**, allowing smooth switching between panels (login, marketplace, chat, etc.).
- Initializes with the **LoginPanel** and switches to **MarketPanel** after successful login.
- Stores current logged-in user information including:
   - Username
   - Email
   - Balance

- Provides methods to:
   - **addPanel(JPanel p, String name)**: Add a new panel/screen.
   - **showPanel(String name)**: Switch to a specified screen.
   - **loginSuccess(...)**: Called after login to create and display the market panel.
   - **updateBalance(newBal)**: Updates the cached balance and refreshes the market panel’s balance display.

**Testing**
- No automated test cases.
- Manual GUI testing performed to verify:
   - Successful login transitions to the marketplace.
   - New panels can be added and displayed correctly.
   - Balance updates reflect in the **MarketPanel** without errors.
