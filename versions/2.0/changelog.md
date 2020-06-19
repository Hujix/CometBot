## CometBot - v2.0

Here it is, the first major update since the 1.0 update. I'm honestly surprised this bot has made it this far. The first commit was on December 30th 2019.

A lot has changed in this update and I hope you enjoy it. Thank you for using cometbot.
-codedcosmos

### Major changes
* Implemented queue and single track looping via the `.loop` command
* Greatly improved the information sent to users for the now playing messages
* State like shuffle and looping is now saved on a per server basis
* Greatly improved recommendations for mistyped commands
* Cometbot now records some interesting statistics, see `.stats` for more details

### Auto volume
* ~~Added dynamic volume mode, reducing volume when people are speaking~~
* This feature was working early on in development but due discords latest update, this will have to be reworked and is currently disabled.

### New commands
* `.about` shows basic details about cometbot
* `.autovolume` toggles the dynamic volume
* `.changelog` shows the changes for the current version
* `.currentsettings` Shows the current values for each setting
* `.loop` cycles single track loops, queue loops and no loops
* `.runtimestatus` Shows details about the jvm/runtime
* `.playtop` plays the top n most liked songs
* `.save` Saves settings and current state
* `.stats` Shows the server specific stats
* `.globalstats` Shows global/total stats for all servers

### Performance changes
* Increased responsiveness of pausing via message reactions

### Quality of life
* Reduced the amount of spam when playing tracks
* Simplified now playing
* Improved play/pause messages
* Made it clearer when an error occured
* Added output messages for `.seek`, `.forward` and `.rewind`

### Existing command changes
* Improved `.queue` command to include play next songs and to start the queue list from 1 instead of 0
* Added more responses to the `.purpose` command
* Made the usage parts in `.help` clearer

### Developer changes
* Improved code quality
* Added more comments as well
* Improved folder structure for project
* Removed testing logging that should have been removed earlier and added some new useful ones
* Added unit tests which should improve stability in the future
* Improved build file
* Updated HyperDiscord
* Updated JDA to 4.1.1_165
* Updated Lavaplayer to 1.3.50

### Bug fixes
* Fixed some invalid argument bugs with `.eq` command
* Fixed a bug where live tracks would immediately skip and play attempt to play two tracks at once
* Fixed `.lasterror` to not spam the chat unnecessarily
* Fixed a lot of bugs related with out of date lavaplayer dependencies. The songs still won't be able to play if it's out of date, but it at least won't fail so spectacularly now.