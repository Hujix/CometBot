## CometBot - v2.0

Here it is, the first major update since the 1.0 update. I'm honestly surprised this bot has made it this far. The first commit was on December 30th 2019.

A lot has changed in this update and I hope you enjoy it. Thank you for using cometbot.
-codedcosmos

### Major changes
* Added dynamic volume mode, reducing volume when people are speaking
* Implemented queue and single track looping via the `.loop` command
* Greatly improved the information sent to users for the now playing messages

### New commands
* `.about` shows basic details about cometbot
* `.loop` cycles single track loops, queue loops and no loops
* `.changelog` shows the changes for the current version
* `.autovolume` toggles the dynamic volume
* `.runtimestatus` Shows details about the jvm/runtime

### Performance changes
* Increased responsiveness of pausing via message reactions

### Quality of life
* Reduced the amount of spam when playing tracks
* Simplified now playing
* Improved play/pause messages
* Made it clearer when an error occured

### Existing command changes
* Improved `.queue` command to include play next songs and to start the queue list from 1 instead of 0
* Added more responses to the `.purpose` command
* Made the usage parts in  `.help` clearer
* Fixed `.lasterror` to not spam the chat unnecessarily

### Developer changes
* Improved code quality
* Improved folder structure for project
* Added unit tests which should improve stability in the future
* Improved build file
* Updated HyperDiscord
* Updated JDA 4.1.1_164