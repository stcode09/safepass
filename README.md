<a href="http://stcode09.github.io/safepass/" >SafePass - Password Manager</a>
========

SafePass is a password manager for Android devices that lets users securely store their login information, credit card details and notes. The application utilizes password based encryption (AES-256) to store user data. The user initially sets up a master password which grants entry into the application. The master password is never stored, and encryption is done locally on the device.

<h2> Install </h2>
<ul>
<li>Requires Android ICS 4.1.2 and above (API 16+) </li>
<li> <a href="https://github.com/stcode09/safepass/blob/master/SafePass.apk?raw=true">Download</a> the .apk file </li>
<li> Go to Settings -> Security -> Unknown sources (make sure it is enabled) </li>
<li> Click on the downloaded .apk file to install </li>
</ul>

<h2>Main Features</h2>
<ul>
<li>Securely store login information (passwords), credit card information and notes. </li>
<li>Generate new and secure passwords with a customizable password generator. </li>
<li>Password strength indicator. Also indicates if the password entered is common or easily guessable. </li>
<li>Backup encrypted data to a backup file. </li>
<li>Share backup file with a variety of applications like Dropbox, Google Drive, Gmail, etc. </li>
<li>Import encrypted backup file. </li>
<li>Customizable auto-lock feature that locks the app after a certain amount of time. </li>
<li>Customize the number of login attempts. </li>
<li>Quick copy of passwords, credit card details, and notes. </li>
<li>Clear clipboard option. </li>
<li>Reset master password. </li>
<li>Contains no advertisements. </li>
</ul>
 
<h2>Security Features</h2>
<ul>
<li>The application encrypts all user data with <a href="http://en.wikipedia.org/wiki/Advanced_Encryption_Standard">AES-256 bit encryption</a>. </li>
<li>The key to encrypt the data is derived from the Master Password using a <a href="http://en.wikipedia.org/wiki/PBKDF2">Password Based Key Derivation Function</a> (PBKDF2 with Hmac SHA1). The key is salted with a randomly generated 256-bit salt using Java's SecureRandom class. The encryption is performed with 1024 iterations to encrypt the data. This makes brute force attacks more difficult. </li>
<li>The Master Password is never stored. Instead, it is transformed into a one-way 512-bit salted hash (using PBKDF2 with Hmac SHA1). The result is then hashed 1024 times. The salt helps to protect from offline dictionary attacks and the repetitive hashing makes brute force attacks more difficult. </li>
<li>The app auto-locks to prevent a security breach (customizable). </li>
<li>Clears the clipboard after exiting the application (customizable). </li>
<li>Limits the number of login attempts to prevent a security breach (customizable). </li>
<li>No internet access permission used. Only permissions used are to read and write to external storage (to support import and export of backups). </li>
</ul>

<h2> Screenshots </h2>

<img src="https://raw.githubusercontent.com/stcode09/safepass/master/screens/Screenshot_2014-10-12-22-16-31.png" width="25%" height="25%"/>
<img src="https://raw.githubusercontent.com/stcode09/safepass/master/screens/Screenshot_2014-10-12-22-17-50.png" width="25%" height="25%"/>
<img src="https://raw.githubusercontent.com/stcode09/safepass/master/screens/Screenshot_2014-10-12-22-21-23.png" width="25%" height="25%"/>
<img src="https://raw.githubusercontent.com/stcode09/safepass/master/screens/Screenshot_2014-10-12-22-19-26.png" width="25%" height="25%"/>
<img src="https://raw.githubusercontent.com/stcode09/safepass/master/screens/Screenshot_2014-10-12-22-21-38.png" width="25%" height="25%"/>
<img src="https://raw.githubusercontent.com/stcode09/safepass/master/screens/Screenshot_2014-10-12-22-20-52.png" width="25%" height="25%"/>
<img src="https://raw.githubusercontent.com/stcode09/safepass/master/screens/Screenshot_2014-10-12-22-21-54.png" width="25%" height="25%"/>
<img src="https://raw.githubusercontent.com/stcode09/safepass/master/screens/Screenshot_2014-10-12-22-22-16.png" width="25%" height="25%"/>
<img src="https://raw.githubusercontent.com/stcode09/safepass/master/screens/Screenshot_2014-10-12-22-22-53.png" width="25%" height="25%"/>
<img src="https://raw.githubusercontent.com/stcode09/safepass/master/screens/Screenshot_2014-10-12-22-23-19.png" width="25%" height="25%"/>
<img src="https://raw.githubusercontent.com/stcode09/safepass/master/screens/Screenshot_2014-10-12-22-42-59.png" width="25%" height="25%"/>
<img src="https://raw.githubusercontent.com/stcode09/safepass/master/screens/Screenshot_2014-10-12-22-42-06.png" width="25%" height="25%"/>

