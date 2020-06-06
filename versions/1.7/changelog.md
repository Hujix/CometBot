## CometBot - v1.7

Song control update

### Commands
* Added `.seek` Command, which seeks to a specific point in song
* Added `.forward` Command which adds a couple seconds to where the track is playing
* Added `.rewind` Command which removes a couple seconds to where the track is playing

### General changes
* Refactored audio package location
* Renamed package containing now playing message
* Added opus
* Updated JDA to 4.1.1_142

### User experience
* Removed redundent stop messages when the bot leaves etc
* Displays when nowplaying track is livestream
