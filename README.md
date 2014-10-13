SafePass - Password Manager
========

SafePass is a password manager for Android devices that lets users securely store their login information, credit card details and notes. The application utilizes password based encryption (AES-256) to store user data. The user initially sets up a master password which grants entry into the application. The master password is never stored, and all the encryption is done locally on the device.

<h2>Main Features</h2>
1) Securely store login information (passwords), credit card information and notes. <br>
2) Generate new and secure passwords with a customizable password generator. <br>
3) Supports backup of stored  encrypted data. <br>
4) Share backup file with a variety of applications like Dropbox, Google Drive, etc. <br>
5) Import saved encrypted backups. <br>
6) Customizable auto-lock feature that locks the app after a certain amount of time. <br>
7) Set the number of login attempts allowed. <br>
8) Clear clipboard option. <br>
9) Contains no advertisements. <br>
10) Quick copy of passwords, credit card details, and notes. <br> 
11) Reset master password. <br>
 
<h2>Security Features</h2>
• The application encrypts all user data with AES-256 bit encryption. <br>
• The key to encrypt the data is derived from the Master Password using a Password Based Key Derivation Function (PBKDF2 with Hmac SHA1). The key is salted with a randomly generated 256-bit salt using Java's SecureRandom class. The encryption is performed with 1024 iterations to encrypt the data. This makes brute force attacks more difficult. <br>
• The Master Password is never stored. Instead, it transformed into a 512-bit salted hash (using PBKDF2 with Hmac SHA1). The result is then hashed 1024 times. The salt helps to protect from offline dictionary attacks and the 1024 iterations makes brute force attacks more difficult. <br>
• The app auto-locks to prevent a security breach (customizable). <br>
• Clears the clipboard after exiting the application (customizable). <br>
• Limited number of login attempts to prevent a security breach (customizable). <br>
• No internet access permission used. Only permissions used are to read and write to external storage (to support import and export of backups). <br>

<h2> Install </h2>
• Requirements - Android ICS 4.1.2 and above (API 16+) <br>
• To install SafePass, download the .apk file, (make sure install from unknown sources is enabled) and click on install. <br>
