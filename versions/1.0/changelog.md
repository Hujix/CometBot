## CometBot - v1.0

Finally the release of this fun little music bot.

### General Changes
* Improved stability, performance and code quality of audio playback.
* Code rebase on HyperDiscord

### Playback
* Added Stop, Pause and skip to player embed
* Somewhat improved latency when requesting long songs (Greater than 1 hour in length)
* Improved load time for large queues (800 or more songs)

### New commands
* Greatly improved `.Help` command 
* Added more answers to the `.Purpose` command 


* Added command `.GoodBot` -- Let the bot know it's doing good
* Added command `.HelpClassic` -- The old help command
* Added command `.Last` -- Rexecutes the last command
* Added command `.LastError` -- Relays the last error
* Added command `.PlayAgain` -- Adds the current song back into the queue
* Added comamnd `.Queue` -- Displays all the songs in the queue
* Added command `.Search` -- Currently deactivated


* Added extra command aliases

### User experience
* More useful message sent when calling `.ClearQueue` when the queue was already empty
* Improved the help message for the `.Pause` command

### Fun
* Randomised some messages
* Added a couple extra easter eggs

### Bug fixes
* Fixed many state issues
* Stop didn't always clear the queue
* Now Playing Embed would continue to send updates even when song wasn't actually playing
* Bot would duplicate leave messages if asked to leave with `.Leave`
* Playback would not play next song in queue in some cases