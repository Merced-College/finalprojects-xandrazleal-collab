[![Open in Codespaces](https://classroom.github.com/assets/launch-codespace-2972f46106e565e64193e422d61a12cf1da4916b45550586e14ef0a7c637dd04.svg)](https://classroom.github.com/open-in-codespaces?assignment_repo_id=23604597)
# cpsc39-finalProjects

Soundboard

Xandra Leal

Simple Soundboard program to play a variety of user added fx

To run the program download the Soundboard file in main that contains Soundboard.exe and run it in the downloaded file.

This program has the ability to play sound, add mp3 files, loop sound, play multiple audio tracks,
and dynamically search for audio

This program mainly uses Array Lists to create multiple instances of fxItem.java. this class contains all the set and getters for
the variables necessary to retrieve paths, names, and location. The Buttons updates using Hashmaps, A hashmap instance is created for each button and added the table. Strings are used to store path files
and the names of each audio file.

This program contains a quick sort sorting Algorithm, and a binary search searching Algorithm. This program consists of many small algorithms that handle a
specific task. The more notable ones are startLayeredPlayer, which handles all stacking audio by creating new instances of audioPlayer
and keeping track of which one is currently playing. The volumeSlider algorithm handles the main Volume controls, globally
updating volume. The updateGrid function handles the grid display, adding buttons and reading hashmaps.
