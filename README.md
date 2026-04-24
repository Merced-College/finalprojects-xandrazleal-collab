[![Open in Codespaces](https://classroom.github.com/assets/launch-codespace-2972f46106e565e64193e422d61a12cf1da4916b45550586e14ef0a7c637dd04.svg)](https://classroom.github.com/open-in-codespaces?assignment_repo_id=23604597)
# cpsc39-finalProjects

Soundboard

Xandra Leal

Simple Soundboard program to play a variety of user added fx

To run the program:

On the right site of Github find Releases and Download SoundBoard 1.0.zip

Extract the 7zip file somewhere and doubleclick the Soundbaord.exe

This program has the ability to play sound, add mp3 files, loop sound, play multiple audio tracks,
and dynamically search for audio

This program mainly uses Array Lists to create multiple instances of fxItem.java. this class contains all the set and getters for
the variables necessary to retrieve paths, names, and location. The volume Sliders are tracked using Hashmaps, so when the user changes
the volume of a sub Volume bar it doesn't have to loop through a list. And uses an Instance of Arrays to grab all the files out of the 
AudioFolder

This program contains a quick sort sorting Algorithm, and a binary search searching Algorithm. This program consists of many small algorithms that handle a
specific task. The more notable ones are startLayeredPlayer, which handles all stacking audio by creating new instances of audioPlayer
and keeping track of which one is currently playing. The volumeSlider algorithm handles the main Volume controls, globally
updating volume. The updateGrid function handles the grid display, adding buttons
 