# Compose Multiplatform Media Player

Mysteriously, none of Chaintech's repos contains sources for their libraries,
instead only containing parts of the sources for the sample apps.
While the maven artifacts are published under MIT and the README also claims 
MIT, the repo contains the Apache license.

This project is a recreation of the sources for the library and sample app of
Chaintech's Multiplatform Media Player. The goal is to allow third parties to
build it from source or fork it easily.

## Original Readme

> # Compose Multiplatform Media Player
> [![Maven Central](https://img.shields.io/maven-central/v/network.chaintech/compose-multiplatform-media-player.svg)](https://central.sonatype.com/artifact/network.chaintech/compose-multiplatform-media-player)
> [![Kotlin](https://img.shields.io/badge/kotlin-v2.0.20-blue.svg?logo=kotlin)](http://kotlinlang.org)
> [![Compose Multiplatform](https://img.shields.io/badge/Compose%20Multiplatform-v1.7.0_beta02-blue)](https://github.com/JetBrains/compose-multiplatform)
> [![License](https://img.shields.io/github/license/Chaintech-Network/ComposeMultiplatformMediaPlayer)](http://www.apache.org/licenses/LICENSE-2.0)
> 
> ![badge-android](http://img.shields.io/badge/platform-android-3DDC84.svg?style=flat)
> ![badge-ios](http://img.shields.io/badge/platform-ios-FF375F.svg?style=flat)
> ![badge-desktop](http://img.shields.io/badge/platform-desktop-FF9500.svg?style=flat)
> 
> Compose Multiplatform Media Player is a powerful media player library designed for Compose Multiplatform projects. It enables seamless video playback, reels viewing, audio playing, YouTube video integration, and now HLS m3u8 support on iOS, Android, and Desktop platforms. The library offers extensive customization options for various controls, making it flexible for different types of media applications.
>
> ## 🎉 What's New in Version 1.0.22
> 🖥️ Desktop support added
> 
> ## ✨ Features
> **Cross-Platform Compatibility:** Works seamlessly on iOS, Android, and Desktop platforms within Compose Multiplatform projects.
> 
> **Video Playback:** Effortlessly play videos in your app with high performance and reliability.
> 
> **HLS m3u8 Playback:** Stream live and on-demand content using the HLS .m3u8 format.
> 
> **YouTube Playback:** Integrate YouTube videos directly into your app, with full control over playback and video state management.
> 
> **Reel Viewing:** Enjoy reel viewing with support for horizontal and vertical scrolling.
> 
> **Audio Playback:** Enjoy high-quality audio playback with customizable controls.
> 
> **Customizable Controls:** Enable/disable pause/resume functionality and adjust the appearance and visibility of the seek bar, along with various control icons and colors.
> 
> ## 📦 Installation
> 
> Add the following dependency to your `build.gradle.kts` file:
> 
> ```kotlin
> commonMain.dependencies {
>     implementation("network.chaintech:compose-multiplatform-media-player:1.0.22")
> }
> ```
> 💡 **Note:** For desktop video playback support, VLC player must be installed on your system.
> 
> ## 🚨 **Compatibility Notice** 🚨
> 
> If you are using **Kotlin version below 2.0.0** and **Jetpack Compose version below 1.6.11**, you may encounter compatibility issues with the current dependencies. ⚠️ To resolve these issues, please consider using an **older version** of the Compose Multiplatform Media Player library. 🎯
> ```kotlin
> commonMain.dependencies {
>     implementation("network.chaintech:compose-multiplatform-media-player:1.0.16")
> }
> ```
> 
> ## 🎬 Usage
> 
> ### 📹 Video Playback
> To play videos in your app, use the VideoPlayerView composable:
> ```kotlin
> VideoPlayerView(
>     modifier = Modifier.fillMaxSize(),
>     url = videoUrl
> )
> ```
> 💡 **Note:** The VideoPlayerView supports both online and local video playback. You can provide a URL for a remote video or a local file path.
> 
> ### ▶️ YouTube Playback
> To play youtube videos in your app, use the YouTubePlayerView composable:
> ```kotlin
> YouTubePlayerView(
>     modifier = Modifier.fillMaxSize(),
>     videoId = youtubeVideoId
> )
> ```
> 
> ### 🎥 Reel Viewing
> For reel viewing, utilize the ReelsPlayerView composable:
> ```kotlin
> ReelsPlayerView(
>     modifier = Modifier.fillMaxSize(),
>     urls = videoUrlArray
> )
> ```
> 
> ### 🎧 Audio Playback
> To play audio in your app, use the AudioPlayerView composable:
> ```kotlin
> AudioPlayerView(
>     modifier = Modifier,
>     audios = audioFilesArray
> )
> ```
> 💡 **Note:** The AudioPlayerView supports both online and local audio playback. You can provide a URL for a remote audio file or a local file path.
> 
> ## ⚙️ Customization
> You can customize various aspects of the media player:
> 
> * `modifier`: Modifies the layout and appearance of the video player and reel player.
> * `url`: The URL of the video to be played in the video player.
> * `videoId`: The YouTube video ID or URL to be played in the YouTube player.
> * `urls`: An array of URLs for the reel player, allowing playback of multiple reels.
> * `playerConfig`: You can configure various aspects of the video player appearance and behavior using the PlayerConfig data class.
> 
> | Property                                              | Description                                                                                                                     |
> |-------------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------|
> | isPauseResumeEnabled                                  | Enable or disable the pause/resume functionality.                                                                               |
> | isSeekBarVisible                                      | Toggle the visibility of the seek bar.                                                                                          |
> | isDurationVisible                                     | Control the display of the playback time duration.                                                                              |
> | seekBarThumbColor                                     | Customize the color of the seek bar thumb.                                                                                      |
> | seekBarActiveTrackColor                               | Customize the color of the seek bar’s active track, representing the portion of the media content that has already been played. |
> | seekBarInactiveTrackColor                             | Customize the color of the seek bar’s inactive track, representing the remaining portion of the media content yet to be played. |
> | durationTextColor                                     | Customize the color of the duration text displayed alongside the seek bar.                                                      |
> | durationTextStyle                                     | Customize the text style of the duration text, including font size and weight.                                                  |
> | seekBarBottomPadding                                  | Configure the bottom padding for the seek bar control, ensuring proper alignment within the UI layout.                          |
> | playIconResource & pauseIconResource                  | Customize the play and pause button icons.                                                                                      |
> | pauseResumeIconSize                                   | Customize the size of the pause/resume icons.                                                                                   |
> | reelVerticalScrolling                                 | Manage vertical and horizontal scrolling in reel viewing.                                                                       |
> | isAutoHideControlEnabled & controlHideIntervalSeconds | Enable the automatic hiding of controls after a specified time interval (in seconds).                                           |
> | isFastForwardBackwardEnabled                          | Enable or disable fast forward and backward controls.                                                                           |
> | fastForwardBackwardIconSize                           | Customize the size of the fast forward/backward icons.                                                                          |
> | fastForwardIconResource & fastBackwardIconResource    | Customize the icons for fast forward and fast backward controls.                                                                |
> | fastForwardBackwardIntervalSeconds                    | Set the interval (in seconds) for fast forward and backward actions.                                                            |
> | isMuteControlEnabled                                  | Enable or disable mute control functionality.                                                                                   |
> | unMuteIconResource & muteIconResource                 | Customize the icons for unmute and mute controls.                                                                               |
> | topControlSize                                        | Customize the size of the top control buttons.                                                                                  |
> | isSpeedControlEnabled                                 | Enable or disable speed control functionality.                                                                                  |
> | speedIconResource                                     | Customize the icon for speed control.                                                                                           |
> | isFullScreenEnabled                                   | Enable or disable full-screen functionality.                                                                                    |
> | controlTopPadding                                     | Configure the top padding for controls, ensuring proper alignment within the UI layout.                                         |
> | isScreenLockEnabled                                   | Enable or disable screen lock functionality.                                                                                    |
> | iconsTintColor                                        | Customize the tint color of the control icons.                                                                                  |
> | isMute                                                | Manage the mute/unmute state of the player.                                                                                     |
> | muteCallback                                          | Callback function triggered when the mute state changes.                                                                        |
> | isPause                                               | Manage the pause/resume state of the player.                                                                                    |
> | pauseCallback                                         | Callback function triggered when the pause/resume state changes.                                                                |
> | isScreenResizeEnabled                                 | Enable or disable screen resize (Fit/Fill) functionality.                                                                       |
> | bufferCallback                                        | Callback function triggered when Buffer state changes.                                                                          |                                                                      |
> | didEndVideo                                           | Callback function triggered when current video end.                                                                             |                                                                      |
> | loop                                                  | Enable or disable video playing in loop.                                                                                        |                                                                      |
> | showDesktopControls                                   | Hide/Show Desktop video player advance controls                                                                                 |
> | loadingIndicatorColor                                 | Customize the color of the loading indicator.                                                                                   |
> | loaderView                                            | Custom loader for showing loading state.                                                                                        |
> 
> * `audioPlayerConfig`: You can configure various aspects of the audio player appearance and behavior using the AudioPlayerConfig data class.
>   
> | Property                                       | Description                                                                                                                     |
> |------------------------------------------------|---------------------------------------------------------------------------------------------------------------------------------|
> | isControlsVisible                              | Toggle the visibility of the player controls.                                                                                   |
> | backgroundColor                                | Customize the background color of the audio player.                                                                             |
> | coverBackground                                | Customize the background color of the cover image.                                                                              |
> | seekBarThumbColor                              | Customize the color of the seek bar thumb.                                                                                      |
> | seekBarActiveTrackColor                        | Customize the color of the seek bar’s active track, representing the portion of the media content that has already been played. |
> | seekBarInactiveTrackColor                      | Customize the color of the seek bar’s inactive track, representing the remaining portion of the media content yet to be played. |
> | fontColor                                      | Customize the color of the text used in the player.                                                                             |
> | durationTextStyle                              | Customize the text style of the duration text, including font size and weight.                                                  |
> | titleTextStyle                                 | Customize the text style of the title text, including font size and weight.                                                     |
> | controlsBottomPadding                          | Configure the bottom padding for the controls, ensuring proper alignment within the UI layout.                                  |
> | playIconResource & pauseIconResource           | Customize the play and pause button icons.                                                                                      |
> | pauseResumeIconSize                            | Customize the size of the pause/resume icons.                                                                                   |
> | previousNextIconSize                           | Customize the size of the previous and next track icons.                                                                        |
> | previousIconResource & nextIconResource        | Customize the icons for the previous and next track controls.                                                                   |
> | iconsTintColor                                 | Customize the tint color of the control icons.                                                                                  |
> | loadingIndicatorColor                          | Customize the color of the loading indicator.                                                                                   |
> | shuffleOnIconResource & shuffleOffIconResource | Customize the icons for the shuffle control when enabled and disabled.                                                          |
> | advanceControlIconSize                         | Customize the size of the advance control icons (e.g., fast forward/backward).                                                  |
> | repeatOnIconResource & repeatOffIconResource   | Customize the icons for the repeat control when enabled and disabled.                                                           |
> 
> 
> ```kotlin
> VideoPlayerView(modifier = Modifier.fillMaxSize(),
>                 url = videoUrl,
>                 playerConfig = PlayerConfig(
>                     isPauseResumeEnabled = true,
>                     isSeekBarVisible = true,
>                     isDurationVisible = true,
>                     seekBarThumbColor = Color.Red,
>                     seekBarActiveTrackColor = Color.Red,
>                     seekBarInactiveTrackColor = Color.White,
>                     durationTextColor = Color.White,
>                     seekBarBottomPadding = 10.dp,
>                     pauseResumeIconSize = 40.dp,
>                     isAutoHideControlEnabled = true,
>                     controlHideIntervalSeconds = 5,
>                     isFastForwardBackwardEnabled = true,
>                     playIconResource = Res.drawable.icn_play,
>                     pauseIconResource = Res.drawable.icn_pause,
>                 )
>             )
> ```
> 
> ```kotlin
> YouTubePlayerView(modifier = Modifier.fillMaxSize(),
>                 videoId = "QFxN2oDKk0E",
>                 playerConfig = PlayerConfig(
>                     isPauseResumeEnabled = true,
>                     isSeekBarVisible = true,
>                     isDurationVisible = true,
>                     seekBarThumbColor = Color.Red,
>                     seekBarActiveTrackColor = Color.Red,
>                     seekBarInactiveTrackColor = Color.White,
>                     durationTextColor = Color.White,
>                     seekBarBottomPadding = 10.dp,
>                     pauseResumeIconSize = 40.dp,
>                     isAutoHideControlEnabled = true,
>                     controlHideIntervalSeconds = 5,
>                     isFastForwardBackwardEnabled = true,
>                     playIconResource = Res.drawable.icn_play,
>                     pauseIconResource = Res.drawable.icn_pause,
>                 )
>             )
> ```
> 
> 
> ```kotlin
> ReelsPlayerView(modifier = Modifier.fillMaxSize(),
>         urls = videoUrlArray,
>         playerConfig = PlayerConfig(
>             isPauseResumeEnabled = true,
>             isSeekBarVisible = false,
>             isDurationVisible = false,
>             isMuteControlEnabled = false,
>             isSpeedControlEnabled = false,
>             isFullScreenEnabled = false,
>             isScreenLockEnabled = false,
>             reelVerticalScrolling = true
>         )
>     )
> ```
> 
> ```kotlin
> val audioFilesArray = listOf(
>         AudioFile(
>             audioUrl = "https://codeskulptor-demos.commondatastorage.googleapis.com/GalaxyInvaders/theme_01.mp3",
>             audioTitle = "Galaxy Invaders",
>             thumbnailUrl = "https://c.saavncdn.com/866/On-My-Way-English-2019-20190308195918-500x500.jpg"
>         ),
>         AudioFile(
>             audioUrl = "https://codeskulptor-demos.commondatastorage.googleapis.com/pang/paza-moduless.mp3",
>             audioTitle = "Paza Moduless"
>         )
>     )
> 
> AudioPlayerView(
>         modifier = Modifier,
>         audios = audioFilesArray,
>         audioPlayerConfig = AudioPlayerConfig(
>             isControlsVisible = true,
>             fontColor = Color.White,
>             iconsTintColor = Color.White
>         )
>     )
> ```
> 
> ## 📀 Format Support
> |  Format  | Android  |   iOS    | Desktop  |
> |:--------:|:--------:|:--------:|:--------:|
> |   MP4    |    ✅     |    ✅     |    ✅     |
> |   MOV    |    ✅     |    ✅     |    ✅     |
> |   3GP    |    ✅     |    ✅     |    ✅     |
> |   AVI    |    ✅     |    ❌     |    ✅     |
> |   MKV    |    ✅     |    ❌     |    ✅     |
> |   WEBM   |    ✅     |    ❌     |    ✅     |
> |   MTS    |    ✅     |    ❌     |    ✅     |
> |   m3u8   |    ✅     |    ✅     |    ✅     |
> |   MP3    |    ✅     |    ✅     |    ✅     |
> |   FLAC   |    ✅     |    ✅     |    ✅     |
> |   WAV    |    ✅     |    ✅     |    ✅     |
> |   AAC    |    ✅     |    ❌     |    ✅     |
> |   AIF    |    ❌     |    ✅     |    ✅     |
> |   ALAC   |    ✅     |    ❌     |    ✅     |
> |   OGG    |    ✅     |    ❌     |    ✅     |
> | YouTube  |    ✅     |    ✅     |    ❌     |
> 
> ## 📖 Detailed Explanation
> For an in-depth guide and detailed explanation, check out our comprehensive Medium Blog Post.
> 
> [![Medium](https://img.shields.io/badge/Medium-12100E?style=for-the-badge&logo=medium&logoColor=white)](https://medium.com/mobile-innovation-network/introducing-compose-multiplatform-media-player-your-go-to-solution-for-seamless-media-playback-691df3cc4da9)  
> [![LinkedIn](https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/showcase/mobile-innovation-network)
> 
> ## 🛤️ Roadmap
> We're committed to continuously improving and expanding the capabilities of our media player library. Here's a glimpse into our future plans:
> 
> ### 🌟 Upcoming Features
> - Picture-in-Picture (PiP) Mode
> - YouTube support for Desktop
> 
> ## 📄 License
> ```
> Copyright 2023 Mobile Innovation Network
> 
> Licensed under the Apache License, Version 2.0 (the "License");
> you may not use this file except in compliance with the License.
> You may obtain a copy of the License at
> 
>    http://www.apache.org/licenses/LICENSE-2.0
> 
> Unless required by applicable law or agreed to in writing, software
> distributed under the License is distributed on an "AS IS" BASIS,
> WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
> See the License for the specific language governing permissions and
> limitations under the License.
> ```