<h2>Frequently Asked Questions (FAQ)</h2>
<ol>
<li><a href="#q1">How do I navigate within the application?</a></li>
<li><a href="#q2">How do I add a login, wallet or note entry?</a></li>
<li><a href="#q3">How do I delete a login, wallet or note entry?</a></li>
<li><a href="#q4">How do I copy data from a login, wallet or note entry?</a></li>
<li><a href="#q5">How do I edit data of a login, wallet or note entry?</a></li>
<li><a href="#q6">How do I generate a new password?</a></li>
<li><a href="#q7">How do I change the password generator settings?</a></li>
<li><a href="#q8">How do I clear the clipboard?</a></li>
<li><a href="#q9">How do I customize the number of login attempts?</a></li>
<li><a href="#q10">How do I customize the auto-lock timeout?</a></li>
<li><a href="#q11">How do I change/reset the Master Password?</a></li>
<li><a href="#q12">How do I backup my data?</a></li>
<li><a href="#q13">How do I import from a backup file?</a></li>
<li><a href="#q14">How do I share my backup file?</a></li>
<li><a href="#q15">Where is my backup file located?</a></li>
<li><a href="#q16">Why do I get the message "No backup folder found. Cannot export" when trying to export?</a></li>
<li><a href="#q17">Why do I get the message "No backup file found" when trying to import?</a></li>
<li><a href="#q18">Why don't I see any data when importing from a backup?</a></li>
<li><a href="#q19">Where is my Master Password stored?</a></li>
<li><a href="#q20">I forgot my Master Password. How can I recover it?</a></li>
<li><a href="#q21">What encryption scheme is used, and how secure is my data?</a></li>
</ol>

