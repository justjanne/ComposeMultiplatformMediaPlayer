package chaintech.videoplayer.youtube

internal val htmlContent = """
<!DOCTYPE html>
<html>
<style type="text/css">
        html, body {
            height: 100%;
            width: 100%;
            margin: 0;
            padding: 0;
            background-color: #000000;
            overflow: hidden;
            position: fixed;
            pointer-events: none;
        }
    </style>

  <head>
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <script defer src="https://www.youtube.com/iframe_api"></script>
  </head>

  <body>
    <div id="youTubePlayer"></div>
  </body>

<script type="text/javascript">
var UNSTARTED = "UNSTARTED";
var ENDED = "ENDED";
var PLAYING = "PLAYING";
var PAUSED = "PAUSED";
var BUFFERING = "BUFFERING";
var CUED = "CUED";

var player;
var timerId;

function onYouTubeIframeAPIReady() {

  document.title = 'ytplayer://onYouTubeIframeAPIReady';

    player = new YT.Player('youTubePlayer', {

    height: '100%',
    width: '100%',
    
    events: {
        onReady: function(event) {
             document.title = 'ytplayer://onReady';
             hidePlayerControls();
        },
        onStateChange: function(event) {
            sendPlayerStateChange(event.data);
        },
        onError: function(error) {
            document.title = 'ytplayer://onError?data='+event.data;
        }
      },
      
      playerVars: {
                autoplay: 1, 
                controls: 0,            
                cc_load_policy: 0,      
                fs: 1,                  
                iv_load_policy: 0,      
                rel: 0,                 
                enablejsapi: 1,         
                mute: 0,                
                playsinline: 1,         
                origin: "https://www.youtube.com", 
                showinfo: 0,           
                modestbranding: 1      
            }
  });
}

function hidePlayerControls() {
    var playerFrame = document.getElementById('youTubePlayer');
    var playerFrameDocument = playerFrame.contentDocument || playerFrame.contentWindow.document;

    var hideControlsCSS = `
        .ytp-chrome-top, .ytp-chrome-bottom, .ytp-show-cards-title, .ytp-ce-element, .ytp-title, .ytp-gradient-top, .ytp-gradient-bottom {
            display: none !important;
        }
        .ytp-title-link {
            display: none !important;
        }
    `;

    var styleElement = document.createElement('style');
    styleElement.type = 'text/css';
    styleElement.appendChild(document.createTextNode(hideControlsCSS));

    playerFrameDocument.head.appendChild(styleElement);
}

function sendPlayerStateChange(playerState) {
  clearTimeout(timerId);

  switch (playerState) {
    case YT.PlayerState.UNSTARTED:
      sendStateChange(UNSTARTED);
      return;

    case YT.PlayerState.ENDED:
      sendStateChange(ENDED);
      return;

    case YT.PlayerState.PLAYING:
      sendStateChange(PLAYING);

      startSendCurrentTimeInterval();
      sendVideoData(player);
      return;

    case YT.PlayerState.PAUSED:
      sendStateChange(PAUSED);
      return;

    case YT.PlayerState.BUFFERING:
      sendStateChange(BUFFERING);
      return;

    case YT.PlayerState.CUED:
      sendStateChange(CUED);
      return;
  }

  function sendVideoData(player) {
    var videoDuration = player.getDuration();
    document.title = 'ytplayer://onVideoDuration?data='+videoDuration;
  }

  function sendStateChange(newState) {
    document.title = 'ytplayer://onStateChange?data='+newState;
  }

  function startSendCurrentTimeInterval() {
    timerId = setInterval(function() {
        document.title = 'ytplayer://onCurrentTimeChange?data='+player.getCurrentTime();
    }, 100 );
  }
}

function seekTo(startSeconds) {
  player.seekTo(startSeconds, true);
  document.title = 'ytplayer://onCurrentTimeChange?data='+startSeconds;
}

function seekBy(startSeconds) {
  seekTo(player.getCurrentTime()+startSeconds);
}

function pauseVideo() {
  player.pauseVideo();
}

function playVideo() {
  player.playVideo();
}

function loadVideo(videoId, startSeconds) {
  player.loadVideoById(videoId, startSeconds);
  setTimeout(function() {
        document.title = 'ytplayer://onVideoId?data=' + videoId;
    }, 500); 
}

function mute() {
  player.mute();
}

function unMute() {
  player.unMute();
}

function setVolume(volumePercent) {
  player.setVolume(volumePercent);
}

function setPlaybackRate(playbackRate) {
  player.setPlaybackRate(playbackRate);
}

  </script>
</html>
        """
    .trimIndent()