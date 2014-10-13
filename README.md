<a href="http://stcode09.github.io/safepass/" >SafePass - Password Manager</a>
========

SafePass is a password manager for Android devices that lets users securely store their login information, credit card details and notes. The application utilizes password based encryption (AES-256) to store user data. The user initially sets up a master password which grants entry into the application. The master password is never stored, and all the encryption is done locally on the device.

<h2> Install </h2>
<ul>
<li>Requires Android ICS 4.1.2 and above (API 16+) </li>
<li> Dowload the .apk file </li>
<li> Go to Settings > Security > Unkown sources (make sure it is enabled) </li>
<li> Click on the downloaded .apk file to install </li>
</ul>

<h2>Main Features</h2>
<ul>
<li>Securely store login information (passwords), credit card information and notes. </li>
<li>Generate new and secure passwords with a customizable password generator. </li>
<li>Supports backup of stored  encrypted data. </li>
<li>Share backup file with a variety of applications like Dropbox, Google Drive, etc. </li>
<li>Import saved encrypted backups. </li>
<li>Customizable auto-lock feature that locks the app after a certain amount of time. </li>
<li>Set the number of login attempts allowed. </li>
<li>Clear clipboard option. </li>
<li>Contains no advertisements. </li>
<li>Quick copy of passwords, credit card details, and notes. </li>
<li>Reset master password. </li>
</ul>
 
<h2>Security Features</h2>
<ul>
<li>The application encrypts all user data with AES-256 bit encryption. </li>
<li>The key to encrypt the data is derived from the Master Password using a Password Based Key Derivation Function (PBKDF2 with Hmac SHA1). The key is salted with a randomly generated 256-bit salt using Java's SecureRandom class. The encryption is performed with 1024 iterations to encrypt the data. This makes brute force attacks more difficult. </li>
<li>The Master Password is never stored. Instead, it transformed into a 512-bit salted hash (using PBKDF2 with Hmac SHA1). The result is then hashed 1024 times. The salt helps to protect from offline dictionary attacks and the 1024 iterations makes brute force attacks more difficult. </li>
<li>The app auto-locks to prevent a security breach (customizable). </li>
<li>Clears the clipboard after exiting the application (customizable). </li>
<li>Limited number of login attempts to prevent a security breach (customizable). </li>
<li>No internet access permission used. Only permissions used are to read and write to external storage (to support import and export of backups). </li>
</ul>