<ol>
<li name="q1"><b>How do I navigate within the application?</b><br>
To navigate within the application, simply swipe left or right.
</li>
<li name="q2"><b>How do I add a login, wallet or note entry?</b><br>
To add an entry, navigate to the particular tab and tap on the add icon (located on the upper right hand corner).
</li>
<li name="q3"><b>How do I delete a login, wallet or note entry?</b><br>
To delete an entry, navigate to the particular tab and long click on the desired entry. A delete icon will appear on the upper right hand corner of the screen. You can select multiple entries and then tap the delete icon.
</li>
<li name="q4"><b>How do I copy data from a login, wallet or note entry?</b><br>
To quickly copy data from an entry, tap on the clipboard icon of the desired entry. To access the advanced copy menu, long-click on the clipboard icon of the desired entry. 
</li>
<li name="q5"><b>How do I edit data of a login, wallet or note entry?</b><br>
To edit an entry, tap on the edit icon located on the right hand corner of the desired entry.
</li>
<li name="q6"><b>How do I generate a new password?</b><br>
To generate a new password, tap on the generate icon located to the right of the password field. To access the quick settings for the password generator, long-click on the generate icon and select the desired settings. These settings inherit from the main password generator options located in the settings menu.
</li>
<li name="q7"><b>How do I change the password generator settings?</b><br>
There are two ways to change the password generator settings. One option is to go to the Settings menu and selecting the desired options under "Password Generator" (these settings are applied to all passwords). Another way is to long-click on the generate password icon located to the right of the password field (these settings apply only to the current password, and inherit from the main settings). 
</li>
<li name="q8"><b>How do I clear the clipboard?</b><br>
To clear the clipboard, tap on the expand icon on the taskbar. A drop-down menu will appear. Then, tap on "Clear Clipboard". The application can also clear the clipboard automatically when it exits. To enable this, go to the Settings menu and make sure "Clear Clipboard" is enabled.
</li>
<li name="q9"><b>How do I customize the number of login attempts?</b><br>
To customize the number of login attempts, go to the Settings menu and tap on "Login Attempts".
</li>
<li name="q10"><b>How do I customize the auto-lock timeout?</b><br>
To customize the number auto-lock timeout, go to the Settings menu and tap on "Auto-lock Timeout".
</li>
<li name="q11"><b>How do I change/reset the Master Password?</b><br>
To change/reset the Master Password, tap on the expand icon on the taskbar. A drop-down menu will appear. Then, tap on "Reset Password".
</li>
<li name="q12"><b>How do I backup my data?</b><br>
To backup your data, tap on the expand icon on the taskbar. A drop-down menu will appear. Then, tap on "Export Backup". This will write an encrypted backup file to your primary storage destination (either internal storage, or SD Card).
</li>
<li name="q13"><b>How do I import from a backup file?</b><br>
To import your data, tap on the expand icon on the taskbar. A drop-down menu will appear. Then, tap on "Import Backup". Next, enter the password associated with the backup file and tap "Import".
</li>
<li name="q14"><b>How do I share my backup file?</b><br>
To share your backup file, click on "Export Backup". After a successful export, a "Share File" dialog box will appear. Select the desired application from the dialog box.
</li>
<li name="q15"><b>Where is my backup file located?</b><br>
To view the location of your backup file, go to the Settings menu and navigate to the "About" section. You will see your backup file location under "Backup Location".
</li>
<li name="q16"><b>Why do I get the message "No backup folder found. Cannot export." when trying to export?</b><br>
This message appears when the application cannot find your primary storage destination. It may be caused because your primary storage destination is either full, or temporarily un-mounted. 
</li>
<li name="q17"><b>Why do I get the message "No backup file found" when trying to import?</b><br>
This message appears when the application cannot find your a backup file in the primary storage destination. To import from a backup, place the backup file named "backup.crypt" under the "SafePass" folder in your primary storage destination. If no such folder exists, then please create one manually. (A file browser will be added in the next release). 
</li>
<li name="q18"><b>Why don't I see any data when importing from a backup?</b><br>
This could occur because the password you entered while importing may be incorrect. Please ensure that the password entered when importing from a backup is the one associated with that backup file. 
</li>
<li name="q19"><b>Where is my Master Password stored?</b><br>
Your master password is never stored. Only you know your master password. 
</li>
<li name="q20"><b>I forgot my Master Password. How can I recover it?</b><br>
Sorry, your Master Password cannot be recovered at this time. This is to ensure data security, so be sure to remember your Master Password. (Future versions will include a recovery option). 
</li>
<li name="q21"><b>What encryption scheme is used, and how secure is my data?</b><br>
Your data is ecnrypted using <a href="http://en.wikipedia.org/wiki/Advanced_Encryption_Standard">AES-256 bit encryption</a> (Advanced Encryption Standard). This encryption standard is adopted by the U.S. government and is used worldwide. The key to encrypt the data is derived from the Master Password using a <a href="http://en.wikipedia.org/wiki/PBKDF2">Password Based Key Derivation Function</a> (PBKDF2 with Hmac SHA1). The key is salted with a randomly generated 256-bit salt using Java's SecureRandom class. The encryption is performed with 1024 iterations to encrypt the data. This makes brute force attacks more difficult.
</li>
</ol>


