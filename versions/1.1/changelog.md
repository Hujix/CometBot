## CometBot - v1.1

Mostly a bug fix and quality of life update

### General Changes
* Slightly improved stability, mostly in relation to playing tracks.
* Improved performance and startup latency of playback
* Updated HyperDiscord, JDA, and lavaplayer dependencies

### User experience
* Added message to inform user when the queue is empty.
* Made Stop messages more informative.

### Discord
* Removed some unnecessary requests to discords server
* Reduced number of requests sent for the dynamic "now playing" message

### Bug fixes
* Fixed a small state issue that would only come up if no songs where played with the bot.
* Some newer youtube videos where unable to be played. This has been fixed with the lavaplayer dependency upgrade.

### Also note
* The logo .ase (aseprite file) and png are now avaliable on github