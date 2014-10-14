<a href="http://stcode09.github.io/safepass/" >SafePass - Password Manager</a>
========

SafePass is a password manager for Android devices that lets users securely store their login information, credit card details and notes. The application utilizes password based encryption (AES-256) to store user data. The user initially sets up a master password which grants entry into the application. The master password is never stored, and all the encryption is done locally on the device.

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
<li>The application encrypts all user data with AES-256 bit encryption. </li>
<li>The key to encrypt the data is derived from the Master Password using a Password Based Key Derivation Function (PBKDF2 with Hmac SHA1). The key is salted with a randomly generated 256-bit salt using Java's SecureRandom class. The encryption is performed with 1024 iterations to encrypt the data. This makes brute force attacks more difficult. </li>
<li>The Master Password is never stored. Instead, it is transformed into a 512-bit salted hash (using PBKDF2 with Hmac SHA1). The result is then hashed 1024 times. The salt helps to protect from offline dictionary attacks and the iterations make brute force attacks more difficult. </li>
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
To clear the clipboard, tap on the expand icon on the task-bar. A drop-down menu will appear. Then, tap on "Clear Clipboard". The application can also clear the clipboard automatically when it exits. To enable this, go to the Settings menu and make sure "Clear Clipboard" is enabled.
</li>
<li name="q9"><b>How do I customize the number of login attempts?</b><br>
To customize the number of login attempts, go to the Settings menu and tap on "Login Attempts". Select the desired number of attemps from the pop-up dialog.
</li>
</ol>
