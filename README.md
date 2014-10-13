SafePass - Password Manager
========

SafePass is a password manager for Android devices that lets users securely store their login information, credit card details and notes. The application utilizes password based encryption (AES-256) to store user data. The user intially sets up a master password which grants entry into the application. The master password is never stored, and all the encryption is done locally on the device.

<h2>Main Features</h2>
1) Securely store login information (passwords), credit card information and notes. <br>
2) Genereate new and secure passwords with a customizable password generator. <br>
3) Supports backup of stored  encrypted data. <br>
4) Share backup file with a variety of applications like Dropbox, Google Drive, etc. <br>
5) Import saved encrypted backups. <br>
6) Customizable auto-lock feature that locks the app after a certain amount of time. <br>
7) Set the number of login attempts allowed. <br>
8) Clear clipboard option. <br>
9) Contains no advertisments. <br>
10) Quick copy of passwords, credit card details, and notes. <br> 
11) Reset master password. <br>
 
<h2>Security Features</h2>
• The application encrypts all user data with AES-256 bit encryption. <br>
• The key to encrypt the data is derived from the Master Password using a Password Based Key Derivation Function (PBKDF2 with Hmac SHA1). The key is salted with a randomly generated 256-bit salt using Java's SecureRandom class. The encryption is performed with 1024 iterations to encrypt the data. This makes brute force attacks more difficult. <br>
• The master password is never stored. Instead, it transformed into a 512-bit hash, and then salted (using PBKDF2 with Hmac SHA1). The result is then hashed 1024 times. The salt helps to protect from offline dictionary attacks and the 1024 iterations makes brute force attacks more difficult. <br>
• The app auto-locks to prevent a security breach (customizable). <br>
• Clears the cliipboard after exiting the application (customizable). <br>
• Limited number of login attempts to prevent a security breach (customizable). <br>
• No internet access permission used. Only permissions used are to read and write to external storage (to suppor import and export of backups). <br>

<h2> FAQ </h2>
<b>1) How do I navigate within the application?</b> <br>
A: Simply swipe left/right to navigate within the application <br>

<b>2) How do I add a new entry (login, wallet, note)?</b><br>
A: Navigate to the respective window and click on the "+" icon <br>

<b>3) How do I view a selected entry?</b><br>
A: Simply tap on the deired entry<br>

<b>4) How do I delete a selected entry?</b><br>
A: Long-click on the desired entry (you can select multiple entries) and click on the delete icon <br>

<b>5) How do I edit a selected entry?</b><br>
A: Click on the edit icon of the particular entry <br>

<b>6) How do I copy saved data?</b><br>
A: Click on the copy icon of the particular entry. This copies the primary field of the enrty. For access to advanced copy menu, long-click on the copy icon and select the desired field to copy.<br>

<b>7) How do I generate a new password?</b><br>
A: Click on the generate password icon located next to the password field. This generates a random password. To change password generator settings, long-click on the icon and select the deired settings. The password generator is set to generate "very strong" passwords by default. <br>

<h3> Install </h3>
To install SafePass - Beta, download the .apk file, (make sure install from unkown sources is enabled) and click on install.
